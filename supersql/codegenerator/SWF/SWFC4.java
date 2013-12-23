package supersql.codegenerator.SWF;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class SWFC4 extends Connector implements SWFTFE{
	Manager manager;
	SWFEnv swf_env;	
	SWFValue value;

	int def_x;
	int def_y;


	public SWFC4(Manager manager, SWFEnv swf_env) {
		this.manager = manager;
		this.swf_env = swf_env;
	}

	@Override
	public String work(ExtList data_info) {

		int width = 0;
		int height = 0;
		int tmp_height = 0;		
		int tmp_width = 0;

		swf_env.level++;

		System.out.println("------- C4 -------");
		System.out.println("[SWFC4:work]tfe_info = " + makele0());
		System.out.println("[SWFC4:work]data_info = " + data_info);

		value = new SWFValue("C4");    

		this.setDataList(data_info);

		int i = 0;
		int dx = 0;
		int dy = 0;

		while (this.hasMoreItems()) {
			ITFE tfe = (ITFE) tfes.get(i);

			//int tmp = 0;
			def_x = swf_env.def_x;
			def_y = swf_env.def_y;


			this.worknextItem();


			SWFValue myvalue = ((SWFTFE)tfe).getInstance();	

			System.out.println("[SWFC4:work]++++ C4��Att��set���ޤ�");
			value.inList.add(myvalue);

			tmp_height = swf_env.tmp_height;
			tmp_width = swf_env.tmp_width;
			if (tmp_width > width) {
				width = tmp_width;
			}
			if (tmp_height > height){
				height = tmp_height;
			}

			i++;
		} //while



		value.width = width;
		value.height = height;

		swf_env.tmp_width = width;
		swf_env.tmp_height = height;


		swf_env.level--;


		if(swf_env.level==0) swf_env.final_value = value;

		Log.out("value.inList.size()= " + value.inList.size());		
		Log.out("C4: value.width = "+value.width);
		Log.out("C4: value.height = "+value.height);
		return null;

	}


	public SWFValue getInstance(){

		return value;
	}

	@Override
	public String getSymbol() {
		return "C4";
	}

}
