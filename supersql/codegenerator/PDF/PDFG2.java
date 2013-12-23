package supersql.codegenerator.PDF;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.codegenerator.TFE;
import supersql.extendclass.ExtList;

public class PDFG2 extends Grouper implements PDFTFE {

	private PDFEnv pdf_env;
	
	//追加10.17
	private PDFValue value;
	
	private ExtList columns;		//各インスタンスの各列の要素数  注：クラス変数では無理
	
	//移動10.28  元々はworkメソッド内
	private float tmp_width;
	private float tmp_height;
	private float box_widthSUM;		//折り畳みの変数　幅の合計
	private float box_heightMAX;	//折り畳みの変数　高さの最大値
	private float fold;				//折り畳みの変数　何cm、何個で折り畳むかを格納			
	private int topID;				//widthをre_setする時inListの何番目からか

	
	private ExtList maxWidths;			//このG2の各列最大幅
	
	private int columnNum;				//各インスタンスの折り畳んだ回数
	private ExtList columnWidths;		//各インスタンスの各列最大幅

	//----- setLevelメソッドで幅調節のSuffix用 -----//
	int repeatNum = 0;
	
	
	//レイアウト変換用
	TFE newLE;
	boolean change = false;
	
	
	//コンストラクタ
	public PDFG2(Manager manager, PDFEnv pdf_env) {
		this.pdf_env = pdf_env;
		//追加10.30  折り畳み用
		maxWidths = new ExtList();
	}

