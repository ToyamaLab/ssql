package supersql.dataconstructor;

import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.extendclass.Leaf;
import supersql.extendclass.Node;
import supersql.extendclass.TreeNode;
import supersql.parser.Preprocessor;

/**
 * ŽÌŽÚŽ¹Ž½ŽÂŽ¤Ž¥Ž¸Ž¥Ž§Ž¥ŽÍŽ¥?Ž¡Ž¼Ž¥Ž¿Ž¥Ž¯Ž¥ò§Ž¹
 */
public class TreeGenerator {

	public ExtList makeTree(ExtList sch, ExtList<ExtList<String>> tuples) {
		ExtList result = new ExtList();

		if (Preprocessor.isAggregate()) {
			ExtList info = new ExtList();
			ExtList criteria_set = new ExtList();
			Aggregate aggregate = new Aggregate();

			info = Preprocessor.getAggregateList();
			tuples = aggregate.aggregate(criteria_set, info, sch, tuples);
		}

		for (int i = 0; i < tuples.size(); i++) {
			result = nest_tuple(sch, tuples.get(i));
			tuples.set(i, result);
		}

		if(tuples.size() != 0)
		{
			SortNesting sn = new SortNesting();
			long start = System.currentTimeMillis();
			sn.bufferall(tuples);
			long end = System.currentTimeMillis();
			System.out.println("bufferAll time: " + (end - start) + "msec");

			if (Preprocessor.isOrderBy()) {
				ExtList info = new ExtList();		
				info = OrderBy.tableToList(Preprocessor.getOrderByTable());
				result = new ExtList(sn.GetResultWithOrderBy(info, sch));
				Log.out("= orderBy completed =");
			} else {
				result = new ExtList();
				result.add(sn.GetResult());
			}
			return result;
		}
		else
			return result;
	}

	public TreeNode<String> makeTreeNew(ExtList<ExtList<String>> tuples, TreeNode<String> schema) {
		TreeNode<String> result = new Node<String>();
		TreeNode<String> nestedTuples = new Node<String>();

//		if (Preprocessor.isAggregate()) {
//			ExtList info = new ExtList();
//			ExtList criteria_set = new ExtList();
//			Aggregate aggregate = new Aggregate();
//
//			info = Preprocessor.getAggregateList();
//			tuples = aggregate.aggregate(criteria_set, info, sch, tuples);
//		}

		for (int i = 0; i < tuples.size(); i++) {
			//result = nest_tuple(sch, tuples.get(i));
			TreeNode<String> nestedTuple = nestTuple(schema, tuples.get(i), 0);
			nestedTuples.addChild(nestedTuple);
			//tuples.set(i, result);
		}

		if(nestedTuples.size() != 0)
		{
			SortNesting sn = new SortNesting();
			long start = System.currentTimeMillis();
			sn.bufferallNew(nestedTuples);
			long end = System.currentTimeMillis();
			System.out.println("bufferAll time: " + (end - start) + "msec");

//			if (Preprocessor.isOrderBy()) {
//				ExtList info = new ExtList();		
//				info = OrderBy.tableToList(Preprocessor.getOrderByTable());
//				result = new ExtList(sn.GetResultWithOrderBy(info, sch));
//				Log.out("= orderBy completed =");
//			} else {
			result = sn.getResult();
//			}

			return result;
		}
		else
			return result;
	}
	
	private ExtList nest_tuple(ExtList sch, ExtList<String> tuple) {
		int tidx = 0;
		int count;
		ExtList result = new ExtList();
		Object o;
		for (int idx = 0; idx < sch.size(); idx++) {
			o = sch.get(idx);
			if (o instanceof ExtList) {
				count = ((ExtList) o).contain_itemnum();
				result.add(nest_tuple((ExtList) o, tuple.ExtsubList(tidx, tidx
						+ count)));
				tidx += count;
			} else {
				result.add(tuple.get(tidx));
				tidx++;
			}
		}

		return result;
	}
	
	private TreeNode<String> nestTuple(TreeNode<String> schema, ExtList<String> tuple, int tupleIndex) {
		TreeNode<String> nestedTuple = new Node<String>();
		
		if (schema.isLeaf()) {
			nestedTuple = new Leaf<String>(tuple.get(tupleIndex));
		} else {
			for(TreeNode<String> subSchema : schema.getChildren()) {
				nestedTuple.addChild(nestTuple(subSchema, tuple, tupleIndex));
				tupleIndex++;
			}
		}
		
		return nestedTuple;
	}

}