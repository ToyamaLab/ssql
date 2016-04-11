/*
 * Created on 2005/02/23 by hanki
 * Modified on 2006/07/01 by hanki
 */
package supersql.parser;

import supersql.codegenerator.Asc_Desc;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class Preprocessor {

	private static boolean is_order_by;
	private static boolean is_aggregate;

	private StringBuffer tmp;

	private static ExtList order_by_list;
	private static ExtList aggregate_list;

	/* constructor */
	public Preprocessor(String tfe) {

		is_order_by = false;
		is_aggregate = false;

		tmp = new StringBuffer();
		tmp.append(tfe);

		order_by_list = new ExtList();
		aggregate_list = new ExtList();

	}

    public static boolean isOrderBy() {
    	return is_order_by;
    }

    public static boolean isAggregate() {
    	return is_aggregate;
    }

	/* store "order by" information into a hashtable */
    public static void putOrderByTable(String order, ExtList sch) {
    	Log.out("order by list "+order+" "+sch);
    	order_by_list.add(order + sch);
	}

	/* return an "order by" hashtable */
    public static ExtList getOrderByTable() {
    	return order_by_list;
	}

	/* store "aggregate functions" information into a list */
	public static void putAggregateList(ExtList sch, String aggregate) {
		aggregate_list.add(sch.get(0) + " " + aggregate);
	}

	/* return an "aggregate functions" list */
	public static ExtList getAggregateList() {
		return aggregate_list;
	}

	/* push "order by" into the decoration */
    public StringBuffer pushOrderBy() {

    	boolean order_flag = false;

    	String token;

    	String order = null;
    	String close = null;

    	StringBuffer order_by_string = new StringBuffer();

    	TFEtokenizer st = new TFEtokenizer(tmp.toString());
    	
    	Asc_Desc ascDesc = new Asc_Desc();
//    	ascDesc.preProcess();

    	while (st.hasMoreTokens()) {

    		token = st.nextToken();
    		
    		
    		if(token.equals("dynamic")){
    			ascDesc.add_asc_desc_Array();
    			ascDesc.dynamicCount++;
    			
    			//TODO (asc)@{static}! (asc)@{dynamic}! 
    		}

    		/* 3. convert if there exist "order by" */
    		if (order_flag) {
    			ascDesc.addOrderBy(order, token);
    			/* decoration exists originally */
    			if (st.lookToken().equals("@")) {
    					token = token + st.nextToken();
    					token = token + st.nextToken();
    					token = token + order + ",";

    			/* decoration doesn't exist originally */
    			} else {
    					token = token + "@{" + order + "}";
    			}

    			order_flag = false;
    		}

    		/* 1. "(" found in the tfe */
    		if (token.equals("(")) {

    			/* it's "order by" */
    			if (st.lookToken().toLowerCase().indexOf("asc") > -1 ||
    				st.lookToken().toLowerCase().indexOf("desc") > -1) {

    				/* set flag for "order by" */
    				order_flag = true;
    				is_order_by = true;

    				order = st.nextToken();

    				/* check ")" */
    				close = st.nextToken();

					if (!close.equals(")")) {
    					Log.err("*** Can't close '" + order + "' ***");
    					throw (new IllegalStateException());
    				}

				/* it's not "order by" */
    			} else {
    				order_by_string.append(token);
    			}

    		/* 2. "(" not found in the tfe */
    		} else {
    			order_by_string.append(token);
    		}
    	}

    	/* log out for "-debug" */
    	if (isOrderBy()) {
    		Log.out("[Order By] tfe : " + order_by_string);
    	}

    	tmpUpdate(order_by_string.toString());

    	return order_by_string;

    }

    /* push "aggregate functions" into the decoration */
    public StringBuffer pushAggregate() {

    	boolean agg_flag = false;

    	String token;
    	String agg = null;

    	StringBuffer aggregate_string = new StringBuffer();

    	TFEtokenizer st = new TFEtokenizer(tmp.toString());

    	while (st.hasMoreTokens()) {

    		token = st.nextToken();

    		/* 3. convert if there exist "aggregate functions" */
    		if (agg_flag) {

    			/* check "[" */
    			if (!token.equals("[")) {
    				Log.err("*** Can't open '" + agg + "' ***");
    				throw (new IllegalStateException());
    			} else {
    				token = st.nextToken();
    			}

    			/* check "]" */
    			if (!st.lookToken().equals("]")) {
    				Log.err("*** Can't close '" + agg + "' ***");
					throw (new IllegalStateException());
    			} else {
    				st.nextToken();
    			}

    			/* push "aggregate functions" into the decoration */
    			token = token + "@{" + agg;

    			/* decoration doesn't exist originally */
    			if (!st.lookToken().equals("@")) {
    				token = token + "}";

    			/* decoration exists originally */
    			} else {
    				st.nextToken();

    				/* check "{" */
    				if (!st.lookToken().equals("{")) {
    			           Log.err("*** Illegal Token Found after Decoration token '@' ***");
    			           throw (new IllegalStateException());
    				} else {
    					st.nextToken();
    				}

    				token = token + ",";

    				/* attach remaining decoration until the right brace */
    				while (!st.lookToken().equals("}")) {
  						token = token + st.nextToken();
    				}

    				token = token + st.nextToken();

    			}

    			agg_flag = false;
     		}

    		/* 1. "aggregate functions" found */
  			if (token.equalsIgnoreCase("max") ||
    			token.equalsIgnoreCase("min") ||
				token.equalsIgnoreCase("avg") ||
    			token.equalsIgnoreCase("sum") ||
    			token.equalsIgnoreCase("count") /*||
    			//added by goto 20130122
				token.equalsIgnoreCase("slideshow")*/) {

    			/* set flag for "aggregate functions" */
    			agg_flag = true;
    			is_aggregate = true;
    			agg = token;

    		/* 2. "aggregate functions" not found */
    		} else {
    			aggregate_string.append(token);
    		}

    	}

    	/* log out for "-debug" */
    	if (isAggregate()) {
    		Log.out("[Aggregate] tfe : " + aggregate_string);
    	}

    	tmpUpdate(aggregate_string.toString());

    	return aggregate_string;

    }

    /* update the string "tmp" */
    private void tmpUpdate(String tfe) {

    	tmp.replace(0, tmp.length(), tfe);

    }

}
