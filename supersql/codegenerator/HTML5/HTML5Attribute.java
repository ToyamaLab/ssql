package supersql.codegenerator.HTML5;

import java.io.File;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class HTML5Attribute extends Attribute {
	
	private HTML5Env html5Env;
	private HTML5Env html5Env2;
	
	public HTML5Attribute(Manager manager, HTML5Env hEnv, HTML5Env hEnv2) {
		super();
		this.html5Env = hEnv;
		this.html5Env2 = hEnv2;
	}
	
	@Override
	public String work(ExtList data_info) {
		
		// css情報書き込み
		html5Env.append_css_def_att(HTML5Env.getClassID(this), this.decos);
		
		// HTMLコード書き込み
		if (html5Env.tableFlag) {
			html5Env.code.append("<td class=\"");
			html5Env.code.append(HTML5Env.getClassID(this));
			html5Env.code.append(" att\">");
		} else {
			html5Env.code.append("<div class=\"");
			html5Env.code.append(HTML5Env.getClassID(this));
			html5Env.code.append(" att\">");
		}
		
		// C3, G3の場合 <a>
		if (html5Env.linkFlag > 0) {
			//TODO
			String fileDir = new File(html5Env.linkUrl).getAbsoluteFile().getParent();
			if (fileDir.length() < html5Env.linkUrl.length() && fileDir.equals(html5Env.linkUrl.substring(0, fileDir.length()))) {
				String relative_path = html5Env.linkUrl.substring(fileDir.length() + 1);
				html5Env.code.append("<a href=\"" + relative_path + "\">");
				Log.out("<a href=\"" + relative_path + "\">");
			} else {
				html5Env.code.append("<a href=\"" + html5Env.linkUrl + "\">");
				Log.out("<a href=\"" + html5Env.linkUrl + "\">");
			}
		}
		
		Log.out("data_info = " + data_info);
		String data = this.getStr(data_info);
		Log.out("data = " + data);
		data = data.replace("\\r\\n", "<br>");
		data = data.replace("\\r", "<br>");
		data = data.replace("\\n", "<br>");
		Log.out("replace data = " + data);
		// html5Env.code.append(this.getStr(data_info));
		html5Env.code.append(data);
		
		// C3, G3の場合 </a>
		if (html5Env.linkFlag > 0) {
			//TODO
			html5Env.code.append("</a>");
			Log.out("</a>");
		}
		
		if (html5Env.tableFlag) {
			html5Env.code.append("</td>\n");
		} else {
			html5Env.code.append("</div>\n");
		}
		
		return null;
	}
	
}
