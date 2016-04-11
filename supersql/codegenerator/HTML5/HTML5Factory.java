package supersql.codegenerator.HTML5;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Connector;
import supersql.codegenerator.Factory;
import supersql.codegenerator.Function;
import supersql.codegenerator.Grouper;
import supersql.codegenerator.Manager;

public class HTML5Factory extends Factory {
	
	private HTML5Env html5Env;
	private HTML5Env html5Env2;
	
	@Override
	public Attribute createAttribute(Manager manager) {
		return new HTML5Attribute(manager, html5Env, html5Env2);
	}
	
	@Override
	public Function createFunction(Manager manager) {
		return new HTML5Function(manager, html5Env, html5Env2);
	}
	
	@Override
	public Connector createC1(Manager manager) {
		return new HTML5C1(manager, html5Env, html5Env2);
	}
	
	@Override
	public Connector createC2(Manager manager) {
		return new HTML5C2(manager, html5Env, html5Env2);
	}
	
	@Override
	public Connector createC3(Manager manager) {
		return new HTML5C3(manager, html5Env, html5Env2);
	}
	
	@Override
	public Grouper createG1(Manager manager) {
		return new HTML5G1(manager, html5Env, html5Env2);
	}
	
	@Override
	public Grouper createG2(Manager manager) {
		return new HTML5G2(manager, html5Env, html5Env2);
	}
	
	@Override
	public void createLocalEnv() {
		html5Env = new HTML5Env();
		html5Env2 = new HTML5Env();
	}
	
	@Override
	public Manager createManager() {
		return new HTML5Manager(html5Env, html5Env2);
	}
}
