package supersql.codegenerator.SWF;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class SWFC2 extends Connector implements SWFTFE{
	Manager manager;
	SWFEnv swf_env;
	SWFValue value;

	public SWFC2(Manager manager, SWFEnv swf_env) {
		this.manager = manager;
		this.swf_env = swf_env;
	}

	@Override
	public String work(ExtList data_info) {

		int width = 0;
		int height = 0;
		int tmp_height = 0;
		int tmp_width = 0;

		System.out.println("");
		System.out.println("------- C2 -------");
		System.out.println("[SWFC2:work]tfe_info = " + makele0());
		System.out.println("[SWFC2:work]data_info = " + data_info);

		swf_env.level++;

		value = new SWFValue("C2");

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

			if(myvalue.type.equals("Att")){
				value.inList.add(myvalue);
				if(swf_env.cthree_flag==0){
					value.inListC3.add(myvalue);
					value.inListC3Leaf.add(myvalue);
					swf_env.cthree_string++;
				}
			}
			else if(myvalue.type.equals("Func")){
				value.inList.add(myvalue);
				if(swf_env.cthree_flag==0){
					value.inListC3Img.add(myvalue);
					value.inListC3Leaf.add(myvalue);
					swf_env.cthree_func++;
				}
			}else{
				value.inList.add(myvalue);
				value.inListC3.add(myvalue);
				value.inListC3Img.add(myvalue);
				value.inListC3Leaf.add(myvalue);
			}

			height += swf_env.tmp_height;
			tmp_width = swf_env.tmp_width;
			if (tmp_width > width) {
				width = tmp_width;
			}

			i++;
		}//while

		value.width = width;
		value.height = height;

		swf_env.tmp_width = width;
		swf_env.tmp_height = height;


		if(dim4size!=0) value = C2to4(value,dim4size,opr);

		swf_env.level--;

		if(swf_env.level==0) {
			swf_env.final_value = value;
			swf_env.movieWidth = value.width+20;
//			swf_env.movieHeight = value.height+20;
			swf_env.movieHeight = value.height+40;
		}

		Log.out("value.inList.size()= " + value.inList.size());
		Log.out("C2: value.width = "+value.width);
		Log.out("C2: value.height = "+value.height);
		return null;
	}

	public SWFValue C2to4(SWFValue self, int dim4, String opr){
		SWFValue value4 = new SWFValue(opr);
		Log.out("to4");
		
		int maxHeight = 0;

		for(int j=0; j<dim4; j++){
			SWFValue valueC2 = new SWFValue("C2");
			int tmph = 0;
			for(int k=0; k<value.inList.size(); k++){
				SWFValue reset = (SWFValue)value.inList.get(k);
				if(reset.type.equals("C4")||reset.type.equals("G4")){
					SWFValue tmp = (SWFValue)reset.inList.get(j);

					valueC2.inList.add(tmp);
					valueC2.duration = tmp.duration;
				}else{

					valueC2.inList.add(reset);
				}
				tmph += reset.height;
			}
			valueC2.width = value.width;
			valueC2.height = tmph;
			valueC2.x = value.x;
			valueC2.y = value.y;
			value4.inList.add(valueC2);
			value4.duration = valueC2.duration;

			if(maxHeight<valueC2.height)
				maxHeight = valueC2.height;
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
		return "C2";
	}

}
