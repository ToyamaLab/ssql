package supersql.codegenerator;

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
		if (tfe instanceof Attribute) {
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