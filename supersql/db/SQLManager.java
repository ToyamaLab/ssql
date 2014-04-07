package supersql.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5;
import supersql.common.DB;

import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.common.Suggest;
import supersql.extendclass.ExtList;
import supersql.parser.FromParse;

public class SQLManager {

	private Connection conn;
	private ExtList<String> header_name;
	private ExtList<String> header_type;
	private ExtList<ExtList<String>> tuples;

	private ConnectDB cdb;
	private boolean isMulti = false;

    public SQLManager(ConnectDB in_cdb)
    {
    	cdb = in_cdb;
    	isMulti = true;
    }

    public SQLManager(String url, String user, String driver, String password) {
        Log.out("[SQLManager Open]");
        try {
        	if (GlobalEnv.getframeworklist() == null) {
	            Class.forName(driver);
	            Log.out("********** Database's URL is **********");
	            Log.out(url);
	            conn = DriverManager.getConnection(url, user, password);
        	}

        } catch (SQLException e) {
            System.err
                    .println("Error[SQLManager]: Can't Connect DB : jdbc path = "
                            + url + " , user = " + user);
            System.err.println(e);
            GlobalEnv.addErr("Error[SQLManager]: Can't Connect DB : jdbc path = "
                    + url + " , user = " + user);
            return ;
        } catch (ClassNotFoundException e) {
            System.err
                    .println("Error[SQLManager]: Can't Load JDBC driver : driver = "
                            + driver);
            GlobalEnv.addErr("Error[SQLManager]: Can't Load JDBC driver : driver = "
                            + driver);
            return ;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void ExecSQL(String query) {
    	if(isMulti)
    	{
    		Log.out("thred name:"+cdb.getName());
    		Log.out("isAlive?"+cdb.isAlive());
    		try{
    			cdb.join();
	    	}catch(InterruptedException e)
	    	{}

	    	conn = cdb.getConn();
    	}

    	if(query.contains(" #"))	query = query.substring(0,query.indexOf(" #"));	//TODO
    	query = Mobile_HTML5.checkQuery(query);
        Log.out("[SQLManager ExecQuery]");
        if(!query.equals("SELECT DISTINCT  FROM ;") && !query.equals("SELECT  FROM ;")){
	        Log.info("\n********** SQL is **********");
	        Log.info(query);
        }
        GlobalEnv.query = query;

        header_name = new ExtList<String>();
        header_type = new ExtList<String>();
        tuples = new ExtList<ExtList<String>>();

        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                header_name.add(rsmd.getColumnName(i));
                header_type.add(Integer.toString(rsmd
                        .getColumnType(i)));
            }

            ExtList<String> tmplist;
            String val;
            while (rs.next()) {
                tmplist = new ExtList<String>();
                for (int i = 1; i <= columnCount; i++) {
                    val = rs.getString(i);
                    if (val != null) {
                        tmplist.add(val.trim());
                    } else {
                        tmplist.add("");
                        Log.out("[Warning] null value exist!");
                    }
                }
                tuples.add(tmplist);
            }
            Log.out("[SQLManager:execQuerySQL] Result tuples count = "
                    + tuples.size());
            GlobalEnv.setTuplesNum(tuples.size());

            if (tuples.size() == 0) {
                tmplist = new ExtList<String>();
            	for (int i = 1; i <= columnCount; i++) {
                    tmplist.add("");
                    Log.out("[Warning] null value exist!");
                }
                tuples.add(tmplist);
                GlobalEnv.setTuplesNum(tuples.size());
                throw (new IllegalStateException());
            }

        } catch (SQLException e) {
        	if(!query.equals("SELECT DISTINCT  FROM ;") && !query.equals("SELECT  FROM ;")){
	              System.err
	              .println("Error[SQLManager.ExecSQL]: Can't Exec Query : query = "
			                      + query);
			      System.err.println(e);
			      GlobalEnv.addErr("Error[SQLManager.ExecSQL]: Can't Exec Query : query = "
			              + query);
			      
			      //added by goto 20131016 start
			      String list = "";
			      String errorContents[] = Suggest.getErrorContents(e);  //return: [0]=Error message, [1]=Error table name or column name, [2]=Error table alias
		    	  ArrayList<ArrayList> tableNameAndAlias = DB.getTableNamesFromQuery(query);	//return: (0)=Table name, (1)=Table alias, (2)=From phrase
		    	  
			      String driver = GlobalEnv.getDriver().toLowerCase();
		    	  if( (driver.contains("postgresql") && (errorContents[0].contains("column") || errorContents[0].contains("table")) ||
		    		  //(driver.contains("mysql") ) ||	//TODO
		    		  //(driver.contains("db2") ) ||	//TODO
		    		  (driver.contains("sqlite") && errorContents[0].contains("column")))
		    		){
			    	  if(errorContents[0].contains("ambiguous")){
			    		  list = Suggest.getgetAmbiguousTableAndColumnNameList(conn, tableNameAndAlias.get(0), errorContents[1]);
			    	  }else{
				    	  list = Suggest.getTableAndColumnNameList(conn, tableNameAndAlias.get(0), tableNameAndAlias.get(1), errorContents[1], errorContents[2], tableNameAndAlias.get(2));
			    	  }
			    	  if(!list.isEmpty())	Log.err("\n## Column list ##\n" + list);
			    	  
			      }
		    	  else if( (driver.contains("postgresql") && (errorContents[0].contains("relation"))) ||
		    			   //(driver.contains("mysql") ) ||	//TODO
		    			   //(driver.contains("db2") ) ||	//TODO
		    			   (driver.contains("sqlite") && errorContents[0].contains("table"))
		    			 ){
			    	  list = Suggest.getTableNameList(conn, errorContents[1]);
			    	  Log.err("\n## Table list ##\n" + list + "\n");
			      }
			      //added by goto 20131016 end

			      return ;
        	}
        } catch (IllegalStateException e) {
            System.err
                    .println("Error[SQLManager.ExecSQL]: No Data Found : query = "
                            + query);
        }
    }


