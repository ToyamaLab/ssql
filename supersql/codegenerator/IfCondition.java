package supersql.codegenerator;

import supersql.extendclass.ExtList;

public class IfCondition extends Operator {

	protected Attribute condition;
	protected TFE thenTfe;
	protected TFE elseTfe;

	public IfCondition(Attribute condition, TFE thenTfe, TFE elseTfe) {
		super();
		this.condition = condition;
		this.thenTfe = thenTfe;
		this.elseTfe = elseTfe;
	}

	public void debugout(int count) {
		Debug dbgout = new Debug();
		dbgout.prt(count, "<IfCondition type=" + getSymbol() + " thenTfe="
				+ thenTfe + " elseTfe=" + elseTfe + " condition=" + condition.toString() + ">");

		dbgout.prt(count, "</IfCondition>");
	}

	private String getSymbol() {
		return "HTMLIfCondition";
	}

	public ExtList makesch() {
		ExtList outsch = new ExtList();
		outsch.addAll(condition.makesch());
		outsch.addAll(thenTfe.makesch());
		outsch.addAll(elseTfe.makesch());
		return outsch;
	}

	public ExtList makele0() {
		ExtList le0 = new ExtList();

		le0.add(this.getSymbol());
		le0.addAll(condition.makele0());
		le0.addAll(thenTfe.makele0());
		le0.addAll(elseTfe.makele0());

		return le0;
	}

	public String work(ExtList data_info) {
		return null;
//		return aggregate;
	}

	public int countconnectitem() {
		// TODO Auto-generated method stub
		if(elseTfe != null)
			return 3;
		else
			return 2;
	}

	public ExtList makeschImage() {
		// TODO Auto-generated method stub
		ExtList outsch = new ExtList();
		outsch.addAll(condition.makeschImage());
		outsch.addAll(thenTfe.makeschImage());
		outsch.addAll(elseTfe.makeschImage());
		return outsch;
	}

	public Attribute getCondition() {
		return condition;
	}

	public ITFE getThenTfe() {
		return thenTfe;
	}

	public ITFE getElseTfe() {
		return elseTfe;
	}

	@Override
	public String toString() {
		if(elseTfe == null)
			return "if " + condition + " then "+ thenTfe;
		else
			return "if " + condition + " then "+ thenTfe + " else "+elseTfe;
	}

	@Override
	public Object createNode(ExtList<ExtList<String>> data_info) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
