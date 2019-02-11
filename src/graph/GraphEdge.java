package graph;

public class GraphEdge {
	GraphNode start;
	GraphNode end;
	
	public GraphEdge(GraphNode n1, GraphNode n2) {
		start = n1;
		end = n2;
	}
	
	public GraphNode getStartEdge() {
		return start;
	}
	
	public GraphNode getEndEdge() {
		return end;
	}
	
	public void invertEdge() {
		GraphNode newEnd = start;
		start = end;
		end = newEnd;
	}
	
	 public String toString()
		{
			 String toReturn = "";
			 toReturn += getStartEdge().toString()+ " "+ getEndEdge().toString();
			 return toReturn;
		}
}
