package supersql.invoke;

import java.io.File;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class InvokeEnv {

	// dbhost
	public static final String dbHost = "localhost";

	// dbuser
	public static final String dbUser = "mai";

	// query
	public static final String queryBaseDir = "/opt/http/htdocs/ssql";
	// ImageFile
	public static final String wwwBaseURI = "http://ssql.db.ics.keio.ac.jp";
	// Invoke�
	public static final String InvokeDir = "/tmp/Invoke";

	// Invoke
	String InvokeServletPath = "";

	//dbname
	String dbname = "cinema";

	//filename
	String filename = "";

	// Invoke、where
	String dyna_where = "";

	// Invoke、html
	String SSQLhtmlfile;

	// HttpRequest
	String querystring = "";

	public InvokeEnv(HttpServletRequest req) {

		querystring = req.getQueryString();

		StringTokenizer st = new StringTokenizer(querystring, "+");
		int paramcount = st.countTokens();

		/*
		 * Invoke function : <td> <a
		 * href="${server_path}/supersql.invoke.InvokeServlet?
		 * ${dbname}+${query_filename}+${added_condition}"> TFE </a> </td>
		 */
		for (int i = 0; i < paramcount; i++) {
			switch (i) {
			case 0:
				dbname = decode(st.nextToken());
				break;
			case 1:
				filename = decode(st.nextToken());
				break;
			case 2:
				dyna_where = decode(st.nextToken());
				break;
			default:
				break;
			}
		}

		if (filename.startsWith("./http")){
			filename = filename.substring(filename.indexOf("./")+2,filename.length());
		}
		else if (!filename.startsWith("/")) {
			filename = queryBaseDir + "/" + filename;
		}

		long t = System.currentTimeMillis();
		SSQLhtmlfile = InvokeDir + "/Invoke" + t + ".html";
		
		InvokeServletPath = req.getRequestURL().toString();

	}

	public void unlinkTempFile() {
		File f = new File(SSQLhtmlfile);
		f.delete();
		return;
	}

	/**
	 *sSQLhtmlfile
	 */
	public String getSSQLhtmlfile() {
		return SSQLhtmlfile;
	}

	/**
	 * dbname
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * dyna_where
	 */
	public String getDyna_where() {
		return dyna_where;
	}

	/**
	 * filename
	 */
	public String getFilename() {
		return filename;
	}

	private String decode(String str) {

		String decode_str = new String();

		for (int i = 0; i < str.length(); i++) {
			if (str.startsWith("!61", i)) {
				decode_str += "=";
				i += 2;
			} else if (str.startsWith("!32", i)) {
				decode_str += " ";
				i += 2;
			} else if (str.startsWith("!33", i)) {
				decode_str += "!";
				i += 2;
			} else if (str.startsWith("!38", i)) {
				decode_str += "&";
				i += 2;
			} else if (str.startsWith("!43", i)) {
				decode_str += "+";
				i += 2;
			} else if (str.startsWith("!39", i)) {
				decode_str += "'";
				i += 2;
			} else if (str.startsWith("\\(", i)) {
				decode_str += "(";
				i += 2;
			} else if (str.startsWith("\\)", i)) {
				decode_str += ")";
				i += 2;
			} else {
				decode_str += str.substring(i, i + 1);
			}
		}
		return decode_str;
	}

	/**
	 *query string
	 */
	public String getQuerystring() {
		return querystring;
	}
	
	
	/**
	 * invokeServletPath
	 */
	public String getInvokeServletPath() {
		return InvokeServletPath;
	}
}