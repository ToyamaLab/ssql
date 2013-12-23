package supersql.db;

import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class GetFromDB {

	private SQLManager sqlm;

    public GetFromDB() {
        Log.out("[GetFromDB]");

        String hostname = GlobalEnv.gethost();
        String dbname = GlobalEnv.getdbname();
        String user = GlobalEnv.getusername();
        String driver = GlobalEnv.getDriver();
        String dbms = GlobalEnv.getdbms();
        String url = GlobalEnv.geturl();
        String password = GlobalEnv.getpassword();
        

        Log.out("[hostname : " + hostname + "]");
        Log.out("[dbname   : " + dbname + "]");
        Log.out("[user : " + user + "]");
        Log.out("[driver : " + driver + "]");
        Log.out("[dbms : " + dbms + "]");
        Log.out("[password: " + password + "]");

        sqlm = new SQLManager(url, user, driver, password);

    }

    public GetFromDB(ConnectDB cdb) {
        sqlm = new SQLManager(cdb);

    }

    public void execQuery(String query, ExtList ResultData) {

        ResultData.clear();

        if (GlobalEnv.getframeworklist() != null) {
            Log.out("## From framework DB list ##");
            Log.out(GlobalEnv.getframeworklist());
            sqlm.ExecListToResult(GlobalEnv.getframeworklist(),query);
        	ResultData.addAll(sqlm.GetBody());
        }else{
        	sqlm.ExecSQL(query);
        	ResultData.addAll(sqlm.GetBody());
        }
        return;

    }

    public void close() {
        sqlm.close();
        return;
    }

}