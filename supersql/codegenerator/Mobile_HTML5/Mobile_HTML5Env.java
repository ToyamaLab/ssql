package supersql.codegenerator.Mobile_HTML5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

import supersql.codegenerator.Connector;
import supersql.codegenerator.DecorateList;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.LocalEnv;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.parser.SSQLparser;

public class Mobile_HTML5Env extends LocalEnv {
    String data;

    String pre_operator;

    Vector written_classid;

    Connector connector;
    
    String title = "";		//added by goto 20130411  "title"
    String bg = "";			//added by goto 20130311  "background"
//    int maxWidth = 350;		//added by goto 20130512  "max-width"	Default:350
    int portraitWidth = -1;		//added by goto 20130512  "max-width"	Default:-1
    int landscapeWidth = -1;	//added by goto 20130512  "max-width"	Default:-1
    int pcWidth = -1;			//added by goto 20130512  "max-width"	Default:-1
    
    public static String 	//added by goto 20130515  "search"
	    PHP = "<?php\n" +	//初期定義
	    		"//XSS対策\n" +
	    		"function checkHTMLsc($str){\n" +
	    		"	return htmlspecialchars($str, ENT_QUOTES, 'UTF-8');\n" +
	    		"}\n" +
	    		"?>\n";
    
//    public static String 				//added by goto 20130531 
//    	PHPpost = "<?php\n" +	//初期定義(dummy if)
//	    		"//dummy\n" +
//	    		"if($_POST['dummy']){\n" +
//				"	;\n" +
//				"}\n";
//    public static String PHPfunc = "";	//added by goto 20130531 
    
//    static boolean dynamicFlg = false;			//20130529  dynamic
//    static StringBuffer staticBuf;				//20130529  dynamic
//    static StringBuffer dynamicBuf;				//20130529  dynamic
    
    //Vector not_written_classid;
    Vector<String> not_written_classid= new Vector();

    int total_element = 0;

    int glevel = 0;

    String filename;

    String outfile;

    String linkoutfile;

    String nextbackfile = new String();

    String outdir;

    int countfile;

    PrintWriter writer;

    StringBuffer code;

    StringBuffer css;

	static int ID_counter=0;	//add oka

	static int ID_old=0;		//add oka
	
	String charset=null;					//added by goto 20120715
	static boolean charsetFlg=false;		//added by goto 20120715

	String copyright="";					//added by goto 20130518
	String fff = "";						//20130518  "show query"

	Boolean flickBarFlg = false;				//20130521  "flickbar"
	
    //tk start///////////////////////////////////////////////////
    StringBuffer meta = new StringBuffer();
    StringBuffer div = new StringBuffer();
    //StringBuffer title = new StringBuffer();		//disuse
    StringBuffer titleclass = new StringBuffer();
    StringBuffer cssfile = new StringBuffer();
	StringBuffer jsFile = new StringBuffer();		//added by goto 20130703
	StringBuffer cssjsFile = new StringBuffer();	//added by goto 20130703
    String tableborder=new String("1");
    boolean embedflag = false;
    int embedcount = 0;

    int haveClass = 0;

    //for ajax
    String ajaxquery = new String();
    String ajaxcond = new String();
    String ajaxatt = new String();
    String ajaxtarget = new String();
    int inEffect = 0;
    int outEffect = 0;
    boolean has_dispdiv = false;

    //for drag
    StringBuffer script = new StringBuffer();
    int scriptnum = 0;
    boolean draggable = false;
    String dragdivid = new String();

    //for panel
    boolean isPanel = false;
    //tk end//////////////////////////////////////////////////////

    StringBuffer header;

    StringBuffer footer;

    //20130503  Panel
    StringBuffer code1;
    StringBuffer code2;
    StringBuffer panel = new StringBuffer();
    int panelCount = 1;

    boolean foreach_flag;

    boolean sinvoke_flag = false;

    int link_flag;

    String linkurl;
    
    static int uiGridCount = 0;		//20130314  C1 ui-Grid用
    static int uiGridCount2 = 0;	//20130314  G1 ui-Grid用
    //static boolean tableFlg = false;		//20130314  table
    
    static int tabCount = 1;			//20130330  tab用
    //static boolean tabFlg = false;		//20130330  tab用
    static int maxTab = 15;				//20130330  tab用
    
	static String divWidth = "";		//20131002
	
	static boolean noAd = false;		//20131106
    

    // ��?�Ѥ�CSS CLASS����?��?
    private String KeisenMode = "";

    public Mobile_HTML5Env() {
    }

    public String getEncode(){
    	if(getOs().contains("Windows")){
        	return "Shift_JIS";
    	}else{
    		return "EUC_JP";
    	}
    }

    public String getOs(){
    	String osname = System.getProperty("os.name");
    	return osname;
    }

    public void getHeader(int headerFlag) {		//[headerFlag] 1:通常、2:Prev/Next
   		int index = 0;
   		if(GlobalEnv.getframeworklist() == null){
	        header.insert(index,"<HEAD>\n");
	        header.insert(index,"<HTML>\n");
	        Log.out("<HTML>");
	        Log.out("<head>");
	        
	        //added by goto 20130508  "Login&Logout"
	        if(SSQLparser.sessionFlag){
	        	//header.insert(index,"<?php\n	session_start();\n	session_regenerate_id(TRUE);\n?>\n");
	        	String s = "<?php\n	session_start();\n";
	        	if(headerFlag == 1)	s += "	session_regenerate_id(TRUE);\n";
	        	s += "?>\n";
	        	header.insert(index, s);
	        }
	        
	        //Generator
	        header.append("<meta name=\"GENERATOR\" content=\" SuperSQL (Generate Mobile_HTML5) \">\n");
	        
	        //added by goto 20120629
	        //header.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
	        
	        //changed by goto 20120622 start
	        //Log.out("<style type=text/css><!--");
   		}
        /*commonCSS();
        header.append(css);
        Log.out(css.toString());

        if(GlobalEnv.getframeworklist() == null)
        	header.append("--></STYLE>");*/
   		//changed by goto 20120622 end

        //tk start////////////////////////////////////////////////////
        header.append(meta);

        if(GlobalEnv.isAjax())
        {
        	String js = GlobalEnv.getJsDirectory();
        	if(js != null)
        	{
        		if(js.endsWith("/"))
        			js = js.substring(0,js.lastIndexOf("/"));

	        	header.append("<script src=\""+js+"/prototype.js\" type=\"text/javascript\"></script>\n");
	        	header.append("<script src=\""+js+"/ajax.js\" type=\"text/javascript\"></script>");

        	}
        	else
        	{
	        	header.append("<script src=\"http://localhost:8080/tab/prototype.js\" type=\"text/javascript\"></script>\n");
	        	header.append("<script src=\"http://localhost:8080/tab/ajax.js\" type=\"text/javascript\"></script>");
        	}

            header.append("<script type=\"text/javascript\" src=\"build/yahoo/yahoo-min.js\"></script>\n");
            header.append("<script type=\"text/javascript\" src=\"build/event/event-min.js\" ></script>\n");
            header.append("<script type=\"text/javascript\" src=\"build/dom/dom-min.js\"></script>\n");
            header.append("<script type=\"text/javascript\" src=\"build/dragdrop/dragdrop-min.js\" ></script>\n");
            header.append("<script type=\"text/javascript\" src=\"ssqlajax.js\" ></script>\n");
            header.append("<script type=\"text/javascript\" src=\"prototype.js\" ></script>\n");

            //for tab
            header.append("<script type=\"text/javascript\" src=\"build/element/element-beta.js\"></script>\n");
            header.append("<script type=\"text/javascript\" src=\"build/tabview/tabview.js\"></script>\n");

            //for panel
            header.append("<script type=\"text/javascript\" src=\"build/container/container.js\"></script>\n");

            //for animation
            header.append("<script type=\"text/javascript\" src=\"build/animation/animation.js\"></script>\n");

            //for lightbox
            header.append("<script type=\"text/javascript\" src=\"js/prototype.js\"></script>\n");
            header.append("<script type=\"text/javascript\" src=\"js/scriptaculous.js?load=effects\"></script>\n");
            header.append("<script type=\"text/javascript\" src=\"js/lightbox.js\"></script>\n");

            //for tab css
            header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"build/tabview/assets/border_tabs.css\">\n");
            header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"build/tabview/assets/tabview.css\">\n");

