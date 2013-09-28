package supersql.codegenerator.XML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;


public class XMLManager extends Manager{

    XMLEnv xml_env;
    private Document doc;

    public XMLManager(XMLEnv xenv) {
        this.xml_env = xenv;

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			this.setDoc(docBuilder.newDocument());

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void generateCode(ITFE tfe_info, ExtList<ExtList<String>> data_info) {
        xml_env.countfile = 0;
        xml_env.code = new StringBuffer();
        xml_env.header = new StringBuffer();

        getOutfilename();
        xml_env.filename = xml_env.outfile + ".xml";

        if(data_info.size() == 0)
        {
        	Log.out("no data");
        	xml_env.code.append("NO DATA FOUND");
        }
        else {
        	tfe_info.work(data_info);
        }

        xml_env.getHeader();

        try {
        	if(!GlobalEnv.isOpt()){
	        	PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
	                    xml_env.filename)));

	            pw.println(xml_env.header);
	            pw.println(xml_env.code);
	            pw.close();
        	}
        } catch (FileNotFoundException fe) {
        	fe.printStackTrace();
        	System.err.println("Error: specified outdirectory \""
                    + xml_env.outdir + "\" is not found to write " + xml_env.filename );
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Error[XMLManager]: File IO Error in XMLManager");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void generateDOMCode(ITFE tfe_info, ExtList<ExtList<String>> data_info) {
		try {
			Element rootElement = this.getDoc().createElement("ssql");
			this.getDoc().appendChild(rootElement);

	        if(data_info.size() == 0)
	        {
	        	Log.out("no data");
	        	rootElement.appendChild(this.getDoc().createTextNode("NO DATA FOUND"));
	        }
	        else {
	        	Element childNode = (Element) tfe_info.createNode(data_info);
		        rootElement.appendChild(childNode);
	        }
	        
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
	        getOutfilename();
	        xml_env.filename = xml_env.outfile + ".xml";
			StreamResult result = new StreamResult(new File(xml_env.filename));
			DOMSource source = new DOMSource(this.getDoc());
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
    }
    
    private void getOutfilename() {
        String file = GlobalEnv.getfilename();
        String outdir = GlobalEnv.getoutdirectory();
        String outfile = GlobalEnv.getoutfilename();
        xml_env.outdir = outdir;

        if (outfile == null) {
        	if (file.indexOf(".sql")>0) {
        		xml_env.outfile = file.substring(0, file.indexOf(".sql"));
        	} else if (file.indexOf(".ssql")>0) {
        		xml_env.outfile = file.substring(0, file.indexOf(".ssql"));
        	}
        } else {
            xml_env.outfile = getOutfile(outfile);
        }

        if (xml_env.outfile.indexOf("/") > 0) {
            xml_env.linkoutfile = xml_env.outfile.substring(xml_env.outfile
                    .lastIndexOf("/") + 1);
        } else {
            xml_env.linkoutfile = xml_env.outfile;
        }

        if (outdir != null) {
            connectOutdir(outdir, outfile);
        }
    }

    private String getOutfile(String outfile) {
        String out = new String();
        if (outfile.indexOf(".xml") > 0) {
            out = outfile.substring(0, outfile.indexOf(".xml"));
        } else {
            out = outfile;
        }
        return out;
    }

    private void connectOutdir(String outdir, String outfile) {
        String tmpqueryfile = new String();
        if (xml_env.outfile.indexOf("/") > 0) {
            if (outfile != null) {
                if (xml_env.outfile.startsWith(".")
                        || xml_env.outfile.startsWith("/")) {
                    tmpqueryfile = xml_env.outfile.substring(xml_env.outfile
                            .indexOf("/") + 1);
                }
            } else {
                tmpqueryfile = xml_env.outfile.substring(xml_env.outfile
                        .lastIndexOf("/") + 1);
            }
        } else {
            tmpqueryfile = xml_env.outfile;
        }
        if (!outdir.endsWith("/")) {
            outdir = outdir.concat("/");
        }
        xml_env.outfile = outdir.concat(tmpqueryfile);
    }

    public void finish() {

    }


	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}
}