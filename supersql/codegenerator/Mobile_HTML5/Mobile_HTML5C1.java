package supersql.codegenerator.Mobile_HTML5;

import javax.swing.text.html.CSS;

import supersql.codegenerator.Connector;
import supersql.codegenerator.DecorateList;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class Mobile_HTML5C1 extends Connector {

    Manager manager;

    Mobile_HTML5Env html_env;
    Mobile_HTML5Env html_env2;
    
    //20130309
    int gridInt = 0;
    String[] gridString = {"a","b","c","d","e"};
    static int ii =0, jj = 0, Count = 0;
    //static String buf = "";
    
    static boolean tableFlg = false;		//20130314  table
    static boolean table0Flg = false;		//20130325  table0
    static boolean divFlg = false;			//20130326  div
    
    //���󥹥ȥ饯��
    public Mobile_HTML5C1(Manager manager, Mobile_HTML5Env henv, Mobile_HTML5Env henv2) {
        this.manager = manager;
        this.html_env = henv;
        this.html_env2 = henv2;
    }

    //C1��work�᥽�å�
    public String work(ExtList data_info) {
    	Mobile_HTML5.preProcess(getSymbol(), decos, html_env);	//Pre-process (前処理)
    	
    	//20131001 tableDivHeader
    	if(decos.containsKey("header") && Mobile_HTML5G2.tableDivHeader_Count2<1){
    		Mobile_HTML5G2.tableDivHeader_codeBuf = html_env.code.toString();
    		Mobile_HTML5G2.tableDivHeader_Count2++;
    	}
    	
        int panelFlg = 0;	//20130503  Panel

//    	Vector vector_local = new Vector();
        Log.out("------- C1 -------");
        Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
        Log.out("tfes.size=" + tfes.size());
        Log.out("countconnetitem=" + countconnectitem());
        this.setDataList(data_info);

//        if(decos.containsKey("form")){
//        	html_env2.code.append("<form"+Mobile_HTML5Env.getFormNumber()+"start />");
//        	if(decos.getStr("form").toLowerCase().equals("search"))
//        		Mobile_HTML5Env.setSearch(true);
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
        
        //tk start///////////////////////////////////////////////////////////////////////
        html_env.append_css_def_td(Mobile_HTML5Env.getClassID(this), this.decos);
        
        //20130325  table0
        if(decos.containsKey("table0"))	table0Flg = true;
        else							table0Flg = false;
    	//20130314  table
        //Log.info("	"+HTMLC1.tableFlg+" "+HTMLC2.tableFlg+" "+HTMLG1.tableFlg);
        //if(decos.containsKey("table") || !decos.containsKey("div") || table0Flg || HTMLC2.tableFlg || HTMLG1.tableFlg || HTMLG2.tableFlg){
//        if(decos.containsKey("table") || table0Flg){
        //if(decos.containsKey("table") || table0Flg || HTMLC1.tableFlg || HTMLC2.tableFlg || HTMLG1.tableFlg || HTMLG2.tableFlg){
    	if(decos.containsKey("table") || table0Flg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg){
//    		Log.info("C1!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//    		Log.info("C1 tableFlg = true !!");
    		tableFlg = true;
    	}//else	tableFlg = false;
    	
    	//20130326  div
        if(decos.containsKey("div")){
//    	if(decos.containsKey("div") && !HTMLC2.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg){
        //if((decos.containsKey("div") && !HTMLC1.tableFlg && !HTMLC2.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg)
        //		|| HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
    	//if(decos.containsKey("div") || HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
//    	if(decos.containsKey("div") || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
    		divFlg = true;
    		tableFlg = false;
    	}//else divFlg = false;
        
        
//        //20130529
//        if(decos.containsKey("dynamic")){
//        	if(!Mobile_HTML5Env.dynamicFlg)	Mobile_HTML5Env.staticBuf = html_env.code;
//        	Mobile_HTML5Env.dynamicFlg = true;
//        	Log.i("※C1 HTMLEnv.staticBuf: "+Mobile_HTML5Env.staticBuf);
//        }

        if(!GlobalEnv.isOpt()){
        	//20130503  Panel
    	    panelFlg = panelProcess1(decos, html_env);
        	
        	//20130330 tab
        	//tab1
        	if(decos.containsKey("tab1")){
//        		//↓下記はおそらく不要	20130924
//        		//TODO: 下記の効率化、最後の</DIV>を出力しない
//        		//,で結合(水平結合)した際
//        		//replace: 不要な「<div class=〜」をカット
//    			String[] s = {"a","b","c","d","e"};
//    			int j=0;
//    			while(!HTMLManager.replaceCode(html_env, "<div class=\"ui-block-"+s[j]+" "+HTMLEnv.getClassID(this)+"\">", "")
//    					 || !HTMLManager.replaceCode(html_env, "<DIV Class=\"ui-grid-"+s[j]+" #"+(HTMLEnv.uiGridCount-1)+"\">", "")){
//    				if(j>3) break;
//    				j++;
//    			}
        		
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
    	        		//HTMLManager.replaceCode(html_env, "<div class=\""+HTMLEnv.getClassID(this)+" \">", "");
    	        		if(!Mobile_HTML5Manager.replaceCode(html_env, "<div class=\""+Mobile_HTML5Env.getClassID(this)+" \">", "")){
    	        		//String cutClass="class=\""+HTMLEnv.getClassID(this)+" \"";
    	        		//if(!HTMLManager.replaceCode(html_env, "<div "+cutClass+">", "")){
    	        		//	cutClass="";
    	        			
    	        			//TODO ,で結合(水平結合)した際の処理		20130924
//    	        			//Log.info("Cannot cut. "+HTMLEnv.getClassID(this));
//    	        			String[] s = {"a","b","c","d","e"};
//    	        			int j=0;
//    	        			while(!HTMLManager.replaceCode(html_env, "<div class=\"ui-block-"+s[j]+" "+HTMLEnv.getClassID(this)+"\">", "")){
//    	        				//,で結合(水平結合)した際に、このwhileに入る（レアケース）
//    	        				j++;
//    	        				if(j>4) break;
//    	        			}
    	        		}
    	            	
    	        		html_env.code.append("<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
    	        		//上記でカットしたcutClassをappend
    	        		//html_env.code.append("<div id=\"tabs-"+HTMLEnv.tabCount+"\" "+cutClass+">\n");
    	            	break;
    	        	}
        			i++;
//        			if(i>HTMLEnv.maxTab)	HTMLEnv.tabFlg =false;
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
//        	if(!HTMLG1.G1Flg && !tableFlg)	html_env.code.append("<DIV Class=\"ui-grid-a\"");
        	//if((!HTMLG1.G1Flg && !tableFlg) || divFlg){		//20130326  div
        	if(/* !HTMLG1.G1Flg  && */ !tableFlg){
        		//html_env.code.append("<DIV Class=\"ui-grid-a #"+HTMLEnv.uiGridCount+"\"");
        		if(html_env.written_classid.contains(Mobile_HTML5Env.getClassID(this)))
        			html_env.code.append("<DIV Class=\"ui-grid #"+Mobile_HTML5Env.uiGridCount+" "+Mobile_HTML5Env.getClassID(this)+"\"");
//        			html_env.code.append("<DIV Class=\"ui-grid-a #"+HTMLEnv.uiGridCount+" "+HTMLEnv.getClassID(this)+"\"");
        		else
        			html_env.code.append("<DIV Class=\"ui-grid #"+Mobile_HTML5Env.uiGridCount+"\"");
//        			html_env.code.append("<DIV Class=\"ui-grid-a #"+HTMLEnv.uiGridCount+"\"");
        		Mobile_HTML5Env.uiGridCount++;
        		//Log.info("ui-grid-a #"+HTMLEnv.uiGridCount+"	"+"<DIV Class=\"ui-grid-a #"+HTMLEnv.uiGridCount+"\"");
        	}
        	//if((!HTMLG1.G1Flg && !tableFlg) || divFlg)		html_env.code.append(">");		//20130326  div	//20130309	
        	if(/* !HTMLG1.G1Flg  && */ !tableFlg)	html_env.code.append(">");		//20130309
        	
        	//20130314  table
        	if(tableFlg){
        		html_env.code.append(getTableStartTag(html_env, decos, this)+"<TR>");
        	}
        	
        }

        
        //tk end////////////////////////////////////////////////////////////////////
      
      
        //Log.out("<TABLE class=\""+HTMLEnv.getClassID(this) + "\"><TR>");
        
        int i = 0;
        
//        if(decos.containsKey("form")){
//        	html_env.code.append(Mobile_HTML5Function.createForm(decos));
//           	Mobile_HTML5Env.setFormItemFlg(true,null);
//        }
        
        Mobile_HTML5.beforeWhileProcess(getSymbol(), decos, html_env);
        while (this.hasMoreItems()) {
            //Log.info("C1-1:	"+tableFlg+"	"+decos.containsKey("table")+"	"+decos.containsKey("table0"));
            if(decos.containsKey("table0") || Mobile_HTML5C2.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	table0Flg = true;
            //else	table0Flg=false;
            if(decos.containsKey("table") || Mobile_HTML5C2.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || table0Flg)	tableFlg=true;
//            if(decos.containsKey("table") || HTMLC1.tableFlg || HTMLC2.tableFlg || HTMLG1.tableFlg || HTMLG2.tableFlg || table0Flg)	tableFlg=true;
//            if(decos.containsKey("table") || table0Flg)	tableFlg=true;
//            Log.i("C1:	decos = "+decos+"		"+HTMLC1.tableFlg+" "+HTMLC2.tableFlg+"	tableFlg = "+tableFlg+"	divFlg = "+decos.containsKey("div"));
            if(decos.containsKey("div")){
        	//if(decos.containsKey("div") && !HTMLC2.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg){
            //if((decos.containsKey("div") && !HTMLC1.tableFlg && !HTMLC2.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg)
            //		|| HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
//            if(decos.containsKey("div") || HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
//            if(decos.containsKey("div") || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
        		divFlg = true;
        		tableFlg = false;
        	}//else divFlg = false;
        	
            ITFE tfe = (ITFE) tfes.get(i);
//            Log.e(tfes.contain_itemnum());
            
            //20130309
            //Count = ( ((gridInt>=jj)&&(!HTMLG1.G1Flg))? jj:gridInt );
            Count = ( (gridInt>=jj)? jj:gridInt );
//            Log.info("☆C1☆"+gridInt+" "+ii+" "+jj+"  "+Count+"  "+Count%5+"	"+HTMLG1.G1Flg+"	"+
//            ((!HTMLC3.C3Flg)?"":HTMLC3.C3Flg));
            
            //Count %= 5;	//commented out 20130924
            
            //gridInt %= 5;
            //html_env.code.append("\n<div class=\"ui-block-"+gridString[gridInt]+"\">\n");	//20130309
           	//if((!HTMLG1.G1Flg && !tableFlg) || divFlg)		//20130326  div
            if(/* !HTMLG1.G1Flg  && */ !tableFlg){	//div
            	//20131002
            	int tfesItemNum = tfes.size();
            	String divWidth = Mobile_HTML5.getDivWidth(decos, tfesItemNum - Mobile_HTML5Function.func_null_count);	//null()
//            	Log.e(tfes.contain_itemnum()+" "+tfes.size()+" "+tfes);
//            	tfe.addDeco("width", Mobile_HTML5Env.divWidth);	//☆HTMLEnvで行うように変更した <= この方法は、widthが上書き？されるためNG
            	
            	html_env.code.append("\n<div class=\"ui-block "+Mobile_HTML5Env.getClassID(tfe)+"\" style=\"width:"+divWidth+"\">\n");	//20130309
//            	if(Count!=0)	html_env.code.append("\n<div class=\"ui-block "+Mobile_HTML5Env.getClassID(tfe)+"\" style=\"width:"+divWidth+"\">\n");	//20130309
//            	else			html_env.code.append("\n<div class=\"ui-block "+Mobile_HTML5Env.getClassID(tfe)+"\" style=\"width:"+divWidth+" clear:left;\">\n");	//20130309

//            	float divWidth0 = (float)Math.floor((double)(100.0/(Count))* 1000) / 1000;
//            	String style0 = "style=\"width:"+divWidth0+"%;\"";
            	//html_env.code.append("\n<div class=\"ui-block "+HTMLEnv.getClassID(tfe)+"\" style=\"width:"+divWidth0+";\">\n");	//20130309
//            	html_env.code.append("\n<div class=\"ui-block-"+gridString[Count]+" "+HTMLEnv.getClassID(tfe)+"\">\n");	//20130309
            }
            	
            //20130314  table
        	if(tableFlg){
        		//html_env.code.append("<TD align=\"center\" valign=\"middle\" class=\""
        		html_env.code.append("<TD valign=\"middle\" class=\"" + Mobile_HTML5Env.getClassID(tfe) + " nest\">\n");
        	}
            String classid = Mobile_HTML5Env.getClassID(tfe);
            

//	      	if(Mobile_HTML5Env.dynamicFlg){	//20130529 dynamic
//	      		//☆★
//	      		Log.info("☆★C1 tfe : " + tfe);
//	    		//☆★            Log.info("C1 tfe : " + tfe);
//	            //☆★
//	      		Log.info("	C1 tfes : " + this.tfes);
//	            //☆★
//	      		Log.info("	C1 tfeItems : " + this.tfeItems);
//	      	}
	      	
	      	//20130914  "text"
//	      	Log.e("	decosC2 = "+decos);
	        if(decos.containsKey("text")){
	        	Log.e("	C1 in!");
	        	Mobile_HTML5Function.textFlg2 = true;
	        }
	        
	        Mobile_HTML5.whileProcess1(getSymbol(), decos, html_env, data, data_info, tfe, tfes, tfeItems);
    	    
            //Log.out("<TD class=\""
            //        + HTMLEnv.getClassID(tfe) + " nest\"> decos : " + decos);
//x            html_env.code.append("	<Table border=1 align=center valign=middle text-align=center><tr><td>\n");
            this.worknextItem();
//x            html_env.code.append("	</td></tr></Table>\n");
            //Log.info("C1-2:	"+tableFlg+"	"+decos.containsKey("table")+"	"+decos.containsKey("table0"));
           
	        Mobile_HTML5.whileProcess2(getSymbol(), decos, html_env, data, data_info, tfe, tfes, tfeItems);
            
            if(decos.containsKey("table0") || Mobile_HTML5C2.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	table0Flg = true;
            //else	table0Flg=false;
//            if(decos.containsKey("table") || HTMLC1.tableFlg || HTMLC2.tableFlg || HTMLG1.tableFlg || HTMLG2.tableFlg || table0Flg)	tableFlg=true;
            if(decos.containsKey("table") || Mobile_HTML5C2.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || table0Flg)	tableFlg=true;
//            if(decos.containsKey("table") || table0Flg)	tableFlg=true;
            if(decos.containsKey("div")){
        	//if(decos.containsKey("div") && !HTMLC2.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg){
            //if(decos.containsKey("div") && (HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg)){
            //if((decos.containsKey("div") && !HTMLC1.tableFlg && !HTMLC2.tableFlg && !HTMLG1.tableFlg && !HTMLG2.tableFlg)
            //		|| HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
//            if(decos.containsKey("div") || HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
//        	if(decos.containsKey("div") || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
        		divFlg = true;
        		tableFlg = false;
        	}//else divFlg = false;
            
//            //20130529
//            if(decos.containsKey("dynamic"))	Mobile_HTML5Env.dynamicFlg = true;
            
//        	//TODO 必要？不要？　→　おそらく不要？
//            //20130309
//        	//if((Count>1 && !HTMLG1.G1Flg && !tableFlg) || (Count>1 && divFlg)){		//20130326  div
//            if(Count>1 && /* !HTMLG1.G1Flg  && */ !tableFlg){
////            	String rep="ui-grid-"+gridString[Count-2];
//        		String rep="ui-grid-"+gridString[Count-2]+" #"+(HTMLEnv.uiGridCount-1);
//            	//Log.info("rep = "+rep+" TO "+"ui-grid-"+gridString[Count-1]+" #"+(HTMLEnv.uiGridCount));
//            	try{
//	            	html_env.code.replace(
//	            			html_env.code.lastIndexOf(rep), 
//	            			html_env.code.lastIndexOf(rep)+rep.length(),
//	            			"ui-grid-"+gridString[Count-1]+" #"+(HTMLEnv.uiGridCount++));
//	            	//Log.info("	C1 !   replaced !!");
////		            Log.info("	C1 !   rep = "+rep+"  TO  ui-grid-"+gridString[Count-1]+"	"+HTMLEnv.uiGridCount+" TO "+(++HTMLEnv.uiGridCount));
//            	}catch(Exception e){ /*Log.info("C1 Catch exception.");*/ }
//            }
            ii++;
            jj++;
            gridInt++;
            //HTMLEnv.uiGridCount++;
            
            //TODO: 20130309  HTMLEnvで「class="ui-grid-〜#HTMLEnv.uiGridCount"」の「#以下」を削除
            
            //20130309
            //20130314  table
        	if(tableFlg){
        		try{
		            if (html_env.not_written_classid.contains(classid)){
		            	html_env.code.delete(html_env.code.indexOf(classid),html_env.code.indexOf(classid)+classid.length()+1);
		            }
        		}catch (Exception e) { }
        	}
            
        	if(Mobile_HTML5Function.func_null_count<1){	//null()
	            if(/* !HTMLG1.G1Flg  && */ !tableFlg)	html_env.code.append("</div>\n");	//20130309
	        	if(tableFlg)	html_env.code.append("</TD>\n");					//20130314  table
        	}
//        	if(HTMLFunction.textFlg){					//20130914  "text"
//        		Log.e("C1 text!");
//	      		html_env.code.append(HTMLFunction.text);
//	      		HTMLFunction.text = "";
//	      		HTMLFunction.textFlg = false;
//	      	}
            //Log.out("</TD>");

            i++;
        }	// /while
        Mobile_HTML5.afterWhileProcess(getSymbol(), decos, html_env);

        
        //20130309
        if(/* !HTMLG1.G1Flg  && */ !tableFlg)	html_env.code.append("\n</DIV>\n");			//20130309
        
        //20130314  table
      	if(tableFlg){
      		html_env.code.append("</TR></TABLE>\n");	//20130309
      		tableFlg = false;//Log.i("tableFlg = false! 1");
      		table0Flg = false;			//20130325 table0
      	}
        
        //20130312 collapsible
    	if(decos.containsKey("collapse")){
        	html_env.code.append("</DIV>");
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
    	panelProcess2(decos, html_env, panelFlg);
    	
    	
//    	//20130313 header
//    	if(decos.containsKey("head")){
//    		html_env.code.append("</div>\n");
//    	}
        
        jj=0;
      	
      	if(divFlg)	divFlg = false;		//20130326  div
      	
//        if(Mobile_HTML5Env.dynamicFlg){
//        	Mobile_HTML5Env.dynamicFlg = false;		//20130529 dynamic
////        	StringBuffer buf = new StringBuffer();
////        	buf = HTMLEnv.staticBuf;
////        	Log.i("\nbuf = "+buf);
//////        	HTMLEnv.dynamicBuf.append(html_env.code.delete(0, buf.length()));
////        	HTMLEnv.dynamicBuf.insert(0,"");
//////        	HTMLEnv.dynamicBuf=HTMLEnv.staticBuf;
////        	HTMLEnv.dynamicBuf.append(html_env.code.delete(0, buf.length()));
////        	
////        	html_env.code = buf;
////        	Log.i("\n\nHTMLEnv.dynamicBuf = "+HTMLEnv.dynamicBuf);
////        	Log.i("\n\nhtml_env.code = "+html_env.code);
//        }
      	
        //Log.out("</TR></TABLE>");
        
        Mobile_HTML5.postProcess(getSymbol(), decos, html_env);	//Post-process (後処理)

        //Log.out("TFEId = " + HTMLEnv.getClassID(this));
        //html_env.append_css_def_td(HTMLEnv.getClassID(this), this.decos);
        //↑必要？不要？？ -> 不要
        
        //20131001 tableDivHeader
//        if(decos.containsKey("header") && (HTMLG2.tableDivHeader_Count1+1)%2==0)
    	if(decos.containsKey("header"))
        	html_env.code = Mobile_HTML5G2.createAndCutTableDivHeader(html_env);
		return null;
    }
    
    //20130503  Panel
    public static int panelProcess1(DecorateList decos, Mobile_HTML5Env html_env){
    	int panelFlg = 0;
    	if(decos.containsKey("panel"))			panelFlg = 1;	//left panel
	    else if(decos.containsKey("lpanel"))	panelFlg = 2;	//left panel
	    else if(decos.containsKey("rpanel"))	panelFlg = 3;	//right panel
    	if(panelFlg != 0){
       		html_env.code1 = new StringBuffer();
       		html_env.code2 = new StringBuffer();
       		
       		html_env.code1.append(html_env.code);	//code1 = code
       	}
    	return panelFlg;
    }
	//20130503  Panel
	public static boolean panelProcess2(DecorateList decos, Mobile_HTML5Env html_env, int panelFlg){
	    if(panelFlg != 0){
    		/* code1:1   code:1+2   code2:null
    		 * code2=code;			//code2:1+2
    		 * code=code1;			//code:1
    		 * Panel=code2-code;	//code1:2	*/
    		html_env.code2 = html_env.code;			//code2 = code
    		html_env.code = html_env.code1;			//code = code1
    		
    		//panel.append
    		html_env.panel.append("<div data-role=\"panel\" id=\"ssql_panel_"+html_env.panelCount+"\"  " +
    				"data-position=\""+( (panelFlg!=3)? ("left") : ("right") )+"\" " +
    				"data-display=\"reveal\" data-swipe-close=\"false\" data-dismissible=\"true\" " +
    				"data-position-fixed=\"true\" data-animate=\"true\">\n");
    		html_env.panel.append(html_env.code2.delete(0, html_env.code.length()));	//panel = (code2 - code1)
    		html_env.panel.append("<br>\n" +
    				"<a href=\"#content\" data-rel=\"close\" data-role=\"button\" data-icon=\"delete\">Close</a>\n" +
    				"</div>\n");
    		
    		//code.append
    		html_env.code.append("<a href=\"#ssql_panel_"+html_env.panelCount+"\" " +
    				"data-role=\"button\" data-icon=\"arrow-");
        	if(panelFlg == 1 && !decos.getStr("panel").equals(""))			//left panel
        		html_env.code.append("l\">\n"+decos.getStr("panel")+"\n");
        	else if(panelFlg == 2 && !decos.getStr("lpanel").equals(""))	//left panel
        		html_env.code.append("l\">\n"+decos.getStr("lpanel")+"\n");
        	else if(panelFlg == 3 && !decos.getStr("rpanel").equals(""))	//right panel
        		html_env.code.append("r\" data-iconpos=\"right\">\n"+decos.getStr("rpanel")+"\n");
        	else
        		html_env.code.append(( (panelFlg!=3)? ("l"):("r\" data-iconpos=\"right") )+"\">\n" +
        				"Open panel "+html_env.panelCount+"\n");
    		html_env.code.append("</a>");
    		html_env.panelCount++;
    		
    		return true;
    	}
    	return false;
    }

	//return <TABLE cellSpacing=\"0\" cellPadding=\"0\" border=  class=   >
    public static String getTableStartTag(Mobile_HTML5Env html_env, DecorateList decos, ITFE tfe) {
    	String s = "";
    	s += "<TABLE width=\"100%\" cellSpacing=\"0\" cellPadding=\"0\" border=\"";
		//s += "<TABLE width=\"100%\" align=\"center\" cellSpacing=\"0\" cellPadding=\"0\" border=\"";
    	//s += "<TABLE width=\"95%\" align=\"center\" cellSpacing=\"0\" cellPadding=\"0\" border=\"";
		//s += ((!table0Flg)? html_env.tableborder : "0") + "\"";
    	if(Mobile_HTML5C1.table0Flg || Mobile_HTML5C2.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)
    		s += "0" + "\"";	//20130325 table0
    	else	s += html_env.tableborder + "\"";
    	//s += html_env.tableborder + "\"";
    	s += html_env.getOutlineMode();
    	
    	s += getClassIdText(html_env, decos, tfe);
    	return s+">";
    }

    //return class=
    public static String getClassIdText(Mobile_HTML5Env html_env, DecorateList decos, ITFE tfe) {
    	String s = "";
    	if(html_env.written_classid.contains(Mobile_HTML5Env.getClassID(tfe))){
    		s += " class=\"" + Mobile_HTML5Env.getClassID(tfe);
    	}
    	if(decos.containsKey("class")){
    		if(!html_env.written_classid.contains(Mobile_HTML5Env.getClassID(tfe))){
    			s += " class=\"";
    		}else{
    			s += " ";
    		}
    		s += decos.getStr("class")+"\" ";
    	}else if(html_env.written_classid.contains(Mobile_HTML5Env.getClassID(tfe))){
    		s += "\" ";
    	}
    	return s;
    }
    
    
    public String getSymbol() {
    	return "Mobile_HTML5C1";
    }
}