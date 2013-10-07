package supersql.extendclass;

import java.util.List;

public abstract class TreeNode<T> {
	private T nodeData;
	
	public abstract boolean isLeaf();
	public abstract boolean isEmpty();
	public abstract int size();
	public abstract List<TreeNode<T>> getChildren();
	public abstract TreeNode<T> childAt(int index);
	public abstract void addChild(TreeNode<T> child);
	public abstract String toString();

	
	public T getNodeData() {
		return this.nodeData;
	}

	public void setNodeData(T nodeData) {
		this.nodeData = nodeData;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof TreeNode<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		TreeNode<T> other = (TreeNode<T>) obj;
		
		if (this.getNodeData() == null) {
			if (!(other.getNodeData() == null))
				return false;
		} else if (!this.getNodeData().equals(other.getNodeData())) {
			return false;
		}
		
		if (this.isLeaf()) {
			if (!other.isLeaf())
				return false;
		}
		
		int size = this.size();
		if (size != other.size()) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if(!this.childAt(i).equals(other.childAt(i))) {
				return false;
			}
		}
		
		return true;
	}
}
