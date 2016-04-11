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
    
    public static int G1_count = 0;
    
    //20130309
    static int gridInt = 0;
    static int ii =0, jj = 0, Count = 0;
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
    	G1_count++;
    	Mobile_HTML5Attribute.attributeHasWidth = false;
    	
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
    	else			numberOfColumns = data_info.size();	//div
    	if(decos.containsKey("column") && !Mobile_HTML5.dynamicDisplay){
        	try{
            	numberOfColumns = Integer.parseInt(decos.getStr("column").replace("\"", ""));
            	if(numberOfColumns<2){
            		Log.err("<<Warning>> column指定の範囲は、2〜です。指定された「column="+numberOfColumns+"」は使用できません。");
	            	if(tableFlg)	numberOfColumns = -1;							//20130917  [ ],10@{table}
	            	else			numberOfColumns = data_info.size();	//div
            	}else columnFlg = true;
        	}catch(Exception e){ }
        }
        
    	//added by goto 20130413  "row Prev/Next"
    	//1ページごとの行数指定 (Default: 1, range: 1〜)
    	String parentfile = null;
        String parentnextbackfile = null;
        StringBuffer parentcode = null;
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
                parentheader = html_env.header;
                parentfooter = html_env.footer;
    	        html_env.header = new StringBuffer();
    	        html_env.footer = new StringBuffer();
            	rowFlg = true;
        	}
        }
    	
        Log.out("------- G1 -------");
        this.setDataList(data_info);
        
        String classid = Mobile_HTML5Env.getClassID(this);
        String classid2 = Mobile_HTML5Env.getClassID(tfe);
        html_env.append_css_def_td(classid, this.decos);
        
        //20130309
        G1Flg=true;
        
        //☆重要★
        gridInt = 0;
        
        //20130325  table0
        if(decos.containsKey("table0"))	table0Flg = true;
        else							table0Flg = false;
    	//20130314  table
        if(decos.containsKey("table") || table0Flg || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G2.tableFlg){
    		tableFlg = true;
    	}//else	tableFlg = false;

        //20130326  div
        if(decos.containsKey("div")){
    		divFlg = true;
    		tableFlg = false;
    	}//else divFlg = false;
        
        //20130914  "text"
        if(decos.containsKey("text")){
        	Mobile_HTML5Function.textFlg2 = true;
        }
        
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
    	        		Mobile_HTML5Manager.replaceCode(html_env, "<div class=\""+classid+" \">", "");
    	            	
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
        	if(!tableFlg){
//        		if(html_env.written_classid.contains(classid))
        			html_env.code.append("\n<DIV Class=\"ui-grid ##"+Mobile_HTML5Env.uiGridCount2+" "+classid+"\"");
//        		else
//        			html_env.code.append("\n<DIV Class=\"ui-grid ##"+Mobile_HTML5Env.uiGridCount2+"\"");
        		html_env.code.append(">\n");
        		Mobile_HTML5Env.uiGridCount2++;
        	}
            
            //20130314  table
            if(tableFlg){
            	//added by goto 20130318  横スクロール
            	if(numberOfColumns < 0)	html_env.code.append("<div style=\"overflow:auto;\">\n");	//20130917  [ ],10@{table}
            	
            	if(row>1 && tableFlg)	Mobile_HTML5G2.tableStartTag = Mobile_HTML5C1.getTableStartTag(html_env, decos, this)+"<TR>";
            	else					html_env.code.append(Mobile_HTML5C1.getTableStartTag(html_env, decos, this)+"<TR>");
            }
            
        }
        
        Mobile_HTML5.beforeWhileProcess(getSymbol(), decos, html_env);
        while (this.hasMoreItems()) {
        	Mobile_HTML5Function.glvl = html_env.glevel;	//added by goto 20130914  "SEQ_NUM"
        	
            if(decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5C2.table0Flg || Mobile_HTML5G2.table0Flg)	table0Flg = true;
            if(decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G2.tableFlg || table0Flg)	tableFlg=true;
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
            
            
            //20130309
            Count = ( ((gridInt>=jj)&&(Mobile_HTML5G1.G1Flg))? jj:gridInt );
            Count %= numberOfColumns;
            
            
        	//20130917  [ ],10@{table}
            if(tableFlg && numberOfColumns > 1 && Count == 0){
            	if(table_column_num>0)	html_env.code.append("</TR><TR>\n");
            	else					table_column_num++;
            }
            
    		//20130309
    	    if(!tableFlg){
    	    	//20131002
    	    	//TODO style=を.cssへ
    	    	String divWidth = Mobile_HTML5.getDivWidth("G1", decos, numberOfColumns - Mobile_HTML5Function.func_null_count);	//null()
    	    	if(!decos.containsKey("column")){
//    	    		if(Count!=0)	html_env.code.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1_count+"\" style=\"width:"+divWidth+"\">\n");
//    	    		else			html_env.code.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1_count+"\" style=\"width:"+divWidth+"; clear:left;\">\n");
//    	    		if(!decos.containsKey("width")){
    	    			if(Count!=0)	html_env.code.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1_count+"\">\n");
    	    			else			html_env.code.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1_count+"\" style=\"clear:left;\">\n");
    	    			if(gridInt<1)	Mobile_HTML5Attribute.attributeDivWidth2 += "."+classid2+"-"+G1_count+"{ width:"+divWidth+"; }\n";
//    	    		}else {
//    	    			if(Count!=0)	html_env.code.append("\n<div class=\"ui-block"+" "+classid2+"\">\n");
//    	    			else			html_env.code.append("\n<div class=\"ui-block"+" "+classid2+"\" style=\"clear:left;\">\n");
//					}
    	    	}else{
    	    		if(Count!=0)	html_env.code.append("\n<div class=\"ui-block"+" "+classid2+"\">\n");
    	    		else			html_env.code.append("\n<div class=\"ui-block"+" "+classid2+"\" style=\"clear:left;\">\n");
    	    		Mobile_HTML5Attribute.attributeDivWidth = divWidth;
    	    	}
    	    	//System.out.println(this.decos+ " "+ divWidth+" "+gridInt);
    	    }
    	    //20130314  table
    	    else{
	            html_env.code.append("<TD class=\"" + classid2 + " nest\">\n");       
    	    }
    	    classid = classid2;
            	
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
            if(decos.containsKey("div")){
        		divFlg = true;
        		tableFlg = false;
        	}
            
            ii++;
            jj++;
            gridInt++;
            

            if(!tableFlg)	html_env.code.append("</div>");	//20130309
        	else	        html_env.code.append("</TD>\n");    //20130314 table
            
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
        Mobile_HTML5.afterWhileProcess(getSymbol(), classid2, decos, html_env);
        
        //added by goto 20130413  "row Prev/Next"
        if(rowFlg){
        	if(rowNum%(row*numberOfColumns)!=0){	//最後の child HTML を create
        		Mobile_HTML5G2.createHTMLfile_ForPrevNext(html_env, codeBuf);
        	}
        	//ファイル名・コード等をparent HTMLのものへ戻す
        	html_env.filename = parentfile;
        	html_env.code = parentcode;
            html_env.header = parentheader;
            html_env.footer = parentfooter;
            html_env.nextbackfile = parentnextbackfile;
            Log.out("TFEId = " + classid);
            html_env.append_css_def_td(classid, this.decos);
            
            int first = 1, last = ((rowNum%(row*numberOfColumns)!=0)? (rowNum/(row*numberOfColumns)+1):(rowNum/(row*numberOfColumns)));	//for G1
            Mobile_HTML5G2.PrevNextProcess(html_env, rowNum, row, first, last, numberOfColumns);
        }
        
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

        if(!tableFlg)	html_env.code.append("\n</DIV>\n");			//20130309
        else{
        	if(!(row>1 && tableFlg))	html_env.code.append("</TR></TABLE>\n");	//20130314  table
        	tableFlg = false;
        	table0Flg = false;		//20130325 table0
        	if(numberOfColumns < 0)	html_env.code.append("</div>\n");	//added by goto 20130318  横スクロール		//20130917  [ ],10@{table}
        }
        
        if(divFlg)	divFlg = false;		//20130326  div
        
        G1Flg=false;
        Log.out("</TR></TABLE>");
        
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
    	
    	//added by goto 20130413  "row Prev/Next"
        if(rowFlg){
        	Mobile_HTML5G2.rowFileNum++;
        	rowFlg = false;
        	Mobile_HTML5G2.tableStartTag = "";
        }
        
        Mobile_HTML5.postProcess(getSymbol(), classid2, decos, html_env);	//Post-process (後処理)
        
        //added by goto 20130914  "SEQ_NUM"
        Mobile_HTML5Function.Func_seq_num_initialization(html_env.glevel);

        Log.out("TFEId = " + classid);
		return null;
    }

    @Override
	public String getSymbol() {
        return "Mobile_HTML5G1";
    }

}