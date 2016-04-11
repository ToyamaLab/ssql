/*
 * Created on 2004/07/25
 */
package supersql.codegenerator.HTML;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Jscss;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.common.Utils;
import supersql.extendclass.ExtList;

public class HTMLG3 extends Grouper {

	private String backfile = new String();
	private int countinstance = 0;

	private HTMLEnv html_env;

	private HTMLEnv html_env2;

	// ���󥹥ȥ饯��
	public HTMLG3(Manager manager, HTMLEnv henv, HTMLEnv henv2) {
		this.html_env = henv;
		this.html_env2 = henv2;
	}

	private void setLinkButton() {
		String nextfile = new String();
		nextfile = html_env.linkOutFile
				+ String.valueOf(html_env.countFile + 1) + ".html";
		html_env.code.append("<DIV class=\"linkbutton "
				+ HTMLEnv.getClassID(tfe) + "\">\n");
		if (countinstance > 1) {
			html_env.code.append("<A href=\"" + backfile + "\">");
			html_env.code.append("[back]");
			html_env.code.append("</A>\n");
		}
		if (this.hasMoreItems()) {
			html_env.code.append("<A href=\"" + nextfile + "\">");
			html_env.code.append("[next]");
			html_env.code.append("</A>\n");
		}
		html_env.code.append("</div>\n");

		// html_env.addLinkButtonCSS();

	}

	@Override
	public String getSymbol() {
		return "HTMLG3";
	}

	// G3��work�᥽�å�
	@Override
	public String work(ExtList data_info) {
		String parentfile = html_env.fileName;
		String parentnextbackfile = html_env.nextBackFile;
		StringBuffer parentcode = html_env.code;
		StringBuffer parentcss = html_env.css;
		StringBuffer parentheader = html_env.header;
		StringBuffer parentfooter = html_env.footer;
		String parentfile2 = html_env2.fileName;
		String parentnextbackfile2 = html_env2.nextBackFile;
		StringBuffer parentcode2 = html_env2.code;
		StringBuffer parentcss2 = html_env2.css;
		StringBuffer parentheader2 = html_env2.header;
		StringBuffer parentfooter2 = html_env2.footer;
		Log.out("------- G3 -------");

		html_env.css = new StringBuffer();
		html_env.header = new StringBuffer();
		html_env.footer = new StringBuffer();
		html_env2.css = new StringBuffer();
		html_env2.header = new StringBuffer();
		html_env2.footer = new StringBuffer();
		this.setDataList(data_info);
		while (this.hasMoreItems()) {
			html_env.gLevel++;

			html_env.code = new StringBuffer();
			html_env2.code = new StringBuffer();

			/*
			 * ����Foreach func�Ǥʤ��?鐃緒申鐃緒申鐃縮わ申G3
			 */
			if (!html_env.foreachFlag) {
				backfile = html_env.nextBackFile;
				html_env.countFile++;
				countinstance++;
				html_env.fileName = html_env.outFile
						+ String.valueOf(html_env.countFile) + ".html";
				html_env.nextBackFile = html_env.linkOutFile
						+ String.valueOf(html_env.countFile) + ".html";
			}

			html_env.setOutlineMode();
			this.worknextItem();

			if (!html_env.foreachFlag) {
				setLinkButton();
			}
			html_env.gLevel--;
			html_env.getHeader();
			html_env.getFooter();
			html_env2.header.append("<?xml version=\"1.0\" encoding=\""
					+ Utils.getEncode() + "\"?><SSQL>");
			html_env2.footer.append("</SSQL>");

			try {
				// changed by goto 20120715_2 start
				// This is for 'link/foreach'.
				// PrintWriter pw2 = new PrintWriter(new BufferedWriter(new
				// FileWriter(
				// html_env.fileName)));
				PrintWriter pw;
				if (html_env.charset != null) {
					pw = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(
									html_env.fileName), html_env.charset)));
					// Log.info("File encoding: "+html_env.charset);
				} else
					pw = new PrintWriter(new BufferedWriter(new FileWriter(
							html_env.fileName)));
				// Log.info("File encoding: "+((html_env.charset!=null)?
				// html_env.charset : "UTF-8"));
				// changed by goto 20120715_2 end
				pw.println(html_env.header);
				pw.println(html_env.code);
				pw.println(html_env.footer);
				pw.close();
				if (GlobalEnv.isOpt()) {
					html_env2.fileName = html_env.fileName.substring(0,
							html_env.fileName.lastIndexOf(".html")) + ".xml";
					// changed by goto 20120715_2 start
					// PrintWriter pw2 = new PrintWriter(new BufferedWriter(new
					// FileWriter(
					// html_env2.filename)));
					PrintWriter pw2;
					if (html_env.charset != null) {
						pw2 = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(
										html_env2.fileName), html_env.charset)));
						// Log.info("File encoding: "+html_env.charset);
					} else
						pw2 = new PrintWriter(new BufferedWriter(
								new FileWriter(html_env2.fileName)));
					// Log.info("File encoding: "+((html_env.charset!=null)?
					// html_env.charset : "UTF-8"));
					// changed by goto 20120715_2 end

					pw2.println(html_env2.header);
					pw2.println(html_env2.code);
					pw2.println(html_env2.footer);
					pw2.close();
					HTMLoptimizer xml = new HTMLoptimizer();
					String xml_str = xml.generateHtml(html_env2.fileName);
					pw = new PrintWriter(new BufferedWriter(new FileWriter(
							html_env.fileName)));
					pw.println(html_env.header);
					pw.println(xml_str);
					StringBuffer footer = new StringBuffer(
							"</div></body></html>");
					pw.println(footer);
					pw.close();
				}
				html_env.header = new StringBuffer();
				Jscss.process();	//masato 20141231
				html_env.footer = new StringBuffer();
				html_env2.header = new StringBuffer();
				html_env2.footer = new StringBuffer();
			} catch (FileNotFoundException fe) {
//				System.err.println("Error: specified outdirectory \""
//						+ html_env.outDir + "\" is not found");
				Log.err("Error: specified outdirectory \""
						+ html_env.outDir + "\" is not found");
//				GlobalEnv.errorText += "Error: specified outdirectory \""
//						+ html_env.outDir + "\" is not found";
				GlobalEnv.addErr("Error: specified outdirectory \""
						+ html_env.outDir + "\" is not found");
				// comment out by chie
				// System.exit(-1);
			} catch (IOException e) {
				Log.err("Error[HTMLG3]: File IO Error in HTMLG3");
//				GlobalEnv.errorText += "Error[HTMLG3]: File IO Error in HTMLG3";
				e.printStackTrace();
				GlobalEnv.addErr("Error[HTMLG3]: File IO Error in HTMLG3");
				// comment out by chie
				// System.exit(-1);
			}
		}

		html_env.fileName = parentfile;
		html_env.code = parentcode;
		html_env.css = parentcss;
		html_env.header = parentheader;
		html_env.footer = parentfooter;
		html_env.nextBackFile = parentnextbackfile;
		html_env2.fileName = parentfile2;
		html_env2.code = parentcode2;
		html_env2.css = parentcss2;
		html_env2.header = parentheader2;
		html_env2.footer = parentfooter2;
		html_env2.nextBackFile = parentnextbackfile2;

		Log.out("TFEId = " + HTMLEnv.getClassID(this));
		html_env.append_css_def_td(HTMLEnv.getClassID(this), this.decos);
		return null;

	}

}
