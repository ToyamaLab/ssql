package supersql.codegenerator.HTML5;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class HTML5C2 extends Connector{
	private HTML5Env html5Env;
	private HTML5Env html5Env2;
	
	public HTML5C2(Manager manager, HTML5Env hEnv, HTML5Env hEnv2) {
		this.html5Env = hEnv;
		this.html5Env2 = hEnv2;
	}
	
	@Override
	public String work(ExtList data_info) {
		Log.out("------- C2 -------");
		Log.out("tfes.contain_itemnum = " + tfes.contain_itemnum());
		Log.out("tfes.size = " + tfes.size());
		Log.out("countconnectitem = " + countconnectitem());
		
		// connector カウント初期化
		this.setDataList(data_info);
		
		// 親要素がtable状態なのかcheck
		boolean table_event = false;
		if (html5Env.tableFlag) {
			table_event = true;
		}
		
		// cssの情報を取得
		html5Env.append_css_def_att(HTML5Env.getClassID(this), this.decos);
		
		// htmlコード書き込み
		if (!GlobalEnv.isOpt()) {
			if (table_event) {
				html5Env.code.append("<td>\n");
			} else {
				html5Env.code.append("<div class=\"");
				html5Env.code.append(HTML5Env.getClassID(this));
				html5Env.code.append(" col\">\n");
			}
			if (html5Env.tableFlag) { // table
				html5Env.code.append("<table class=\"");
				html5Env.code.append(HTML5Env.getClassID(this));
				html5Env.code.append(" col\">\n");
			}
		}
		
		// 子要素に書き込み
		int i = 0;
		while (this.hasMoreItems()) {
			ITFE tfe = tfes.get(i);
			String classid = HTML5Env.getClassID(tfe);
			
			if (html5Env.tableFlag) {
				html5Env.code.append("<tr>\n");
			}
			
			this.worknextItem();
			
			if (html5Env.tableFlag) {
				html5Env.code.append("</tr>\n");
			}
			
			i++;
		}
		
		if (html5Env.tableFlag) {
			html5Env.code.append("</table>\n");
			if (!table_event) {
				html5Env.tableFlag = false;
				Log.out("********table end********");
			}
		}
		
		if (table_event) {
			html5Env.code.append("</td>\n");
		} else {
			html5Env.code.append("</div>\n");
		}
		
		Log.out("+++++++ C2 +++++++");
		return null;
	}
}
