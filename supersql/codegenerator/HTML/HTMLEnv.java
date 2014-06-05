package supersql.codegenerator.HTML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import supersql.codegenerator.ITFE;
import supersql.codegenerator.LocalEnv;
import supersql.common.GlobalEnv;
import supersql.common.Log;

public class HTMLEnv extends LocalEnv {
	private static Document htmlDocument;
	public static Vector<String> writtenClassId;
	public static Vector<String> notWrittenClassId = new Vector<String>();
	public static int gLevel = 0;
	public static String fileName;
	public static String outFile;
	public static String linkOutFile;
	public static String nextBackFile = new String();
	public static String outDir;
	public static int countFile;
	/** @deprecated use the static HTMLEnv functions instead **/
	@Deprecated
	public static StringBuffer code;
	/** @deprecated use the HTMLEnv.addStyle(String style) function instead**/
	@Deprecated
	public static StringBuffer css;
	private static int IDCounter = 0; // add oka
	private static int IDOld = 0; // add oka
	@Deprecated
	/** @deprecated use the HTMLUtils.processDecos(Element, DecorateList) instead **/
	public static StringBuffer cssFile = new StringBuffer();
	@Deprecated
	/** @deprecated use the HTMLUtils.processDecos(Element, DecorateList) instead **/
	public static StringBuffer jsFile = new StringBuffer(); // added by goto 20130703
	@Deprecated
	/** @deprecated use the HTMLUtils.processDecos(Element, DecorateList) instead **/
	public static StringBuffer cssjsFile = new StringBuffer(); // added by goto
														// 20130703
	@Deprecated
	public static String tableBorder = "1";
	public static boolean embedFlag = false;
	public static int embedCount = 0;
	@Deprecated
	public static int haveClass = 0;

	// for ajax
	public static String ajaxQuery = new String();
	public static String ajaxCond = new String();
	public static String ajaxtarget = new String();
	public static int inEffect = 0;
	public static int outEffect = 0;
	public static boolean hasDispDiv = false;

	// for drag
	public static StringBuffer script = new StringBuffer();
	public static int scriptNum = 0;
	public static boolean draggable = false;
	public static String dragDivId = new String();

	// for panel
	public static boolean isPanel = false;
	// tk end//////////////////////////////////////////////////////

	@Deprecated
	/** @deprecated use the static HTMLEnv functions instead **/
	public static StringBuffer header;
	@Deprecated
	/** @deprecated use the static HTMLEnv functions instead **/
	public static StringBuffer footer;
	public static boolean foreachFlag;
	public static int linkFlag;
	public static String linkUrl;

	// outline鐃緒申鐃緒申呂鐃緒申鐃緒申匹鐃緒申鐃緒申離侫薀逸申鐃�
	protected static boolean OutlineMode = false;
	private static boolean isFormItem;
	private static String formItemName;
	private static boolean selectFlg;
	private static String formValueString;
	private static boolean selectRepeat;

	// global form item number : t1,t2,t3...
	public static int formPartsNumber = 1;
	public static String formPartsName = null;

	// global form number : 1,2,3...
	private static int formNumber = 1;
	private static String[] formDetail = new String[256];
	private static String IDUst = new String();
	private static String selected = "";
	public static String nameId = "";
	private static String checked = "";
	private static boolean search = false;
	public static int searchId = 0;
	public static String condName = "";
	public static String cond = "";
	private static String exchange_form_name = "";

	@Deprecated
	public static String bg = ""; // added by goto 20130311 "background"

	private HTMLEnv() {
	}
	
