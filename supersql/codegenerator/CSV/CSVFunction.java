package supersql.codegenerator.CSV;

import supersql.codegenerator.Function;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class CSVFunction extends Function {

	boolean embedflag = false;

    public CSVFunction()
    {
    }

    public CSVFunction(Manager manager, CSVEnv xenv, CSVEnv xenv2) {
        super();
    }

    public String work(ExtList data_info) {
        this.setDataList(data_info);

        Log.out("TFEId = " + CSVEnv.getClassID(this));
		return null;
    }

    public static String Func_replace(String s){
	    if(s.contains("&"))
	    	s = s.replace("&", "&amp;");
	    if(s.contains("<"))
	    	s = s.replace("<", "&lt;");
	    if(s.contains(">"))
	    	s = s.replace(">", "&gt;");

	    return s;
    }

    public static String Func_att_replace(String s){
    	if(s.contains("'"))
	    	s = s.replace("'", "&apos;");
	    if(s.contains("\""))
	    	s = s.replace("\"", "&quot;");

	    return s;
    }

}