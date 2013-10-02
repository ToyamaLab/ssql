package supersql.extendclass;

import java.util.ArrayList;
import java.util.List;
    
public class Node<T> {
	private List<Node<T>> children;
	private T nodeData;

	public Node() {
		this.setChildren(new ArrayList<Node<T>>());
	}

	public Node(T nodeData) {
		this.setChildren(new ArrayList<Node<T>>());
		this.setNodeData(nodeData);
	}

	public List<Node<T>> getChildren() {
		return children;
	}

	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}

	public void addChild(Node<T> child) {
		this.children.add(child);
	}

	public boolean isLeaf() {
		return false;
	}

	public boolean isEmpty() {
		return this.children.isEmpty();
	}

	public void setNodeData(T nodeData) {
		this.nodeData = nodeData;
	}

	public T getNodeData() {
		return nodeData;
	}

	public String toString() {
		String out = "[";
		String delim = "";
		for (Node<T> child : this.getChildren()) {
			out += delim + child.toString();
			delim = ", ";
		}
		return out + "]";
	}
}