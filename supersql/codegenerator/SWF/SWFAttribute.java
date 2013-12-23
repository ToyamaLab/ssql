/*
 * Created on 2004/11/26 by mai
*/
package supersql.codegenerator.SWF;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;
//import java.lang.Character.UnicodeBlock;

public class SWFAttribute extends Attribute implements SWFTFE{
	Manager manager;
	SWFEnv swf_env;
	SWFValue value;

	int level;
	String data;
	String name;
	String int_name;//morya wrote
	String int_namet;
	String int_x;
	String int_y;
	String int_w;
	String int_h;
	String int_bgcolor;
	String int_bdcolor;
	String int_color;
	String int_alpha;
	String int_size;
	String int_align;
	String int_margin;
	String int_str;
	String inter_imgpath;//photo
	String inter_x;
	String inter_y;
	String inter_w;
	String inter_h;
	String inter_name;

	String lnum;//only scene0
	String visibleflag;
	String lx;
	String ly;
	String lw;
	String lh;
	String lstop;
	String tmp;//photo
	String limgnum;


	int data_width;
	int data_height;
	int fontsize;
	int width;
	int height;
	int margin;
	// edit tomo 2007.12.23
	String texttype;
	int mask=-1;
	public SWFAttribute(Manager manager, SWFEnv senv) {
		super();
		this.manager = manager;
		this.swf_env = senv;
	}

	@Override
	public String work(ExtList data_info) {
		margin = swf_env.margin;
		fontsize = swf_env.fontSize;
		this.value = new SWFValue("Att");
		name = Integer.toString(swf_env.instanceID);
		Log.out("[SWFAtt:work]name = "+name);
		int_name = "linker" + Integer.toString(swf_env.interactionNUM);
		Log.out("[SWFAtt:workda]int_name = "+int_name);
		int_namet = "linkert" + Integer.toString(swf_env.interactionNUM);
		inter_name = "intimg" + Integer.toString(swf_env.interactionImgNUM);
		tmp = "tmp" + Integer.toString(swf_env.interactionImgNUM);
		value.lnum = Integer.toString(swf_env.visibleflag_counter);

		data = this.getStr(data_info);
		int_str = this.getStr(data_info);
		Log.out("[SWFAtt:work]data = "+data);
		setDecoration1();

//		data_width = stringWidth(data, fontsize);
		data_width = swf_env.stringWidth(data,fontsize);
		data_height = (int) Math.ceil(fontsize * 1.6); 
		width = data_width + margin * 2;
		height = data_height;

		texttype = value.texttype;
		setDecoration2();
//		Log.out("[SWFAtt:work]setDecoration2 finished");
		if(data_width > width){ 
			width = data_width + margin * 2;
		}
		if(data_height > height){
			height = data_height + margin;
		}

//		if(texttype.equals("telop") || texttype.equals("scroll")) {
//			if (mask == -1) {
////				mask = width;
//				mask = data_width;
//			} else {
////				int buf = width;
////				width = mask;
////				mask = buf;
//				int buf = data_width;
//				data_width = mask;
//				mask = buf;
//			}
//		}
		value.instanceName = name;
		value.data = data;
//		Log.out("[SWFAtt:work]value.data = "+value.data);
		value.data_width = data_width;
//		Log.out("[SWFAtt:work]value.data_width = "+value.data_width);
		value.data_height = data_height;
//		Log.out("[SWFAtt:work]value.data_height = "+value.data_height);
		value.width = width;
//		Log.out("[SWFAtt:work]value.width = "+value.width);
		value.height = height;
//		Log.out("[SWFAtt:work]value.height = "+value.height);
		value.margin = margin;
//		Log.out("[SWFAtt:work]value.margin = "+value.margin);
		value.fontsize = fontsize;
//		Log.out("[SWFAtt:work]value.fontsize = "+value.fontsize);
		value.level = level;
//		Log.out("[SWFAtt:work]value.level = "+value.level);
		value.texttype = texttype;
		value.mask = mask;
//  morya wrote
		value.interactionName = int_name;
		value.interactionNamet = int_namet;
		value.inter_name = inter_name;
		value.tmp = tmp;
		value.int_str = int_str;
		value.int_w = width;
		value.int_h = height;
		setDecoration3();

//		swf_env.tmp_width = width;
		swf_env.tmp_height = height;
//		edit tomo 2007.12.29
//		swf_env.tmp_width = mask;
		if(texttype.equals("telop") || texttype.equals("scroll")) {
			if (mask == -1) {
				swf_env.tmp_width = width;
			} else {
				swf_env.tmp_width = mask;
			}
		} else {
			swf_env.tmp_width = width;
		}
		swf_env.interactionNUM++;
		swf_env.interactionImgNUM++;
		return null;
	}

