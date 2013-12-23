package supersql.codegenerator.X3D;

import supersql.codegenerator.Function;
import supersql.codegenerator.Manager;
import supersql.extendclass.ExtList;

public class X3DFunction extends Function{
	   Manager manager;

	    X3DEnv x3d_env;

	    public X3DFunction(Manager manager, X3DEnv lenv) {
	        super();
	        this.manager = manager;
	        this.x3d_env = lenv;
	    }

	    @Override
		public String work(ExtList data_info) {
			return null;
	    }

}
