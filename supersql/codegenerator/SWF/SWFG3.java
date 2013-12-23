package supersql.codegenerator.SWF;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.extendclass.ExtList;

public class SWFG3 extends Grouper implements SWFTFE{
	Manager manager;

	SWFEnv swf_env;
	
	SWFValue value;
	
	public SWFG3(Manager manager, SWFEnv swf_env) {
		this.manager = manager;
		this.swf_env = swf_env;
	}
	
	@Override
	public String work(ExtList data_info) {
		return null;
	}
	
	public SWFValue getInstance(){
		return this.value;
	}
	
	@Override
	public String getSymbol() {
		return "G3";
	}
	
}
