package supersql.extendclass;

import java.util.List;

public abstract class TreeNode<T> {
	private T nodeData;
	
	public abstract boolean isLeaf();
	public abstract String toString();
	public abstract List<TreeNode<T>> getChildren();
	
	public T getNodeData() {
		return this.nodeData;
	}

	public void setNodeData(T nodeData) {
		this.nodeData = nodeData;
	}
}
