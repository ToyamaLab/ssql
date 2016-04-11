package supersql.embed;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import supersql.common.Log;
public class TestServlet extends HttpServlet {
	
  private static final long serialVersionUID = 8021503235844232672L;

  @Override
public void doGet(HttpServletRequest req, 
                      HttpServletResponse res) 
                          throws ServletException, IOException {
	  
	  //work(req,res);

	  	
	    // ContentTypeを設定
//	    res.setContentType("text/xml; charset=UTF8");
	    res.setCharacterEncoding("UTF8");
	    req.setCharacterEncoding("Shift-JIS");
	    
	    // 出力用PrintWriterを取得
	    PrintWriter out = res.getWriter();
	    
		Date d1 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
		String today = sdf.format(d1);
		Log.info("今は:"+today);
		
	    String msg ="";
//	    msg = "<script type=\"text/javascript\"> \n";
		msg += "testdrag = new DragDrop(\"testdrag1\",\"testdrop1\");\n";
		msg += "testdrop= new YAHOO.util.DDTarget(\"testdrop1\", \"testdrop1\");\n";
		msg += "drop2007590222590567 = new DragDrop(\"http://localhost:8080/tab/car.sql+ca.id=31\", \"car\");\n";

		msg += "<end of script>\n";
		//		msg += "</script>\n";

		msg += "<div id=\"http://localhost:8080/tab/car.sql+ca.id=31\"  class=\"carname\" >レジェンド</div>";
		msg += "<div class=\"menu\" id =\"testdrag1\">testfromhtml</div>\n";
//		msg += "<div class=\"drop1\" id=\"testdrop1\" >test 1</div>\n";
//		msg += "<div id=\"output\"></div>\n";
		
		out.println(msg);
		System.out.println(msg);
		out.flush();
	    out.close();
  }
  
  protected void work(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	
    PrintWriter out = response.getWriter();
	  out.println("hogehoge");	  
	  return;
  }
}
