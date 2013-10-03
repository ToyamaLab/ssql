package supersql.codegenerator.SWF;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class SWFC1 extends Connector implements SWFTFE{
	Manager manager;

	SWFEnv swf_env;

	SWFValue value;

	public SWFC1(Manager manager, SWFEnv swf_env) {
		this.manager = manager;
		this.swf_env = swf_env;
	}

	@Override
	public void work(ExtList data_info) {

		int width = 0;
		int height = 0;
		int tmp_height = 0;
		int tmp_width = 0;

		swf_env.level++;

		System.out.println("");
		System.out.println("------- C1 -------");
		System.out.println("[SWFC1:work]data_info = " + data_info);

		System.out.println("++++ C1 +++++");
		value = new SWFValue("C1");

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
				Log.out(""+opr+""+dim4size+"");
			}

			System.out.println("++++ C1");
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

			Log.out("ListC3Imgiti"+value.inListC3Img.size());

			width += swf_env.tmp_width;
			tmp_height = swf_env.tmp_height;
			if (tmp_height > height) {
				height = tmp_height;
			}

			i++;
		}

		value.width = width;
		value.height = height;

		swf_env.tmp_width = width;
		swf_env.tmp_height = height;

		if(dim4size!=0) value = C1to4(value,dim4size,opr);



		swf_env.level--;

		if(swf_env.level==0) {
			swf_env.final_value = value;
			swf_env.movieWidth = value.width+20;
			swf_env.movieHeight = value.height+20;
		}

		Log.out("value.inList.size()= " + value.inList.size());
		Log.out("C1: value.width = "+value.width);
		Log.out("C1: value.height = "+value.height);
	}

	public SWFValue C1to4(SWFValue self, int dim4, String opr){
		SWFValue value4 = new SWFValue(opr);
		int maxWidth=0;
		Log.out("to4");

		for(int j=0; j<dim4; j++){
			SWFValue valueC1 = new SWFValue("C1");
			int tmpw = 0;
			for(int k=0; k<value.inList.size(); k++){
				SWFValue reset = (SWFValue)value.inList.get(k);
				Log.out(""+reset.type+"");
				if(reset.type.equals("C4")||reset.type.equals("G4")){
					SWFValue tmp = (SWFValue)reset.inList.get(j);
					Log.out(""+tmp.data+"");
					valueC1.inList.add(tmp);
					valueC1.duration = tmp.duration;
				}else{
					Log.out(""+reset.data+"");
					valueC1.inList.add(reset);
				}
				tmpw += reset.width;
			}
			valueC1.width = tmpw;
			valueC1.height = value.height;
			valueC1.x = value.x;
			valueC1.y = value.y;
			value4.inList.add(valueC1);
			value4.duration = valueC1.duration;

			if(maxWidth<valueC1.width)
				maxWidth = valueC1.width;
		}
		value4.width = maxWidth;
		value4.height = value.height;
		value4.x = value.x;
		value4.y = value.y;

		return value4;
	}

	public SWFValue getInstance(){
		System.out.println("++++ C1");
		return value;
	}

	@Override
	public String getSymbol() {
		return "C1";
	}

}