	@Override
	public String work(ExtList data_info) {
		//追加10.14 メソッド内に移動10.24
		float box_width = 0;
		float box_height = 0;
//		int level = pdf_env.level;
		
//		int fold_num;

		box_widthSUM = 0;
		box_heightMAX = 0;
		fold = 0;
		
		columnNum = 0;
		
		
		
		System.out.println("");
		System.out.println("------- G2 -------");
//		System.out.println("[PDFG2:work]tfe_info = " + makele0());
		System.out.println("[PDFG2:work]data_info = " + data_info);

		System.out.println("++++ G2でvalueをnewします");
		//追加10.17
		value = new PDFValue("G2");
		
		columns = new ExtList();
		columnWidths = new ExtList();

		int i;

		setDataList(data_info);
		setDecoration();

		///////////////////////////////
		//////////////////(G2 1)のとき
		///////////////////////////////

		if (tfe instanceof Attribute) {
			System.out.println("[PDFG2:work]tfe is Attribute");
			System.out.println("number of data = " + data_info.size());


			for (i = 0; i < data_info.size(); i++) {
				//追加10.27　右下方向のみに深くならない構造の兄弟を判別するポイント
//				pdf_env.level_max = level;

				
				//追加10.24 追加10.28折り畳み対応　兄弟に同じ数字を割り振る処理
//				pdf_env.level++;
////				pdf_env.level += columnNum;
//				if(pdf_env.level > pdf_env.level_max ){
//					pdf_env.level_max = pdf_env.level;
//				}

				worknextItem();
				
				//追加10.24 兄弟に同じ数字を割り振る処理　変更10.27
				//pdf_env.level--;
//				pdf_env.level = level;
				
				System.out.println("gggggggg "+pdf_env.tmp_height);
				
				System.out.println("tttttt box_height = " +box_height);
				
				//追加10.28　折り畳み判断
				if(fold != 0){
					if(Fold_or_Not(box_width, box_height, fold)){
						System.out.println("foldfoldfoldfoldfold");
						box_width = 0;
						box_height = 0;
					}
				}
				
				
				//追加10.17
				//ここでtmp_width,heightを足しこんでいく
				tmp_width = pdf_env.tmp_width;
				if (box_width < tmp_width)
					box_width = tmp_width;
				box_height += pdf_env.tmp_height;
				
				
				//追加10.30
				//checkMaxWidth(pdf_env.tmp_width);
				checkMaxWidth(box_width);
				
				
				System.out.println("++++ G2でAttをsetします");
				//追加10.17 valueのセット
				value.inList.add(((PDFTFE)tfe).getInstance());
				
				//移動

			}

			
		}

		//////////////////////////////////////////
		//////////////////(G2 (C1 1 2))とかのとき
		//////////////////////////////////////////
		else {
			System.out.println("[PDFG2:work]tfe is Operator");


			//tableの値があるだけ、処理をする
			for (i = 0; i < data_info.size(); i++) {
				//追加10.27　右下方向のみに深くならない構造の兄弟を判別するポイント
//				pdf_env.level_max = level;


				//追加10.24 追加10.28折り畳み対応　 兄弟に同じ数字を割り振る処理
//				pdf_env.level++;
////				pdf_env.level += columnNum;
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
				tmp_width = pdf_env.tmp_width;
				if (box_width < tmp_width)
					box_width = tmp_width;
				box_height += pdf_env.tmp_height;

				
				//追加10.30
				//checkMaxWidth(pdf_env.tmp_width);
				checkMaxWidth(box_width);
				
				
				System.out.println("++++ G2でリストをsetします");
				//追加10.17 valueのセット
				value.inList.add(((PDFTFE)tfe).getInstance());
				
				
				//移動

			}

			
		}

		///////////////////////////////

		//追加10.24  変更10.28  メソッド化して折り畳みと共有
		//for(i=0; i<value.inList.size(); i++){
		//	PDFValue re_set = (PDFValue)value.inList.get(i);
		//	re_set.box_width = box_width;
		//}
		re_setWidth(topID, value.inList.size(), box_width);

		
		//追加10.28  折り畳み時はbox_width,heightの調整
		if(fold != 0){
			box_width += box_widthSUM;//下のこのインスタンスの幅取得でいいかも
			if(box_height < box_heightMAX){
				box_height = box_heightMAX;
			}
		}

		
		//おそらく下のようにただ追加するだけでいい
//		if(columns.size() == 0)
//			columns.add(Integer.toString(value.inList.size()));
		//追加11.2  大事
		columns.add(Integer.toString(value.inList.size()));

		
		//---------- このインスタンスの幅取得 ------------//
		float columnWidth = 0;
		
		for(i=0; i<columnWidths.size(); i++)
			columnWidth += Float.parseFloat((String)columnWidths.get(i));
		box_width = columnWidth;
		//------------------------------------------------//
		
		//変更　11.29
		value.box_width = box_width;
		value.box_height = box_height;
		
		value.originalWidth = box_width;	//C2のレイアウト変換に使用
		value.originalHeight = box_height;	//C1のレイアウト変換に使用
		
		
		value.columnNum = columnNum;
		value.columns = columns;
		value.columnWidths = columnWidths;
		
		value.fold = fold;
		value.tfeID = getId();
		
		pdf_env.tmp_width = box_width;
		pdf_env.tmp_height = box_height;
		
		
		
		//-------- このG2の各列最大幅を常に監視 ----------//
		float maxWidth;
		
		if( maxWidths.size() < columns.size() ){
			int diff = columns.size() - maxWidths.size();
			for(i=0; i<diff; i++)
				maxWidths.add("0");
		}
		for(i=0; i<columnWidths.size(); i++){
			maxWidth = Float.parseFloat((String)maxWidths.get(i));
			columnWidth = Float.parseFloat((String)columnWidths.get(i));
			if(maxWidth < columnWidth){
				System.out.println("yyyyyyyyyyyyyy re-set");
				maxWidths.set(i, Float.toString(columnWidth));
			}
		}
		//------------------------------------------------//
		return null;
		
	}
	
	
	
	
	//追加10.28　折り畳み判断
	public boolean Fold_or_Not(float box_width, float box_height, float fold){
		boolean fold_or_not = false;
		tmp_height = box_height;
		tmp_height += pdf_env.tmp_height;
		if(tmp_height>fold){
			Fold(box_width, box_height);
			columns.add(Integer.toString(value.inList.size()));
			//1つ前の要素、折り返す直前の要素に情報を代入したい
			columnNum++;
			fold_or_not = true;
		}
		return fold_or_not;
	}
	
	//追加10.28  折り畳み
	public void Fold(float box_width, float box_height){	
		box_widthSUM += box_width;//他と大小比較が必要　checkMaxWidth()でいいのかも
		tmp_height = box_height;
		if(tmp_height > box_heightMAX){
			box_heightMAX = tmp_height;
		}
		//この時inListにsetされているvalueにはbox_widthをre-set
		re_setWidth(topID, value.inList.size(), box_width);
		topID = value.inList.size();
	}
	
	//追加10.30
	public void checkMaxWidth(float box_width){
		float columnTmpWidth;
		
		if(columnNum >= columnWidths.size())
			columnWidths.add("0");
		columnTmpWidth = Float.parseFloat((String)columnWidths.get(columnNum));
		if(columnTmpWidth < box_width)
			columnWidths.set(columnNum, Float.toString(box_width));
	}
	
	public void re_setWidth(int start, int end, float set_width){
		int i;
		for(i=start; i<end; i++){
			PDFValue re_set = (PDFValue)value.inList.get(i);
			re_set.box_width = set_width;
		}
	}
	