            //for panel css
            header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"build/container/assets/container.css\">\n");
            header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"build/container/assets/container.css\">\n");

            //for lightbox css
            header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/lightbox.css\"  media=\"screen\">\n");

            //for custom tab
            header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/tabview-core.css\"  media=\"screen\">\n");

            //for custom panel
            header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/panel.css\"  media=\"screen\">\n");

            header.append("<script type=\"text/javascript\">");
            header.append(script);
            header.append("</script>");
        }

        if(GlobalEnv.getframeworklist() == null){
            //added by goto 20130411  "title"
	        if (!title.equals(""))
	        	header.append("<title>"+title+"</title>\n");
	        
            //added by goto 20121217 start
        	header.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>\n");
        	
	        header.append("<STYLE TYPE=\"text/css\">\n");
	        header.append("<!--\n");
	        commonCSS();
	        header.append(css);
	        Log.out(css.toString());
	        header.append("\n-->\n</STYLE>\n");

//退避	        header.append("<link rel=\"stylesheet\" href=\"http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css\"/>\n");
//退避	        header.append("<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.css\"/>\n");
//退避	        header.append("<link rel=\"stylesheet\" href=\"js/jqm-icon-pack-2.0-original.css\"/>\n");
	        header.append("<link rel=\"stylesheet\" href=\"jscss/jquery-ui.css\"/>\n");
            header.append("<link rel=\"stylesheet\" href=\"jscss/jquery.mobile-1.3.1.min.css\"/>\n");
            header.append("<link rel=\"stylesheet\" href=\"jscss/jqm-datebox.min.css\"/>\n");
            header.append("<link rel=\"stylesheet\" href=\"jscss/jqm-icon-pack-2.0-original.css\"/>\n");
            header.append("<link rel=\"stylesheet\" href=\"jscss/jquery.simplePagination.css\"/>\n");
            //header.append("<link rel=\"stylesheet\" href="css/custom.css\"/>\n");
            //20130206
            //※※　要注意　※※　 jquery.jsより先にjquerymobile.jsをインポートすると、ボタン等の表示がうまくいかなくなる!!
//退避            header.append("<script src=\"http://code.jquery.com/jquery-1.7.1.min.js\"></script>\n");
//退避            header.append("<script src=\"http://code.jquery.com/ui/1.9.2/jquery-ui.min.js\"></script>\n");
//退避            header.append("<script src=\"http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.js\"></script>\n");
//退避            header.append("<script src=\"js/jquery.mobile.dynamic.popup.js\"></script>\n");
            header.append("<script src=\"jscss/jquery-1.7.1.min.js\"></script>\n");
            header.append("<script src=\"jscss/jquery-ui.min.js\"></script>\n");
            header.append("<script src=\"jscss/showmore.js\"></script>\n");
            header.append("<script src=\"jscss/jquery.mobile-1.3.1.min.js\"></script>\n");
            header.append("<script src=\"jscss/jqm-datebox.core.min.js\"></script>\n");
            header.append("<script src=\"jscss/jqm-datebox.mode.calbox.min.js\"></script>\n");
            header.append("<script src=\"jscss/jqm-datebox.mode.datebox.min.js\"></script>\n");
            header.append("<script src=\"jscss/jqm-datebox.mode.flipbox.min.js\"></script>\n");
            header.append("<script src=\"jscss/jquery.simplePagination.js\"></script>\n");
            header.append("<script src=\"jscss/jquery.mobile.dynamic.popup.js\"></script>\n");
            //header.append("<script src=\"js/config.js\"></script>\n");

            //added by goto 20130110 start
            //for slideshow
            //header.append("<link href=\"js/photoswipe/photoswipe.css\" type=\"text/css\" rel=\"stylesheet\" />\n");
            //header.append("<link href=\"js/photoswipe/jquery-mobile.css\" type=\"text/css\" rel=\"stylesheet\" />\n");
       		//header.append("<script src=\"js/photoswipe/klass.min.js\"></script>\n");
       		//header.append("<script src=\"js/photoswipe/code.photoswipe.jquery-3.0.4.min.js\"></script>\n");
//退避            header.append("<link href=\"http://www.db.ics.keio.ac.jp/ssqlcss/photoswipe/photoswipe.css\" type=\"text/css\" rel=\"stylesheet\" />\n");
//退避            header.append("<link href=\"http://www.db.ics.keio.ac.jp/ssqljscss/photoswipe/jquery-mobile.css\" type=\"text/css\" rel=\"stylesheet\" />\n");
//退避    		header.append("<script src=\"http://www.db.ics.keio.ac.jp/ssqljscss/photoswipe/klass.min.js\"></script>\n");
//退避    		header.append("<script src=\"http://www.db.ics.keio.ac.jp/ssqljscss/photoswipe/code.photoswipe.jquery-3.0.4.min.js\"></script>\n");
            //added by goto 20130110 end
       		
    		//added by goto 20130413  "row Prev/Next"
//    		header.append("<script src=\"js/jquery.iframe-auto-height.plugin.js\"></script>\n");
//    		header.append("<script src=\"js/script2.js\"></script>\n");
//退避    		header.append("<script src=\"http://www.db.ics.keio.ac.jp/ssqljscss/jquery.iframe-auto-height.plugin.js\"></script>\n");
//退避    		header.append("<script src=\"http://www.db.ics.keio.ac.jp/ssqljscss/script2.js\"></script>\n");
    		header.append("<script src=\"jscss/jquery.iframe-auto-height.plugin.js\"></script>\n");
    		header.append("<script src=\"jscss/jquery.validate.min.js\"></script>\n");
    		header.append("<script src=\"jscss/script2.js\"></script>\n");

    		
    		//added by goto 20130512  "max-width"
//    		header.append("<script type=\"text/javascript\"> var windowWidthThreshold = "+maxWidth+" </script>\n");
			header.append(
					"<script type=\"text/javascript\">\n" +
					"<!-- \n" +
					"var portraitWidth = "+portraitWidth+";\n" +
					"var landscapeWidth = "+landscapeWidth+";\n" +
					"var pcWidth = "+pcWidth+";\n" +
					"-->\n" +
					"</script>\n");
    		
//退避    		header.append("<script src=\"js/script1.js\"></script>\n");
    		header.append("<script src=\"jscss/script1.js\"></script>\n");
            //added by goto 20121217 end
            
            header.append("\n");
            
            //added by goto 20130311  "background"
	        if (!bg.equals("")){
	        	header.append("<style type=\"text/css\">");
	            header.append("<!-- .ui-page{ background: transparent url("+bg+") } -->");
	          	header.append("</style>\n");
	        }
	        
            //20130309  "div"
            header.append("<style type=\"text/css\">\n");
            header.append("<!-- div{ text-align:center; float:center; vertical-align:middle; } -->\n");
            //20130315	"長い文字が...と省略されるのを防ぐ (*:全てのタイプに適用) "
            header.append("<!-- * { white-space: normal; } -->\n");
            header.append("<!-- .error{ color:red; text-align:left; display:block; } -->\n");
            header.append("<!--\n");
            header.append(".ui-grid { overflow: hidden; }\n");
            header.append(".ui-block { margin: 0; padding: 0; border: 0; float: left; min-height: 1px; -webkit-box-sizing: border-box; -moz-box-sizing: border-box; -ms-box-sizing: border-box; box-sizing: border-box; }\n");
            header.append("-->\n");
          	header.append("</style>\n");

	        //added by goto 20130512  "Tableのセンタリング（画面横幅の大きいデバイスで見ると効果が分かる）"
//          	header.append("<style type=\"text/css\"><!-- table{ margin:auto; } --></style>\n");
            
            //20130206
            //下記は2013.02のSSQL教育で使用
            //※※　要注意　※※ generate mobileでは、ボタン等の表示に干渉してしまうため使用しない方が良い!!
//            header.append("<link href=\"http://www.db.ics.keio.ac.jp/ssqljscss/jquery-ui/css/ui-lightness/jquery-ui-1.9.2.custom.min.css\" type=\"text/css\" rel=\"stylesheet\" />\n");
//            header.append("<script src=\"http://code.jquery.com/jquery-1.7.1.min.js\"></script>\n");
//            header.append("<script src=\"http://www.db.ics.keio.ac.jp/ssqljscss/jquery-ui/js/jquery-ui-1.9.2.custom.min.js\" type=\"text/javascript\"></script>\n");
//            header.append("<script type=\"text/javascript\">\n");
//            header.append("$(function() {\n");
//            header.append("	$(\"[id=bounce]\").click(function() {\n");
//            header.append("		$(this).effect(\"bounce\", '', 1500);\n");
//         	  header.append("	});\n");
//            header.append("});\n");
//         	  header.append("</script>\n");
//            header.append("\n");
            
            
//            //20130205
              //下記は未完成
//            header.append("<script type=\"text/javascript\" src=\"interface.js\"></script>\n");
//            header.append("<script type=\"text/javascript\">" +
//            		"$(function(){" +
//            		"	$('#fisheye').Fisheye({" +
////            		"	$('[id=fisheye]').Fisheye({" +
//            		"		maxWidth: 80," +
//            		"		items: 'a'," +
//            		"		itemsText: 'span'," +
//            		"		container: '.fisheyeContainter'," +
//            		"		itemWidth: 80," +
//            		"		proximity: 90," +
//            		"		halign : 'center'" +
//            		"	})" +
//            		"})" +
//            		"</script>\n");
//            header.append("<style type=\"text/css\" media=\"all\">" +
//            		"#fisheye{" +
//            		"	margin-top:200px;" +
//            		"}" +
//            		".fisheye{" +
//            		"	text-align: center;" +
//            		"	height: 200px;" +
//            		"	position: relative;" +
//            		"}a.fisheyeItem{" +
//            		"	text-align: center;" +
//            		"	color: #000;" +
//            		"	font-weight: bold;" +
//            		"	text-decoration: none;" +
//            		"	width: 40px;" +
//            		"	position: absolute;" +
//            		"	display: block;" +
//            		"	top: 0;" +
//            		"}" +
//            		".fisheyeItem img{border: none;	margin: 0 auto 5px auto;	width: 100%;}" +
//            		".fisheyeItem span{	display: none;	positon: absolute;}" +
//            		".fisheyeContainter{height: 200px;	width: 700px;	position: absolute;}" +
//            		"</style>\n");
            

            
          	if(headerFlag==1){		//通常時のみ（Prev/Nextでは行わない）
	            //added by goto 20121222 start, changed by goto 20130110
	            //js/script1.jsの生成・書き込み
	            // TODO: 下記の場所を他へ変更（下記だと複数回生成・書き込みが行われる）
	            // TODO: -outdir?時の処理（下記は、出力先が.ssqlファイル格納場所に限定）
	//            System.out.println("GlobalEnv.getfilename()="+GlobalEnv.getfilename());
	            String fileName=GlobalEnv.getfilename();
	            String fileDir = "";
	            if(fileName.contains("/")){
	            	//TODO: filename.substring(ファイル名)へ変更
	            	fileDir = fileName.substring(0,fileName.lastIndexOf("/"));
	            }else{
	            	//TODO: fileNameのカレントディレクトリの絶対パスを取得
	            	//fileDir = fileNameのカレントディレクトリの絶対パス
	            }
	            //		下記は、linkを使うときのみ有効 (commentted out by goto 20130110)
	            //      String fileDir = new File(linkurl).getAbsoluteFile().getParent();	//htm_env.~をcut
	//            System.out.println("fileDir= "+fileDir);
	//            String relative_path = linkurl.substring(fileDir.length()+1);
	            // 書き込むファイルの名前
	            //String outputFileName = "/Applications/XAMPP/htdocs/ssql/js/c2.js";
	            String outputFileName = fileDir + "/jscss/script1.js";
	//            System.out.println("outputFileName="+outputFileName);
	            // ファイルオブジェクトの生成
	            File outputFile = new File(outputFileName);
	            
	            File dir = outputFile.getParentFile();  
	            if (!dir.exists()) {
	                dir.mkdirs();   //make folders
	            }
	
	            try {
	              // 出力ストリームの生成
	//              FileOutputStream fos = new FileOutputStream(outputFile);
	//              OutputStreamWriter osw = new OutputStreamWriter(fos);
	//              PrintWriter pw = new PrintWriter(osw);
	              
	    	  		PrintWriter pw;
	    	        if (charset != null){
	    	        	pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
	    	        			new FileOutputStream(outputFile),charset)));
	    	        	//Log.info("File encoding: "+html_env.charset);
	    	        }else
	    	        	pw = new PrintWriter(new BufferedWriter(new FileWriter(
	    	        			outputFile)));
	    	        
	    	        //ファイルへの書き込み
	    	        //フッタ(id=\"footer1\")の処理
	    	        //左右フリック・タップすることにより、[進む]・[戻る]・[更新]を行う		//added by goto 20130520
	    	        pw.println("\n" +
	    	        		"/** フッタ(id=\"footer1\")の処理 **/\n" +
	    	        		"//左フリック: [進む]、右フリック: [戻る]、タップ: [更新]\n" +
	    	        		"$(function() {\n" +
	    	        		"	//タップ時: [更新]\n" +
	    	        		"	$('#footer1').bind('tap',function(){\n" +
	    	        		"		history.go(0);\n" +
	    	        		"	});\n" +
	    	        		"\n" +
	    	        		"	//タッチイベントの取得\n" +
	    	        		"	$(\"#footer1\").bind(\"touchstart touchmove touchend\", touchHandler);\n" +
	    	        		"	function touchHandler(e) {  \n" +
	    	        		"		e.preventDefault();  \n" +
	    	        		"		var touch = e.originalEvent.touches[0];  \n" +
	    	        		"\n" +
	    	        		"		if(e.type == \"touchstart\"){\n" +
	    	        		"			//タッチ開始時のX座標(startX)\n" +
	    	        		"			startX = touch.pageX; \n" +
	    	        		"		}else if(e.type == \"touchmove\"){\n" +
	    	        		"			//移動距離(diffX) = スライド時のX座標 - 開始時のX座標\n" +
	    	        		"			diffX = touch.pageX - startX;\n" +
	    	        		"			if(( diffX > 0 ) || ( diffX < 0 )) {\n" +
	    	        		"				$('#footer1').css( \"left\", diffX );\n" +
	    	        		"			}\n" +
	    	        		"		}else if(e.type == \"touchend\"){\n" +
	    	        		"			if(diffX > 10) {		//右に10px以上移動: [戻る]\n" +
	    	        		"				history.go(-1);\n" +
	    	        		"			}else if(diffX < -10){	//左に10px以上移動: [進む]\n" +
	    	        		"				history.go(1);\n" +
	    	        		"			}else{\n" +
	    	        		"				$( '#footer1' ).animate({ left: 0 }, 200);\n" +
	    	        		"			} \n" +
	    	        		"		}\n" +
	    	        		"	}\n" +
	    	        		"});\n");
	    	        //rel="external"と指定されていた場合は、別ウィンドウを開く
	    	        //（W3C target="_blank" strict対策）		//added by goto 20130518
	    	        pw.println("\n/** rel=\"external\"と指定されていた場合は、別ウィンドウを開く **/\n" +
							"/**（W3C target=\"_blank\" strict対策）**/\n" +
							"$(function(){\n" +
							"	$(\"a[rel='external']\").click(function(){\n" +
							"		window.open($(this).attr(\"href\"));\n" +
							"		return false;\n" +
							"	});\n" +
							"});\n");
	    	        //画面サイズに応じて表示widthを変更		//added by goto 20130512
	    	        pw.println("\n/** 画面サイズに応じて表示widthを変更 **/\n" +
	    	        		"/** 画面width > 閾値 のとき、widthを固定して表示をセンタリング **/\n" +
	    	        		//"//閾値(windowWidthThreshold)は、HTMLファイルの<head>で指定\n" +
	    	        		"//PC: pc-width値に固定(指定なし(-1):350)\n" +
	    	        		"//モバイル: 縦=portrait-width値に固定(指定なし(-1):100%) ／ 縦=landscape-width値に固定(指定なし(-1):100%)\n" +
	    	        		//"var windowWidthThreshold = 500; 	//閾値\n" +
	    	        		"//初期load時\n" +
	    	        		"var windowWidthThreshold = 0;\n" +
	    	        		"$(document).ready(function(){\n" +
	    	        		"	windowWidthThreshold = getWindowWidthThreshold();\n" +
	    	        		"	if( $(window).width() > windowWidthThreshold ){\n" +
	    	        		"		//$(\"table\").css(\"width\",\"auto\");\n" +
	    	        		"		$(\"#header1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"		$(\"#content1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"		$(\"#footer1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"		$(\"#LOGINpanel1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"		$(\"#LOGOUTpanel1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"	}\n" +
	    	        		"});\n" +
	    	        		"//画面サイズが変更されたとき\n" +
	    	        		"window.onresize = function() {\n" +
	    	        		"	windowWidthThreshold = getWindowWidthThreshold();\n" +
	    	        		"	if( $(window).width() > windowWidthThreshold ){\n" +
	    	        		"    	//$(\"table\").css(\"width\",\"auto\");\n" +
	    	        		"		$(\"#header1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"		$(\"#content1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"		$(\"#footer1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"		$(\"#LOGINpanel1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"		$(\"#LOGOUTpanel1\").css(\"width\",windowWidthThreshold).css(\"margin\",\"auto\");\n" +
	    	        		"	}else{\n" +
	    	        		"		//$(\"table\").css(\"width\",\"100%\");\n" +
	    	        		"		$(\"#header1\").css(\"width\",\"100%\");\n" +
	    	        		"		$(\"#content1\").css(\"width\",\"100%\");\n" +
	    	        		"		$(\"#footer1\").css(\"width\",\"100%\");\n" +
	    	        		"		$(\"#LOGINpanel1\").css(\"width\",\"97%\");\n" +
	    	        		"		$(\"#LOGOUTpanel1\").css(\"width\",\"100%\");\n" +
	    	        		"	}\n" +
	    	        		"}\n" +
	    	        		"//画面width閾値を返す\n" +
	    	        		"function getWindowWidthThreshold(){\n" +
	    	        		"	if(!isSmartphone()){\n" +
	    	        		"		//PC\n" +
	    	        		"		if(pcWidth < 0)	return 350;\n" +
	    	        		"		else			return pcWidth;\n" +
	    	        		"	}else{\n" +
	    	        		"		//モバイル\n" +
	    	        		"		if(isPortrait()){\n" +
	    	        		"			//縦向き\n" +
	    	        		"			return portraitWidth;\n" +
	    	        		"		}else{\n" +
	    	        		"			//横向き\n" +
	    	        		"			return landscapeWidth;\n" +
	    	        		"		}\n" +
	    	        		"	}\n" +
	    	        		"}\n" +
	    	        		"//端末が縦向きかどうか\n" +
	    	        		"function isPortrait(){\n" +
	    	        		"	if($(window).width() < $(window).height())\n" +
	    	        		"		return true;\n" +
	    	        		"	return false;\n" +
	    	        		"}\n\n");
	    	        //twitter
	              	pw.println("$(document).on(\"pagebeforecreate\",'[data-role=page]',function(e){\n" +
	              			"  $.ajaxSetup({cache : true});\n" +
	              			"  $.getScript('http://platform.twitter.com/widgets.js');\n" +
	              			"  $.ajaxSetup({cache : false});\n" +
	              			"});\n\n");
	              	//facebook
	              	pw.println("$(document).on('pageshow', '[data-role=page]', function(e) {\n" +
	              			"  var src = '//www.facebook.com/plugins/like.php?href=';\n" +
	              			"  src += encodeURIComponent(location.href);\n" +
	              			"  src += '&send=false&layout=button_count&width=200&show_faces=true&action=like&colorscheme=light&height=21';\n" +
	              			"  $('.like-btn').attr('src', src);\n" +
	              			"});\n\n");
	              	//bookmark
	              	pw.println("function addBookmark(title,url) {\n" +
	              			"	//IE\n" +
	              			"	if(navigator.userAgent.indexOf(\"MSIE\") > -1){\n" +
							"		window.external.AddFavorite(url, title);\n" +
							"	}\n" +
							"	//Firefox\n" +
							"	else if(navigator.userAgent.indexOf(\"Firefox\") > -1){\n" +
							"		window.sidebar.addPanel(title, url, \"\");\n" +
							"	}\n" +
							"	//Opera\n" +
							"	else if(navigator.userAgent.indexOf(\"Opera\") > -1){\n" +
							"		document.write('<div style=\"text-align:center\"><a href=\"'+url+'\" rel=\"sidebar\" title=\"'+title+'\">ブックマークに追加</a></div><br>');\n" +
							"	}\n" +
							"	//Netscape\n" +
							"	else if(navigator.userAgent.indexOf(\"Netscape\") > -1){\n" +
							"		document.write('<div style=\"text-align:center\"><input type=\"button\" value=\"ブックマークに追加\"');\n" +
							"		document.write(' onclick=\"window.sidebar.addPanel(\\''+title+'\\',\\''+url+'\\',\\'\\');\"></div><br>');\n" +
							"	}\n" +
							"	else{\n" +
							"    	alert(\"このブラウザへのお気に入り追加ボタンは、Chrome/Safari等には対応しておりません。\\nChrome/Safariの場合、CtrlキーとDキーを同時に押してください。\\nその他の場合はご自身のブラウザからお気に入りへ追加下さい。\");\n" +
							"  	}\n" +
							"}\n");
	              	//added by goto 20130110
	              	//slideshow
	              	pw.println("$(document).on('pageshow', '#p-gallery', function(e){\n" +
	              			"	var currentPage = $(e.target);\n" +
	              			"	photoSwipeInstance = $(\"ul.gallery a\", e.target).photoSwipe({},  currentPage.attr('id'));\n" +
	              			"}).on('pagehide', '#p-gallery', function(e){\n" +
	              			"	var currentPage = $(e.target),\n" +
	              			"	photoSwipeInstance = window.Code.PhotoSwipe.getInstance(currentPage.attr('id'));\n" +
	              			"	if (typeof photoSwipeInstance != \"undefined\" && photoSwipeInstance != null) {\n" +
	              			"		window.Code.PhotoSwipe.detatch(photoSwipeInstance);\n" +
	              			"	}\n" +
	              			"});\n");
	              	
	              	//added by goto 20130330
	              	//tab
	              	pw.println("$(document).ready(function() {\n" +
	              			"	$( \"[id=tabs]\" ).tabs();\n" +
	//              			"	$( \"#tabs\" ).tabs();\n" +
	              			"});\n");
	              	
	              	//form validation
	              	pw.println("\n/** <form> validation **/\n" +
	              			"$(document).ready(function () {\n" +
	              			"	jQuery.validator.addMethod(\n" +
	              			"	  \"jqValidate_TelephoneNumber\", function(value, element) {\n" +
	              			"	     return this.optional(element) || new RegExp(\"^[0-9\\-]+$\").test(value);\n" +
	              			"	   }, \"Please enter a valid telephone number.\"\n" +
	              			"	);\n" +
	              			"	jQuery.validator.addMethod(\n" +
	              			"	  \"jqValidate_Alphabet\", function(value, element) {\n" +
	              			"	     return this.optional(element) || new RegExp(\"^[a-zA-Z]+$\").test(value);\n" +
	              			"	   }, \"You can enter only the alphabet.\"\n" +
	              			"	);\n" +
	              			"	jQuery.validator.addMethod(\n" +
	              			"	  \"jqValidate_AlphabetNumber\", function(value, element) {\n" +
	              			"	     return this.optional(element) || new RegExp(\"^[0-9a-zA-Z]+$\").test(value);\n" +
	              			"	   }, \"You can enter only the alphabet and a number.\"\n" +
	              			"	);\n" +
	              			"});\n");
	              	
	//              	//added by goto 20130503
	//              	//panel
	//              	pw.println("$(document).on('click',\"button.open\",function(){\n" +
	//              			"	$(\"[id=ssqlpanel]\").panel(\"open\")\n" +
	//      					"}).on('click',\"button.close\",function(){\n" +
	//						"	$(\"[id=ssqlpanel]\").panel(\"close\")\n" +
	//						"});\n");
	              
	              	// 後始末
	              	pw.close();
	            // エラーがあった場合は、スタックトレースを出力
	            } catch(Exception e) {
	            	e.printStackTrace();
	            }
	            //added by goto 20121222 end
          	}//通常のみの処理（jsファイル作成）end
        	
          	header.append("\n");
	        header.append(cssjsFile);	//added by goto 20130703
	        header.append(cssfile);
			header.append(jsFile);		//added by goto 20130703
	        header.append("</HEAD>\n");

	        //header.append("<BODY class=\"body\">\n");
	        header.append("<BODY>\n");
	        header.append("<!-- data-role=page start -->\n<div data-role=\"page\" id=\"p-top1\" >\n\n");
	        
	        //added by goto 20130508  "Login&Logout" start
	        //ログイン・ログアウト・新規登録
	        if(SSQLparser.sessionFlag){
	        	String s = SSQLparser.sessionString.trim();
	        	//Log.i("sessionString:" + s);
	        	
	        	//置換 ( " , "  ->  " ; " )
	    		//Log.i("Before sessionString: "+s);
	        	boolean dqFlg = false;
	        	for(int i=0; i<s.length();i++){
	        		if(!dqFlg){
	        			if(s.charAt(i)=='"')		dqFlg=true;
	        		}else if(dqFlg){
	        			if(s.charAt(i)==',')
	        				s = s.substring(0,i)+";"+s.substring(i+1);	//置換
	    	    		else if(s.charAt(i)=='"')	dqFlg=false;
	        		}
	        	}
	        	//Log.i("After sessionString:  "+s);
	        	
	        	String sessionVariable_UniqueName = "ssql_";		//セッション変数に使用するユニークな名前（session()の最後から取ってくる）
	        	
	        	String s_val = "";
	        	if(s.startsWith("login"))	s_val = s.substring("login".length(),s.indexOf("(")).trim();
	        	else						s_val = s.substring("session".length(),s.indexOf("(")).trim();
	        	if(s_val.equals(""))		s_val = "1";
//	        	Log.i("s_val:" + s_val);
	        	String DB = GlobalEnv.getdbname();										//DB
	        	String c1str = "";
	        	if(!s_val.equals("3"))	c1str = "ID:";									//ID string
	        	else					c1str = "E-mail address:";						//E-mail string
	        	String c1 = "";															//ID column
//	        	String c1 = s.substring(s.indexOf("(")+1,s.indexOf(",")).trim();		//ID column
//	        	if(c1.contains(":")){
//	        		c1str = c1.substring(0,c1.lastIndexOf(":")+1);
//	        		c1 = c1.substring(c1.lastIndexOf(":")+1).trim();
//	        	}
	        	String c2str = "Password";												//PW string
	        	String c2 = "";															//PW column
//	        	String c2 = s.substring(s.indexOf(",")+1,s.lastIndexOf(",")).trim();	//PW column
//	        	if(c2.contains(":")){
//	        		c2str = c2.substring(0,c2.lastIndexOf(":"));
//	        		c2 = c2.substring(c2.lastIndexOf(":")+1).trim();
//	        	}
	        	String from = "";
	        	String expire_time = "";
	        	String sender_name = "";
	        	String admin_email = "";
	        	String c3 = "";
	        	String[] c3_array = null;
	        	int c3_array_num = 0;
	        	if(s_val.equals("1") || s_val.equals("2")){
	        		//s_val:1 or 2
	        		//Log.i("s: "+s);
//	        		for(int i=0; i<s.length(); i++) Log.i(s.charAt(i));
	        		String buf = s.substring(0,s.indexOf(","));
		        	c1 = buf.substring(buf.indexOf("\"")+1,buf.indexOf(";")).trim();			//ID column,
		        	if(c1.contains(":")){
		        		c1str = c1.substring(0,c1.lastIndexOf(":")+1);
		        		c1 = c1.substring(c1.lastIndexOf(":")+1).trim();
		        	}
		        	c2 = buf.substring(buf.indexOf(";")+1,buf.lastIndexOf("\"")).trim();		//PW column
		        	if(c2.contains(":")){
		        		c2str = c2.substring(0,c2.lastIndexOf(":"));
		        		c2 = c2.substring(c2.lastIndexOf(":")+1).trim();
		        	}
		        	
		        	s = s.substring(s.indexOf(",")+1);
		        	from = s.substring(0,s.indexOf(",")).trim();		//FROM
	        		
	        		s = s.substring(s.indexOf(",")+1);
	        		c3 = s.substring(0,s.indexOf(",")).replaceAll("\"","").replaceAll(";",",").trim();	//セッション変数として保存する属性
	        		if(c3.endsWith(","))	c3 = c3.substring(0, c3.lastIndexOf(","));
	        		//Log.i(c3.length() - c3.replaceAll(",","").length()+1);
	        		c3_array_num = c3.length() - c3.replaceAll(",","").length()+1;
	        		c3_array = new String[c3_array_num];
	        		String c3_buf = c3+",";
	        		for(int i=0; i<c3_array_num; i++){
	        			c3_array[i] = c3_buf.substring(0,c3_buf.indexOf(",")).trim();
	        			c3_buf = c3_buf.substring(c3_buf.indexOf(",")+1);
	        		}
	        		
	        		sessionVariable_UniqueName += s.substring(s.lastIndexOf(",")+1,s.indexOf(")")).replaceAll("\"","").replaceAll(" ","").trim()+"_";	//セッション変数に使用するユニークな名前
	        		//Log.i("sessionVariable_UniqueName: "+sessionVariable_UniqueName);
	        	}else{
	        		//s_val:3（E-mail登録）
	        		c1 = s.substring(s.indexOf("(")+1,s.indexOf(",")).trim();			//ID column
		        	if(c1.contains(":")){
		        		c1str = c1.substring(0,c1.lastIndexOf(":")+1);
		        		c1 = c1.substring(c1.lastIndexOf(":")+1).trim();
		        	}
		        	s = s.substring(s.indexOf(",")+1);
		        	c2 = s.substring(0,s.indexOf(",")).trim();							//PW column
		        	if(c2.contains(":")){
		        		c2str = c2.substring(0,c2.lastIndexOf(":"));
		        		c2 = c2.substring(c2.lastIndexOf(":")+1).trim();
		        	}
		        	s = s.substring(s.indexOf(",")+1);
	        		from = s.substring(0,s.indexOf(",")).trim();						//FROM
	        		s = s.substring(s.indexOf(",")+1);
	        		//for(int j=0; j<3;j++)	s = s.substring(s.indexOf(",")+1);			//3つ目の,までカット
//	        		Log.i("s: "+s);
	        		expire_time = s.substring(0, s.indexOf(",")).replaceAll("\"","").trim();
	        		if(expire_time.equals(""))	expire_time = "30";
	        		s = s.substring(s.indexOf(",")+1);
	        		sender_name = s.substring(0, s.indexOf(",")).replaceAll("\"","").trim();
	        		s = s.substring(s.indexOf(",")+1);
	        		admin_email = s.substring(0, s.indexOf(",")).replaceAll("\"","").trim();
	        		s = s.substring(s.indexOf(",")+1);
	        		sessionVariable_UniqueName += s.substring(0, s.indexOf(")")).replaceAll("\"","").replaceAll(" ","").trim()+"_";	//セッション変数に使用するユニークな名前
	        		
	        		Log.i("s: "+s+" expire_time:"+expire_time+" sender_name:"+sender_name+" admin_email:"+admin_email+" sessionVariable_UniqueName:"+sessionVariable_UniqueName);
	        	}
	        	String DBMS = GlobalEnv.getdbms();										//DBMS
	        	//Log.i(c1str+c1+"   "+c2str+c2+"      "+c3+"   from:"+from+"   DB:"+DB+"	DBMS:"+DBMS);
	        	
	        	//sqlite3 php
	        	if(DBMS.equals("sqlite") || DBMS.equals("sqlite3")){
	        		if(s_val.equals("1") || s_val.equals("2")){
		        		header.append("\n" +
		        				"<!-- \"Login & Logout\" start -->\n" +
		        				"<?php\n" +
		        				"//最初にログイン中かどうか判定\n" +
		        				"if($_SESSION["+sessionVariable_UniqueName+"id] != \"\"){\n" +
		        				"?>\n" +
		        				"	<script type=\"text/javascript\">\n" +
		        				"	$(document).ready(function(){\n" +
		        				"		$('#login1').attr('checked', 'checked');	//load時\n" +
		        				"		$('#LOGINpanel1').hide();\n" +
		        				"	});\n" +
		        				"	</script>\n" +
		        				"<?php\n" +
		        				"	display_html();								//display_html\n" +
		        				//ユーザ名
		        				"	echo '<script type=\"text/javascript\">$(\\'#showValues\\').html(\\'<div style=\\\"text-align:right;color:gray;font-size:12;background-color:whitesmoke;\\\">ログイン日時: '.$_SESSION["+sessionVariable_UniqueName+"logintime].'<br>ようこそ '.$_SESSION["+sessionVariable_UniqueName+"id].'さん</div>\\');</script>';\n" +
		        				"}else{\n" +
		        				"?>\n" +
		        				"	<script type=\"text/javascript\">\n" +
		        				"	$(document).ready(function(){\n" +
		        				"		$('#LOGOUTpanel1').hide();\n" +
		        				"	});\n" +
		        				"	</script>\n" +
		        				"<?php\n" +
		        				"}\n" +
		        				"?>\n" +
		        				"\n" +
		        				"<!-- Login & Registration start -->\n" +
		        				"<!-- Login Panel start -->\n" +
		        				"<br>\n" +
		        				//"<div id=\"LOGINpanel1\" style=\"background-color:black; width:100%;\" data-role=\"none\">\n" +
		        				//"<div id=\"LOGINpanel1\" style=\"background-color:whitesmoke; border-radius:20px; border:5px gray solid;\" data-role=\"none\">\n" +
		        				"<div id=\"LOGINpanel1\" style=\"background-color:whitesmoke; border-radius:10px; border:3px gray solid;\" data-role=\"none\">\n" +
		        				//"<div style=\"color:lightgray; font-size:30; background-color:black; border-radius:15px 15px 0px 0px;\" id=\"loginTitle1\">Log in</div>\n" +
		        				"<div style=\"color:lightgray; font-size:30; background-color:black; border-radius:5px 5px 0px 0px;\" id=\"loginTitle1\">Log in</div>\n" +
		        				"<br>\n" +
		        				"<form method=\"post\" action=\"\" target=\"login_ifr1\">\n" +
		        				"<div>\n" +
		        				"	<div style=\"font-size:20;\">"+c1str+"&nbsp;&nbsp;</div>\n" +
		        				"	<input type=\"text\" name=\"id\" data-mini=\"true\">\n");
		        		
	    				if(!s_val.equals("1"))
		        			header.append(
		        				"	<fieldset data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\">\n" +
		        				"		<input type=\"radio\" name=\"choose\" id=\"login1\" value=\"login1\" checked=\"checked\">\n" +
		        				"	    <label for=\"login1\">I have an account</label>\n" +
		        				"		<input type=\"radio\" name=\"choose\" id=\"signup1\">\n" +
		        				"		<label for=\"signup1\"> I am new!</label>\n" +
		        				"	</fieldset>\n");
	    				
	    				header.append(
		        				"</div>\n" +
		        				"<div id=\"login_block\">\n" +
		        				"	<div style=\"font-size:20;\">"+c2str+":&nbsp;&nbsp;&nbsp;</div>\n" +
		        				"	<input type=\"password\" name=\"password\" id=\"password\" data-mini=\"true\">\n" +
		        				"	<input type=\"submit\" value=\" Login \" name=\"sqlite3_login1\" id=\"sqlite3_login1\" data-mini=\"false\" data-inline=\"false\">\n" +
		        				"</div>\n");
		        				
		        		if(!s_val.equals("1"))
		        			header.append(
		        				"<div id=\"signup_block\" style=\"display:none\" data-role=\"none\">\n" +
		        				"	<div style=\"font-size:20;\">Choose "+c2str+":&nbsp;&nbsp;</div>\n" +
		        				"	<input type=\"password\" name=\"newpassword\" id=\"newpassword\" data-mini=\"true\">\n" +
		        				"	<div style=\"font-size:20;\">Reinput "+c2str+":&nbsp;&nbsp;</div>\n" +
		        				"	<input type=\"password\" name=\"re_newpassword\" id=\"re_newpassword\" data-mini=\"true\">\n" +
		        				"	<input type=\"submit\" value=\" Signup \" name=\"sqlite3_login1\" id=\"sqlite3_login1\" data-mini=\"false\" data-inline=\"false\">\n" +
		        				"</div>\n");
		        		
		        		header.append(
		        				"</form>\n" +
		        				"\n" +
		        				"<iframe name=\"login_ifr1\" style=\"display:none;\"></iframe>\n" +
		        				"<p id=\"Login_text1\"  data-role=\"none\"><!-- ここに表示 --></p>\n" +
		        				"<br>\n" +
		        				"</div>\n" +
		        				"<!-- Login Panel end -->\n" +
		        				"\n" +
		        				"<?php\n" +
		        				"//Login or Registration\n" +
		        				"if($_POST['sqlite3_login1']){\n" +
		        				"	//ユーザ定義\n" +
		        				"	$sqlite3_DB = '"+DB+"';\n" +
		        				"	$sqlite3_id = '"+c1+"';\n" +
		        				"	$sqlite3_pw = '"+c2+"';\n" +
		        				"	$sqlite3_c3 = '"+((!c3.equals(""))?(","+c3):("") )+"';\n" +
		        				"	$sqlite3_table = '"+from+"';\n" +
		        				"\n" +
		        				"	$id = checkHTMLsc($_POST['id']);\n" +
		        				"	$pw = checkHTMLsc($_POST['password']);\n" +
		        				"	$newpw = checkHTMLsc($_POST['newpassword']);\n" +
		        				"	$re_newpw = checkHTMLsc($_POST['re_newpassword']);\n" +
		        				"\n" +
		        				"	if($pw && $id){\n" +
		        				"		//Login\n" +
		        				"		$db = new SQLite3($sqlite3_DB);\n" +
		        				"		$sql = \"SELECT \".$sqlite3_id.\",\".$sqlite3_pw.\"\".$sqlite3_c3.\" FROM \".$sqlite3_table.\" where \".$sqlite3_id.\"='\".$id.\"' and \".$sqlite3_pw.\"='\".$pw.\"'\";\n" +
		        				"	    $result = $db->query($sql);\n" +
		        				"	    $i = 0;\n" +
		        				//"	    while($res = $result->fetchArray(SQLITE3_ASSOC)){\n" +
		        				"	    while($res = $result->fetchArray()){\n");
//		        				"	          $_SESSION[name] = $res[2];\n" +
						if(!c3.equals("")){
			        		for(int i=0; i<c3_array_num; i++){
								//c3_array[i];
								header.append(
				        				"	          $_SESSION["+c3_array[i]+"] = $res["+(i+2)+"];\n");
							}
						}
						header.append(
		        				"	          $i++;\n" +
		        				"	          if($i==1)	break;\n" +
		        				"	    }\n" +
		        				"	    if($i == 0)	p('<font color=#ff0000>Login failed.</font>');	//Login failed.\n" +
		        				"	    else{\n" +
		        				"	    	//Login success.\n" +
		        				"	    	$_SESSION["+sessionVariable_UniqueName+"id] = $id;\n" +
		        				"	    	$_SESSION["+sessionVariable_UniqueName+"logintime] = date('Y/m/d(D) H:i:s', time());\n" +		//ユーザ名
		        				"			echo '<script type=\"text/javascript\">window.parent.$(\\'#Login_text1\\').text(\"\");</script>';\n" +
		        				"			echo '<script type=\"text/javascript\">window.parent.history.go(0);</script>';	//reload\n" +
		        				"	    }\n" +
		        				"	}else if($newpw && $re_newpw && $id){\n" +
		        				"		//check (pw==re_pw)?\n" +
		        				"		if($newpw != $re_newpw){\n" +
		        				"			p('<font color=#ff0000>Please input the same "+c2str+".</font>');	//PW is not equal\n" +
		        				"		}else{\n" +
		        				
		        				"			//check & registration\n" +
		        				"			$db = new SQLite3($sqlite3_DB);\n" +
		        				"			\n" +
		        				"			//check\n" +
		        				"			$sql1 = \"SELECT \".$sqlite3_id.\" FROM \".$sqlite3_table.\" where \".$sqlite3_id.\"='\".$id.\"'\";\n" +
		        				"		    $result1 = $db->query($sql1);\n" +
		        				"		    $i=0;\n" +
		        				"		    while($res = $result1->fetchArray(SQLITE3_ASSOC)){\n" +
		        				"	   	       $i++;\n" +
		        				"	   	       if($i==1)	break;\n" +
		        				"		    }\n" +
		        				"		    if($i > 0)	p('<font color=#ff0000>\\\''.$id.'\\\' has been already registered.</font>');	//already registered.\n" +
		        				"		    else{\n" +
		        				"	   	       //registration\n" +
		        				"	   	       $sql2 = \"INSERT INTO \".$sqlite3_table.\" (\".$sqlite3_id.\", \".$sqlite3_pw.\") VALUES ('\".$id.\"','\".$newpw.\"')\";\n" +
		        				"	   	       try{\n" +
		        				"					$result2 = $db->exec($sql2);\n" +
		        				"					p('<font color=gold>Registration Success!!</font>');\n" +
		        				"	   	       }catch(Exception $e){\n" +
		        				"					p('<font color=#ff0000>Registration failed.</font>'.$e->getMessage());\n" +
		        				"	   	       }\n" +
		        				"		    }\n" +
		        				
		        				"		}\n" +
		        				"	}else{\n" +
		        				"		p('<font color=#ff0000>Please input form.</font>');\n" +
		        				"	}\n" +
		        				"	unset($db);\n" +
		        				"}\n" +
		        				"function p($str){\n" +
		        				"	//親ウインドウのJavaScript関数（テキストエリアへ書き込み）を呼び出す\n" +
		        				"	echo '<script type=\"text/javascript\">window.parent.Login_echo1(\"'.$str.'\");</script>';\n" +
		        				"}\n" +
		        				"?>\n" +
		        				"\n" +
		        				"<script type=\"text/javascript\">\n");
		        		
	    				if(!s_val.equals("1"))
		        			header.append(
		        				"$(document).ready(function(){\n" +
		        				"	//アカウントあり or 新規登録 クリック時の処理\n" +
		        				"	$('#signup1').click(function(){\n" +
		        				"		$('#loginTitle1').text('Sign up');\n" +
		        				"		$('#password').val('');\n" +
		        				"		$('#login_block').hide();\n" +
		        				"		$('#signup_block').show();\n" +
		        				"		Login_echo1(\"\");\n" +
		        				"	});\n" +
		        				"	$('#login1').click(function(){\n" +
		        				"		$('#loginTitle1').text('Log in');\n" +
		        				"		$('#newpassword').val('');\n" +
		        				"		$('#re_newpassword').val('');\n" +
		        				"		$('#signup_block').hide();\n" +
		        				"		$('#login_block').show();\n" +
		        				"		Login_echo1(\"\");\n" +
		        				"	});\n" +
		        				"});\n" +
		        				"\n");
	    				
	    				header.append(
		        				"//テキストエリアへ書き込み\n" +
		        				"function Login_echo1(str){\n" +
		        				"  var textArea = document.getElementById(\"Login_text1\");\n" +
		        				"  textArea.innerHTML = \"<h2>\" + str + \"</h2>\";\n" +
		        				"}\n" +
		        				"</script>\n" +
		        				"<!-- Login & Registration end -->\n" +
		        				"\n" +
		        				"\n");
	    				
	        		}else{	//s_val:3（E-mail登録）
	        			header.append(
	        					"<!-- \"Login & Logout\" start -->\n" +
    							"<?php\n" +
    							"    //定義\n" +
    							"    $tokendir = dirname( __FILE__ ).DIRECTORY_SEPARATOR.\"token\".DIRECTORY_SEPARATOR;\n" +
    							"    $email = \"\";\n" +
    							"    $expire_time = "+expire_time+"*60;						//分\n" +
    							"    $sender_name = \""+sender_name+"\";                    //送信者名\n" +
    							"    $admin_email = \""+admin_email+"\";	//本当は、管理者メールアドレスはconfig.phpのMAILTO_MASTERを利用した方が良い\n" +
    							"    \n" +
    							"    $success_page = \"http://\".$_SERVER[\"HTTP_HOST\"].$_SERVER[\"REQUEST_URI\"];	//ログインページ\n" +
    							"    $pos = strpos($success_page, '?');		//?以下をカット\n" +
    							"	if($pos !== false)	$success_page = substr($success_page, 0, $pos); \n" +
    							"    $resistration_page = $success_page;											//もう一度こちらから\n" +
    							"    \n" +
    							"    mb_language(\"ja\");				//カレントの言語を日本語に設定する\n" +
    							"    mb_internal_encoding(\"UTF-8\");	//内部文字エンコードを設定する\n" +
    							"?>\n" +
    							"\n" +
    							"<?php\n" +
    							"//最初にログイン中かどうか判定\n" +
    							"if($_SESSION["+sessionVariable_UniqueName+"id] != \"\"){\n" +
    							"?>\n" +
    							"	<script type=\"text/javascript\">\n" +
    							"	$(document).ready(function(){\n" +
    							"		$('#login1').attr('checked', 'checked');	//load時\n" +
    							"		$('#LOGINpanel1').hide();\n" +
    							"	});\n" +
    							"	</script>\n" +
    							"<?php\n" +
    							"	display_html();								//display_html\n" +
    							"}else{\n" +
    							"?>\n" +
    							"	<script type=\"text/javascript\">\n" +
    							"	$(document).ready(function(){\n" +
    							"		$('#LOGOUTpanel1').hide();\n" +
    							"	});\n" +
    							"	</script>\n" +
    							"<?php\n" +
    							"}\n" +
    							"?>\n" +
    							"\n" +
    							"<div id=\"message\"  data-role=\"none\"><!-- ここに表示 --></div>\n" +
    							"\n" +
    							"<script type=\"text/javascript\">\n" +
    							"$(document).ready(function(){\n" +
    							"<?php\n" +
    							"    if(isset($_GET[\"key\"])){\n" +
    							"        if(delete_old_token($_GET[\"key\"])){\n" +
    							"            global $email,$success_page;\n" +
    							"           \n" +
    							"            if(mail_to_success($email)){//ユーザーにメール\n" +
    							"            	mail_to_master($email); //管理者にメール\n" +
    							"?>\n" +
    							"            	$(\"#message\").html(\"<br><div>登録完了メールを送信しました。<a href=\\\"<?php echo $success_page; ?>\\\" target=\\\"_self\\\"><br>ログインページ</a>へ</div>\");\n" +
    							"            	$(\"#LOGINpanel1\").hide();\n" +
    							"<?php\n" +
    							"			}\n" +
    							"        }else{\n" +
    							"            //document.write(\"もう一度初めからやり直してください。\");\n" +
    							"?>\n" +
    							"            $(\"#message\").html(\"<br><div>もう一度<a href=\\\"<?php echo $resistration_page; ?>\\\" target=\\\"_self\\\">こちら</a>からやり直してください。</div>\");\n" +
    							"            $(\"#LOGINpanel1\").hide();\n" +
    							"<?php\n" +
    							"        }\n" +
    							"    }\n" +
    							"?>\n" +
    							"});\n" +
    							"</script>\n" +
    							"\n" +
    							"<!-- Login & Registration start -->\n" +
    							"<!-- Login Panel start -->\n" +
    							"<div id=\"LOGINpanel1\">\n" +
    							"	<br>\n" +
    							"	<div style=\"text-align:right; margin-right:20px; font-size:18px;\">\n" +
    							"		<span id=\"login1\"><span style=\"font-weight:bold;\">ログイン</span></span>\n" +
    							"		<span id=\"signup1\"><a href=\"\">新規登録</a></span>\n" +
    							"		<!--<fieldset data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\" data-role=\"none\">\n" +
    							"			<input type=\"radio\" name=\"choose\" id=\"login1\" value=\"login1\" checked=\"checked\">\n" +
    							"		    <label for=\"login1\">I have an account</label>\n" +
    							"			<input type=\"radio\" name=\"choose\" id=\"signup1\">\n" +
    							"			<label for=\"signup1\"> I am new!</label>\n" +
    							"		</fieldset>-->\n" +
    							"	</div>\n" +
    							"	<div style=\"background-color:whitesmoke; width:97%;  border-radius:20px; border:5px gray solid;\" data-role=\"none\">\n" +
    							"		<div style=\"color:lightgray; font-size:30; background-color:black; border-radius:15px 15px 0px 0px;\" id=\"loginTitle1\">ログイン</div>\n" +
    							"		<br><br>\n" +
    							"		\n" +
    							"		<form method=\"post\" action=\"\" target=\"login_ifr1\">\n" +
    							"			<div id=\"login_block\">\n" +
    							"				<div style=\"font-size:20;\">"+c1str+"&nbsp;&nbsp;</div>\n" +
    							"				<input type=\"text\" name=\"id\" data-mini=\"true\">	\n" +
    							"				<div style=\"font-size:20;\">"+c2str+":&nbsp;&nbsp;&nbsp;</div>\n" +
    							"				<input type=\"password\" name=\"password\" id=\"password\" data-mini=\"true\">\n" +
    							"				<input type=\"submit\" value=\" Login \" name=\"sqlite3_login1\" id=\"sqlite3_login1\" data-mini=\"false\" data-inline=\"false\">\n" +
    							"			</div>\n" +
    							"			<div id=\"signup_block\" style=\"display:none\" data-role=\"none\">\n" +
    							"			    <div style=\"font-size:20;\">"+c1str+"&nbsp;&nbsp;</div>\n" +
    							"			    <input type=\"text\" name=\"mail1\" id=\"mail1\" data-mini=\"true\">\n" +
    							"			    <input type=\"submit\" value=\" Send \" name=\"mail\" id=\"mail\" data-mini=\"false\" data-inline=\"false\">\n" +
    							"			</div>\n" +
    							"		</form>\n" +
    							"		\n" +
    							"		<iframe name=\"login_ifr1\" style=\"display:none;\"></iframe>\n" +
    							"		<div id=\"Login_text1\"  data-role=\"none\"><!-- ここに表示 --></div>\n" +
    							"		<br>\n" +
    							"	</div>\n" +
    							"</div>\n" +
    							"\n" +
    							"<form method=\"post\" action=\"\" target=\"reset_ifr1\" id=\"RESETpanel1\" name=\"RESETpanel1\">\n" +
    							"<input type=\"hidden\" name=\"sqlite3_reset1\" value=\"\">\n" +
    							"</form>\n" +
    							"<iframe name=\"reset_ifr1\" style=\"display:none;\"></iframe>\n" +
    							"<!-- Login Panel end -->\n" +
    							"\n" +
    							"<?php\n" +
    							"	//ログイン処理\n" +
    							"	if($_POST['sqlite3_login1']){\n" +
    							"		//ユーザ定義\n" +
    							"		$sqlite3_DB = '"+DB+"';\n" +
    							"		$sqlite3_id = '"+c1+"';\n" +
    							"		$sqlite3_pw = '"+c2+"';\n" +
    							"		$sqlite3_table = '"+from+"';\n" +
    							"	\n" +
    							"		$id = checkHTMLsc($_POST['id']);\n" +
    							"		$pw = checkHTMLsc($_POST['password']);\n" +
    							"	\n" +
    							"		if($pw && $id){\n" +
    							"	   		$db = new SQLite3($sqlite3_DB);\n" +
    							"			$sql = \"SELECT \".$sqlite3_id.\",\".$sqlite3_pw.\" FROM \".$sqlite3_table.\" where \".$sqlite3_id.\"='\".$id.\"' and \".$sqlite3_pw.\"='\".$pw.\"'\";\n" +
    							"		    $result = $db->query($sql);\n" +
    							"		    $i = 0;\n" +
    							"		    while($res = $result->fetchArray(SQLITE3_ASSOC)){\n" +
    							"		          $i++;\n" +
    							"		          if($i==1)	break;\n" +
    							"		    }\n" +
    							"		    if($i == 0)	p('<font color=#ff0000>Login failed.</font>');	//Login failed.\n" +
    							"		    else{\n" +
    							"		    	//Login success.\n" +
    							"		    	$_SESSION["+sessionVariable_UniqueName+"id] = $id;\n" +
    							"				echo '<script type=\"text/javascript\">window.parent.$(\\'#Login_text1\\').text(\"\");</script>';\n" +
    							"				echo '<script type=\"text/javascript\">window.parent.history.go(0);</script>';	//reload\n" +
    							"		    }\n" +
    							"		}else{\n" +
    							"			p('<font color=#ff0000>Please input form.</font>');\n" +
    							"		}\n" +
    							"		unset($db);\n" +
    							"	}\n" +
    							"\n" +
    							"	//メール送信 ＆ 新規登録\n" +
    							"	if($_POST['mail'] || $_POST['mail1']){\n" +
    							"	    if($_POST[\"mail1\"]==\"\"){\n" +
    							"	        p(\"<font color=#ff0000>メールアドレスを入力してください。</font>\");\n" +
    							"	    }elseif(mb_strlen($_POST[\"mail1\"])> 0 && !preg_match(\"/^([a-z0-9_]|\\-|\\.|\\+)+@(([a-z0-9_]|\\-)+\\.)+[a-z]{2,6}$/i\",$_POST[\"mail1\"])){\n" +
    							"	        p(\"<font color=#ff0000>メールアドレスの書式に<br>誤りがあります。</font>\");\n" +
    							"	    }else{\n" +
    							"	    	//TODO: トークンが存在しているかどうかチェック\n" +
    							"	    	//hasToken($_POST[\"mail1\"]);\n" +
    							"	    	\n" +
    							"			//ユーザ定義\n" +
    							"			$sqlite3_DB = '"+DB+"';\n" +
    							"			$sqlite3_id = '"+c1+"';\n" +
    							"			$sqlite3_table = '"+from+"';\n" +
    							"			\n" +
    							"			$email = $_POST[\"mail1\"];\n" +
    							"	        \n" +
    							"	        //DB登録未登録のメールアドレスかどうかの判定\n" +
    							"	        if( isResistered($email, $sqlite3_DB, $sqlite3_id, $sqlite3_table) ){\n" +
    							"	       		//登録済み\n" +
    							"		    	p('<font color=#ff0000>\\''.$email.'\\' は<br>既に登録済です。</font><br><span style=\\\"font-size:11px;\\\">パスワードを忘れた場合は、<a href=\\\"\\\" onclick=\\\"window.parent.document.RESETpanel1.sqlite3_reset1.value=\\''.$email.'\\';window.parent.document.RESETpanel1.submit();return false;\\\">こちら</a>をクリックしてください。</span>');\n" +
    							"	        }else{\n" +
    							"	        	//未登録　→　確認メール送信\n" +
    							"	        	p(\"確認メールを送信しました。\".\"<br>\".'<span style=\\\"font-size:15px;\\\">E-mail to: '.$email.'</span>');\n" +
    							"	        	mail_to_token($email);\n" +
    							"	        }\n" +
    							"	    }\n" +
    							"	}\n" +
    							"\n" +
    							"	//パスワードをランダムパスワードで初期化。ユーザーへメール送信\n" +
    							"	if($_POST['sqlite3_reset1']){ \n" +
    							"		$mail = $_POST['sqlite3_reset1'];\n" +
    							"	    echo '<script type=\"text/javascript\">window.parent.window.parent.Login_echo1(\"'.$mail.' 登録済みパスワードを初期化します。\");</script>';\n" +
    							"		password_reset_and_send_mail($mail);\n" +
    							"		//echo '<script type=\"text/javascript\">window.parent.window.parent.Login_echo1(\"テスト2\");</script>';\n" +
    							"	}\n" +
    							"	\n" +
    							"	function password_reset_and_send_mail($mail){\n" +
    							"		//8桁ランダムパスワードの発行\n" +
    							"		$r_password = getRandomPassword(8);\n" +
    							"		\n" +
    							"		//ユーザ定義\n" +
    							"		$sqlite3_DB = '"+DB+"';\n" +
    							"		$sqlite3_id = '"+c1+"';\n" +
    							"		$sqlite3_pw = '"+c2+"';\n" +
    							"		$sqlite3_table = '"+from+"';\n" +
    							"\n" +
    							"		//$mail列のパスワードを「ランダムpassword」で初期化\n" +
    							"		$reset_sql1 = \"UPDATE \".$sqlite3_table.\" SET \".$sqlite3_pw.\"='\".$r_password.\"' where \".$sqlite3_id.\"='\".$mail.\"'\";\n" +
    							"		$db = new SQLite3($sqlite3_DB);\n" +
    							"		$result1 = $db->exec($reset_sql1);\n" +
    							"		if ($result1) {\n" +
    							"			$changed_num = $db->changes();		//変更された行の数: $db->changes()\n" +
    							"			\n" +
    							"			if($changed_num > 0){\n" +
    							"				//リセット成功　→　リセット済みパスワードの送信\n" +
    							"				echo '<script type=\"text/javascript\">window.parent.window.parent.Login_echo1(\"パスワードを初期化して<br>ご登録先へ送信しました。\");</script>';\n" +
    							"				mail_to_reset($mail,$r_password);\n" +
    							"			}else{\n" +
    							"				//リセット失敗\n" +
    							"				echo '<script type=\"text/javascript\">window.parent.window.parent.Login_echo1(\"<span style=\\\"color:red;\\\">初期化に失敗しました。しばらく経ってからもう一度お試しください。</span>\");</script>';\n" +
    							"			}\n" +
    							"		}\n" +
    							"		unset($db);\n" +
    							"	}\n" +
    							"\n" +
    							"	//DB登録未登録かどうかの判定\n" +
    							"    function isResistered($check_string, $database, $c1, $table) {\n" +
    							"		//SQLite3\n" +
    							"		//DB登録未登録のメールアドレスかどうかチェック\n" +
    							"   		$db = new SQLite3($database);\n" +
    							"		$sql = \"SELECT \".$c1.\" FROM \".$table.\" where \".$c1.\"='\".checkHTMLsc($check_string).\"'\";\n" +
    							"	    $result = $db->query($sql);\n" +
    							"	    $i = 0;\n" +
    							"	    while($res = $result->fetchArray(SQLITE3_ASSOC)){\n" +
    							"	          $i++;\n" +
    							"	          if($i==1)	break;\n" +
    							"	    }\n" +
    							"	    unset($db);\n" +
    							"	    if($i > 0)	return true;	//登録済\n" +
    							"	    else		return false;  	//未登録\n" +
    							"    }\n" +
    							"    \n" +
    							"    function mail_to_token($address) {\n" +
    							"        global $tokendir, $sender_name, $expire_time;\n" +
    							"      \n" +
    							"        $limit = (time()+$expire_time);\n" +
    							"        $token= rand(0,100).uniqid();		//トークン\n" +
    							"        touch($tokendir.$token.\".log\");		//トークンファイル作成\n" +
    							"    \n" +
    							"        $url = $_SERVER[\"HTTP_REFERER\"].\"?key=\".$token;\n" +
    							"        file_put_contents($tokendir.$token.\".log\", $address, LOCK_EX);	//期限保存\n" +
    							"        delete_old_token($tokendir);									//古いトークンの削除\n" +
    							"        $message=\"登録を完了するには、以下のアドレスを開いてください。\n\".($expire_time/60).\"分以内にアクセスが無かった場合は無効となります。\n\";\n" +
    							"        $message.= $url.\"\n\n\";\n" +
    							"\n" +
    							"        if($sender_name != \"\")	my_send_mail($address,'['.$sender_name.'] 登録確認',$message);\n" +
    							"        else					my_send_mail($address,'登録確認',$message);\n" +
    							"    }\n" +
    							"    \n" +
    							"    function mail_to_success($mailto) {\n" +
    							"        global $success_page, $resistration_page, $sender_name;\n" +
    							"       \n" +
    							"        //「メールアドレス」と「ランダムpassword」をDBへ登録\n" +
    							"        $random_password = register_email_and_random_password($mailto);\n" +
    							"      	if($random_password != \"\"){\n" +
    							"	        $message=\"登録が完了しました。\n\nログイン仮パスワード：\".$random_password.\" (※ログイン後に必ず変更してください）\n\n\";\n" +
    							"	        $message.=\"こちらからログインできます: \".$success_page.\"\n\n\";\n" +
    							"	        if($sender_name != \"\")	my_send_mail($mailto, '['.$sender_name.'] ご登録ありがとうございます', $message);\n" +
    							"	        else					my_send_mail($mailto, 'ご登録ありがとうございます', $message);\n" +
    							"	        return true;\n" +
    							"	    }else{\n" +
    							"	    	p('<font color=#ff0000>\\''.$mailto.'\\'の登録に失敗しました。既に登録済みの可能性があります。<br>もう一度<a href=\"'.$resistration_page.'\" target=\"_self\">こちら</a>からやり直してください。</font>');	//登録失敗（レアケース）\n" +
    							"	    	return false;\n" +
    							"	    }\n" +
    							"    }\n" +
    							"    \n" +
    							"    //「メールアドレス」と「ランダムpassword」をDBへ登録する\n" +
    							"    function register_email_and_random_password($mail) {\n" +
    							"		//8桁ランダムパスワードの発行\n" +
    							"		$r_password = getRandomPassword(8);\n" +
    							"		\n" +
    							"		//ユーザ定義\n" +
    							"		$sqlite3_DB = '"+DB+"';\n" +
    							"		$sqlite3_id = '"+c1+"';\n" +
    							"		$sqlite3_pw = '"+c2+"';\n" +
    							"		$sqlite3_table = '"+from+"';\n" +
    							"		\n" +
    							"		//「メールアドレス」と「ランダムpassword」をDBへ登録\n" +
    							"		//check & registration\n" +
    							"		$db = new SQLite3($sqlite3_DB);\n" +
    							"		//一応、再check\n" +
    							"		$sql1 = \"SELECT \".$sqlite3_id.\" FROM \".$sqlite3_table.\" where \".$sqlite3_id.\"='\".$mail.\"'\";\n" +
    							"	    $result1 = $db->query($sql1);\n" +
    							"	    $i=0;\n" +
    							"	    while($res = $result1->fetchArray(SQLITE3_ASSOC)){\n" +
    							"   	       $i++;\n" +
    							"   	       if($i==1)	break;\n" +
    							"	    }\n" +
    							"	    if($i > 0){\n" +
    							"	    	unset($db);\n" +
    							"	     	return \"\";		//既に登録済み（レアケース）\n" +
    							"	    }else{\n" +
    							"   	       //「メールアドレス」と「ランダムpassword」をDBへ登録\n" +
    							"   	       $sql2 = \"INSERT INTO \".$sqlite3_table.\" (\".$sqlite3_id.\", \".$sqlite3_pw.\") VALUES ('\".$mail.\"','\".$r_password.\"')\";\n" +
    							"   	       try{\n" +
    							"				$result2 = $db->exec($sql2);\n" +
    							"				unset($db);\n" +
    							"				return $r_password;	//登録成功\n" +
    							"   	       }catch(Exception $e){\n" +
    							"   	       		unset($db);\n" +
    							"   	       		return \"\";			//登録失敗\n" +
    							"   	       }\n" +
    							"	    }\n" +
    							"	}\n" +
    							"	function getRandomPassword($length){\n" +
    							"    	return substr(str_shuffle('1234567890abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 0, $length);\n" +
    							"	}\n" +
    							"\n" +
    							"    function mail_to_master($mail) {\n" +
    							"        global $admin_email, $sender_name;\n" +
    							"        \n" +
    							"        if($admin_email != \"\"){\n" +
    							"	        $message =\"新しく登録されたユーザーの情報は次の通りです。\n\";\n" +
    							"	        $message.=\"────────────────────────────────────────────────\n\";\n" +
    							"	        $message.= $mail.\"\n\n\";\n" +
    							"	        $message.=\"DATE: \".date(\"Y/m/d - H:i:s\").\"\n\";\n" +
    							"	        $message.=\"IP: \".$_SERVER['REMOTE_ADDR'].\"\n\";\n" +
    							"	        $message.=\"HOST: \".@gethostbyaddr($_SERVER['REMOTE_ADDR']).\"\n\";\n" +
    							"	        $message.=\"USER AGENT: \".$_SERVER['HTTP_USER_AGENT'].\"\n\";\n" +
    							"	        $message.=\"────────────────────────────────────────────────\n\";\n" +
    							"	      \n" +
    							"	        //my_send_mail(MAILTO_MASTER, 'ユーザー登録通知', $message);\n" +
    							"	        if($sender_name != \"\")	my_send_mail($admin_email, '['.$sender_name.' admin] ユーザー登録通知', $message);\n" +
    							"	        else					my_send_mail($admin_email, '[admin] ユーザー登録通知', $message);\n" +
    							"        }\n" +
    							"    }\n" +
    							"    \n" +
    							"    function mail_to_reset($mail,$random_password) {\n" +
    							"        global $sender_name,$success_page;\n" +
    							"       \n" +
    							"       	$message=\"ご登録パスワードを初期化しました。\n\nログイン仮パスワード：\".$random_password.\" (※ログイン後に必ず変更してください）\n\n\";\n" +
    							"	    $message.=\"こちらからログインできます: \".$success_page.\"\n\n\";\n" +
    							"      \n" +
    							"        if($sender_name != \"\")	my_send_mail($mail, '['.$sender_name.'] パスワード初期化通知', $message);\n" +
    							"        else					my_send_mail($mail, 'パスワード初期化通知', $message);\n" +
    							"    }\n" +
    							"    \n" +
    							"    function delete_old_token($token = NULL) {\n" +
    							"        global $tokendir,$email,$expire_time;\n" +
    							"        if (is_dir($tokendir)) {\n" +
    							"            if ($dh = opendir($tokendir)) {\n" +
    							"                while (($file = readdir($dh)) !== false) {\n" +
    							"                    if(is_file($tokendir.$file) && is_null($token)){\n" +
    							"                        $log = file_get_contents($tokendir.$file);\n" +
    							"                        list($data,$mail) = preg_split(\"<>\",$log);\n" +
    							"                        $email = $mail;\n" +
    							"                        if(time()> $data) @unlink($tokendir.$file);\n" +
    							"                      \n" +
    							"                    }else if(basename($file,\".log\")==$token && !is_null($token)){\n" +
    							"                        if(time() <(filemtime($tokendir.$token.\".log\")+$expire_time) ){\n" +
    							"                            $log = file_get_contents($tokendir.$token.\".log\");\n" +
    							"                            //p(\"log:\".$log);\n" +
    							"                           \n" +
    							"                            $mail = trim($log);\n" +
    							"                            $email = $mail;\n" +
    							"                      \n" +
    							"                            @unlink($tokendir.$token.\".log\");\n" +
    							"                            return true;\n" +
    							"                        }else{\n" +
    							"                            @unlink($tokendir.$token.\".log\");\n" +
    							"                            return false;\n" +
    							"                        }\n" +
    							"                    }\n" +
    							"                }\n" +
    							"                closedir($dh);\n" +
    							"            }\n" +
    							"        }\n" +
    							"    }\n" +
    							"    \n" +
    							"    function my_send_mail($mailto, $subject, $message) {\n" +
    							"        global $sender_name;\n" +
    							"       \n" +
    							"        $header =\"From: \".$sender_name.\" <info@example.com>\n\";\n" +
    							"        $mmm = mb_send_mail($mailto, $subject, $message, $header);\n" +
    							"    }\n" +
    							"\n" +
    							"	function p($str){\n" +
    							"	    //親ウインドウのJavaScript関数（テキストエリアへ書き込み）を呼び出す\n" +
    							"	    echo '<script type=\"text/javascript\">window.parent.Login_echo1(\"'.$str.'\");</script>';\n" +
    							"	}\n" +
    							"?>\n" +
    							"\n" +
    							"<script type=\"text/javascript\">\n" +
    							"$(document).ready(function(){\n" +
    							"	//アカウントあり or 新規登録 クリック時の処理\n" +
    							"	$('#signup1').click(function(){\n" +
    							"		$('#login1').html('<a href=\"\" style=\"color:#0000CC;\">ログイン</a>');\n" +
    							"		$('#signup1').html('<span style=\"font-weight:bold;\">新規登録</span>');\n" +
    							"		$('#loginTitle1').text('新規登録');\n" +
    							"		$('#password').val('');\n" +
    							"		$('#login_block').hide();\n" +
    							"		$('#signup_block').show();\n" +
    							"		Login_echo1(\"\");\n" +
    							"	});\n" +
    							"	$('#login1').click(function(){\n" +
    							"		$('#login1').html('<span style=\"font-weight:bold;\">ログイン</span>');\n" +
    							"		$('#signup1').html('<a href=\"\" style=\"color:#0000CC;\">新規登録</a>');\n" +
    							"		$('#loginTitle1').text('ログイン');\n" +
    							"		$('#mail1').val('');\n" +
    							"		$('#signup_block').hide();\n" +
    							"		$('#login_block').show();\n" +
    							"		Login_echo1(\"\");\n" +
    							"	});\n" +
    							"});\n" +
    							"\n" +
    							"//テキストエリアへ書き込み\n" +
    							"function Login_echo1(str){\n" +
    							"  var textArea = document.getElementById(\"Login_text1\");\n" +
    							//"  textArea.innerHTML =  str +\"<br>\";\n" +
		        				"  textArea.innerHTML = \"<h2>\" + str + \"</h2>\";\n" +
    							"}\n" +
    							"</script>\n" +
    							"<!-- Login & Registration end -->\n" +
    							"\n");
	        		}
	        				
    				//ログアウトボタンの付加
    				
					//通常時のみ（Prev/Nextでは行わない）
    				if(headerFlag==1)
    				header.append(
	        				"<!-- Logout start -->\n" +
	        				"<form method=\"post\" action=\"\" target=\"logout_ifr1\" id=\"LOGOUTpanel1\" name=\"LOGOUTpanel1\">\n" +
	        				"<input type=\"submit\" value=\" Logout \" name=\"sqlite3_logout1\" data-mini=\"false\" data-inline=\"false\">\n" +
	        				"<input type=\"hidden\" value=\" Logout \" name=\"sqlite3_logout1\">\n" +
	        				"</form>\n" +
	        				"<iframe name=\"logout_ifr1\" style=\"display:none;\"></iframe>\n" +
	        				"\n");
    				//通常時のみ（Prev/Nextでは行わない）&& ( header()があるとき || button("logout")があるとき )
    				if(headerFlag==1 && ( !Mobile_HTML5Function.headerString.equals("") || Mobile_HTML5Function.logoutButtonFlg ))
    				header.append(
    						"<script type=\"text/javascript\">\n" +
							"$(document).ready(function(){\n" +
	        				"	$('#LOGOUTpanel1').hide();\n" +
	        				"});\n" +
	        				"</script>\n"
    						);
    				//通常時のみ（Prev/Nextでは行わない）
	        		if(headerFlag==1)
	        		header.append("<?php\n" +
	        				"if($_POST['sqlite3_logout1']){\n" +
	        				"	//ログアウト処理\n" +
	        				"	//セッション変数を全て解除\n" +
	        				"	$_SESSION = array();\n" +
	        				"	//セッションを切断するにはセッションクッキーも削除\n" +
	        				"	//Note: セッション情報だけでなくセッションを破壊\n" +
	        				"	if (isset($_COOKIE[session_name()])) {\n" +
	        				"    	setcookie(session_name(), '', time()-42000, '/');\n" +
	        				"	}\n" +
	        				"	session_destroy();\n" +
	        				"	echo '<script type=\"text/javascript\">window.parent.history.go(0);</script>';	//reload\n" +
	        				"}\n" +
	        				"?>\n" +
	        				"<!-- Logout end -->\n");
	        				
					header.append(
	        				"<!-- \"Login & Logout\" end -->\n" +
	        				"\n" +
	        				"\n" +
	        				"<?php\n" +
	        				"//<!-- display_html start -->\n" +
	        				"function display_html(){\n" +
	        				"	if($_SESSION["+sessionVariable_UniqueName+"id] != \"\"){\n" +
	        				"		echo <<<EOF\n" +
	        				"		\n");
	        	}
	        	//else if(DBMS.equals("postgresql")){
	        	//	;
	        	//}
	        }
	        //added by goto 20130508  "Login&Logout" end
	        
	        if(headerFlag==1){		//通常時のみ（Prev/Nextでは行わない）
		        if(!Mobile_HTML5Function.headerString.equals("")){		//data-role="header"
		        	header.append("\n<!-- data-role=header start -->\n"
						+Mobile_HTML5Function.headerString+"<!-- data-role=header end -->\n\n\n");
		        }
	        }
	        //data-role="content"
	        header.append("<!-- data-role=content start -->\n<div data-role=\"content\" style=\"padding:0\" id=\"content1\">\n");
	        header.append("\n<div id=\"showValues\"><!-- ユーザ名等を表示 --></div>\n");	//ユーザ名

	        //commented out by goto  201203
//	        header.append("<div");
//	        header.append(div);
//	        header.append(titleclass);
//	        header.append(">");
	        //header.append(title);		//disuse
	        //tk end///////////////////////////////////////////////////////
	        //chie//

	        Log.out("--></style></head>");
	        Log.out("<body>");
        }

        if(Connector.loginFlag ){
        	header.append("<form action = \""+ GlobalEnv.getFileDirectory() + "/servlet/supersql.form.Session\" method = \"post\" name=\"theForm\">\n");
        	//header.append("<form action = \""+ GlobalEnv.getFileDirectory() + "/supersql.form.Session\" method = \"post\" name=\"theForm\">\n");
        	header.append("<input type=\"hidden\" name=\"tableinfo\" value=\"" + SSQLparser.get_from_info_st() + "\" >");
        	header.append("<input type=\"hidden\" name=\"configfile\" value=\"" + GlobalEnv.getconfigfile() + "\" >");
        }

        if(Connector.logoutFlag ){
        	header.append("<form action = \""+ GlobalEnv.getFileDirectory() + "/servlet/supersql.form.Session\" method = \"post\" name=\"theForm\">\n");
        	//header.append("<form action = \""+ GlobalEnv.getFileDirectory() + "/supersql.form.Session\" method = \"post\" name=\"theForm\">\n");
        	//header.append("<input type=\"hidden\" name=\"tableinfo\" value=\"" + SSQLparser.get_from_info_st() + "\" >");
        	header.append("<input type=\"hidden\" name=\"configfile\" value=\"" + GlobalEnv.getconfigfile() + "\" >");
        }


        if(Connector.insertFlag || Connector.deleteFlag || Connector.updateFlag){
        	header.append("<form action = \""+ GlobalEnv.getFileDirectory() + "/servlet/supersql.form.Update\" method = \"post\" name=\"theForm\">\n");
        	//header.append("<form action = \""+ GlobalEnv.getFileDirectory() + "/supersql.form.Update\" method = \"post\" name=\"theForm\">\n");
        	header.append("<input type=\"hidden\" name=\"tableinfo\" value=\"" + SSQLparser.get_from_info_st() + "\" >");
        	header.append("<input type=\"hidden\" name=\"configfile\" value=\"" + GlobalEnv.getconfigfile() + "\" >");
        	if(Connector.insertFlag)
        		header.append("<input type=\"hidden\" name=\"sql_param\" value=\"insert\" >");
        	if(Connector.deleteFlag)
        		header.append("<input type=\"hidden\" name=\"sql_param\" value=\"delete\" >");
        	if(Connector.updateFlag)
            	header.append("<input type=\"hidden\" name=\"sql_param\" value=\"update\" >");
        }

    }

    private void commonCSS() {
        //        header.append("TABLE {border-collapse:collapse; table-layout:fixed; border:none; height:100%;}\n");


    	//tk modified
    	//header.append(".nest { height:100%;z-index: 1}\n");
        //header.append(".nest { position : relative ; top ; 5px ; }");
    	//header.append(".nest {top : 5px ;}");

    	//comment out 200805 chie
    	//header.append(".nest {top : 5px ; vertical-align : top;}");

    	//        header.append(".outline { border: 2px solid gray; height:auto;}\n");
      	//        header.append("TD { padding: 0;}\n");
        //        header.append("TD.tate { border-top: 2px solid gray;}\n");
        //        header.append("TD.top { border-top: none;}\n");
        //        header.append("TD.yoko { border-left: 2px solid gray;}\n");
        //        header.append("TD.left { border-left: none;}\n");
        //tk//
    	if(!GlobalEnv.isOpt()){
        header.append(".att { padding: 0px; margin : 0px;height : 100%; z-index: 2}\n");
        header.append(".linkbutton {text-align:center; margin-top: 5px; padding:5px;}\n");
        header.append(".embed { vertical-align : text-top; padding : 0px ; margin : 0px; border: 0px,0px,0px,0px; width: 100%; }\n");
        header.append(".noborder { border-width : 0px; " +
        		"margin-top : -1px; padding-top : -1px; " +
        		"margin-bottom : -1px; padding-bottom : -1px; }\n");
    	}
    }

    public void getFooter(int footerFlag) {		//[headerFlag] 1:通常、2:Prev/Next
    	if(Connector.updateFlag || Connector.insertFlag|| Connector.deleteFlag || Connector.loginFlag ){
	    	footer.append("<input type=\"submit\" name=\"login\" value=\"Let's go!\">");
	    	footer.append("</form>\n");
	    	Log.out("</form>");
        	Connector.updateFlag = false;
        	Connector.insertFlag = false;
        	Connector.deleteFlag = false;
        	Connector.loginFlag = false;
    	}

    	if(Connector.logoutFlag ){
	    	footer.append("</form>\n");
	    	Log.out("</form>");
        	Connector.logoutFlag = false;
    	}

    	if(GlobalEnv.getframeworklist() == null){
    		if(footerFlag==1){		//通常時のみ（Prev/Nextでは行わない）
    			if(!noAd || !copyright.isEmpty())
    				footer.append("<hr size=\"1\">");
	    		if(!copyright.equals("")){	//copyrightを付加
	    			footer.append("<div>\n");
	    			footer.append("Copyright &COPY; "+copyright+" All Rights Reserved.\n");
	    			footer.append("</div>\n\n");
	    		}
	    		if(!noAd){
		    		//SuperSQLの宣伝を付加
		    		footer.append("<div style=\"font-size:11;\">\n");
		//    		footer.append("<a href=\""+filename.substring(0,filename.lastIndexOf(".html"))+"_sql.html\" target=\"_self\">This HTML</a> was generated by <a href=\"http://ssql.db.ics.keio.ac.jp/\" target=\"_self\">SuperSQL</a>\n");
		    		if(fff.equals(""))	footer.append("This HTML was generated by <a href=\"http://ssql.db.ics.keio.ac.jp/\" rel=\"external\">SuperSQL</a>\n");
		    		else				footer.append("<a href=\""+ fff +"\" target=\"_self\">This HTML</a> was generated by <a href=\"http://ssql.db.ics.keio.ac.jp/\" rel=\"external\">SuperSQL</a>\n");
					//footer.append("<a href=\""+filename+"_sql.html\" target=\"_self\">This HTML</a> was generated by <a href=\"http://ssql.db.ics.keio.ac.jp/\" target=\"_self\">SuperSQL</a>\n");
					footer.append("</div>\n\n");
	    		}
    		}
    		
    		footer.append("</div><!-- Close <div data-role=\"content\"> -->\n<!-- data-role=content end -->\n");		//Close <div data-role="content">
    		//data-role="footer"
    		
    		if(footerFlag==1 && Mobile_HTML5Function.footerString.equals("") && flickBarFlg)	//通常時のみ（Prev/Nextでは行わない）
    			Mobile_HTML5Function.footerString
//    			+= "<div data-role=\"footer\" data-position=\"fixed\" style=\"padding:11px 0px; border:1px solid gray; background:rgba(0,0,0,0.4);\" id=\"footer1\">\n" +
    			+= "<div data-role=\"footer\" data-position=\"fixed\" style=\"padding:11px 0px; background:gray; filter: alpha(opacity=25); -moz-opacity:0.25; opacity:0.25;\" id=\"footer1\">\n" +
    			   "<=&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Flick bar&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=>"+"\n" +
		    	   "</div>\n";
    		if(footerFlag==1 && !Mobile_HTML5Function.footerString.equals(""))	//通常時のみ（Prev/Nextでは行わない）
    			footer.append("\n\n<!-- data-role=footer start -->\n"+Mobile_HTML5Function.footerString+"<!-- data-role=footer end -->\n\n");
    		if(panel.length() != 0)															//20130503  Panel
    			footer.append("\n<!-- Panel start -->\n"+panel+"\n<!-- Panel end -->\n\n");	//Add panel contents.	
    		//added by goto 20130508  "Login&Logout"
    		if(SSQLparser.sessionFlag){
    			footer.append("\n" +
    					"EOF;\n" +
    					"	}//end if\n" +
    					"}//end function\n" +
    					"//<!-- display_html end -->\n" +
    					"?>\n");
    		}//else
    		footer.append("<iframe name=\"dummy_ifr\" style=\"display:none;\"><!-- dummy iframe for Form target --></iframe>\n");	//dummy iframe
    		footer.append(Mobile_HTML5Env.PHP);			//PHPストリングを付加			//added by goto 20130515  "search"
    		//footer.append(HTMLEnv.PHPpost+"\n?>\n");		//PHPpostストリングを付加		//added by goto 20130531
    		//footer.append(HTMLEnv.PHPfunc);		//PHPfuncストリングを付加		//added by goto 20130531
    		if(footerFlag==1)		//通常時のみ（Prev/Nextでは行わない）
    			footer.append("\n\n");
//	    		footer.append("\n<BR><BR>\n");
    		footer.append("</div><!-- Close <div data-role=\"page\"> -->\n<!-- data-role=page end -->\n");			//Close <div data-role="page">
    		footer.append("\n</BODY>\n</HTML>\n");
	        Log.out("</body></html>");
    	}
    }

    public void append_css_def_td(String classid, DecorateList decos) {
    	haveClass=0;
    	Log.out("[HTML append_css_def_att] classid=" + classid);
        Log.out("decos = " + decos);

        //��classid�Υ�����?�����Ȥ��������ꤷ�����Ȥ���?��
        if (written_classid.contains(classid)) {
            // �����?�ѤΥ�����?������
        	haveClass=1;
            Log.out("==> already created style");
            return;
        }else if(not_written_classid != null && not_written_classid.contains(classid)){
        	Log.out("==> style is null. not created style");
            return;
        }

        Log.out("==> new style");
        Log.out("@@ creating style no " + classid);

        StringBuffer cssbuf = new StringBuffer();

        //tk start////////////////////////////////////////////////////////////////
        StringBuffer metabuf = new StringBuffer();

        if (decos.containsKey("class")){
        	cssclass.put(classid,decos.getStr("class"));
        	Log.out("class =" + classid + decos.getStr("class"));
        }

		//changed by goto 20130703  ex) cssfile=" a.css; b.css "
		if (decos.containsKey("cssfile")) {
			String css = decos.getStr("cssfile").trim();
			if(!css.contains(",")){
				cssfile.delete(0, cssfile.length());
				if (GlobalEnv.isServlet()) {
					cssfile.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""
							+ GlobalEnv.getFileDirectory()
							+ css + "\">\n");
				} else {
					cssfile.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""
							+ css + "\">\n");
				}
			}else{
				if(!css.endsWith(","))	css+=",";
				while(css.contains(",")){
					cssfile.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + css.substring(0,css.indexOf(",")).trim() + "\">\n");
					css = css.substring(css.indexOf(",")+1);
				}
			}
        }else if(cssfile.length() == 0){
        	if(GlobalEnv.isServlet()){
            	cssfile.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + GlobalEnv.getFileDirectory() +"/default1.css \">\n");
            }else{
            	if(getOs().contains("Windows")){
            		cssfile.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"default1.css\">\n");
            	}else{
            		//commented out by goto 201303
//            		//itc
//            		if(GlobalEnv.isOpt())
//            			cssfile.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.db.ics.keio.ac.jp/ssqljscss/default_opt.css\">\n");
//            		else
//            			cssfile.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.db.ics.keio.ac.jp/ssqljscss/default1.css\">\n");
            	}
            }
        }
		
		//added by goto 20130703  ex) jsfile=" a.js; b.js "
		if (decos.containsKey("jsfile")) {
			String js = decos.getStr("jsfile").trim();
			if(!js.endsWith(","))	js+=",";
			while(js.contains(",")){
				jsFile.append("<script type=\"text/javascript\" src=\""	+ js.substring(0,js.indexOf(",")).trim() + "\"></script>\n");
				js = js.substring(js.indexOf(",")+1);
			}
		}
		
		//added by goto 20130703  ex) require=" a.css; a.js;  b.css; b.js "
		if (decos.containsKey("require")) {
			String file = decos.getStr("require").trim();
			if(!file.endsWith(","))	file+=",";
			String fileName = "";
			while(file.contains(",")){
				fileName = file.substring(0,file.indexOf(",")).trim();
				if(fileName.endsWith(".css"))
					cssjsFile.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + fileName + "\">\n");
				else if(fileName.endsWith(".js"))
					cssjsFile.append("<script type=\"text/javascript\" src=\"" + fileName + "\"></script>\n");
				else{
					//added by goto 20130710  ex) require="Folder name"
			        try{
			            String[] fileArray = new File(fileName).getAbsoluteFile().list();
			            for(int i = 0; i < fileArray.length; i++) {
			                if(fileArray[i].endsWith(".css"))
								cssjsFile.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + fileName + "/" + fileArray[i] + "\">\n");
							else if(fileArray[i].endsWith(".js"))
								cssjsFile.append("<script type=\"text/javascript\" src=\"" + fileName + "/" + fileArray[i] + "\"></script>\n");
			            }
			        }catch (Exception e){
			        	System.err.println("<Warning> require=に指定されたフォルダ「"+fileName+"」が見つかりません。");
			        }
				}
				
				file = file.substring(file.indexOf(",")+1);
			}
		}

        if (decos.containsKey("divalign") && div.length() == 0)
        	div.append(" align=" +decos.getStr("divalign"));

        //if (decos.containsKey("title") && title.length() == 0)	//disuse
        //	title.append(decos.getStr("title"));
        if (decos.containsKey("title_class"))
        	titleclass.append(" class=\""+decos.getStr("title_class")+"\"");
        if (decos.containsKey("tableborder") )//&& tableborder.length() == 0)
        	tableborder = decos.getStr("tableborder");

        //tk end//////////////////////////////////////////////////////////////

        //20131002
		if(!decos.containsKey("width")){
			if(!Mobile_HTML5Env.divWidth.equals(""))
				decos.put("width", Mobile_HTML5Env.divWidth);
	  	}
		Mobile_HTML5Env.divWidth = "";
		
		// ��??
        if (decos.containsKey("width")) {
        	if(GlobalEnv.getframeworklist() == null)
        		cssbuf.append(" width:" + decos.getStr("width") + ";");
        	else
        		cssbuf.append(" width:" + decos.getStr("width") + "px;");
        }

        // ��??
        if (decos.containsKey("height")){
        	if(GlobalEnv.getframeworklist() == null)
        		cssbuf.append(" height:" + decos.getStr("height") + ";");
        	else
        		cssbuf.append(" height:" + decos.getStr("height") + "px;");
        }


     // margin
        if (decos.containsKey("margin")) {
            cssbuf.append(" margin:" + decos.getStr("margin") + ";");
            //        } else {
            //            cssbuf.append(" padding:0.3em;");
        }

        // �ѥǥ��󥰡�;���
        if (decos.containsKey("padding")) {
            cssbuf.append(" padding:" + decos.getStr("padding") + ";");
            //        } else {
            //            cssbuf.append(" padding:0.3em;");
        }
        //padding
        if (decos.containsKey("padding-left")) {
            cssbuf.append(" padding-left:" + decos.getStr("padding-left") + ";");
        }
        if (decos.containsKey("padding-top")) {
            cssbuf.append(" padding-top:" + decos.getStr("padding-top") + ";");
        }
        if (decos.containsKey("padding-right")) {
            cssbuf.append(" padding-right:" + decos.getStr("padding-right") + ";");
        }
        if (decos.containsKey("padding-bottom")) {
            cssbuf.append(" padding-bottom:" + decos.getStr("padding-bottom") + ";");
        }

        // ������
        if (decos.containsKey("align"))
            cssbuf.append(" text-align:" + decos.getStr("align") + ";");

        // �İ���
        if (decos.containsKey("valign"))
            cssbuf.append(" vertical-align:" + decos.getStr("valign") + ";");

        // �طʿ�
        if (decos.containsKey("background-color"))
            cssbuf.append(" background-color:"
                    + decos.getStr("background-color") + ";");
        if (decos.containsKey("bgcolor"))
            cssbuf.append(" background-color:" + decos.getStr("bgcolor") + ";");

        // ʸ��
        if (decos.containsKey("color"))
            cssbuf.append(" color:" + decos.getStr("color") + ";");
        if (decos.containsKey("font-color"))
            cssbuf.append(" color:" + decos.getStr("font-color") + ";");
        if (decos.containsKey("font color"))
            cssbuf.append(" color:" + decos.getStr("font color") + ";");

        // ʸ����
        if (decos.containsKey("font-size"))
        	if(GlobalEnv.getframeworklist() == null)
        		cssbuf.append(" font-size:" + decos.getStr("font-size") + ";");
        	else
        		cssbuf.append(" font-size:" + decos.getStr("font-size") + "px;");
        if (decos.containsKey("font size"))
        	if(GlobalEnv.getframeworklist() == null)
        		cssbuf.append(" font-size:" + decos.getStr("font size") + ";");
        	else
        		cssbuf.append(" font-size:" + decos.getStr("font size") + "px;");
        if (decos.containsKey("size"))
        	if(GlobalEnv.getframeworklist() == null)
        		cssbuf.append(" font-size:" + decos.getStr("size") + ";");
        	else
        		cssbuf.append(" font-size:" + decos.getStr("size") + "px;");

        // ʸ�������
        if (decos.containsKey("font-weight"))
            cssbuf.append(" font-weight:" + decos.getStr("font-weight") + ";");

        // ʸ����?
        if (decos.containsKey("font-style"))
            cssbuf.append(" font-style:" + decos.getStr("font-style") + ";");
        if (decos.containsKey("font-family"))
            cssbuf.append(" font-family:" + decos.getStr("font-family") + ";");

        if(decos.containsKey("border"))
        	cssbuf.append(" border:" + decos.getStr("border")+";");
        if(decos.containsKey("border-width"))
        	cssbuf.append(" border-width:" + decos.getStr("border-width")+";");
        if(decos.containsKey("border-color"))
        	cssbuf.append(" border-color:" + decos.getStr("border-color")+";");
        if(decos.containsKey("border-style"))
        	cssbuf.append(" border-style:" + decos.getStr("border-style")+";");
        if(decos.containsKey("border-collapse"))
        	cssbuf.append(" border-collapse:" + decos.getStr("border-collapse")+";");

        //tk start////////////////////////////////////////////////////////////////
        //added by goto 20120715 start
