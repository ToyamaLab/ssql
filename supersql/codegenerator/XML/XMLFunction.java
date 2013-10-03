package supersql.codegenerator.XML;

import supersql.codegenerator.Function;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.parser.SSQLparser;


public class XMLFunction extends Function {
    private Manager manager;
    private XMLEnv xml_env;
    boolean embedflag = false;
    public static int xpath_first = 0;
    public static int function_close = 0;

    public XMLFunction(Manager manager, XMLEnv xenv) {
        super();
        this.manager = manager;
        this.xml_env = xenv;
    }

    public void work(ExtList data_info) {
//        this.setDataList(data_info);
//
//        String FuncName = this.getFuncName();
//
//        if(FuncName.equalsIgnoreCase("null")) {
//
//        	if(XMLAttribute.absent_on_null_flag != 1){
//	        	if(XMLAttribute.tagcount!=0){
//	    			xml_env.code.append(">" + XMLAttribute.tag_value + "</" + XMLAttribute.tag + ">");
//	    		}
//        	}
//
//        	XMLAttribute.absent_on_null_flag = 0;
//        	XMLAttribute.tagcount = 0;
//
//            Func_null();
//
//            XMLAttribute.no_close_tag_flag = 1;
//            function_close = 1;
//        }
//        else if(FuncName.equalsIgnoreCase("text")){
//
//        	if(XMLC0.parent_attflag == 1){
//        		xml_env.code.append(">");
//        		XMLC0.parent_attflag = 2;
//        	}
//        	if(XMLAttribute.absent_on_null_flag != 1){
//	        	if(XMLAttribute.tagcount!=0){
//	    			xml_env.code.append(">" + XMLAttribute.tag_value + "</" + XMLAttribute.tag + ">");
//	    		}
//        	}
//
//        	XMLAttribute.absent_on_null_flag = 0;
//        	XMLAttribute.tagcount = 0;
//
//        	Func_text(data_info);
//
//        	XMLAttribute.no_close_tag_flag = 1;
//        	function_close = 1;
//        }
//        else if(FuncName.equalsIgnoreCase("xmlquery")){
//
//        	if(XMLC0.parent_attflag == 1){
//        		xml_env.code.append(">");
//        		XMLC0.parent_attflag = 2;
//        	}
//        	if(xpath_first == 0){
//        		if(XMLAttribute.absent_on_null_flag != 1){
//	        		if(XMLAttribute.tagcount!=0){
//		    			xml_env.code.append(">" + XMLAttribute.tag_value + "</" + XMLAttribute.tag + ">");
//		    		}
//        		}
//
//        		XMLAttribute.absent_on_null_flag = 0;
//        	}
//
//        	XMLAttribute.tagcount = 0;
//
//        	Func_xmlquery(data_info);
//
//        	XMLAttribute.no_close_tag_flag = 1;
//        	function_close = 1;
//        }
//        else if(FuncName.equalsIgnoreCase("xpath")){
//
//        	if(XMLC0.parent_attflag == 1){
//        		xml_env.code.append(">");
//        		XMLC0.parent_attflag = 2;
//        	}
//        	if(xpath_first == 0){
//        		if(XMLAttribute.absent_on_null_flag != 1){
//	        		if(XMLAttribute.tagcount!=0){
//		    			xml_env.code.append(">" + XMLAttribute.tag_value + "</" + XMLAttribute.tag + ">");
//		    		}
//        		}
//        		XMLAttribute.absent_on_null_flag = 0;
//        	}
//
//        	XMLAttribute.tagcount = 0;
//
//        	Func_xpath(data_info);
//
//        	XMLAttribute.no_close_tag_flag = 1;
//        	function_close = 1;
//        }
//        else if(FuncName.equalsIgnoreCase("comment")){
//
//        	if(XMLC0.parent_attflag == 1){
//        		xml_env.code.append(">");
//        		XMLC0.parent_attflag = 2;
//        	}
//        	if(XMLAttribute.absent_on_null_flag != 1){
//	        	if(XMLAttribute.tagcount!=0){
//	    			xml_env.code.append(">" + XMLAttribute.tag_value + "</" + XMLAttribute.tag + ">");
//	        	}
//        	}
//
//        	XMLAttribute.absent_on_null_flag = 0;
//        	XMLAttribute.tagcount = 0;
//
//        	Func_comment(data_info);
//
//        	XMLAttribute.no_close_tag_flag = 1;
//        	function_close = 1;
//        }
//
//        Log.out("TFEId = " + XMLEnv.getClassID(this));
    }

