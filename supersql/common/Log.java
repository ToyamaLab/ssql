package supersql.common;

import javax.smartcardio.CommandAPDU;

import supersql.codegenerator.CodeGenerator;

public class Log {

	private static int flag = 0;
	private static int infoflag = 1;

	private static int dotcount = 0;

	private static StringBuffer buf = new StringBuffer();

	public static void setLog(int f) {
		flag = f;
	}

	public static void setinfoLog(int f) {
		infoflag = f;
	}

	public static void out(Object o) {

		switch (flag) {
		case 0:
			// do nothing
			break;
		case 1:
			// ɸ����Ϥؽ���
			System.out.println(o.toString());
			break;
		case 2:
			// ɸ�२�顼�ؽ���
			Log.err(o.toString());
			break;
		case 3:
			// ɸ����Ϥ˥ɥåȤ����
			if (dotcount >= 600) {
				dotreturn();
			}
			if (dotcount % 10 == 9) {
				System.out.print(".");
			}
			dotcount++;
			break;
		case 10:
			// buffer�˳�Ǽ
			buf.append(o.toString()).append('\n');
			break;
		}

	}

	private static void dotreturn() {
		if (flag == 3 && dotcount >= 9) {
			System.out.print("\n");
			dotcount = 0;
		}
	}

	public static void initBuffer() {
		buf = new StringBuffer();
	}

	public static String getBuffer() {
		return buf.toString();
	}

	// ɸ��Ū��ư��ǽ��Ϥ������ (-quiet���ˤϽФʤ�)
	public static void info(Object o) {
		// add 20141204 masato for ehtml
		if (CodeGenerator.getMedia() != null
				&& CodeGenerator.getMedia().equalsIgnoreCase("ehtml")) {
			return;
		}
		switch (infoflag) {
		case 0:
			// do nothing
			break;
		case 1:
			// ɸ����Ϥؽ���
			dotreturn();
			System.out.println(o.toString());
			break;
		case 2:
			// ɸ�२�顼�ؽ���
			Log.err(o.toString());
			break;
		case 10:
			// buffer�˳�Ǽ
			buf.append(o.toString()).append('\n');
			break;
		}
	}

	public static void ehtmlInfo(Object o) {
		// add 20141204 masato for ehtml
			System.out.println(o.toString());
	}
	
	// added by goto 20130415
	public static void i(Object o) {
		info(o);
	}

	public static void o(Object o) {
		out(o);
	}

	public static void err(Object o) {
		System.err.println(o.toString());
		GlobalEnv.errorText += o.toString();
	}

	public static void e(Object o) {
		err(o);
	}
}