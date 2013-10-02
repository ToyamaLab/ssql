package supersql.codegenerator.XML;

import org.w3c.dom.Element;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Manager;
import supersql.extendclass.ExtList;
import supersql.extendclass.Node;

public class XMLAttribute extends Attribute {

    XMLManager manager;
    XMLEnv xml_env;

	public static String tag;
	public static String tag_value;

	public static String att;
	public static String att_value;

	public static String parent_att;
	public static String parent_att_value;

	public static int flag = 0;
	public static int tagcount;

	public static int no_close_tag_flag;
	public static int absent_on_null_flag;


    public XMLAttribute(Manager manager, XMLEnv xenv) {
        super();
        this.manager = (XMLManager) manager;
        this.xml_env = xenv;
    }

//    public void work(ExtList data_info){
//    	Log.out("[ XMLAttribute ]");
//
//    	XMLFunction.function_close = 0;
//    	
//    	tag = this.toString();
//    	
//    	if(decos.containsKey("tag")) 
//    		tag = decos.getStr("tag");
//
//    	xml_env.code.append("<" + tag + ">");
//    	xml_env.code.append(data_info.get(0).toString());
//    	xml_env.code.append("</" + tag + ">");
//
//    	XMLC0.tagclose_flag = 0;
//    }
    
    public Object createNode(ExtList data_info) {
    	tag = this.toString();
    	if(decos.containsKey("tag"))
    		tag = decos.getStr("tag");
    	
    	Element node = this.manager.getDoc().createElement(tag);
    	node.appendChild(this.manager.getDoc().createTextNode(data_info.get(0).toString()));
    	return node;
    }
    
    public Object createNodeNew(Node<String> dataNode) {
    	tag = this.toString();
    	if(decos.containsKey("tag"))
    		tag = decos.getStr("tag");
    	
    	Element node = this.manager.getDoc().createElement(tag);
    	String nodeData = dataNode.getNodeData();
    	node.appendChild(this.manager.getDoc().createTextNode(nodeData));
    	return node;
    }
}