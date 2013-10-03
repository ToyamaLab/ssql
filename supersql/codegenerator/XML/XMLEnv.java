package supersql.codegenerator.XML;

import supersql.codegenerator.ITFE;
import supersql.codegenerator.LocalEnv;


public class XMLEnv extends LocalEnv {
    String data;
    String pre_operator;
    int total_element = 0;
    int glevel = 0;
    String filename;
    String outfile;
    String linkoutfile;
    String nextbackfile = new String();
    String outdir;
    int countfile;

    public static String getClassID(ITFE tfe) {
    	String result;

        result =  "TFE" + tfe.getId();
        	return result;
    }

}