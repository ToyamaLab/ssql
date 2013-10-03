package supersql.extendclass;

public class Leaf<T> extends TreeNode<T>{
	public Leaf(T nodeData) {
		this.setNodeData(nodeData);
	}
	
	public boolean isLeaf() {
		return true;
	}
	
	public String toString() {
		return this.getNodeData().toString();
	}
}
