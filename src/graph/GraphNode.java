package graph;

import java.util.HashSet;

public class GraphNode {
	int id;
	HashSet<GraphEdge> edges;
	
	
	
	public GraphNode(int num) {
		id= num;
		edges = new HashSet<GraphEdge>();
	}

	public void addEdge(GraphEdge e) {
		 edges.add(e);
	}
	
	public HashSet<GraphEdge> getNeighbors(){
		HashSet<GraphEdge>Neighbors = new HashSet<GraphEdge>(edges);
		return Neighbors;
	}
	
	public int getId() {
		return id;
	}
	
	//Helper method for testing purposes.
	public void printEdges() {
		for(GraphEdge i : edges) {
			System.out.println(i.toString());
		}
	}
	
	 public String toString()
	{
		 String toReturn = Integer.toString(getId());
		 return toReturn;
	}
	 
	 public void invertEdges() {
		for(GraphEdge f : edges) {
			f.invertEdge();
		}
		}
}
