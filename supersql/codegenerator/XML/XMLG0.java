package supersql.codegenerator.XML;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class XMLG0 extends Grouper {

    Manager manager;
    XMLEnv xml_env;

    public XMLG0(Manager manager, XMLEnv xenv) {
        this.manager = manager;
        this.xml_env = xenv;
    }

    @Override
	public void work(ExtList data_info) {
        Log.out("************* XMLG0 *************");
        this.setDataList(data_info);

        String tag = "";

        String grouper_att0 = null;
        String grouper_att_value0 = null;

        String grouper_att = null;
        String grouper_att_value = null;

        boolean Grouper_flag = false;

        if(decos.containsKey("tag")){
        	tag = decos.getStr("tag");
        	if(!tag.equals("")){
        		xml_env.code.append("<" + tag);
        		Grouper_flag = true;
        	}
        }

       if(decos.containsKey("root_att") && decos.containsKey("value")){
    	   grouper_att0 = decos.getStr("root_att");
    	   XMLFunction.Func_att_replace(grouper_att0);

    	   grouper_att_value0 = decos.getStr("value");
    	   XMLFunction.Func_att_replace(grouper_att_value0);

    	   xml_env.code.append(" " + grouper_att0 + "=\"" + grouper_att_value0 + "\"");
       }

       if(decos.containsKey("root_att1") && decos.containsKey("value1")){
    	   int attNo = 1;

		   while (decos.containsKey("root_att"+attNo)){

			   	if(decos.containsKey("root_att"+attNo)){
			   		grouper_att = decos.getStr("root_att"+attNo);
			   		XMLFunction.Func_att_replace(grouper_att);
			   	}

			   	if(decos.containsKey("value"+attNo)){
			   		grouper_att_value = decos.getStr("value"+attNo);
			   		XMLFunction.Func_att_replace(grouper_att_value);
			   	}

			   	xml_env.code.append(" " + grouper_att + "=\"" + grouper_att_value + "\"");
			   	attNo++;
		   }
       }

       if(Grouper_flag){
    	   xml_env.code.append(">");
       }
       Log.out("G0 tag(start) : " + tag);

        while (this.hasMoreItems()) {
        	this.worknextItem();
        }

        if(Grouper_flag){
        	xml_env.code.append("</" + tag + ">");
        	Grouper_flag = false;
        }

        XMLAttribute.tagcount = 0;

        Log.out("G0 tag(end) : " + tag);
        Log.out("TFEId = " + XMLEnv.getClassID(this));
    }

    @Override
	public String getSymbol() {
        return "XMLG0";
    }

}