	public static void createHTMLDocument(){
		// TODO Put the file name in the configuration
		File input = new File("template.html");
		try {
			htmlDocument = Jsoup.parse(input, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Elements createHeader() {
		Elements headElements = new Elements();

		if (GlobalEnv.isAjax()) {
			String js = GlobalEnv.getJsDirectory();

			if (js != null) {
				if (js.endsWith("/"))
					js = js.substring(0, js.lastIndexOf("/"));
				headElements.add(JsoupFactory.createJsElement(js
						+ "/prototype.js"));
				headElements.add(JsoupFactory.createJsElement(js + "/ajax.js"));
			} else {
				headElements
						.add(JsoupFactory
								.createJsElement("http://localhost:8080/tab/prototype.js"));
				headElements.add(JsoupFactory
						.createJsElement("http://localhost:8080/tab/ajax.js"));
			}

			// Insertion of javascript files
			ArrayList<Element> elements = JsoupFactory.createJsElements(
					"js/lightbox.js", "js/scriptaculous.js?load=effects",
					"js/prototype.js", "build/animation/animation.js",
					"build/container/container.js", "build/tabview/tabview.js",
					"build/element/element-beta.js", "prototype.js",
					"ssqlajax.js", "build/yahoo/yahoo-min.js",
					"build/event/event-min.js", "build/dom/dom-min.js",
					"build/dragdrop/dragdrop-min.js");

			headElements.addAll(elements);

			// Insertion of css files
			elements = JsoupFactory.createStylesheetElements(
					"build/tabview/assets/border_tabs.css",
					"build/tabview/assets/tabview.css",
					"build/container/assets/container.css");
			headElements.addAll(elements);
			elements = JsoupFactory.createStylesheetElements(new String[] {
					"css/lightbox.css", "screen" }, new String[] {
					"css/tabview-core.css", "screen" }, new String[] {
					"css/panel.css", "screen" });

			headElements.addAll(elements);

			Attributes jsType = new Attributes();
			jsType.put("type", "text/javascript");
			Element element = new Element(Tag.valueOf("script"), "", jsType);
			element.text(script.toString());
			headElements.add(element);
		}
		if (GlobalEnv.getframeworklist() == null) {
			htmlDocument.head().appendElement("meta").attr("name", "GENERATOR").attr("content", " SuperSQL (GENERATE HTML) ");
		}
		ArrayList<Element> boxStylesheets = JsoupFactory
				.createStylesheetElements("styles/box.css", "styles/div.css",
						"styles/reset.css");

		for (Element elt : headElements) {
			htmlDocument.head().appendChild(elt);
		}
		for (Element elt : boxStylesheets) {
			htmlDocument.head().appendChild(elt);
		}
		return headElements;
	}

	public static void setOutlineMode() {
		OutlineMode = true;
	}

	public static boolean isOutlineMode() {
		if (OutlineMode) {
			OutlineMode = false;
			return true;
		}
		return false;
	}

	public static String getOutlineModeAtt() {
		if (OutlineMode) {
			OutlineMode = false;
			return " outline";
		}
		return "";
	}

	public static String getClassID(ITFE tfe) {
		String result;
		if (tfe instanceof HTMLC3) {
			result = getClassID(((ITFE) ((HTMLC3) tfe).tfes.get(0)));
			return result;
		}
		if (tfe instanceof HTMLG3) {
			result = getClassID(((ITFE) ((HTMLG3) tfe).tfe));
			return result;
		}
		result = "TFE" + tfe.getId();
		return result;
	}

	public static String getDataID(ITFE tfe) {
		String ClassID;
		int DataID = 0;
		String return_value;

		if (tfe instanceof HTMLC3) {
			return getClassID(((ITFE) ((HTMLC3) tfe).tfes.get(0)));
		}
		if (tfe instanceof HTMLG3) {
			return getClassID(((ITFE) ((HTMLG3) tfe).tfe));
		}
		ClassID = String.valueOf(tfe.getId());
		DataID = Integer.valueOf(
				(ClassID.substring(ClassID.length() - 3, ClassID.length())))
				.intValue();

		Log.out("ClassID=" + ClassID);
		Log.out("DataID=" + DataID);
		Log.out("ID_counter=" + IDCounter);

		if (DataID < IDOld) {
			IDCounter = DataID;
		} else {
			if (DataID != IDCounter && DataID > IDCounter) {
				DataID = IDCounter;
			}
		}
		IDCounter++;
		IDOld = DataID;
		return_value = String.valueOf(DataID);
		return return_value;
	}

	public static void initAllFormFlg() {
		setFormItemFlg(false, null);
		setSelectFlg(false);
		setSelectRepeat(false);
		setFormValueString(null);
		setFormPartsName(null);
		setSelected("");
		setIDU("");
		formPartsNumber = 1;
		exchange_form_name = new String();
		formDetail = new String[256];
		formNumber = 1;
		nameId = "";
		search = false;
		searchId = 0;
		condName = "";
		cond = "";
	}

	// form tag is written : true
	public static void setFormItemFlg(boolean b, String s) {
		isFormItem = b;
		formItemName = s;
		return;
	}

	public static boolean getFormItemFlg() {
		return isFormItem;
	}

	public static String getFormItemName() {
		if (formItemName == null) {
			return "0";
		}
		return formItemName;
	}

	public static void setSelectFlg(boolean b) {
		selectFlg = b;
	}

	public static boolean getSelectFlg() {
		return selectFlg;
	}

	public static void setFormValueString(String s) {
		formValueString = s;
	}

	public static String getFormValueString() {
		return formValueString;
	}

	// select_repeat flag
	// not write "<tr><td>" between "<option>"s
	public static void setSelectRepeat(boolean b) {
		selectRepeat = b;
	}

	public static boolean getSelectRepeat() {
		return selectRepeat;
	}

	public static String getFormPartsName() {
		if (formPartsName == null) {
			return "t" + formPartsNumber;
		} else {
			return formPartsName;
		}
	}

	public static void incrementFormPartsNumber() {
		formPartsNumber++;
	}

	public static void setFormPartsName(String s) {
		formPartsName = s;
	}

	

	public static void exFormName() {
		String s = "t" + formPartsNumber + ":" + formPartsName + ":";
		if (exchange_form_name == null || exchange_form_name.isEmpty()) {
			exchange_form_name = ":" + s;
		} else {
			if (!exchange_form_name.contains(s))
				exchange_form_name += s;
		}
	}

	public static Element exFormNameCreateForJsoup() {
		if (exchange_form_name != null) {
			Element result = new Element(Tag.valueOf("input"), "");
			result.attr("type", "hidden");
			result.attr("name", "exchangeName");
			result.attr("value", exchange_form_name);
			return result;
		} else {
			return null;
		}
	}

	public static String getFormName() {
		// return formNumber f1,f2,f3...
		return "f" + formNumber;
	}

	public static void setFormDetail(String s) {
		if (formDetail[formNumber] == null)
			formDetail[formNumber] = s;
		else
			formDetail[formNumber] += s;
	}

	public static String getFormDetail(int i) {
		return formDetail[i];
	}

	public static void setIDU(String s) {
		IDUst = s;
	}

	public static String getIDU() {
		return IDUst;
	}

	public static void setSelected(String s) {
		selected = s;
	}

	public static String getSelected() {
		return selected;
	}

	public static String getNameid() {
		if (nameId != null) {
			return nameId;
		} else {
			return "";
		}
	}

	public static void setChecked(String s) {
		System.out.println("checked:" + s);
		checked = s;
	}

	public static String getChecked() {
		return checked;
	}

	public static void setSearch(boolean b) {
		search = b;
		searchId = 0;
	}

	public static boolean getSearch() {
		return search;
	}

	public static Document getHtmlEnv1() {
		return htmlDocument;
	}

	public static void setCharset(String charset) {
		htmlDocument.head().getElementsByAttribute("charset")
				.attr("charset", charset);
	}

	public static void addCssStylesheet(String string) {
		htmlDocument.head().appendChild(
				JsoupFactory.createStylesheetElement(string));
	}

	public static void addJsFile(String string) {
		htmlDocument.head().appendChild(JsoupFactory.createJsElement(string));
	}

	public static void setTitle(String string) {
		htmlDocument.head().getElementsByTag("title").html(string);
	}

	public static void addMeta(String name, String content) {
		htmlDocument.head().appendElement("meta").attr("name", name)
				.attr("content", content);
	}

	public static void setBackground(String str) {
		htmlDocument.head().appendElement("style").attr("type", "text/css")
				.html("body {background: " + str + ";}");
	}

	public static void addStyle(String str) {
		htmlDocument.head().appendElement("style").attr("type", "text/css")
				.html(str);
	}

	public static void appendHeader() {
		htmlDocument.head().children().addAll(createHeader());
	}

}
