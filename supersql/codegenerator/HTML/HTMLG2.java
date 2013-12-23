package supersql.codegenerator.HTML;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5Function;
import supersql.common.GlobalEnv;
import supersql.extendclass.ExtList;

public class HTMLG2 extends Grouper {

	/** @deprecated use HTMLG2() instead **/
	public HTMLG2(Manager manager, HTMLEnv henv, HTMLEnv henv2) {
		Dimension =2;
	}
	
	public HTMLG2(){
		Dimension = 2;
	}
	
	public Element createTableNode(ExtList<ExtList<String>> dataInfo){
		Element result = new Element(Tag.valueOf("table"), "");
		this.setDataList(dataInfo);
		nodeCreationPreProcess(result);
		while (this.hasMoreItems()) {
			HTMLEnv.gLevel++;
			HTMLUtils.propagateDeco(tfe, decos);
			result.appendElement("tr").appendElement("td").appendChild((Element) this.createNextItemNode());
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
		Element result = new Element(Tag.valueOf("div"), "");
		result.addClass("box").addClass("group2");
		this.setDataList(dataInfo);

		nodeCreationPreProcess(result);

		while (this.hasMoreItems()) {
			HTMLFunction.glvl = HTMLEnv.gLevel;	//added by goto 20130914  "SEQ_NUM"
			HTMLEnv.gLevel++;
			HTMLUtils.propagateDeco(tfe, decos);
			result.appendChild((Element) this.createNextItemNode());
			HTMLEnv.gLevel--;
		}
		nodeCreationPostProcess(result);
		
		return result;
	}
	
	private void nodeCreationPreProcess(Element result){
		if (decos.containsKey("layout")
				&& decos.getStr("layout").equalsIgnoreCase("standard")
				|| GlobalEnv.getLayout().equalsIgnoreCase("standard"))
    		result.addClass("vertical_display");
    	else
    		result.addClass("vertical");

		if (!GlobalEnv.isOpt()) {
			if (HTMLEnv.embedFlag)
				result.addClass("embed");

			if (HTMLEnv.writtenClassId.contains(HTMLEnv.getClassID(this))) {
				result.addClass(HTMLEnv.getClassID(this));
			}
			result.addClass("nest");

			if (!HTMLEnv.isOutlineMode()) {
				result.attr("frame", "void");
			}
		}
	}
	
	private void nodeCreationPostProcess(Element result){
		HTMLUtils.checkIfForm(result);
		

		if (HTMLEnv.getSelectRepeat()) {
			if (HTMLEnv.getSelectRepeat()) {
				HTMLEnv.setSelectRepeat(false);
				HTMLEnv.incrementFormPartsNumber();
			} else {
				HTMLEnv.incrementFormPartsNumber();
			}
		}
		HTMLUtils.processDecos(result, decos);
		HTMLFunction.Func_seq_num_initialization(HTMLEnv.gLevel);	//added by goto 20130914  "SEQ_NUM"
	}

	@Override
	public String getSymbol() {
		return "HTMLG2";
	}

}
