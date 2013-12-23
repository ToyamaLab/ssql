package supersql.codegenerator.PDF;

import java.util.Vector;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.extendclass.ExtList;

public class PDFC3 extends Connector {

	private PDFManager manager;

	private PDFEnv pdf_env;

	//コンストラクタ
	public PDFC3(Manager manager, PDFEnv pdf_env) {
		this.manager = (PDFManager) manager;
		this.pdf_env = pdf_env;
	}

	//C1のworkメソッド
	@Override
	public String work(ExtList data_info) {
		System.out.println("");
		System.out.println("------- C3 -------");
		System.out.println("[PDFC3:work]tfe_info = " + makele0());
		System.out.println("[PDFC3:work]data_info = " + data_info);

		Vector vector_local = new Vector();
		PDFModifier modifier = new PDFModifier();

		int i;
		int y_default; //y_tmp
		int tate_tmp;
		int tate_max = 1;
		int yoko_inside = 0;
		int lap = 0;

		//このC3WORKのデフォルトのy座標を退避
		y_default = pdf_env.y_back;
		//y_max = y_default;

		/*
		 * //(C3 ○ ○ ○ …)を調べる int number = 0; Enumeration e = le.elements();
		 * while(e.hasMoreElements()){ e.nextElement(); number++; }
		 * System.out.println("number = " + number);
		 * 
		 * Enumeration e1 = le.elements(); // ※もう１回初めから!? Object o[] = new
		 * Object[number]; for(i=0;i <number;i++){ o[i] = e1.nextElement();
		 * System.out.println("[PDFC3:work]o[i] = " + o[i]); }
		 */

		/*
		 * modi //le2も同様に調べる //le2 = (List)(le2.car());//余計な括弧を取り除く Object modi =
		 * le2.car(); //連結子のオペレータにつく装飾 中括弧などでまとめて指定する時に使用 if( modi instanceof
		 * AddedInfo ){ modifier.clean2(); modifier.get_modifier2(modi);
		 * ope_modi = true; } le2 = le2.cdr();//連結子のオペレータにつく装飾を取り除く Enumeration
		 * e_modi = le2.elements(); Object o_modi[] = new Object[number];
		 * for(i=0;i <number;i++){ o_modi[i] = e_modi.nextElement();
		 * System.out.println("[PDFC3:work]o_modi[i] = " + o_modi[i]); }
		 */

		setDataList(data_info);

		///////////////////////////////////////////////////////////////////////
		for (i = 0; i < tfeItems; i++) {
			ITFE tfe = (ITFE) tfes.get(i);

			////////////対象がリストでないとき
			if (tfe instanceof Attribute) {
				System.out.println("[PDFC3:work]tfe is Attribute");

				//modi if( ope_modi == false )
				modifier.clean2();
				//modi if( o_modi[i] instanceof AddedInfo )
				//modi modifier.get_modifier2(o_modi[i]);//オペランドの装飾情報のメソッド

				//String s_tmp = o[i].toString();
				//int i_tmp = Integer.valueOf(s_tmp).intValue();
				//System.out.println("[PDFC3:work]i_tmp = " + i_tmp);

				//tableから文字列を変数valに取得する
				//List tab = table;

				//for文の終了条件を i_tmp-1 から i に変更
				//for(j=0; j<i; j++)
				//    tab = tab.cdr();

				//Object val = tab.car();
				//table = table.cdr();

				//Object data = data_info.get(i);
				//Object data = ((ExtList)data_List).get(0);

				worknextItem();
				

				//String s = data.toString();

//del
				
/////			vector_local.addElement(value);		////////////


				//lap++;
				//?? table = table.cdr();

				//もしこれがC3最後の要素で,flagがtrueならば→ベクトルを配列へ
				//if( i == number-1 && writer.flag == true ) {
				//    writer.str[] = new PDFValue[writer.vector.size()];
				//    writer.vector.copyInto(str);
				//}

				//出力してページを閉じ,新しいページを開く
				//最後の要素ではしない
				if (hasMoreItems()) {
//					manager.pagePrint();
					manager.pageReady();
				}
			}

			////////////対象がリストの時
			else {
				System.out.println("[PDFC3:work]tfe is instance of Operator");

				//もしこれがC3最後の要素で,flagがtrueならばflagはtrue
				//他の場合はflagをfalseにする
				//if( i == number-1 && writer.flag == true )
				//    writer.flag = true;
				//else
				//    writer.flag = false;

				System.out.println("lap=" + lap);
				//対象となるリストの前にある要素の分だけtableのcdrをとる
				//for(i=0; i<lap; i++)
				//    table_list = table_list.cdr();

				//////////////////////
				//リストの処理をする前にデフォルトのy座標に戻しておく
				pdf_env.y_back = y_default;

				////////////////////
				//if(ope.toString() == "C1" || ope.toString() == "C2")
				//    size_of_connector = o[i].size() - 1;

				//対応するオペレータのworkメソッドへ 注:le2の引数これじゃ駄目!!
				//tfe.work(data_info);

				pdf_env.pre_operator = getSymbol();
				worknextItem();

				/*
				 * もともと if(ope.toString() == "G1" || ope.toString() == "G2"){
				 * table = table.cdr();
				 * System.out.println("??????????????????????");
				 * //writer.x_back++; // flag = true;
				 * }////////////////////////////////// else if(ope.toString() ==
				 * "C1" || ope.toString() == "C2"){ for(j = 0; j <
				 * ((List)o[i]).size()-1; j++) table = table.cdr();
				 * System.out.println("######################"); }
				 */

				/*
				 * int cdr_num = 0; cdr_num = cdr_count((List)o[i], cdr_num);
				 * for( j=0; j <cdr_num; j++) table = table.cdr();
				 */

				yoko_inside = yoko_inside + pdf_env.yoko_back;

				//if(flag == true)
				//    writer.y_back = y_tmp;

				//リストの処理が終ったら
				tate_tmp = pdf_env.tate_back;
				if (tate_max < tate_tmp)
					tate_max = tate_tmp;
				//y_tmp = writer.y_back;
				//if( y_max < y_tmp )
				//    y_max = y_tmp;

				//処理していたリストがC2又はG2ならばx座標をプラス
				//if( ope.toString() == "C2" || ope.toString() == "G2") {
				//    writer.x_back++;
				//    System.out.println("************* **************");
				//}

				//出力してページを閉じ,新しいページを開く
				//ただし最後の要素ではしない
				if (hasMoreItems()) {
//					manager.pagePrint();
					manager.pageReady();
				}
			}

		}
		///////////////////////////////////////////////////////////////////////////
		//このC3のworkメソッドが終ったところで,
		pdf_env.tate_back = tate_max;

		System.out.println("***** tate_max = " + tate_max);

		PDFValue[] array = new PDFValue[vector_local.size()];
		vector_local.copyInto(array);

		//このC3リストのtate_maxをこのリストの要素のtateにする
		//for(i=0; i<vector_local.size(); i++)
		//    ((PDFValue)((writer.vector.elementAt(array[i].element)))).tate =
		// tate_max;

		///////////////////////////////
		pdf_env.y_back = y_default + tate_max - 1;
		//writer.y_back = y_max;

		pdf_env.yoko_back = yoko_inside;
		return null;

	}

	/*
	 * public int cdr_count(List le_tmp, int cdr_num){
	 * if(le_tmp.car().toString() == "G1"||le_tmp.car().toString() ==
	 * "G2"||le_tmp.car().toString() == "G3") cdr_num++; else{ Enumeration e3 =
	 * le_tmp.cdr().elements(); while( e3.hasMoreElements() ){ Object le_element =
	 * e3.nextElement(); if( le_element instanceof List ) cdr_num =
	 * cdr_count((List)le_element, cdr_num ); else { cdr_num++;
	 * System.out.println("element = "+le_element); System.out.println("num =
	 * "+cdr_num); } } } return cdr_num; }
	 */

	/*
	 * public void createSchema(List table, List le, List le1, List le2, List
	 * le3){ }
	 */

	/*
	 * public String toString(){ return "C3"; }
	 */

	@Override
	public String getSymbol() {
		return "C3";
	}

}