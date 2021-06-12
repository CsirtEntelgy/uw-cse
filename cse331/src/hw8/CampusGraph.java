package hw8;

import java.util.*;

/**
 * CampusGraph is a mutable interface for representing a map.
 * CampusGraph is a collection of nodes and edges stretching out from the nodes.
 * Mutable meaning the user can add or remove nodes and edges from the graph as they desire.
 * <p>
 *
 * A CampusGraph can be described as {n_1 = [e_n_1_1, ... ,e_n_1_n], ... , n_m = [e_n_m_1, ... ,e_n_m_n]},
 * where n_1 ... n_m are the nodes in the graph and 
 * [e_n_1_1, ... ,e_n_1_n] are the edges reaching out from n_1 
 * ... 
 * [e_n_m_1, ... ,e_n_m_n] are the edges reaching out from n_m
 * <p>
 * 
 * The generic type N stands for the type the nodes of the graph holds.
 * The generic type E stands for the type the edges of the graph holds.
 * <p>
 *
 * An example of a CampusGraph includes: {GraphNode("a") = [GraphEdge(GraphNode("a"), GraphNode("b"))], 
 * 										  GraphNode("b") = [GraphEdge(GraphNode("b"), GraphNode("a"))]} 
 * 								   		  and
 * 								   		 {GraphNode("a") = [GraphEdge(GraphNode("a"), GraphNode("b"))], 
 * 										  GraphNode("b") = [GraphEdge(GraphNode("b"), GraphNode("c"))],
 * 										  GraphNode("c") = [GraphEdge(GraphNode("c"), GraphNode("a"))]}
 *
 * @author Young Bin Cho
 */

public class CampusGraph<N, E extends Number>{
	
	// This class represents a graph with the set of nodes and them matched with
	// the edges reaching out of them.
	//
	// Abstraction Function:
	//		A Graph g represents the graph constructed from the nodes and edges
	//		contained in "graph":
	//		(GraphNode i : graph.keySet()) 
	//			i.isConnected(some element(s) in graph.keySet())
	//
	// Representation Invariant:
	//		graph != null
	// 		for <i, ... , i_n> in graph.keySet()
	//			All i, ... ,i_n isConnected to some i_k
	//			Some i and i_k is never equal
	// In other words:
	//		The graph must be initialized.
	//		Every edge should be pointing from and to a valid node.
	//		There should be no identical nodes (this is always kept by using a HashMap implementation).
	
	/** A map that holds all the relations between a GraphNode and the edges reaching out of them*/
	private Map<CampusNode<N>, List<CampusEdge<N, E>>> graph;
	
	/**
	 * @effects constructs an empty graph
	 */
	public CampusGraph() {
		graph = new HashMap<CampusNode<N>, List<CampusEdge<N,E>>>();
		checkRep();
	}
	
	/**
	 * constructs the graph out of another CampusGraph
	 * @param c the other graph that is to be set as this one
	 * @requires c != null
	 * @effects constructs a new graph from given graph
	 */
	public CampusGraph(CampusGraph<N,E> c) {
		graph = new HashMap<CampusNode<N>, List<CampusEdge<N,E>>>(c.graph);
		checkRep();
	}
	
	/**
	 * constructs the graph out of given map
	 * @param m given map set to be this object's map
	 * @requires m != null
	 * @effects constructs a new graph from the given map
	 */
	public CampusGraph(Map<CampusNode<N>, List<CampusEdge<N, E>>> m) {
		this.graph = m;
	}
	
	/**
	 * adds a node to the graph
	 * @param g the GraphNode to be added
	 * @modifies this
	 * @effects adds the given GraphNode to graph
	 * @throws IllegalStateException if the given node is already in the map
	 */
	public void addNode(CampusNode<N> g) {
		if(containsNode(g))
			throw new IllegalStateException("Graph already contains the node");
		graph.put(g, new ArrayList<CampusEdge<N,E>>());
		checkRep();
	}
	
	/**
	 * adds an edge to the graph
	 * @param g the GraphEdge to be added to graph
	 * @modifies this
	 * @effects adds the given GraphEdge to graph
	 * @throws IllegalArgumentException if the given GraphEdge doesn't reach out from an already existing node
	 */
	public void addEdge(CampusEdge<N,E> g) {
		if(!containsNode(g.getFrom()))
			throw new IllegalArgumentException("Graph does not contain either starting for ending node");
		graph.get(g.getFrom()).add(g);
		checkRep();
	}
	
	/**
	 * removes a node and all corresponding edges reaching in and out from that node 
	 * @param g GraphNode to be removed
	 * @modifies this
	 * @effects removes the given GraphNode from graph and all related edges
	 * @throws IllegalArgumentException if graph does not contain the node to be removed
	 */
	public void removeNode(CampusNode<N> g) {
		if(!containsNode(g))
			throw new IllegalArgumentException("Graph does not contain the node");
		graph.get(g).clear();
		for(CampusNode<N> i : graph.keySet()) {
			for(CampusEdge<N,E> j : graph.get(i)) {
				if(j.getTo().equals(g))
					graph.get(i).remove(j);
			}
		}
		graph.remove(g);
		checkRep();
	}
	
	/**
	 * removes the first occurrence of the given edge
	 * @param g GraphEdge to be removed
	 * @modifies this
	 * @effects removes the first occurrence of given GraphEdge
	 * @throws IllegalArgumentException if graph does not contain the edge to be removed
	 */
	public void removeEdge(CampusEdge<N,E> g) {
		if(!containsEdge(g))
			throw new IllegalArgumentException("Graph does not contain the edge");
		graph.get(g.getFrom()).remove(g);
	}
	
	/**
	 * returns all nodes being considered in this graph
	 * @return set of all nodes
	 */
	public Set<CampusNode<N>> getAllNodes() {
		return graph.keySet();
	}
	
	/**
	 * returns all edges contained in the graph
	 * @return list of all edges
	 */
	public List<CampusEdge<N,E>> getAllEdges() {
		List<CampusEdge<N,E>> temp = new ArrayList<CampusEdge<N,E>>();
		for(CampusNode<N> i : graph.keySet())
			temp.addAll(graph.get(i));
		return temp;
	}
	
	/**
	 * returns all edges reaching out from the given node
	 * @requires g != null
	 * @param g the node to be examined
	 * @return list of all edges reaching out of given GraphNode
	 * @throws IllegalArgumentException if graph doesn't contain the given GraphNode
	 */
	public List<CampusEdge<N,E>> getAllEdgesFrom(CampusNode<N> g) {
		if(!containsNode(g))
			throw new IllegalArgumentException("Graph does not contain the node");
		return graph.get(g);
	}
	
	/**
	 * returns true if the graph is empty, false otherwise
	 * @requires graph != null
	 * @return whether graph is empty or not
	 */
	public boolean isEmpty() {
		return graph.isEmpty();
	}
	
	/**
	 * returns true if graph contains the node, false otherwise
	 * @require g != null
	 * @param g GraphNode to be searched for
	 * @return whether given GraphNode is in the graph or not
	 */
	public boolean containsNode(CampusNode<N> g) {
		return graph.containsKey(g);
	}
	
	/**
	 * returns true if graph contains the edge, false otherwise
	 * @require g != null
	 * @param g GraphEdge to be searched for
	 * @return whether given GraphEdge is in the graph or not
	 */
	public boolean containsEdge(CampusEdge<N,E> g) {
		return getAllEdges().contains(g);
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	public void checkRep() {
		assert(graph != null);
	}
}