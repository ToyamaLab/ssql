package supersql.invoke;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import supersql.codegenerator.CodeGenerator;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.dataconstructor.DataConstructor;
import supersql.form.FormEnv;
import supersql.form.FormServlet;
import supersql.parser.SSQLparser;

public class InvokeServlet2 extends HttpServlet {

	/**
	 * <code>serialVersionUID</code> �Υ�����
	 */
	private static final long serialVersionUID = 8021503235844232672L;
	private FormServlet fs;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		//os
		String enc = "EUC-JP";
		
		if(System.getProperty("os.name").indexOf("Windows")>=0){
			enc =  "Shift_JIS";
		}
		Log.info(enc);
		// ContentType��ݒ�
		res.setContentType("text/html; charset="+enc);
		req.setCharacterEncoding(enc);

		String querystring = req.getQueryString();
		// "stop" for destroy servlet
		if (querystring.equals("stop")) {
			res.setHeader("Pragma", "no-cache");
			OutputStream out = res.getOutputStream();

			PrintStream print = new PrintStream(out);
			print
			.println("<HTML><HEAD><TITLE>Stop Servlet</TITLE></HEAD><BODY>");
			print.println("Stop Servlet Invoke...");
			print.println("</BODY></HTML>");
			//System.setOut(new PrintStream(out));
			out.close();
			this.destroy();
			return;
		}

		InvokeEnv ienv = new InvokeEnv(req);

		if (ienv.getFilename().equals("")) {
			res.setHeader("Pragma", "no-cache");
			OutputStream out = res.getOutputStream();
			PrintStream print = new PrintStream(out);
			print
			.println("<HTML><HEAD><TITLE>InvokeServlet Error</TITLE></HEAD><BODY>");
			print.println("filename is missing!");
			print.println("</BODY></HTML>");
			//System.setOut(new PrintStream(out));
			out.close();
			return;
		}


		res.setHeader("Pragma", "no-cache");
		OutputStream out = res.getOutputStream();

		PrintStream print = new PrintStream(out);

		String error = new String();
		String config = new String();
		String query = new String();
		String cond = new String();
		String message = new String();

		Log.info("URL"+req.getRequestURL());
		Log.info("servpath"+req.getServletPath());
		Log.info("context"+req.getContextPath());

		String path = req.getRequestURL().toString();
		path = path.replace(req.getServletPath().toString(),"") + "/";

		String invokeservletpath = req.getRequestURL().toString();

		config = req.getParameter("config");
		if(config == null)
			error = "no config file defined";

		if (config.startsWith("http")){

		}else if(config.startsWith("./")){
			config = config.replace("./", path);
		}else{
			config = path + config;
		}

		query = req.getParameter("query");
		if(query == null)
			error = "no query file defined";

		if (query.startsWith("http")){

		}else if(query.startsWith("./")){
			query = query.replace("./", path);
		}else{
			query = path + query;
		}

		if(query.startsWith("./"))
		{
			query = query.substring(query.indexOf("./")+2,query.length());
		}

		fs = new FormServlet();
		cond = req.getParameter("cond");
		if(cond == null){
			cond = "";
		}else if(cond.trim().endsWith("=")){
			fs.errflg = true;
			cond = " false ";
		}

		String where = req.getParameter("where");
		if(where != null){
			where = new String(where.getBytes("ISO-8859-1"), enc); 
			if(cond.isEmpty()){
				cond = where;
			}else{
				cond += " AND " + where;
			}
		}

		//Log.info("bbbbbb"+cond);
		String tmp = getSession(query,req,null);
		//Log.info("bbbbbb2");

		message = req.getParameter("message");
		if(message != null){
			message = new String(message.getBytes("ISO-8859-1"), enc); 
		}
		if(message != null && tmp != null){
			fs.errflg = true;
			fs.errmessage += message;
			tmp = fs.errMessage2(tmp);
		}else if(message != null){
			fs.errflg = true;
			fs.errmessage += message;
			tmp = fs.errMessage(query);
		}else if(tmp != null){
			tmp = fs.errMessage2(tmp);
		}else{
			tmp = fs.errMessage(query);
		}
		Log.info("aaaaaa"+tmp);


		if(tmp != null && !tmp.isEmpty()){
			String[] args = {"-query",tmp,"-c",config, "-o",query,"-cond",cond,
					"-invokeservletpath",invokeservletpath,"-debug"};

			GlobalEnv.setGlobalEnv(args);
		}else{

			String[] args = {"-f",query,"-c",config, "-o",query,"-cond",cond,
					"-invokeservletpath",invokeservletpath,"-debug"};

			GlobalEnv.setGlobalEnv(args);
		}



