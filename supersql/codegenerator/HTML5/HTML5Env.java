package supersql.codegenerator.HTML5;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import supersql.codegenerator.DecorateList;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Jscss;
import supersql.codegenerator.LocalEnv;
import supersql.common.GlobalEnv;
import supersql.common.Log;

public class HTML5Env extends LocalEnv {
	
	private Document html5Env;
	
	public StringBuffer code;
	public int countFile = 0; // C3, G3
	public static StringBuffer css;
	public String fileName;
	public StringBuffer footer;
	public static boolean footerFlag = false;
	public int haveClass = 0;
	public StringBuffer header;
	public static boolean headerFlag = false;
	public int linkFlag = 0; // C3, G3
	public String linkOutFile;
	public String linkUrl; // C3, G3
	public Vector<String> notWrittenClassId = new Vector<String>();
	public String outDir;
	public String outFile;
	public boolean tableFlag = false;
	public Vector<String> writtenClassId = new Vector<String>();
	
	public HTML5Env() {
		this.html5Env = new Document("");
		new Document("");
	}
	
	public void append_css_def_att(String classid, DecorateList decos) {
		Log.out("[HTML5 append_css_def_att] classid = " + classid);
		Log.out("decos = " + decos);
		
		haveClass = 0;
		if (writtenClassId.contains(classid)) {
			haveClass = 1;
			Log.out("==> already created css style");
			return;
		} else if (notWrittenClassId != null && notWrittenClassId.contains(classid)) {
			Log.out("==> css style is null. not created style");
			return;
		}
		Log.out("==> new style @@ creating style no " + classid);
		
		// css情報の書き込み
		StringBuffer cssbuf = new StringBuffer();
		
		// background-attachment
		if (decos.containsKey("background-attachment")) { // TODO
			cssbuf.append("\tbackground-attachment: " + decos.getStr("background-attachment") + ";\n");
		}
		
		// background-clip (CSS3)
		if (decos.containsKey("background-clip")) { // TODO
			cssbuf.append("\tbackground-clip: " + decos.getStr("background-clip") + ";\n");
		}
		
		// background-color & bgcolor
		if (decos.containsKey("background-color")) { // TODO
			cssbuf.append("\tbackground-color: " + decos.getStr("background-color") + ";\n");
		} else if (decos.containsKey("bgcolor")) {
			cssbuf.append("\tbackground-color: " + decos.getStr("bgcolor") + ";\n");
		}
		
		// background-image
		if (decos.containsKey("background-image")) { // TODO
			if ((decos.getStr("background-image").indexOf("url") != -1) || decos.getStr("background-image").equals("none") || decos.getStr("background-image").equals("inherit")) {
				cssbuf.append("\tbackground-image: " + decos.getStr("background-image") + ";\n");
			} else {
				cssbuf.append("\tbackground-image: url(\"" + decos.getStr("background-image") + "\");\n");
			}
		}
		
		// background-origin (CSS3)
		if (decos.containsKey("background-origin")) { // TODO
			cssbuf.append("\tbackground-origin: " + decos.getStr("background-origin") + ";\n");
		}
		
		// background-repeat
		if (decos.containsKey("background-repeat")) { // TODO
			cssbuf.append("\tbackground-repeat: " + decos.getStr("background-repeat") + ";\n");
		}
		
		// background-size (CSS3)
		if (decos.containsKey("background-size")) { // TODO
			if (isNumber(decos.getStr("background-size"))) {
				cssbuf.append("\tbackground-size: " + decos.getStr("background-size") + "px;\n");
			} else {
				cssbuf.append("\tbackground-size: " + decos.getStr("background-size") + ";\n");
			}
		}
		
		// border-collapse (only table element)
		if (decos.containsKey("border-collapse")) { // TODO
			cssbuf.append("\tborder-collapse: " + decos.getStr("border-collapse") + ";\n");
		}
		
		// border-color
		if (decos.containsKey("border-color")) { // TODO
			cssbuf.append("\tborder-color: " + decos.getStr("border-color") + ";\n");
		}
		// border-bottom-color & border-color-bottom
		if (decos.containsKey("border-bottom-color")) { // TODO
			cssbuf.append("\tborder-bottom-color: " + decos.getStr("border-bottom-color") + ";\n");
		} else if (decos.containsKey("border-color-bottom")) {
			cssbuf.append("\tborder-bottom-color: " + decos.getStr("border-color-bottom") + ";\n");
		}
		// border-left-color & border-color-left
		if (decos.containsKey("border-left-color")) { // TODO
			cssbuf.append("\tborder-left-color: " + decos.getStr("border-left-color") + ";\n");
		} else if (decos.containsKey("border-color-left")) {
			cssbuf.append("\tborder-left-color: " + decos.getStr("border-color-left") + ";\n");
		}
		// border-right-color & border-color-right
		if (decos.containsKey("border-right-color")) { // TODO
			cssbuf.append("\tborder-right-color: " + decos.getStr("border-right-color") + ";\n");
		} else if (decos.containsKey("border-color-right")) {
			cssbuf.append("\tborder-right-color: " + decos.getStr("border-color-right") + ";\n");
		}
		// border-top-color & border-color-top
		if (decos.containsKey("border-top-color")) { // TODO
			cssbuf.append("\tborder-top-color: " + decos.getStr("border-top-color") + ";\n");
		} else if (decos.containsKey("border-color-top")) {
			cssbuf.append("\tborder-top-color: " + decos.getStr("border-color-top") + ";\n");
		}
		
		// border-radius (CSS3)
		if (decos.containsKey("border-radius")) { // TODO
			if (isNumber(decos.getStr("border-radius"))) {
				cssbuf.append("\tborder-radius: " + decos.getStr("border-radius") + "px;\n");
			} else {
				cssbuf.append("\tborder-radius: " + decos.getStr("border-radius") + ";\n");
			}
		}
		
		// border-spacing (only table element)
		if (decos.containsKey("border-spacing")) { // TODO
			if (isNumber(decos.getStr("border-spacing"))) {
				cssbuf.append("\tborder-spacing: " + decos.getStr("border-spacing") + "px;\n");
			} else {
				cssbuf.append("\tborder-spacing: " + decos.getStr("border-spacing") + ";\n");
			}
		}
		
		// border-style
		if (decos.containsKey("border-style")) { // TODO
			cssbuf.append("\tborder-style: " + decos.getStr("border-style") + ";\n");
		}
		// border-bottom-style & border-style-bottom
		if (decos.containsKey("border-bottom-style")) { // TODO
			cssbuf.append("\tborder-bottom-style: " + decos.getStr("border-bottom-style") + ";\n");
		} else if (decos.containsKey("border-style-bottom")) {
			cssbuf.append("\tborder-bottom-style: " + decos.getStr("border-style-bottom") + ";\n");
		}
		// border-left-style & border-style-left
		if (decos.containsKey("border-left-style")) { // TODO
			cssbuf.append("\tborder-left-style: " + decos.getStr("border-left-style") + ";\n");
		} else if (decos.containsKey("border-style-left")) {
			cssbuf.append("\tborder-left-style: " + decos.getStr("border-style-left") + ";\n");
		}
		// border-right-style & border-style-right
		if (decos.containsKey("border-right-style")) { // TODO
			cssbuf.append("\tborder-right-style: " + decos.getStr("border-right-style") + ";\n");
		} else if (decos.containsKey("border-style-right")) {
			cssbuf.append("\tborder-right-style: " + decos.getStr("border-style-right") + ";\n");
		}
		// border-top-style & border-style-top
		if (decos.containsKey("border-top-style")) { // TODO
			cssbuf.append("\tborder-top-style: " + decos.getStr("border-top-style") + ";\n");
		} else if (decos.containsKey("border-style-top")) {
			cssbuf.append("\tborder-top-style: " + decos.getStr("border-style-top") + ";\n");
		}
		
		// border-width
		if (decos.containsKey("border-width")) { // TODO
			if (isNumber(decos.getStr("border-width"))) {
				cssbuf.append("\tborder-width: " + decos.getStr("border-width") + "px;\n");
			} else {
				cssbuf.append("\tborder-width: " + decos.getStr("border-width") + ";\n");
			}
		}
		// border-bottom-width & border-width-bottom
		if (decos.containsKey("border-bottom-width")) { // TODO
			if (isNumber(decos.getStr("border-bottom-width"))) {
				cssbuf.append("\tborder-bottom-width: " + decos.getStr("border-bottom-width") + "px;\n");
			} else {
				cssbuf.append("\tborder-bottom-width: " + decos.getStr("border-bottom-width") + ";\n");
			}
		} else if (decos.containsKey("border-width-bottom")) { 
			if (isNumber(decos.getStr("border-width-bottom"))) {
				cssbuf.append("\tborder-bottom-width: " + decos.getStr("border-width-bottom") + "px;\n");
			} else {
				cssbuf.append("\tborder-bottom-width: " + decos.getStr("border-width-bottom") + ";\n");
			}
		}
		// border-left-width & border-width-left
		if (decos.containsKey("border-left-width")) { // TODO
			if (isNumber(decos.getStr("border-left-width"))) {
				cssbuf.append("\tborder-left-width: " + decos.getStr("border-left-width") + "px;\n");
			} else {
				cssbuf.append("\tborder-left-width: " + decos.getStr("border-left-width") + ";\n");
			}
		} else if (decos.containsKey("border-width-left")) { 
			if (isNumber(decos.getStr("border-width-left"))) {
				cssbuf.append("\tborder-left-width: " + decos.getStr("border-width-left") + "px;\n");
			} else {
				cssbuf.append("\tborder-left-width: " + decos.getStr("border-width-left") + ";\n");
			}
		}
		// border-right-width & border-width-right
		if (decos.containsKey("border-right-width")) { // TODO
			if (isNumber(decos.getStr("border-right-width"))) {
				cssbuf.append("\tborder-right-width: " + decos.getStr("border-right-width") + "px;\n");
			} else {
				cssbuf.append("\tborder-right-width: " + decos.getStr("border-right-width") + ";\n");
			}
		} else if (decos.containsKey("border-width-right")) { 
			if (isNumber(decos.getStr("border-width-right"))) {
				cssbuf.append("\tborder-right-width: " + decos.getStr("border-width-right") + "px;\n");
			} else {
				cssbuf.append("\tborder-right-width: " + decos.getStr("border-width-right") + ";\n");
			}
		}
		// border-top-width & border-width-top
		if (decos.containsKey("border-top-width")) { // TODO
			if (isNumber(decos.getStr("border-top-width"))) {
				cssbuf.append("\tborder-top-width: " + decos.getStr("border-top-width") + "px;\n");
			} else {
				cssbuf.append("\tborder-top-width: " + decos.getStr("border-top-width") + ";\n");
			}
		} else if (decos.containsKey("border-width-top")) { 
			if (isNumber(decos.getStr("border-width-top"))) {
				cssbuf.append("\tborder-top-width: " + decos.getStr("border-width-top") + "px;\n");
			} else {
				cssbuf.append("\tborder-top-width: " + decos.getStr("border-width-top") + ";\n");
			}
		}
		
		// box-decoration-break (CSS3)
		if (decos.containsKey("box-decoration-break")) { // TODO
			cssbuf.append("\tbox-decoration-break: " + decos.getStr("box-decoration-break") + ";\n");
		}
		
		// box-sizing (CSS3)
		if (decos.containsKey("box-sizing")) { // TODO
			cssbuf.append("\tbox-sizing: " + decos.getStr("box-sizing") + ";\n");
		}
		
		// caption-side & caption (only caption element)
		if (decos.containsKey("caption-side")) { // TODO
			cssbuf.append("\tcaption-side: " + decos.getStr("caption-side") + ";\n");
		} else if (decos.containsKey("caption")) {
			cssbuf.append("\tcaption-side: " + decos.getStr("caption") + ";\n");
		}
		
		// color & font-color
		if (decos.containsKey("color")) { // TODO
			cssbuf.append("\tcolor: " + decos.getStr("color") + ";\n");
		} else if (decos.containsKey("font-color")) {
			cssbuf.append("\tcolor: " + decos.getStr("font-color") + ";\n");
		}
		
		// column-count (CSS3)
		if (decos.containsKey("column-count")) { // TODO
			cssbuf.append("\t-moz-column-count: " + decos.getStr("column-count") + ";\n");
			cssbuf.append("\t-webkit-column-count: " + decos.getStr("column-count") + ";\n");
			cssbuf.append("\tcolumn-count: " + decos.getStr("column-count") + ";\n");
		}
		
		// column-fill (CSS3)
		if (decos.containsKey("column-fill")) { // TODO
			cssbuf.append("\t-moz-column-fill: " + decos.getStr("column-fill") + ";\n");
			cssbuf.append("\t-webkit-column-fill: " + decos.getStr("column-fill") + ";\n");
			cssbuf.append("\tcolumn-fill: " + decos.getStr("column-fill") + ";\n");
		}
		
		// column-gap (CSS3)
		if (decos.containsKey("column-gap")) { // TODO
			if (isNumber(decos.getStr("column-gap"))) {
				cssbuf.append("\t-moz-column-gap: " + decos.getStr("column-gap") + "px;\n");
				cssbuf.append("\t-webkit-column-gap: " + decos.getStr("column-gap") + "px;\n");
				cssbuf.append("\tcolumn-gap: " + decos.getStr("column-gap") + "px;\n");
			} else {
				cssbuf.append("\t-moz-column-gap: " + decos.getStr("column-gap") + ";\n");
				cssbuf.append("\t-webkit-column-gap: " + decos.getStr("column-gap") + ";\n");
				cssbuf.append("\tcolumn-gap: " + decos.getStr("column-gap") + ";\n");
			}
		}
		
		// column-rule-color (CSS3)
		if (decos.containsKey("column-rule-color")) { // TODO
			cssbuf.append("\t-moz-column-rule-color: " + decos.getStr("column-rule-color") + ";\n");
			cssbuf.append("\t-webkit-column-rule-color: " + decos.getStr("column-rule-color") + ";\n");
			cssbuf.append("\tcolumn-rule-color: " + decos.getStr("column-rule-color") + ";\n");
		}
		
		// column-rule-style (CSS3)
		if (decos.containsKey("column-rule-style")) { // TODO
			cssbuf.append("\t-moz-column-rule-style: " + decos.getStr("column-rule-style") + ";\n");
			cssbuf.append("\t-webkit-column-rule-style: " + decos.getStr("column-rule-style") + ";\n");
			cssbuf.append("\tcolumn-rule-style: " + decos.getStr("column-rule-style") + ";\n");
		}
		
		// column-rule-width (CSS3)
		if (decos.containsKey("column-rule-width")) { // TODO
			if (isNumber(decos.getStr("column-rule-width"))) {
				cssbuf.append("\t-moz-column-rule-width: " + decos.getStr("column-rule-width") + "px;\n");
				cssbuf.append("\t-webkit-column-rule-width: " + decos.getStr("column-rule-width") + "px;\n");
				cssbuf.append("\tcolumn-rule-width: " + decos.getStr("column-rule-width") + "px;\n");
			} else {
				cssbuf.append("\t-moz-column-rule-width: " + decos.getStr("column-rule-width") + ";\n");
				cssbuf.append("\t-webkit-column-rule-width: " + decos.getStr("column-rule-width") + ";\n");
				cssbuf.append("\tcolumn-rule-width: " + decos.getStr("column-rule-width") + ";\n");
			}
		}
		
		// column-span (CSS3)
		if (decos.containsKey("column-span")) { // TODO
			cssbuf.append("\t-webkit-column-rule-width: " + decos.getStr("column-span") + ";\n");
			cssbuf.append("\tcolumn-rule-width: " + decos.getStr("column-span") + ";\n");
		}
		
		// column-width (CSS3)
		if (decos.containsKey("column-width")) { // TODO
			if (isNumber(decos.getStr("column-width"))) {
				cssbuf.append("\t-moz-column-width: " + decos.getStr("column-width") + "px;\n");
				cssbuf.append("\t-webkit-column-width: " + decos.getStr("column-width") + "px;\n");
				cssbuf.append("\tcolumn-width: " + decos.getStr("column-width") + "px;\n");
			} else {
				cssbuf.append("\t-moz-column-width: " + decos.getStr("column-width") + ";\n");
				cssbuf.append("\t-webkit-column-width: " + decos.getStr("column-width") + ";\n");
				cssbuf.append("\tcolumn-width: " + decos.getStr("column-width") + ";\n");
			}
		}
		
		// counter-increment
		if (decos.containsKey("counter-increment")) { // TODO
			cssbuf.append("\tcounter-increment: " + decos.getStr("counter-increment") + ";\n");
		}
		
		// counter-reset
		if (decos.containsKey("counter-reset")) { // TODO
			cssbuf.append("\tcounter-reset: " + decos.getStr("counter-reset") + ";\n");
		}
		
		// cursor
		if (decos.containsKey("cursor")) { // TODO
			cssbuf.append("\tcursor: " + decos.getStr("cursor") + ";\n");
		}
		
		// direction
		if (decos.containsKey("direction")) { // TODO
			cssbuf.append("\tdirection: " + decos.getStr("direction") + ";\n");
		}
		
		// display
		if (decos.containsKey("display")) { // TODO
			cssbuf.append("\tdisplay: " + decos.getStr("display") + ";\n");
		}
		
		// empty-cells (only cell elements)
		if (decos.containsKey("empty-cells")) { // TODO
			cssbuf.append("\tempty-cells: " + decos.getStr("empty-cells") + ";\n");
		}
		
		// font
		if (decos.containsKey("font")) { // TODO
			cssbuf.append("\tfont: " + decos.getStr("font") + ";\n");
		}
		
		// font-family
		if (decos.containsKey("font-family")) { // TODO
			cssbuf.append("\tfont-family: " + decos.getStr("font-family") + ";\n");
		}
		
		// font-size & size
		if (decos.containsKey("font-size")) { // TODO
			if (isNumber(decos.getStr("font-size"))) {
				cssbuf.append("\tfont-size: " + decos.getStr("font-size") + "px;\n");
			} else {
				cssbuf.append("\tfont-size: " + decos.getStr("font-size") + ";\n");
			}
		} else if (decos.containsKey("size")) {
			if (isNumber(decos.getStr("size"))) {
				cssbuf.append("\tfont-size: " + decos.getStr("size") + "px;\n");
			} else {
				cssbuf.append("\tfont-size: " + decos.getStr("size") + ";\n");
			}
		}
		
		// font-size-adjust
		if (decos.containsKey("font-size-adjust")) { // TODO
			cssbuf.append("\tfont-size-adjust: " + decos.getStr("font-size-adjust") + ";\n");
		}
		
		// font-stretch (CSS3)
		if (decos.containsKey("font-stretch")) { // TODO
			cssbuf.append("\tfont-stretch: " + decos.getStr("font-stretch") + ";\n");
		}
		
		// font-style
		if (decos.containsKey("font-style")) { // TODO
			cssbuf.append("\tfont-style: " + decos.getStr("font-style") + ";\n");
		}
		
		// font-variant
		if (decos.containsKey("font-variant")) { // TODO
			cssbuf.append("\tfont-variant: " + decos.getStr("font-variant") + ";\n");
		}
		
		// font-weight
		if (decos.containsKey("font-weight")) { // TODO
			cssbuf.append("\tfont-weight:" + decos.getStr("font-weight") + ";\n");
		}
		
		// height
		if (decos.containsKey("height")) { // TODO
			if (isNumber(decos.getStr("height"))) {
				cssbuf.append("\theight: " + decos.getStr("height") + "px;\n");
			} else {
				cssbuf.append("\theight: " + decos.getStr("height") + ";\n");
			}
		}
		
		// letter-spacing
		if (decos.containsKey("letter-spacing")) { // TODO
			if (isNumber(decos.getStr("letter-spacing"))) {
				cssbuf.append("\tletter-spacing: " + decos.getStr("letter-spacing") + "px;\n");
			} else {
				cssbuf.append("\tletter-spacing: " + decos.getStr("letter-spacing") + ";\n");
			}
		}
		
		// line-break (CSS3)
		if (decos.containsKey("line-break")) { // TODO
			cssbuf.append("\tline-break: " + decos.getStr("line-break") + ";\n");
		}
		
		// line-height
		if (decos.containsKey("line-height")) { // TODO
			if (isNumber(decos.getStr("line-height"))) {
				cssbuf.append("\tline-height: " + decos.getStr("line-height") + "px;\n");
			} else {
				cssbuf.append("\tline-height: " + decos.getStr("line-height") + ";\n");
			}
		}
		
		// list-style-image
		if (decos.containsKey("list-style-image")) { // TODO
			if ((decos.getStr("list-style-image").indexOf("url") != -1) || decos.getStr("list-style-image").equals("none") || decos.getStr("list-style-image").equals("inherit")) {
				cssbuf.append("\tlist-style-image: " + decos.getStr("list-style-image") + ";\n");
			} else {
				cssbuf.append("\tlist-style-image: url(\"" + decos.getStr("list-style-image") + "\");\n");
			}
		}
		
		// list-style-position
		if (decos.containsKey("list-style-position")) { // TODO
			cssbuf.append("\tlist-style-position: " + decos.getStr("list-style-position") + ";\n");
		}
		
		// list-style-type
		if (decos.containsKey("list-style-type")) { // TODO
			cssbuf.append("\tlist-style-type: " + decos.getStr("list-style-type") + ";\n");
		}
		
		// margin
		if (decos.containsKey("margin")) { // TODO
			if (isNumber(decos.getStr("margin"))) {
				cssbuf.append("\tmargin: " + decos.getStr("margin") + "px;\n");
			} else {
				cssbuf.append("\tmargin: " + decos.getStr("margin") + ";\n");
			}
		}
		// margin-bottom
		if (decos.containsKey("margin-bottom")) { // TODO
			if (isNumber(decos.getStr("margin-bottom"))) {
				cssbuf.append("\tmargin-bottom: " + decos.getStr("margin-bottom") + "px;\n");
			} else {
				cssbuf.append("\tmargin-bottom: " + decos.getStr("margin-bottom") + ";\n");
			}
		}
		// margin-left
		if (decos.containsKey("margin-left")) { // TODO
			if (isNumber(decos.getStr("margin-left"))) {
				cssbuf.append("\tmargin-left: " + decos.getStr("margin-left") + "px;\n");
			} else {
				cssbuf.append("\tmargin-left: " + decos.getStr("margin-left") + ";\n");
			}
		}
		// margin-right
		if (decos.containsKey("margin-right")) { // TODO
			if (isNumber(decos.getStr("margin-right"))) {
				cssbuf.append("\tmargin-right: " + decos.getStr("margin-right") + "px;\n");
			} else {
				cssbuf.append("\tmargin-right: " + decos.getStr("margin-right") + ";\n");
			}
		}
		// margin-top
		if (decos.containsKey("margin-top")) { // TODO
			if (isNumber(decos.getStr("margin-top"))) {
				cssbuf.append("\tmargin-top: " + decos.getStr("margin-top") + "px;\n");
			} else {
				cssbuf.append("\tmargin-top: " + decos.getStr("margin-top") + ";\n");
			}
		}
		
		// max-height
		if (decos.containsKey("max-height")) { // TODO
			if (isNumber(decos.getStr("max-height"))) {
				cssbuf.append("\tmax-height: " + decos.getStr("max-height") + "px;\n");
			} else {
				cssbuf.append("\tmax-height: " + decos.getStr("max-height") + ";\n");
			}
		}
		// max-width
		if (decos.containsKey("max-width")) { // TODO
			if (isNumber(decos.getStr("max-width"))) {
				cssbuf.append("\tmax-width: " + decos.getStr("max-width") + "px;\n");
			} else {
				cssbuf.append("\tmax-width: " + decos.getStr("max-width") + ";\n");
			}
		}
		
		// min-height
		if (decos.containsKey("min-height")) { // TODO
			if (isNumber(decos.getStr("min-height"))) {
				cssbuf.append("\tmin-height: " + decos.getStr("min-height") + "px;\n");
			} else {
				cssbuf.append("\tmin-height: " + decos.getStr("min-height") + ";\n");
			}
		}
		// min-width
		if (decos.containsKey("min-width")) { // TODO
			if (isNumber(decos.getStr("min-width"))) {
				cssbuf.append("\tmin-width: " + decos.getStr("min-width") + "px;\n");
			} else {
				cssbuf.append("\tmin-width: " + decos.getStr("min-width") + ";\n");
			}
		}
		
		// opacity (CSS3)
		if (decos.containsKey("opacity")) { // TODO
			cssbuf.append("\topacity: " + decos.getStr("oapcity") + ";\n");
		}
		
		// outline-color
		if (decos.containsKey("outline-color")) { // TODO
			cssbuf.append("\toutline-color: " + decos.getStr("outline-color") + ";\n");
		}
		
		// outline-style
		if (decos.containsKey("outline-style")) { // TODO
			cssbuf.append("\toutline-style: " + decos.getStr("outline-style") + ";\n");
		}
		
		// outline-width
		if (decos.containsKey("outline-width")) { // TODO
			if (isNumber(decos.getStr("outline-width"))) {
				cssbuf.append("\toutline-width: " + decos.getStr("outline-width") + "px;\n");
			} else {
				cssbuf.append("\toutline-width: " + decos.getStr("outline-width") + ";\n");
			}
		}
		
		// overflow
		if (decos.containsKey("overflow")) { // TODO
			cssbuf.append("\toverflow: " + decos.getStr("overflow") + ";\n");
		}
		
		// padding
		if (decos.containsKey("padding")) { // TODO
			if (isNumber(decos.getStr("padding"))) {
				cssbuf.append("\tpadding: " + decos.getStr("padding") + "px;\n");
			} else {
				cssbuf.append("\tpadding: " + decos.getStr("padding") + ";\n");
			}
		}
		// padding-bottom
		if (decos.containsKey("padding-bottom")) { // TODO
			if (isNumber(decos.getStr("padding-bottom"))) {
				cssbuf.append("\tpadding-bottom: " + decos.getStr("padding-bottom") + "px;\n");
			} else {
				cssbuf.append("\tpadding-bottom: " + decos.getStr("padding-bottom") + ";\n");
			}
		}
		// padding-left
		if (decos.containsKey("padding-left")) { // TODO
			if (isNumber(decos.getStr("padding-left"))) {
				cssbuf.append("\tpadding-left: " + decos.getStr("padding-left") + "px;\n");
			} else {
				cssbuf.append("\tpadding-left: " + decos.getStr("padding-left") + ";\n");
			}
		}
		// padding-right
		if (decos.containsKey("padding-right")) { // TODO
			if (isNumber(decos.getStr("padding-right"))) {
				cssbuf.append("\tpadding-right: " + decos.getStr("padding-right") + "px;\n");
			} else {
				cssbuf.append("\tpadding-right: " + decos.getStr("padding-right") + ";\n");
			}
		}
		// padding-top
		if (decos.containsKey("padding-top")) { // TODO
			if (isNumber(decos.getStr("padding-top"))) {
				cssbuf.append("\tpadding-top: " + decos.getStr("padding-top") + "px;\n");
			} else {
				cssbuf.append("\tpadding-top: " + decos.getStr("padding-top") + ";\n");
			}
		}
		
		// position
		if (decos.containsKey("position")) { // TODO
			cssbuf.append("\tposition: " + decos.getStr("position") + ";\n");
		}
		// top (set position)
		if (decos.containsKey("position") && decos.containsKey("top")) { // TODO
			if (isNumber(decos.getStr("top"))) {
				cssbuf.append("\ttop: " + decos.getStr("top") + "px;\n");
			} else {
				cssbuf.append("\ttop: " + decos.getStr("top") + ";\n");
			}
		}
		// right (set position)
		if (decos.containsKey("position") && decos.containsKey("right")) { // TODO
			if (isNumber(decos.getStr("top"))) {
				cssbuf.append("\tright: " + decos.getStr("right") + "px;\n");
			} else {
				cssbuf.append("\tright: " + decos.getStr("right") + ";\n");
			}
		}
		// bottom (set position)
		if (decos.containsKey("position") && decos.containsKey("bottom")) { // TODO
			if (isNumber(decos.getStr("bottom"))) {
				cssbuf.append("\tbottom: " + decos.getStr("bottom") + "px;\n");
			} else {
				cssbuf.append("\tbottom: " + decos.getStr("bottom") + ";\n");
			}
		}
		// left (set position)
		if (decos.containsKey("position") && decos.containsKey("left")) { // TODO
			if (isNumber(decos.getStr("left"))) {
				cssbuf.append("\tleft: " + decos.getStr("left") + "px;\n");
			} else {
				cssbuf.append("\tleft: " + decos.getStr("left") + ";\n");
			}
		}
		// z-index (set position)
		if (decos.containsKey("position") && decos.containsKey("z-index")) { // TODO
			cssbuf.append("\tz-index: " + decos.getStr("z-index") + ";\n");
		}
		
		// table-layout (only table element)
		if (decos.containsKey("table-layout")) { // TODO
			cssbuf.append("\ttable-layout: " + decos.getStr("table-layout") + ";\n");
		}
		
		// tab-size (CSS3)
		if (decos.containsKey("tab-size")) { // TODO
			cssbuf.append("\ttab-size: " + decos.getStr("tab-size") + ";\n");
		}
		
		// text-align & align
		if (decos.containsKey("text-align")) { // TODO
			cssbuf.append("\ttext-align: " + decos.getStr("text-align") + ";\n");
		} else if (decos.containsKey("align")) {
			cssbuf.append("\ttext-align: " + decos.getStr("align") + ";\n");
		}
		
		// text-align-last (CSS3)
		if (decos.containsKey("text-align-last")) { // TODO
			cssbuf.append("\ttext-align-last: " + decos.getStr("text-align-last") + ";\n");
		}
		
		// text-decoration & text-decoration-line (CSS3)
		if (decos.containsKey("text-decoration")) { // TODO
			cssbuf.append("\ttext-decoration: " + decos.getStr("text-decoration") + ";\n");
		} else if (decos.containsKey("text-decoration-line")) {
			cssbuf.append("\t-moz-text-decoration-line: " + decos.getStr("text-decoration-line") + ";\n");
			cssbuf.append("\ttext-decoration-line: " + decos.getStr("text-decoration-line") + ";\n");
		}
		
		// text-decoration-color (CSS3)
		if (decos.containsKey("text-decoration-color")) { // TODO
			cssbuf.append("\t-moz-text-decoration-color: " + decos.getStr("text-decoration-color") + ";\n");
			cssbuf.append("\ttext-decoration-color: " + decos.getStr("text-decoration-color") + ";\n");
		}
		
		// text-decoration-skip (CSS3)
		if (decos.containsKey("text-decoration-skip")) { // TODO
			cssbuf.append("\t-moz-text-decoration-skip: " + decos.getStr("text-decoration-skip") + ";\n");
			cssbuf.append("\ttext-decoration-skip: " + decos.getStr("text-decoration-skip") + ";\n");
		}
		
		// text-decoration-style (CSS3)
		if (decos.containsKey("text-decoration-style")) { // TODO
			cssbuf.append("\t-moz-text-decoration-style: " + decos.getStr("text-decoration-style") + ";\n");
			cssbuf.append("\ttext-decoration-style: " + decos.getStr("text-decoration-style") + ";\n");
		}
		
		// text-indent & indent
		if (decos.containsKey("text-indent")) { // TODO
			if (isNumber(decos.getStr("text-indent"))) {
				cssbuf.append("\ttext-indent: " + decos.getStr("text-indent") + "px;\n");
			} else {
				cssbuf.append("\ttext-indent: " + decos.getStr("text-indent") + ";\n");
			}
		} else if (decos.containsKey("indent")) {
			if (isNumber(decos.getStr("indent"))) {
				cssbuf.append("\ttext-indent: " + decos.getStr("indent") + "px;\n");
			} else {
				cssbuf.append("\ttext-indent: " + decos.getStr("indent") + ";\n");
			}
		}
		
		// text-justify (CSS3)
		if (decos.containsKey("text-justify")) {
			cssbuf.append("\ttext-justify: " + decos.getStr("text-justify") + ";\n");
		}
		
		// text-transform
		if (decos.containsKey("text-transform")) { // TODO
			cssbuf.append("\ttext-transform: " + decos.getStr("text-transform") + ";\n");
		}
		
		// text-underline-position (CSS3)
		if (decos.containsKey("text-underline-position")) { // TODO
			cssbuf.append("\t-moz-text-underline-position: " + decos.getStr("text-underline-position") + ";\n");
			cssbuf.append("\ttext-underline-position: " + decos.getStr("text-underline-position") + ";\n");
		}
		
		// text-wrap (CSS3)
		if (decos.containsKey("text-wrap")) { // TODO
			cssbuf.append("\ttext-wrap: " + decos.getStr("text-wrap") + ";\n");
		}
		
		// unicode-bidi
		if (decos.containsKey("unicode-bidi")) { // TODO
			cssbuf.append("\tunicode-bidi: " + decos.getStr("unicode-bidi") + ";\n");
		}
		
		// virtical-align & valign
		// tableと分ける必要性あり
		if (decos.containsKey("virtical-align")) {  // TODO
			//cssbuf.append("\tvirtical-align: " + decos.getStr("virtical-align") + ";\n");
			if (decos.getStr("virtical-align").equals("top")) {
				cssbuf.append("\t-webkit-align-self: flex-start;\n");
				cssbuf.append("\talign-self: flex-start;\n");
			} else if (decos.getStr("virtical-align").equals("middle")) {
				cssbuf.append("\t-webkit-align-self: center;\n");
				cssbuf.append("\talign-self: center;\n");
			} else if (decos.getStr("virtical-align").equals("bottom")) {
				cssbuf.append("\t-webkit-align-self: flex-end;\n");
				cssbuf.append("\talign-self: flex-end;\n");
			}
		} else if (decos.containsKey("valign")) {
			//cssbuf.append("\tvirtical-align: " + decos.getStr("valign") + ";\n");
			if (decos.getStr("valign").equals("top")) {
				cssbuf.append("\t-webkit-align-self: flex-start;\n");
				cssbuf.append("\talign-self: flex-start;\n");
			} else if (decos.getStr("valign").equals("middle")) {
				cssbuf.append("\t-webkit-align-self: center;\n");
				cssbuf.append("\talign-self: center;\n");
			} else if (decos.getStr("valign").equals("bottom")) {
				cssbuf.append("\t-webkit-align-self: flex-end;\n");
				cssbuf.append("\talign-self: flex-end;\n");
			}
		}
		
		// visibility
		if (decos.containsKey("visibility")) { // TODO
			cssbuf.append("\tvisibility: " + decos.getStr("visibility") + ";\n");
		}
		
		// white-space
		if (decos.containsKey("white-space")) { // TODO
			cssbuf.append("\twhite-space: " + decos.getStr("white-space") + ";\n");
		}
		
		// width
		if (decos.containsKey("width")) { // TODO
			if (isNumber(decos.getStr("width"))) {
				cssbuf.append("\twidth: " + decos.getStr("width") + "px;\n");
			} else {
				cssbuf.append("\twidth: " + decos.getStr("width") + ";\n");
			}
		} else {
			if (tableFlag) {
				cssbuf.append("\twidth: auto;\n"); // table default
			} else {
				cssbuf.append("\twidth: 100%;\n"); // default
			}
		}
		
		// word-break (CSS3)
		if (decos.containsKey("word-break")) { // TODO
			cssbuf.append("\tword-break: " + decos.getStr("word-break") + ";\n");
		}
		
		// word-spacing
		if (decos.containsKey("word-spacing")) { // TODO
			if (isNumber(decos.getStr("word-spacing"))) {
				cssbuf.append("\tword-spacing: " + decos.getStr("word-spacing") + "px;\n");
			} else {
				cssbuf.append("\tword-spacing: " + decos.getStr("word-spacing") + ";\n");
			}
		}
		
		// word-wrap (CSS3)
		if (decos.containsKey("word-wrap")) { // TODO
			cssbuf.append("\tword-wrap: " + decos.getStr("word-wrap") + ";\n");
		}
		
		// これ以降CSSでないルール
		// tableタグで生成
		if (decos.containsKey("table")) { // TODO
			Log.out("********table start********");
			tableFlag = true;
		}
		
		if (cssbuf.length() > 0) { // css情報が1つ以上ある
			haveClass = 1;
			css.append("." + classid + "{\n");
			css.append(cssbuf);
			css.append("}\n");
			writtenClassId.addElement(classid);
		} else {
			Log.out("==> css style is null.");
			notWrittenClassId.addElement(classid);
		}
		Log.out("");
	}
	
