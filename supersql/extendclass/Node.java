package supersql.extendclass;

import java.util.ArrayList;
import java.util.List;
    
public class Node<T> extends TreeNode<T> {
	private List<TreeNode<T>> children;

	public Node() {
		this.children = new ArrayList<TreeNode<T>>();
	}

	public Node(T nodeData) {
		this.children = new ArrayList<TreeNode<T>>();
		this.setNodeData(nodeData);
	}

	@Override
	public int size() {
		return this.children.size();
	}
	
	public boolean isLeaf() {
		return false;
	}
	
	public boolean isEmpty() {
		return this.children.isEmpty();
	}
	
	public List<TreeNode<T>> getChildren() {
		return children;
	}
	
	@Override
	public TreeNode<T> childAt(int index) {
		return this.children.get(index);
	}

	public void addChild(TreeNode<T> child) {
		this.children.add(child);
	}

	public String toString() {
		String out = "[";
		String delim = "";
		for (TreeNode<T> child : this.getChildren()) {
			out += delim + child.toString();
			delim = ", ";
		}
		return out + "]";
	}
}