	public void setDecoration1(){

		if(decos.containsKey("margin")){
			margin = Integer.parseInt(decos.get("margin").toString());
			value.int_margin = Integer.parseInt(decos.get("margin").toString());
		}

		// ʸ����
		if(decos.containsKey("font-size")){
			fontsize = Integer.parseInt(decos.get("font-size").toString());
			value.int_size = Integer.parseInt(decos.get("font-size").toString());
		}else if(decos.containsKey("font size")){
			fontsize = Integer.parseInt(decos.get("font size").toString());
			value.int_size = Integer.parseInt(decos.get("font size").toString());
		}else if(decos.containsKey("size")){
			fontsize = Integer.parseInt(decos.get("size").toString());
			value.int_size = Integer.parseInt(decos.get("size").toString());
		}else if(decos.containsKey("fontsize")){
			fontsize = Integer.parseInt(decos.get("fontsize").toString());
			value.int_size = Integer.parseInt(decos.get("fontsize").toString());
		}
	}

	public void setDecoration2(){

		if(decos.containsKey("width"))
			width = Integer.parseInt(decos.get("width").toString());
		if(decos.containsKey("height"))
			height = Integer.parseInt(decos.get("height").toString());

		// edit tomo 2007.12.23

		if(decos.containsKey("texttype"))
			texttype = decos.get("texttype").toString();

		if(decos.containsKey("mask"))
			mask = Integer.parseInt(decos.get("mask").toString());
	}

	public void setDecoration3(){
		if(decos.containsKey("align")){
			value.align = decos.get("align").toString();
			value.int_align = decos.get("align").toString();
		}

		if(decos.containsKey("background-color")){
			value.bgcolor = decos.get("background-color").toString();
			value.int_bgcolor = decos.get("background-color").toString();
		}else if(decos.containsKey("bgcolor")){
			value.bgcolor = decos.get("bgcolor").toString();
			value.int_bgcolor = decos.get("bgcolor").toString();
		}

		if(decos.containsKey("boderColor")){
			value.bdcolor = decos.get("boderColor").toString();
			value.int_bdcolor = decos.get("boderColor").toString();
		}else if(decos.containsKey("border-color")){
			value.bdcolor = decos.get("border-color").toString();
			value.int_bdcolor = decos.get("border-color").toString();
		}else if(decos.containsKey("bordercolor")){
			value.bdcolor = decos.get("bordercolor").toString();
			value.int_bdcolor = decos.get("bordercolor").toString();
		}else if(decos.containsKey("bdcolor")){
			value.bdcolor = decos.get("bdcolor").toString();
			value.int_bdcolor = decos.get("bdcolor").toString();
		}

		//ʸ��
		if(decos.containsKey("color")){
			value.fontcolor = decos.get("color").toString();
			value.int_color = decos.get("color").toString();
		}else if(decos.containsKey("font-color")){
			value.fontcolor = decos.get("font-color").toString();
			value.int_color = decos.get("font-color").toString();
		}else if(decos.containsKey("font color")){
			value.fontcolor = decos.get("font color").toString();
			value.int_color = decos.get("font color").toString();
		}else if(decos.containsKey("fontcolor")){
			value.fontcolor = decos.get("fontcolor").toString();
			value.int_color = decos.get("fontcolor").toString();
		}

		if(decos.containsKey("style"))
			value.fontstyle = decos.get("style").toString();
		else if(decos.containsKey("font-style"))
			value.fontstyle = decos.get("font-style").toString();
		else if(decos.containsKey("font style"))
			value.fontstyle = decos.get("font style").toString();
		else if(decos.containsKey("fontstyle"))
			value.fontstyle = decos.get("fontstyle").toString();

		//morya wrote
		if(decos.containsKey("alpha"))
			value.int_alpha = Integer.parseInt(decos.get("alpha").toString());
		if(decos.containsKey("stop")){
			Log.out(decos.get("stop").toString());
			if(decos.get("stop").toString().equals("true")){
				value.lstop = 1;
			}else{
				value.lstop = 0;
			}
		}
		if(decos.containsKey("window-x"))
			value.window_x = Integer.parseInt(decos.get("window-x").toString());
		if(decos.containsKey("window-y"))
			value.window_y = Integer.parseInt(decos.get("window-y").toString());
	}
/*
	public float stringWidth(String data, int fontsize){
		float data_width = 0;
		float chr = 0;
		int len = data.length();

     for(int i=0; i<len; i++){
          char c = data.charAt(i);
//          Log.out("[" + c + "] ");
          UnicodeBlock b = UnicodeBlock.of(c);
          if(b == UnicodeBlock.BASIC_LATIN){

          } else{
          		chr += 1.4;
          }
      }
     data_width = fontsize * chr;
     return data_width;
  }
*/

	public SWFValue getInstance(){
		return this.value;
	}

}