//      if (decos.containsKey("charset"))
//      metabuf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + decos.getStr("charset") + "\">");
////      else
////       	metabuf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=euc-jp\">");
        if (decos.containsKey("charset"))
            charset=decos.getStr("charset");
        else if(!charsetFlg)
        		charset="UTF-8";		//default charset = UTF-8
        if(!charsetFlg && charset!=null){
        	//changed by goto 20130110 start
        	//metabuf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charset + "\">");
        	metabuf.append("<meta charset=\"" + charset + "\">");
        	//changed by goto 20130110 end
        	charsetFlg=true;
        }
//        if (decos.containsKey("charset")){
//            metabuf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + decos.getStr("charset") + "\">");
//            charset=decos.getStr("charset");
//            charsetFlg=1;
//        }else if(charsetFlg!=1){
//        		metabuf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=EUC-JP\">");
//        		charset="EUC-JP";		//default charset = EUC-JP
//        		charsetFlg=1;
//      	}
        //added by goto 20120715 end
        
        //added by goto 20130501  "style"
        if (decos.containsKey("style")){
        	String style = decos.getStr("style");
        	cssbuf.append(" " + style);
        	if(!style.matches(".*;\\s*$"))	cssbuf.append(";");	//最後に";"が無かった場合
        }
        
        //added by goto 20130411  "title"
        if (decos.containsKey("title"))
        	title = decos.getStr("title");
        
        //added by goto 20130311  "background"
        if (decos.containsKey("background"))
        	bg = decos.getStr("background");
        
        //added by goto 20130512  "max-width"
        try{
//	        if (decos.containsKey("max-width"))
//	        	maxWidth = Integer.parseInt(decos.getStr("max-width"));
//	        else if (decos.containsKey("maxwidth"))
//	        	maxWidth = Integer.parseInt(decos.getStr("maxwidth"));
	        if (decos.containsKey("portrait-width"))
	        	portraitWidth = Integer.parseInt(decos.getStr("portrait-width"));
	        else if (decos.containsKey("p-width"))
	        	portraitWidth = Integer.parseInt(decos.getStr("p-width"));
	        if (decos.containsKey("landscape-width"))
	        	landscapeWidth = Integer.parseInt(decos.getStr("landscape-width"));
	        else if (decos.containsKey("l-width"))
	        	landscapeWidth = Integer.parseInt(decos.getStr("l-width"));
	        if (decos.containsKey("pc-width"))
	        	pcWidth = Integer.parseInt(decos.getStr("pc-width"));
        }catch(Exception e){ /*数値以外*/ }
        
        if (decos.containsKey("description"))
            metabuf.append("\n<meta name=\"Description\" content=\"" + decos.getStr("description") + "\">");
        if (decos.containsKey("keyword"))
            metabuf.append("\n<meta name=\"Keyword\" content=\"" + decos.getStr("keyword") + "\">");
        if (decos.containsKey("author"))
        	metabuf.append("\n<meta name=\"Author\" content=\"" + decos.getStr("author") + "\">");
        if (decos.containsKey("copyright")){
        	copyright = decos.getStr("copyright");		//あとでfooterにappendする
        	metabuf.append("\n<meta name=\"Copyright\" content=\"" + copyright + "\">");
        }
        if (decos.containsKey("pragma"))
            metabuf.append("\n<meta http-equiv=\"Pragma\" content=\"" + decos.getStr("pragma") + "\">");
        if (decos.containsKey("robot"))
            metabuf.append("\n<meta name=\"Robot\" content=\"" + decos.getStr("robot") + "\">");
        
        //added by goto 20130519  "moveto"
        if (decos.containsKey("refresh")){
        	//<meta http-equiv="refresh" content="3; URL=http://ssql.db.ics.keio.ac.jp/mdemo/list.html">
        	metabuf.append("<meta http-equiv=\"refresh\" content=\""+decos.getStr("refresh")+"\">");
        }else if(!Mobile_HTML5Function.movetoFlg.equals("")){
        	metabuf.append(Mobile_HTML5Function.movetoFlg);
        	Mobile_HTML5Function.movetoFlg = "";
        }
        

        //20130518  "show query"
        if (decos.containsKey("showquery") || decos.containsKey("showq") || decos.containsKey("showquery&noshow") || decos.containsKey("showq&noshow") || decos.containsKey("show")  || decos.containsKey("query")){
        	String replaceStrings = "";
        	if(decos.containsKey("showquery&noshow"))
        		replaceStrings = decos.getStr("showquery&noshow");
        	else if(decos.containsKey("showq&noshow"))
        		replaceStrings = decos.getStr("showq&noshow");
        	
        	fff = filename;
			fff = fff.substring(0,fff.lastIndexOf(".html"));	//.htmlをカット
			//TODO: fffが相対パスだった場合、ファイルパスを取得してきてappend（相対パス → 絶対パス）
			
			String code = "";
			code += "<html>\n<head>\n";
			code += "<meta name=\"GENERATOR\" content=\" SuperSQL (Generate Mobile_HTML5) \">\n" +
					"<meta charset=\""+charset+"\">\n" +
					"<title>"+fff.substring(fff.lastIndexOf("/")+1)+".ssql</title>\n" +
					"\n" +
					"<style type=\"text/css\">\n" +
					"<!--\n" +
					"ol {\n" +
					"    position: absolute;\n" +
					"    top:40px;\n" +
					"	list-style-type: decimal-leading-zero;\n" +
					"	font-size: 15px;\n" +
					"	line-height: 18.03px;\n" +
					"}\n" +
					"li { \n" +
					"	margin: 0 0 2 2em;\n" +
					"}\n" +
					"body,ol {\n" +
					"	margin: 0px;\n" +
					"	width: 100%;\n" +
					"	background: #f0f0f0;						/* pngがインポートされなかったとき */\n" +
					"	background-image: url(http://www.db.ics.keio.ac.jp/ssqljscss/code_bg/code_bg1.png);\n" +
					"	overflow: auto;\n" +
					"}\n" +
					"#bgcolor { margin-left:35px; }\n" +
					"#b0 { color:gray; font-size:12; }\n" +
					"#b1 { color:honeydew; }\n" +
					"#b2 { color:lightgoldenrodyellow; }\n" +
					"#b3 { color:gray; }\n" +
					"#t1 { color:gray; font-size:19; }\n" +
					"-->\n" +
					"</style>\n" +
					"\n" +
					"<script src=\"http://code.jquery.com/jquery-1.7.1.min.js\"></script>\n" +
					"<script type=\"text/javascript\">\n" +
					"<!--\n" +
					"$(function(){\n" +
					"    $('#b1').mouseover(function(){\n" +
					"   		c(1,\"black\",\"gray\",\"honeydew\");\n" +
					"    });\n" +
					"    $('#b2').mouseover(function(){\n" +
					"    	c(2,\"black\",\"gray\",\"lightyellow\");\n" +
					"    });\n" +
					"    $('#b3').mouseover(function(){\n" +
					"    	c(3,\"white\",\"white\",\"black\");\n" +
					"    });\n" +
					"    function c(val,color1,color2,bg){\n" +
					"        $(\"body,ol\").css(\"background\",bg);		//pngがインポートされなかったとき\n" +
					"        $(\"body,ol\").css(\"background-image\",\"url(http://www.db.ics.keio.ac.jp/ssqljscss/code_bg/code_bg\"+val+\".png)\");\n" +
					"        $(\"ol\").css(\"color\",color1);\n" +
					"        $(\"#t1\").css(\"color\",color2);\n" +
					"    }\n" +
					"});\n" +
					"-->\n" +
					"</script>\n" +
					"</head>\n\n";
			code += "<body>\n" +
					"<div id=\"bgcolor\">\n" +
					"	<span id=\"b0\">Background-color:&nbsp;</span>\n" +
					"	<span id=\"b1\">■</span>\n" +
					"	<span id=\"b2\">■</span>\n" +
					"	<span id=\"b3\">■</span>\n" +
					"</div>\n" +
					"\n" +
					"<pre>\n" +
					"<code>\n" +
					"\n" +
					"<ol>\n" +
					"<span id=\"t1\">"+fff.substring(fff.lastIndexOf("/")+1)+".ssql</span>";
			//create HTML file
			try {
				//Log.i("create HTML file エンコードcharset:"+charset);
	    		PrintWriter pw;
	            if (charset != null)
		        	pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
		        			new FileOutputStream(fff+"_sql.html"),charset)));
	            else
	            	pw = new PrintWriter(new BufferedWriter(new FileWriter(
	        	                    fff+"_sql.html")));
	        	pw.println(code);
	        	
	            BufferedReader br = null;
	            try{
	            	  //TODO: file-encodingを取得して第二引数へ反映させる処理
	            	  br = new BufferedReader(new InputStreamReader(new FileInputStream(fff+".ssql"), "UTF-8"));		//fileを開く
//		              br = new BufferedReader(new InputStreamReader(new FileInputStream(fff+".ssql"), charset));		//fileを開く
		              String queryString = new String();
		              int c;
		              while ((c = br.read()) != -1)	queryString += ((char) c);
		              
		              //***へ置換
		  			  //Log.i("replaceStrings: "+replaceStrings);
					  replaceStrings = replaceStrings.trim();
					  if(!replaceStrings.equals("")){
						  if(!replaceStrings.endsWith(";"))		replaceStrings += ";";
						  while(replaceStrings.contains(";")){
							  queryString = queryString.replaceAll(replaceStrings.substring(0,replaceStrings.indexOf(";")).trim(),"***");
							  replaceStrings = replaceStrings.substring(replaceStrings.indexOf(";")+1);
						  }
					  }
					  //書き込み
					  pw.println( queryString.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("^", "<li>").replaceAll("\n", "\n<li>") );
	            }finally{
		              br.close();
	            }
	        	
	            pw.println("</ol>\n\n</code>\n</pre>\n</body>\n</html>");
	            pw.close();
	        } catch (Exception e) { /*Log.i("Create HTML failed: "+e);*/ }
			
        	//HTMLfilenameを絶対パスから「相対パス形式」へ変更
			String fileDir = new File(filename).getAbsoluteFile().getParent();
			if(fileDir.length() < filename.length()
			&& fileDir.equals(filename.substring(0,fileDir.length()))){
//				String relative_path_filename = filename.substring(fileDir.length()+1);
//				fff = relative_path_filename;
				//code.append("<A href=\"" + relative_path + "\" ");
				//Log.i("relative_path: "+relative_path_filename);
				fff = filename.substring(fileDir.length()+1);
			}else
				fff = filename;
				//Log.i("linkurl: " + filename);
				//code.append("<A href=\"" + htmlEnv.linkUrl + "\" ");
			fff = fff.substring(0,fff.lastIndexOf(".html"));	//.htmlをカット
			fff += "_sql.html";
        	
        }
        
        //20130521  "flickbar"
        //if (decos.containsKey("nobar") || decos.containsKey("noflick") || decos.containsKey("noflickbar"))
        if (decos.containsKey("flickbar"))
        	flickBarFlg = true;
        
        //20131106
        if (decos.containsKey("noad") || decos.containsKey("noAd"))
            noAd = true;
        
        
        if (cssbuf.length() > 0) {
        	haveClass = 1;
            //����?�Υ�����?����
            css.append("." + classid + "{");

            css.append(cssbuf);
            //��?�Υ�����?�Ĥ�
            css.append(" }\n");

            //������?��?�Ѥߥ��饹��id����¸���Ƥ���
            written_classid.addElement(classid);
        }else{
        	Log.out("==> style is null. not created style");
        	not_written_classid.addElement(classid);
        }

        //tk start//////////////////////////////////////////////////////////
        if(metabuf.length() > 0)
        {
            //meta.append(" ");		//commented out by goto 201303
            meta.append(metabuf);
         	meta.append("\n");

        }
       //tk end////////////////////////////////////////////////////////////


    }

    // outline����Ϥ�?���ɤ����Υե饰��?
    boolean OutlineMode = false;

    public void setOutlineMode() {
        OutlineMode = true;
    }

    public String getOutlineMode() {
        if (OutlineMode) {
            OutlineMode = false;
            return "";
        }
//        return " frame=void class=nest ";
      return " frame=void ";
    }

    public String getOutlineModeAtt() {
        if (OutlineMode) {
            OutlineMode = false;
            return " outline";
        }
        return "";
    }

    public static String getClassID(ITFE tfe) {
    	String result;
        if (tfe instanceof Mobile_HTML5C3) {
            result = getClassID(((ITFE) ((Mobile_HTML5C3) tfe).tfes.get(0)));
            return result;
        }
        if (tfe instanceof Mobile_HTML5G3) {
            result = getClassID(((ITFE) ((Mobile_HTML5G3) tfe).tfe));
            	return result;
        }
        result =  "TFE" + tfe.getId();
        	return result;
    }

    /***start oka***/
    public static String getDataID(ITFE tfe) {
    	String ClassID;
    	int DataID = 0;
    	String return_value;

        if (tfe instanceof Mobile_HTML5C3) {
            return getClassID(((ITFE) ((Mobile_HTML5C3) tfe).tfes.get(0)));
        }
        if (tfe instanceof Mobile_HTML5G3) {
            return getClassID(((ITFE) ((Mobile_HTML5G3) tfe).tfe));
        }
        ClassID = String.valueOf(tfe.getId());
        DataID = Integer.valueOf((ClassID.substring(ClassID.length()-3,ClassID.length()))).intValue();

        Log.out("ClassID="+ClassID);
        Log.out("DataID="+DataID);
        Log.out("ID_counter="+ID_counter);

        if(DataID < ID_old){
        	ID_counter = DataID;
        }
        else{
        	if(DataID != ID_counter && DataID > ID_counter){
        		DataID = ID_counter;
        	}
        }
        ID_counter++;
        ID_old = DataID;
        return_value = String.valueOf(DataID);
        return return_value;
    }


    /********  form method  ************/
    /********** 2009 chie **************/

    //
    public static void initAllFormFlg(){
    	setFormItemFlg(false,null);
    	setSelectFlg(false);
    	setSelectRepeat(false);
    	setFormValueString(null);
    	setFormPartsName(null);
    	setSelected("");
    	setIDU("");
    	form_parts_number = 1;
    	exchange_form_name = new String();
    	form_detail = new String[256];
    	form_number = 1;
    	nameId = "";
    	search = false;
    	searchid = 0;
    	cond_name="";
    	cond ="";
    }


    static boolean isFormItem;
    static String formItemName;
    //form tag is written : true
    public static void setFormItemFlg(boolean b,String s){
    	isFormItem = b;
    	formItemName = s;
    	return;
    }

    public static boolean getFormItemFlg(){
        return isFormItem;
    }

    public static String getFormItemName(){
    	if(formItemName == null){
    		return "0";
    	}
    	return formItemName;
    }

	static boolean select_flg;
	//function select flg -> in func_select true

	//set and get select_flg
	public static void setSelectFlg(boolean b){
		select_flg = b;
	}

	public static boolean getSelectFlg(){
		return select_flg;
	}


	static String formValueString;
	public static void setFormValueString(String s){
		formValueString = s;
	}
	public static String getFormValueString(){
		return formValueString;
	}



	static boolean select_repeat;
	//select_repeat flag
	//not write "<tr><td>" between "<option>"s
	//set and get select_repeat
	public static void setSelectRepeat(boolean b){
		select_repeat = b;
	}
	public static boolean getSelectRepeat(){
		return select_repeat;
	}

	//global form item number : t1,t2,t3...
	static int form_parts_number = 1;
	static String form_parts_name = null;
	public static String getFormPartsName(){
		if(form_parts_name == null){
			return "t"+form_parts_number;
		}else{
			return form_parts_name;
		}
	}
	public static void incrementFormPartsNumber(){
		form_parts_number++;
	}


	public static void setFormPartsName(String s){
		form_parts_name = s;
	}

	private static String exchange_form_name = new String();
	public static void exFormName(){
		String s = "t" + form_parts_number + ":" + form_parts_name +":";
		if(exchange_form_name == null || exchange_form_name.isEmpty()){
			exchange_form_name = ":"+s;
		}else{
			if(!exchange_form_name.contains(s))
				exchange_form_name += s;
		}
	}
	public static String exFormNameCreate(){
		String ret = new String();
		if(exchange_form_name != null){
			ret = "<input type=\"hidden\" name=\"exchangeName\" value=\""+exchange_form_name+"\" />";
			setFormDetail(ret);
			return ret;
		}else{
			return null;
		}
	}



	//global form number : 1,2,3...
	static int form_number = 1;
	public static void incrementFormNumber(){
		form_number++;
	}

	public static int getFormNumber(){
		//return formNumber 1,2,3...
		return form_number;
	}
	public static String getFormName(){
		//return formNumber f1,f2,f3...
		return "f"+form_number;
	}

	static String[] form_detail = new String[256];
	public static void setFormDetail(String s){
		if(form_detail[form_number] == null)
			form_detail[form_number] = s;
		else
			form_detail[form_number] += s;
	}
	public static String getFormDetail(int i){
		return form_detail[i];
	}

	static String IDUst = new String();
	public static void setIDU(String s){
		IDUst = s;
	}

	public static String getIDU(){
		return IDUst;
	}

	static String selected = "";

	public static void setSelected(String s){
		selected = s;
	}
	public static String getSelected(){
		return selected;
	}

	static String nameId = "";
	public static String getNameid(){
		if(nameId != null){
			return nameId;
		}else{
			return "";
		}
	}

	static String checked = "";

	public static void setChecked(String s){
		System.out.println("checked:"+s);
		checked = s;
	}
	public static String getChecked(){
		return checked;
	}

	static boolean search = false;
	static int searchid = 0;
	static String cond_name = "";
	static String cond = "";

	public static void setSearch(boolean b){
		search = b;
		searchid = 0;
	}
	public static boolean getSearch(){
		return search;
	}
	
	
	//goto 20131123
	public String getFileName(){
		//absolute path filename (/home/---/XXX.html)
		return filename;
	}
	public String getFileName1(){
		//absolute path filename (/home/---/XXX.html)
		return getFileName();
	}
	public String getFileName2(){
		//absolute path filename (/home/---/XXX)
		return getFileName().substring(0, getFileName().lastIndexOf("."));
	}
	public String getFileName3(){
		//file name (XXX.html)
		return new File(getFileName1()).getName();
	}
	public String getFileName4(){
		//file name (XXX)
		return new File(getFileName2()).getName();
	}
	public String getFileParent(){
		//file path (/home/---/)
		return new File(filename).getParent();
	}

}