package supersql.codegenerator.X3D;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.extendclass.ExtList;


public class X3DC3 extends Connector{
	   Manager manager;

	    X3DEnv x3d_env;
	    
	    X3DValue value;

	    public X3DC3(Manager manager, X3DEnv lenv) {
	        this.manager = manager;
	        this.x3d_env = lenv;
	    }

	    @Override
		public String work(ExtList data_info) {
	        int i = 0;
	        this.setDataList(data_info);
	        value = new X3DValue();
	        
	        while (this.hasMoreItems()) {
	            ITFE tfe = (ITFE) tfes.get(i);
	            
	            this.worknextItem();

	            if (X3DEnv.x3d_add == 1) {
	            }
	            else {
	            	X3DEnv.z = X3DEnv.bwz - 13.5;
	            	X3DEnv.zi = X3DEnv.z + 4.0;
	            	X3DEnv.x = value.x_ini;
	            	X3DEnv.xi = X3DEnv.x;
	            	X3DEnv.ly[X3DEnv.lcount] = X3DEnv.ly[X3DEnv.lcount-1];
	            	X3DEnv.c3[X3DEnv.lcount] = 1;
	            }
	            i++;

	        }
			return null;
	    }

	    @Override
		public String getSymbol() {
	        return "X3DC3";
	    }	
}
