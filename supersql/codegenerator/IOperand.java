/* TFE abstract class */

package supersql.codegenerator;

import supersql.extendclass.ExtList;

public abstract interface IOperand extends ITFE {

	public String work(ExtList<ExtList<String>> data_info);

}