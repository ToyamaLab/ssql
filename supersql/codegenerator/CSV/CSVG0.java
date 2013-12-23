package supersql.codegenerator.CSV;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class CSVG0 extends Grouper {

    private CSVEnv csvEnv;
    public static String attribute;

    //コンストラクタ
    public CSVG0(Manager manager, CSVEnv xenv, CSVEnv xenv2) {
        this.csvEnv = xenv;
    }

    //G0のworkメソ�?��
    public String work(ExtList data_info) {
        Log.out("------- G0 -------");
        this.setDataList(data_info);

        while (this.hasMoreItems()) {
        	csvEnv.glevel++;

        	CSVEnv.getClassID(tfe);

        	this.worknextItem();

		    csvEnv.glevel--;
        }

        Log.out("TFEId = " + CSVEnv.getClassID(this));
		return null;
    }

    public String getSymbol() {
        return "CSVG0";
    }

}