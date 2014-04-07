package supersql.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

import supersql.codegenerator.AttributeItem;
import supersql.codegenerator.CodeGenerator;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5Function;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.db.SQLManager;
import supersql.extendclass.ExtList;

public class SSQLparser {

	private String commentOutLetters = ""+GlobalEnv.COMMENT_OUT_LETTER+GlobalEnv.COMMENT_OUT_LETTER;	//="--"
    
    //added by goto 20130508  "Login&Logout"
	public static boolean sessionFlag = false;
	public static String sessionString = "";
	
	//goto 20130915  "<? - ?>"
	public static ArrayList<String> textString = new ArrayList<String>();
	public static int textNum = 0;
	
	private static boolean dbpediaQuery = false;
	private static String fromInfoString;
	public static String DB2_XQUERY = new String();
	public static int numXpath = 0;
	public static String tmpXmlQuery1 = new String();

	public static String tmpXmlQuery2 = new String();
	public static String tmpXpath1 = new String();
	public static String XMLQuery = new String();

	public static int xmlTextFlag = 0;
	public static String Xpath = new String();
	public static String[] xpath_query = { "" };

	public static int xpathExist = 0;
	public static String XpathQuery;
	public static int xpathStart = 0;
	public static String xpathTag = new String();

	public static int xpathTagExist = 0;

	private CodeGenerator codeGenerator;

	private StringBuffer embedFrom = new StringBuffer();
	private StringBuffer embedGroup = new StringBuffer();
	private StringBuffer embedHaving = new StringBuffer();
	private StringBuffer embedWhere = new StringBuffer();
	private boolean foreachFlag = false;
	private String foreachFrom = "";

	private ForeachInfo foreachInfo;
	private String foreachWhere = "";

	private FromInfo fromInfo;
	private String groupStatement;
	private String havingStatement;
	private String media;
	private String QueryImage;

	private int tableNum = 0;

	private TFEparser tfeInfo;
	private WhereInfo whereInfo = new WhereInfo();

	private StringBuffer from_c = new StringBuffer();
	private StringBuffer where_c = new StringBuffer();
	private StringBuffer order_c = new StringBuffer();
	private StringBuffer group_c = new StringBuffer();
	private StringBuffer having_c = new StringBuffer();
	
	private static boolean distinct = false;

	public static String get_from_info_st() {
		if (fromInfoString == null) {
			return "";
		}
		return fromInfoString;
	}

	public static boolean isDbpediaQuery() {
		return dbpediaQuery;
	}

	public static void set_from_info_st(String fi) {
		fromInfoString = fi;
	}

	public static void setDbpediaQuery(boolean dbpediaQuery) {
		SSQLparser.dbpediaQuery = dbpediaQuery;
	}

	public SSQLparser() {
		parseSSQL(this.getSSQLQuery(), 10000);
	}

	public SSQLparser(int id) {
		parseSSQL(this.getSSQLQuery(), id);
	}

	public SSQLparser(String a) {
		parseSSQL(this.getSSQLQuery2(), 10000);
	}

	public SSQLparser(StringBuffer querybuffer) {
		parseSSQL(querybuffer.toString(), 10000);
	}

	private String getSSQLQuery() {

		String query = GlobalEnv.getQuery();
		if (query != null) {
			query = query.trim();
		}

		String filename = GlobalEnv.getfilename();
		if (filename == null || filename.isEmpty()) {
			System.err.println("Error[SQLparser]: File Is Not Specified.");
			GlobalEnv.addErr("Error[SQLparser]: File Is Not Specified.");
			return "";
		}

		Log.info("[Parser:Parser] filename = " + filename);
		BufferedReader in;
		StringBuffer tmp = new StringBuffer();
		try{
			//in = new BufferedReader(new FileReader(filename));
			//TODO: file-encoding鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃夙随申任鐃緒申鐃緒申鐃緒申鐃緒申 鐃緒申鐃緒申JISAutoDetect: 文鐃緒申鐃宿の種申動判鐃緒申鐃�eader鐃緒申鐃初ス鐃塾わ申鐃緒申鐃術可￥申
			// 鐃緒申鐃緒申鐃瞬ワ申奪箸妊螢�申鐃緒申鐃緒申肇如鐃緒申鐃緒申鐃淑醐申鐃緒申匹鐃銃逸申儡鐃緒申鐃緒申鐃緒申匹濆鐃緒申鐃緒申鐃�			//BufferedReader
			//InputStream is = getInputStream(filename);
			//in = new BufferedReader(new InputStreamReader(getInputStream(filename), "JISAutoDetect"));
			//in = new BufferedReader(new InputStreamReader(res.getInputStream(), "JISAutoDetect"));
			// 鐃緒申鐃緒申鐃瞬ワ申奪箸妊譽刻申櫂鵐好如鐃緒申鐃緒申鐃�UC鐃緒申鐃緒申鐃宿わ申鐃術器申鐃緒申鐃銃書き刻申鐃緒申鐃緒申
			//PrintWriter out = new PrintWriter(new OutputStreamWriter(res.getOutputStream(), "EUC-JP"))
			in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));		//changed by goto 20130519 (This is an important change.)

