package supersql.codegenerator.PDF;

import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.extendclass.ExtList;

public class PDFManager extends Manager {
	
	private PDFEnv pdf_env;
	private PDFWriter writer;
	private PDFOptimizer optimizer;

	//追加10.14
	private ITFE tfe_info;
	private ExtList data_info;

	//コンストラクタ
	public PDFManager(PDFEnv pdf_env) {
		this.pdf_env = pdf_env;
		writer = new PDFWriter(pdf_env);
		optimizer = new PDFOptimizer(pdf_env);
	}

	@Override
	public void generateCode(ITFE tfe_info, ExtList data_info) {
		System.out.println("[PDFManager:generateCode]data_info = " + data_info);

		//追加10.14
		this.tfe_info = tfe_info;
		this.data_info = data_info;
		
		//追加12.13
		optimizer.set_tfeInfo(tfe_info);
		
		
		String filename = GlobalEnv.getoutfilename();
		
		if (filename == null) {
			filename = GlobalEnv.getfilename();
        	if (filename.indexOf(".sql")>0) {
        		filename = filename.substring(0, filename.indexOf(".sql")) + ".pdf";
        	} else if (filename.indexOf(".ssql")>0) {
        		filename = filename.substring(0, filename.indexOf(".ssql")) + ".pdf";
        	}
        }
		
		System.out.println(GlobalEnv.getoutfilename());
		pdf_env.createPDFfile(filename);


		pageReady();//ページを用意して初?設?

/*		
		tfe_info.work(data_info);
		
		
		boolean layoutChange;
		layoutChange = boxAdjust();
		
		//?イアウトが変?っていたらもう?度！　こ?だと?回だけ
		//boxAdjustにoptimizeSTARTが?って?のは問題か?
		if(layoutChange){
			tfe_info.work(data_info);
			boxAdjust();
		}
*/
		int layoutChange = 0;
		
		while(layoutChange != -1){
			switch(layoutChange){
				case 1:
					layoutChange = boxAdjust();
					break;
				case 2:
					tfe_info.work(data_info);
					layoutChange = boxAdjust();
					break;
				default:		//初めはここの処?
					tfe_info.work(data_info);
					layoutChange = boxAdjust();
					break;
			}
		}		
		
		pagePrint();
	}
	

	
	//ファイ?を用意す?メソッドを呼ぶ
	public void pageReady(){
		writer.page_ready();
	}

	
	public int boxAdjust(){
		int layoutChange;
		
		optimizer.initializeLabel();	
		
		System.out.println();
		PDFValue result = ((PDFTFE)tfe_info).getInstance();
		
		System.out.println();	
		System.out.println("====================");
		System.out.println("      Set Label     ");	
		System.out.println("====================");
		
		((PDFTFE)tfe_info).setLabel(result);		

		
		optimizer.initializeAdjust();
		
		System.out.println();	
		System.out.println("====================");
		System.out.println("    Adjusting Box   ");	
		System.out.println("====================");
		
		//折?畳みでラベ?を貼りなおす??1が返ってきて
		//貼りなおす必要がない??0が返ってく?
		layoutChange = optimizer.adjust(result);
		
		
		if(layoutChange == 0){

			System.out.println();	
			System.out.println("====================");
			System.out.println("  Start Optimizing  ");	
			System.out.println("====================");
			
			//?イアウトの変更があった??2が返ってきてもう?度Adjust
			//変更がない?合は-1が返ってきて出力へ
			layoutChange = optimizer.optimizeSTART(data_info, result);
			//layoutChange = optimizer.optimizeSTART(tfe_info, data_info, result);
		
		}
		
		return layoutChange;
	}
	
	//PDFファイ?にテキストBOXとBOXを書き出すメソッドを呼ぶ
	public void pagePrint(){
		
		System.out.println();	
		PDFValue result = ((PDFTFE)tfe_info).getInstance();
		
		//resultの分割　???は調整済み　反?子直下インスタンスの切?目でページ変え
		//AllPageに代?
		optimizer.divideResult(result);		
		//ここでも?ンクなどの?イアウト変更があったらメソッドの戻?値として真を返す

		
		ExtList outputResult = optimizer.getOutputResult();
		
		
		System.out.println();	
		System.out.println("====================");
		System.out.println("   Start Printing   ");	
		System.out.println("====================");

		writer.pagePrint(outputResult);
	}

	//ファイ?を閉じ?メソッドを呼ぶ
	@Override
	public void finish(){

		//System.out.println("[PDFManager:finish]table_le2 = " + table_le2);

		//generateCode内に移動
		//page_print();
		
		//writer側の?がいい気がす?
		//writer.closePDFfile();
		pdf_env.closePDFfile();

		/*
		 * System.out.println(filename);
		 * 
		 * try{ //出力先ディ?クト?指? filename =
		 * "/home/toyama/shinpei/ssql_java/package/bin/" + filename; /-test-*
		 * PrintWriter writer = new PrintWriter(new BufferedWriter(new
		 * FileWriter(filename))); writer.write(value.pdf); writer.close();
		 * -test-/ } catch(Exception e){ System.out.println("Exception: " + e); }
		 *  
		 */

	}
}