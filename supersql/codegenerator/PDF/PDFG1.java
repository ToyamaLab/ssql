package supersql.codegenerator.PDF;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.codegenerator.TFE;
import supersql.extendclass.ExtList;

public class PDFG1 extends Grouper implements PDFTFE {

	private PDFManager manager;

	private PDFEnv pdf_env;
	
	//追加10.17
	private PDFValue value;
	
	private ExtList rows;		//各インスタンスの各列の要素数  注：クラス変数では無理
	
	//移動10.28  元々はworkメソッド内
	private float tmp_width;
	private float tmp_height;
	private float box_widthMAX;		//折り畳みの変数　幅の最大値
	private float box_heightSUM;	//折り畳みの変数　高さの合計
	private float fold;				//折り畳みの変数　何cm、何個で折り畳むかを格納			
	private int topID;				//widthをre_setする時inListの何番目からか

//	boolean fold_or_not = false;
	
	ExtList maxHeights;			//このG1の各列最大高さ
	
	int rowNum;					//各インスタンスの折り畳んだ回数
	ExtList rowHeights;			//各インスタンスの各列最大高さ

//	float rowMaxHeight;			//メソッド内に移動

	
	//----- setLevelメソッドで幅調節のSuffix用 -----//
	int repeatNum = 0;
	
	
	//レイアウト変換用
	TFE newLE;
	boolean change = false;
	
	
	//コンストラクタ
	public PDFG1(Manager manager, PDFEnv pdf_env) {
		this.manager = (PDFManager) manager;
		this.pdf_env = pdf_env;
		//追加10.30  折り畳み用
		maxHeights = new ExtList();
	}

