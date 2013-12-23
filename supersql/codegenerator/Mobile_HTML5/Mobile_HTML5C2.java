package supersql.codegenerator.Mobile_HTML5;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;


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

//        if(decos.containsKey("form")){
//        	html_env2.code.append("<form"+Mobile_HTML5Env.getFormNumber()+"start />");
//        	if(decos.getStr("form").toLowerCase().equals("search")){
//        		Mobile_HTML5Env.setSearch(true);
//        	}
//        }	
        
        if(decos.containsKey("insert")){
        	Mobile_HTML5Env.setIDU("insert");
        }
        if(decos.containsKey("update")){
        	Mobile_HTML5Env.setIDU("update");
        }
        if(decos.containsKey("delete")){
        	Mobile_HTML5Env.setIDU("delete");
        }

        //tk start////////////////////////////////////////////////////////////////
        html_env.append_css_def_td(Mobile_HTML5Env.getClassID(this), this.decos);
        
    	
        //20130325  table0
        if(decos.containsKey("table0"))	table0Flg = true;
        else							table0Flg = false;
        //20130314  table
        //if(decos.containsKey("table") || !decos.containsKey("div") || table0Flg || HTMLC1.tableFlg || HTMLG1.tableFlg || HTMLG2.tableFlg){
        if(decos.containsKey("table") || table0Flg || Mobile_HTML5C1.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg){
//        if(decos.containsKey("table") || table0Flg){
        //if(decos.containsKey("table") || table0Flg || HTMLC1.tableFlg || HTMLC2.tableFlg || HTMLG1.tableFlg || HTMLG2.tableFlg){
    	//if(decos.containsKey("table") || HTMLC1.tableFlg || HTMLG1.tableFlg){
//    		Log.info("C2!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//    		Log.info("C2 tableFlg = true !!");
    		tableFlg = true;
    	}//else	tableFlg = false;
        
        //20130326  div
        if(decos.containsKey("div")){
        //if(decos.containsKey("div") && !HTMLC1.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg){
        //if((decos.containsKey("div") && !HTMLC1.tableFlg && !HTMLC2.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg)
        //		|| HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
    	//if(decos.containsKey("div") || HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
    	//if(decos.containsKey("div") || HTMLC1.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
    		divFlg = true;
    		tableFlg = false;
    	}//else divFlg = false;
        
//        //20130529
//        if(decos.containsKey("dynamic")){
//        	if(!Mobile_HTML5Env.dynamicFlg)	Mobile_HTML5Env.staticBuf = html_env.code;
//        	Mobile_HTML5Env.dynamicFlg = true;
//        	Log.i("※C2 HTMLEnv.staticBuf: "+Mobile_HTML5Env.staticBuf);
//        }
        
//        //20130914  "text"
//        if(decos.containsKey("text")){
//        	Log.e("	in!");
//        	HTMLFunction.textFlg2 = true;
//        }
        
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
//            	HTMLEnv.tabFlg = true;
            }
        	//tab2〜tab15
//        	else if(HTMLEnv.tabFlg){
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
    	        		
    	        		//replace: 不要な「<div class=〜」をカット
    	        		Mobile_HTML5Manager.replaceCode(html_env, "<div class=\""+Mobile_HTML5Env.getClassID(this)+" \">", "");
    	        		
    	            	html_env.code.append("<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
    	            	break;
    	        	}
        			i++;
//        			if(i>HTMLEnv.maxTab)	HTMLEnv.tabFlg =false;
        		}
        	}
        	
        	//20130312 collapsible
        	if(decos.containsKey("collapse")){
            	html_env.code.append("<DIv data-role=\"collapsible\" data-content-theme=\"c\" style=\"padding: 0px 12px;\">\n");
            	
            	//header
            	if(!decos.getStr("collapse").equals(""))
            		html_env.code.append("	<h1>"+decos.getStr("collapse")+"</h1>\n");
            	else
            		html_env.code.append("<h1>Contents</h1>\n");
            }
        	
//        	//changed by goto 20130309  border=0
        	//20130309
        	//20130314  table
        	if(tableFlg){
        		html_env.code.append(Mobile_HTML5C1.getTableStartTag(html_env, decos, this));
        	}
        	
        }
        
        
        //html_env2.code.append(">");
        
        //System.out.println(html_env.code);
                
        //tk end//////////////////////////////////////////////////////////////////
        
