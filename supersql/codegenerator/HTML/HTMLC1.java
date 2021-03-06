package supersql.codegenerator.HTML;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;

//tk

public class HTMLC1 extends Connector {

	private HTMLEnv htmlEnv;
	private HTMLEnv htmlEnv2;

	// ���󥹥ȥ饯��
	public HTMLC1(Manager manager, HTMLEnv henv, HTMLEnv henv2) {
		this.htmlEnv = henv;
		this.htmlEnv2 = henv2;
	}

	@Override
	public String getSymbol() {
		return "HTMLC1";
	}

	// C1��work�᥽�å�
	@Override
	public String work(ExtList data_info) {

		Log.out("------- C1 -------");
		Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
		Log.out("tfes.size=" + tfes.size());
		Log.out("countconnetitem=" + countconnectitem());
		this.setDataList(data_info);

		if (decos.containsKey("form")) {
			htmlEnv2.code
					.append("<form" + HTMLEnv.getFormNumber() + "start />");
			if (decos.getStr("form").toLowerCase().equals("search"))
				HTMLEnv.setSearch(true);
		}

		if (decos.containsKey("insert")) {
			HTMLEnv.setIDU("insert");
		}
		if (decos.containsKey("update")) {
			HTMLEnv.setIDU("update");
		}
		if (decos.containsKey("delete")) {
			HTMLEnv.setIDU("delete");
		}

		// tk
		// start///////////////////////////////////////////////////////////////////////
		htmlEnv.append_css_def_td(HTMLEnv.getClassID(this), this.decos);

		if (!GlobalEnv.isOpt()) {
			htmlEnv.code
					.append("<TABLE cellSpacing=\"0\" cellPadding=\"0\" border=\"");
			htmlEnv.code.append(htmlEnv.tableBorder + "\"");
			htmlEnv.code.append(htmlEnv.getOutlineMode());
			/*
			 * if(decos.containsKey("outborder")){
			 * html_env.code.append(" noborder ");
			 * html_env2.code.append(" noborder "); }
			 */
			// classid������Ȥ��ˤ�������
			if (htmlEnv.writtenClassId.contains(HTMLEnv.getClassID(this))) {
				htmlEnv.code.append(" class=\"");
				htmlEnv.code.append(HTMLEnv.getClassID(this));
			}

			if (decos.containsKey("class")) {
				if (!htmlEnv.writtenClassId.contains(HTMLEnv.getClassID(this))) {
					htmlEnv.code.append(" class=\"");
				} else {
					htmlEnv.code.append(" ");
				}
				htmlEnv.code.append(decos.getStr("class") + "\" ");
			} else if (htmlEnv.writtenClassId
					.contains(HTMLEnv.getClassID(this))) {
				htmlEnv.code.append("\" ");
			}
			htmlEnv.code.append("><TR>");
		}

		// xml
		if (GlobalEnv.isOpt()) {
			htmlEnv2.code.append("<tfe type=\"connect\" dimension =\"1\"");
			if (decos.containsKey("tablealign"))
				htmlEnv2.code.append(" align=\"" + decos.getStr("tablealign")
						+ "\"");
			if (decos.containsKey("tablevalign"))
				htmlEnv2.code.append(" valign=\"" + decos.getStr("tablevalign")
						+ "\"");
			if (decos.containsKey("tabletype")) {
				htmlEnv2.code.append(" tabletype=\""
						+ decos.getStr("tabletype") + "\"");
				if (decos.containsKey("cellspacing")) {
					htmlEnv2.code.append(" cellspacing=\""
							+ decos.getStr("cellspacing") + "\"");
				}
				if (decos.containsKey("cellpadding")) {
					htmlEnv2.code.append(" cellpadding=\""
							+ decos.getStr("cellpadding") + "\"");
				}
				if (decos.containsKey("border")) {
					htmlEnv2.code.append(" border=\""
							+ decos.getStr("border").replace("\"", "") + "\"");
				}
				if (decos.containsKey("tableborder")) {
					htmlEnv2.code.append(" tableborder=\""
							+ decos.getStr("tableborder").replace("\"", "")
							+ "\"");
				}
			} else {
				if (decos.containsKey("border")) {
					htmlEnv2.code.append(" border=\""
							+ decos.getStr("border").replace("\"", "") + "\"");
				} else {
					htmlEnv2.code.append(" border=\""
							+ htmlEnv.tableBorder.replace("\"", "") + "\"");
				}
				if (decos.containsKey("tableborder")) {
					htmlEnv2.code.append(" tableborder=\""
							+ decos.getStr("tableborder").replace("\"", "")
							+ "\"");
				}
			}
			if (htmlEnv.writtenClassId.contains(HTMLEnv.getClassID(this))) {
				htmlEnv2.code.append(" class=\"");
				htmlEnv2.code.append(HTMLEnv.getClassID(this));
			}
			if (decos.containsKey("class")) {
				if (!htmlEnv.writtenClassId.contains(HTMLEnv.getClassID(this))) {
					htmlEnv2.code.append(" class=\"");
				} else {
					htmlEnv2.code.append(" ");
				}
				htmlEnv2.code.append(decos.getStr("class") + "\" ");
			} else if (htmlEnv.writtenClassId
					.contains(HTMLEnv.getClassID(this))) {
				htmlEnv2.code.append("\" ");
			}

			if (decos.containsKey("form")) {
				htmlEnv2.code.append(" form=\"" + HTMLEnv.getFormNumber()
						+ "\" ");
			}
			htmlEnv2.code.append(">");
		}

		// tk
		// end////////////////////////////////////////////////////////////////////

		// Log.out("<TABLE class=\""+HTMLEnv.getClassID(this) + "\"><TR>");
		int i = 0;

		if (decos.containsKey("form")) {
			htmlEnv.code.append(HTMLFunction.createForm(decos));
			HTMLEnv.setFormItemFlg(true, null);
		}

		while (this.hasMoreItems()) {
			ITFE tfe = tfes.get(i);
			htmlEnv.code.append("<TD class=\"" + HTMLEnv.getClassID(tfe)
					+ " nest\">\n");
			String classid = HTMLEnv.getClassID(tfe);

			// Log.out("<TD class=\""
			// + HTMLEnv.getClassID(tfe) + " nest\"> decos : " + decos);
			this.worknextItem();

			if (htmlEnv.notWrittenClassId.contains(classid)) {
				htmlEnv.code.delete(htmlEnv.code.indexOf(classid),
						htmlEnv.code.indexOf(classid) + classid.length() + 1);
			}

			htmlEnv.code.append("</TD>\n");
			// Log.out("</TD>");

			i++;
		}

		htmlEnv2.code.append("</tfe>");
		if (decos.containsKey("form")) {
			htmlEnv2.code.append("<form" + HTMLEnv.getFormNumber() + "end />");
			Log.out("<form" + HTMLEnv.getFormNumber() + "end />");
			htmlEnv.code.append(HTMLEnv.exFormNameCreate());
			htmlEnv.code.append("</form>");
			HTMLEnv.setFormItemFlg(false, null);
			HTMLEnv.incrementFormNumber();
			if (decos.getStr("form").toLowerCase().equals("search"))
				HTMLEnv.setSearch(false);
		}

		htmlEnv.code.append("</TR></TABLE>\n");
		// Log.out("</TR></TABLE>");

		// Log.out("TFEId = " + HTMLEnv.getClassID(this));
		// html_env.append_css_def_td(HTMLEnv.getClassID(this), this.decos);
		return null;
	}

}
