package supersql.extendclass;

import java.util.List;

public class Leaf<T> extends TreeNode<T>{
	public Leaf(T nodeData) {
		this.setNodeData(nodeData);
	}
	
	public boolean isLeaf() {
		return true;
	}
	
	@Override
	public int size() {
		return 0;
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
		return true;
	}

	@Override
	public TreeNode<T> childAt(int index) {
		return null;
	}
	
	public String toString() {
		return this.getNodeData().toString();
	}
}
