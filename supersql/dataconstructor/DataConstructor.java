//proposed process
package supersql.dataconstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import supersql.codegenerator.AttributeItem;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.db.ConnectDB;
import supersql.db.GetFromDB;
import supersql.extendclass.ExtList;
import supersql.extendclass.Node;
import supersql.extendclass.TreeNode;
import supersql.parser.SSQLparser;

public class DataConstructor {

	private ExtList<ExtList<String>> dataTable;
	private TreeNode<String> dataTree;
	private ArrayList<SQLQuery> sqlQueries = null;
	private QueryDivider qd; 
	private String key = null;
	private Attribute keyAtt = null;
	private int col = -1;
	private boolean flag = true;
	public static String SQL_string;	//added by goto 20130306  "FROMなしクエリ対策"
	
	public DataConstructor(SSQLparser parser) {
		MakeSQL msql = null;
		ExtList sep_sch = parser.get_TFEschema().makesch();
		
		//Check Optimization Parameters
		if ( GlobalEnv.getOptLevel() == 0 || !GlobalEnv.isOptimizable() || SSQLparser.isDbpediaQuery())
		{
			sqlQueries = null;
		}
		else
		{
			//Initialize QueryDivider
			try 
			{
			    qd = new QueryDivider( parser );
			    if ( qd.MakeGraph() )
			    {
			    	//if graph was made successfully, divide
			    	sqlQueries = qd.divideQuery();	
			    }
			}
			catch ( Exception e )
			{
			}
		}
		
		//Make SQL
		if ( (sqlQueries == null || sqlQueries.size() < 2) && !SSQLparser.isDbpediaQuery())
		{
			//if graph was not made successfully or
			//if graph has only one connected component
			//query cannot be divided
			msql = new MakeSQL(parser);
		}

		ExtList<ExtList<String>> sep_dataTable = new ExtList<ExtList<String>>();
		if(SSQLparser.isDbpediaQuery()){
			sep_dataTable = schemaToData(parser, sep_sch, sep_dataTable);
		}
		else {
			dataTree = schemaToDataNew(parser, msql, sep_sch, sep_dataTable);
			dataTable = schemaToData(parser, msql, sep_sch, sep_dataTable);
		}
	}

	private ExtList schemaToData(SSQLparser parser, ExtList sep_sch,
			ExtList sep_dataTable) {
		int attno = parser.get_att_info().size();
		String[] array = new String[attno];
		int i = 0;
		for(Object info : parser.get_att_info().values()){
			String infoText = ((AttributeItem)info).toString();
			array[i] = infoText;
			i++;
		}
		sep_dataTable = getDataFromDBPedia(parser.get_where_info().getSparqlWhereQuery(), array);
		TreeNode<String> schema = parser.get_TFEschema().makeTreeSchema();
		sep_dataTable = makeTree(sep_sch, sep_dataTable, schema);
		return sep_dataTable;
	}

	private ExtList schemaToData(SSQLparser parser, MakeSQL msql, ExtList sep_sch,
			ExtList sep_dataTable) {

		if ( msql != null )
		{
		    getFromDB(msql, sep_sch, sep_dataTable);
		    TreeNode<String> schema = parser.get_TFEschema().makeTreeSchema();
		    sep_dataTable = makeTree(sep_sch, sep_dataTable, schema); 
		}
		else 
		{
	        getTuples(sep_sch, sep_dataTable);
		    sep_dataTable = MakeTree( qd.getSchema() );
		}
		
        return sep_dataTable;
	}

	private TreeNode<String> schemaToDataNew(SSQLparser parser, MakeSQL msql, ExtList sep_sch, ExtList<ExtList<String>> data) {
		TreeNode<String> dataTree = new Node<String>();
		TreeGenerator tg = new TreeGenerator();
		
		if(msql != null) {
		    data = getFromDB(msql, sep_sch, data);
		    TreeNode<String> schema = parser.get_TFEschema().makeTreeSchema();
			dataTree = tg.makeTreeNew(data, schema); 
		} else {
	        //getTuples(schema, data);
		    //sep_dataTable = MakeTree( qd.getSchema() );
		}
		return dataTree;
	}
	
