package supersql.codegenerator.HTML;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jsoup.nodes.Element;

import supersql.codegenerator.DecorateList;
import supersql.codegenerator.TFE;

public class HTMLUtils {

	private static final HashSet<String> formDecos = new HashSet<String>(
			Arrays.asList(new String[] { "insert", "update", "delete" }));
	
	private HTMLUtils() {
	}

	public static void propagateDeco(TFE tfe, DecorateList decos) {
		for (String s : formDecos) {
			if (decos.containsKey(s))
				tfe.addDeco(s, true);
		}
	}

	public static Element checkIfForm(Element result) {
		if (result.getElementsByTag("input").size() > 0) {
			result.getElementsByTag("form").tagName("div");
			result.tagName("form");
			result.getElementsByAttributeValue("type", "submit").remove();
			result.appendChild(JsoupFactory.createInput("submit", "",
					"Let's go !"));
		}
		return result;
	}

	public static void processDecos(Element result, DecorateList decos) {
		decos.alias("bgcolor", "background-color");
		decos.alias("font-color", "color");
		decos.alias("font color", "color");
		decos.alias("font size", "font-size");
		decos.alias("size", "font-size");
		if (decos.containsKey("charset")) {
			HTMLEnv.setCharset(decos.getStr("charset"));
			decos.remove("charset");
		}
		if (decos.containsKey("cssfile")) {
			String[] cssFiles = decos.getStr("cssfile").trim().split(";");
			for (int i = 0; i < cssFiles.length; i++)
				HTMLEnv.addCssStylesheet(cssFiles[i]);
			decos.remove("cssFile");
		}
		if (decos.containsKey("jsfile")) {
			String[] jsFiles = decos.getStr("jsfile").trim().split(";");
			for (int i = 0; i < jsFiles.length; i++)
				HTMLEnv.addJsFile(jsFiles[i]);
			decos.remove("jsfile");
		}
		if (decos.containsKey("require")) {
			String[] cssAndJsFiles = decos.getStr("require").split(";");
			for (int i = 0; i < cssAndJsFiles.length; i++) {
				String cssOrJsFileOrFolder = cssAndJsFiles[i];
				cssOrJsFileOrFolder = cssOrJsFileOrFolder.trim();
				if (cssOrJsFileOrFolder.endsWith("css"))
					HTMLEnv.addCssStylesheet(cssOrJsFileOrFolder);
				else if (cssOrJsFileOrFolder.endsWith("js"))
					HTMLEnv.addJsFile(cssOrJsFileOrFolder);
				else {
					try {
						String[] fileArray = new File(cssOrJsFileOrFolder)
								.getAbsoluteFile().list();
						for (int j = 0; j < fileArray.length; j++) {
							String file = fileArray[j];
							if (file.endsWith("css")) {
								HTMLEnv.addCssStylesheet(file);
							} else if (file.endsWith("js")) {
								HTMLEnv.addJsFile(file);
							}
						}
					} catch (Exception e) {
						System.err.println("<Warning> require=Impossible to find the folder"+cssOrJsFileOrFolder+"");
					}
				}
			}
			decos.remove("require");
		}
		if(decos.containsKey("title")){
			HTMLEnv.setTitle(decos.getStr("title"));
			decos.remove("title");
		}
		
		String[] metaNameArray = {"description", "keyword", "author", "pragma", "robot"};
		for (int i = 0; i < metaNameArray.length; i++) {
			String key = metaNameArray[i];
			if(decos.containsKey(key)){
				HTMLEnv.addMeta(key.substring(0, 1).toUpperCase() + key.substring(1), decos.getStr(key));
			}
			decos.remove(key);
		}
		if (decos.containsKey("background"))
			HTMLEnv.setBackground(decos.getStr("background"));

		
		
		if(decos.getConditionsSize() > 0)
			HTMLEnv.addStyle(computeConditionalDecorations(decos));
		
		for (String key : decos.keySet()) {
			if(key.equalsIgnoreCase("color") || key.equalsIgnoreCase("background-color") || 
					key.equalsIgnoreCase("width") || key.equalsIgnoreCase("height")){
				result.attr("style", result.attr("style") + ";" + key + ":" + decos.getStr(key));
			}
			result.attr(key, decos.getStr(key));
		}
	}
	
	public static String computeConditionalDecorations(DecorateList decos) {
		Object decorationKeys;
		Object decorationValue;
		String decorationKey;
		String condition;
		String className;
		String style = "";
		for (Entry<String, Object> conditions : decos.getConditions()
				.entrySet()) {
			decorationKeys = conditions.getValue();
			condition = conditions.getKey();

			className = "C_" + decos.getClassesIds().get(condition);
			style += "." + className + "{";

			if (decorationKeys instanceof String) {
				decorationKey = (String) decorationKeys;
				decorationValue = decos
						.getDecorationValueFromDecorationKeyAndCondition(
								decorationKey, condition);
				style += decorationKey + " : " + decorationValue + ";";
				decos.remove(decorationKey);
			} else {
				Iterator<String> decorationKeysIterator = ((ArrayList<String>) (decorationKeys))
						.iterator();
				while (decorationKeysIterator.hasNext()) {
					decorationKey = (String) decorationKeysIterator.next();
					decorationValue = decos
							.getDecorationValueFromDecorationKeyAndCondition(
									decorationKey, condition);
					style+= decorationKey + " : " + decorationValue + ";";
					decos.remove(decorationKey);
				}
			}

			style+= "}";
		}
		return style;
	}
	
}
