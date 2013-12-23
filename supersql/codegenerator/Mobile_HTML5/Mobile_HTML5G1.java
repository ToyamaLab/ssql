package supersql.codegenerator.Mobile_HTML5;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class Mobile_HTML5G1 extends Grouper {

    Manager manager;

    Mobile_HTML5Env html_env;
    Mobile_HTML5Env html_env2;
    
    //20130309
    static int gridInt = 0;
//    String[] gridString = {"a","b","c","d","e"};
    static int ii =0, jj = 0, Count = 0;
    //int ii =0, jj = 0, Count = 0;
    static boolean G1Flg=false;
    int numberOfColumns = 0;		//1行ごとのカラム数 (range: 2〜)
    int table_column_num = 0;		//20130917  [ ],10@{table}
    
    static boolean tableFlg = false;		//20130314  table
    static boolean table0Flg = false;		//20130325  table0
    static boolean divFlg = false;			//20130326  div

    //added by goto 20130413  "row Prev/Next"
    int j = 1;
    int row = 1;		//1ページごとの行数指定 (Default: 1, range: 1〜)
    int rowNum = 0;
    //static int rowFileNum = 1;
    boolean rowFlg = false;
    StringBuffer codeBuf = new StringBuffer();
    
    static String classid = "" ;
    
    //���󥹥ȥ饯��
    public Mobile_HTML5G1(Manager manager, Mobile_HTML5Env henv, Mobile_HTML5Env henv2) {
        this.manager = manager;
        this.html_env = henv;
        this.html_env2 = henv2;
  
    }

    //G1��work�᥽�å�
    @Override
	public String work(ExtList data_info) {
    	Mobile_HTML5.preProcess(getSymbol(), decos, html_env);	//Pre-process (前処理)
    	
    	//20131001 tableDivHeader
    	Mobile_HTML5G2.tableDivHeader = "";	
    	Mobile_HTML5G2.tableDivHeader_codeBuf = "";
    	Mobile_HTML5G2.tableDivHeader_Count1 = 0;
    	Mobile_HTML5G2.tableDivHeader_Count2 = 0;
    	
        int panelFlg = 0;	//20130503  Panel
        
        //1行ごとのカラム数 (range: 2〜)
        boolean columnFlg = false;
    	if(tableFlg)	numberOfColumns = -1;	//@{table}時のDefault	//20130917  [ ],10@{table}
    	else			numberOfColumns = data_info.contain_itemnum();	//div
    	if(decos.containsKey("column") && !Mobile_HTML5.dynamicDisplay){
        	try{
            	numberOfColumns = Integer.parseInt(decos.getStr("column").replace("\"", ""));
            	if(numberOfColumns<2){
            		Log.err("<<Warning>> column指定の範囲は、2〜です。指定された「column="+numberOfColumns+"」は使用できません。");
	            	if(tableFlg)	numberOfColumns = -1;							//20130917  [ ],10@{table}
	            	else			numberOfColumns = data_info.contain_itemnum();	//div
            	}else columnFlg = true;
        	}catch(Exception e){ }
        }
        
    	//added by goto 20130413  "row Prev/Next"
    	//1ページごとの行数指定 (Default: 1, range: 1〜)
    	String parentfile = null;
        String parentnextbackfile = null;
        StringBuffer parentcode = null;
        StringBuffer parentcss = null;
        StringBuffer parentheader = null;
        StringBuffer parentfooter = null;
        if(decos.containsKey("row") && columnFlg && !Mobile_HTML5.dynamicDisplay){
        	row = Integer.parseInt(decos.getStr("row").replace("\"", ""));
        	if(row<1){	//範囲外のとき
        		Log.err("<<Warning>> row指定の範囲は、1〜です。指定された「row="+row+"」は使用できません。");
        	}else{
            	parentfile = html_env.filename;
                parentnextbackfile = html_env.nextbackfile;
                parentcode = html_env.code;
                parentcss = html_env.css;
                parentheader = html_env.header;
                parentfooter = html_env.footer;
    	        html_env.css = new StringBuffer();
    	        html_env.header = new StringBuffer();
    	        html_env.footer = new StringBuffer();
            	rowFlg = true;
        	}
        }
    	
        Log.out("------- G1 -------");
        this.setDataList(data_info);
        
        //tk start///////////////////////////////////////////////////
        html_env.append_css_def_td(Mobile_HTML5Env.getClassID(this), this.decos);
        
        //20130309
        G1Flg=true;
        
        //☆重要★
        gridInt = 0;
    	//HTMLG1.jj = 0;
    	//HTMLG1.gridInt = 0;
        
        //20130325  table0
        if(decos.containsKey("table0"))	table0Flg = true;
        else							table0Flg = false;
    	//20130314  table
        //if(decos.containsKey("table") || !decos.containsKey("div") || table0Flg){
//        if(decos.containsKey("table") || table0Flg){
        if(decos.containsKey("table") || table0Flg || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G2.tableFlg){
    	//if(decos.containsKey("table") || HTMLC1.tableFlg || HTMLC2.tableFlg){
//    		Log.info("C1!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//    		Log.info("C1 tableFlg = true !!");
    		tableFlg = true;
    	}//else	tableFlg = false;

        //20130326  div
    	//if(decos.containsKey("div") || HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
        if(decos.containsKey("div")){
    		divFlg = true;
    		tableFlg = false;
    	}//else divFlg = false;
        
//        //20130529
//        if(decos.containsKey("dynamic")){
//        	if(!Mobile_HTML5Env.dynamicFlg)	Mobile_HTML5Env.staticBuf = html_env.code;
//        	Mobile_HTML5Env.dynamicFlg = true;
//        	Log.i("※G1 HTMLEnv.staticBuf: "+Mobile_HTML5Env.staticBuf);
//        }
        
        //20130914  "text"
//      	Log.e("	decosC2 = "+decos);
        if(decos.containsKey("text")){
//        	Log.e("	G2in!");
        	Mobile_HTML5Function.textFlg2 = true;
        }
        
        if(!GlobalEnv.isOpt()){
        	//20130503  Panel
    	    panelFlg = Mobile_HTML5C1.panelProcess1(decos, html_env);
        	
//        	//20130205
//        	html_env.code.append("<div id=\"fisheye\" class=\"fisheye\">\n" +
//        			"<div class=\"fisheyeContainter\">\n");
        	
        	//20130330 tab
        	//tab1
        	if(decos.containsKey("tab1")){
//        		//,で結合(水平結合)した際
//        		//replace: 不要な「<div class=〜」をカット
//    			String[] s = {"a","b","c","d","e"};
//    			int j=0;
//    			while(!HTMLManager.replaceCode(html_env, "<div class=\"ui-block-"+s[j]+" "+HTMLEnv.getClassID(this)+"\">", "")){
//    				j++;
//    				if(j>4) break;
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
    	        		Mobile_HTML5Manager.replaceCode(html_env, "<div class=\""+Mobile_HTML5Env.getClassID(this)+" \">", "");
    	        		//String cutClass="class=\""+HTMLEnv.getClassID(this)+" \"";
    	        		//if(!HTMLManager.replaceCode(html_env, "<div "+cutClass+">", ""))	cutClass="";
//    	        		if(!HTMLManager.replaceCode(html_env, "<div class=\""+HTMLEnv.getClassID(this)+" \">", "")){
//    	        			//Log.info("Cannot cut. "+HTMLEnv.getClassID(this));
//    	        			String[] s = {"a","b","c","d","e"};
//    	        			int j=0;
//    	        			while(!HTMLManager.replaceCode(html_env, "<div class=\"ui-block-"+s[j]+" "+HTMLEnv.getClassID(this)+"\">", "")){
//    	        				//,で結合(水平結合)した際に、このwhileに入る（レアケース）
//    	        				j++;
//    	        				if(j>4) break;
//    	        			}
//    	        		}
    	            	
    	        		html_env.code.append("<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
    	        		////上記でカットしたcutClassをappend
    	            	//html_env.code.append("<div id=\"tabs-"+HTMLEnv.tabCount+"\" "+cutClass+">\n");
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
//        	else{
            //20130309
//        	//uncommented
        	//if(!tableFlg) 	html_env.code.append("	<DIV Class=\"ui-grid-a\">");
        	if(!tableFlg){
        		if(html_env.written_classid.contains(Mobile_HTML5Env.getClassID(this)))
        			html_env.code.append("\n<DIV Class=\"ui-grid ##"+Mobile_HTML5Env.uiGridCount2+" "+Mobile_HTML5Env.getClassID(this)+"\"");
//	        		html_env.code.append("\n<DIV Class=\"ui-grid-a ##"+HTMLEnv.uiGridCount2+" "+HTMLEnv.getClassID(this)+"\"");
        		else
        			html_env.code.append("\n<DIV Class=\"ui-grid ##"+Mobile_HTML5Env.uiGridCount2+"\"");
//        			html_env.code.append("\n<DIV Class=\"ui-grid-a ##"+HTMLEnv.uiGridCount2+"\"");
        		html_env.code.append(">\n");
        		Mobile_HTML5Env.uiGridCount2++;
        	}
            
            //20130314  table
            if(tableFlg){
            	//added by goto 20130318  横スクロール
            	if(numberOfColumns < 0)	html_env.code.append("<div style=\"overflow:auto;\">\n");	//20130917  [ ],10@{table}
            	//html_env.code.append("<div style=\"height:60px; width:0px; overflow:auto;\">\n");
            	
            	if(row>1 && tableFlg)	Mobile_HTML5G2.tableStartTag = Mobile_HTML5C1.getTableStartTag(html_env, decos, this)+"<TR>";
            	else					html_env.code.append(Mobile_HTML5C1.getTableStartTag(html_env, decos, this)+"<TR>");
            }
            
        }
//        Log.out("<TABLE class=\""+HTMLEnv.getClassID(this) + "\"><TR>");

        //html_env2.code.append("<tfe type=\"connect\" dimension=\"1\" >");
        
        Mobile_HTML5.beforeWhileProcess(getSymbol(), decos, html_env);
        int i = 0;
        while (this.hasMoreItems()) {
        	Mobile_HTML5Function.glvl = html_env.glevel;	//added by goto 20130914  "SEQ_NUM"
        	
            if(decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5C2.table0Flg || Mobile_HTML5G2.table0Flg)	table0Flg = true;
            if(decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G2.tableFlg || table0Flg)	tableFlg=true;
            //if(decos.containsKey("div") || HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
            if(decos.containsKey("div")){
        		divFlg = true;
        		tableFlg = false;
        	}
        	
            html_env.glevel++;
            
            //added by goto 20130413  "row Prev/Next"
            if(rowFlg){
            	html_env.code = new StringBuffer();
            	html_env.countfile++;
                html_env.filename = html_env.outfile + "_row" + Mobile_HTML5G2.rowFileNum + "_" + j + ".html";
                html_env.nextbackfile = html_env.linkoutfile + "_row" + Mobile_HTML5G2.rowFileNum + "_" + j + ".html";
                html_env.setOutlineMode();
            }
            
//            //おそらくXML
//            if(GlobalEnv.isOpt()){
//	            html_env2.code.append("<tfe type=\"repeat\" dimension=\"1\"");
//	            
//	            if(decos.containsKey("class")){
//		        	//class=menu�Ȃǂ̎w�肪��������t��
//	            	html_env2.code.append(" class=\"");
//		        	html_env2.code.append(decos.getStr("class") + " ");
//		        }
//	            if(html_env.written_classid.contains(HTMLEnv.getClassID(this))){
//		        	//TFE10000�Ȃǂ̎w�肪��������t��
//	            	if(decos.containsKey("class")){
//	            		html_env2.code.append(HTMLEnv.getClassID(this) + "\"");
//	            	}else{
//	            		html_env2.code.append(" class=\""
//	            				+ HTMLEnv.getClassID(this) + "\"");
//	            	}
//	            }else if(decos.containsKey("class")){
//	            	html_env2.code.append("\"");
//	            }
//	            
//	            html_env2.code.append(" border=\"" + html_env.tableborder + "\"");
//	
//	            if (decos.containsKey("tablealign") )
//	            	html_env2.code.append(" align=\"" + decos.getStr("tablealign") +"\"");
//	            if (decos.containsKey("tablevalign") )
//	            	html_env2.code.append(" valign=\"" + decos.getStr("tablevalign") +"\"");
//	            
//	            if(decos.containsKey("tabletype")){
//	            	html_env2.code.append(" tabletype=\"" + decos.getStr("tabletype") + "\"");
//	            	if(decos.containsKey("cellspacing")){
//	                	html_env2.code.append(" cellspacing=\"" + decos.getStr("cellspacing") + "\"");
//	                }
//	            	if(decos.containsKey("cellpadding")){
//	                	html_env2.code.append(" cellpadding=\"" + decos.getStr("cellpadding") + "\"");
//	                }
//	            }
//	            html_env2.code.append(">");
//            }
            
            //20130309
//            Count = ( ((gridInt>=jj)&&(!HTMLG1.G1Flg))? jj:gridInt );
            Count = ( ((gridInt>=jj)&&(Mobile_HTML5G1.G1Flg))? jj:gridInt );
            
//            //20130312 collapsible
//        	if(decos.containsKey("collapse")){
//            	Log.info("★  [],2");
////            	html_env.code.append("	<DIV data-role=\"collapsible\">");
////            	html_env.code.append("	<h1>ヘッダ[],</h1>");
//
//            //20130309
//            //if(Count > 4 && Count%5==0){
            
//			//☆G1のみ↓　必要？不要？ 無くても問題ない。あった場合、最終行が個数に合わせて変化
//        	if(Count > pic_column-1 && Count%pic_column==0 && !tableFlg){
//        		if(html_env.written_classid.contains(HTMLEnv.getClassID(this)))
//        			html_env.code.append("\n	</DIV>\n	<DIV Class=\"ui-grid-a ##"+HTMLEnv.uiGridCount2+" "+HTMLEnv.getClassID(this)+"\"");
//        		else
//        			html_env.code.append("\n	</DIV>\n	<DIV Class=\"ui-grid-a ##"+HTMLEnv.uiGridCount2+"\"");
//        		html_env.code.append(">\n");
//        		HTMLEnv.uiGridCount2++;
//        	}
//			//☆G1のみ↑
            
            //Log.info("★G1★"+gridInt+" "+ii+" "+jj+"  "+Count+"  "+Count%5+"	"+HTMLG1.G1Flg);
//            Count %= 5;
            //Log.info("	☆★"+gridInt+" "+ii+" "+jj+"  "+Count+"  "+Count%pic_column+"	"+HTMLG1.G1Flg);
            Count %= numberOfColumns;
            
            
        	//20130917  [ ],10@{table}
            if(tableFlg && numberOfColumns > 1 && Count == 0){
            	if(table_column_num>0)	html_env.code.append("</TR><TR>\n");
            	else					table_column_num++;
            }
            
            
            //html_env.code.append("	<div>");
	        //html_env.code.append("	<div class=\"ui-block-a\">");

//            //20130312 collapsible
//        	if(decos.containsKey("collapse")){
//            	Log.info("★  [],3");
//    	        html_env.code.append("	<p>\n");
//        	}else{
        		//20130309
    	    if(!tableFlg){
    	    	//20131002
            	if(decos.containsKey("width")){
            		Mobile_HTML5Env.divWidth = decos.getStr("width");
    	    	}else{
        	    	float divWidth = (float)Math.floor((double)(100.0/numberOfColumns)* 1000) / 1000;
                	Mobile_HTML5Env.divWidth = divWidth+"%";
    	    	}
            	//tfe.addDeco("width", divWidth);	//☆HTMLEnvで行うように変更した
            	
            	if(Count!=0)	html_env.code.append("\n	<div class=\"ui-block"+" "+Mobile_HTML5Env.getClassID(tfe)+"\">\n");
    	    	else			html_env.code.append("\n	<div class=\"ui-block"+" "+Mobile_HTML5Env.getClassID(tfe)+"\" style=\"clear:left;\">\n");
            	
//    	    	float divWidth = (float)Math.floor((double)(100.0/numberOfColumns)* 1000) / 1000;
//    	    	if(Count!=0)	html_env.code.append("\n	<div class=\"ui-block"+" "+HTMLEnv.getClassID(tfe)+"\" style=\"width:"+divWidth+"%;\">\n");
//    	    	else			html_env.code.append("\n	<div class=\"ui-block"+" "+HTMLEnv.getClassID(tfe)+"\" style=\"width:"+divWidth+"%; clear:left;\">\n");
    	    }
//    	    if(!tableFlg)   html_env.code.append("\n	<div class=\"ui-block-"+gridString[Count]+" "+HTMLEnv.getClassID(tfe)+"\">\n");
    	    //20130314  table
    	    else{
	            html_env.code.append("<TD class=\"" + Mobile_HTML5Env.getClassID(tfe) + " nest\">\n");       
    	    }
    	    //String classid = HTMLEnv.getClassID(tfe);
    	    classid = Mobile_HTML5Env.getClassID(tfe);
            //classid0 = HTMLEnv.getClassID(tfe);
            	
            //Log.out("<TD class=\"" + HTMLEnv.getClassID(tfe) + " nest\">");
    	    //Log.info("tfe : " + tfe);
            //Log.info("tfe : " + this.tfes);
            //Log.info("tfe : " + this.tfeItems);
    	    
	      	
//    	    if(Mobile_HTML5Env.dynamicFlg){	//20130529 dynamic
//	      		//☆★
//	      		Log.info("★★G1-1 tfe : " + tfe);
//	    		//☆★            Log.info("G1 tfe : " + tfe);
//	            //☆★            Log.info("G1 tfes : " + this.tfes);
//	            //☆★            Log.info("G1 tfeItems : " + this.tfeItems);
//	      	}
            
	        Mobile_HTML5.whileProcess1(getSymbol(), decos, html_env, data, data_info, tfe, null, -1);
    	    
            this.worknextItem();
            
	        Mobile_HTML5.whileProcess2(getSymbol(), decos, html_env, data, data_info, tfe, null, -1);
	        
            if(decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5C2.table0Flg || Mobile_HTML5G2.table0Flg)	table0Flg = true;
            if(decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G2.tableFlg || table0Flg)	tableFlg=true;
            //if(decos.containsKey("div") || HTMLC1.divFlg || HTMLC2.divFlg || HTMLG1.divFlg || HTMLG2.divFlg){
            if(decos.containsKey("div")){
        		divFlg = true;
        		tableFlg = false;
        	}
            
//            //20130529
//            if(decos.containsKey("dynamic"))	Mobile_HTML5Env.dynamicFlg = true;
            
	      	
//    	    if(Mobile_HTML5Env.dynamicFlg){	//20130529 dynamic
//	      		//☆★
//	      		Log.info("★★G1-2 tfe : " + tfe);
//	    		//☆★            Log.info("G1 tfe : " + tfe);
//	            //☆★            Log.info("G1 tfes : " + this.tfes);
//	            //☆★            Log.info("G1 tfeItems : " + this.tfeItems);
//	      	}
            //20130309
            //20130314  table
        	if(tableFlg){
	            if (html_env.not_written_classid.contains(classid)){
	            	html_env.code.delete(html_env.code.indexOf(classid),html_env.code.indexOf(classid)+classid.length()+1);
	            }
        	}
            
            //html_env2.code.append("</tfe>");　//おそらくXML
            
//            //20130312 collapsible
//        	if(decos.containsKey("collapse")){
//            	
//            //20130309
////            if(Count>1/* && HTMLG1.G1Flg*/){
//        	}else 
            
//        	//TODO 必要？不要？　→　おそらく不要？
//            if(Count>1 && HTMLG1.G1Flg && /* !HTMLG2.G2Flg &&*/ !tableFlg){
//        		String rep="ui-grid ##"+(HTMLEnv.uiGridCount2-1);
////        		String rep="ui-grid-"+gridString[Count-2]+" ##"+(HTMLEnv.uiGridCount2-1);
//            	try{
//	            	html_env.code.replace(
//	            			html_env.code.lastIndexOf(rep), 
//	            			html_env.code.lastIndexOf(rep)+rep.length(),
//	            			"ui-grid ##"+(HTMLEnv.uiGridCount2++));
////	            			"ui-grid-"+gridString[Count-1]+" ##"+(HTMLEnv.uiGridCount2++));
//            	}catch(Exception e){ /*Log.info("G1 Catch exception.");*/ }
//            }
            ii++;
            jj++;
            gridInt++;
            //HTMLEnv.uiGridCount++;
            
//            //20130312 collapsible
//        	if(decos.containsKey("collapse")){
//            	Log.info("★  [],5");
//    	        html_env.code.append("	</p>\n");
//        	}else{

            if(!tableFlg)	html_env.code.append("	</div>");	//20130309
        	else	        html_env.code.append("</TD>\n");    //20130314 table
            //Log.out("</TD>");
//            if(HTMLFunction.textFlg){					//20130914  "text"
//	      		Log.e("G1 text!");
//	      		html_env.code.append(HTMLFunction.text);
//	      		HTMLFunction.text = "";
//	      		HTMLFunction.textFlg = false;
//	      	}
            
            i++;
            //Log.info("	html_env.glevel = "+html_env.glevel);
//            if(html_env.glevel == 0){
//            	jj = 0;
//            	gridInt = 0;
//            	//Count = 0;
//            }
            html_env.glevel--;

            Mobile_HTML5G2.tableDivHeader_Count1++;	//20131001 tableDivHeader
            
            //added by goto 20130413  "row Prev/Next"
            if(rowFlg){
            	codeBuf.append(html_env.code);
            	if((rowNum+1)%(row*numberOfColumns)==0){
	                Mobile_HTML5G2.createHTMLfile_ForPrevNext(html_env, codeBuf);
	                j++;
	                codeBuf = new StringBuffer();
                }
                rowNum++;
            }
        }	// /while
        Mobile_HTML5.afterWhileProcess(getSymbol(), decos, html_env);
        
        //added by goto 20130413  "row Prev/Next"
        if(rowFlg){
        	if(rowNum%(row*numberOfColumns)!=0){	//最後の child HTML を create
        		Mobile_HTML5G2.createHTMLfile_ForPrevNext(html_env, codeBuf);
        	}
        	//ファイル名・コード等をparent HTMLのものへ戻す
        	html_env.filename = parentfile;
        	html_env.code = parentcode;
            html_env.css = parentcss;
            html_env.header = parentheader;
            html_env.footer = parentfooter;
            html_env.nextbackfile = parentnextbackfile;
            Log.out("TFEId = " + Mobile_HTML5Env.getClassID(this));
            html_env.append_css_def_td(Mobile_HTML5Env.getClassID(this), this.decos);
            
            int first = 1, last = ((rowNum%(row*numberOfColumns)!=0)? (rowNum/(row*numberOfColumns)+1):(rowNum/(row*numberOfColumns)));	//for G1
            Mobile_HTML5G2.PrevNextProcess(html_env, rowNum, row, first, last, numberOfColumns);
        }
        
        
////        //TOOD 必要？不要？　→　不要のような気がするけど？？？　→　あると余計な部分が消されることがあるので無い方が良い(2013.09.26)
////        //[重要] For [ [], ]! || [],
////        //[],内が1つの値のみだったとき -> 直近のui-grid-aとui-block-aをカット
//    	if(HTMLG1.gridInt == 1){
//        //if(HTMLG1.jj == 1 && HTMLG1.gridInt == 1 /*&& HTMLG2.G2Flg*/){
//        	//Log.i("			HTMLG1.jj = "+HTMLG1.jj+"	HTMLG1.gridInt = "+HTMLG1.gridInt+"		HTMLG1.classid = "+HTMLG1.classid+"		!!");
//        	//Log.i("			"+HTMLManager.replaceCode(html_env, "ui-grid-a ##"+(HTMLEnv.uiGridCount2-1), "##"+(HTMLEnv.uiGridCount2-1)));
//        	HTMLManager.replaceCode(html_env, "ui-grid ##"+(HTMLEnv.uiGridCount2-1), "##"+(HTMLEnv.uiGridCount2-1));	//TODO
////        	HTMLManager.replaceCode(html_env, "ui-grid-a ##"+(HTMLEnv.uiGridCount2-1), "##"+(HTMLEnv.uiGridCount2-1));
//    		HTMLManager.replaceCode(html_env, "ui-block "+HTMLG1.classid, "### "+HTMLG1.classid);	//TODO
////    		HTMLManager.replaceCode(html_env, "ui-block-a "+HTMLG1.classid, "### "+HTMLG1.classid);
//    	}

//        Log.i("	"+jj+"	"+gridInt);
    	/* 
//    		//,で結合(水平結合)した際
//    		//replace: 不要な「<div class=〜」をカット
//			String[] s = {"a","b","c","d","e"};
//			int j=0;
//			while(!HTMLManager.replaceCode(html_env, "<div class=\"ui-block-"+s[j]+" "+HTMLEnv.getClassID(this)+"\">", "")){
//				j++;
//				if(j>4) break;
//			}
    	 */
        
        if(Mobile_HTML5Env.getFormItemFlg()){		
	        Mobile_HTML5Env.incrementFormPartsNumber();
		}

        //html_env2.code.append("</tfe>");
        
        if(!tableFlg)	html_env.code.append("\n</DIV>\n");			//20130309
        else{
        	if(!(row>1 && tableFlg))	html_env.code.append("</TR></TABLE>\n");	//20130314  table
        	tableFlg = false;
        	table0Flg = false;		//20130325 table0
        	if(numberOfColumns < 0)	html_env.code.append("</div>\n");	//added by goto 20130318  横スクロール		//20130917  [ ],10@{table}
        }
        
        if(divFlg)	divFlg = false;		//20130326  div
        
//        if(Mobile_HTML5Env.dynamicFlg)	Mobile_HTML5Env.dynamicFlg = false;		//20130529 dynamic
        
        
        G1Flg=false;
        Log.out("</TR></TABLE>");
        
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
    	
    	//added by goto 20130413  "row Prev/Next"
        if(rowFlg){
        	Mobile_HTML5G2.rowFileNum++;
        	rowFlg = false;
        	Mobile_HTML5G2.tableStartTag = "";
        }
        
        Mobile_HTML5.postProcess(getSymbol(), decos, html_env);	//Post-process (後処理)
        
        //added by goto 20130914  "SEQ_NUM"
        Mobile_HTML5Function.Func_seq_num_initialization(html_env.glevel);

        Log.out("TFEId = " + Mobile_HTML5Env.getClassID(this));
        //html_env.append_css_def_td(HTMLEnv.getClassID(this), this.decos);
		return null;
    }

    @Override
	public String getSymbol() {
        return "Mobile_HTML5G1";
    }

}