	@Override
	public String work(ExtList data_info) {
		//追加10.14 メソッド内に移動10.24
		float box_width = 0;
		float box_height = 0;
//		int level = pdf_env.level;
		
//		int fold_num;

		box_widthMAX = 0;
		box_heightSUM = 0;

		fold = 0;
		
		rowNum = 0;
		
		
		
		System.out.println("");
		System.out.println("------- G1 -------");
		System.out.println("[PDFG1:work]tfe_info = " + makele0());
		System.out.println("[PDFG1:work]data_info = " + data_info);

		System.out.println("++++ G1でvalueをnewします");
		//追加10.17
		value = new PDFValue("G1");
		
		rows = new ExtList();
		rowHeights = new ExtList();
//		rowHeights.add("0");

		int i;

		setDataList(data_info);
		setDecoration();

		if (tfe instanceof Attribute) {
			System.out.println("[PDFG1:work]tfe is Attribute");
			System.out.println("number of data = " + data_info.size());


			for (i = 0; i < data_info.size(); i++) {
				//追加10.27　右下方向のみに深くならない構造の兄弟を判別するポイント
//				pdf_env.level_max = level;

				
				//追加10.24 追加10.28折り畳み対応　兄弟に同じ数字を割り振る処理
//				pdf_env.level++;
////				pdf_env.level += rowNum;
//				if(pdf_env.level > pdf_env.level_max ){
//					pdf_env.level_max = pdf_env.level;
//				}

				worknextItem();
				
				//追加10.24 兄弟に同じ数字を割り振る処理　変更10.27
				//pdf_env.level--;
//				pdf_env.level = level;
				
				
				//追加10.28　折り畳み判断
				if(fold != 0){
					if(Fold_or_Not(box_width, box_height, fold)){
						box_width = 0;
						box_height = 0;
					}
				}
				
				
				//追加10.17
				//ここでtmp_width,heightを足しこんでいく
				box_width += pdf_env.tmp_width;
				tmp_height = pdf_env.tmp_height;
				if (box_height < tmp_height)
					box_height = tmp_height;
				
				
				//追加10.30
				//checkMaxHeight(pdf_env.tmp_width);
				checkMaxHeight(box_height);
				
				
				System.out.println("++++ G1でAttをsetします");
				//追加10.17 valueのセット
				value.inList.add(((PDFTFE)tfe).getInstance());
				
				//移動

			}

			
		}

		//////////////////////////////////////////
		//////////////////(G2 (C1 1 2))とかのとき
		//////////////////////////////////////////
		else {
			System.out.println("[PDFG1:work]tfe is Operator");


			//tableの値があるだけ、処理をする
			for (i = 0; i < data_info.size(); i++) {
				//追加10.27　右下方向のみに深くならない構造の兄弟を判別するポイント
//				pdf_env.level_max = level;


				//追加10.24 追加10.28折り畳み対応　 兄弟に同じ数字を割り振る処理
//				pdf_env.level++;
////				pdf_env.level += rowNum;
//				if(pdf_env.level > pdf_env.level_max ){
//					pdf_env.level_max = pdf_env.level;
//				}
				
				worknextItem();
				
				//追加10.24 兄弟に同じ数字を割り振る処理　変更10.27
				//pdf_env.level--;
//				pdf_env.level = level;

				
				
				//追加10.28　折り畳み判断
				if(fold != 0){
					if(Fold_or_Not(box_width, box_height, fold)){
						box_width = 0;
						box_height = 0;
					}
				}
				
				
				//追加10.11,14
				//ここでtmp_width,heightを足しこんでいく
				//G1の場合　heightとwidthを入れ替える
				box_width += pdf_env.tmp_width;
				tmp_height = pdf_env.tmp_height;
				if (box_height < tmp_height)
					box_height = tmp_height;

				
				//追加10.30
				//checkMaxHeight(pdf_env.tmp_height);
				checkMaxHeight(box_height);
				
				
				System.out.println("++++ G1でリストをsetします");
				//追加10.17 valueのセット
				value.inList.add(((PDFTFE)tfe).getInstance());
				
				
				//移動

			}

			
		}

		///////////////////////////////

		//追加10.24  変更10.28  メソッド化して折り畳みと共有
		//for(i=0; i<value.inList.size(); i++){
		//	PDFValue re_set = (PDFValue)value.inList.get(i);
		//	re_set.box_height = box_height;
		//}
		re_setHeight(topID, value.inList.size(), box_height);

		
		//追加10.28  折り畳み時はbox_width,heightの調整
		if(fold != 0){
			if(box_width < box_widthMAX){
				box_width = box_widthMAX;
			}
			box_height += box_heightSUM;//下のこのインスタンスの高さ取得でいいかも
		}

		
		//おそらく下のようにただ追加するだけでいい
//		if(columns.size() == 0)
//			columns.add(Integer.toString(value.inList.size()));
		//追加11.2  大事
		rows.add(Integer.toString(value.inList.size()));

		
		//---------- このインスタンスの高さ取得 ------------//
		float rowHeight = 0;
		
		for(i=0; i<rowHeights.size(); i++)
			rowHeight += Float.parseFloat((String)rowHeights.get(i));
		box_height = rowHeight;
		//------------------------------------------------//
		
		//追加10.17
		value.box_width = box_width;
		value.box_height = box_height;
		
		value.originalWidth = box_width;		//C2のレイアウト変換に使用
		value.originalHeight = box_height;	//C1のレイアウト変換に使用
		
		
		value.rowNum = rowNum;
		value.rows = rows;
		value.rowHeights = rowHeights;
		
		value.fold = fold;
		value.tfeID = getId();
		
		pdf_env.tmp_width = box_width;
		pdf_env.tmp_height = box_height;

		
		
		//-------- このG1の各列最大高さを常に監視 ----------//
		float maxHeight;
		
		if( maxHeights.size() < rows.size() ){
			int diff = rows.size() - maxHeights.size();
			for(i=0; i<diff; i++)
				maxHeights.add("0");
		}
		for(i=0; i<rowHeights.size(); i++){
			maxHeight = Float.parseFloat((String)maxHeights.get(i));
			rowHeight = Float.parseFloat((String)rowHeights.get(i));
			if(maxHeight < rowHeight)
				maxHeights.set(i, Float.toString(rowHeight));
		}
		//------------------------------------------------//
		
		
		System.out.println("折り畳んだ回数＝"+rowNum);
		int local;
		for(local=0;local<rows.size();local++){
			System.out.println("dore"+rows.get(local));
		}
		return null;
	}
	
	
	
	
	//追加10.28　折り畳み判断
	public boolean Fold_or_Not(float box_width, float box_height, float fold){
		boolean fold_or_not = false;
//		fold_or_not = false;
		tmp_width = box_width;
		tmp_width += pdf_env.tmp_width;
		if(tmp_width>fold){
			Fold(box_width, box_height);
			rows.add(Integer.toString(value.inList.size()));
			System.out.println("eeeeeeeeeeeeeee"+value.inList.size());
			//1つ前の要素、折り返す直前の要素に情報を代入したい
			rowNum++;																//これが早い
			fold_or_not = true;
		}
		return fold_or_not;
	}
	
