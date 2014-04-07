package supersql.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.StringTokenizer;

import supersql.common.GlobalEnv;
import supersql.common.Log;

//#import  by goto 201312
public class Import {
	final static String DEFINE = "#define";
	final static String IMPORT = "#import";
	static ArrayList<String> importString = new ArrayList<String>();
	
	public static String checkImportString(BufferedReader in, String line) {
		try{
			if (line.startsWith(IMPORT)){
				while (line.startsWith(IMPORT)){
					importString.add(line);
					line = in.readLine();
				}
			}
		}catch (Exception e) {	}
		return line;
	}

	public static String importProcess(String query) {
		if(importString.isEmpty())	return query;
//		Log.e(importString);
		String file = getImportFileName(importString.get(0));	//TODO 複数import
		
		Log.info("[Parser:Import] import filename = " + file);
		
		ArrayList<String> defileName = new ArrayList<String>();
		ArrayList<String> defileContent = new ArrayList<String>();
		int textNum = 0;
		try{
			StringBuffer tmp = new StringBuffer();
			String line = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			while (true){
				line = in.readLine();
				if (line == null)	break;
				
				line = removeCommentOut(in, line);
				
				if (line.startsWith(DEFINE)){
					try{
						String name = "#"+line.substring(0,line.lastIndexOf("{")).substring(DEFINE.length()).trim();
						defileName.add(textNum, name);
					}catch (Exception e) { }
					
					String buf = "";
					while (!line.startsWith("}")){
						line = in.readLine();
						buf += line+" ";
					}
					buf = buf.substring(0,buf.lastIndexOf("}"));	//substring last '}'
					defileContent.add(textNum, buf);
					textNum++;
				} 
				
				tmp.append(" " + line);
			}
			in.close();
		} catch (FileNotFoundException e) {
			Log.err("Error[SSQLparser]: Import File(" + file	+ ") Is Not Found.");
			GlobalEnv.addErr("Error[SSQLparser]: Impoert File(" + file + ") Is Not Found." + e);
		} catch (IOException e) {
			GlobalEnv.addErr("Error[SSQLparser]:" + e);
		}

		query = replaceQuerySDefine(query, defileName, defileContent);
		
		//Log.e("return query = "+query);
		return query;
	}
	
	//replace query's #func() (ex. #func1() -> "Hello")
	private static String replaceQuerySDefine(String query,	ArrayList<String> defileName, ArrayList<String> defileContent) {
		try{
			for(int j=0; j<defileName.size(); j++){
				
				String dn = defileName.get(j);
				String dc = defileContent.get(j);
				
				String dn0 = dn.substring(0,dn.indexOf("(")).trim();
				String dnc = dn.substring(dn.indexOf("(")+1,dn.lastIndexOf(")")) + ",";
				//Log.e(dn0);
				
				ArrayList<String> rp = new ArrayList<String>();
				int k = 0;
				Map<String,LinkedHashSet<String>> dnMap = new LinkedHashMap<String,LinkedHashSet<String>>();
				LinkedHashSet<String> arguments = new LinkedHashSet<String>();
				while(dnc.contains(",")){
					rp.add(k, dnc.substring(0,dnc.indexOf(",")).trim());
					dnc = dnc.substring(dnc.indexOf(",")+1);
					//Log.e(dn0+" "+rp.get(k)+" "+dc);
					if(!rp.get(k).isEmpty()){
						arguments.add(rp.get(k));
					}
					k++;
				}
		        dnMap.put(dn0, arguments);
		        
	        	query = replaceQuerySDefineDetail(query, dn0, dnMap, dc);
			}
		}catch (Exception e) {
			//Log.e("[replaceQuerySDefine Exception]"+e);
		}
		return query;
	}