			String line = null;
			while (true){
				line = in.readLine();
				if (line == null)	break;
				
				if (line.contains("/*")){
					String line1 = line.substring(0, line.indexOf("/*"));
					while (!line.contains("*/"))
						line = in.readLine();
					line = line1 + line.substring(line.indexOf("*/") + 2);
				}
				
//				//goto 20130915  "<?  ?>", "<$  $>"
//				if (line.startsWith("<?")){
//					String line1 = line.substring(0, line.indexOf("<?"));
//					String buf = "";
//					while (!line.startsWith("?>")){
//						line = in.readLine();
//						buf += line+"\n";
//					}
//					buf = buf.substring(0,buf.lastIndexOf("?>"));	//substring last '?>'
//					textString.add(textNum, buf);
//					line = line1 + "text(\"#TextLabel_"+textNum+"\")!" + line.substring(line.indexOf("?>") + 2);	//add label
//					//Log.i("textString.get("+textNum+") = \n"+textString.get(textNum));
//					textNum++;
//				} 
				
				// #import  by goto 201312
				line = Import.checkImportString(in, line);
				
				if (line.contains(commentOutLetters) || line.contains("\\\"") || line.contains("\"\"")){	//commentOutLetters = "--"
					boolean dqFlg = false;
					int i = 0;
					for (i=0; i < line.length(); i++){
						if (!dqFlg && line.charAt(i) == '"' && i>0 && i<line.length()-1
								&& (line.charAt(i-1) != '\\' && line.charAt(i-1) != '"' && line.charAt(i+1) != '"'))	//omit \" and ""
							dqFlg = true;
						else if (dqFlg && line.charAt(i) == '"' && i>0 && i<line.length()-1
								&& (line.charAt(i-1) != '\\' && line.charAt(i-1) != '"' && line.charAt(i+1) != '"'))	//omit \" and ""
							dqFlg = false;

						if(dqFlg && i>0 && (line.charAt(i-1)=='\\' || line.charAt(i-1) == '"') && line.charAt(i)=='"')	//if \" or ""
							line = line.substring(0,i-1)+"&quot;"+line.substring(i+1,line.length());
						else if(!dqFlg && i>0 && line.charAt(i-1) == '<' && line.charAt(i)=='$'){	//if <$
//							line = line.substring(0,i-1)+"&quot;"+line.substring(i+1,line.length());
						
						}
						else if (!dqFlg && i < line.length()-1 && line.charAt(i)==GlobalEnv.COMMENT_OUT_LETTER && line.charAt(i+1)==GlobalEnv.COMMENT_OUT_LETTER)
							break;
					}
					line = line.substring(0, i);
				}
				
//				//goto 20130915  "<$  $>"
//				//"内は読み飛ばす
//				//TODO 下記をもっとスマートに
//				boolean onlyTextFlg = false;	//This line has only text string.
//				while (line.contains("<$") && !onlyTextFlg){
////				while ((line.contains("<$") || line.contains("$$")) && !onlyTextFlg){
////				if (line.contains("<$")){
//					String line1 = "";
//					String buf = "";
//					
//					int i;
//					int lineCount=0;
//					char c0,c1=' ';
//					boolean dqFlg = false;
//					boolean startFlg = false;
//					boolean endFlg = false;
//					while (true){
//						
//						boolean $$Flg = false;
//						boolean $$endFlg = false;
//						String $$Buf = "";
////						while(true){
//						if(line.contains("$$")){
//							while(!$$endFlg){
//								//TODO: これを簡略化(このifの下のループで一度に処理する)
//								//Log.e("	In line.contains($$)!!");
//	//							boolean $$Flg = false;
//								for (i=0; i < line.length(); i++){
//									c0 = line.charAt(i);
//									if(i<line.length()-1)	c1 = line.charAt(i+1);
//									if (c0 == '"' && !dqFlg)		dqFlg = true;
//									else if (c0 == '"' && dqFlg)	dqFlg = false;
//									
//									//$$ -> ?>  or  $$ -> !<?
//									if(!dqFlg && i<line.length()-1 && c0=='$' && c1=='$'){
//										if(!$$Flg)	$$Flg = true;
//										else		$$Flg = false;
//										if($$Flg){
//											line = line.substring(0,i)+"$>"+line.substring(i+2);
////											$$Buf += line;
//										}
//										else{
//											String s = line.substring(0,i);
//											if(!s.trim().endsWith("!") && !s.trim().endsWith(","))
//												s += "!";	//default
//	//										Log.e("		s: "+s);
//											line =  s + "<$"+line.substring(i+2);
//											//Log.e("		l: "+line);
//											$$Buf += line;
////											tmp.append(" " + line);
//	//										line = line.substring(0,i)+"! <$"+line.substring(i+2);
//										}
//									}
//								}
//								c0 = ' ';
//								c1 = ' ';
//								//Log.e("	In line.contains($$)!!: "+line+" "+$$Flg);
//								if($$Flg){//TODO:　閉じられてないので閉じられるまで改行
//									line = in.readLine();
//								}else{
//									$$endFlg = true;
//									if(line.endsWith("!"))	line=line.substring(0,line.lastIndexOf("!"));
////									Log.e(line);
//									line = "{ "+line+" }@{text}!";
////									Log.e(line);
////									line = $$Buf;
////									$$Buf = "";
//									break;
//								}
//							}
//						}
//						
//						for (i=0; i < line.length(); i++){
//							c0 = line.charAt(i);
//							if(i<line.length()-1)	c1 = line.charAt(i+1);
//							if (c0 == '"' && !dqFlg)		dqFlg = true;
//							else if (c0 == '"' && dqFlg)	dqFlg = false;
//							
////							//$$ -> ?>  or  $$ -> <?
////							if(!dqFlg && i<line.length()-1 && c0=='$' && c1=='$' && startFlg){
////								lineBuf = line.substring(0,i);
////								for(int j=i;j<line.length();j++ )
////									;
////							}
//							
//							if(!dqFlg && i<line.length()-1 && c0=='<' && c1=='$')	//if <?
//								startFlg = true;
//							else if(!dqFlg && i<line.length()-1 && c0=='$' && c1=='>'){ //if ?>
//								endFlg = true;
//								break;
//							}
////							if(!dqFlg && i<line.length()-1 && (c0=='<' || c0=='$') && c1=='$')	//if <? or $$
////								startFlg = true;
////							else if(!dqFlg && i<line.length()-1 && c0=='$' && (c1=='>' || c1=='$')){ //if ?> or $$
////								endFlg = true;
////								break;
////							}
//							if(startFlg)	buf += c0;
//							else			line1 += c0;
//						}
//						if(endFlg)	break;
//						if(lineCount==0){
//							if(!startFlg){
////								Log.e("	match!!!"+line1);
//								onlyTextFlg = true;
//								break;
//							}
//						}
//						line = in.readLine();
//						buf += "\n";
//						lineCount++;
//					}
//					if(!onlyTextFlg){
////						Log.e("buf = "+buf);
//						buf = buf.substring(2);	//substring first '<?'
//						//buf = buf.substring(buf.indexOf("<$")+2);	//substring first '<?'
//						textString.add(textNum, buf);
//						line = line1 + "text(\"#TextLabel_"+textNum+"\")!" + line.substring(i+2);	//add label
////						line = line1 + "<\"#TextLabel_"+textNum+"\">!" + line.substring(i+2);	//add label
////						line = line1 + "'#TextLabel_"+textNum+"'!" + line.substring(i+2);	//add label
//						//Log.e("line = "+line);
//						//Log.e("textString.get("+textNum+") = "+textString.get(textNum));
//						textNum++;
//					}
//				}//End of 'if (line.contains("<$")){'
				
				tmp.append(" " + line);
			}
			in.close();
			query = tmp.toString().trim();
		} catch (FileNotFoundException e) {
			System.err.println("Error[SQLparser]: File(" + filename	+ ") Is Not Found.");
			GlobalEnv.addErr("Error[SQLparser]: File(" + filename + ") Is Not Found." + e);
			return "";
		} catch (IOException e) {
			GlobalEnv.addErr("Error[SQLparser]:" + e);
		}

		if (query.endsWith(";")) {
			query = query.substring(0, query.length() - 1).trim();
		}

		Log.info("[Parser:Parser] ssql statement = " + query);
		
		// #import  by goto 201312
		query = Import.importProcess(query);
		
		//goto 20130915-2  "<$  $>"
		query = Embed.checkEmbed(query);

		//goto
		media = getGenereteMedia(query);
		if(media.endsWith("{")){
			//generate MEDIA{　->　generate MEDIA {
			media = media.substring(0,media.length()-1);
			query = query.replace(media+"{", media+" {");
		}
		media = media.toLowerCase();
		if(media.equals("html") || media.equals("mobile_html5")){
			query = replaceQuery_For_HTML_and_MobileHTML5(query);
			while(query.contains(") ] }@{text}"))						//TODO
				query = query.replace(") ] }@{text}", ") ]! }@{text}");	//TODO
			
		}
		//Log.e("query = "+query);
		return query;
	}

	// goto
	// return Generate media name
	private String getGenereteMedia(String query){
		try{
			StringTokenizer st = new StringTokenizer(query);
			while(st.hasMoreTokens()){
				if(st.nextToken().toString().toLowerCase().equals("generate"))
					return st.nextToken().toString();
			}
		}catch(Exception e){ }
		return "";
	}
	// replaceQuery_For_HTML_and_MobileHTML5
	private String replaceQuery_For_HTML_and_MobileHTML5(String query){
		
		//check by one character and replace
		boolean dqFlg = false;
		boolean exclamationFlg = false;
		boolean squareBracketsFlg = false;
		int exclamationNum = 0;
		char c;
		for (int i = 0; i < query.length(); i++) {
			c = query.charAt(i);
			if (c == '"' && !dqFlg)		dqFlg = true;
			else if (c == '"' && dqFlg)	dqFlg = false;
			
			// () -> ("")
			if (!dqFlg && i < query.length() - 1 && (c == '(' && query.charAt(i+1) == ')'))
				query = query.substring(0,i+1) + "\"\"" + query.substring(i+1);
			
			//20130915
	    	// "\n" -> "<BR>"
			if (dqFlg && i < query.length() - 2 && (query.charAt(i-1) != '\\' && c == '\\' && query.charAt(i+1) == 'n'))	//exclude '\\n'
				query = query.substring(0,i) + "<BR>" + query.substring(i+2);
			
			//20130915  for last '!'
			// ! }  ->  }   or  ! ]  ->  ]   or  ! FROM  ->  FROM
			if(!exclamationFlg && !dqFlg && !squareBracketsFlg && c == ']')		squareBracketsFlg = true;	//check squareBracketsFlg
			else if(!exclamationFlg && !dqFlg && squareBracketsFlg && c == '!')	squareBracketsFlg = false;
			else 
				if (!dqFlg && i > 2 && !exclamationFlg && !squareBracketsFlg && c == '!'){
				//check exclamation -> true
 				exclamationNum = i;
 				exclamationFlg = true;
			}else if(exclamationFlg && !Character.isWhitespace(c) && c != '}' && c != ']' && 
					i < query.length() - 4 && Character.toLowerCase(c) != 'f' && Character.toLowerCase(query.charAt(i+1)) != 'r' && 
							Character.toLowerCase(query.charAt(i+2)) != 'o' && Character.toLowerCase(query.charAt(i+3)) != 'm'){
				//check exclamation -> false
				exclamationNum = 0;
				exclamationFlg = false;
			}else if(exclamationFlg && (
					c == '}' || c == ']' ||
					(i < query.length() - 4 && Character.toLowerCase(c) == 'f' && Character.toLowerCase(query.charAt(i+1)) == 'r' && 
							Character.toLowerCase(query.charAt(i+2)) == 'o' && Character.toLowerCase(query.charAt(i+3)) == 'm') )){
				//replace:  ! }  ->  }  or  ! ]  ->  ]  or  ! FROM  ->  FROM
				query = query.substring(0,exclamationNum) +" "+ query.substring(i);
				exclamationFlg = false;
			}
		}
		
		//20130915  for last '!'
		// if query.endsWith '!' -> ''
		if(query.endsWith("!"))	query = query.substring(0,query.length()-1);
		
		//check by one word and replace
//		try{
//			StringTokenizer st = new StringTokenizer(query);
//			String s = "";
//			while(st.hasMoreTokens()){
//				s = st.nextToken().toString().toLowerCase();
//				Log.i(s);
//			}
//		}catch(Exception e){ }
		
		
		//added by goto For "slideshow"
		if(media.equals("mobile_html5") && query.contains("slideshow")){
			// TODO: 1."sslideshow"鐃緒申離潺鐃緒申鐃緒申鐃緒申彁鐃緒申離鐃緒申蕁蕊緒申鐃緒申鐃�.鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申表鐃緒申鐃緒申鐃宿わ申鐃緒申鐃緒申判鐃緒申
			
			// 20130122
			// 鐃瞬器申: replaceAll
			// <鐃緒申鐃緒申表鐃緒申>
			// 0文鐃緒申幣鐃緒申任鐃春わ申文鐃緒申鐃緒申.*
			// 0鐃縦以常申龍鐃緒申鐃�\s*
			// ( )鐃叔囲っわ申鐃緒申分鐃熟￥申S1,鐃緒申$2鐃緒申箸鐃緒申董鐃緒申峇鐃緒申鐃緒申文鐃緒申鐃緒申忙鐃緒申儔鐃叔緒申鐃緒申峇鐃緒申鐃緒申鐃緒申鐃淑醐申鐃緒申鐃熟￥申$0)

			// "slideshow [" -> "[imagefile("
			query = query.replaceAll("slideshow\\s*\\[", "\\[imagefile(");
			// "[imagefile(*,path="*"*]" ->
			// "[imagefile(*,path="*"*, type="slideshow")"
			query = query.replaceAll("(\\[imagefile\\(.*,\\spath=\".*\".*)\\]",
					"$1, type=\"slideshow\")");

			if (query.matches(".*\\[imagefile\\(.*\\)\\s*\\@\\s*\\{.*\\}.*")) {
				// @鐃緒申鐃緒申
				// "[imagefile(*) @ {*}" -> "[imagefile(*) @ {*} ]! "
				query = query.replaceAll(
								"\\[imagefile\\(.*\\)\\s*\\@\\s*\\{[a-zA-Z0-9=\\s,]*\\}",
								"$0]! ");
			} else {
				// @無鐃緒申
				// "[imagefile(*) " -> "[imagefile(*)]! "
				query = query.replaceAll("\\[imagefile\\(.*\\)", // "(\\[imagefile\\(.*\\)[\\s*|\\s*^\\@])",
						"$0]! ");
			}
		}
		//TODO
        //added by goto 20130422  For "!number, / ,number! / ,number!nuber, "
        //鐃緒申 鐃緒申鐃緒申}(鐃縦わ申鐃暑カ鐃獣ワ申)鐃殉でわ申0文鐃緒申幣鐃緒申任鐃春わ申文鐃緒申鐃緒申: [^\\}]*
        //For ,number!number,
    	query = query.replaceAll("\\]\\s*\\,\\s*([0-9]+)\\s*!\\s*([0-9]+)\\s*%\\s*@\\s*\\{([^\\}]*)", "],@{$3,column=$1,row=$2");
    	query = query.replaceAll("\\]\\s*\\,\\s*([0-9]+)\\s*!\\s*([0-9]+)\\s*%", "],@{column=$1,row=$2}");
    	//For ,number!
    	query = query.replaceAll("\\]\\s*\\,\\s*([0-9]+)\\s*!\\s*@\\s*\\{([^\\}]*)", "],@{$2,column=$1");
    	query = query.replaceAll("\\]\\s*\\,\\s*([0-9]+)\\s*!", "],@{column=$1}");
    	query = query.replaceAll("\\]\\s*\\,\\s*([0-9]+)\\s*\\}\\s*@\\s*\\{([^\\}]*)", "],}@{$2,column=$1");	//TODO
//    	query = query.replaceAll("\\]\\s*\\,\\s*([0-9]+)\\s*!", "],@{column=$1}");							//TODO
        //For !number,
        query = query.replaceAll("\\]\\s*!\\s*([0-9]+)\\s*%\\s*@\\s*\\{([^\\}]*)", "]!@{$2,row=$1");
    	query = query.replaceAll("\\]\\s*!\\s*([0-9]+)\\s*%", "]!@{row=$1}");


//    	Log.i("	query = "+query);
		return query;
	}//End of replaceQuery_For_HTML_and_MobileHTML5

	// to get SSQL file from Internet
	private String getSSQLQuery2() {

		String query = GlobalEnv.getQuery();
		if (query != null) {
			query = query.trim();
		}

		String filename = GlobalEnv.getfilename();
		if (filename != null) {
			Log.info("[Parser:Parser] filename = " + filename);
			StringBuffer tmp = new StringBuffer();
			String line = new String();
			BufferedReader dis;
			try {

				if (filename.startsWith("http:")) {
					URL fileurl = new URL(filename);

					URLConnection fileurlConnection = fileurl.openConnection();
					/*
					 * DataInputStream dis = new
					 * DataInputStream(fileurlConnection.getInputStream());
					 */
					dis = new BufferedReader(new InputStreamReader(
							fileurlConnection.getInputStream(), "EUC-JP"));
				}

				else {
					dis = new BufferedReader(new FileReader(filename));
					line = null;
				}
				while (true) {
					line = dis.readLine();

					if (line == null || line.equals("-1"))
						break;

					if (line != null && line.contains("/*")) {
						int s = line.indexOf("/*");
						String line1 = line.substring(0, s);
						// tmp.append(" "+line1);
						while (!line.contains("*/"))
							line = dis.readLine();
						int t = line.indexOf("*/");
						line = line1 + line.substring(t + 2);
					}
					// added by goto 20130412
					if (line != null && line.contains(commentOutLetters)) {	//commentOutLetters = "--"
						boolean dqFlg = false;
						int i = 0;

						for (i = 0; i < line.length(); i++) {
							if (line.charAt(i) == '"' && !dqFlg)
								dqFlg = true;
							else if (line.charAt(i) == '"' && dqFlg)
								dqFlg = false;

							if (!dqFlg
									&& i < line.length() - 1
									&& (line.charAt(i) == GlobalEnv.COMMENT_OUT_LETTER && line
									.charAt(i + 1) == GlobalEnv.COMMENT_OUT_LETTER))
								break;
						}
						line = line.substring(0, i);
					}

					if (line != null)
						tmp.append(" " + line);
				}
				dis.close();

			} catch (MalformedURLException me) {
				System.out.println("MalformedURLException: " + me);
			} catch (IOException ioe) {
				System.out.println("IOException: " + ioe);
				GlobalEnv.addErr("Error[SQLparser]:" + ioe);
			}

			query = tmp.toString().trim();
		}

		if (query.endsWith(";")) {
			query = query.substring(0, query.length() - 1).trim();
		}

		Log.info("[Parser:Parser] ssql statement = " + query);
		return query;
	}

	//modified by goto 20130915   For 'from' in "" error
	private void processFROM(StringBuffer tfe, StringTokenizer st) {
		boolean dqFlg = false;
		int dqCount;
		while (st.hasMoreTokens()) {
			String nt = st.nextToken().toString();
			dqCount = nt.length() - nt.replaceAll("\\\\\"", "xx").replaceAll("\"", "").length();	//count
			if(!dqFlg && dqCount%2!=0)		dqFlg = true;
			else if(dqFlg && dqCount%2!=0)	dqFlg = false;
			
			if (!dqFlg && nt.equalsIgnoreCase("FROM"))
				break;

			//			if (nt.toUpperCase().contains("XMLDATA(")) {
			//				if (nt.contains(")")) {
			//					String temp_nt = new String();
			//
			//					temp_nt = (nt.toUpperCase().replace("XMLDATA(", ""))
			//							.toLowerCase();
			//
			//					if (temp_nt.contains("))")) {
			//						nt = temp_nt.replace("))", ")");
			//					} else {
			//						nt = temp_nt.replace(")", "");
			//					}
			//
			//					Log.out("nt : " + nt);
			//					tfe.append(nt + " ");
			//					xpathExist = 1;
			//				}
			//			}
			//
			//			else if ((nt.toUpperCase().contains("SSQL::XPATH("))
			//					|| (nt.toUpperCase().contains("XPATH("))) {
			//				if (nt.toUpperCase().contains("SSQL::XPATH(")) {
			//					if (nt.toUpperCase().contains("XPATH(")) {
			//						XpathQuery = nt.toUpperCase().replace(
			//								"SSQL::XPATH(", "");
			//					} else if (nt.toUpperCase().contains("SSQL::XPATH(")) {
			//						XpathQuery = nt.toUpperCase().replace(
			//								"SSQL::XPATH(", "");
			//					}
			//				}
			//
			//				else if (nt.toUpperCase().contains("XPATH(")) {
			//					if (nt.toUpperCase().contains("XPATH(")) {
			//						XpathQuery = nt.toUpperCase().replace("XPATH(", "");
			//					} else if (nt.contains("XPATH(")) {
			//						XpathQuery = nt.toString().replace("XPATH(", "");
			//					}
			//				}
			//
			//				String tmp_xpath2 = new String();
			//
			//				Log.out("xpath after nt (before) : " + nt);
			//
			//				if (nt.contains(")")) {
			//
			//					if (nt.toString().contains("@{")) {
			//						tmpXpath1 = nt.substring(
			//								nt.toUpperCase().indexOf("XPATH(") + 6,
			//								nt.indexOf("@"));
			//						if (nt.toString().contains("@{tag=")) {
			//							xpathTag = nt.substring(
			//									nt.indexOf("@{tag=") + 6,
			//									nt.indexOf("}"));
			//						} else { // @{tag}
			//							xpathTag = tmpXpath1.substring(
			//									tmpXpath1.indexOf(".") + 1,
			//									tmpXpath1.length());
			//						}
			//						xpathTagExist = 1;
			//					} else {
			//						tmpXpath1 = nt.substring(
			//								nt.toUpperCase().indexOf("XPATH(") + 6,
			//								nt.indexOf(","));
			//					}
			//
			//					tmp_xpath2 = nt.substring(nt.indexOf("path=") + 6,
			//							nt.indexOf("\")"));
			//
			//					if (tmp_xpath2.contains("text()")
			//							|| tmp_xpath2.contains("node()")) { // XPath
			//						xmlTextFlag = 1;
			//					}
			//
			//					if (nt.contains("),")) {
			//						nt = "xpath(\"" + tmp_xpath2 + "\"," + tmpXpath1
			//								+ "),";
			//					}
			//
			//					else {
			//						nt = "xpath(\"" + tmp_xpath2 + "\"," + tmpXpath1
			//								+ ")";
			//					}
			//					Xpath = nt;
			//					Log.out("xpath after nt (after) : " + nt);
			//				}
			//
			//				tfe.append(nt + " ");
			//				Log.out("XPATH tfe : " + tfe);
			//				xpathExist = 1;
			//			}
			//
			//			else if ((nt.toUpperCase().contains("SSQL::XMLQUERY("))
			//					|| (nt.toUpperCase().contains("XMLQUERY("))) {
			//				if (nt.toUpperCase().contains("SSQL::XMLQUERY(")) {
			//					if (nt.toUpperCase().contains("XMLQUERY(")) {
			//						XpathQuery = nt.toUpperCase().replace(
			//								"SSQL::XMLQUERY(", "");
			//					} else if (nt.toUpperCase().contains("SSQL::XMLQUERY(")) {
			//						XpathQuery = nt.toUpperCase().replace(
			//								"SSQL::XMLQUERY(", "");
			//					}
			//				}
			//
			//				else if (nt.toUpperCase().contains("XMLQUERY(")) {
			//					if (nt.toUpperCase().contains("XMLQUERY(")) {
			//						XpathQuery = nt.toUpperCase().replace("XMLQUERY(",
			//								"");
			//					} else if (nt.contains("XMLQUERY(")) {
			//						XpathQuery = nt.toString().replace("XMLQUERY(", "");
			//					}
			//				}
			//
			//				Log.out("xmlquery after nt (before) : " + nt);
			//
			//				if (nt.contains(")")) {
			//
			//					if (nt.toString().contains("@{")) {
			//						tmpXmlQuery1 = nt.substring(nt.toUpperCase()
			//								.indexOf("XMLQUERY(") + 9, nt.indexOf("@"));
			//						if (nt.toString().contains("@{tag=")) {
			//							xpathTag = nt.substring(
			//									nt.indexOf("@{tag=") + 6,
			//									nt.indexOf("}"));
			//						} else { // @{tag}
			//							xpathTag = tmpXmlQuery1.substring(
			//									tmpXmlQuery1.indexOf(".") + 1,
			//									tmpXmlQuery1.length());
			//						}
			//
			//						xpathTagExist = 1;
			//					}
			//
			//					else {
			//						tmpXmlQuery1 = nt.substring(nt.toUpperCase()
			//								.indexOf("XMLQUERY(") + 9, nt.indexOf(","));
			//					}
			//
			//					tmpXmlQuery2 = nt.substring(nt.indexOf("path=") + 6,
			//							nt.indexOf("\")"));
			//
			//					if (tmpXmlQuery2.contains("text()")) { // XMLQuery
			//						xmlTextFlag = 1;
			//					}
			//
			//					else if (tmpXmlQuery2.contains("node()")) { // XMLQuery
			//						xmlTextFlag = 1;
			//					}
			//
			//					if (nt.contains("),")) {
			//						nt = "xmlquery(\"$a" + tmpXmlQuery2 + "\"" + ","
			//								+ tmpXmlQuery1 + "),";
			//					}
			//
			//					else if (nt.contains(")")) {
			//						nt = "xmlquery(\"$a" + tmpXmlQuery2 + "\"" + ","
			//								+ tmpXmlQuery1 + ")";
			//					}
			//					DB2_XQUERY = nt;
			//					Log.out("xmlquery after nt (after) : " + nt);
			//				}
			//
			//				tfe.append(nt + " ");
			//				Log.out("XMLQUERY tfe : " + tfe);
			//				xpathExist = 1;
			//			}
			//
			//			else if (nt.contains("sinvoke(")) {
			//				String tmp = new String();
			//				String orig = new String();
			//				String orig2 = new String();
			//				Log.out("sinvoke nt:" + nt);
			//				if (nt.contains(")")) {
			//					orig = nt.substring(0, nt.indexOf(")"));
			//					Log.out("hogehoge" + orig);
			//					orig2 = nt.substring(nt.indexOf(")"), nt.length());
			//					Log.out("hoge" + orig2);
			//					tmp = nt.substring(nt.indexOf("att=") + 4,
			//							nt.indexOf(")"));
			//
			//					Log.out("sinvoke parser tmp:" + tmp);
			//					Log.out("sinvoke parser orig:" + orig);
			//					Log.out("sinvoke parser orig2:" + orig2);
			//
			//					String cond = new String();
			//					cond = "ajaxcond=\"" + tmp + "\"";
			//					if (GlobalEnv.isAjax())
			//						nt = orig + " ," + cond + " " + orig2;
			//					else
			//						nt = orig + orig2;
			//
			//					Log.out("sinvoke after nt:" + nt);
			//				}
			//				tfe.append(nt + " ");
			//				Log.out("sinvoke tfe:" + tfe);
			//			} else if (nt.contains("embed(")) {
			//				StringBuffer tmp = new StringBuffer();
			//				String deco = new String();
			//
			//				if (nt.contains(")")) {
			//					deco = nt.substring(nt.indexOf(")") + 1, nt.length());
			//					nt = nt.substring(0, nt.indexOf(")") + 1);
			//				}
			//
			//				tmp.append(nt + " ");
			//
			//				Log.out("tmp:" + tmp);
			//				while (!nt.contains(")")) {
			//					nt = st.nextToken().toString();
			//
			//					Log.out("embed parser : " + nt);
			//
			//					if (nt.contains("@")) {
			//						deco = nt.substring(nt.indexOf("@"), nt.length());
			//						nt = nt.substring(0, nt.indexOf("@"));
			//						Log.out("deco:" + deco);
			//					}
			//					tmp.append(nt + " ");
			//				}
			//
			//				tfe.append("{ " + embed(tmp.toString()));
			//				tfe.append("}" + deco);
			//				Log.out("append embed tfe : " + tfe);
			//
			//			} else {
			tfe.append(nt + " ");

		}
	}

	/**
	 * Fills the buffers from_c, where_c, order_c, group_c and having_c.
	 * Whenever we encounter a keyword the local variable buffer changes
	 * and points to the next buffer. Keywords are not appended in the 
	 * buffers.
	 * @param st
	 */
	private void processKeywords(StringTokenizer st){
		StringBuffer buffer = new StringBuffer();
		buffer = from_c;
		while (st.hasMoreTokens()) {
			String nt = st.nextToken().toString();
			if (nt.equalsIgnoreCase("WHERE")) {
				buffer = where_c;
			}
			else if (nt.equalsIgnoreCase("ORDER")) {
				buffer = order_c;
			}
			else if (nt.equalsIgnoreCase("GROUP")) {
				buffer = group_c;
			}
			else if (nt.equalsIgnoreCase("HAVING")) {
				buffer = having_c;
			}
			else {
				buffer.append(nt + " ");
			}
			Mobile_HTML5Function.after_from_string += nt+" ";	//added by goto 20130515  "search"
		}
	}

	private void postProcess() {
		// FOREACH
		if (!(foreachFrom.equals(""))) {
			from_c.append("," + foreachFrom);
		}

		groupStatement = group_c.toString();
		Log.out("[Paeser:Group] group = " + groupStatement);
		group_c.append(embedGroup + " ");
		
		havingStatement = having_c.toString();
		Log.out("[Paeser:Having] having = " + havingStatement);
		having_c.append(embedGroup + " ");
		
		fromInfo = new FromInfo(from_c.toString().trim());
		Log.out("[Parser:From] from = " + fromInfo);
		if (!(foreachFrom.equals(""))) {
			Log.out(foreachFrom
					+ ": Used in FOREACH clause and added to FROM clause ");
		}

		if (SSQLparser.isDbpediaQuery())
			whereInfo.setSparqlWhereQuery(where_c.toString().trim());
		else
			whereInfo.appendWhere(where_c.toString().trim());

		if (embedWhere.length() != 0)
			whereInfo.appendWhere(embedWhere + " ");

		Log.out("WHERE:" + whereInfo);
		// FOREACH
		if (!(foreachWhere.equals(""))) {
			whereInfo.appendWhere(foreachWhere);
			Log.out(foreachWhere
					+ ": Used in FOREACH clause and added to WHERE clause ");
		}

		String addCondition = GlobalEnv.getCondition();
		if (addCondition != null) {
			whereInfo.appendWhere(addCondition);
		}
		Log.out("[Paeser:Where] where = " + whereInfo);
	}
	
	private void parseSSQL(String QueryString, int id) {
		// replace '*' to attributes added by chie
		if (QueryString.contains("*")) {
			QueryString = replaceQuery(QueryString);
		}

		QueryImage = QueryString;
		StringTokenizer st = new StringTokenizer(QueryString);

		try {
			if (!st.hasMoreTokens()) {
				System.err.println("*** No Query Specified ***");
				throw (new IllegalStateException());
			}

			String nt = st.nextToken().toString();
			Log.out("[Parser:Parser] start parsing");

			preProcess(st, nt);

			media = st.nextToken().toString();
			if(media.equalsIgnoreCase("DISTINCT")){
				distinct = true;
				media = st.nextToken().toString();
			}

			// for embed css TFE_ID
			codeGenerator = new CodeGenerator(id);
			codeGenerator.setFactory(media.toUpperCase());
			codeGenerator.initiate();

			StringBuffer tfe = new StringBuffer();

			// FOREACH
			if (foreachFlag) {
				tfe.append("[foreach(" + foreachInfo.getForeachAtt() + ")?");
			}

			processFROM(tfe, st);

			// FOREACH
			if (foreachFlag) {
				tfe.append("]%");
			}
			
			// changed by goto 20130122 For "slideshow"
			if (!tfe.toString().contains("type=\"slideshow\""))
				System.out.println("[Parser:tfe] tfe = " + tfe);

			Preprocessor preprocessor = new Preprocessor(tfe.toString());
			tfe = preprocessor.pushAggregate();
			tfe = preprocessor.pushOrderBy();
			Log.out("[Parser:tfe] converted_tfe = " + tfe);

			tfeInfo = new TFEparser(tfe.toString(), codeGenerator);
			tfeInfo.debugout();

			processKeywords(st);
			postProcess();
			
		} catch (IllegalStateException e) {
			System.err
			.println("Error[SSQLparser]: Syntax Error in SSQL statement : "
					+ QueryImage);
			GlobalEnv
			.addErr("Error[SSQLparser]: Syntax Error in SSQL statement : "
					+ QueryImage);
			return;
		}
	}

	private void preProcess(StringTokenizer st, String nt) {
		
		// FOREACH
		if (nt.equalsIgnoreCase("FOREACH")) {
			foreachFlag = true;
			StringBuffer foreach_c = new StringBuffer();
			while (st.hasMoreTokens()) {
				nt = st.nextToken().toString();
				if (nt.equalsIgnoreCase("GENERATE"))
					break;
				foreach_c.append(nt + " ");
			}
			Log.out("*** This query contains FOREACH clause ***");
			Log.out(" foreach_c :" + foreach_c);

			foreachInfo = new ForeachInfo(foreach_c.toString().trim());
			foreachFrom = foreachInfo.getForeachFrom();
			foreachWhere = foreachInfo.getForeachWhere();

			Log.out("[Parser:Foreach] foreach = " + foreachInfo);
		}
		GlobalEnv.foreach_flag = foreachFlag;

		// REQUEST SESSION
		if (nt.equalsIgnoreCase("REQUEST")) {
			while (st.hasMoreTokens()) {
				nt = st.nextToken().toString();
				if (nt.equalsIgnoreCase("GENERATE"))
					break;
			}
		}
		
		//SESSION  //added by goto 20130508  "Login&Logout"
        if (nt.toUpperCase().matches("SESSION.*") || nt.toUpperCase().matches("LOGIN.*")) {
            while (st.hasMoreTokens()) {
            	sessionString += nt+" ";
                nt = st.nextToken().toString();
                if (nt.equalsIgnoreCase("GENERATE"))
                    break;
            }
            sessionFlag = true;
        }

		// GENERATE medium
		if (!nt.equalsIgnoreCase("GENERATE")) {
			System.err.println("*** The Query should start by GENERATE ***");
			throw (new IllegalStateException());
		}
        
		if (!st.hasMoreTokens()) {
			System.err.println("*** No medium/tfe Specified ***");
			throw (new IllegalStateException());
		}
	}

	// added by chie replace '*'
	private String replaceQuery(String query) {
		Log.out("START QUERY REPLACE");
		StringTokenizer fst = new StringTokenizer(query);

		// tfe from where
		String tfe = new String();
		String from_string = new String();
		String where_string = new String();
		String queryResult = new String();

		// separate tfe from where
		while (fst.hasMoreTokens()) {
			String fnt = fst.nextToken();
			queryResult += fnt + " ";
			if (fnt.equalsIgnoreCase("GENERATE")) {
				fnt = fst.nextToken();// media
				queryResult += fnt + " ";
				break;
			}
		}
		String distinctString;
		if(fst.hasMoreTokens()){
			distinctString = fst.nextToken();
			if(distinctString.equalsIgnoreCase("DISTINCT"))
				distinct = true;
			else
				tfe+= distinctString;
		}
		//modified by goto 20130915   For 'from' in "" error
		boolean dqFlg = false;
        int dqCount;
		while (fst.hasMoreTokens()) {
			String fnt = fst.nextToken();
            dqCount = fnt.length() - fnt.replaceAll("\\\\\"", "xx").replaceAll("\"", "").length();    //count
            if(!dqFlg && dqCount%2!=0)        dqFlg = true;
            else if(dqFlg && dqCount%2!=0)    dqFlg = false;
            
			if (!dqFlg && fnt.equalsIgnoreCase("FROM")) {
				break;
			}
			tfe += fnt+" ";		//modified 20130914
		}
		while (fst.hasMoreTokens()) {
			String fnt = fst.nextToken();
			if (fnt.equalsIgnoreCase("WHERE")) {
				where_string += fnt;
				break;
			} else
				from_string += " " + fnt;
		}
		while (fst.hasMoreTokens()) {
			String fnt = fst.nextToken();
			where_string += " " + fnt;
		}

		// remove from_string decoration
		StringTokenizer st = new StringTokenizer(from_string, ",");
		String subfrom_string = new String();
		while (st.hasMoreTokens()) {
			String ch = st.nextToken().trim();
			if(ch.contains("@")){
				ch = ch.substring(0,ch.indexOf("@"));
			}
			subfrom_string += ch + ",";
		}
		subfrom_string = subfrom_string.substring(0,subfrom_string.length()-1);

		//setting of DB
    	String hostname = GlobalEnv.gethost();
        String dbname = GlobalEnv.getdbname();
        String user = GlobalEnv.getusername();
        String driver = GlobalEnv.getDriver();
        String dbms = GlobalEnv.getdbms();
        String url = GlobalEnv.geturl();
        String password = GlobalEnv.getpassword();

        SQLManager SqlM = new SQLManager(url,user,driver, password);

        TFEtokenizer tfetoken = new TFEtokenizer(tfe);
        //TFEtokenizer tt = new TFEtokenizer(tfe);
    	String connector = new String();
    	int repeaterFlg = 0;
    	boolean replaceFlg = false;

    	while(tfetoken.hasMoreTokens()){
    		replaceFlg = false;
        	String fnt = tfetoken.nextToken();
        	//Log.out("tfetoken : " + fnt);
        	if(fnt.contains("*")&& !fnt.contains("\"")){
            	int i = 0;
        		while(tfetoken.hasMoreTokens()){
        			i++;
            		String fnt2 = tfetoken.nextToken();
                	//Log.out("tfetoken* : " + fnt2 + i);
            		if(connector.isEmpty() && fnt2.equals("!") && repeaterFlg == 1){
                		connector = ",";
                	}else if(connector.isEmpty() && fnt2.equals(",") && repeaterFlg == 1){
                		connector = "!";
                	}

            		if(!connector.isEmpty()){
            			//Log.out(subfrom_string);
            			String columnList = SqlM.getAttList(subfrom_string,connector,fnt);
                		fnt = "{" +  columnList +"}";
                		repeaterFlg--;
                		replaceFlg = true;
                		connector = "";
                		break;
            		}
            		if(fnt2.equals("[")){
                		repeaterFlg--;
                	}
            		if(fnt2.equals("]")){
                		repeaterFlg++;
                	}

        		}
        		//back
        		for(;i>0;i--){
        			String tmp = tfetoken.prevToken();
        		}
        		if(replaceFlg == false){
        			connector = ",";
        			String columnList = SqlM.getAttList(subfrom_string,connector,fnt);
            		fnt = "{" +  columnList +"}";
            		connector = "";
        		}
        	}
        	queryResult += fnt;
        }
    	queryResult += " FROM " + from_string + " " + where_string;
    	Log.out("query : " + queryResult);
    	return queryResult;
    }

    public TFEparser gettfe_info() {
        return tfeInfo;
    }

    public ITFE get_TFEschema() {
        return tfeInfo.get_TFEschema();
    }

    public CodeGenerator getcodegenerator() {
    	codeGenerator.TFEid = 10000;
        return codeGenerator;
    }
    
    public CodeGenerator getcodegenerator(int id) {
        codeGenerator.TFEid = id;
    	return codeGenerator;
    }
    
    public FromInfo get_from_info() {
        return fromInfo;
    }

    public WhereInfo get_where_info() {
        return whereInfo;
    }

    public Hashtable get_att_info() {
        return this.tfeInfo.get_attp();
    }

    public String getSSQLsig() {
        StringBuffer sig = new StringBuffer();
        //		sig.append(QueryImage.replaceAll("\\s",""));
        sig.append(QueryImage);
        String addCondition = GlobalEnv.getCondition();
        if (addCondition != null) {
            sig.append("@@");
            sig.append(addCondition);
        }
        //Log.out("[SSQL sig] = " +sig);
        return sig.toString();
    }

    public Object[] getSQLsig(ExtList sep_sch) {

        Hashtable atts = this.get_att_info();
        FromInfo from = this.get_from_info();
        WhereInfo where = this.get_where_info();

        int idx;
        Integer itemno;
        ExtList schf = sep_sch.unnest();
        StringBuffer buf = new StringBuffer();

        Hashtable sig2idx = new Hashtable();
        TreeSet sorter = new TreeSet();
        String attsig;
        ExtList ordersig = new ExtList();

        for (idx = 0; idx < schf.size(); idx++) {
            itemno = (Integer) (schf.get(idx));
            attsig = ((AttributeItem) (atts.get(itemno))).getAttributeSig(from);
            sig2idx.put(attsig, new Integer(idx));
            sorter.add(attsig);
        }

        Iterator ite = sorter.iterator();
        while (ite.hasNext()) {
            attsig = (String) ite.next();
            buf.append(attsig + "@@");
            ordersig.add(sig2idx.get(attsig).toString());
        }

        Log.out("sig:" + buf);
        Log.out("ordersig:" + ordersig);

        buf.append(where.getWhereSig(from));
        Object[] ret = { buf.toString(), ordersig };

        return ret;
    }

    public String getTFEsig(ExtList sep_sch) {

        Hashtable atts = this.get_att_info();
        FromInfo from = this.get_from_info();
        WhereInfo where = this.get_where_info();

        int i, idx;
        Object o;
        Integer itemno;
        StringBuffer buf = new StringBuffer();
        for (idx = 0; idx < sep_sch.size(); idx++) {
            o = sep_sch.get(idx);
            if (o instanceof Integer) {

                itemno = (Integer) (sep_sch.get(idx));
                AttributeItem att1 = (AttributeItem) (atts.get(itemno));
                buf.append(att1.getAttributeSig(from) + "@@");
            } else if (o instanceof ExtList) {
                buf.append("(");
                buf.append(getTFEsig((ExtList) o));
                buf.append(")");
            }
        }

        // Where
        buf.append(where.getWhereSig(from));

        return buf.toString();

    }

    public StringBuffer embed(String tmp)
    {

    	StringBuffer query = new StringBuffer();
       	StringTokenizer st = new StringTokenizer(tmp,",");

    	String[] ArgName = new String[100];
    	String[] ArgValue = new String[100];
    	int num = 0;

    	if(GlobalEnv.getEmbedOption() == 0)
    	{
    		query.append(tmp);
    		return query;
    	}
    	Log.out("**************new embed************************");
    	while(st.hasMoreTokens())
    	{
    		String part = new String();
    		part = st.nextToken();

    		if(part.contains("="))
    		{
	    		Log.out("part : " + part);
	    		String ArgNameTmp = part.substring(0,part.indexOf("="));

	    		String ArgValueTmp = part.substring(part.indexOf("=")+1, part.length());

	    		if(ArgNameTmp.contains("("))
	    		{
	    			ArgNameTmp = ArgNameTmp.substring(ArgNameTmp.indexOf("(")+1, ArgNameTmp.length());
	    		}

	    		if(ArgValueTmp.contains(")"))
	    			ArgValue[num] = ArgValueTmp.replace(")"," ");
	    		else
	    			ArgValue[num] = ArgValueTmp;

	    		ArgName[num] = ArgNameTmp.trim();
	    		ArgValue[num] = ArgValue[num].replace("\""," ");
	    		ArgValue[num] = ArgValue[num].trim();

	    		Log.out("num:"+num);
	    		Log.out("ArgName : " + ArgName[num]);
	    		Log.out("ArgValue : " + ArgValue[num]);

	    		num++;
    		}
    	}

    	int position = 0;

    	String filename = new String();

    	for(int a = 0; a < num ; ++a)
    	{
    		if(ArgName[a].equals("file"))
    			filename = ArgValue[position];
        }

    	if(!filename.contains(".sql") || !filename.contains(".ssql"))
    	{
    		query.append(tmp);
    		return query;
    	}

    	String line= new String();

    	BufferedReader in;

        StringBuffer tmp2 = new StringBuffer();
        try {

            in = new BufferedReader(new FileReader(filename));
            while (true) {
                line = in.readLine();
                if (line == null)
                    break;

                if(line!=null && line.contains("/*"))
                {
                	int s = line.indexOf("/*");
                	String line1 = line.substring(0,s);
                	while(!line.contains("*/"))
                		line = in.readLine();
                	int t = line.indexOf("*/");
                	line = line1+line.substring(t+2);
                }
                if(line!=null && line.contains(commentOutLetters)){	//commentOutLetters = "--"
                	boolean dqFlg=false;
                	int i=0;
                	
                	for(i=0; i<line.length(); i++){
                		if(line.charAt(i)=='"' && !dqFlg)		dqFlg=true;
                		else if(line.charAt(i)=='"' && dqFlg)	dqFlg=false;
                		
                		if(!dqFlg && i<line.length()-1 && (line.charAt(i)==GlobalEnv.COMMENT_OUT_LETTER && line.charAt(i+1)==GlobalEnv.COMMENT_OUT_LETTER))
                			break;
                	}
                	line = line.substring(0,i);
                }
                
                if(line!=null)
                	tmp2.append(" " + line);
            }
            in.close();
        } catch (IOException e2) {
        }

        String QueryTmp = tmp2.toString();
        StringBuffer QueryBuffer = new StringBuffer();
        Log.out("embeded Query : " + QueryTmp);

        StringTokenizer st3 = new StringTokenizer(QueryTmp,"\t{}[]!,()@= \"' ;",true);

        String tmp3 = new String();
        String tmp4 = new String();

        String[] embed_table = new String[10000000];

        for(int i = 0 ; i < 1000000 ; i++)
        {
        	embed_table[i] = null;
        }
        int is_replaced = 0;
        String tmp5;

        while(st3.hasMoreTokens())
        {
        	tmp3 = st3.nextToken().toString();

        	tmp3.trim();

        	if(tmp3.equalsIgnoreCase("HTML"))
        		break;
        }

        //modified by goto 20130915   For 'from' in "" error
        boolean dqFlg = false;
        int dqCount;
        while(st3.hasMoreTokens())
        {
        	tmp3 = st3.nextToken().toString().trim();
            dqCount = tmp3.length() - tmp3.replaceAll("\\\\\"", "xx").replaceAll("\"", "").length();    //count
            if(!dqFlg && dqCount%2!=0)        dqFlg = true;
            else if(dqFlg && dqCount%2!=0)    dqFlg = false;

        	if(!dqFlg && tmp3.equalsIgnoreCase("FROM"))
        	{
        		break;
        	}

	        	if(tmp3.contains(".") && (!tmp3.contains("\"") && !tmp3.contains("html") && !tmp3.contains("http") && !tmp3.contains("css")))
	        	{
	        		tmp4 = tmp3.substring(0,tmp3.indexOf("."));
	        		tmp5 = tmp3.substring(tmp3.indexOf("."),tmp3.length());
	        		
	        		for(int b = 0; b < tableNum ; ++b)
	        		{
	        			if(tmp4.equals(embed_table[b]))
	        			{
	        				tmp4 = "embed" + b;
	        				is_replaced = 1;
	        				break;
	        			}
	        		}

	        		if(is_replaced == 0)
	        		{
	        			embed_table[tableNum] = tmp4;
	        			tmp4 = "embed" + tableNum;
	        			tableNum++;
	        		}

	        		QueryBuffer.append(tmp4);
	        		QueryBuffer.append(tmp5);
	        		is_replaced = 0;
	        	}
	        	else if(!tmp3.equals(" "))
	        		QueryBuffer.append(tmp3);
	    }

    	for(int b = 0; b < tableNum ; ++b) {
    		Log.out( b + " ; " + embed_table[b]);
    	}

        //FROM
    	is_replaced = 0;

    	StringBuffer Embed_From = new StringBuffer();

        while(st3.hasMoreTokens())
        {
        	tmp3 = st3.nextToken().toString();

        	is_replaced = 0;
        	tmp3.trim();
        	Log.out("tmp3:" + tmp3+ " " + tmp3.length());
        	if(tmp3.equalsIgnoreCase("WHERE"))
        		break;

        	for(int b = 0; b < tableNum ; ++b)
        	{
        		if(tmp3.equalsIgnoreCase(embed_table[b]))
        		{
        			Embed_From.append(" embed" + b);
        			is_replaced = 1;
        		}
        	}

        	if(is_replaced == 0 && !tmp3.equalsIgnoreCase(" ") && !tmp3.equalsIgnoreCase(";"))
        	{
        		Embed_From.append(" " + tmp3);
        	}
        }

        Log.out("Embed_From:"+Embed_From);
        if(embedFrom.length() != 0)
        	embedFrom.append(",");
        embedFrom.append(Embed_From);

        //WHERE
        is_replaced = 0;

        StringBuffer Embed_Where = new StringBuffer();

        while(st3.hasMoreTokens())
        {
        	tmp3 = st3.nextToken().toString();
        	tmp3 = tmp3.trim();

        	if(tmp3.equalsIgnoreCase("GROUP"))
        		break;

        	if(!tmp3.equals(" ") || tmp3.length() != 0)
        	{
        		if(tmp3.contains("."))
        		{
        			tmp4 = tmp3.substring(0,tmp3.indexOf("."));
        			tmp5 = tmp3.substring(tmp3.indexOf("."),tmp3.length());

        			for(int b = 0; b < tableNum ; ++b)
        			{
        				if(tmp4.equalsIgnoreCase(embed_table[b]))
        				{
        					tmp4 = "embed" + b;
        					is_replaced = 1;
        				}
        			}

        			Embed_Where.append(tmp4+tmp5);
        		}
        		else
        			Embed_Where.append(tmp3);
        	}
        }

        if(embedWhere.length() != 0 && Embed_Where.length() != 0)
        	embedWhere .append(" AND ");
        embedWhere.append(Embed_Where);
        position = -1;
        for(int a = 0 ; a < num ; ++a)
        	if(ArgName[a].equalsIgnoreCase("where"))
        		position = a;

        if(position != -1)
        {
        	String tmp10 = ArgValue[position];
        	String tmp11 = new String();
        	String tmp12 = new String();
        	is_replaced = 0;

        	if(tmp10.contains("."))
        	{
        		tmp11 = tmp10.substring(0,tmp10.indexOf("."));
        		tmp12 = tmp10.substring(tmp10.indexOf(".")+1 , tmp10.length());
        		for(int a = 0; a < tableNum ; ++a)
        		{
        			if(tmp11.equals(embed_table[a]))
        			{
        				tmp11 = "embed"+a;
        				is_replaced = 1;
        			}
        		}
        		if(is_replaced == 0)
        		{
        			tmp11 = "embed" + tableNum;
        			tableNum++;
        		}
        	}
        	if(position != 0)
        	{
        		if(embedWhere.length() != 0)
        			embedWhere.append(" AND ");

        		embedWhere.append(tmp11+ "." + tmp12);
        	}
        	position = -1;
        	for(int a = 0 ; a < num ; ++a)
       			if(ArgName[a].equalsIgnoreCase("att"))
       				position = a;

        	if(position != -1)
        		embedWhere.append(ArgValue[position] + " ");

           	position = -1;
        	for(int a = 0 ; a < num ; ++a)
       			if(ArgName[a].equalsIgnoreCase("attString"))
       				position = a;

        	if(position != -1)
        		embedWhere.append("" + ArgValue[position] + " ");

        	Log.out("Embed WHERE : " + embedWhere);
        	is_replaced = 0;
        }
        StringBuffer Embed_Group = new StringBuffer();

        while(st3.hasMoreTokens())
        {
        	tmp3 = st3.nextToken();

        	if(tmp3.equalsIgnoreCase("BY"))
        		tmp3 = st3.nextToken();

        	if(tmp3.equalsIgnoreCase("HAVING"))
        		break;

        	for(int b = 0; b < tableNum ; ++b)
        	{
        		if(tmp3.equalsIgnoreCase(embed_table[b]))
        		{
        			Embed_Group.append("embed" + b + " ");
        			is_replaced = 1;
        		}
        	}

        	if(is_replaced == 0)
        		Embed_Group.append(tmp3 + " ");
        }

        embedGroup.append(Embed_Group);
        is_replaced = 0;
        StringBuffer Embed_Having = new StringBuffer();

        while(st3.hasMoreTokens())
        {
        	tmp3 = st3.nextToken();

        	if(tmp3.equalsIgnoreCase("HAVING"))
        		break;

        	for(int b = 0; b < tableNum ; ++b)
        	{
        		if(tmp3.equalsIgnoreCase(embed_table[b]))
        		{
        			Embed_Having.append("embed" + b + " ");
        			is_replaced = 1;
        		}
        	}

        	if(is_replaced == 0)
        		Embed_Having.append(tmp3 + " ");
        }

        embedHaving.append(Embed_Having);

        Log.out("Query Buffer : " + QueryBuffer);
        Log.out("Embed FROM : " + Embed_From);

        query = QueryBuffer;
        return  query;
    }

	public static boolean isDistinct() {
		return distinct;
	}

	public static void setDistinct(boolean distinct) {
		SSQLparser.distinct = distinct;
	}

}
