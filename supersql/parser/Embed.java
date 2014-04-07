package supersql.parser;

import supersql.codegenerator.DecorateList;

//goto 20130915-2  "<$  $>"
public class Embed {
	
	final static String EMBED_LABEL = "_embedLabel_";
	public static boolean embed = false;
	
	public static String checkEmbed(String q) {
		//Log.e("q="+q);
		while(q.contains("<$")){
			int index1,index2;
			index1 = q.indexOf("<$");
			String buf1 = q.substring(index1+2);
			index2 = buf1.indexOf("$>")+4;
			String buf2 = buf1.substring(0, index2-4);
			
			if(!buf2.contains("$$")){
				//<$  $>
				//buf2 = buf2.replace("\"", "\\\"");	//TODO check
				String x = q.substring(0,index1);
				if(!x.trim().endsWith("!") && !x.trim().endsWith("[") && !x.trim().endsWith("{")){
					x += "!";
				}
				q = x + " {\""+buf2+"\"}@{"+EMBED_LABEL+", div}!" + q.substring(index1+index2);
			}else{
				//<$  $$  $$  $>
				// <h1> $$'aaa h1'$$ </h1> 
				//' <a href= '||''bb''||' > '||''aa''||' </a> '
				buf2 = buf2.replace("\"", "'").replace("'", "''");	//TODO check
				//Log.e(buf2);
				String c = buf2, c2 = "", x = "";
				boolean d = false;
				for(int k=0; k<c.length(); k++){
					if(k>0){
						if(c.charAt(k-1)=='$' && c.charAt(k)=='$'){
							if(!d){
								x = c.substring(k+1);
								if(x.trim().startsWith("''")){
									//not attribute process
									x = x.substring(x.indexOf("'")+1);
								}
								c2 = c.substring(0,k-1)+"'||"+x;
								c = c2;
								d = true;
							}else{
								x = c.substring(0,k-1);
								if(x.trim().endsWith("''")){
									//not attribute process
									x = x.substring(0, x.lastIndexOf("'"));
								}
								c2 = x+"||'"+c.substring(k+1);
								c = c2;
								d = false;
							}
						}
					}
				}
				buf2 = c;
				//q = q.substring(0,index1) + " {'"+buf2+"'}@{"+EMBED_LABEL+", div}!" + q.substring(index1+index2);
				q = q.substring(0,index1) + " {'"+buf2+"'}!" + q.substring(index1+index2);
			}
			//Log.e(buf2);
		}
		//Log.e("q="+q);
		return q;
	}
	
	
	public static StringBuffer preProcess(StringBuffer code, DecorateList decos) {
		if(isEmbed(decos)){
			//Log.e(decos);
			code = code.delete(code.lastIndexOf("<"), code.lastIndexOf(">")+1);	//TODO case @table
			embed = true;
			return code;
		}
		return code;
	}
	
	public static StringBuffer postProcess(StringBuffer code) {
		if(embed){
			code = code.delete(code.lastIndexOf("<"), code.lastIndexOf(">")+1);	//TODO case @table
			embed = false;
			return code;
		}
		return code;
	}
	
	public static boolean isEmbed(DecorateList decos) {
		if(decos.containsKey(EMBED_LABEL)){
			return true;
		}
		return false;
	}

}
