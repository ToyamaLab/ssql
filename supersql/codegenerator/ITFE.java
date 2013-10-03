/* TFE abstract class */

package supersql.codegenerator;

import org.w3c.dom.Element;

import supersql.extendclass.ExtList;
import supersql.extendclass.Node;
import supersql.extendclass.TreeNode;

/**
 *
 *
 *
 */
public interface ITFE {

	/**
	 *
	 * @param count
	 */
	void debugout(int count);

	/**
	 *
	 * @return
	 */
	ExtList<Integer> makesch();

	ExtList makele0();

	void work(ExtList<ExtList<String>> data_info);
	Object createNode(ExtList<ExtList<String>> data_info);

	int countconnectitem();

	void addDeco(String key, Object val);
	void setDeco(DecorateList d);
	
	void setId(int id);
	int getId();

	void setOrderBy(String order);
	void setAggregate(String aggregate);

	ExtList makeschImage();

	void addDeco(String name, String value, String condition);

	Object createNodeNew(TreeNode<String> dataRoot);

	Node<String> makeTreeSchema();
}
