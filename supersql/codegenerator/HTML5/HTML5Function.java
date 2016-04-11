package supersql.codegenerator.HTML5;

import supersql.codegenerator.FuncArg;
import supersql.codegenerator.Function;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class HTML5Function extends Function {
	
	private HTML5Env html5Env;
	private HTML5Env html5Env2;
	
	public HTML5Function() {
		
	}
	
	public HTML5Function(Manager manager, HTML5Env hEnv, HTML5Env hEnv2) {
		super();
		this.html5Env = hEnv;
		this.html5Env2 = hEnv2;
	}
	
	@Override
	public String work(ExtList data_info) {
		Log.out("--- start Function ---");
		
		// connector カウント初期化
		this.setDataList(data_info);
		
		// cssの情報を取得
		html5Env.append_css_def_att(HTML5Env.getClassID(this), this.decos);
		
		// 関数名取得
		String FuncName = this.getFuncName();
		
		// anchor関数の発動
		if (FuncName.equalsIgnoreCase("anchor") || FuncName.equalsIgnoreCase("a")) {
			Func_url();
		}
		
		// footer関数の発動
		if (FuncName.equalsIgnoreCase("footer")) {
			Func_footer();
		}
		
		// header関数の発動
		if (FuncName.equalsIgnoreCase("header")) {
			Func_header();
		}
		
		// image関数の発動
		if (FuncName.equalsIgnoreCase("image")) {
			Func_image();
		}
		
		// line関数の発動
		if (FuncName.equalsIgnoreCase("line")) {
			Func_line();
		}
		
		// text関数の発動
		if (FuncName.equalsIgnoreCase("text")) {
			Func_text();
		}
		
		Log.out("--- end Function ---");
		return null;
	}
	
	// footer関数
	protected void Func_footer() {
		// 引数の取得
		FuncArg arg1 = (FuncArg) this.Args.get(0);
		String title = arg1.getStr();
		html5Env.code.append("<div class=\"");
		html5Env.code.append(HTML5Env.getClassID(this));
		html5Env.code.append(" footer\">");
		html5Env.code.append(title);
		html5Env.code.append("</div>\n");
		
		html5Env.footerFlag = true;
	}
	
	// header関数
	protected void Func_header() {
		// 引数の取得
		FuncArg arg1 = (FuncArg) this.Args.get(0);
		String title = arg1.getStr();
		html5Env.code.append("<div class=\"");
		html5Env.code.append(HTML5Env.getClassID(this));
		html5Env.code.append(" header\">");
		html5Env.code.append(title);
		html5Env.code.append("</div>\n");
		
		html5Env.headerFlag = true;
	}
	
	// image関数
	protected void Func_image() {
		// 引数の取得
		FuncArg arg1 = (FuncArg) this.Args.get(0);
		
		// image(path)
		String path = arg1.getStr();
		// HTMLに書き込み
		if (html5Env.tableFlag) {
			html5Env.code.append("<td>\n");
		}
		html5Env.code.append("<img class=\"");
		html5Env.code.append(HTML5Env.getClassID(this));
		html5Env.code.append(" att\" src=\"" + path + "\">\n");
		if (html5Env.tableFlag) {
			html5Env.code.append("</td>\n");
		}
	}
	
	// line関数
	protected void Func_line() {
		try {
			FuncArg arg1 = (FuncArg) this.Args.get(0);
			try { // 引数2つ(色と太さ)
				FuncArg arg2 = (FuncArg) this.Args.get(1);
				String color = arg1.getStr();
				String size = arg2.getStr();
				html5Env.code.append("<hr width=\"100%\" color=\"");
				html5Env.code.append(color);
				html5Env.code.append("\" size=\"");
				html5Env.code.append(size);
				html5Env.code.append("\">\n");
			} catch (Exception e) { // 引数1つ(色のみ)
				String color = arg1.getStr();
				html5Env.code.append("<hr width=\"100%\" color=\"");
				html5Env.code.append(color);
				html5Env.code.append("\">\n");
			}
		} catch (Exception e) { // 引数なし(デフォルト)
			html5Env.code.append("<hr width=\"100%\" color=\"#000000\" size=\"10\">");
		}
	}
	
	// text関数
	protected void Func_text() {
		// 引数の取得
		try {
			
		} catch (Exception e) { // 引数なし(デフォルト) 
			html5Env.code.append("<input type=\"text\">\n");
		}
	}
	
	// anchor関数 and a関数
	protected void Func_url() {
		// 引数の取得
		FuncArg arg1 = (FuncArg) this.Args.get(0);
		FuncArg arg2 = (FuncArg) this.Args.get(1);
		
		try {// 引数が3つ
			FuncArg arg3 = (FuncArg) this.Args.get(2);
			
			// anchor(path, url, "image")
			String path = arg1.getStr();
			String url = arg2.getStr();
			// HTMLに書き込み
			if (html5Env.tableFlag) {
				html5Env.code.append("<td class=\"");
				html5Env.code.append(HTML5Env.getClassID(this));
				html5Env.code.append(" att\">");
			} else {
				html5Env.code.append("<div class=\"");
				html5Env.code.append(HTML5Env.getClassID(this));
				html5Env.code.append(" att\">");
			}
			html5Env.code.append("<a href=\"" + url + "\">");
			html5Env.code.append("<img src=\"" + path + "\"></a>");
			if (html5Env.tableFlag) {
				html5Env.code.append("</td>\n");
			} else {
				html5Env.code.append("</div>\n");
			}
		} catch (Exception e) { // 引数が2つ
			// anchor(name, url)
			String name = arg1.getStr();
			String url = arg2.getStr();
			// HTMLに書き込み
			if (html5Env.tableFlag) {
				html5Env.code.append("<td class=\"");
				html5Env.code.append(HTML5Env.getClassID(this));
				html5Env.code.append(" att\">");
			} else {
				html5Env.code.append("<div class=\"");
				html5Env.code.append(HTML5Env.getClassID(this));
				html5Env.code.append(" att\">");
			}
			html5Env.code.append("<a href=\"" + url + "\">" + name + "</a>");
			if (html5Env.tableFlag) {
				html5Env.code.append("</td>\n");
			} else {
				html5Env.code.append("</div>\n");
			}
		}
	}
}
