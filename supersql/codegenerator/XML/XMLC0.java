package supersql.codegenerator.XML;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Connector;
import supersql.codegenerator.Function;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.parser.TFEparser;

public class XMLC0 extends Connector {
    XMLManager manager;
    public XMLEnv xml_env;
    public static int attflag = 0;
    public static int parent_attflag = 0;
    public static int tagclose_flag = 0;
    public static int decos_tag_flag = 0;

    TFEparser tfeps;

    public XMLC0(Manager manager, XMLEnv xenv) {
        this.manager = (XMLManager) manager;
        this.xml_env = xenv;
    }

    @Override
    public void work(ExtList<ExtList<String>> data_info) {
    	Log.out("------------- XMLC0 -------------");

        this.setDataList(data_info);

        String tag = null;

	    if(decos.containsKey("tag")){
        	tag = decos.getStr("tag");
	        xml_env.code.append("<" + tag + ">");
		    parent_attflag = 1;
		    decos_tag_flag = 1;
	   }
	   else {
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
    }
    
    public Object createNode(ExtList<ExtList<String>> data_info) {
        this.setDataList(data_info);

        String tag = "";
        if(decos.containsKey("tag")) {
        	tag = decos.getStr("tag");
        }
        if(tag.equals("")) {
        	tag = "connector";
        }
        
        Element node = this.manager.getDoc().createElement(tag);
        
        Element childNode;
	    while(this.hasMoreItems()) {
	    	ITFE tfe = (ITFE) tfes.get(sindex);
	    	int ci = tfe.countconnectitem();

	    	ExtList subdata = data.ExtsubList(dindex, dindex + ci);

	    	if (tfe instanceof Connector || tfe instanceof Attribute
	    			|| tfe instanceof Function) {
	    		childNode = (Element) tfe.createNode(subdata);
	    	} else {
	    		childNode = (Element) tfe.createNode((ExtList) subdata.get(0));
	    	}
	    	
	    	node.appendChild(childNode);
	    	sindex++;
	    	dindex += ci;	   		 
	    }
        
    	return node;
    }
    
    public String getSymbol() {
        return "XMLC0";
    }

}