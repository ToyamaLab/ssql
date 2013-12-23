package supersql.codegenerator.SWF;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class SWFC3 extends Connector implements SWFTFE{
	Manager manager;

	SWFEnv swf_env;

	SWFValue value;

	public SWFC3(Manager manager, SWFEnv swf_env) {
		this.manager = manager;
		this.swf_env = swf_env;
	}

	@Override
	public String work(ExtList data_info) {
		int counter = 0;
		int width = 0;
		int height = 0;
		int tmp_height = 0;
		int tmp_width = 0;

		System.out.println("");
		System.out.println("------- C3 -------");
		System.out.println("[SWFC3:work]tfe_info = " + makele0());
		System.out.println("[SWFC3:work]data_info = " + data_info);

		//morya wrote
		swf_env.visibleflag_counter++;
		Log.out("monyaa "+swf_env.visibleflag_counter);

		swf_env.level++;


		value = new SWFValue("C3");

		setDataList(data_info);

		int i=0;
		int dim4size = 0;
		String opr = "";

		while (this.hasMoreItems()) {
			ITFE tfe = (ITFE) tfes.get(i);


			this.worknextItem();

			SWFValue myvalue = ((SWFTFE)tfe).getInstance();
			Log.out("monya"+myvalue.data+myvalue.type+swf_env.cthree_flag);

			if(myvalue.type.equals("C4")||myvalue.type.equals("G4")){
				opr = myvalue.type;
				dim4size = myvalue.inList.size();
			}

			counter++;
			if(counter==1){
				swf_env.cthree_flag = 0;
				myvalue.cthree=0;
				value.inList.add(myvalue);

				Log.out("int_config visibleflag"+swf_env.visibleflag_counter+" x"+myvalue.x+" y"+myvalue.y+" w"+myvalue.width+" h"+myvalue.height+" lstop"+myvalue.lstop);
				SWFIntConfig int_config = new SWFIntConfig();
				swf_env.int_config[swf_env.visibleflag_counter-1][0] = swf_env.visibleflag_counter;
				swf_env.int_config[swf_env.visibleflag_counter-1][1] = myvalue.x;
				swf_env.int_config[swf_env.visibleflag_counter-1][2] = myvalue.y;
				swf_env.int_config[swf_env.visibleflag_counter-1][3] = myvalue.width;
				swf_env.int_config[swf_env.visibleflag_counter-1][4] = myvalue.height;
				swf_env.int_config[swf_env.visibleflag_counter-1][5] = myvalue.lstop;
			}else if(counter==2){
				swf_env.cthree_flag = 1;
				myvalue.cthree=1;
				if(myvalue.type.equals("Func")){
					value.inListC3Img.add(myvalue);
					value.inListC3Leaf.add(myvalue);
					swf_env.cthree_func++;
				}else if(myvalue.type.equals("Att")){
					value.inListC3.add(myvalue);
					value.inListC3Leaf.add(myvalue);
					swf_env.cthree_string++;
				}else{
					value.inListC3.add(myvalue);
					value.inListC3Leaf.add(myvalue);
					value.inListC3Img.add(myvalue);
				}
			}

			if(counter==1){
				width = swf_env.tmp_width;
				height = swf_env.tmp_height;
			}
			//tmp_height = swf_env.tmp_height;
			//tmp_width = swf_env.tmp_width;
			/*if (tmp_width > width) {
				width = tmp_width;
			}*/

			i++;
		}//while

		swf_env.cthree_flag = -1;

		value.width = width;
		value.height = height;

		swf_env.tmp_width = width;
		swf_env.tmp_height = height;

		if(dim4size!=0) value = C3to4(value,dim4size,opr);

		swf_env.level--;

		if(swf_env.level==0) {
			swf_env.final_value = value;
			swf_env.movieWidth = value.width+20;
//			swf_env.movieHeight = value.height+20;
			swf_env.movieHeight = value.height+40;
		}

		Log.out("value.inList.size()= " + value.inList.size());
		Log.out("C3: value.width = "+value.width);
		Log.out("C3: value.height = "+value.height);
		return null;
	}
	public SWFValue C3to4(SWFValue self, int dim4, String opr){
		SWFValue value4 = new SWFValue(opr);
		Log.out("to4");
		int maxHeight = 0;

		for(int j=0; j<dim4; j++){
			SWFValue valueC3 = new SWFValue("C3");
			int tmph = 0;
			for(int k=0; k<value.inList.size(); k++){
				SWFValue reset = (SWFValue)value.inList.get(k);

				if(reset.type.equals("C4")||reset.type.equals("G4")){
					SWFValue tmp = (SWFValue)reset.inList.get(j);

					valueC3.inList.add(tmp);
					valueC3.duration = tmp.duration;
				}else{

					valueC3.inList.add(reset);
				}
				tmph += reset.height;
			}
			valueC3.width = value.width;
			valueC3.height = tmph;
			valueC3.x = value.x;
			valueC3.y = value.y;
			value4.inList.add(valueC3);
			value4.duration = valueC3.duration;

			if(maxHeight<valueC3.height)
				maxHeight = valueC3.height;
		}
		value4.width = value.width;
		value4.height = maxHeight;
		value4.x = value.x;
		value4.y = value.y;

		return value4;
	}

	public SWFValue getInstance(){

		return value;
	}

	@Override
	public String getSymbol() {
		return "C3";
	}

}
