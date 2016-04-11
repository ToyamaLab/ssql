package supersql.form;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import supersql.codegenerator.CodeGenerator;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.dataconstructor.DataConstructor;
import supersql.parser.SSQLparser;

public class FormServlet3 extends HttpServlet {
	
  private static final long serialVersionUID = 8021503235844232672L;

  
  public void doPost(HttpServletRequest req, 
                      HttpServletResponse res) 
                          throws ServletException, IOException {

	long start = System.currentTimeMillis();
		
    res.setContentType("text/html; charset=Shift-JIS");
    req.setCharacterEncoding("Shift-JIS");
    
    PrintWriter out = res.getWriter();

    String sqlfile = new String();
    try{
    	sqlfile = req.getParameter("sqlfile");
    }catch(NullPointerException e){
    	out.println("no ssql file \n");
    	System.exit(-1);
    }
    
    String configfile = new String(); 
   	try{
		configfile = req.getParameter("configfile");
	}catch(NullPointerException e)
	{
		out.println("no config file defined /n");
		System.exit(-1);
		
	}
	
	//�N�G���ǂݍ���
	String tmp_query = getQuery(sqlfile,req,res);
    String Query = new String(tmp_query.toString());
    StringTokenizer st = new StringTokenizer(Query,"\t{}[]!,()@= \"' ",true);

    StringBuffer QueryBuffer = new StringBuffer();
    while(st.hasMoreTokens())
    {
		String tmp = st.nextToken();
    	if(tmp.equalsIgnoreCase("where"))
    	{
    		QueryBuffer.append(tmp);
    		break;
    	}
    	else
    		QueryBuffer.append(tmp);
    }

    StringBuffer wherebuffer = new StringBuffer();
    
    while(st.hasMoreTokens())
    {
  
    	String tmp = st.nextToken();
    	if(tmp.equalsIgnoreCase("order") 
    			|| tmp.equalsIgnoreCase("having")
    			|| tmp.equalsIgnoreCase("group") )
    		break;
    	else
    		wherebuffer.append(tmp);
    }
    
    String where = QueryConverter(wherebuffer.toString(),out);
    QueryBuffer.append( " " + changeQuery(where,req,res,out));

    while(st.hasMoreTokens())
    {
    	String tmp = st.nextToken();
    	QueryBuffer.append(tmp);
    }
    
    Log.err("Query: " + QueryBuffer);
    String[] args = {"-c",configfile, "-o",sqlfile,"-ajax","-servlet"};
    
    GlobalEnv.setGlobalEnv(args);
    
    SSQLparser parser = new SSQLparser(QueryBuffer);

	if(GlobalEnv.getErrFlag() == 0)
	{
	    CodeGenerator codegenerator = parser.getcodegenerator();
		
	    if(GlobalEnv.getErrFlag() == 0)
		{
	    	DataConstructor dc = new DataConstructor(parser);
			
	    	if(GlobalEnv.getErrFlag() == 0)
			{
	    		//add chie
	    		String[] headfoot = codegenerator.generateCode4(parser,dc.getData()).toString().split(" ###split### ");
	    		out.println(headfoot[0]);
	    		if(GlobalEnv.getErrFlag() == 0)
	    		{
	    			out.println(codegenerator.generateCode2(parser, dc.getData()));
	    		}
	    		out.println(headfoot[1]);
			}
		}
	    	
    } 
    
   	if(GlobalEnv.getErrFlag() == 1)
   	{
   		out.println("<font color=RED>");
   		out.println("ERR " + GlobalEnv.getErr());
   	}
   	else
   	{
		long end = System.currentTimeMillis();
		Log.info("���s���ԁF" + ( end - start ) + "�~���b�ł��B");
   	}

  }
  
  
  public String getQuery(String sqlfile, HttpServletRequest req, 
          HttpServletResponse res) 
  			throws ServletException, IOException {
	  
	    BufferedReader dis;
		URL fileurl = new URL(sqlfile);    
	    URLConnection fileurlConnection = fileurl.openConnection();
	    String line = new String();
	    StringBuffer tmp_query = new StringBuffer();
	    
	    dis = new BufferedReader(new InputStreamReader(fileurlConnection.getInputStream(),"JISAutoDetect"));
	    
	    while (true) {
	        line = dis.readLine();

	    	if (line == null || line.equals("-1"))
	            break;
			//changed by goto 20130412
	    	while(line!=null && line.contains("/*"))
	        {
	          	int s = line.indexOf("/*");
	          	String line1 = line.substring(0,s);
	          	while(!line.contains("*/"))
	          		line = dis.readLine();
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
	        	tmp_query.append(" " + line);
	    }
	  return tmp_query.toString();  
  	}
  

  public String QueryConverter(String where, PrintWriter out)
  {
	  StringTokenizer st = new StringTokenizer(where,"\t{}[]!,()@= \"' ",true);
	  StringBuffer tmpBuffer = new StringBuffer();
	  StringBuffer QueryBuffer = new StringBuffer();
	  
	  while(st.hasMoreTokens())
	  {
		  String tmp = st.nextToken();
		  
		  tmpBuffer.append(tmp);
		  if(tmp.equalsIgnoreCase("AND"))
			  {
			  	QueryBuffer.append(tmpBuffer.toString());
			  	tmpBuffer = new StringBuffer();
			  }
		  else if(tmp.equalsIgnoreCase("BETWEEN"))
		  {
			  int andnum = 0;
			  while(st.hasMoreTokens())
			  {
				  tmp = st.nextToken();
				  if( (tmp.equalsIgnoreCase("AND") || tmp.equalsIgnoreCase("OR") )&& andnum == 1)
					   break;		

				  if(tmp.equalsIgnoreCase("AND"))
					  andnum++;
				  tmpBuffer.append(tmp);
			  }			  
			  String result =  betweenconverter(tmpBuffer.toString(),out);
			  QueryBuffer.append(result + " " + tmp);
			  
			  tmpBuffer = new StringBuffer();
		  }
		  else if(tmp.equalsIgnoreCase("IN"))
		  {
			  int string = 0;
			  String result = new String();
			  
			  while(st.hasMoreTokens())
			  {
				 String tmp2 = st.nextToken();
				 
				 if(tmp2.contains("'"))
					 string = 1;
				 
				 tmpBuffer.append(tmp2 + " ");
				 
				 if(tmp2.equals(")"))
					 break;
			  } 
			  result = inconverter(tmpBuffer.toString(),out,string);
			  
			  QueryBuffer.append(result + " ");
			  tmpBuffer = new StringBuffer();
		  }
		  else{
			  	QueryBuffer.append(tmpBuffer.toString());
			  	tmpBuffer = new StringBuffer();  
		  }
	  }
	  return QueryBuffer.toString().trim();
  }
  
  
  public String changeQuery(String where, HttpServletRequest req, 
          HttpServletResponse res,PrintWriter out) 
			throws ServletException, IOException {
	  
	  StringBuffer wherebuffer = new StringBuffer();
	  StringTokenizer st = new StringTokenizer(where,"\t{}[]!,()@ \"' ",true);	 
	  String tmp = new String();
	  int bracketNum = 0;
	  StringBuffer tmpbuffer = new StringBuffer();
	  int inner_flag = 0;
	  String result = new String();
	  String BeforeOperator = new String();
	  String tmp2 = new String();
	  
	  while(st.hasMoreTokens())
	  {
		  tmp = st.nextToken();

		  if(tmp.equals("("))
			  bracketNum += 1;
		  if(tmp.equals(")"))
			  bracketNum -= 1;

		  if( ( !st.hasMoreTokens() || tmp.equalsIgnoreCase("AND") || tmp.equalsIgnoreCase("OR") ) && bracketNum <= 0)
		  {
			  if(!st.hasMoreTokens())
				  tmpbuffer.append(tmp);
			
			  if(inner_flag == 0)
				  result = getParameter(tmpbuffer.toString(),req,res,out) ;
			  else
			  {
				  tmp2 = tmpbuffer.toString();
				  int rpos = tmp2.lastIndexOf(")");
				  int lpos = tmp2.indexOf('(') + 1;
				  if(rpos < tmp2.length() && rpos > lpos)
					  tmp2 = tmp2.substring(lpos, rpos);
				  
				  result = changeQuery(tmp2,req,res,out);
			  }
			  if(BeforeOperator.length() != 0)
			  {
				wherebuffer.append(" "+BeforeOperator+" ");  
			  }
			  
			  tmpbuffer = new StringBuffer();
			  
			  if(!result.equals("undefined"))
			  {  
				  if(inner_flag != 0)
					  wherebuffer.append("(");

				  wherebuffer.append(result);
				  
				  if(inner_flag != 0)
					  wherebuffer.append(")");
			  }
			  else
			  {
				  if(inner_flag != 0)
					  wherebuffer.append("(");

				  if(tmp.toString().equalsIgnoreCase("AND"))
					  wherebuffer.append(" True ");
				  else if(BeforeOperator.equalsIgnoreCase("AND"))
					  wherebuffer.append(" True ");
				  else
					  wherebuffer.append(" False ");
				  

				  if(inner_flag != 0)
					  wherebuffer.append(")");
			  }  
			  
			  BeforeOperator = tmp;
			  
		  }
		  else if((tmp.equalsIgnoreCase("AND") || tmp.equalsIgnoreCase("OR") ) && bracketNum != 0)
		  {
			  tmpbuffer.append(" " + tmp + " ");
			  inner_flag = 1;
		  }
		  else
			  tmpbuffer.append(tmp);
		  

	  }
	  String Query = wherebuffer.toString();
	  return Query;
	}
  
  public String getParameter(String where, HttpServletRequest req, 
          HttpServletResponse res, PrintWriter out) 
	throws ServletException, IOException {
	  StringTokenizer st = new StringTokenizer(where,"%\t{}[]!,()@= \"\' ",true);
	  StringBuffer tmpBuffer = new StringBuffer();
	  String read = new String("");
	  int undefFlag = 0;
	  
	  while(st.hasMoreTokens())
	  {
		String tmp = st.nextToken();
		
		if(tmp.contains("$"))
		{
			read = req.getParameter(tmp);
			
			if(read != null)
				if(read.length() != 0)
					tmpBuffer.append(read);
				else
					undefFlag = 1;
			else
				undefFlag = 1;
		}
		else if(tmpBuffer.toString().contains("\'") && tmp.equals(" "))
		{}
		else
			tmpBuffer.append(tmp);
		
	  }
	  if(undefFlag == 0)
		  return tmpBuffer.toString();
	  else
		  return "undefined";
  }
  
  public String betweenconverter(String tmpwhere, PrintWriter out)
  {
	  StringTokenizer st = new StringTokenizer(tmpwhere);
	  
	  String query = new String();
	  String att = st.nextToken();
	  st.nextToken();
	  
	  String lower = st.nextToken();
	  st.nextToken();
	  String upper = st.nextToken();
	  	  
	  query = " ( " + att + " >= " + lower + " AND " + att + " <= " + upper + " ) ";
	  return query;
  }
  
  public String inconverter(String tmpwhere, PrintWriter out,int string)
  {
	  StringTokenizer st = new StringTokenizer(tmpwhere,"\t{}[]!,()@= \"' ",false);
	  StringBuffer query = new StringBuffer();
	  
	  String att = st.nextToken();
	  st.nextToken();
	  
	  int flag = 0;
	  
	  query.append(" ( ");
	  while(st.hasMoreTokens())
	  {
		  if(flag == 1)
			  query.append(" OR ");
		  
		  if(string == 0) 
			  query.append(" " + att + " = " + st.nextToken() + " ");
		  else
			  query.append(" " + att + " like '" + st.nextToken() + "' ");
		  
		  flag = 1;
		  
	  }
	  query.append(" ) ");
	  return query.toString();
  }
  
}