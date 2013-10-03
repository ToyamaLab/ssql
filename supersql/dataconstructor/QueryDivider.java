package supersql.dataconstructor;

import java.util.ArrayList;
import java.util.TreeMap;

import supersql.extendclass.ExtList;
import supersql.parser.SSQLparser;
import supersql.parser.WhereParse;

public class QueryDivider {

	private final double GROUP = 0.1;
	private SSQLparser parser;
	private TreeMap<String, Attribute> vertices;
	private ExtList schema = new ExtList();
	int num; // the number in charge for setting grouping
	int level; // the level of the attribute in the schema
	int column; // for sorting the table
	boolean dupAtt = false;
	Graph g;
	
	public QueryDivider( SSQLparser p )
	{
		this.parser = p;
		//MakeGraph();
	}
	
    public boolean MakeGraph()
    {
//    	schema = parser.get_TFEschema().makeschImage();
//    	level = 0;
//    	num = 0;
//    	column = 0;
//    	vertices = new TreeMap<String, Attribute>();
//    	//System.out.println( schema );
//    	if ( MakeSchemaNodes( schema ) ) 	//make nodes from the schema
//    	{
//    		return false;
//    	}
//    	
//    	MakeWhereNodes(); 			//make nodes from the where
//    	g = new Graph( vertices ); 	//make the graph
//    	AddWhereEdges();  			//connect nodes equated in where
//    	g.setRoot();
//    	//g.printGraph();
    	return true;
    }
    
	private boolean MakeSchemaNodes(ExtList sch) 
	{
	    Object o;
	    double group;
	    Attribute temp;
	    level++;
	    for (int i = 0; i < sch.size(); i++ )
	    {
	    	o = sch.get( i );
	    	num++;
	    	group = level + ( num * GROUP );
	    	
	    	if ( !(o instanceof ExtList) )
	    	{
	    		Attribute n = NewNode( o, level );
	    		temp = vertices.put( o.toString(), n );
	    		
	    		if ( temp != null )
	    		{
	    			dupAtt = true;
	    		}
	    		
	    		n.setAttribute( true );
	    		n.setColumn( column++ );
	    		sch.set( sch.indexOf( o ), n);
	    		
	    	}
	    	else if ( IsLeaf( (ExtList) o ) )
	    	{
	    		ExtList obj = (ExtList) o;
	    		for ( int j = 0; j < obj.size(); j++ )
	    		{
	    			Attribute n = NewNode( obj.get(j), group );
	    			temp = vertices.put( obj.get( j ).toString(), n );
	    			if ( temp != null )
	    			{
	    				dupAtt = true;
	    			}
	    			n.setAttribute( true );
	    			n.setColumn( column++ );
	    			obj.set( j, n );
	    		}
	    	}
	    	else 
	        {
	    		MakeSchemaNodes( (ExtList) o );
	        }
	    }
	    level--;
	    return dupAtt;
	}
	
    private void MakeWhereNodes()
    {	
        ExtList winfo_clause = parser.get_where_info().getWhereClause();
        
        int winfo_size = winfo_clause.size();
        
        for ( int i = 0; i < winfo_size; i++)
        {
        	Attribute n;
        	ExtList winfo_att = ( (WhereParse) winfo_clause.get( i ) ).getUseAtts();
        	int winfo_asize = winfo_att.size();
        	String label;
        	
        	for ( int j = 0; j < winfo_asize; j++ )
        	{
        		label = ( String ) winfo_att.get( j );
        		
        		//if label is not a table, continue
        		if ( label.indexOf('\'') != -1 ) continue;
	            if ( ( n = vertices.get( label ) ) == null )
	            {
	            	n = NewNode( ( String ) winfo_att.get( j ), -1 );
	            }
	            
	            //n.setWhere( ( ( WhereParse ) winfo_clause.get( i ) ).getLine() );
	            vertices.put( label, n );
        	}
        }
    }
    
    private void AddWhereEdges()
    {
    	//where info: with parenthesis, add edge
    	//same group, add an edge
        //add filters too
    	
        int winfo_size = parser.get_where_info().getWhereClause().size();
    	
        for ( int i = 0; i < winfo_size; i++)
        {
        	ExtList winfo_clause = parser.get_where_info().getWhereClause();
        	ExtList winfo_att = ( (WhereParse) winfo_clause.get( i ) ).getUseAtts();
        	int winfo_asize = winfo_att.size();
        	
        	for ( int j = 0; j < winfo_asize; j+=2 )
        	{   	
	        	Attribute node1 = vertices.get( (String) winfo_att.get( j ) ); 
	        	Attribute node2 = vertices.get( (String) winfo_att.get( j + 1 ) ); 
	            
	        	if ( node2 == null ) continue;
	        	
	        	node1.setWhere( ( ( WhereParse ) winfo_clause.get( i ) ).getLine() );
	        	node2.setWhere( ( ( WhereParse ) winfo_clause.get( i ) ).getLine() );
	        	
	        	//algorithm for checking connectors start
	        	if ( node1.getConnTable().isEmpty() )
	        	{
	        		node1.setConnTable(node2.getTable());
	        	}
	        	
	        	if ( node2.getConnTable().isEmpty() )
	        	{
	        		node2.setConnTable(node1.getTable());
	        	}
	        	
	        	if ( node1.getConnTable().compareTo( node2.getTable() ) != 0  
	        			&& node1.getTable().compareTo( node2.getTable() ) != 0 )
	        	{ 
		        	node1.setConnector(true);
		        	node1.setWhere("");
	        	}
	        	
	        	if ( node2.getConnTable().compareTo( node1.getTable() ) != 0
	        			&& node1.getTable().compareTo( node2.getTable() ) != 0 )
	        	{ 
		        	node2.setConnector(true);
		        	node2.setWhere("");
	        	}
	        	node1.connectTo( node2 );
	        	node2.connectTo( node1 );
	            //algorithm for checking connectors end
	        	
        	}
        	
        	//connect nodes within the parentheses
        	for ( int j = 0; (j + 2) < winfo_asize; j+=2 )
        	{ 
        		
        		Attribute node1 = vertices.get( (String) winfo_att.get( j ) ); 
    	        Attribute node2 = vertices.get( (String) winfo_att.get( j + 2 ) );

    	        node1.connectTo( node2 );
    	        node2.connectTo( node1 );

        	}
        }
    }
    
	private Attribute NewNode( Object l, double group )
	{
		Attribute n;
		String label = (String) l;
		
		String table = parser.get_from_info().getOrigTable( label.substring( 0, label.indexOf( '.' ) ) )
			+ " " + label.substring( 0, label.indexOf( '.' ) );
		
		n = new Attribute( label, table, group );
		
		return n;
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
	
	public ArrayList<SQLQuery> divideQuery()
	{
		return g.connectedComponents();
	}
	
	public ExtList getSchema()
	{
		return schema;
	}
	

}
