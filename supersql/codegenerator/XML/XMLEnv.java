package supersql.codegenerator.XML;

import java.io.PrintWriter;
import java.util.Vector;

import supersql.codegenerator.ITFE;
import supersql.codegenerator.LocalEnv;


public class XMLEnv extends LocalEnv {
    String data;
    String pre_operator;
    Vector written_classid;
    Vector not_written_classid;
    int total_element = 0;
    int glevel = 0;
    String filename;
    String outfile;
    String linkoutfile;
    String nextbackfile = new String();
    String outdir;
    int countfile;
    PrintWriter writer;
    public StringBuffer code;
    StringBuffer header;
    StringBuffer footer;

    public XMLEnv() {
    }

    public String getEncode(){

    	String osname = System.getProperty("os.name");

    	if(osname.indexOf("Windows")>=0){
        	return "Shift_JIS";
    	}else{
    		return "EUC_JP";
    	}
    }

    public void getHeader() {
   		int index = 0;
   	   	String osname = System.getProperty("os.name");

   		if(osname.indexOf("Windows")>=0){
   			header.insert(index, "<?xml version=\"1.0\" encoding=\"Shift_JIS\" ?>\n");
   		}
   		else{
   			header.insert(index, "<?xml version=\"1.0\" encoding=\"EUC_JP\" ?>\n");
   		}
    }

    public void getFooter() {

    }

    public static String getClassID(ITFE tfe) {
    	String result;

        result =  "TFE" + tfe.getId();
        	return result;
    }

}