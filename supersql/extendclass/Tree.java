package supersql.extendclass;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private Node<T> root;

    public Tree() {
        root = new Node<T>();
        root.setChildren(new ArrayList<Node<T>>());
    }
    
    public Node<T> getRoot() {
    	return root;
    }
    
    public static class Node<T> {
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
    }
    
    public static class Leaf<T> extends Node<T> {
    	
		public Leaf(T nodeData) {
			this.setNodeData(nodeData);
		}

		public boolean isLeaf() {
			return true;
		}
    }
}