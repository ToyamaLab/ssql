package supersql.codegenerator.HTML5;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import supersql.codegenerator.Connector;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class HTML5C3 extends Connector{
	
	private HTML5Env html5Env;
	private HTML5Env html5Env2;
	
	public HTML5C3(Manager manager, HTML5Env henv, HTML5Env henv2) {
		this.html5Env = henv;
		this.html5Env2 = henv2;
	}
	
	@Override
	public String work(ExtList data_info) {
		
		Log.out("------- C3 -------");
		
		// connector カウント初期化
		this.setDataList(data_info);
		
		// cssの情報を取得 (多分いらない？)
		html5Env.append_css_def_att(HTML5Env.getClassID(this), this.decos);
		
		// <a>タグをattributeに含ませる処理
		int c3Items = tfeItems;
		html5Env.countFile++;
		html5Env.linkUrl = html5Env.linkOutFile + "_" + String.valueOf(html5Env.countFile) + ".html";
		html5Env.linkFlag++;
		this.worknextItem();
		html5Env.linkFlag--;
		
		// 今までの情報格納
		String parentfile = html5Env.fileName;
		String parentfile2 = html5Env2.fileName;
		
		// <a>タグ先のhtmlファイルを出力する処理
		for (int k = 1; k < c3Items; k++) {
			StringBuffer parentcode = html5Env.code;
			StringBuffer parentheader = html5Env.header;
			StringBuffer parentfooter = html5Env.footer;
			html5Env.code = new StringBuffer();
			html5Env.header = new StringBuffer();
			html5Env.footer = new StringBuffer();
			
			html5Env.fileName = html5Env.outFile + "_" + String.valueOf(html5Env.countFile) + ".html";
			
			// さらにリンク先がある場合
			if (k < c3Items - 1) {
				html5Env.countFile++;
				html5Env.linkUrl = html5Env.linkOutFile + "_" + String.valueOf(html5Env.countFile) + ".html";
				html5Env.linkFlag++;
			}
			
			this.worknextItem();
			
			if (k < c3Items - 1) {
				html5Env.linkFlag--;
			}
			
			// ヘッダーフッター呼び出し
			html5Env.getHeader();
			html5Env.getFooter();
			
			// ファイル出力
			try {
				PrintWriter pw;
				pw = new PrintWriter(new BufferedWriter(new FileWriter(html5Env.fileName)));
				pw.println(html5Env.header);
				pw.println(html5Env.code);
				pw.println(html5Env.footer);
				pw.close();
			} catch (FileNotFoundException fe) {
				fe.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			html5Env.code = parentcode;
			html5Env.header = parentheader;
			html5Env.footer = parentfooter;
		}
		
		// ファイル名を元に戻す
		html5Env.fileName = parentfile;
		html5Env2.fileName = parentfile2;
		
		Log.out("+++++++ C3 +++++++");
		
		return null;
	}
	
}