	public void setDecoration(){
		//折り畳み
		if(decos.containsKey("fold")){
			fold = Float.parseFloat(decos.get("fold").toString());
		}

	}
	
	public PDFValue getInstance(){
		System.out.println("++++ G2をsetしました");
		return value;
	}

	
	public void setLabel(PDFValue result) {

		int i;
		PDFValue instance;
		
		//---- rowNumをrepeatNumとしてクラス変数に変更 ----//
		//int rowNum = 0;				//反復出力した回数　レベルの接尾語「何行目」
		int columnNum = 0;				//折り畳み対応　レベルの接尾語「何列目」
		
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
		
		
		//注　C2直下のG2直下のC2で幅を揃えようとするには、
		//    ここで親がC2だったらmaxをカウントアップしなければいいはず
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
			

			
			//------------- 折り畳んだらSuffixHの数字を増やす ----------------//
			if( i == Integer.parseInt((String)result.columns.get(columnNum)) ){
				columnNum++;
			}
			String columnStr = Integer.toString(columnNum);
			if(labelSuffixH.equals("null"))
				pdf_env.labelSuffixH = "-" + columnStr;
			else
				pdf_env.labelSuffixH = labelSuffixH + "-" + columnStr;
			//----------------------------------------------------------------//
			
			
			//------------- インスタンス毎にSuffixVの数字を増やす ------------//
			repeatNum++;
			//rowNum = i;		//多分こっちでOK
			String rowStr = Integer.toString(repeatNum);
			if(labelSuffixV.equals("null"))
				pdf_env.labelSuffixV = "-" + rowStr;
			else
				pdf_env.labelSuffixV = labelSuffixV + "-" + rowStr;
			//----------------------------------------------------------------//
			
	
			
			instance = (PDFValue)result.inList.get(i);
			((PDFTFE)tfe).setLabel(instance);
			
				
			//いらないっぽい
			//pdf_env.labelH = labelH;
			
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
			
			float heightSUM = 0;
			float maxWidth = 0;
			int columnNum = 0;
			
			this.columns.clear();
			this.columnWidths.clear();
			
			for(i=0; i<check.inList.size(); i++){
				PDFValue inBox = (PDFValue)check.inList.get(i);
			
				if(inBox.box_height > check.fold){
					System.out.println("ERROR: one Box's height is over the folding height");
					System.exit(0);
				}
				
				heightSUM += inBox.box_height;
				
				if(heightSUM > check.fold){
					this.columns.add(Integer.toString(i));
					this.columnWidths.add(Float.toString(maxWidth));
					
					maxWidth = 0;
					heightSUM = inBox.box_height;
					columnNum++;
				}
				
				if(maxWidth < inBox.box_width)
					maxWidth = inBox.box_width;				
			}
		
			this.columns.add(Integer.toString(i));
			this.columnWidths.add(Float.toString(maxWidth));
			
			this.columnNum = columnNum;		
			
			check.columns = this.columns;
			check.columnWidths = this.columnWidths;
			check.columnNum = this.columnNum;
		}
		
	}
	
	
	public boolean optimizeW(float Dexcess, PDFValue box){
		boolean flex = false;
		int i;
		
		PDFValue inBox = (PDFValue)box.inList.get(0);
		
		//------------ 最上位で折り畳んでいる場合はinBoxとDexcessを上書き ------------//
		if( box.labelH.equals("0") && box.fold!=0 ){
			float tmpWidth;
			float maxColumnWidth = 0;
			for(i=0; i<box.columnNum+1; i++){
				tmpWidth = Float.parseFloat((String)box.columnWidths.get(i));
				if(maxColumnWidth < tmpWidth){
					maxColumnWidth = tmpWidth;
					inBox = (PDFValue)box.inList.get(Integer.parseInt((String)columns.get(i))-1);
				}
			}
			Dexcess = maxColumnWidth - pdf_env.widthPaper + pdf_env.paddingPaper_H*2;
		}
		//----------------------------------------------------------------------------//

		flex = ((PDFTFE)tfe).optimizeW(Dexcess, inBox);
		//直下の子が変わっているか調べて、変わっていたら各子要素をgetNewChildで取ってきてセット
		if( ((PDFTFE)tfe).changeORnot() ){	
			//newLE = new PDFG2(manager, pdf_env);
			//((PDFG2)newLE).tfe = ((PDFTFE)tfe).getNewChild();	//これではTFE型でセットされる。それで処理が出来るか？
			this.tfe = ((PDFTFE)tfe).getNewChild();
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
		return "G2";
	}

}