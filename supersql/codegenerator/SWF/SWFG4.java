package supersql.codegenerator.SWF;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class SWFG4 extends Grouper implements SWFTFE{
	Manager manager;
	SWFEnv swf_env;
	SWFValue value;

	int def_x;
	int def_y;

	StringBuffer imgpath = new StringBuffer();
	StringBuffer name = new StringBuffer();
	StringBuffer data = new StringBuffer();
	StringBuffer x = new StringBuffer();
	StringBuffer y = new StringBuffer();
	StringBuffer w = new StringBuffer();
	StringBuffer h = new StringBuffer();
	StringBuffer bgcolor = new StringBuffer();
	StringBuffer color = new StringBuffer();
	StringBuffer align = new StringBuffer();
	StringBuffer size = new StringBuffer();
	StringBuffer margin = new StringBuffer();

	public SWFG4(Manager manager, SWFEnv swf_env) {
		this.manager = manager;
		this.swf_env = swf_env;
	}

	public void work(ExtList data_info) {

		System.out.println("------- G4 -------");
		System.out.println("[SWFG4:work]data_info = " + data_info);
		value = new SWFValue("G4");

		this.setDataList(data_info);

		int i = 0;
		int dx = 0;
		int dy = 0;

		swf_env.level++;

		while (this.hasMoreItems()) {
			def_x = swf_env.def_x;
			def_y = swf_env.def_y;

			this.worknextItem();
			swf_env.visibleflag_count = swf_env.visibleflag_counter;
			swf_env.visibleflag_counter = 0;
			swf_env.interactionNUM = 0;
			swf_env.interactionImgNUM = 0;

			SWFValue myvalue = ((SWFTFE)tfe).getInstance();


			if(myvalue.type.equals("C4")||myvalue.type.equals("G4")){

				for(int k=0; k<myvalue.inList.size(); k++){
					value.inList.add(((SWFValue)myvalue.inList.get(k)));
				}
			}else{
				value.inList.add(myvalue);
			}
			if(swf_env.movieWidth < myvalue.width+20) swf_env.movieWidth = myvalue.width+20;
//			if(swf_env.movieHeight < myvalue.height+20) swf_env.movieHeight = myvalue.height+20;
			if(swf_env.movieHeight < myvalue.height+40) swf_env.movieHeight = myvalue.height+40;

			i++;
		} //while

		/*
		for(i=0; i<value.inList.size(); i++){
			initArray();

			SWFValue pos = (SWFValue)value.inList.get(i);


			resize_cell(pos);


			set_position(pos,dx,dy);


			if(pos.type.equals("Att") || pos.type.equals("Func")){
				makeArray1(pos);
			}else{
				makeArray2(pos);
			}
			writeArray();


			writeDispFunc();
		}
		 */


		swf_env.getDecos(this.decos);


		swf_env.level--;



		if(swf_env.level==0) swf_env.final_value = value;
	}


//	private void initArray() {
//		imgpath = new StringBuffer();
//		name = new StringBuffer();
//		x = new StringBuffer();
//		y = new StringBuffer();
//		w = new StringBuffer();
//		h = new StringBuffer();
//		bgcolor = new StringBuffer();
//		color = new StringBuffer();
//		size = new StringBuffer();
//		align = new StringBuffer();
//		margin = new StringBuffer();
//		data = new StringBuffer();
//
//		imgpath.append("$imgpath = array(");
//		name.append("$name = array(");
//		x.append("$x = array(");
//		y.append("$y = array(");
//		w.append("$w = array(");
//		h.append("$h = array(");
//		bgcolor.append("$bgcolor = array(");
//		color.append("$color = array(");
//		size.append("$size = array(");
//		align.append("$align = array(");
//		margin.append("$margin = array(");
//		data.append("$str = array(");
//	}


	public void makeArray2(SWFValue parent) {
		SWFValue child;
		String type = parent.type;

		Log.out("** type="+type);
		Log.out("** parent.inList.size="+parent.inList.size());


		for(int j=0;j<parent.inList.size();j++){
			child = (SWFValue)parent.inList.get(j);

			Log.out("@@@@G4:child.type = " + child.type);
			if(child.type.equals("Att") || child.type.equals("Func")){

				imgpath.append("\"").append(child.imgpath).append("\",");
				Log.out("G4:value.imgpath = "+child.imgpath);

				name.append("\"").append(child.instanceName).append("\",");
				Log.out("G4:value.name = "+child.instanceName);

				data.append("\"").append(child.data).append("\",");
				System.out.println("G4:value.data = "+child.data);

				x.append(Float.toString(child.x)).append(",");
				System.out.println("G4:value.x = "+child.x);

				y.append(Float.toString(child.y)).append(",");
				System.out.println("G4:value.y = "+child.y);

				w.append(Float.toString(child.width)).append(",");
				Log.out("G4:value.w = "+child.width);

				h.append(Float.toString(child.height)).append(",");
				Log.out("G4:value.h = "+child.height);

				bgcolor.append("\"").append(child.bgcolor).append("\",");
				Log.out("G4:value.bgcolor = "+child.bgcolor);

				color.append("\"").append(child.fontcolor).append("\",");
				Log.out("G4:value.color = "+child.fontcolor);

				size.append(Integer.toString(child.fontsize)).append(",");
				Log.out("G4:value.size = "+child.fontsize);

				align.append("\"").append(child.align).append("\",");
				Log.out("G4:value.align = "+child.align);

				margin.append(Float.toString(child.margin)).append(",");
				Log.out("G4:value.margin = "+child.margin);
				Log.out("-------------------------------------");
			}
			makeArray2(child);
		}

	}

	public void makeArray1(SWFValue parent){

		imgpath.append("\"").append(parent.imgpath).append("\",");
		Log.out("G4:value.imgpath = "+parent.imgpath);

		name.append("\"").append(parent.instanceName).append("\",");
		Log.out("G4:value.name = "+parent.instanceName);

		data.append("\"").append(parent.data).append("\",");
		System.out.println("G4:value.data = "+parent.data);

		x.append(Float.toString(parent.x)).append(",");
		System.out.println("G4:value.x = "+parent.x);

		y.append(Float.toString(parent.y)).append(",");
		System.out.println("G4:value.y = "+parent.y);

		w.append(Float.toString(parent.width)).append(",");
		Log.out("G4:value.w = "+parent.width);

		h.append(Float.toString(parent.height)).append(",");
		Log.out("G4:value.h = "+parent.height);

		bgcolor.append("\"").append(parent.bgcolor).append("\",");
		Log.out("G4:value.bgcolor = "+parent.bgcolor);

		color.append("\"").append(parent.fontcolor).append("\",");
		Log.out("G4:value.color = "+parent.fontcolor);

		size.append(Integer.toString(parent.fontsize)).append(",");
		Log.out("G4:value.size = "+parent.fontsize);

		align.append("\"").append(parent.align).append("\",");
		Log.out("G4:value.align = "+parent.align);

		margin.append(Float.toString(parent.margin)).append(",");
		Log.out("G4:value.margin = "+parent.margin);
		Log.out("-------------------------------------");

	}

	public void resize_cell (SWFValue self){
		SWFValue child;
		int j=0;
		int num_of_child = self.inList.size();
		int w_tmp = 0;
		int h_tmp = 0;
		while(j<num_of_child){
			child = (SWFValue)self.inList.get(j);

			Log.out("+++++++++++child.type:"+child.type);
			Log.out("+++++++++++child.data:"+child.data);


			if(self.type.equals("G1")||self.type.equals("C1")){

				child.height = self.height;

				if(j==num_of_child-1){

					child.width = self.width - w_tmp;
					Log.out("child.width:" +child.width);
				}
				else{
					w_tmp += child.width;
				}
				Log.out(child.data+"(w,h)=("+child.width+","+child.height+")");
			}

			else if(self.type.equals("G2")||self.type.equals("C2")){
				child.width = self.width;

				if(j==num_of_child-1){

					child.height = self.height - h_tmp;
					Log.out("child.height:" +child.height);
				}
				else{

					h_tmp += child.height;
				}
				Log.out(child.data+"(w,h)=("+child.width+","+child.height+")");
			}

			if(!(child.type.equals("Att")||child.type.equals("Func")))


			resize_cell(child);

			j++;

		}
	}

	public void set_position (SWFValue self, int dx, int dy){
		SWFValue child;
		int j=0;
		while(j<self.inList.size()){
			child = (SWFValue)self.inList.get(j);
			int dx_tmp = 0;
			int dy_tmp = 0;

			Log.out(">>>>>>>>>>>child.type:"+child.type);
			Log.out(">>>>>>>>>>>child.data:"+child.data);

			if(self.type.equals("G1")||self.type.equals("C1")){

				child.x = self.x + dx;
				child.y = self.y;

				Log.out(child.data+"(x,y)=("+child.x+","+child.y+")");

				self.x = child.x;
				self.y = child.y;

				if(child.type.equals("C1")||child.type.equals("G1")){
					dx_tmp = 0;
				}
				dx = child.width;
				dy = 0;
				}
			else if(self.type.equals("G2")||self.type.equals("C2")){

				child.y = self.y + dy;
				child.x = self.x;

				Log.out(child.data+"(x,y)=("+child.x+","+child.y+")");

				self.x = child.x;
				self.y = child.y;

				if(child.type.equals("C2")||child.type.equals("G2")){
					dy_tmp = 0;
				}
				dy = child.height;
				dx = 0;
			}

			if(!(child.type.equals("Att")||child.type.equals("Func")))


			set_position(child,dx_tmp,dy_tmp);

			j++;
		}

	}

	public void writeArray(){

		imgpath.deleteCharAt(imgpath.length()-1).append(");\n");
		name.deleteCharAt(name.length()-1).append(");\n");
		data.deleteCharAt(data.length()-1).append(");\n");
		w.deleteCharAt(w.length()-1).append(");\n");
		h.deleteCharAt(h.length()-1).append(");\n");
		x.deleteCharAt(x.length()-1).append(");\n");
		y.deleteCharAt(y.length()-1).append(");\n");
		bgcolor.deleteCharAt(bgcolor.length()-1).append(");\n");
		color.deleteCharAt(color.length()-1).append(");\n");
		size.deleteCharAt(size.length()-1).append(");\n");
		align.deleteCharAt(align.length()-1).append(");\n");
		margin.deleteCharAt(margin.length()-1).append(");\n");


		swf_env.allFrame.append(imgpath);
		swf_env.allFrame.append(name);
		swf_env.allFrame.append(w);
		swf_env.allFrame.append(h);
		swf_env.allFrame.append(x);
		swf_env.allFrame.append(y);
		swf_env.allFrame.append(bgcolor);
		swf_env.allFrame.append(color);
		swf_env.allFrame.append(size);
		swf_env.allFrame.append(align);
		swf_env.allFrame.append(margin);
		swf_env.allFrame.append(data);

	}


	private void writeDispFunc(){
		String func = "frame($movie,$mask,$rate,$duration,$offset,$name,$x,$y,$w,$h,$bgcolor,$color,$size,$align,$margin,$str,$imgpath);\n\n";
		swf_env.allFrame.append(func);
		Log.out("@@@ write out dispFunc @@@");
	}

	public String getSymbol() {
		return "G4";
	}

	public SWFValue getInstance(){
		return value;
	}


}
