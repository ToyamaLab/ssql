package supersql.codegenerator.XML;

import org.w3c.dom.Element;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Connector;
import supersql.codegenerator.Function;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
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