    //morya start
    public void ExecListToResult(String listarg, String query) {

        Log.out("[SQLManager ExecListToResult]");
        Log.info("********** Query For Framework **********");
        query = query.replace("SELECT DISTINCT", "");
        query = query.replace(query.substring(query.indexOf("FROM"),query.length()),"").trim();
        Log.info(query);

        header_name = new ExtList<String>();
        header_type = new ExtList<String>();
        tuples = new ExtList<ExtList<String>>();

        //necessary variable for framework
        List <String> listcol = new ArrayList<String>();
        List <List<String>> listdb = new ArrayList<List<String>>();
        List <Integer> num_from_left = new ArrayList<Integer>();
        int listdb_column_num=0;

        int fromchnum = 0, tochnum=0;
        int flag=0;
        listdb_column_num++;
        // \t divide turn  **1turn=attribute get
        while(listarg.indexOf("\t",fromchnum) != -1){
        	tochnum = listarg.indexOf("\t",fromchnum);
        	String tmpstr = listarg.substring(fromchnum,tochnum);
        	int fromchnum2 = 0, tochnum2 = 0;
        	while(tmpstr.indexOf(",",fromchnum2) != -1){
        		tochnum2 = tmpstr.indexOf(",",fromchnum2);
        		listcol.add(tmpstr.substring(fromchnum2,tochnum2));
        		fromchnum2 = tochnum2 + 1;
        		if(flag==0) listdb_column_num++;
        	}
        	tochnum2 = tmpstr.length();
            listcol.add(tmpstr.substring(fromchnum2,tochnum2));
            flag++;

        	listdb.add(listcol);
        	listcol = new ArrayList<String>();

        	fromchnum = tochnum + 1; //need +1
        }
        tochnum = listarg.length();
        String tmpstr = listarg.substring(fromchnum,tochnum);
    	int fromchnum2 = 0, tochnum2 = 0;
    	while(tmpstr.indexOf(",",fromchnum2) != -1){
    		tochnum2 = tmpstr.indexOf(",",fromchnum2);
    		listcol.add(tmpstr.substring(fromchnum2,tochnum2));
    		fromchnum2 = tochnum2 + 1;
    	}
    	tochnum2 = tmpstr.length();
        listcol.add(tmpstr.substring(fromchnum2,tochnum2));
    	listdb.add(listcol);

        //query process
        fromchnum = 0; tochnum=0;
        while(query.indexOf(",",fromchnum) != -1){
        	tochnum = query.indexOf(",",fromchnum);
        	header_name.add(query.substring(fromchnum,tochnum).trim());
        	header_type.add("4");
        	for(int i=0;i<listdb_column_num;i++){
        		if(query.substring(fromchnum,tochnum).trim().equals(listdb.get(0).get(i))){
        			num_from_left.add(i);
        		}
        	}
        	fromchnum = tochnum + 1;
        }
        tochnum = query.length();
        header_name.add(query.substring(fromchnum,tochnum).trim());
        header_type.add("4");
        for(int i=0;i<listdb_column_num;i++){
    		if(query.substring(fromchnum,tochnum).trim().equals(listdb.get(0).get(i))){
    			num_from_left.add(i);
    		}
    	}

        //get record turn
        ExtList<String> tmplist;
        String val;
        for (int i=1;i<listdb.size();i++) {//from 1 roop
            tmplist = new ExtList<String>();
            for (int j=0;j<num_from_left.size();j++) {
            	val = (String)listdb.get(i).get(num_from_left.get(j));
                if (val != null) {
                    tmplist.add(val);
                } else {
                    tmplist.add("");
                    Log.out("[Warning] null value exist!");
                }
            }
            tuples.add(tmplist);

        }
        Log.out("[SQLManager:ExecListToResult] Result tuples count = "
                + tuples.size());

        Log.out("   "+tuples+"  ");
    }

    public ExtList<ExtList<String>> GetBody() {
        return tuples;
    }

    public void close() {
    	if (GlobalEnv.getframeworklist() == null) {
	        try {
	            if (!conn.isClosed()) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            System.err.println("Error[SQLManager]: Can't Close DB :");
	            GlobalEnv.addErr("Error[SQLManager]: Can't Close DB :");
	            return ;
	        }
    	}

    }

    public String getAttList(String from_string, String connector, String att){
    	String columnList = new String();
    	try{
    		Statement stmt = conn.createStatement();
    		String[] spritFrom = from_string.split(",");
    		if(connector == null || connector.isEmpty()){
    			connector = ",";
    		}
    		for(int fromnum = 0; fromnum < spritFrom.length ;fromnum++){
    			FromParse fp = new FromParse(spritFrom[fromnum].trim());
    			if(att.equals( fp.getAlias() + "." + "*") || att.equals("*")){
    				String sql = "SELECT " + att + " FROM " + fp.getRealName() + " " + fp.getAlias() +" WHERE false;";
    				ResultSet rs = stmt.executeQuery(sql);
    				ResultSetMetaData metaData = rs.getMetaData();
    				int columnCount = metaData.getColumnCount();
    				for(int i = 1 ; i <= columnCount;i++){
    					columnList += fp.getAlias() + "." + metaData.getColumnName(i) + connector;
    				}
    			}
    		}
    		columnList = columnList.substring(0,columnList.length()-1);
    	}catch(Exception e){
    		System.err.println(e);
    	}
    	return columnList;
    }

}