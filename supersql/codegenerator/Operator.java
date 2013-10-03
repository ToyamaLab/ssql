package supersql.codegenerator;

import supersql.extendclass.ExtList;

public abstract class Operator extends TFE implements IOperator {

	int Dimension;
	protected ExtList data;
    protected int dindex;
    
    public Operator() {
    	super();
    }
}
