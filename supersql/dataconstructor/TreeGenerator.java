package supersql.dataconstructor;

import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.parser.Preprocessor;

/**
 * 鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申?鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申鐃緒申
 */
public class TreeGenerator {

	public TreeGenerator() {
	}

	public ExtList makeTree(ExtList sch, ExtList tuples) {
		//		public void makeTree(ExtList sch, ExtList tuples) {

		ExtList result = new ExtList();
		Log.out("= makeTree =");
		Log.out("sch : " + sch);
		Log.out("tuples : " + tuples);

		//hanki start
		if (Preprocessor.isAggregate()) {

			ExtList info = new ExtList();
			ExtList criteria_set = new ExtList();
			Aggregate aggregate = new Aggregate();

			Log.out("= aggregate started =");

			info = Preprocessor.getAggregateList();
			tuples = aggregate.aggregate(criteria_set, info, sch, tuples);

			Log.out("= aggregate completed =");
			Log.out("tuples : " + tuples);

		}
		//hanki end

		for (int i = 0; i < tuples.size(); i++) {
			result = nest_tuple(sch, (ExtList) tuples.get(i));
			//			Log.out("result = " + result);
			tuples.set(i, result);
		}

		Log.out("= nest_tuple end =");
		Log.out("tuples : " + tuples);
		//tk start/////
		if(tuples.size() != 0)
		{
			//tk end///////
			SortNesting sn = new SortNesting();
			sn.bufferall(tuples);

		//hanki start
		if (Preprocessor.isOrderBy()) {

			ExtList info = new ExtList();

			Log.out("= order by started =");
			Log.out(" * schema : " + sch + " *");
Log.out("BEFORE"+Preprocessor.getOrderByTable());
			info = OrderBy.tableToList(Preprocessor.getOrderByTable(), sch.contain_itemnum());
Log.out("AFTER "+info);
			result = new ExtList(sn.GetResultWithOrderBy(info, sch));

			Log.out("= orderBy completed =");

		} else {
		//hanki end

			result = new ExtList(sn.GetResult());

		//hanki start
		}
		//hanki end

;
		tuples.clear();
		tuples.addAll(((ExtList) result.get(0)));
		Log.out("= makeTree end =");

		//hanki
		//return;
		return tuples;

		//tk start///////////////////////////////////////////////
		}
		else
			return tuples;
		//tk end//////////////////////////////////////////////////
	}


	private ExtList nest_tuple(ExtList sch, ExtList tuple) {
		int tidx = 0;
		int count;
		ExtList result = new ExtList();
		Object o;
		//		Log.out("sch = "+sch);
		//		Log.out("tuple = "+tuple);

		for (int idx = 0; idx < sch.size(); idx++) {
			o = sch.get(idx);
			//			Log.out("sep_sch = "+o);
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
		//		Log.out("result = "+result);
		return result;
	}

}