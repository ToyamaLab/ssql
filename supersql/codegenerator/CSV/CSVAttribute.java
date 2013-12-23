package supersql.codegenerator.CSV;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Manager;
import supersql.extendclass.ExtList;

public class CSVAttribute extends Attribute {

    private CSVEnv csvEnv;
    public static String value;

    public CSVAttribute(Manager manager, CSVEnv xenv, CSVEnv xenv2) {
        super();
        this.csvEnv = xenv;
    }

    public String work(ExtList data_info){
    	value = this.getStr(data_info);
        csvEnv.code.append(value + ", ");
		return null;
    }
}

