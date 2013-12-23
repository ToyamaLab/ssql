/*
 * Created on 2004/11/26 by mai
 */
package supersql.codegenerator.SWF;

import supersql.codegenerator.Function;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class SWFFunction extends Function implements SWFTFE {
	Manager manager;

	SWFEnv swf_env;
	SWFValue value;

	int data_width, data_height, width, height;
	int margin = 3;
	int border = 1;

	public SWFFunction(Manager manager, SWFEnv senv) {
		super();
		this.manager = manager;
		this.swf_env = senv;
	}

	@Override
	public String work(ExtList data_info) {
		this.setDataList(data_info);
		//    	Log.out("FuncName= " + this.getFuncName());
		//    	Log.out("filename= " + this.getAtt("filename"));
		//    	Log.out("condition= " + this.getAtt("condition"));

		String FuncName = this.getFuncName();

		if(FuncName.equalsIgnoreCase("imagefile")){
			Func_imagefile();
		}

		swf_env.getDecos(this.decos);
		return null;
	}

	public void Func_imagefile(){
		//  imagefile function : (imagefile, path="")

		swf_env.containImg = true;


		this.value = new SWFValue("Func");

		String path = this.getAtt("path", ".");
		String filename = this.getAtt("default");
		String type = "auto";
		value.instanceName = "img"+ Integer.toString(swf_env.instanceID);
		value.data = "";
		value.imgpath = path + "/" + filename;
		value.inter_imgpath = path + "/" + filename;
		value.inter_name = "intimg" + Integer.toString(swf_env.interactionImgNUM);
		value.tmp = "tmp" + Integer.toString(swf_env.interactionImgNUM);
		value.lnum = Integer.toString(swf_env.visibleflag_counter);


		if(!path.startsWith("/")) {
			String basedir = GlobalEnv.getBaseDir();
			Log.out("basedir= " +basedir);
			if(basedir != null && basedir != "") {
				path = basedir + path;
			}
		}

		String filepath = path + "/" + filename;


		setDecoration1();
		value.margin = margin;


		System.out.println("filepath = "+filepath);
		int img = swf_env.open_image_file(type, filepath);
		value.img = img;

		data_width = swf_env.get_value("imagewidth", img);
		data_height = swf_env.get_value("imageheight", img);
		System.out.println("imagesize: "+data_width+" "+data_height);
		width = data_width + margin * 2;
		height = data_height + margin * 2;


		setDecoration2();

		if(data_width > width){
			int original_width = data_width;
			data_width = width - margin * 2;
			int scale = data_width / original_width;
			data_height = data_height * scale;
			height = data_height + margin * 2;
		}
		if(data_height > height){
			int original_height = data_height;
			data_height = height - margin * 2;
			int scale = data_height / original_height;
			data_width = data_width * scale;
			width = data_width + margin * 2;
		}

		value.data_width = data_width;
		value.data_height = data_height;
		value.width = width;
		value.height = height;
		//morya wrote
		value.int_w = width;
		value.int_h = height;


		setDecoration3();

		swf_env.tmp_width = width;
		swf_env.tmp_height = height;


		swf_env.instanceID++;

	}

	public void setDecoration1(){

		if(decos.containsKey("margin")){
			margin = Integer.parseInt(decos.get("margin").toString());
			margin = Integer.parseInt(decos.get("margin").toString());
		}
		if(decos.containsKey("border")){
			border = Integer.parseInt(decos.get("border").toString());
			swf_env.border = border;
		}
	}

	public void setDecoration2(){

		if(decos.containsKey("width"))
			width = Integer.parseInt(decos.get("width").toString());

		if(decos.containsKey("height"))
			height = Integer.parseInt(decos.get("height").toString());

	}

	public void setDecoration3(){

		if(decos.containsKey("align"))
			value.align = decos.get("align").toString();
		if(decos.containsKey("valign"))
			value.valign = decos.get("valign").toString();


		if(decos.containsKey("background-color"))
			value.bgcolor = decos.get("background-color").toString();
		else if(decos.containsKey("bgcolor"))
			value.bgcolor = decos.get("bgcolor").toString();


		if(decos.containsKey("boderColor"))
			value.bdcolor = decos.get("boderColor").toString();
		else if(decos.containsKey("border-color"))
			value.bdcolor = decos.get("border-color").toString();
		else if(decos.containsKey("bordercolor"))
			value.bdcolor = decos.get("bordercolor").toString();
		else if(decos.containsKey("bdcolor"))
			value.bdcolor = decos.get("bdcolor").toString();

	}

	public SWFValue getInstance(){

		return this.value;
	}
}
