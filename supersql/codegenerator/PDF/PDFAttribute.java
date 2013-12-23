package supersql.codegenerator.PDF;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Manager;
import supersql.codegenerator.TFE;
import supersql.extendclass.ExtList;

public class PDFAttribute extends Attribute implements PDFTFE {

	private PDFEnv pdf_env;
	private PDFValue value;
	private String data;
	private float data_width;
	private float data_height;
	private float box_width;
	private float box_height;
	private float padding_H;
	private float padding_V;
	private float fontsize;
	private String fontstyle;

	
	//レイアウト変換用 newLEはAttributeでは特に意味はない
	private TFE newLE;
	private boolean change = false;
	private boolean fontsizeDECO = false;
	private boolean widthDECO = false;
	private float tuneWidth = 0;
	private float tmpTuneWidth;
	private float tuneFontsize = 0;
	private float tmpTuneFontsize;
	
	
	//コンストラクタ
	public PDFAttribute(Manager manager, PDFEnv penv) {
		super();
		this.pdf_env = penv;
		
		new PDFModifier();
	}

	//Attributeのworkメソッド
	@Override
	public String work(ExtList data_info) {
		
//		int level = pdf_env.level;
		
		padding_H = pdf_env.padding_H;
		padding_V = pdf_env.padding_V;
		fontsize = pdf_env.DefaultFontSize;
		
		System.out.println("++++ Attでvalueをnewします");
		this.value = new PDFValue("Att");
		
		//上位の装飾情報の退避 from PDFEnv
			
		data = this.getStr(data_info);
		System.out.println("[PDFAtt]data = "+data);
		
		
		//値を代入する前に処理する装飾子
		setDecoration1();
		
		if(tuneFontsize != 0)
			fontsize = tuneFontsize;
		
		//-------------------------------------------------------//
		//data_width = modifier.stringwidth(data, pdf_env);
		data_width = pdf_env.stringwidth(data, fontsize);
		//data_height = pdf_env.fontsize;////仮に高さを固定にする
		data_height = fontsize;////仮に高さを固定にする
		
		//誤差による表構造の乱れの解消！！
		if(data_width - (int)data_width > 0.9)
			data_width = (int)data_width + 1;
		
		box_width = data_width + padding_H * 2;
		box_height = data_height + padding_V * 2;
		//-------------------------------------------------------//
		
		//値を代入する直前に処理する装飾子
		fontstyle = pdf_env.DefaultFontStyle;
		setDecoration2();//imageのように場所を変えるべきかも
		
		if(tuneWidth != 0)
			box_width = tuneWidth;
		
		//追加10.27　テキストボックス処理のため
		if(box_width - padding_H * 2 < data_width){
			data_width = box_width - padding_H * 2;
			data_height = pdf_env.textflow_blind(data, fontsize, fontstyle, data_width);
			box_height = data_height + padding_V * 2;
		}
		
		System.out.println("*************"+data_width+" "+box_width);
		
		//変更10.17
		value.data = data;
		value.data_width = data_width;
		value.data_height = data_height;
		value.box_width = box_width;
		value.box_height = box_height;
		value.padding = padding_H;
		value.fontsize = fontsize;
		value.fontstyle = fontstyle;
		
		value.originalWidth = box_width;		//C2のレイアウト変換に使用
		value.originalHeight = box_height;	//C1のレイアウト変換に使用
		

		//値を代入した後にに処理する装飾子
		setDecoration3();
			
		
		//問題点
		//modifier.set_modifier2(data, data_width, pdf_env, value);
		
		//追加10.17
		pdf_env.tmp_width = box_width;
		pdf_env.tmp_height = box_height;
		return null;
		
		//退避していた装飾情報を戻す to PDFEnv
		
	}
	
	public void setDecoration1(){
		//余白
		if(decos.containsKey("padding")){
			padding_H = Float.parseFloat(decos.get("padding").toString());
			padding_V = Float.parseFloat(decos.get("padding").toString());
		}
		
		//文字サイズ
		if(decos.containsKey("font-size")){
			fontsize = Float.parseFloat(decos.get("font-size").toString());
			fontsizeDECO = true;
		}
		else if(decos.containsKey("font size")){
			fontsize = Float.parseFloat(decos.get("font size").toString());
			fontsizeDECO = true;
		}
		else if(decos.containsKey("size")){
			fontsize = Float.parseFloat(decos.get("size").toString());
			fontsizeDECO = true;
		}
		else if(decos.containsKey("fontsize")){
			fontsize = Float.parseFloat(decos.get("fontsize").toString());
			fontsizeDECO = true;
		}
		
		//文字のスタイル
		if(decos.containsKey("style"))
			fontstyle = decos.get("style").toString();
		else if(decos.containsKey("font-style"))
			fontstyle = decos.get("font-style").toString();
		else if(decos.containsKey("font style"))
			fontstyle = decos.get("font style").toString();
		else if(decos.containsKey("fontstyle"))
			fontstyle = decos.get("fontstyle").toString();
	}
	
	public void setDecoration2(){
		//セルの幅・高さ dataで取らないとテキストボックスにならない＋heightを指定しないと行を変えられない
		if(decos.containsKey("width")){
			box_width = Float.parseFloat(decos.get("width").toString());
			widthDECO = true;
		}
		//テキストボックス処理 text_flowで解決
		if(decos.containsKey("height"))
			box_height = Float.parseFloat(decos.get("height").toString());
	}
	
