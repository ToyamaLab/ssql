package supersql.extendclass;

public abstract class TreeNode<T> {
	private T nodeData;
	
	public abstract boolean isLeaf();
	public abstract String toString();
	
	public T getNodeData() {
		return this.nodeData;
	}

	public void setNodeData(T nodeData) {
		this.nodeData = nodeData;
	}
}
