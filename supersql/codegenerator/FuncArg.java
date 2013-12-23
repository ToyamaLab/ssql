package supersql.codegenerator;

import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5Function;
import supersql.common.Log;
import supersql.extendclass.ExtList;
//import common.Log;


public class FuncArg {

	String Name; //

	TFE tfe; //

	ExtList Data = new ExtList(); //

	public FuncArg(String nm, TFE t) {
		Name = nm;
		tfe = t;
	}

	public void debugout() {
		debugout(0);
	}

	public void debugout(int count) {

		Debug dbgout = new Debug();

		dbgout.prt(count, "<FuncArg Name:" + Name + ">");
		tfe.debugout(count + 1);
		dbgout.prt(count, "</FuncArg>");

	}

	public ExtList makesch() {

		return tfe.makesch();

	}
	
	public ExtList makeschImage() {

		return tfe.makeschImage();

	}

	public ExtList makele0() {

		return tfe.makele0();

	}

	public int countconnectitem() {
		return tfe.countconnectitem();
	}

	public String getKey() {
		if (Name == null) {
			return "default";
		}
		return Name;
	}

	@Override
	public String toString() {
		return tfe.toString();
	}

	public void setData(ExtList data_info) {
		Data = data_info;
	}

	public String getStr() {
		if (tfe instanceof Function) {
			//20131201 nesting function
			Function.nestingLevel++;
			return tfe.work(Data);	//recursive call

		} else if (tfe instanceof Attribute) {
			
			//20131118 dynamic
			if(Mobile_HTML5.dynamicDisplay){
				return Mobile_HTML5.dynamicFuncArgProcess(tfe);
			}
//			//20131127 form
//			if(Mobile_HTML5.form){
//				return Mobile_HTML5.formFuncArgProcess(tfe);
//			}

			return ((Attribute) tfe).getStr(Data);
		} else {
			return null;
		}
	}

	public void workAtt() {
		tfe.work(Data);
		return;
	}
	
	public Object createNode(){
		return tfe.createNode(Data);
	}
	
	public Class<? extends TFE> getTFEClass(){
		return tfe.getClass();
	}

}