	public void setDecoration3(){
		//文字位置
		if(decos.containsKey("align"))
			value.align = decos.get("align").toString();
		if(decos.containsKey("valign"))
			value.valign = decos.get("valign").toString();
		
		//背景色
		if(decos.containsKey("background-color"))
			value.bgcolor = decos.get("background-color").toString();
		else if(decos.containsKey("bgcolor"))
			value.bgcolor = decos.get("bgcolor").toString();
		
		//文字色
		if(decos.containsKey("color"))
			value.fontcolor = decos.get("color").toString();
		else if(decos.containsKey("font-color"))
			value.fontcolor = decos.get("font-color").toString();
		else if(decos.containsKey("font color"))
			value.fontcolor = decos.get("font color").toString();
		else if(decos.containsKey("fontcolor"))
			value.fontcolor = decos.get("fontcolor").toString();
	}
	
	public PDFValue getInstance(){
		System.out.println("++++ Attをsetしました");
		return this.value;
	}
	
	
	public void setLabel(PDFValue result) {
		
		int labelH = pdf_env.labelH;
		int labelV = pdf_env.labelV;
		
		int labelO = pdf_env.labelO;

		
		if( pdf_env.labelSuffixH.equals("null") )
			result.labelH = Integer.toString(labelH);
		else
			result.labelH = Integer.toString(labelH) + pdf_env.labelSuffixH;
		
		if( pdf_env.labelSuffixV.equals("null") )
			result.labelV = Integer.toString(labelV);
		else
			result.labelV = Integer.toString(labelV) + pdf_env.labelSuffixV;
		
		if( pdf_env.labelSuffixH.equals("null") )
			result.labelOH = Integer.toString(labelO);
		else
			result.labelOH = Integer.toString(labelO) + pdf_env.labelSuffixH;
		
		if( pdf_env.labelSuffixV.equals("null") )
			result.labelOV = Integer.toString(labelO);
		else
			result.labelOV = Integer.toString(labelO) + pdf_env.labelSuffixV;
		
		
		
		if( !pdf_env.labelListH.contains(result.labelH) )
			pdf_env.labelListH.add(result.labelH);
		if( !pdf_env.labelListV.contains(result.labelV) )
			pdf_env.labelListV.add(result.labelV);
		
		if( !pdf_env.labelListOH.contains(result.labelOH) )
			pdf_env.labelListOH.add(result.labelOH);
		if( !pdf_env.labelListOV.contains(result.labelOV) )
			pdf_env.labelListOV.add(result.labelOV);
		
	}
	
	
	//Attributeでは特に意味はない
	public void restoreFOLD(PDFValue check){
		
	}
	
	
	public boolean optimizeW(float Dexcess, PDFValue box){
		boolean flex = false;
		
		//fontsizeの縮小も幅の指定もまだ仮　条件がいい加減
		
		if(!fontsizeDECO){		//下のif文の条件を変えたので、指定があってもいいかも
			int charNum = box.data.length();//英数字が入ると等幅ではないからlengthじゃあ対応しきれないかも
			float DperChar = Dexcess / charNum;
			float newFontsize = box.fontsize - DperChar;
			if( newFontsize > box.fontsize - (pdf_env.DefaultFontSize - pdf_env.minFontsize) ){
			//if(newFontsize > pdf_env.minFontsize){
				
	//			decos.put("fontsize", Float.toString(newFontsize));
				tmpTuneFontsize = tuneFontsize;
				tuneFontsize = newFontsize;
				
				System.out.println("fontsize change");
				//change = true;//layoutの変更がある訳ではないので
				flex = true;
				pdf_env.cutWidth = Dexcess;
			}
		}
	
		//else if(!widthDECO){
		if(!widthDECO && !flex){
		//if(!widthDECO){
			float newWidth = box.box_width - Dexcess;
			//pdf_env.flexTHを使わないといけない
			if(newWidth > 0){
				
				//--------------------------------------//
				//	Attributeは新しくnewするのは難しい	//
				//	newLE = new PDFAttribute(manager, pdf_env);
				//	((PDFAttribute)newLE).decos = this.decos;
				//	((PDFAttribute)newLE).Items = this.Items;
				//	decos.put("width", Float.toString(newWidth));
				//	change = true;//やっぱり変更タイプにするC1に凝る影響
				//--------------------------------------//
				
	//			decos.put("width", Float.toString(newWidth));
				tmpTuneWidth = tuneWidth;
				tuneWidth = newWidth;
		
				System.out.println("width change");
				//change = true;//layoutの変更がある訳ではないので		
				flex = true;
				pdf_env.cutWidth = Dexcess;
			}
		}
		
		return flex;
	}
	
	
	public boolean optimizeH(float Dexcess, PDFValue box){
		boolean flex = false;
		
		return flex;
	}
	
	
	public void redoChange(){
		tuneWidth = tmpTuneWidth;
		tuneFontsize = tmpTuneFontsize;
	}
	
	
	//Attributeでは特に意味はない
	public TFE getNewChild(){
		return newLE;
	}
	
	
	public boolean changeORnot(){
		return change;
	}
	
	
}