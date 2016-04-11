package supersql.codegenerator.HTML_Flexbox;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Connector;
import supersql.codegenerator.Factory;
import supersql.codegenerator.Grouper;
import supersql.codegenerator.IfCondition;
import supersql.codegenerator.Manager;
import supersql.codegenerator.TFE;

//Operator, Manager���������륯�饹

public class HTML_FlexboxFactory extends Factory {

	@Override
	public void createLocalEnv() {
		HTML_FlexboxEnv.createHTMLDocument();
		setClassPrefix("supersql.codegenerator.HTML_Flexbox.HTML_Flexbox");
	}

	@Override
	public Connector createC4(Manager manager) {
		return new HTML_FlexboxC1();
	}

	@Override
	public Grouper createG0(Manager manager) {
		return new HTML_FlexboxG1();
	}

	@Override
	public Grouper createG4(Manager manager) {
		return new HTML_FlexboxG1();
		//return new HTMLG4(manager, html_env);
	}


	@Override
	public Attribute createConditionalAttribute(Manager manager) {
		return new HTML_FlexboxAttribute(true);
	}

	@Override
	public IfCondition createIfCondition(Manager manager, supersql.codegenerator.Attribute condition, TFE thenTfe, TFE elseTfe) {
		return new HTML_FlexboxIfCondition(condition, thenTfe, elseTfe);
	}

}