	//追加10.28  折り畳み
	public void Fold(float box_width, float box_height){	
		tmp_width = box_width;
		if(tmp_width > box_widthMAX){
			box_widthMAX = tmp_width;
		}
		box_heightSUM += box_height;//他と大小比較が必要　checkMaxHeight()でいいのかも
		//この時inListにsetされているvalueにはbox_heightをre-set
		re_setHeight(topID, value.inList.size(), box_height);
		topID = value.inList.size();
	}
	
	//追加10.30
	public void checkMaxHeight(float box_height){
		float rowTmpHeight;
		
		if(rowNum >= rowHeights.size())
			rowHeights.add("0");
		rowTmpHeight = Float.parseFloat((String)rowHeights.get(rowNum));
		if(rowTmpHeight < box_height)
			rowHeights.set(rowNum, Float.toString(box_height));
		
//		if(fold_or_not)
//			rowNum++;
	}
	
	public void re_setHeight(int start, int end, float set_height){
		int i;
		for(i=start; i<end; i++){
			PDFValue re_set = (PDFValue)value.inList.get(i);
			re_set.box_height = set_height;
		}
	}
	
	public void setDecoration(){
		//折り畳み
		if(decos.containsKey("fold")){
			fold = Float.parseFloat(decos.get("fold").toString());
		}
	}
	
