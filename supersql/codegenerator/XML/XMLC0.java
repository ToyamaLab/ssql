package supersql.codegenerator.XML;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Connector;
import supersql.codegenerator.Function;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.parser.TFEparser;

public class XMLC0 extends Connector {

    Manager manager;

    public static XMLEnv xml_env;
    XMLEnv xml_env2;

    public static int attflag = 0;
    public static int parent_attflag = 0;
    public static int tagclose_flag = 0;
    public static int decos_tag_flag = 0;
    public static int XMLC0_passflag = 0;

    TFEparser tfeps;

    public XMLC0(Manager manager, XMLEnv xenv, XMLEnv xenv2) {
        this.manager = manager;
        XMLC0.xml_env = xenv;
        this.xml_env2 = xenv2;
    }

    @Override
    public String work(ExtList data_info) {
    	Log.out("------------- XMLC0 -------------");

    	XMLC0_passflag++;

        this.setDataList(data_info);

        int i = 0;

        String tag = null;

	    if(decos.containsKey("tag")){
        	tag = decos.getStr("tag");

	        xml_env.code.append("<" + tag + ">");
		    parent_attflag = 1;
		    decos_tag_flag = 1;
	   }
	    else{
	    	decos_tag_flag = 0;
	    }

	    while(this.hasMoreItems()) {
	    	ITFE tfe = (ITFE) tfes.get(sindex);
	    	Log.out("tfe : " + tfe);

	    	int ci = tfe.countconnectitem();

	    	ExtList subdata = data.ExtsubList(dindex, dindex + ci);
	    	Log.out("subdata : " + subdata);

	    	if (tfe instanceof Connector || tfe instanceof Attribute
	    			|| tfe instanceof Function) {
	    		tfe.work(subdata);
	    	} else {
	    		tfe.work((ExtList) subdata.get(0));
	    	}
	    	sindex++;
	    	dindex += ci;
	    	Log.out("tfe.countconnectitem() : " + ci);	   		 
	    }

	   	XMLFunction.function_close = 0;
	   	tagclose_flag++;

	   	if(decos.containsKey("tag")){
	   		xml_env.code.append("</" + tag + ">");
	   	}

	   	Log.out("C0 tag(end) : " + tag);
	   	Log.out("TFEId = " + XMLEnv.getClassID(this));
		return null;
    }
    
    public String getSymbol() {
        return "XMLC0";
    }

}