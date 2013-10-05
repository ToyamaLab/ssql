package supersql.codegenerator;

import supersql.extendclass.ExtList;
import supersql.extendclass.Node;
import supersql.extendclass.TreeNode;

public class TFE implements ITFE {

	protected int id; // SchemaID
	protected boolean orderFlag;
	protected boolean aggregateFlag;
	protected String order;
	protected String aggregate;    
	public DecorateList decos;

	public TFE() {
		orderFlag = false;
		aggregateFlag = false;
		decos = new DecorateList();
	}
	
	@Override
	public void debugout(int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExtList<Integer> makesch() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public ExtList makele0() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public void work(ExtList<ExtList<String>> data_info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int countconnectitem() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setOrderBy(String order) {
		orderFlag = true;
		this.order = new String();
		this.order = order;
	}

	@Override
	public void setAggregate(String aggregate) {
		aggregateFlag = true;
		this.aggregate = new String();
		this.aggregate = aggregate;
	}


//	@Override
//	public ExtList makeschImage() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public void addDeco(String key, Object val) {
		decos.put(key, val);
	}
	
	@Override
	public void addDeco(String name, String value, String condition) {
		decos.put(name, value,condition);
	}

	@Override
	public void setDeco(DecorateList d) {
		decos = d;
	}

	@Override
	public Object createNode(ExtList<ExtList<String>> data_info) {
		return null;
	}

	public Object createNodeNew(TreeNode<String> dataChild) {
		return null;
	}

	@Override
	public TreeNode<String> makeTreeSchema() {
		// TODO Auto-generated method stub
		return null;
	}
}