    private void Func_null() {
        return;
    }

    public void Func_text(ExtList data_info){
//     	String tag_value = new String();
//     	tag_value = data_info.get(0).toString().substring(4);
//
//     	Func_replace(tag_value);
//
//     	if(!tag_value.equals("")){
//     		xml_env.code.append(tag_value + "\n");
//     	}
//     }
//
//    public void Func_comment(ExtList data_info) {
//    	String comment_value = new String();
//    	comment_value = data_info.get(0).toString().substring(4);
//
//    	if(!comment_value.contains("--")){
//    		xml_env.code.append("<!-- ");
//    		xml_env.code.append(comment_value);
//    		xml_env.code.append(" -->");
//    	}
    }

    public void Func_xmlquery(ExtList data_info){
//    	String xml_value = new String();
//    	xml_value = data_info.get(0).toString().substring(4);
//
//    	xml_value = xml_value.toString().replace("{", "");
//    	xml_value = xml_value.toString().replace("}", "");
//
//    	if(SSQLparser.xmlTextFlag != 1){
//    		xml_value = xml_value.toString().replace(",", "");
//    	}
//
//    	if(SSQLparser.xpathTagExist == 1){
//    		if(SSQLparser.xmlTextFlag == 1){
//            	String[] text_tagvalue = xml_value.split(",");
//
//        		for(int count=0; count < text_tagvalue.length; count++){
//        			xml_env.code.append("<" + SSQLparser.xpathTag + ">");
//        			xml_env.code.append(text_tagvalue[count]);
//        			xml_env.code.append("</" + SSQLparser.xpathTag + ">");
//        		}
//    		}
//    		else{
//		    	xml_env.code.append("<" + SSQLparser.xpathTag + ">");
//		        xml_env.code.append(xml_value);
//		    	xml_env.code.append("</" + SSQLparser.xpathTag + ">");
//    		}
//    	}
//    	else{
//    		xml_env.code.append(xml_value);
//    	}
    }

    public void Func_xpath(ExtList data_info){
//    	String xml_value = new String();
//    	xml_value = data_info.get(0).toString().substring(4);
//
//    	xml_value = xml_value.toString().replace("{", "");
//    	xml_value = xml_value.toString().replace("}", "");
//
//    	if(SSQLparser.xmlTextFlag == 1){
//
//    	}
//    	else{
//    		xml_value = xml_value.toString().replace(",", "");
//    	}
//
//    	if(SSQLparser.xpathTagExist == 1){
//    		if(SSQLparser.xmlTextFlag == 1){
//
//            	String[] text_tagvalue = xml_value.split(",");
//
//        		for(int count=0; count < text_tagvalue.length; count++){
//        			xml_env.code.append("<" + SSQLparser.xpathTag + ">");
//        			xml_env.code.append(text_tagvalue[count]);
//        			xml_env.code.append("</" + SSQLparser.xpathTag + ">");
//        		}
//    		}
//    		else{
//		    	xml_env.code.append("<" + SSQLparser.xpathTag + ">");
//		        xml_env.code.append(xml_value);
//		    	xml_env.code.append("</" + SSQLparser.xpathTag + ">");
//    		}
//    	}
//    	else{
//    		xml_env.code.append(xml_value);
//    	}
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