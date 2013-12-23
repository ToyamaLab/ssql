package supersql.codegenerator.X3D;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class X3DC2 extends Connector{
    Manager manager;

    X3DEnv x3d_env;
    
    X3DValue value;

    public X3DC2(Manager manager, X3DEnv lenv) {
        this.manager = manager;
        this.x3d_env = lenv;
    }
    
    @Override
	public String work(ExtList data_info) {
        Log.out("------- C2 -------");
        Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
        Log.out("tfessize=" + tfes.size());
        Log.out("countconnetitem=" + countconnectitem());

        this.setDataList(data_info);
        value = new X3DValue();
        
        int i = 0;
        while (this.hasMoreItems()) {
            ITFE tfe = (ITFE) tfes.get(i);
            
            this.worknextItem();

            if (X3DEnv.x3d_add == 1) {
            }
            else {
            	X3DEnv.ly[X3DEnv.lcount] = X3DEnv.ly[X3DEnv.lcount-1] + 15.5;
            	X3DEnv.lz[X3DEnv.lcount] = value.z_ini;
            	X3DEnv.x = value.x_ini;
            	X3DEnv.xi = X3DEnv.x;
            	X3DEnv.y += 15.5;
            	X3DEnv.z = value.z_ini;
            	X3DEnv.fwy += 15.5;
            	X3DEnv.bwy += 15.5;
            	X3DEnv.rwy += 15.5;
            	X3DEnv.lwy += 15.5;
            	X3DEnv.c2[X3DEnv.lcount] = 1;
            }
            i++;

        }
		return null;
    }

    @Override
	public String getSymbol() {
        return "X3DC2";
    }

}
