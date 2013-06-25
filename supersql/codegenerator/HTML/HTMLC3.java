package supersql.codegenerator.HTML;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.common.Utils;
import supersql.extendclass.ExtList;

public class HTMLC3 extends Connector {

	private HTMLEnv htmlEnv;
	private HTMLEnv htmlEnv2;

    //･ｳ･�ｹ･ﾈ･鬣ｯ･ｿ
    public HTMLC3(Manager manager, HTMLEnv henv, HTMLEnv henv2) {
        this.htmlEnv = henv;
        this.htmlEnv2 = henv2;
    }
    
    public Element createNode(ExtList data_info){
    	
    	//TODO
    	Element result = new Element(Tag.valueOf("div"), "");
    	
    	String parentfile = htmlEnv.fileName;
        String parentfile2 = htmlEnv2.fileName;
        StringBuffer parentcode = new StringBuffer();
        StringBuffer parentcss = new StringBuffer();
        StringBuffer parentheader = new StringBuffer();
        StringBuffer parentfooter = new StringBuffer();
        StringBuffer parentcode2 = new StringBuffer();
        StringBuffer parentcss2 = new StringBuffer();
        StringBuffer parentheader2 = new StringBuffer();
        StringBuffer parentfooter2 = new StringBuffer();
        ITFE[] tfe = new ITFE[tfeItems];
        int c3items = tfeItems;
        for (int j = 0; j < tfeItems - 1; j++) {
            tfe[j] = (ITFE) tfes.get(j);
            if (j < tfeItems - 2 && tfe[j] instanceof HTMLG3) {
                System.err.println("Error: % after []% is not allowed");
                GlobalEnv.addErr("Error: % after []% is not allowed");
            }
        }
        htmlEnv.countFile++;

        htmlEnv.linkUrl = htmlEnv.linkOutFile
                + String.valueOf(htmlEnv.countFile) + ".html";
        htmlEnv.linkFlag++;
        this.setDataList(data_info);

        result.appendChild((Element) this.createNextItemNode(data_info));
        htmlEnv.linkFlag--;

        for (int k = 1; k < c3items; k++) {
            ITFE intfe = (ITFE) tfes.get(k);
            htmlEnv.fileName = htmlEnv.outFile
                    + String.valueOf(htmlEnv.countFile) + ".html";
            if (intfe instanceof HTMLG3) {
                htmlEnv.countFile--;
                result.appendChild((Element) this.createNextItemNode(data_info));
            } else {
                parentcode = htmlEnv.code;
                parentcss = htmlEnv.css;
                parentheader = htmlEnv.header;
                parentfooter = htmlEnv.footer;
                htmlEnv.code = new StringBuffer();
                htmlEnv.header = new StringBuffer();
                htmlEnv.footer = new StringBuffer();

                if (k < c3items - 1) {
                    htmlEnv.countFile++;
                    htmlEnv.linkUrl = htmlEnv.linkOutFile
                            + String.valueOf(htmlEnv.countFile) + ".html";
                    htmlEnv.linkFlag++;
                }

                htmlEnv.setOutlineMode();
                result.appendChild((Element) this.createNextItemNode(data_info));

                if (k < c3items - 1) {
                    htmlEnv.linkFlag--;
                }
                htmlEnv.getHeader();
                htmlEnv.getFooter();
                htmlEnv.code = parentcode;
                htmlEnv.css = parentcss;
                htmlEnv.header = parentheader;
                htmlEnv.footer = parentfooter;
            }
        }
        // TODO
        htmlEnv.fileName = parentfile;

        //result.appendChild(cssDefTd(HTMLEnv.getClassID(this), this.decos));
        htmlEnv.append_css_def_td(HTMLEnv.getClassID(this), this.decos);
        return result;
    }

