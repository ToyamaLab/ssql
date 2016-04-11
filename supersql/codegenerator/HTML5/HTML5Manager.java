package supersql.codegenerator.HTML5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import supersql.codegenerator.CodeGenerator;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Jscss;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.dataconstructor.DataConstructor;
import supersql.extendclass.ExtList;

public class HTML5Manager extends Manager {

	private HTML5Env html5Env;
	private HTML5Env html5Env2;
	
	public HTML5Manager(HTML5Env hEnv, HTML5Env hEnv2) {
		this.html5Env = hEnv;
		this.html5Env2 = hEnv2;
	}
	
	private void connectOutdir(String outdir, String outfile) {
		String fileDir = new File(html5Env.outFile).getAbsoluteFile().getParent();
		if (fileDir.length() < html5Env.outFile.length() && fileDir.equals(html5Env.outFile.substring(0, fileDir.length()))) {
			html5Env.outFile = html5Env.outFile.substring(fileDir.length() + 1);
		}
		
		String tmpqueryfile = new String();
		if (html5Env.outFile.indexOf("/") > 0) {
			if (outfile != null) {
				if (html5Env.outFile.startsWith(".") || html5Env.outFile.startsWith("/")) {
					tmpqueryfile = html5Env.outFile.substring(html5Env.outFile.indexOf("/") + 1);
				}
			} else {
				tmpqueryfile = html5Env.outFile.substring(html5Env.outFile.lastIndexOf("/") + 1);
			}
		} else {
			tmpqueryfile = html5Env.outFile;
		}
		html5Env.outFile = outdir.concat(tmpqueryfile);
	}

	@Override
	public void finish() {
	}
	
	@Override
	public void generateCode(ITFE tfe_info, ExtList data_info) {
		
		// 引数の宣言(ないとNullPointerException)
		html5Env.code = new StringBuffer();
		html5Env.css = new StringBuffer();
		html5Env.header = new StringBuffer();
		html5Env.footer = new StringBuffer();
		html5Env.writtenClassId = new Vector();
		html5Env.notWrittenClassId = new Vector();
		
		Log.out("[HTML5Manager:generateCode]");

		// ファイル名、出力先の設定
		getOutfilename();
		
		// 出力ファイル名の決定
		html5Env.fileName = html5Env.outFile + ".html";
		html5Env2.fileName = html5Env.outFile + ".xml";
		
		// 順にコードの生成
		tfe_info.work(data_info);
		
		// <html><head> ~ </head><body>まで記述
		html5Env.getHeader();
		// </body></html>まで記述
		html5Env.getFooter();
		
		// ファイルの生成
		try {
			if (CodeGenerator.getMedia().equalsIgnoreCase("html5")) {
				if (!GlobalEnv.isOpt()) {
					PrintWriter pw;
					pw = new PrintWriter(new BufferedWriter(new FileWriter(html5Env.fileName)));
					
					pw.println(html5Env.header);
					pw.println(html5Env.code);
					pw.println(html5Env.footer);
					pw.close();
				}
			}
			// CSSファイル生成
			Jscss.process();
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException e){
			System.err.println("Error[HTML5Manager]: File IO Error in HTML5Manager");
			GlobalEnv.addErr("Error[HTML5Manager]: File IO Error in HTML5Manager");
		}
	}
	
	private String getOutfile(String outfile) {
		String out = new String();
		if (outfile.indexOf(".html") > 0) {
			out = outfile.substring(0, outfile.indexOf(".html"));
		} else {
			out = outfile;
		}
		return out;
	}
	
	protected void getOutfilename() { // ファイル名、出力先の設定
		String file = GlobalEnv.getfilename();
		String outdir = GlobalEnv.getoutdirectory();
		String outfile = GlobalEnv.getoutfilename();
		
		html5Env.outDir = outdir;
		
		if (outfile == null) {
			if (file.toLowerCase().indexOf(".sql") > 0) {
				html5Env.outFile = file.substring(0, file.toLowerCase().indexOf(".sql"));
			} else if (file.toLowerCase().indexOf(".ssql") > 0) {
				html5Env.outFile = file.substring(0, file.toLowerCase().indexOf(".ssql"));
			}
		} else {
			html5Env.outFile = getOutfile(outfile);
		}
		
		if (html5Env.outFile.indexOf("/") > 0) {
			html5Env.linkOutFile = html5Env.outFile.substring(html5Env.outFile.lastIndexOf("/") + 1);
		} else {
			html5Env.linkOutFile = html5Env.outFile;
		}
		
		if (outdir != null) {
			connectOutdir(outdir, outfile);
		}
	}
	
}
