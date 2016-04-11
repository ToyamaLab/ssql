package supersql.invoke;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvokeServlet extends HttpServlet {

	/**
	 * <code>serialVersionUID</code> のコメント
	 */
	private static final long serialVersionUID = 8021503235844232672L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String querystring = req.getQueryString();

		// "stop" for destroy servlet
		if (querystring.equals("stop")) {
			res.setContentType("text/html");
			res.setHeader("Pragma", "no-cache");
			OutputStream out = res.getOutputStream();

			PrintStream print = new PrintStream(out);
			print
					.println("<HTML><HEAD><TITLE>Stop Servlet</TITLE></HEAD><BODY>");
			print.println("Stop Servlet Invoke...");
			print.println("</BODY></HTML>");
			//System.setOut(new PrintStream(out));
			out.close();
			this.destroy();
			return;
		}

		InvokeEnv ienv = new InvokeEnv(req);

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
		InvokeSSQL issql = new InvokeSSQL(print, ienv, 0);

		//System.setOut(new PrintStream(out));
		out.close();

	}

}