package hw8;

/**
 * CampusEdge is an immutable class for representing a weighted edge.
 * A CampusEdge consists of two CampusNodes of type N and an object of type E.
 * The two CampusNodes that represent the starting and ending point of this edge.
 * Weight of the edge is the object of type E.
 * Edges are compared via their from, to, and distance fields.
 * Immutable meaning data stored in CampusEdge can't be changed after initialization.
 * <p>
 *
 * A CampusEdge can be described as
 * <n1, n2, w1>
 * where n1 is the starting point(node) of this edge,
 * n2 is the ending point(node) of this edge, and w1 is the weight of this edge.
 * <p>
 * 
 * The generic type N stands for the type the CampusNodes of this edge holds.
 * The generic type E stands for some type that extends the Number implementation
 * that represents the weight of this edge.
 * <p>
 *
 * An example of a CampusEdge includes: (<1.0, 2.0>, "short_name", "full_name") and
 * 										(<3.0, 1.2223>, "r_s", "random_short")
 *
 * @author Young Bin Cho
 */

public class CampusEdge<N,E extends Number> implements Comparable<CampusEdge<N,E>> {
	
	// This class represents an edge connecting two nodes of type N 
	// and has the weight of type E.
	// Does not represent an ADT.
	//
	// Representation Invariant:
	//		from != null && to != null
	// In other words:
	//		the nodes from and to must be initialized
	
	/** data fields that hold the nodes and weight that compose the CampusEdge*/
	private final CampusNode<N> from;
	private final CampusNode<N> to;
	private final E distance;
	
	/**
	 * @effects constructs an empty edge
	 */
	public CampusEdge() {
		this(null, null, null);
	}
	
	/**
	 * constructs a new CampusEdge out of another CampusEdge
	 * @param c another CampusEdge to be set as this one
	 * @requires c != null
	 * @effects constructs a new edge from another edge
	 */
	public CampusEdge(CampusEdge<N,E> c) {
		from = new CampusNode<N>(c.getFrom());
		to = new CampusNode<N>(c.getTo());
		distance = c.getDistance();
		checkRep();
	}
	
	/**
	 * constructs a new CampusEdge out of given fields
	 * @param from CampusNode to be set starting point
	 * @param to CampusNode to be set ending point
	 * @param distance data that represents the weight
	 * @requires from != null && to != null
	 * @effects constructs a new edge out of given fields
	 */
	public CampusEdge(CampusNode<N> from, CampusNode<N> to, E distance) {
		this.from = from;
		this.to = to;
		this.distance = distance;
		checkRep();
	}
	
	/**
	 * returns a protected copy of from
	 * @return from
	 */
	public CampusNode<N> getFrom() {
		return this.from;
	}
	
	/**
	 * returns a protected copy of to
	 * @return to
	 */
	public CampusNode<N> getTo() {
		return this.to;
	}
	
	/**
	 * returns a protected copy of distance
	 * @return distance
	 */
	public E getDistance() {
		return this.distance;
	}
	
	/**
	 * generated method
	 * @returns unique integer generated from from to and distance
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((distance == null) ? 0 : distance.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	/**
	 * generated method
	 * @returns true if two object's from to distance fields are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampusEdge<?,?> other = (CampusEdge<?,?>) obj;
		if (distance == null) {
			if (other.distance != null)
				return false;
		} else if (!distance.equals(other.distance))
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
	 * compares two CampusEdge by their distance values
	 * @return integer value generated from comparing two distances
	 */
	@Override
	public int compareTo(CampusEdge<N,E> arg) {
		return Double.compare((Double) this.getDistance(), (Double) arg.getDistance());
	}
	
	/**
	 * returns a string format of this object
	 * @return string format of this object in order of from to distance
	 */
	@Override
	public String toString() {
		return "<"+from+", "+to+", "+distance+">";
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	private void checkRep() {
		assert(from != null && to != null);
	}
}