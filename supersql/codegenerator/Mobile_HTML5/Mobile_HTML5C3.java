package supersql.codegenerator.Mobile_HTML5;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class Mobile_HTML5C3 extends Connector {

    Manager manager;

    Mobile_HTML5Env html_env;
    Mobile_HTML5Env html_env2;
    
    //20130309
    //public static boolean C3Flg=false;

    //･ｳ･�ｹ･ﾈ･鬣ｯ･ｿ
    public Mobile_HTML5C3(Manager manager, Mobile_HTML5Env henv, Mobile_HTML5Env henv2) {
        this.manager = manager;
        this.html_env = henv;
        this.html_env2 = henv2;
    }

    //C3､ﾎwork･皈ｽ･ﾃ･ﾉ
    @Override
	public String work(ExtList data_info) {
        int i = 0;
        String parentfile = html_env.filename;
        String parentfile2 = html_env2.filename;
        StringBuffer parentcode = new StringBuffer();
        StringBuffer parentcss = new StringBuffer();
        StringBuffer parentheader = new StringBuffer();
        StringBuffer parentfooter = new StringBuffer();
        StringBuffer parentcode2 = new StringBuffer();
        StringBuffer parentcss2 = new StringBuffer();
        StringBuffer parentheader2 = new StringBuffer();
        StringBuffer parentfooter2 = new StringBuffer();
        String linkfile = new String();
        ITFE[] tfe = new ITFE[tfeItems];
        int c3items = tfeItems;
        for (int j = 0; j < tfeItems - 1; j++) {
            tfe[j] = (ITFE) tfes.get(j);
            if (j < tfeItems - 2 && tfe[j] instanceof Mobile_HTML5G3) {
                System.err.println("Error: % after []% is not allowed");
                GlobalEnv.addErr("Error: % after []% is not allowed");
                //comment out by chie
                //System.exit(-1);
            }
        }
        boolean checknexttfe = tfe[0] instanceof Mobile_HTML5C1
                || tfe[0] instanceof Mobile_HTML5C2 || tfe[0] instanceof Mobile_HTML5C3;
        Log.out("------- C3 -------");
        //C3Flg=true; 	//20130309
     //   HTMLC1.Count=0;
     //   HTMLC1.jj=0;
        //Log.info("	[C3    start!!!]");		//20130309
//        Log.info("    HTMLC1.Count="+HTMLC1.Count);
//        HTMLC1.Count=0;
//        Log.info("    HTMLC1.Count="+HTMLC1.Count);
        html_env.countfile++;

        html_env.linkurl = html_env.linkoutfile
                + String.valueOf(html_env.countfile) + ".html";
        html_env.link_flag++;
        Log.out("linkflag =" + html_env.link_flag);
        this.setDataList(data_info);

        this.worknextItem();
        html_env.link_flag--;

        for (int k = 1; k < c3items; k++) {
            ITFE intfe = (ITFE) tfes.get(k);
            html_env.filename = html_env.outfile
                    + String.valueOf(html_env.countfile) + ".html";
            html_env2.filename = html_env.outfile
            + String.valueOf(html_env.countfile) + ".xml";
            boolean b = intfe instanceof Attribute
                    || intfe instanceof Mobile_HTML5Function;
            if (intfe instanceof Mobile_HTML5G3) {
                html_env.countfile--;
                this.worknextItem();
            } else {
                parentcode = html_env.code;
                parentcss = html_env.css;
                parentheader = html_env.header;
                parentfooter = html_env.footer;
                html_env.code = new StringBuffer();
                html_env.header = new StringBuffer();
                html_env.footer = new StringBuffer();

                parentcode2 = html_env2.code;
                parentcss2 = html_env2.css;
                parentheader2 = html_env2.header;
                parentfooter2 = html_env2.footer;
                html_env2.code = new StringBuffer();
                html_env2.header = new StringBuffer();
                html_env2.footer = new StringBuffer();

                if (k < c3items - 1) {
                    html_env.countfile++;
                    html_env.linkurl = html_env.linkoutfile
                            + String.valueOf(html_env.countfile) + ".html";
                    html_env.link_flag++;
                    Log.out("linkflag =" + html_env.link_flag);
                }

                html_env.setOutlineMode();
                this.worknextItem();

                if (k < c3items - 1) {
                    html_env.link_flag--;
                }
                html_env.getHeader(1);
                html_env.getFooter(1);
                //html_env2.header.append("<?xml version=\"1.0\" encoding=\"Shift_JIS\"?><SSQL>");
                html_env2.header.append("<?xml version=\"1.0\" encoding=\""+html_env.getEncode()+"\"?><SSQL>");
                html_env2.footer.append("</SSQL>");
                try {
            		//changed by goto 20120715_2 start
                	//This is for '%'.
    	        	//PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(
    	            //        html_env.filename)));
            		PrintWriter pw;
    	            if (html_env.charset != null){
    		        	pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
    		        			new FileOutputStream(html_env.filename),html_env.charset)));
    		        	//Log.info("File encoding: "+html_env.charset);
    	            }else
    	            	pw = new PrintWriter(new BufferedWriter(new FileWriter(
    	        	                    html_env.filename)));
    	            //Log.info("File encoding: "+((html_env.charset!=null)? html_env.charset : "UTF-8"));
            		//changed by goto 20120715_2 end
                    pw.println(html_env.header);
                    pw.println(html_env.code);
                    pw.println(html_env.footer);
                    pw.close();
                    //html_env.header = new StringBuffer();
                    //html_env.footer = new StringBuffer();
	                if(GlobalEnv.isOpt()){
		        		//changed by goto 20120715_2 start
			        	//PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(
			            //        html_env2.filename)));
		        		PrintWriter pw2;
			            if (html_env.charset != null){
				        	pw2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				        			new FileOutputStream(html_env2.filename),html_env.charset)));
				        	//Log.info("File encoding: "+html_env.charset);
			            }else
			            	pw2 = new PrintWriter(new BufferedWriter(new FileWriter(
			        	                    html_env2.filename)));
			            //Log.info("File encoding: "+((html_env.charset!=null)? html_env.charset : "UTF-8"));
		        		//changed by goto 20120715_2 end
			            
	                    pw2.println(html_env2.header);
	                    pw2.println(html_env2.code);
	                    pw2.println(html_env2.footer);
	                    pw2.close();
	                    Mobile_HTML5optimizer xml = new Mobile_HTML5optimizer();
	                    String xml_str = xml.generateHtml(html_env2.filename);
	                    pw = new PrintWriter(new BufferedWriter(new FileWriter(html_env.filename)));
						pw.println(html_env.header);
						pw.println(xml_str);
						StringBuffer footer = new StringBuffer("</div></body></html>");
						pw.println(footer);
						pw.close();
                    }
                } catch (FileNotFoundException fe) {
                	fe.printStackTrace();
                    System.err.println("Error: specified outdirectory \""
                            + html_env.outdir + "\" is not found");
                    GlobalEnv.addErr("Error: specified outdirectory \""
                            + html_env.outdir + "\" is not found");
                    //comment out by chie
                    //System.exit(-1);
                } catch (IOException e) {
                    System.err.println("Error[HTMLC3]: File IO Error in HTMLC3");
                    e.printStackTrace();
                    GlobalEnv.addErr("Error[HTMLC3]: File IO Error in HTMLC3");
                    //comment out by chie
                    //System.exit(-1);
                }
                html_env.code = parentcode;
                html_env.css = parentcss;
                html_env.header = parentheader;
                html_env.footer = parentfooter;
                html_env2.code = parentcode2;
                html_env2.css = parentcss2;
                html_env2.header = parentheader2;
                html_env2.footer = parentfooter2;
            }
        }
        html_env.filename = parentfile;
        html_env2.filename = parentfile2;

        Log.out("TFEId = " + Mobile_HTML5Env.getClassID(this));
        html_env.append_css_def_td(Mobile_HTML5Env.getClassID(this), this.decos);

        //20130309
        //C3Flg=false;
        //Log.info("	[C3    end!!!]");
        return null;
    }

    @Override
	public String getSymbol() {
        return "Mobile_HTML5C3";
    }

}