	private ExtList[] getTuples( ExtList sep_sch, ExtList sep_dataTable) {
		ExtList[] table;
		GetFromDB gfd;
		int comp_size;
		
		comp_size = sqlQueries.size();
		table = new ExtList[comp_size];
		
		if(GlobalEnv.isMultiThread())
		{
			System.out.println("[Enter MultiThread mode]");
			ConnectDB cdb = new ConnectDB(GlobalEnv.geturl(),GlobalEnv.getusername(), GlobalEnv.getDriver(), GlobalEnv.getpassword());
			System.out.println(GlobalEnv.geturl() + GlobalEnv.getusername() + GlobalEnv.getpassword());
		
			cdb.setName("CDB1");
			cdb.run();
		
			gfd = new GetFromDB(cdb);
		}

		else{
			gfd = new GetFromDB();
		}

        for (int i = 0; i < sqlQueries.size()-1; i++)       
		{
			table[i] = new ExtList();
			
			long time1 = System.nanoTime();
			String s = sqlQueries.get(i).getString();
			
			gfd.execQuery( s, table[i] );
			sqlQueries.get(i).setResult(table[i]);
		}

		gfd.close();
		return table;
	}

	private ExtList<ExtList<String>> getFromDB(MakeSQL msql, ExtList sep_sch,
			ExtList sep_dataTable) {

        //MakeSQL
		SQL_string = msql.makeSQL(sep_sch);
		//Connect to DB
		
		GetFromDB gfd;
		if(GlobalEnv.isMultiThread())
		{
			System.out.println("[Enter MultiThread mode]");
			ConnectDB cdb = new ConnectDB(GlobalEnv.geturl(),GlobalEnv.getusername(), GlobalEnv.getDriver(), GlobalEnv.getpassword());
			System.out.println(GlobalEnv.geturl() + GlobalEnv.getusername() + GlobalEnv.getpassword());

			cdb.setName("CDB1");
			cdb.run();
		
			gfd = new GetFromDB(cdb);
		}
		else {
			gfd = new GetFromDB();
		}

		gfd.execQuery(SQL_string, sep_dataTable);
		gfd.close();
        
		return sep_dataTable;
	}

	private ExtList<ExtList<String>> getFromDBNew(MakeSQL msql, SSQLparser parser, ExtList<ExtList<String>> data) {
		return data;
	}
	
	private ExtList makeTree(ExtList sep_sch, ExtList sep_dataTable, TreeNode<String> schema) {
		TreeGenerator tg = new TreeGenerator();
		dataTable = tg.makeTree(sep_sch, sep_dataTable);
		return dataTable;
	}

	public ExtList getData() {
		return dataTable;
	}
	
	public TreeNode<String> getDataTree() {
		return dataTree;
	}
	