	private static String replaceQuerySDefineDetail(String query, String dn0, Map<String,LinkedHashSet<String>> dnMap, String dc) {
		//Log.i("!! "+dn0+"  "+dnMap+" "+dc);
		String dc0 = dc;
		
		//check by one word and replace
		String q = "";
		try{
			StringTokenizer st = new StringTokenizer(query);
			String s = "";
			while(st.hasMoreTokens()){
				s = st.nextToken().toString();
				//Log.i(s);
				
				String def = "";
				String s1 = "", s2 = "";
				if(s.contains(dn0)){
					//Log.e("!!! "+s+"	"+dn0);
					//Log.i("	!!!!!!!!!!!!!!!!!!!!!!!!! " + s);
					def = s;
					s2 = s;
					if(!s.contains(")")){
						while(st.hasMoreTokens()){
							String x = st.nextToken().toString();
							s2 += x;
							def += x+" ";
							if(x.contains(")")){
								break;
							}
						}
					}
//					Log.e("def0 = "+def);
					def = def.substring(def.indexOf(dn0));
					def = def.substring(0, def.indexOf(")")+1);
				}
				
				if(!def.isEmpty()){
					//Log.e("def = "+def);
					LinkedHashSet<String> arg = dnMap.get(dn0);
					//Log.e("arg = "+arg);
					if(arg.isEmpty()){
						//引数0
						if(def.contains(dn0+"()")){	//TODO正規表現: 0文字以上の空白
							s = s2.replace(dn0+"()", dc);
//							Log.e("arg.isEmpty()  "+dn0+"() | " + s2+" => "+s);
						}
					}else{
						//Log.e("!arg.isEmpty()  " +def+" "+dn0+"()");
						
						//TODO: 関数が複数くっついている場合にちゃんと動くかチェック	ex) #test1( "Test1!!" )!#test1( "Test1!!" )!
						
						//引数1~
						String dn2 = def.substring(0,def.indexOf("(")).trim();
						
						ArrayList<String> rp2 = new ArrayList<String>();
						if(def.contains(dn2)){
							//Log.e(def+" "+dn2);
							//Log.i("	!!!!!!!!!!!!!!!!!!!!!!!!! " + s2);
							String ss = def.substring(def.indexOf("(")+1,def.lastIndexOf(")")) + ",";
							//Log.i("	!!!!!!!!!!!!!!!!!!!!!!!!! " + ss);
							
							ArrayList<String> a = new ArrayList<String>();
							Iterator<String> it = arg.iterator();
			                while(it.hasNext()){
			                	a.add(it.next());
			                	//Log.i(a);
			                }
			                
			                dc = dc0;
							int l = 0;
							while(ss.contains(",")){
								if(l>=a.size()){
									//The number of arguments passed to the function is wrong. (渡された引数の個数が異なる)
									Log.err("[Parser:Import] Argument Error in "+dn0+"() : wrong number of arguments (more than "+l+" for "+a.size()+")");
									Log.err("\n### "+dn0+"() is ###");
									Log.err("#define "+dn0.substring(1)+a.toString().replace("[","(").replace("]",") {")+"\n"+dc0+"\n}\n");
									break;
								}
								rp2.add(l, ss.substring(0,ss.indexOf(",")).trim());
								ss = ss.substring(ss.indexOf(",")+1);
								//Log.e("!!!!2 rp2.get("+l+") = "+rp2.get(l));
								//Log.e("!!!!2 "+dn0+"  "+dnMap+" "+arg+" "+dc);
								String b = rp2.get(l);
								dc = dc.replace(a.get(l), b);
								l++;
							}
							s1 = s2.substring(0,s2.indexOf(dn2));
							s2 = s2.substring(s2.indexOf(")")+1);
							if(l==a.size()){
								s = s1+dc+s2;	//TODO
							}else{
								//The number of arguments passed to the function is wrong. (渡された引数の個数が異なる)
								Log.err("[Parser:Import] Argument Error in "+dn0+"() : wrong number of arguments ("+l+" for "+a.size()+")");
								Log.err("\n### "+dn0+"() is ###");
								Log.err("#define "+dn0.substring(1)+a.toString().replace("[","(").replace("]",") {")+"\n"+dc0+"\n}\n");
								s = s1+s2;
							}
						}
						
					}
				}
				q += s+" ";
			}
		}catch(Exception e){ 
			//Log.e("[replaceQuerySDefineDetail Exception]"+e);
		}
		return q;
	}
	

	//get Import file name
	private static String getImportFileName(String importString) {
		String file = importString.toLowerCase().substring(IMPORT.length()).trim();
		if(!new File(file).exists()){
			//Change to the absolute file name (絶対パスファイル名へ変更)
			if(file.startsWith("./")){
				file = file.substring("./".length()).trim();
			}
			String generateFile = GlobalEnv.getfilename();
			String fileDir = new File(generateFile).getAbsoluteFile().getParent();
			file = fileDir + GlobalEnv.OS_FS + file;
		}
		//Log.e("Import file name = "+file);
		return file;
	}

	//remove Comment-out String ( /* */ and -- )
	private static String removeCommentOut(BufferedReader in, String line) {
		String commentOutLetters = ""+GlobalEnv.COMMENT_OUT_LETTER+GlobalEnv.COMMENT_OUT_LETTER;	//="--"
		
		if (line.contains("/*")){
			String line1 = line.substring(0, line.indexOf("/*"));
			while (!line.contains("*/")){
				try {
					line = in.readLine();
				} catch (IOException e) { }
			}
			line = line1 + line.substring(line.indexOf("*/") + 2);
		}
		
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
//					line = line.substring(0,i-1)+"&quot;"+line.substring(i+1,line.length());
				
				}
				else if (!dqFlg && i < line.length()-1 && line.charAt(i)==GlobalEnv.COMMENT_OUT_LETTER && line.charAt(i+1)==GlobalEnv.COMMENT_OUT_LETTER)
					break;
			}
			line = line.substring(0, i);
		}
		
		return line;
	}

	
}