	public PDFValue getInstance(){
		System.out.println("++++ G1をsetしました");
		return value;
	}

	
	public void setLabel(PDFValue result) {

		int i;
		PDFValue instance;
		
		//---- columnNumをrepeatNumとしてクラス変数に変更 ----//
		int rowNum = 0;					//折り畳み対応　レベルの接尾語「何列目」
		//int columnNum = 0;			//反復出力した回数　レベルの接尾語「何行目」
		
		int labelH = pdf_env.labelH;					//自分の幅ラベルを取得＆退避
		int labelV = pdf_env.labelV;					//自分の高さラベルを取得＆退避
		String labelSuffixH = pdf_env.labelSuffixH;		//自分の幅ラベルSuffixを取得
		String labelSuffixV = pdf_env.labelSuffixV;		//自分の高さラベルSuffixを取得
		
		
		//----------------------------------------------
		int labelO = pdf_env.labelO;
		
		
		//--------- labelとSuffixを合わせて、自分のラベルを代入 -----------//
		if( pdf_env.labelSuffixH.equals("null") )
			result.labelH = Integer.toString(labelH);
		else
			result.labelH = Integer.toString(labelH) + labelSuffixH;
		
		if( pdf_env.labelSuffixV.equals("null") )
			result.labelV = Integer.toString(labelV);
		else
			result.labelV = Integer.toString(labelV) + labelSuffixV;
		//-----------------------------------------------------------------//
		if( pdf_env.labelSuffixH.equals("null") )
			result.labelOH = Integer.toString(labelO);
		else
			result.labelOH = Integer.toString(labelO) + labelSuffixH;
		
		if( pdf_env.labelSuffixV.equals("null") )
			result.labelOV = Integer.toString(labelO);
		else
			result.labelOV = Integer.toString(labelO) + labelSuffixV;
		
		
		//--------- 各ラベルが初めての値ならばラベルリストに追加　---------//
		if( !pdf_env.labelListH.contains(result.labelH) )
			pdf_env.labelListH.add(result.labelH);
		if( !pdf_env.labelListV.contains(result.labelV) )
			pdf_env.labelListV.add(result.labelV);
		//-----------------------------------------------------------------//
		if( !pdf_env.labelListOH.contains(result.labelOH) )
			pdf_env.labelListOH.add(result.labelOH);
		if( !pdf_env.labelListOV.contains(result.labelOV) )
			pdf_env.labelListOV.add(result.labelOV);
		
		
		//注　C1直下のG1直下のC1で高さを揃えようとするには、
		//    ここで親がC1だったらmaxをカウントアップしなければいいはず
		//    しかし、それには親がなんだかわかる必要がある
		//    さらに揃えようとしても実際あまり極端にキレイにはならないはず
		
		
		int maxHtmp;				//子要素に渡すmaxを退避しておく変数
		int maxVtmp;				//子要素に渡すmaxを退避しておく変数
		
		pdf_env.labelmaxH++;
		maxHtmp = pdf_env.labelmaxH;			//maxを退避
		pdf_env.labelmaxV++;
		maxVtmp = pdf_env.labelmaxV;			//maxを退避
		
		
		int labelHtmp;				//子要素に渡すlabelHを退避しておく変数
		int labelVtmp;				//子要素に渡すlabelVを退避しておく変数
		
		pdf_env.labelH = pdf_env.labelmaxH;
		labelHtmp = pdf_env.labelH;				//labelHを退避		
		pdf_env.labelV = pdf_env.labelmaxV;
		labelVtmp = pdf_env.labelV;				//labelVを退避
		
		//----------------------------------
		int maxOtmp;
		pdf_env.labelmaxO++;
		maxOtmp = pdf_env.labelmaxO;
		int labelOtmp;
		pdf_env.labelO = pdf_env.labelmaxO;
		labelOtmp = pdf_env.labelO;
		
		
		for (i = 0; i < result.inList.size(); i++) {		
			
			pdf_env.labelH = labelHtmp;
			pdf_env.labelmaxH = maxHtmp;
			
			pdf_env.labelV = labelVtmp;
			pdf_env.labelmaxV = maxVtmp;

			
			//-----------------------------------
			pdf_env.labelO = labelOtmp;
			pdf_env.labelmaxO = maxOtmp;
			
			
			
			//------------- インスタンス毎にSuffixHの数字を増やす ------------//
			repeatNum++;
			//columnNum = i;		//多分こっちでOK
			String columnStr = Integer.toString(repeatNum);
			if(labelSuffixH.equals("null"))
				pdf_env.labelSuffixH = "-" + columnStr;
			else
				pdf_env.labelSuffixH = labelSuffixH + "-" + columnStr;
			//----------------------------------------------------------------//
			

			//------------- 折り畳んだらSuffixVの数字を増やす ----------------//
			if( i == Integer.parseInt((String)result.rows.get(rowNum)) ){
				rowNum++;
			}
			String rowStr = Integer.toString(rowNum);
			if(labelSuffixV.equals("null"))
				pdf_env.labelSuffixV = "-" + rowStr;
			else
				pdf_env.labelSuffixV = labelSuffixV + "-" + rowStr;
			//----------------------------------------------------------------//
			
	
			
			instance = (PDFValue)result.inList.get(i);
			((PDFTFE)tfe).setLabel(instance);
			
				
			//いらないっぽい
			//pdf_env.labelV = labelV;
			
			//------------ for文で繰り返す前にSuffixを元に戻す ---------------//
			pdf_env.labelSuffixH = labelSuffixH;
			pdf_env.labelSuffixV = labelSuffixV;
			//----------------------------------------------------------------//
		}			


	}
	
	
	public void restoreFOLD(PDFValue check){

		if( check.tfeID != getId() ){
			((PDFTFE)tfe).restoreFOLD(check);
		}
		else{			
			int i;
			
			float widthSUM = 0;
			float maxHeight = 0;
			int rowNum = 0;
			
			this.rows.clear();
			this.rowHeights.clear();
			
			for(i=0; i<check.inList.size(); i++){
				PDFValue inBox = (PDFValue)check.inList.get(i);
			
				if(inBox.box_width > check.fold){
					System.out.println("ERROR: one Box's width is over the folding width");
					System.exit(0);
				}
				
				widthSUM += inBox.box_width;
				
				if(widthSUM > check.fold){
					this.rows.add(Integer.toString(i));
					this.rowHeights.add(Float.toString(maxHeight));
					
					maxHeight = 0;
					widthSUM = inBox.box_width;
					rowNum++;
				}
				
				if(maxHeight < inBox.box_height)
					maxHeight = inBox.box_height;				
			}
		
			this.rows.add(Integer.toString(i));
			this.rowHeights.add(Float.toString(maxHeight));
			
			this.rowNum = rowNum;	
			
			check.rows = this.rows;
			check.rowHeights = this.rowHeights;
			check.rowNum = this.rowNum;
		}
		
	}
	
	
	public boolean optimizeW(float Dexcess, PDFValue box){
		boolean flex = false;
		
		int local;
		
		float tmpDexcess;
		float tmpWidth = 0;
		float tmpHeight = 0;
		PDFValue keyBox = box;		//とりあえずの初期化
		
		float sumCutWidth = 0;
	
		TFE originalTFE = this.tfe;		//退避
		
		for (local=box.inList.size()-1; local>-1; local--) {	//逆から走査
			PDFValue inBox = (PDFValue)box.inList.get(local);
			
			//------------ 全てAtt又はImageの時のみ　臨時 -------------------//
			if(inBox.type.equals("Att") || inBox.type.equals("Func")){
				flex = false;
				break;
			}
			//---------------------------------------------------------------//
			
			tmpDexcess = (inBox.box_width / box.box_width) * Dexcess;	//ここはインスタンスのoriginalWidthではなく、max
			flex = ((PDFTFE)tfe).optimizeW(tmpDexcess, inBox);
			if(!flex)
				break;		//反復要素で変換出来きない要素があったらもうやらない　本当はもっと細かくやりたい
			else{
				sumCutWidth += pdf_env.cutWidth;
			}
			
			//注：C1と違って全反復要素で同じ変換をしたい <-- 難 というか不可能
			//このままでは各要素で違う変換をする可能性がある
			// --> 結局tfe_infoは１つなので最後の要素の変換が採用される
			//それでは入りきるかどうかわからない <-- 最後の要素の変換でtmpDexcess以上減るとは限らない
		}
		if(flex){
			//全部変換できたら、直下の子が変わっているか調べて、変わっていたら子要素をgetNewChildで取ってきてセット
			if( ((PDFTFE)tfe).changeORnot() ){
				this.tfe = ((PDFTFE)tfe).getNewChild();
				//flex = true;
				pdf_env.cutWidth = sumCutWidth;
			}
		}
		
		//折り畳みトライ
		if(!flex){
			float newFold;
			
			newFold = box.box_width - Dexcess;
			tmpHeight = box.box_height;			//初めの１行分
			for(local=0; local<box.inList.size(); local++){
				PDFValue inBox = (PDFValue)box.inList.get(local);
				if(tmpWidth > newFold){
					tmpWidth = 0;
					tmpHeight += inBox.box_height;
				}
				tmpWidth += inBox.box_width;
			}
			if( !(pdf_env.flexTH < (tmpHeight - box.box_height) / Dexcess) ){
				decos.put("fold", Float.toString(newFold));
				System.out.println("G1 is folding!!");
				flex = true;
				pdf_env.cutWidth = Dexcess;
			}
		}
		
		if(!flex){
			tmpWidth = 0;
			tmpHeight = 0;
			
			//--------- G2にした時にその幅となるinBoxを探す -----------//
			for(local=0; local<box.inList.size(); local++){
				PDFValue inBox = (PDFValue)box.inList.get(local);
				if(tmpWidth < inBox.box_width){			//originalWidthではなくmax
					keyBox = inBox;
					tmpWidth = inBox.box_width;
				}
				tmpHeight += inBox.box_height;
			}
			//---------------------------------------------------------//
			if(box.box_width - keyBox.box_width >= Dexcess){		//ここはkeyBoxのoriginalWidthではないと思う
				//固さのチェック
				if( !(pdf_env.flexTH < (tmpHeight - box.box_height) / Dexcess) ){
					newLE = new PDFG2(manager, pdf_env);
					((PDFG2)newLE).tfe = originalTFE;//this.tfe;
					((PDFG2)newLE).decos = this.decos;
					this.decos.put("fold", "0");
					//maxWidthの変更 <-- しないかも
					System.out.println("G1 change to G2");
					change = true;
					flex = true;
					pdf_env.cutWidth = box.box_width - keyBox.box_width;
				}
				else						//このelse文いらない気がする
					flex = false;
			}
		}
		
		return flex;
	}
	
	
	public boolean optimizeH(float Dexcess, PDFValue box){
		boolean flex = false;
		
		return flex;
	}

	
	public TFE getNewChild(){
		return newLE;
	}
	
	
	public boolean changeORnot(){
		return change;
	}
	
	
	public void redoChange(){
		
	}
	
	
	@Override
	public String getSymbol() {
		return "G1";
	}

}