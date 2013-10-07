package supersql.test;

import org.junit.Assert;
import org.junit.Test;

import supersql.extendclass.Leaf;
import supersql.extendclass.Node;
import supersql.extendclass.TreeNode;

/**
 * Tests for TreeNode operations.
 * 
 * @author lucasdchamps@gmail.com (Lucas Deschamps)
 */
public class TreeNodeTest {

	/**
	 * Explicitely test the custom equal method we 
	 * defined in TreeNode
	 */
	@Test
	public void equalTest() {
		TreeNode<String> tree1 = new Leaf<String>("0");
		TreeNode<String> tree2 = new Leaf<String>("0");
		Assert.assertTrue(tree1.equals(tree2));
		
		tree1 = new Node<String>();
		tree2 = new Node<String>();
		Assert.assertTrue(tree1.equals(tree2));
		
		tree1.setNodeData("0");
		tree2.setNodeData("0");
		Assert.assertTrue(tree1.equals(tree2));
		
		tree1.addChild(new Leaf<String>("0"));
		tree2.addChild(new Leaf<String>("0"));
		Assert.assertTrue(tree1.equals(tree2));
		
		TreeNode<String> subTree1 = new Node<String>();
		TreeNode<String> subTree2 = new Node<String>();
		subTree1.addChild(new Leaf<String>("0"));
		subTree2.addChild(new Leaf<String>("0"));
		tree1.addChild(subTree1);
		tree2.addChild(subTree2);
		Assert.assertTrue(tree1.equals(tree2));
 	}
	
	@Test
	public void unEqualTest() {
		TreeNode<String> tree1 = new Leaf<String>("0");
		TreeNode<String> tree2 = new Leaf<String>("1");
		Assert.assertFalse(tree1.equals(tree2));
		
		tree1 = new Node<String>();
		tree2 = new Node<String>();
		Assert.assertTrue(tree1.equals(tree2));
		
		tree1.setNodeData("0");
		tree2.setNodeData("1");
		Assert.assertFalse(tree1.equals(tree2));
		tree2.setNodeData("0");
		
		tree1.addChild(new Leaf<String>("0"));
		tree2.addChild(new Leaf<String>("1"));
		Assert.assertFalse(tree1.equals(tree2));
		tree2.childAt(0).setNodeData("0");
		
		TreeNode<String> subTree1 = new Node<String>();
		TreeNode<String> subTree2 = new Node<String>();
		subTree1.addChild(new Leaf<String>("0"));
		subTree2.addChild(new Leaf<String>("1"));
		tree1.addChild(subTree1);
		tree2.addChild(subTree2);
		Assert.assertFalse(tree1.equals(tree2));
	}
	
	@Test
	public void hashEqualTest() {
		TreeNode<String> tree1 = new Leaf<String>("0");
		TreeNode<String> tree2 = new Leaf<String>("0");
		Assert.assertEquals(tree1.hashCode(), tree2.hashCode());
		
		tree1 = new Node<String>();
		tree2 = new Node<String>();
		Assert.assertEquals(tree1.hashCode(), tree2.hashCode());
		
		tree1.setNodeData("0");
		tree2.setNodeData("0");
		Assert.assertEquals(tree1.hashCode(), tree2.hashCode());
		
		tree1.addChild(new Leaf<String>("0"));
		tree2.addChild(new Leaf<String>("0"));
		Assert.assertEquals(tree1.hashCode(), tree2.hashCode());
		
		TreeNode<String> subTree1 = new Node<String>();
		TreeNode<String> subTree2 = new Node<String>();
		subTree1.addChild(new Leaf<String>("0"));
		subTree2.addChild(new Leaf<String>("0"));
		tree1.addChild(subTree1);
		tree2.addChild(subTree2);
		Assert.assertEquals(tree1.hashCode(), tree2.hashCode());
	}
	
	@Test
	public void hashUnqualTest() {
		TreeNode<String> tree1 = new Leaf<String>("0");
		TreeNode<String> tree2 = new Leaf<String>("1");
		Assert.assertNotEquals(tree1.hashCode(), tree2.hashCode());
		
		tree1 = new Node<String>();
		tree2 = new Node<String>();
		Assert.assertEquals(tree1.hashCode(), tree2.hashCode());
		
		tree1.setNodeData("0");
		tree2.setNodeData("1");
		Assert.assertNotEquals(tree1.hashCode(), tree2.hashCode());
		tree2.setNodeData("0");
		
		tree1.addChild(new Leaf<String>("0"));
		tree2.addChild(new Leaf<String>("1"));
		Assert.assertNotEquals(tree1.hashCode(), tree2.hashCode());
		tree2.childAt(0).setNodeData("0");
		
		TreeNode<String> subTree1 = new Node<String>();
		TreeNode<String> subTree2 = new Node<String>();
		subTree1.addChild(new Leaf<String>("0"));
		subTree2.addChild(new Leaf<String>("1"));
		tree1.addChild(subTree1);
		tree2.addChild(subTree2);
		Assert.assertNotEquals(tree1.hashCode(), tree2.hashCode());
	}
	
}
