package supersql.codegenerator.X3D;

import supersql.codegenerator.Connector;
import supersql.codegenerator.Manager;
import supersql.extendclass.ExtList;

public class X3DC0 extends Connector{
	Manager manager;

	X3DEnv x3d_env;
	
	X3DValue value;

	public X3DC0(Manager manager, X3DEnv lenv) {
		this.manager = manager;
		this.x3d_env = lenv;
	}

       String xs, ys, zs;

       String bxs, bys, bzs;
       
       String vxs, vys, vzs;
	@Override
	public String work(ExtList data_info) {

		this.setDataList(data_info);
		while (this.hasMoreItems()) {
			this.worknextItem();
		}
		return null;

	}

	@Override
	public String getSymbol() {
		return "X3DC0";
	}

}
