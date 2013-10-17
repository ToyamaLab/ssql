package supersql.dataconstructor;

import java.util.Enumeration;
import java.util.Hashtable;

import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.extendclass.Leaf;
import supersql.extendclass.Node;
import supersql.extendclass.TreeNode;

/* ŽÆ?ŽÎŽÏŽ¤ŽÏ schŽ¤ŽËŽ¤Ž½Ž¤ŽÃŽ¤Ž¿nestŽ¤Ž¬Ž½ŽªŽ¤?Ž¤ŽÃŽ¤Ž¿tupleŽ¤ŽÇŽ¤Ž¢Ž¤?Ž¤Ž³Ž¤ŽÈ */

public class SortNesting {

	private Hashtable BufferedData;
	private Hashtable<ExtList<String>, SortNesting> nestingMap;
	private TreeNode<String> buffer;
	
	public SortNesting() {
		this.BufferedData = new Hashtable();
		this.nestingMap = new Hashtable<ExtList<String>, SortNesting>();
		this.buffer = new Node<String>();
	}

	public SortNesting(ExtList t) {
		this.BufferedData = new Hashtable();
		buffered(t);
	}

	public void bufferall(ExtList nestedTuples) {
		for (int i = nestedTuples.size() - 1; i >= 0; i--) {
			buffered((ExtList) ((ExtList) nestedTuples.get(i)).get(0));
		}
	}
	
	public void bufferallNew(TreeNode<String> nestedTuples) {
		for(TreeNode<String> nestedTuple : nestedTuples.getChildren()) {
			this.bufferedNew(nestedTuple);
		}
	}

	private void buffered(ExtList t) {
		ExtList ExtListkey = this.KeyAtt(t);

		if (!BufferedData.containsKey(ExtListkey)) {
			ExtList buffer = new ExtList();
			ExtList o;
			for (int i = 0; i < t.size(); i++) {
				if (t.get(i) instanceof String) {
					buffer.add(t.get(i));
				} else {
					SortNesting s = new SortNesting((ExtList) t.get(i));
					buffer.add(s);
				}
			}
			BufferedData.put(ExtListkey, buffer);
		} else {
			ExtList gotExtList = (ExtList) (BufferedData.get(ExtListkey));
			for (int idx = 0; idx < gotExtList.size(); idx++) {
				Object o;
				o = gotExtList.get(idx);
				if (o instanceof SortNesting) {
					((SortNesting) o).buffered((ExtList) t.get(idx));
				}
			}
			BufferedData.remove(ExtListkey);
			BufferedData.put(ExtListkey, gotExtList);
		}
	}

	
	private void bufferedNew(TreeNode<String> nestedTuple) {
		if (nestedTuple.isLeaf()) {
			this.buffer = nestedTuple;
			return;
		}
		
		ExtList<String> ExtListKey = this.KeyAttNew(nestedTuple);
		SortNesting s;

		// We only have attributes
		if (ExtListKey.size() == nestedTuple.size()) {
			for (TreeNode<String> child : nestedTuple.getChildren()) {
				this.buffer.addChild(child);
			}
			return;
		}
		
		// We have nested connectors or nested groupers
		if (!nestingMap.containsKey(ExtListKey)) {
			s = new SortNesting();
			for (TreeNode<String> child : nestedTuple.getChildren()) {
				if (child.isLeaf()) {
					this.buffer.addChild(child);
				} else {
					s.bufferedNew(child);
				}
			}
			this.nestingMap.put(ExtListKey, s);
		} else {
			s = this.nestingMap.get(ExtListKey);
			for (TreeNode<String> child : nestedTuple.getChildren()) {
				if (child.isLeaf()) {
					this.buffer.addChild(child);
				} else {
					s.bufferedNew(child);
				}
			}
		}
	}

	private ExtList KeyAtt(ExtList t) {
		ExtList o;
		ExtList result = new ExtList();

		for (int i = 0; i < t.size(); i++) {
			if (t.get(i) instanceof String) {
				result.add(t.get(i));
			}
		}
		return result;
	}
	
	private ExtList<String> KeyAttNew(TreeNode<String> nestedTuple) {
		ExtList<String> result = new ExtList<String>();

		for (TreeNode<String> child : nestedTuple.getChildren()) {
			if (child.isLeaf()) {
				result.add(child.getNodeData());
			}
		}
		
		return result;
	}
	
	public ExtList GetResult() {
		ExtList result = new ExtList();
		ExtList buffer, buffer1;

		Enumeration e = BufferedData.elements();

		while (e.hasMoreElements()) {
			buffer = (ExtList) e.nextElement();
			for (int i = 0; i < buffer.size(); i++) {
				if (buffer.get(i) instanceof SortNesting) {
					buffer1 = ((SortNesting) (buffer.get(i))).GetResult();
					buffer.set(i, buffer1);
				}
			}
			result.add(buffer);
		}
		
		return result;
	}

	public TreeNode<String> getResult() {
		TreeNode<String> result = new Node<String>();
		
		if (this.nestingMap.isEmpty()) {
			result = this.buffer;
		} else {
			Enumeration<ExtList<String>> keyBuffers = this.nestingMap.keys();
			while(keyBuffers.hasMoreElements()) {
				ExtList<String> keyBuffer = keyBuffers.nextElement();
				for (String field : keyBuffer) {
					result.addChild(new Leaf<String>(field));
				}
				result.addChild(nestingMap.get(keyBuffer).getResult());
			}
		}
		
		return result;
	}
	
	public ExtList GetResultWithOrderBy(ExtList info, ExtList sch) {

		int a;
		ExtList result = new ExtList();
		ExtList buffer, buffer1;

		Enumeration e = BufferedData.elements();
		
		while (e.hasMoreElements()) {
			buffer = (ExtList) e.nextElement();
			for (int i = 0; i < buffer.size(); i++) {
				if (buffer.get(i) instanceof SortNesting) {
					buffer1 = ((SortNesting) (buffer.get(i))).GetResultWithOrderBy(info, (ExtList)sch.get(i));
					buffer.set(i, buffer1);
				}
			}
			result.add(buffer);
		}
		
		Log.out(" * sort at the schema level " + sch + " *");
		Log.out(" " + result);

		/* sort from the deepest schema level */
		OrderBy order_by = new OrderBy();
		
		for (int i = 0; i < info.size(); i++) {
			for (int j = 0; j < sch.size(); j++) {
				a = info.get(i).toString().indexOf(" ");
				if (info.get(i).toString().substring(0, a).equals
						(sch.get(j).toString())) {
					
					result = order_by.sort(info.get(i).toString(), sch, result);
					
				}
			}
		}
	
		return result;
	}

	@Override
	public String toString() {
		return "[SortNesting:" + this.buffer + "]";
	}
}