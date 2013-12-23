package supersql.codegenerator.CSV;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class CSVC0 extends Connector {

    private CSVEnv csvEnv;
    public CSVC0(Manager manager, CSVEnv xenv, CSVEnv xenv2) {
        this.csvEnv = xenv;
    }

    public String work(ExtList data_info) {

    	Log.out("------- C0 -------");
        Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
        Log.out("tfes.size=" + tfes.size());
        Log.out("countconnetitem=" + countconnectitem());

        this.setDataList(data_info);

        int i = 0;

	   	 while(this.hasMoreItems()) {

		    ITFE tfe = (ITFE)tfes.get(i);

		    CSVEnv.getClassID(tfe);

		    this.worknextItem();
		}

	     csvEnv.code.append("\n");
	   	 Log.out("TFEId = " + CSVEnv.getClassID(this));
		return null;
	}

    public String getSymbol() {
        return "CSVC0";
    }

}