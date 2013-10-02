package supersql.extendclass;

public class Leaf<T> extends Node<T>{
	
	public Leaf(T nodeData) {
		super(nodeData);
	}
	
	public boolean isLeaf() {
		return true;
	}
	
	public String toString() {
		return this.getNodeData().toString();
	}
}
