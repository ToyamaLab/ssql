package supersql.codegenerator.HTML;

import supersql.codegenerator.Connector;
import supersql.codegenerator.Manager;
import supersql.extendclass.ExtList;

//import common.Log;

public class HTMLC0 extends Connector {
	private HTMLEnv htmlEnv;
	private HTMLEnv htmlEnv2;

	// 鐃緒申鐃藷ストラク鐃緒申
	public HTMLC0(Manager manager, HTMLEnv henv, HTMLEnv henv2) {
		this.htmlEnv = henv;
		this.htmlEnv2 = henv2;
	}

	@Override
	public String getSymbol() {
		return "HTMLC0";
	}

	// C2鐃緒申work鐃潤ソ鐃獣ワ申
	@Override
	public String work(ExtList data_info) {

		// Log.out("data_info =" +data_info);

		this.setDataList(data_info);

		if (decos.containsKey("form")) {
			htmlEnv.code.append(HTMLFunction.createForm(decos));
			HTMLEnv.setFormItemFlg(true, null);
			htmlEnv2.code
					.append("<form" + HTMLEnv.getFormNumber() + "start />");
			if (decos.getStr("form").toLowerCase().equals("search"))
				HTMLEnv.setSearch(true);
		}

		while (this.hasMoreItems()) {
			this.worknextItem();
		}

		if (decos.containsKey("form")) {
			htmlEnv2.code.append("<form" + HTMLEnv.getFormNumber() + "end />");
			htmlEnv.code.append(HTMLEnv.exFormNameCreate());
			htmlEnv.code.append("</form>");
			HTMLEnv.setFormItemFlg(false, null);
			HTMLEnv.incrementFormNumber();
			if (decos.getStr("form").toLowerCase().equals("search"))
				HTMLEnv.setSearch(false);
		}
		return null;
	}

}