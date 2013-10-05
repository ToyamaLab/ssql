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

	public TreeNode<String> makeTreeNew(TreeNode<String> schemaTree, ExtList schema, ExtList<ExtList<String>> tuples) {
		TreeNode<String> result = new Node<String>();
		ExtList nestedTuple = new ExtList();

		if (Preprocessor.isAggregate()) {
			ExtList info = new ExtList();
			ExtList criteria_set = new ExtList();
			Aggregate aggregate = new Aggregate();

			info = Preprocessor.getAggregateList();
			tuples = aggregate.aggregate(criteria_set, info, schema, tuples);
		}

		for (int i = 0; i < tuples.size(); i++) {
			nestedTuple = nest_tuple(schema, (ExtList) tuples.get(i));
			tuples.set(i, nestedTuple);
		}

		if(tuples.size() != 0)
		{
			SortNesting sn = new SortNesting();
			sn.bufferall(tuples);

//			if (Preprocessor.isOrderBy()) {
//				ExtList info = new ExtList();		
//				info = OrderBy.tableToList(Preprocessor.getOrderByTable());
//				result = new ExtList(sn.GetResultWithOrderBy(info, sch));
//			} else {
			result.addChild(sn.getResultNew());
			return result;
		}
		else
			return result;
	}

	public ExtList makeTree(ExtList sch, ExtList tuples) {
		ExtList result = new ExtList();

		if (Preprocessor.isAggregate()) {
			ExtList info = new ExtList();
			ExtList criteria_set = new ExtList();
			Aggregate aggregate = new Aggregate();

			info = Preprocessor.getAggregateList();
			tuples = aggregate.aggregate(criteria_set, info, sch, tuples);
		}

		for (int i = 0; i < tuples.size(); i++) {
			result = nest_tuple(sch, (ExtList) tuples.get(i));
			tuples.set(i, result);
		}

		if(tuples.size() != 0)
		{
			SortNesting sn = new SortNesting();
			sn.bufferall(tuples);

			if (Preprocessor.isOrderBy()) {
				ExtList info = new ExtList();		
				info = OrderBy.tableToList(Preprocessor.getOrderByTable());
				result = new ExtList(sn.GetResultWithOrderBy(info, sch));
				Log.out("= orderBy completed =");
			} else {
				result = new ExtList(sn.GetResult());
			}
			tuples.clear();
			tuples.addAll(((ExtList) result));

			return tuples;
		}
		else
			return tuples;
	}
	
	private ExtList nest_tuple(ExtList sch, ExtList tuple) {
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

}