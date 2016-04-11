package supersql.codegenerator.Mobile_HTML5;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.parser.Embed;


public class Mobile_HTML5C2 extends Connector {

    Manager manager;

    Mobile_HTML5Env html_env;
    Mobile_HTML5Env html_env2;
    
    static boolean tableFlg = false;		//20130314  table
    static boolean table0Flg = false;		//20130325  table0
    static boolean divFlg = false;			//20130326  div

    //���󥹥ȥ饯��
    public Mobile_HTML5C2(Manager manager, Mobile_HTML5Env henv, Mobile_HTML5Env henv2) {
        this.manager = manager;
        this.html_env = henv;
        this.html_env2 = henv2;
    }

    //C2��work�᥽�å�
    public String work(ExtList data_info) {
    	Mobile_HTML5.preProcess(getSymbol(), decos, html_env);	//Pre-process (前処理)
    	
    	//20131001 tableDivHeader
    	if(decos.containsKey("header") && Mobile_HTML5G2.tableDivHeader_Count2<1){
    		Mobile_HTML5G2.tableDivHeader_codeBuf = html_env.code.toString();
    		Mobile_HTML5G2.tableDivHeader_Count2++;
    	}
    	
        int panelFlg = 0;	//20130503  Panel
    	
        Log.out("------- C2 -------");
        Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
        Log.out("tfessize=" + tfes.size());
        Log.out("countconnetitem=" + countconnectitem());
        
        this.setDataList(data_info);

        if(decos.containsKey("insert")){
        	Mobile_HTML5Env.setIDU("insert");
        }
        if(decos.containsKey("update")){
        	Mobile_HTML5Env.setIDU("update");
        }
        if(decos.containsKey("delete")){
        	Mobile_HTML5Env.setIDU("delete");
        }

        String classid = Mobile_HTML5Env.getClassID(this);
        html_env.append_css_def_td(classid, this.decos);
        
    	
        //20130325  table0
        if(decos.containsKey("table0"))	table0Flg = true;
        else							table0Flg = false;
        //20130314  table
        if(decos.containsKey("table") || table0Flg || Mobile_HTML5C1.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg){
    		tableFlg = true;
    	}//else	tableFlg = false;
        
        //20130326  div
        if(decos.containsKey("div")){
    		divFlg = true;
    		tableFlg = false;
    	}//else divFlg = false;
        
        if(!GlobalEnv.isOpt()){
        	//20130503  Panel
    	    panelFlg = Mobile_HTML5C1.panelProcess1(decos, html_env);
        	
        	//20130330 tab
        	//tab1
        	if(decos.containsKey("tab1")){
            	html_env.code.append("<div data-role=\"content\"> <div id=\"tabs\">\n<ul>\n");
            	html_env.code.append("	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">");
            	if(!decos.getStr("tab1").equals(""))	html_env.code.append(decos.getStr("tab1"));
            	else          							html_env.code.append("tab1");
            	html_env.code.append("</a></li>\n");
            	html_env.code.append("</ul>\n<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
            }
        	//tab2〜tab15
        	else{
        		int i=2;
        		while(i<=Mobile_HTML5Env.maxTab){		//HTMLEnv.maxTab=15
        			//Log.info("i="+i+" !!");
        			if(decos.containsKey("tab"+i) || (i==2 && decos.containsKey("tab"))){
    	        		//replace: </ul>の前に<li>〜</li>を付加
    	        		String a = "</ul>";
    	        		String b = "	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">";
    	        		if(decos.containsKey("tab"+i))
	    	        		if(!decos.getStr("tab"+i).equals(""))	b += decos.getStr("tab"+i);
	    	            	else				            		b += "tab"+i;
    	        		else
    	        			if(!decos.getStr("tab").equals(""))		b += decos.getStr("tab");
	    	            	else				            		b += "tab";
    	            	b += "</a></li>\n";
    	            	Mobile_HTML5Manager.replaceCode(html_env, a, b+a);
    	            	
    	            	//replace: 最後の</div></div></div>カット
    	        		Mobile_HTML5Manager.replaceCode(html_env, "</div></div></div>", "");
    	        		
//    	        		//replace: 不要な「<div class=〜」をカット
//    	        		Mobile_HTML5Manager.replaceCode(html_env, "<div class=\""+Mobile_HTML5Env.getClassID(this)+" \">", "");
    	        		
    	            	html_env.code.append("<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
    	            	break;
    	        	}
        			i++;
        		}
        	}
        	
        	//20130312 collapsible
        	if(decos.containsKey("collapse")){
            	html_env.code.append("<DIV data-role=\"collapsible\" data-content-theme=\"c\" style=\"padding: 0px 12px;\">\n");
            	
            	//header
            	if(!decos.getStr("collapse").equals(""))
            		html_env.code.append("	<h1>"+decos.getStr("collapse")+"</h1>\n");
            	else
            		html_env.code.append("<h1>Contents</h1>\n");
            }
        	
        	//20130309
        	//20130314  table
        	if(tableFlg){
        		html_env.code.append(Mobile_HTML5C1.getTableStartTag(html_env, decos, this));
        	}
        	
        }

        int i = 0;
        
    	Mobile_HTML5.beforeWhileProcess(getSymbol(), decos, html_env);
        while (this.hasMoreItems()) {
            if(decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	table0Flg = true;
            if(decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || table0Flg)	tableFlg=true;
            if(decos.containsKey("div")){
        		divFlg = true;
        		tableFlg = false;
        	}//else divFlg = false;
            
            ITFE tfe = (ITFE) tfes.get(i);
            String classid2 = Mobile_HTML5Env.getClassID(tfe);
            
            //20130312 collapsible
	      	if(decos.containsKey("collapse"))
	          	html_env.code.append("<p>\n");
	      	else if(!tableFlg && !Mobile_HTML5Function.textFlg2) 
	            //20130309
	            html_env.code.append("<div class=\""+classid2+" \">\n");
	      	
	      	//20130314  table
	      	if(tableFlg){
	      		html_env.code.append("<TR><TD valign=\"middle\" class=\""
                   + classid2 + " nest\">\n");
	      		Log.out("<TR><TD class=\"nest "
                    + classid2 + " nest\"> decos:" + decos);
	      	}
        	
	      	
//	      	if(Mobile_HTML5Env.dynamicFlg){	//20130529 dynamic
//	      		//☆★
//	      		Log.info("☆★C2 tfe : " + tfe);
//	      		//☆★      		 Log.info("C2 tfe : " + tfe);
//            	//☆★            Log.info("C2 tfes : " + this.tfes);
//            	//☆★            Log.info("C2 tfeItems : " + this.tfeItems);
//	      	}
	      	
	        Mobile_HTML5.whileProcess1(getSymbol(), decos, html_env, data, data_info, tfe, tfes, tfeItems);
	      	
            this.worknextItem();
           
	        Mobile_HTML5.whileProcess2(getSymbol(), decos, html_env, data, data_info, tfe, tfes, tfeItems);
            
            if(decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	table0Flg = true;
            if(decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || table0Flg)	tableFlg=true;
            if(decos.containsKey("div")){
        		divFlg = true;
        		tableFlg = false;
        	}//else divFlg = false;
            
            //20130314  table
	      	if(tableFlg)
	      		html_env.code.append("</TD></TR>\n");
	      	//Log.out("</TD></TR>");
	      	
            //20130312 collapsible
	      	if(decos.containsKey("collapse"))
	          	html_env.code.append("</p>\n");
	      	else if(!tableFlg && !Mobile_HTML5Function.textFlg && !Mobile_HTML5Function.textFlg2)	//20130914  "text"
	        	html_env.code.append("\n</div>");
	      	if(Mobile_HTML5Function.textFlg){					//20130914  "text"
	      		Mobile_HTML5Function.textFlg = false;
	      	}
            
            html_env.code.append("\n");		//20130309
            
            html_env.code = Embed.postProcess(html_env.code);	//goto 20130915-2  "<$  $>"

            i++;
        }	//	/while
        Mobile_HTML5.afterWhileProcess(getSymbol(), classid, decos, html_env);

        html_env2.code.append("</tfe>");

      	//20130314  table
      	if(tableFlg){
      		html_env.code.append("</TABLE>\n");		//20130309
      		tableFlg = false;
      		table0Flg = false;		//20130325 table0
      	}
      	
        //20130312 collapsible
      	if(decos.containsKey("collapse")){
          	html_env.code.append("</DIV>");
        }
      	
    	//20130330 tab
		int a=1;
    	while(a<=Mobile_HTML5Env.maxTab){
    		if(decos.containsKey("tab"+a) || (a==1 && decos.containsKey("tab"))){
	    		html_env.code.append("</div></div></div>\n");
	    		Mobile_HTML5Env.tabCount++;
	    		break;
	    	}
	    	a++;
    	}
      	
    	//20130503  Panel
    	Mobile_HTML5C1.panelProcess2(decos, html_env, panelFlg);
	    	
      	if(divFlg)	divFlg = false;		//20130326  div
      	
        Mobile_HTML5.postProcess(getSymbol(), classid, decos, html_env);	//Post-process (後処理)

        Log.out("TFEId = " + classid);
        
        //20131001 tableDivHeader
        if(decos.containsKey("header"))
        	html_env.code = Mobile_HTML5G2.createAndCutTableDivHeader(html_env);
		return null;
    }

    public String getSymbol() {
        return "Mobile_HTML5C2";
    }

}