		SSQLparser parser = new SSQLparser("online");

		if(GlobalEnv.getErrFlag() == 0)
		{
			CodeGenerator codegenerator = parser.getcodegenerator();

			if(GlobalEnv.getErrFlag() == 0)
			{
				DataConstructor dc = new DataConstructor(parser);

				if(GlobalEnv.getErrFlag() == 0 || GlobalEnv.getErr().contains("No Data Found"))
				{
					//add chie
					String[] headfoot = codegenerator.generateCode4(parser,dc.getData()).toString().split(" ###split### ");
					print.println(headfoot[0]);
					if(GlobalEnv.getErrFlag() == 0)
					{
						String code = codegenerator.generateCode2(parser, dc.getData()).toString();
						print.println(code);
						//    			Log.out("code:"+code);
					}
					//add chie
					print.println(headfoot[1]);
				}

			} 
			if (GlobalEnv.getErrFlag() == 0){
				Log.info("// completed normally //");
			}else{
				Log.info("error!");
				print.println(GlobalEnv.getErr());
			}
		}


//		InvokeSSQL issql = new InvokeSSQL(print, ienv, 0);

		//System.setOut(new PrintStream(out));
		out.close();

	}

	private String getSessionValue(String query){
		BufferedReader in;
		StringBuffer tmp = new StringBuffer();
		try {
			if(query.startsWith("http:")){
				URL fileurl = new URL(query);
				URLConnection fileurlConnection = fileurl.openConnection();
				in = new BufferedReader(new InputStreamReader(fileurlConnection.getInputStream(),"EUC-JP"));
			}else{
				in = new BufferedReader(new FileReader(query));
			}
			String line = null;
			while (true) {
				line = in.readLine();
				if (line == null){
					break;
				}
				//for comment statement
				//commented out by goto 20130412
//				if(line.startsWith("//"))
//					line = in.readLine();
				//changed by goto 20130412
				while(line!=null && line.contains("/*"))
	            {
	              	int s = line.indexOf("/*");
	              	String line1 = line.substring(0,s);
//		          	tmp.append(" "+line1);
	              	while(!line.contains("*/"))
	              		line = in.readLine();
	              	int t = line.indexOf("*/");
	              	line = line1+line.substring(t+2);
	            }
	            //added by goto 20130412
	            if(line!=null && line.contains("--")){
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
	            	tmp.append(" " + line);
			}
			in.close();
			String QueryString = tmp.toString();
			StringTokenizer st = new StringTokenizer(QueryString);
			boolean isSession = false;
			String session = new String();
			while(true){
				if (!st.hasMoreTokens()) {
					Log.err("*** No Query Specified ***");
					throw (new IllegalStateException());
				}
				String nt = st.nextToken();
				if(nt.equalsIgnoreCase("GENERATE")){
					break;
				}
				if( isSession || nt.equalsIgnoreCase("REQUEST")){
					session += nt + " ";
					isSession = true;
				}
			}

			if(isSession){
				if(session.indexOf("(") > 0 && session.indexOf(")") >0){
					session = session.substring(session.indexOf("(")+1,session.indexOf(")"));
				}else{
					Log.err("Error[InvokeServlet2] : Syntax Err");
				}
				return session;
			}else{
				return null;
			}

		} catch (Exception e) {
			GlobalEnv.addErr("Error[InvokeServlet2]:"+ e);
			return e.toString();
		}
	}

	public String getSession(String query, HttpServletRequest req,String querystring){
		String[] array_name = new String[1];
		String[] array_val = new String[1];
		int noSession = 0;

		//getSessionValue
		if(query != null){
			String session_var = getSessionValue(query); //ex : "id,name"
			if(session_var != null && !session_var.isEmpty()){
				Log.info("SESSION :" + session_var);
				String[] array_s = session_var.split(","); //"id,name" -> {"id","name"}
				HttpSession se = req.getSession(false);
				array_val = new String[array_s.length];

				if(se != null){
					for(int i = 0 ; i < array_s.length; i++){
						Log.info("VAL" + i +" = "+array_s[i]);
						String tmp = (String)se.getAttribute(array_s[i]);
						if(tmp!= null && !tmp.isEmpty()){
							array_val[i] = tmp;
						}else{
							array_val[i] = " ";
						}
						Log.info("VAL" + i +" = "+array_s[i] + ":" + array_val[i]);
					}
				}else{
					for(int i = 0 ; i < array_s.length; i++){
						array_val[i] = "";
					}
					noSession = 1;
					Log.info("nosessionerr");
				}
				array_name = array_s.clone();
			}
		}


		String readQuery;

		if(querystring != null)
			readQuery = querystring;
		else
			readQuery = readFile(query);

		//Log.info(tmp);

		int flg = 0; //session flg
		while(true){
			if(readQuery.contains("session(")){
				int st = readQuery.indexOf("session(");
				int en = readQuery.indexOf(")", st);
				int len = "session(".length();
				if(st >= 0 && en >= 0){
					String sptmp = readQuery.substring(st+len,en); //ex : id
					for(int i = 0 ; i < array_name.length; i++){
						if(sptmp.equalsIgnoreCase(array_name[i])){
							Log.info(sptmp + ">>" + array_val[i]);
							if(readQuery.toLowerCase().contains("where")){
								String s1 = readQuery.substring(0,readQuery.toLowerCase().indexOf("where"));
								String s2 = readQuery.substring(readQuery.toLowerCase().indexOf("where"),readQuery.length());
								s1 = s1.replace("session("+ array_name[i] +")","\""+array_val[i]+"\"");
								s2 = s2.replace("session("+ array_name[i] +")",""+array_val[i]+"");
								readQuery = s1 + s2;
							}else{
								readQuery = readQuery.replace("session("+ array_name[i] +")","\""+array_val[i]+"\"");
							}
							flg = 1;
							break;
						}else{
							flg = 0;
						}
					}
					if(flg == 0){
						Log.err("Err[InvokeServ2]: session variable " + sptmp + " does not exist");
						break;
					}
				}
			}else{
				break;
			}
		}


		if(noSession == 1 ){
			if(readQuery.toLowerCase().contains("errmessage()")){
				//readQuery = readQuery.replace("errmessage()","\""+FormEnv.getLogin()+"\"");
				fs.errflg = true;
				fs.errmessage += " "+FormEnv.getLogin()+" ";
				flg = 1;
			}
			if(readQuery.toLowerCase().contains("errmessage(\"")){
				int f = readQuery.toLowerCase().indexOf("errmessage(\"");
				int f2 = readQuery.toLowerCase().indexOf("errmessage(\"") + "errmessage(\"".length();
				int e = readQuery.toLowerCase().indexOf("\")",f2);
				int e2 = readQuery.toLowerCase().indexOf("\")",f2) + "\")".length();
				String message = readQuery.substring(f2,e);
				fs.errflg = true;
				//fs.errmessage += " "+message+" ";
				//readQuery = readQuery.substring(0,f) + message + readQuery.substring(e2);
				flg = 1;
			}
			if(readQuery.toLowerCase().contains("where")){
				int i = readQuery.toLowerCase().lastIndexOf("where");
				readQuery = readQuery.substring(0,i)+" WHERE false";
				flg = 1;
			}
		}

		Log.info(readQuery);
		if(flg == 1){
			return readQuery;
		}else{
			return null;
		}
	}

	private String readFile(String filename){
		BufferedReader in;
		String tmp= new String();
		try {
			if(filename.startsWith("http:")){
				URL fileurl = new URL(filename);
				URLConnection fileurlConnection = fileurl.openConnection();
				in = new BufferedReader(new InputStreamReader(fileurlConnection.getInputStream(),"EUC-JP"));
				while(true){
					String line = in.readLine();
					if (line == null){
						break;
					}
					//for comment statement
					//commented out by goto 20130412
//					if(line.startsWith("//"))
//						line = in.readLine();
					//changed by goto 20130412
					while(line!=null && line.contains("/*"))
		            {
		              	int s = line.indexOf("/*");
		              	String line1 = line.substring(0,s);
//			          	tmp.append(" "+line1);
		              	while(!line.contains("*/"))
		              		line = in.readLine();
		              	int t = line.indexOf("*/");
		              	line = line1+line.substring(t+2);
		            }
		            //added by goto 20130412
		            if(line!=null && line.contains("--")){
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
		            	tmp += " " + line.trim();
				}
			}else{
				in = new BufferedReader(new FileReader(filename));
			}
		}catch(Exception e){}
		return tmp;
	}

}