package supersql.invoke;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import supersql.cache.CacheData;

public class CacheInvokeServlet extends HttpServlet {

	/**
	 * <code>serialVersionUID</code> のコメント
	 */
	private static final long serialVersionUID = 7659912749361655506L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// for MyTime
		long start_time = System.currentTimeMillis();

		InvokeEnv ienv = new InvokeEnv(req);
		
		// "stop" for destroy servlet
		if (ienv.getQuerystring().equals("stop")) {
			CacheData.resetCacheAll();
			res.setContentType("text/html");
			res.setHeader("Pragma", "no-cache");
			OutputStream out = res.getOutputStream();

			PrintStream print = new PrintStream(out);
			print
					.println("<HTML><HEAD><TITLE>Stop Servlet</TITLE></HEAD><BODY>");
			print.println("Stop Servlet CacheInvoke...");
			print.println("</BODY></HTML>");
			//System.setOut(new PrintStream(out));
			out.close();
			this.destroy();
			return;
		}

		// "reset" for cache reset
		if (ienv.getQuerystring().equals("reset")) {
			CacheData.resetCacheAll();
			res.setContentType("text/html");
			res.setHeader("Pragma", "no-cache");
			OutputStream out = res.getOutputStream();

			PrintStream print = new PrintStream(out);
			print
					.println("<HTML><HEAD><TITLE>Reset Cache</TITLE></HEAD><BODY>");
			print.println("Reset Cache");
			print.println("</BODY></HTML>");
			//System.setOut(new PrintStream(out));
			out.close();
			return;
		}

		// "print" for cache print
		if (ienv.getQuerystring().equals("print")) {
			res.setContentType("text/html");
			res.setHeader("Pragma", "no-cache");
			OutputStream out = res.getOutputStream();

			PrintStream print = new PrintStream(out);
			print.println("<HTML><HEAD><TITLE>Cache Data</TITLE></HEAD>");
			print.println("<BODY><pre>");
			print.println(CacheData.cacheToString());
			print.println("</pre></BODY>");
			print.println("</HTML>");
			//System.setOut(new PrintStream(out));
			out.close();
			return;
		}

		if (ienv.getFilename().equals("")) {
			res.setContentType("text/html");
			res.setHeader("Pragma", "no-cache");
			OutputStream out = res.getOutputStream();
			PrintStream print = new PrintStream(out);
			print
					.println("<HTML><HEAD><TITLE>InvokeServlet Error</TITLE></HEAD><BODY>");
			print.println("filename is missing!");
			print.println("</BODY></HTML>");
			//System.setOut(new PrintStream(out));
			out.close();
			return;
		}

		res.setContentType("text/html");
		res.setHeader("Pragma", "no-cache");
		OutputStream out = res.getOutputStream();

		PrintStream print = new PrintStream(out);
		InvokeSSQL issql = new InvokeSSQL(print, ienv, 7);

		//System.setOut(new PrintStream(out));
		out.close();


		// for MyTime
		long end_time = System.currentTimeMillis();
		long process_time = end_time - start_time;
		this.log("Result time = " + process_time + " msec.");

	}



}