    //C3､ﾎwork･皈ｽ･ﾃ･ﾉ
    @Override
	public void work(ExtList data_info) {
        String parentfile = htmlEnv.fileName;
        String parentfile2 = htmlEnv2.fileName;
        StringBuffer parentcode = new StringBuffer();
        StringBuffer parentcss = new StringBuffer();
        StringBuffer parentheader = new StringBuffer();
        StringBuffer parentfooter = new StringBuffer();
        StringBuffer parentcode2 = new StringBuffer();
        StringBuffer parentcss2 = new StringBuffer();
        StringBuffer parentheader2 = new StringBuffer();
        StringBuffer parentfooter2 = new StringBuffer();
        new String();
        ITFE[] tfe = new ITFE[tfeItems];
        int c3items = tfeItems;
        for (int j = 0; j < tfeItems - 1; j++) {
            tfe[j] = (ITFE) tfes.get(j);
            if (j < tfeItems - 2 && tfe[j] instanceof HTMLG3) {
                System.err.println("Error: % after []% is not allowed");
                GlobalEnv.addErr("Error: % after []% is not allowed");
                //comment out by chie
                //System.exit(-1);
            }
        }
        Log.out("------- C3 -------");
        htmlEnv.countFile++;

        htmlEnv.linkUrl = htmlEnv.linkOutFile
                + String.valueOf(htmlEnv.countFile) + ".html";
        htmlEnv.linkFlag++;
        Log.out("linkflag =" + htmlEnv.linkFlag);
        this.setDataList(data_info);

        this.worknextItem();
        htmlEnv.linkFlag--;

        for (int k = 1; k < c3items; k++) {
            ITFE intfe = (ITFE) tfes.get(k);
            htmlEnv.fileName = htmlEnv.outFile
                    + String.valueOf(htmlEnv.countFile) + ".html";
            htmlEnv2.fileName = htmlEnv.outFile
            + String.valueOf(htmlEnv.countFile) + ".xml";
            if (intfe instanceof HTMLG3) {
                htmlEnv.countFile--;
                this.worknextItem();
            } else {
                parentcode = htmlEnv.code;
                parentcss = htmlEnv.css;
                parentheader = htmlEnv.header;
                parentfooter = htmlEnv.footer;
                htmlEnv.code = new StringBuffer();
                htmlEnv.header = new StringBuffer();
                htmlEnv.footer = new StringBuffer();

                parentcode2 = htmlEnv2.code;
                parentcss2 = htmlEnv2.css;
                parentheader2 = htmlEnv2.header;
                parentfooter2 = htmlEnv2.footer;
                htmlEnv2.code = new StringBuffer();
                htmlEnv2.header = new StringBuffer();
                htmlEnv2.footer = new StringBuffer();

                if (k < c3items - 1) {
                    htmlEnv.countFile++;
                    htmlEnv.linkUrl = htmlEnv.linkOutFile
                            + String.valueOf(htmlEnv.countFile) + ".html";
                    htmlEnv.linkFlag++;
                    Log.out("linkflag =" + htmlEnv.linkFlag);
                }

                htmlEnv.setOutlineMode();
                this.worknextItem();

                if (k < c3items - 1) {
                    htmlEnv.linkFlag--;
                }
                htmlEnv.getHeader();
                htmlEnv.getFooter();
                //html_env2.header.append("<?xml version=\"1.0\" encoding=\"Shift_JIS\"?><SSQL>");
                htmlEnv2.header.append("<?xml version=\"1.0\" encoding=\""+Utils.getEncode()+"\"?><SSQL>");
                htmlEnv2.footer.append("</SSQL>");
                try {
            		//changed by goto 20120715_2 start
                	//This is for '%'.
    	        	//PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(
    	            //        html_env.filename)));
            		PrintWriter pw;
    	            if (htmlEnv.charset != null){
    		        	pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
    		        			new FileOutputStream(htmlEnv.fileName),htmlEnv.charset)));
    		        	//Log.info("File encoding: "+html_env.charset);
    	            }else
    	            	pw = new PrintWriter(new BufferedWriter(new FileWriter(
    	        	                    htmlEnv.fileName)));
    	            //Log.info("File encoding: "+((html_env.charset!=null)? html_env.charset : "UTF-8"));
            		//changed by goto 20120715_2 end
                    pw.println(htmlEnv.header);
                    pw.println(htmlEnv.code);
                    pw.println(htmlEnv.footer);
                    pw.close();
                    //html_env.header = new StringBuffer();
                    //html_env.footer = new StringBuffer();
	                if(GlobalEnv.isOpt()){
		        		//changed by goto 20120715_2 start
			        	//PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(
			            //        html_env2.filename)));
		        		PrintWriter pw2;
			            if (htmlEnv.charset != null){
				        	pw2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				        			new FileOutputStream(htmlEnv2.fileName),htmlEnv.charset)));
				        	//Log.info("File encoding: "+html_env.charset);
			            }else
			            	pw2 = new PrintWriter(new BufferedWriter(new FileWriter(
			        	                    htmlEnv2.fileName)));
			            //Log.info("File encoding: "+((html_env.charset!=null)? html_env.charset : "UTF-8"));
		        		//changed by goto 20120715_2 end
			            
	                    pw2.println(htmlEnv2.header);
	                    pw2.println(htmlEnv2.code);
	                    pw2.println(htmlEnv2.footer);
	                    pw2.close();
	                    HTMLoptimizer xml = new HTMLoptimizer();
	                    String xml_str = xml.generateHtml(htmlEnv2.fileName);
	                    pw = new PrintWriter(new BufferedWriter(new FileWriter(htmlEnv.fileName)));
						pw.println(htmlEnv.header);
						pw.println(xml_str);
						StringBuffer footer = new StringBuffer("</div></body></html>");
						pw.println(footer);
						pw.close();
                    }
                } catch (FileNotFoundException fe) {
                	fe.printStackTrace();
                    System.err.println("Error: specified outdirectory \""
                            + htmlEnv.outDir + "\" is not found");
                    GlobalEnv.addErr("Error: specified outdirectory \""
                            + htmlEnv.outDir + "\" is not found");
                    //comment out by chie
                    //System.exit(-1);
                } catch (IOException e) {
                    System.err.println("Error[HTMLC3]: File IO Error in HTMLC3");
                    e.printStackTrace();
                    GlobalEnv.addErr("Error[HTMLC3]: File IO Error in HTMLC3");
                    //comment out by chie
                    //System.exit(-1);
                }
                htmlEnv.code = parentcode;
                htmlEnv.css = parentcss;
                htmlEnv.header = parentheader;
                htmlEnv.footer = parentfooter;
                htmlEnv2.code = parentcode2;
                htmlEnv2.css = parentcss2;
                htmlEnv2.header = parentheader2;
                htmlEnv2.footer = parentfooter2;
            }
        }
        htmlEnv.fileName = parentfile;
        htmlEnv2.fileName = parentfile2;

        Log.out("TFEId = " + HTMLEnv.getClassID(this));
        htmlEnv.append_css_def_td(HTMLEnv.getClassID(this), this.decos);

    }

    @Override
	public String getSymbol() {
        return "HTMLC3";
    }

}
