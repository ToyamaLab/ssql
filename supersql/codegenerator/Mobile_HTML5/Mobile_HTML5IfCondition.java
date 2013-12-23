package supersql.codegenerator.Mobile_HTML5;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Connector;
import supersql.codegenerator.Function;
import supersql.codegenerator.IfCondition;
import supersql.codegenerator.Manager;
import supersql.codegenerator.TFE;
import supersql.extendclass.ExtList;

public class Mobile_HTML5IfCondition extends IfCondition {

	public Mobile_HTML5IfCondition(Manager manager, Mobile_HTML5Env html_env, Mobile_HTML5Env html_env2, Attribute condition, TFE thenTfe, TFE elseTfe) {
		super(condition, thenTfe, elseTfe);
	}
	
	public String work(ExtList data_info) {

		String conditionResult = (String) data_info.get(0);
		if(conditionResult.equals("t") || conditionResult.equals("1")) {
			if(thenTfe instanceof Connector || thenTfe instanceof Attribute
				|| thenTfe instanceof Function || thenTfe instanceof IfCondition)
				thenTfe.work((ExtList)data_info.ExtsubList(1, thenTfe.countconnectitem()+1));
		}
		if((((data_info.get(0))).toString()).equals("t")){
			if(thenTfe instanceof Connector || thenTfe instanceof Attribute
				|| thenTfe instanceof Function || thenTfe instanceof IfCondition)
				thenTfe.work(data_info.ExtsubList(1, thenTfe.countconnectitem()+1));
			else
				thenTfe.work((ExtList)data_info.get(1));

		}
		else if(elseTfe != null){
			if(elseTfe instanceof Connector || elseTfe instanceof Attribute
					|| elseTfe instanceof Function || elseTfe instanceof IfCondition)
				elseTfe.work(data_info.ExtsubList(2, data_info.size()));
			else
				elseTfe.work((ExtList)data_info.get(2));
			}
		return null;		
		}
}
