package supersql.codegenerator.SWF;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class SWFG2 extends Grouper implements SWFTFE{
	Manager manager;
	SWFEnv swf_env;
	SWFValue value;

	int tmp_width;
	int tmp_height;

	public SWFG2(Manager manager, SWFEnv swf_env) {
		this.manager = manager;
		this.swf_env = swf_env;
	}

	@Override
	public void work(ExtList data_info) {

		int width = 0;
		int height = 0;
		String colors = "";
		String color1 = "";
		String color2 = "";
		int tcount = 0;
		int tinterval = 0;

		swf_env.level++;

		System.out.println("");
		System.out.println("------- G2 -------");
		System.out.println("[SWFG2:work]data_info = " + data_info);

		value = new SWFValue("G2");

		setDataList(data_info);

		int i = 0;
		int dim4size = 0;
		String opr = "";

		while (this.hasMoreItems()) {

			this.worknextItem();

			SWFValue myvalue = ((SWFTFE)tfe).getInstance();

			if(myvalue.type.equals("C4")||myvalue.type.equals("G4")){
				opr = myvalue.type;
				dim4size = myvalue.inList.size();

			}

			tmp_width = swf_env.tmp_width;
			if (width < tmp_width)
				width = tmp_width;
			height += swf_env.tmp_height;

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


			i++;
		}
		/*
		for(i=0; i<value.inList.size(); i++){
			SWFValue re_set = (SWFValue)value.inList.get(i);
			if(!re_set.type.equals("G1"))
				resize_width(re_set,width);
			re_set.width = width;
		}
		 */

		value.width = width;
		value.height = height;

		swf_env.tmp_width = width;
		swf_env.tmp_height = height;


		swf_env.getDecos(this.decos);
		if (this.decos.containsKey("bgcolors")){
			colors = decos.getStr("bgcolors");
			color1 = colors.substring(0,colors.indexOf("&"));
			color2 = colors.substring(colors.indexOf("&")+1,colors.length());
			Log.out("color1:"+color1);
			Log.out("color2:"+color2);
			swf_env.alternateColors(value,color1,color2);
		}


		if (this.decos.containsKey("tfold")){
			tcount = Integer.parseInt(decos.getStr("tfold"));
			tinterval = Integer.parseInt(decos.getStr("interval"));


			value = tfoldG2(value,tcount,tinterval);

		}

		Log.out("value.inList.size()= " + value.inList.size());
		Log.out("G2: value.width = "+value.width);
		Log.out("G2: value.height = "+value.height);


		if(dim4size!=0) value = G2to4(value,dim4size,opr);


		swf_env.level--;



		if(swf_env.level==0){
			swf_env.final_value = value;
			swf_env.movieWidth = value.width+20;
//			swf_env.movieHeight = value.height+20;
			swf_env.movieHeight = value.height+40;
		}
	}

	public SWFValue G2to4(SWFValue self, int dim4, String opr){
		SWFValue value4 = new SWFValue(opr);



		int maxHeight = 0;
		for(int j=0; j<dim4; j++){
			SWFValue valueG2 = new SWFValue("G2");
			int tmph = 0;
			for(int k=0; k<value.inList.size(); k++){
				SWFValue reset = (SWFValue)value.inList.get(k);

				if(reset.type.equals("C4")){
					SWFValue tmp = (SWFValue)reset.inList.get(j);

					valueG2.inList.add(tmp);
					valueG2.duration = tmp.duration;
				}else{

					valueG2.inList.add(reset);
				}
				tmph += reset.height;
			}
			valueG2.width = value.width;
			valueG2.height = tmph;
			valueG2.x = value.x;
			valueG2.y = value.y;
			value4.inList.add(valueG2);
			value4.duration = valueG2.duration;

			if(maxHeight<valueG2.height)
				maxHeight = valueG2.height;
		}
		value4.width = value.width;
		value4.height = maxHeight;
		value4.x = value.x;
		value4.y = value.y;

		return value4;
	}



	public SWFValue tfoldG2(SWFValue self, int count, int interval){
		SWFValue valueG4 = new SWFValue("G4");

		int scenes = self.inList.size()/count;
		if(self.inList.size()%count!=0) scenes += 1;



		int maxHeight=0;

		int j=0;
		for(int m=0; m<scenes; m++){
			SWFValue valueG2 = new SWFValue("G2");
			int tmph=0;
			int k=0;
			while(k<count && j<self.inList.size()){
				SWFValue reset = (SWFValue)value.inList.get(j);
				valueG2.inList.add(reset);
				tmph += reset.height;
				k++;
				j++;
			}
			valueG2.width = value.width;
			valueG2.height = tmph;
			valueG2.x = value.x;
			valueG2.y = value.y;
			valueG2.duration = interval;
			valueG4.inList.add(valueG2);

			if(maxHeight<valueG2.height)
				maxHeight = valueG2.height;
		}

		valueG4.width = value.width;
		valueG4.height = maxHeight;
		valueG4.x = value.x;
		valueG4.y = value.y;
		valueG4.duration = interval;

		return valueG4;
	}


	public void resize_width(SWFValue self, int max_width) {
		SWFValue child;
		String type = self.type;
		int j=0;
		boolean att=false;

		while(j<self.inList.size() && !att){
			int diff = max_width - self.width;
			child = (SWFValue)self.inList.get(j);

			Log.out("@@@@C2:child.type = " + child.type);
			if(child.type.equals("Att") || child.type.equals("Func")){
				Log.out("diff:"+diff);
				child.width +=diff;

				if(!(self.type.equals("G2")||self.type.equals("C2")))att = true;
				Log.out("-------------------------------------");
			}
			resize_width(child,max_width);
			j++;
		}
	}

	public SWFValue getInstance(){

		return value;
	}

	@Override
	public String getSymbol() {
		return "G2";
	}

}
