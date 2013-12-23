package supersql.codegenerator.HTML;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5Function;
import supersql.common.GlobalEnv;
import supersql.extendclass.ExtList;

public class HTMLG1 extends Grouper {

	/** @deprecated use HTMLG1() instead **/
	public HTMLG1(Manager manager, HTMLEnv henv, HTMLEnv henv2) {
		Dimension = 1;
	}

	public HTMLG1(){
		Dimension = 1;
	}

	public Element createTableNode(ExtList<ExtList<String>> dataInfo) {
		this.setDataList(dataInfo);
		Element result = new Element(Tag.valueOf("table"), "");
		Element tr = result.appendElement("tr");

		nodeCreationPreProcess(result);

		while (this.hasMoreItems()) {
			HTMLFunction.glvl = HTMLEnv.gLevel;	//added by goto 20130914  "SEQ_NUM"
			HTMLEnv.gLevel++;
			HTMLUtils.propagateDeco(tfe, decos);
			tr.appendElement("td").appendChild(
					(Element) this.createNextItemNode());
			HTMLEnv.gLevel--;
		}

		nodeCreationPostProcess(result);
		return result;
	}

	@Override
	public Element createNode(ExtList<ExtList<String>> dataInfo) {
		if ((GlobalEnv.getLayout().equalsIgnoreCase("table") && !decos
				.containsKey("layout"))
				|| (decos.containsKey("layout") && decos.getStr("layout")
						.equalsIgnoreCase("table")))
			return createTableNode(dataInfo);
		this.setDataList(dataInfo);

		Element result = new Element(Tag.valueOf("div"), "");
		result.attr("class", "box group1");
		nodeCreationPreProcess(result);

		while (this.hasMoreItems()) {
			HTMLEnv.gLevel++;
			HTMLUtils.propagateDeco(tfe, decos);
			result.appendChild((Element) this.createNextItemNode());
			HTMLEnv.gLevel--;
		}
		nodeCreationPostProcess(result);
		return result;

	}

	private void nodeCreationPreProcess(Element result) {
		if (decos.containsKey("layout")
				&& decos.getStr("layout").equalsIgnoreCase("standard")
				|| GlobalEnv.getLayout().equalsIgnoreCase("standard"))
			result.addClass("horizontal_display");
		else
			result.addClass("horizontal");
		if (!GlobalEnv.isOpt()) {

			if (HTMLEnv.embedFlag)
				result.addClass("embed");

			if (decos.containsKey("class")) {
				result.addClass(decos.getStr("class"));
			}
			result.addClass(HTMLEnv.getClassID(this));
			result.addClass("nest");
		}
	}

	private void nodeCreationPostProcess(Element result) {
		if (result.getElementsByTag("input").size() > 0) {
			result.getElementsByTag("form").removeClass("form").tagName("div");
			result.tagName("form");
			result.getElementsByAttributeValue("type", "submit").remove();
			result.attr("class", "form horizontal box");
			result = new Element(Tag.valueOf("div"), "").attr("class",
					"group1 vertical box").appendChild(result);
			result.appendChild(JsoupFactory.createInput("submit", "",
					"Let's go !"));
		}

		if (HTMLEnv.getFormItemFlg()) {
			HTMLEnv.incrementFormPartsNumber();
		}
		HTMLUtils.processDecos(result, decos);
		HTMLFunction.Func_seq_num_initialization(HTMLEnv.gLevel);	//added by goto 20130914  "SEQ_NUM"
	}

	@Override
	public String getSymbol() {
		return "HTMLG1";
	}

}