	public static String commonCSS() { // デフォルトのcss付与
		// TODO
		String s = "";
		if (!GlobalEnv.isOpt()) {
			if (headerFlag && footerFlag) {
				s += "body {\n width: 1280px;\n margin-left: auto;\n margin-right: auto;\n padding-top: 30px;\n padding-bottom: 30px;\n}\n";
				s += ".header {\n position: fixed !important;\n position: absolute;\n top: 0;\n left: 0;\n width: 100%;\n height: 30px;\n background-color: #000000;\n color: #fff;\n}\n";
				s += ".footer {\n position: fixed !important;\n position: absolute;\n bottom: 0;\n left: 0;\n width: 100%;\n height: 30px;\n background-color: #000000;\n color: #fff;\n}\n";
			} else if (headerFlag) {
				s += "body {\n width: 1280px;\n margin-left: auto;\n margin-right: auto;\n padding-top: 30px;\n}\n";
				s += ".header {\n position: fixed !important;\n position: absolute;\n top: 0;\n left: 0;\n width: 100%;\n height: 30px;\n background-color: #000000;\n color: #fff;\n}\n";
			} else if (footerFlag) {
				s += "body {\n width: 1280px;\n margin-left: auto;\n margin-right: auto;\n padding-bottom: 30px;\n}\n";
				s += ".footer {\n position: fixed !important;\n position: absolute;\n bottom: 0;\n left: 0;\n width: 100%;\n height: 30px;\n background-color: #000000;\n color: #fff;\n}\n";
			} else {
				s += "body {\n width: 1280px;\n margin-left: auto;\n margin-right: auto;\n}\n";
			}
			s += ".row {\n display: flex;\n flex-direction: row;\n border: solid 1px #F0F0F0;\n text-align: center;\n}\n";
			s += ".col {\n display: flex;\n flex-direction: column;\n border: solid 1px #F0F0F0;\n text-align: center;\n}\n";
			s += ".att {\n margin-left: auto;\n margin-right: auto;\n border: solid 1px #F0F0F0;\n}\n";
		}
		return s;
	}
	
	public static String getClassID(ITFE tfe) {
		String result;
		
		// ここにC3, G3の何かが入る可能性
		
		result = "TFE" + tfe.getId();
		return result;
	}
	
	public void getHeader() { // ヘッダーの作成
		header.append("<html>\n");
		header.append("<head>\n");
		header_creation(); // ヘッダー情報の追加 cssなど
		header.append("</head>\n");
		header.append("<body>\n");
	}
	
	public void getFooter() { // フッダーの作成
		footer.append("</body>\n");
		footer.append("</html>\n");
	}
	
	public void header_creation() { // css等詳細ヘッダーの追加
		header.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		header.append("<!-- Genarated CSS -->\n");
		header.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + Jscss.getGenerateCssFileName(0) + "\">\n");
	}
	
	public boolean isNumber(String val) { // 文字列が全部数字であるかチェック
		String regex = "^\\-?[0-9]*\\.?[0-9]+$";
	    Pattern p = Pattern.compile(regex);
	    Matcher m = p.matcher(val);
	    return m.find();
	}
}
