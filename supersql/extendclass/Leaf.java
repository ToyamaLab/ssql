package supersql.extendclass;

import java.util.List;

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

	@Override
	public List<TreeNode<T>> getChildren() {
		return null;
	}

	@Override
	public void addChild(TreeNode<T> child) {
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
