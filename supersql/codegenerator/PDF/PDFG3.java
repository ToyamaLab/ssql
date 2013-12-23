package supersql.codegenerator.PDF;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;
import supersql.extendclass.ExtList;

public class PDFG3 extends Grouper {

	private PDFManager manager;

	private PDFEnv pdf_env;

	//コンストラクタ
	public PDFG3(Manager manager, PDFEnv pdf_env) {
		this.manager = (PDFManager) manager;
		this.pdf_env = pdf_env;
	}

	@Override
	public String work(ExtList data_info) {
		System.out.println("\n------- G3 -------");
		System.out.println("[PDFG3:work]tfe_info = " + makele0());
		System.out.println("[PDFG3:work]data_info = " + data_info);

		PDFModifier modifier = new PDFModifier();

		int i;
		boolean fold = false;
		int alternate = 1;

		//オペレータ側
		modifier.clean1();
		/*
		 * if( le2.car() instanceof AddedInfo ){ modifier.get_modifier1(
		 * le2.car() ); limit = modifier.limit; modifier.set_modifier1( writer );
		 * //writer.separate = modifier.separate; }
		 */
		//要素側
		modifier.clean2();
		/*
		 * if( (le2.cdr()).car() instanceof AddedInfo ) modifier.get_modifier2(
		 * (le2.cdr()).car() );
		 */

		setDataList(data_info);

		///////////////////////////////
		//////////////////(G3 1)のとき
		///////////////////////////////

		if (tfe instanceof Attribute) {
			System.out.println("[PDFG3:work]tfe is Attribute");
			System.out.println("number of data = " + data_info.size());

			//cast????
			//	    ExtList test = ((Attribute)tfe).get_DecorateList();
			//	    System.out.println("[PDFG2:work]decorate = " + test );
			//

			/*
			 * //////////////////////////tableから値をとってくる
			 * //le.car()を調べる(table位置を知るため) Object o = le.car(); //String s =
			 * o.toString(); //i_tmp = Integer.valueOf(s).intValue();
			 * //System.out.println("[PDFG3:work]i_tmp = " + i_tmp);
			 * 
			 * 
			 * //tableから文字列のリストを変数tabに取得する List tab = new List(); tab =
			 * (List)table.car();
			 * 
			 * System.out.println("[PDFG3:work]tab = " + tab);
			 */

			for (i = 0; i < data_info.size(); i++) {

				//２度手間 ExtListにメソッドでまとめた方がいいかも
				//Object data_List = data_info.get(i);
				//System.out.println(data_List);
				//Object data = ((ExtList)data_List).get(0);
				//System.out.println(data);

				pdf_env.tate = 1;////////////
				pdf_env.yoko = 1;////////////
				
				worknextItem();
				

				//String s1 = data.toString();

//del

				//出力してページを閉じ,新しいページを開く
				if (hasMoreItems()) {
//					manager.pagePrint();
					//modifier.page_clean(writer);
					manager.pageReady();
				}

				alternate++;
			}
		}

		//////////////////////////////////////////
		//////////////////(G3 (C1 1 2))とかのとき
		//////////////////////////////////////////
		else {
			System.out.println("[PDFG3:work]tfe is Operator");

			//現在のx,y座標を1にする
			//writer.x_back = 1;
			//writer.y_back = 1;

			//////////////////////////////
			//現在のtate,yokoを退避
			//tate_tmp = writer.tate_back;
			//yoko_tmp = writer.yoko_back;

			//tableの値があるだけ、処理をする
			for (i = 0; i < data_info.size(); i++) {
				//System.out.println("le2.cdr() = " + le2.cdr());
				//System.out.println("le2.car() = " + le2.car());

				//繰り返し対応するオペレータのworkへ
				//Object tmp2 = le2.car();

				pdf_env.alternate = alternate;

				//tfe.work( (ExtList)data_info.get(i) );

				pdf_env.pre_operator = getSymbol();
				worknextItem();

				///////11.25 折り畳みの前に移動
				//３重反覆の真ん中で折り畳むとちょっと問題
				////解決策はlimitになる毎に退避して前のと比較する////
				//if( fold != true )
				//    tate_inside = tate_inside + writer.tate_back;

				///////////////////////////////
				//退避していたtate,yokoを戻す
				//writer.tate_back = tate_tmp;
				//writer.yoko_back = yoko_tmp;

				//if( yoko_inside < writer.yoko_back )
				//    yoko_inside = writer.yoko_back;

				/*
				 * //27//折り畳んだものがG1だったりした時これは必須になってくる if( fold_now == true || (
				 * fold == true && !(e2.hasMoreElements()) ) ){ yoko_tmp =
				 * yoko_tmp + yoko_inside; yoko_inside = 0; fold_now = false; }
				 */
				//////////////////////
				//writer.tate_back++;
				//if( fold == true )//25
				//    tate_inside = tate_inside + tate_fold;//25
				//else//25
				//    tate_inside = tate_inside + writer.tate_back;
				//出力してページを閉じ,新しいページを開く
				if (hasMoreItems()) {
//					manager.pagePrint();
					//modifier.page_clean(writer);
					manager.pageReady();
				}

				alternate++;

			}

			if (fold == true) {
				//writer.y_back = y_tmp + limit - 1;
				//tate_inside = tate_inside + tate_fold;//25
			}
		}
		/*
		 * /////////////////////////////// writer.tate_back = tate_inside;
		 * 
		 * if( fold == false ) writer.yoko_back = yoko_inside; else
		 * writer.yoko_back = yoko_tmp;
		 */
		return null;

	}

	/*
	 * public void createSchema(List table, List le, List le1, List le2, List
	 * le3){ }
	 */

	@Override
	public String getSymbol() {
		return "G3";
	}

}