package hw5;

import java.util.*;

/**
 * Graph is a mutable interface for representing a directed labeled multigraph.
 * Graph is a collection of GraphNodes, each linked with GraphEdges reaching out of them.
 * Mutable meaning the user can add or remove nodes and edges from the graph as they desire.
 * <p>
 *
 * A Graph can be described as {n_1 = [e_n_1_1, ... ,e_n_1_n], ... , n_m = [e_n_m_1, ... ,e_n_m_n]},
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
 * An example of a Graph includes: {GraphNode("a") = [GraphEdge(GraphNode("a"), GraphNode("b"))], 
 * 									GraphNode("b") = [GraphEdge(GraphNode("b"), GraphNode("a"))]} 
 * 								   and
 * 								   {GraphNode("a") = [GraphEdge(GraphNode("a"), GraphNode("b"))], 
 * 									GraphNode("b") = [GraphEdge(GraphNode("b"), GraphNode("c"))],
 * 									GraphNode("c") = [GraphEdge(GraphNode("c"), GraphNode("a"))]}
 *
 * @author Young Bin Cho
 */

public class Graph<N, E>{
	
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
	private Map<N, List<GraphEdge<N, E>>> graph;
	
	/**
	 * @effects constructs an empty graph
	 */
	public Graph() {
		graph = new HashMap<N, List<GraphEdge<N,E>>>();
		checkRep();
	}
	
	/**
	 * <The Default Constructor>
	 * constructs the graph out of given list of edges
	 * @param l the list of GraphEdges that is to be sorted as a graph
	 * @requires l != null
	 * @effects constructs a new graph from the given edges
	 */
	public Graph(List<GraphEdge<N, E>> l) {
		graph = new HashMap<N, List<GraphEdge<N,E>>>();
		for(GraphEdge<N, E> g : l) {
			if(!graph.containsKey(g.getFrom()))
				graph.put(g.getFrom(), new ArrayList<GraphEdge<N, E>>());
			graph.get(g.getFrom()).add(g);
		}
	}
	
	/**
	 * <Constructor for MarvelPaths and MarvelPaths2>
	 * constructs the graph out of given set of characters and map of books
	 * @param characters set of all the characters
	 * @param books map of books and characters in them
	 * @requires characters != null && books != null
	 * @effects constructs a new graph from the given characters and books
	 */
	public Graph(Set<N> characters, Map<E, List<N>> books) {
		graph = new HashMap<N, List<GraphEdge<N, E>>>();
		for(N character : characters) {
			graph.put(character, new ArrayList<GraphEdge<N, E>>());
			for(E book : books.keySet()) {
				if(books.get(book).contains(character)) {
					for(N s : books.get(book)) {
						if(!s.equals(character))
							graph.get(character).add(new GraphEdge<N,E>(character, s, book));
					}
				}
			}
		}
		checkRep();
	}
	
	/**
	 * adds a node to the graph
	 * @param g the GraphNode to be added
	 * @modifies this
	 * @effects adds the given GraphNode to graph
	 * @throws IllegalStateException if the given node is already in the map
	 */
	public void addNode(N g) {
		if(containsNode(g))
			throw new IllegalStateException("Graph already contains the node");
		graph.put(g, new ArrayList<GraphEdge<N,E>>());
		checkRep();
	}
	
	/**
	 * adds an edge to the graph
	 * @param g the GraphEdge to be added to graph
	 * @modifies this
	 * @effects adds the given GraphEdge to graph
	 * @throws IllegalArgumentException if the given GraphEdge doesn't reach out from an already existing node
	 */
	public void addEdge(GraphEdge<N,E> g) {
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
	public void removeNode(N g) {
		if(!containsNode(g))
			throw new IllegalArgumentException("Graph does not contain the node");
		removeAllEdgesFromAndTo(g);
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
	public void removeEdge(GraphEdge<N,E> g) {
		if(!containsEdge(g))
			throw new IllegalArgumentException("Graph does not contain the edge");
		graph.get(g.getFrom()).remove(g);
	}
	
	/**
	 * removes all occurrences of the given edge
	 * @param g GraphEdge to be removed
	 * @modifies this
	 * @effects removes all occurrences of given GraphEdge
	 * @throws IllegalArgumentException if graph does not contain the edge to be removed
	 */
	public void removeAllEdge(GraphEdge<N,E> g) {
		if(!containsEdge(g))
			throw new IllegalArgumentException("Graph does not contain the edge");
		while(graph.get(g.getFrom()).contains(g))
			graph.get(g.getFrom()).remove(g);
	}
	
	/**
	 * removes all edges from and to the given node
	 * @param g GraphNode to be isolated
	 * @modifies this
	 * @effects removes all edges reaching in and out of given GraphNode
	 * @throws IllegalArugmentException if graph does not contain the node to be isolated
	 */
	public void removeAllEdgesFromAndTo(N g) {
		if(!containsNode(g))
			throw new IllegalArgumentException("Graph does not contain the node");
		graph.get(g).clear();
		for(N i : graph.keySet()) {
			for(GraphEdge<N,E> j : graph.get(i)) {
				if(j.getTo().equals(g))
					graph.get(i).remove(j);
			}
		}
	}
	
	/**
	 * returns all nodes being considered in this graph
	 * @return set of all nodes
	 */
	public Set<N> getAllNodes() {
		return graph.keySet();
	}
	
	/**
	 * returns all edges contained in the graph
	 * @return list of all edges
	 */
	public List<GraphEdge<N,E>> getAllEdges() {
		List<GraphEdge<N,E>> temp = new ArrayList<GraphEdge<N,E>>();
		for(N i : graph.keySet())
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
	public List<GraphEdge<N,E>> getAllEdgesFrom(N g) {
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
	public boolean containsNode(N g) {
		return graph.containsKey(g);
	}
	
	/**
	 * returns true if graph contains the edge, false otherwise
	 * @require g != null
	 * @param g GraphEdge to be searched for
	 * @return whether given GraphEdge is in the graph or not
	 */
	public boolean containsEdge(GraphEdge<N,E> g) {
		return getAllEdges().contains(g);
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	public void checkRep() {
		assert(graph != null);
		for(GraphEdge<N,E> g : getAllEdges()) {
			assert(getAllNodes().contains(g.getTo()) && 
					getAllNodes().contains(g.getFrom())) : "Pointing to or from an inexistant node";
		}
	}
}