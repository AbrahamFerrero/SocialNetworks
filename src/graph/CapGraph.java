/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import util.GraphLoader;

/**
 * @author Abraham.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	HashMap<Integer,GraphNode> pointsMap;
	HashSet<GraphEdge> edges;
	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	public CapGraph(){
		pointsMap = new HashMap<Integer,GraphNode>();
		edges = new HashSet<GraphEdge>();
	}
	@Override
	public void addVertex(int num) {
		// TODO Auto-generated method stub
		//System.out.println("trying to add " + num);
		GraphNode newNode = new GraphNode(num);
		pointsMap.put(num, newNode);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		// TODO Auto-generated method stub
		GraphNode n1 = pointsMap.get(from);
		GraphNode n2 = pointsMap.get(to);
		if (n1 == null)
			throw new NullPointerException("addEdge: pt1:"+from+"is not in graph");
		if (n2 == null)
			throw new NullPointerException("addEdge: pt2:"+to+"is not in graph");
		GraphEdge newEdge = new GraphEdge(n1,n2);
		edges.add(newEdge);
		n1.addEdge(newEdge);
	}
	
	
	//returns the array with all the vertex
	public ArrayList<GraphNode> getVertexes(){
		ArrayList<GraphNode> Vertexes = new ArrayList<GraphNode>();
		for(GraphNode i : pointsMap.values()) {
			Vertexes.add(i);
		}
		return Vertexes;
	}
	
	public HashSet<GraphEdge> getEdges(){
		return edges;
	}
	
	/** Takes the graphnodes of an edge and returns the edge inverted. As it is a two ways edge, it is a helper method 
	 * for my algorithm to calculate the egonet.
	 * 
	 * @param start The starting edge
	 * @param end The ending edge
	 * @return the GraphEdge inverted, if it was 1 2, it gives you a 2 1 edge.
	 */
	private GraphEdge invertEdge(GraphNode start, GraphNode end) {
		GraphEdge inverted = new GraphEdge(end,start);
		return inverted;
	}
	
	/** It takes the whole graph and creates another one just with the egonet of a chosen vertex .
	 * 
	 * @param center The center of the egonet you want to calculate
	 * @return a brand new Graph with the egonet of a chosen vertex.
	 */
	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		//Creates a new graph
		CapGraph newGraph = new CapGraph();
		//gets the current vertex and adds it to the new graph:
		GraphNode currentNode = pointsMap.get(center);
		newGraph.addVertex(center);
		HashSet<GraphEdge> neighsbours = currentNode.getNeighbors();
		//List of nodes to explore:
		Queue<GraphNode> toExplore = new LinkedList<GraphNode>();
		toExplore.add(currentNode);
		/* For each neighbor of the current node, we add the vertex to the list to explore
		 *  and add the vertex to the new graph.
		 * For each edge of this neighbors, we get the neighbors again, and if they are related
		 * to each other, we add the edges.*/
		for(GraphEdge neighbor : neighsbours) {
			toExplore.add(neighbor.getEndEdge());
			GraphNode vertexFound = neighbor.getEndEdge();
			newGraph.addVertex(vertexFound.getId());
			for(GraphEdge f : vertexFound.getNeighbors()) {
				if(newGraph.getVertexes().contains(f.getStartEdge()) && newGraph.getVertexes().contains(f.getEndEdge()) || toExplore.contains(f.getEndEdge())) {
					newGraph.addEdge(f.getStartEdge().getId(), f.getEndEdge().getId());
					GraphEdge edgeInverted = invertEdge(f.getStartEdge(),f.getEndEdge());
					if(!newGraph.getEdges().contains(edgeInverted)){
						newGraph.addEdge(edgeInverted.getStartEdge().getId(),edgeInverted.getEndEdge().getId());
					}	
				}
			}
		}
		return newGraph;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	private Stack<GraphNode> DFS(Stack<GraphNode> vertices) {
		HashSet<GraphNode> visited = new HashSet<GraphNode>();
		Stack<GraphNode> finished = new Stack<GraphNode>();
		while(!vertices.isEmpty()) {
			GraphNode v=vertices.pop();
			if(!visited.contains(v)) {
				dfsVisit(v,visited,finished);
			}
		}
		return finished;
	}
	
	private void dfsVisit(GraphNode v,HashSet<GraphNode> visited,Stack<GraphNode> finished){
		visited.add(v);
		for(GraphEdge n : v.getNeighbors()) {
			GraphNode N = n.getEndEdge();
			if(!visited.contains(N)) {
				dfsVisit(N,visited,finished);
			}
			
			//System.out.println("pushing " + v.toString());
		}
		finished.push(v);
	}
	
	
	
	private Graph reverseGraph() {
		CapGraph reversedGraph = new CapGraph();
		for(GraphNode g : pointsMap.values()) {
			reversedGraph.addVertex(g.getId());
		}
		for(GraphNode g : pointsMap.values()) {
			HashSet<GraphEdge> nodeNeighbours = g.getNeighbors();
			for (GraphEdge edge : nodeNeighbours) {
				int start = edge.getStartEdge().getId();
				int end = edge.getEndEdge().getId();
				reversedGraph.addEdge(end, start);
			}
		}
		return reversedGraph; 
	}
	
	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub
		
		//1.- DFS(G):
		
		Stack<GraphNode> vertices = new Stack<GraphNode>();
		for(GraphNode v : pointsMap.values()) {
			vertices.add(v);
			//v.printEdges();
		}
		Stack<GraphNode> finished = DFS(vertices);
		
		//2.- Reverse Graph: 
		
		Graph trasposed = reverseGraph();//it works
		
		//3.- Same process in reverse graph:
		Stack<GraphNode> verticesT = new Stack<>();
		for(GraphNode v : finished) {
			GraphNode vertexToAdd = ((CapGraph) trasposed).pointsMap.get(v.getId());
			verticesT.add(vertexToAdd);
		}
		Stack<GraphNode> finishedT = ((CapGraph) trasposed).DFS(verticesT);
		
		//4.- Make a list of SCC graphs:

		List<Graph> components;
		components = constructComponents(trasposed, finishedT);
		return components;
	}
	
	private List<Graph> constructComponents(Graph graph, Stack<GraphNode> toExplore) {
		List<Graph> components = new ArrayList<>();

		HashSet<GraphNode> visited = new HashSet<>();
		for (GraphNode vertex: toExplore) {

			if (! visited.contains(vertex)) {
				Stack<GraphNode> finished = new Stack<>();
				((CapGraph) graph).dfsVisit(vertex, visited, finished);

				Graph component = new CapGraph();
				for (GraphNode node: finished) {
					component.addVertex(node.getId());
				}
				components.add(component);
			}
		}

		return components;
	}
	
	private int edgeToInt(GraphEdge e) {
		return e.getEndEdge().getId();
	}
	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub
		HashMap<Integer, HashSet<Integer>> toExport = new HashMap<Integer, HashSet<Integer>>();
		for(Integer i : pointsMap.keySet()) {
			//Adds every vertex to the map as an integer.
			toExport.put(i, null);
		}
		for (GraphNode g : pointsMap.values()) {
			for(Integer i : toExport.keySet()) {
				if(i == g.getId()) {
					HashSet<GraphEdge> neighs = g.getNeighbors();
					HashSet<Integer> neighstoInt = new HashSet<Integer>();
					for(GraphEdge edge : neighs) {
						neighstoInt.add(edgeToInt(edge));
					}
					/*If there is the same id in both maps, we basically replace the null value for a hashset with the int edge 
					 * value at the end of the edge (1 3 and 1 2, would be a hashset 3,2). Not the most efficient way to do it
					 * as we are iterating over and over through a map but the one plausible way to make the grader work.
					 */
					toExport.replace(g.getId(), neighstoInt);
				}
			}
			
		}
		return toExport;
	}
	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		CapGraph theMap = new CapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadGraph(theMap,"data/scc/test_4.txt");
		System.out.println("DONE.");
		theMap.getSCCs();
		for (Graph g : theMap.getSCCs()) {
			System.out.println("vertexes:");
			for(int d : ((CapGraph)g).pointsMap.keySet()) {
				System.out.println(d);
			}
		}
		
	}

}
