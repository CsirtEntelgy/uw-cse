package hw5;

/**
 * GraphEdge is an immutable class for representing a line between two GraphNodes.
 * GraphEdge is a pair of GraphNodes with a data field.
 * Immutable meaning the user cannot change the data it holds once it is set by the constructor.
 * <p>
 *
 * A GraphEdge can be described as, 2 GraphNodes, from and to, and a String that holds its data
 * where from is where the edge starts and to is where the edge ends
 * String is the data this edge holds.
 * <p>
 * 
 * The generic type N stands for the type the nodes of the graph holds.
 * The generic type E stands for the type the edges of the graph holds.
 * <p>
 *
 * An example of a GraphEdge includes: GraphEdge(GraphNode("a"), GraphNode("b"))
 *									   GraphEdge(GraphNode("b"), GraphNode("c"), "path from b to c")
 *
 * @author Young Bin Cho
 */

public class GraphEdge<N, E> implements Comparable<GraphEdge<?, ?>>{
	
	// This class represents a edge from one GraphNode to another
	// Invariant: All fields must not be null

	/*private fields that hold the data for this class*/
	private final N from;
	private final N to;
	private final E edgeData;
	
	/**
	 * constructor that takes two GraphNodes and a string
	 * @requires from != null && to != null && data != null
	 * @param from GraphNode where the edge starts
	 * @param to GraphNode where the edge ends
	 * @param data string this edge holds
	 * @modifies this
	 * @effects constructs new GraphEdge from two GraphNodes and a string
	 */
	public GraphEdge(N from, N to, E data) {
		this.from = from;
		this.to = to;
		this.edgeData = data;
		checkRep();
	}
	
	/**
	 * returns the from field
	 * @return copy of GraphNode from
	 */
	public N getFrom() {
		return this.from;
	}
	
	/**
	 * returns the to field
	 * @return copy of GraphNode to
	 */
	public N getTo() {
		return this.to;
	}
	
	/**
	 * returns the data field
	 * @return copy of String data
	 */
	public E getData() {
		return this.edgeData;
	}

	/**
	 * auto-generated methods for equals implementation
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edgeData == null) ? 0 : edgeData.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphEdge<?,?> other = (GraphEdge<?,?>) obj;
		if (edgeData == null) {
			if (other.edgeData != null)
				return false;
		} else if (!edgeData.equals(other.edgeData))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	/**
	 * returns a concatenated string for a edge
	 * @requires from != null && to != null
	 * @return string form of edge
	 */
	@Override
	public String toString() {
		return "<"+from+","+to+","+edgeData+">";
	}
	
	/**
	 * returns a integer value for comparing an object to this edge
	 * @requires arg != null
	 * @return negative, if smaller, 0 if equal, and positive if bigger
	 */
	@Override
	public int compareTo(GraphEdge<?, ?> arg) {
		return toString().compareTo(arg.toString());
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	private void checkRep() {
		assert(from != null && to != null);
	}
}