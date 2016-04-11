package supersql.dataconstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.TreeMap;

import supersql.common.Log;
import supersql.extendclass.ExtList;

public class Graph 
{
	private Attribute rootAttribute;
	private ArrayList<Attribute> nodeList;
	private ArrayList<Attribute> visitedList = new ArrayList<Attribute>();
	private int cycle = 0;
	private int size;
	

	public Graph( TreeMap<String, Attribute> vertices )
	{
		size = vertices.size();
		nodeList = new ArrayList<Attribute>();
		nodeList.addAll( vertices.values() );
		AddEdges();
		
		Log.info("No. of vertices: " + size );
	}
	
	public void addAttribute( Attribute n )
	{
		nodeList.add( n );
	}
	
	private void AddEdges()
	{
		//add a condition here about where to connect :D
		for ( int i = 0; i < size; i++ )
		{	
			for ( int j = 0; j < i; j++ )
			{
				if ( i == j ) 
				{
					continue;
				}
				else if ( nodeList.get(i).getTable().compareTo(nodeList.get(j).getTable() ) == 0 ||
						( nodeList.get(i).getGroup() != -1 && nodeList.get(i).getGroup() == nodeList.get(j).getGroup() ) )
				{
					nodeList.get( i ).connectTo( nodeList.get( j ) );
					nodeList.get( j ).connectTo( nodeList.get( i ) );
				}
			}
		}
	}
	
	private boolean ValidConnector( Attribute n )
	{
		ArrayList<String> connTo = new ArrayList<String>();
		
		ExtList adjList = n.getAdjNodes();
		
		for ( int i = 0; i < adjList.size(); i++ )
		{
			Attribute temp = (Attribute) adjList.get(i);
			if ( !connTo.contains( temp.getTable() ) )
			{
				connTo.add( temp.getTable() );
			}
		}
		
		if ( connTo.size() > 1 )
		    return true;
		else 
			return false;
	}
	
	
	private int cycle ( Attribute current , Attribute prev )
	{
		//0 - no cycle; 1- cycle within same table; 2- cycle
		if ( visitedList.contains( current ) )
		{
			//if prev is an anscestor of current
			if ( prev == null ) return 0;
			if ( visitedList.indexOf( current ) - visitedList.indexOf( prev )  > 1 )
					
			{
				if ( current.getTable().equals( prev.getTable() ) )
				{
					cycle = 1;
					return 1;
				}
				else
				{
					cycle = 2;
					return 2;
				}
			}
			else
			{
				return 0;
			}
		}
		
		visitedList.add( current );
		
		ExtList adjList = current.getAdjNodes();
        for ( int i = 0; i < adjList.size(); i++ )
        {
            cycle( ( Attribute ) adjList.get(i), current );
        }
        return cycle;
	}

	private Attribute GetUnvisitedChildAttribute( Attribute n )
	{
		ExtList adjNodes = n.getAdjNodes();
		
		for ( int i = 0; i < adjNodes.size(); i++ )
		{
			if ( ( (Attribute) adjNodes.get(i) ).isVisited() == false )
				return (Attribute) adjNodes.get(i);
		}

		return null;
	}

	private ArrayList<SQLQuery> depthFirstSearch( Attribute root )
	{
		//DFS uses Stack data structure
		Stack<Attribute> s = new Stack<Attribute>();
		SQLQuery path = new SQLQuery();
		ArrayList<SQLQuery> pathList = new ArrayList<SQLQuery>();

		s.push ( root );
	    path.add( root );
		root.setVisited(true);

		while( !s.isEmpty() )
		{
			Attribute n = (Attribute) s.peek();
			
			Attribute child = GetUnvisitedChildAttribute( n );
			
			if ( child != null )
			{
				if ( n.isConnector() ) {
					if ( !ValidConnector( n ) )
					{
						n.setConnector( false );
					}
				}
				
				if ( n.isConnector() && !n.getTable().equals(child.getTable()) )
				{
					int cycleVal = cycle ( n, null );

					if ( cycleVal == 2 )
					{
						System.out.println( "Cannot be divided. There is a cycle." );
						return new ArrayList<SQLQuery>();
					}
					
					if ( cycleVal == 0 )
					{
						Collections.sort( path, new ByColumn() );
						pathList.add( path );
						path = new SQLQuery();
						path.add( n );
					}
				}
				child.setVisited( true );
				s.push( child );
				path.add( child );
			}
			else
			{
				s.pop();
			}
		}
		Collections.sort( path, new ByColumn() );
		pathList.add(path);
		
		return pathList;
	}
	
	private int unvisitedAttribute()
	{
		for (int i=0; i < size; i++)
		{
			if (! ((Attribute)nodeList.get(i)).isVisited() )
			{
				return i;
			}
		}
		return -1;
	}

	public ArrayList<SQLQuery> connectedComponents()
	{
		int index;
		ArrayList<SQLQuery> components = new ArrayList<SQLQuery>();
		
		components.addAll( depthFirstSearch( rootAttribute ) );
		
		while ( ( index = unvisitedAttribute() ) != -1)
		{
			Attribute current = ( Attribute ) nodeList.get( index );
			components.addAll( depthFirstSearch( current ) );
		}
		
		if ( cycle == 2 ) components = new ArrayList<SQLQuery>();
		
        //printComponents(components);
		return components;
	}
	
	public void clearAttributes()
	{
		int i = 0;
		while ( i < size)
		{
			Attribute n = ( Attribute ) nodeList.get( i );
			n.setVisited(false);
			i++;
		}
	}
	
	public void printComponents( ArrayList<SQLQuery> components )
	{
		for (int j = 0; j < components.size(); j++)
		{
			System.out.print("Component " + j + ": ");
			for (int i = 0; i < components.get(j).size(); i++)
			{
				System.out.print( ( (Attribute) components.get(j).get(i) ).getLabel() + " " );
			}
			System.out.println();
		}
	}
	
	public void setRoot()
	{
		for ( int i = 0; i < size; i++ )
		{
			Attribute n = nodeList.get(i);
			if ( ! n.isConnector() ) 
			{
				this.rootAttribute = n;
				break;
			}
		}
	}
}