	private ExtList MakeTree( ExtList schema ) 
	{
		//added by ria
	    Object o;
	    ExtList buf = new ExtList();
	    for ( int i = 0; i < schema.size(); i++ )
	    {
	    	o = schema.get( i );
	    	
	    	if ( !(o instanceof ExtList) )
	    	{
	    		if ( keyAtt == null )
	    		{
	    			keyAtt = (Attribute) o;
		    		buf.add( keyAtt.getTuple() );
		    		//System.out.println(buf);
		    		key = keyAtt.getTuple().toString();
		    		col = keyAtt.getColumn();
	    		}
	    		else
	    		{
	    			Attribute a = (Attribute) o;
	    			if ( a == keyAtt )
	    			{
	    				buf.add( keyAtt.getTuple() );
	    				//System.out.println(buf);
			    		key = keyAtt.getTuple().toString();
	    			}
	    			else
	    			{
	    				//add here checking if the keyAtt is a connector
	    				buf.add(a.getTuple(key, col));
	    				//System.out.println(buf);
	    				a.delTuples(key, col);
	    			}
	    		}
	    	}
	    	else if ( IsLeaf( (ExtList) o ) )
	    	{
	    		
	    		ExtList obj = (ExtList) o;
	    		ExtList temp = new ExtList();

	    		Attribute a = (Attribute) obj.get(0);
		    	temp.addAll((a.getTuples( key, col )));
		    	
		    	if ( temp.size() == 0 )
		    	{
		    		flag = false;
		    	}
		    	else 
		    	{
		    		flag = true;
		    	}
		    	
		    	buf.add(temp);
		    	//System.out.println(buf);
	    		
	    		if (keyAtt != null )
	    		{
	    		    keyAtt.delTuples( key, col );
	    		}

	    	}
	    	else 
	        {
	    		if ( schema.size() == 1 )
	    		{
	    			ExtList temp = new ExtList();
	    			do
	    			{
	    				ExtList temp2 = MakeTree( (ExtList) o );
	    				if ( !temp2.isEmpty() ) 
	    			    {
	    					temp.add( temp2 );
	    					if ( keyAtt != null )
	    					{
	    						keyAtt.delTuples( key, col );
	    					}
	    				}
	    			} while ( (keyAtt != null) && keyAtt.getSize() != 0 );
	    	    	
	    			buf.add( temp );
	    			//System.out.println(buf);
	    	    	flag = true;
	    		}
	    		else
	    		{
	    			ExtList temp = new ExtList();
    	    		temp.add( MakeTree( (ExtList) o ) );
    	    		
    	    		if (flag)
    	    		{
    	    		    buf.add( temp );
    	    		    //System.out.println(buf);
    	    		}
	    		}
	        }
	    }
	    if ( !flag ) {
	        buf = new ExtList();
	    }
	    
	    return buf;
	}
	
	private boolean IsLeaf(ExtList sch)
	{
		for (int i = 0; i < sch.size(); i++)
		{
			if ( sch.get(i) instanceof ExtList)
				return false;
		}
		return true;
	}
	
	public static ExtList getDataFromDBPedia(String sparqlWhereQuery, String[] varNames){
	    BufferedReader br = null;
	    String everything = "";
		try {
			br = new BufferedReader(new FileReader("dbpedia.config"));
		} catch (FileNotFoundException e1) {
			System.err.println("*** DBPedia config file not found ***");
			e1.printStackTrace();
			throw new IllegalStateException();
		}
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } catch (IOException e) {
			System.err.println("*** Error while reading the Dbpedia config file ***");
			e.printStackTrace();
		} finally {
	        try {
				br.close();
			} catch (IOException e) {
				System.err.println("*** Error while closig the dbpedia config file ***");
				e.printStackTrace();
			}
	    }
		try {
			Document doc;
			ExtList data = new ExtList();
				String query = everything + "\nSELECT ";
				for(int i = (varNames.length-1); i >= 0 ; i--){
					query+= "?" + varNames[i] + " ";
				}
				query+=" WHERE "+sparqlWhereQuery+"";
				doc = Jsoup.connect("http://dbpedia.org/sparql?")
						.data("default-graph-uri", "http://dbpedia.org")
						.data("query", query)
						.data("format", "text/html")
						.data("debug", "on")
						.timeout(0)
						.get();
			Elements tdInfos = doc.getElementsByTag("td");
			int columnCount = 0;
			int rowCount = -1;
			for(Element info : tdInfos){
				String infoText = info.html();
				columnCount %= varNames.length;
				if(columnCount == 0){
					ExtList e = new ExtList();
					e.add(infoText);
					data.add(e);
					columnCount+=1;
					rowCount +=1;
				}else{
					((ExtList) data.get(rowCount)).add(infoText);
					columnCount+=1;
				}
				
				
			}
			return data;
		} catch (IOException e) {
			System.err.println("*** Error while querying dbpedia, please check your internet connection and your query syntax ***");
			throw new IllegalStateException();
		}
	}
}