//        Log.out("<TABLE class=\""+HTMLEnv.getClassID(this) + "\">");

        int i = 0;
        
//    	if(decos.containsKey("form")){
//           	html_env.code.append(Mobile_HTML5Function.createForm(decos));
//           	Mobile_HTML5Env.setFormItemFlg(true,null);
//        }
        
    	Mobile_HTML5.beforeWhileProcess(getSymbol(), decos, html_env);
        while (this.hasMoreItems()) {
            //Log.info("C2-1:	"+tableFlg+"	"+decos.containsKey("table")+"	"+decos.containsKey("table0"));
            if(decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	table0Flg = true;
            //else	table0Flg=false;
            if(decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || table0Flg)	tableFlg=true;
//            if(decos.containsKey("table") || HTMLC1.tableFlg || HTMLC2.tableFlg || HTMLG1.tableFlg || HTMLG2.tableFlg || table0Flg)	tableFlg=true;
            //if(decos.containsKey("table") || table0Flg)	tableFlg=true;
//            Log.i("C2:	decos = "+decos+"		"+HTMLC1.tableFlg+" "+HTMLC2.tableFlg+"	tableFlg = "+tableFlg+"	divFlg = "+decos.containsKey("div"));
            if(decos.containsKey("div")){
        	//if(decos.containsKey("div") && !HTMLC1.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg){
            //if((decos.containsKey("div") && !HTMLC1.tableFlg && !HTMLC2.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg)
            //		|| HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
//            if(decos.containsKey("div") || HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
//            if(decos.containsKey("div") || HTMLC1.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
        		divFlg = true;
        		tableFlg = false;
        	}//else divFlg = false;
            
            ITFE tfe = (ITFE) tfes.get(i);
            
            //20130312 collapsible
	      	if(decos.containsKey("collapse"))
	          	html_env.code.append("<p>\n");
	      	else if(!tableFlg && !Mobile_HTML5Function.textFlg2) 
	            //20130309
	            //x html_env.code.append("	<DIV Class=\"ui-grid-a\">	<div class=\"ui-block-a\">");		
	            html_env.code.append("<div class=\""+Mobile_HTML5Env.getClassID(tfe)+" \">\n");
	      	
	      	//20130914  "text"
//	      	Log.e("	decosC2 = "+decos);
//	      	Log.e("	C2 HTMLFunction.textFlg2 = "+HTMLFunction.textFlg2);
	        if(decos.containsKey("text")){	//TODO
//	        	Log.e("	C2 in!");
//	        	HTMLFunction.textFlg2 = true;
	        }
	        
	      	//20130314  table
	      	String classid = null ;
	      	if(tableFlg){
	      		//x html_env.code.append("	<Table><tr><td>\n");
	      		//added 20130314  table align="center" valign="middle"
	      		html_env.code.append("<TR><TD valign=\"middle\" class=\""
	      		//html_env.code.append("<TR><TD align=\"center\" valign=\"middle\" class=\""
//        	   	html_env.code.append("<TR><TD class=\""
                   + Mobile_HTML5Env.getClassID(tfe) + " nest\">\n");
	      		classid = Mobile_HTML5Env.getClassID(tfe);
	      		Log.out("<TR><TD class=\"nest "
                    + Mobile_HTML5Env.getClassID(tfe) + " nest\"> decos:" + decos);
	      	}
        	
	      	
//	      	if(Mobile_HTML5Env.dynamicFlg){	//20130529 dynamic
//	      		//☆★
//	      		Log.info("☆★C2 tfe : " + tfe);
//	      		//☆★      		 Log.info("C2 tfe : " + tfe);
//            	//☆★            Log.info("C2 tfes : " + this.tfes);
//            	//☆★            Log.info("C2 tfeItems : " + this.tfeItems);
//	      	}
	      	
	        Mobile_HTML5.whileProcess1(getSymbol(), decos, html_env, data, data_info, tfe, tfes, tfeItems);
	      	
        	//Log.info("C2  !!");
            this.worknextItem();
            //Log.info("C2-2:	"+tableFlg+"	"+decos.containsKey("table")+"	"+decos.containsKey("table0"));
           
	        Mobile_HTML5.whileProcess2(getSymbol(), decos, html_env, data, data_info, tfe, tfes, tfeItems);
            
            if(decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	table0Flg = true;
            //else	table0Flg=false;
            if(decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || table0Flg)	tableFlg=true;
            //if(decos.containsKey("table") || HTMLC1.tableFlg || HTMLC2.tableFlg || HTMLG1.tableFlg || HTMLG2.tableFlg || table0Flg)	tableFlg=true;
//            if(decos.containsKey("table") || table0Flg)	tableFlg=true;
            if(decos.containsKey("div")){
        	//if(decos.containsKey("div") && !HTMLC1.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg){
            //if(decos.containsKey("div") && (HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg)){
            //if((decos.containsKey("div") && !HTMLC1.tableFlg && !HTMLC2.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg)
            //		|| HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
            //if(decos.containsKey("div") || HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
//            if(decos.containsKey("div") || HTMLC1.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
        		divFlg = true;
        		tableFlg = false;
        	}//else divFlg = false;
            
//            //20130529
//            if(decos.containsKey("dynamic"))	Mobile_HTML5Env.dynamicFlg = true;

            //20130306
            //20130314  table
	      	if(tableFlg){
	      		try{
	            if (html_env.not_written_classid.contains(classid)){
	            	html_env.code.delete(html_env.code.indexOf(classid),html_env.code.indexOf(classid)+classid.length()+1);
	            }
	      		}catch(Exception e){}
	            //x html_env.code.append("	</td></tr></Table>");
	      	}
          
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
//	      		Log.e("C2 text! ");
//	      		html_env.code.append(HTMLFunction.text);
//	      		HTMLFunction.text = "";
	      		Mobile_HTML5Function.textFlg = false;
	      		//HTMLFunction.textFlg2 = false;	//TODO
	      	}
//	      	HTMLFunction.textFlg = false;				//20130914  "text"
            
            html_env.code.append("\n");		//20130309

            i++;
        }	//	/while
        Mobile_HTML5.afterWhileProcess(getSymbol(), decos, html_env);

        html_env2.code.append("</tfe>");
        //Log.out("</TABLE>");
//        if(decos.containsKey("form")){
//        	html_env.code.append(Mobile_HTML5Env.exFormNameCreate());
//           	html_env.code.append("</form>");
//           	Mobile_HTML5Env.setFormItemFlg(false,null);
//           	Mobile_HTML5Env.incrementFormNumber();
//           	if(decos.getStr("form").toLowerCase().equals("search"))
//        		Mobile_HTML5Env.setSearch(false);
//        }

      	//20130314  table
      	if(tableFlg){
      		html_env.code.append("</TABLE>\n");		//20130309
      		tableFlg = false;//Log.i("tableFlg = false! 2");
      		table0Flg = false;		//20130325 table0
      	}
      	
        //20130312 collapsible
      	if(decos.containsKey("collapse")){
          	html_env.code.append("</DIv>");
        }
      	
    	//20130330 tab
//    	if(HTMLEnv.tabFlg){
    		int a=1;
	    	while(a<=Mobile_HTML5Env.maxTab){
	    		//Log.info("a="+a);
	    		if(decos.containsKey("tab"+a) || (a==1 && decos.containsKey("tab"))){
		    		html_env.code.append("</div></div></div>\n");
		    		Mobile_HTML5Env.tabCount++;
		    		break;
		    	}
		    	a++;
	    	}
//    	}
      	
    	//20130503  Panel
    	Mobile_HTML5C1.panelProcess2(decos, html_env, panelFlg);
	    	
      	if(divFlg)	divFlg = false;		//20130326  div
      	
//        if(Mobile_HTML5Env.dynamicFlg)	Mobile_HTML5Env.dynamicFlg = false;		//20130529 dynamic
        
        Mobile_HTML5.postProcess(getSymbol(), decos, html_env);	//Post-process (後処理)

        Log.out("TFEId = " + Mobile_HTML5Env.getClassID(this));
        //html_env.append_css_def_td(HTMLEnv.getClassID(this), this.decos);
        //↑必要？不要？？ -> 不要
        
        //20131001 tableDivHeader
        if(decos.containsKey("header"))
        	html_env.code = Mobile_HTML5G2.createAndCutTableDivHeader(html_env);
		return null;
    }

    public String getSymbol() {
        return "Mobile_HTML5C2";
    }

}