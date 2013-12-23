package supersql.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DB {
	static Connection con = null;
	static Statement stmt = null;
	static DatabaseMetaData dmd = null;
	
	// Get table names from query
    /* return: (0)=Table name, (1)=Table alias, (2)=From phrase */
	@SuppressWarnings({ "rawtypes", "serial" })
	public static ArrayList<ArrayList> getTableNamesFromQuery(String query){
  	  	String tableNames = "";
  	  	final ArrayList<String> tableName = new ArrayList<String>();
  	  	final ArrayList<String> tableAlias = new ArrayList<String>();
  	  	final ArrayList<String> fromPhrase = new ArrayList<String>();
  	  	try{
	    	  String q = query.toLowerCase();
	    	  q = q.substring(q.lastIndexOf("from")+4).replaceAll(";", "");
	    	  tableNames = q;
	    	  if(q.contains("where")){
	    		  tableNames = q.substring(0, q.lastIndexOf("where"));
	    	  }else if(q.contains("group")){
	    		  tableNames = q.substring(0, q.lastIndexOf("group"));
	    	  }else if(q.contains("order")){
	    		  tableNames = q.substring(0, q.lastIndexOf("order"));
	    	  }
	    	  fromPhrase.add(0,tableNames);
	    	  
	    	  int i=0;
	    	  tableNames += ",";
	    	  while(tableNames.contains(",")){
	    		  int index = tableNames.indexOf(",");
	    		  tableName.add(i, tableNames.substring(0,index).trim());
	    		  tableAlias.add(i, "");
	    		  String tn = tableName.get(i);
	    		  if(tn.contains(" ")){
	    			  tableName.set(i, tn.substring(0,tn.indexOf(" ")).trim());
	    			  tableAlias.set(i, tn.substring(tn.lastIndexOf(" ")).trim());
	    		  }
	    		  tableNames = tableNames.substring(index+1);
	    		  i++;
	    	  }
  	  	}catch(Exception e){}
  	  	return new ArrayList<ArrayList>() {{add(tableName); add(tableAlias); add(fromPhrase);}};
    }
	
	// Get all table and column names(.toLowerCase()) from DB
	public static Map<String,HashSet<String>> getTableAndColumnNamesMap() {
		Map<String,HashSet<String>> tables = new HashMap<String,HashSet<String>>();
		try {
			getDMD();
			ResultSet rs1 = null;
			ResultSet rs2 = null;
			String types[] = { "TABLE" };
			rs1 = dmd.getTables(null, null, "%", types);
			try {
				while (rs1.next()) {
					String tn = rs1.getString("TABLE_NAME").toLowerCase();
					rs2 = dmd.getColumns(null, null, tn, null);
					HashSet<String> columns = new HashSet<String>();
	    			try {
	    				while(rs2.next()){
	    					columns.add(rs2.getString("COLUMN_NAME").toLowerCase());
	    				}
	    			} finally {
	    				tables.put(tn, columns);
	    				rs2.close();
	    			}
				}
			} finally {
				rs1.close();
			}
			close();
		} catch (Exception e) {	}
		return tables;
	}
	
	private static void getDMD() {
		String user = GlobalEnv.getusername();
		String url = GlobalEnv.geturl();
		String password = GlobalEnv.getpassword();
		try {
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
			dmd = con.getMetaData();
		}catch (Exception e) {}
	}
	private static void close() {
		try{
			stmt.close();
			con.close();
		}catch (Exception e) {}
	}
}
