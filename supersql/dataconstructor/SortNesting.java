package supersql.dataconstructor;

import java.util.Enumeration;
import java.util.Hashtable;

import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.extendclass.Leaf;
import supersql.extendclass.Node;

/* ŽÆ?ŽÎŽÏŽ¤ŽÏ schŽ¤ŽËŽ¤Ž½Ž¤ŽÃŽ¤Ž¿nestŽ¤Ž¬Ž½ŽªŽ¤?Ž¤ŽÃŽ¤Ž¿tupleŽ¤ŽÇŽ¤Ž¢Ž¤?Ž¤Ž³Ž¤ŽÈ */

public class SortNesting {

	Hashtable BufferedData;

	public SortNesting() {
		BufferedData = new Hashtable();
	}

	public SortNesting(ExtList t) {
		BufferedData = new Hashtable();
		buffered(t);
	}

	public void bufferall(ExtList tuples) {
		for (int i = tuples.size() - 1; i >= 0; i--) {
			buffered((ExtList) tuples.get(i));
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

	public Node<String> getResultNew() {
		ExtList buffer;
		Node<String> result = new Node<String>();
		Enumeration e = BufferedData.elements();

		while (e.hasMoreElements()) {
			buffer = (ExtList) e.nextElement();
			Node<String> child = new Node<String>();
			for (int i = 0; i < buffer.size(); i++) {
				Object bufferItem = buffer.get(i);
				if (buffer.get(i) instanceof SortNesting) {
					child = ((SortNesting) (bufferItem)).getResultNew();
				} else {
					child = new Leaf<String>(bufferItem.toString());
				}
				result.addChild(child);
			}
		}
		
		return result;
	}
	
	//hanki start
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
	//hanki end
	
	@Override
	public String toString() {
		return "[SortNesting:" + BufferedData + "]";
	}
}