package hw8;

/**
 * CampusPath is an mutable class for representing a path.
 * A CampusPath consists of a collection of CampusEdges of type <N,E>.
 * Also stores the latest edge added and accumulated distance of all the edges in the path.
 * CampusPaths are compared via their the list of CampusEdges, the latest edge, and accumulated distance.
 * Immutable meaning CampusEdges can be added and subtracted from the path.
 * <p>
 *
 * A CampusEdge can be described as
 * <n1, n2, w1>
 * where n1 is the starting point(node) of this edge,
 * n2 is the ending point(node) of this edge, and w1 is the weight of this edge.
 * <p>
 * 
 * The generic type N and E stands for the type the CampusEdges this path holds.
 * E must extend Number in order to be considered as weight.
 * <p>
 *
 * An example of a CampusPath includes: List of Campus edges like
 * 										(<1.0, 2.0>, "short_name", "full_name"),
 * 										(<3.0, 1.2223>, "r_s", "random_short"),
 * 										(<4.567, 8.9101>, "s", "shortName")
 *
 * @author Young Bin Cho
 */

import java.util.*;

public class CampusPath<N,E extends Number> implements Comparable<CampusPath<?,?>>{

	// This class represents a path, collection of CampusEdges.
	// Does not represent an ADT.
	//
	// Representation Invariant:
	//		path != null && accDistance >= 0.0
	// In other words:
	//		list and accDistance must be initialized
	
	/** data fields that hold the accumulated paths, 
	 * the latest edges added, and the accumulated distance*/
	private List<CampusEdge<N,E>> path;
	private CampusEdge<N,E> latest;
	private double accDistance;
	
	/**
	 * @effects constructs an empty path
	 */
	public CampusPath() {
		path = new ArrayList<CampusEdge<N,E>>();
		latest = null;
		accDistance = 0.0;
		checkRep();
	}
	
	/**
	 * constructs a new CampusPath out of another CampusPath
	 * @param c another CampusPath to be set as this one
	 * @requires c != null
	 * @effects constructs a new path from another path
	 */
	public CampusPath(CampusPath<N,E> c) {
		path = new ArrayList<CampusEdge<N,E>>(c.getPath());
		latest = new CampusEdge<N,E>(c.getLatest());
		accDistance = c.getAccDistance();
		checkRep();
	}
	
	/**
	 * adds an edge to the path
	 * @param e CampusEdge to be added to path
	 * @requires e != null
	 * @effects adds given CampusEdge to path
	 */
	public void addEdge(CampusEdge<N,E> e) {
		path.add(e);
		latest = e;
		if(e.getDistance() != null)
			accDistance += e.getDistance().doubleValue();
		checkRep();
	}
	
	/**
	 * simply removes an edge from list
	 * @param e CampusEdge to be removed
	 * @requires e != null
	 * @effects removes edge from list
	 */
	public void removeLastly(CampusEdge<N,E> e) {
		path.remove(e);
	}
	
	/**
	 * returns a protected copy of the list
	 * @return path
	 */
	public List<CampusEdge<N,E>> getPath(){
		return this.path;
	}
	
	/**
	 * returns a protected copy of the accumulated distance
	 * @return accDistance
	 */
	public double getAccDistance() {
		return this.accDistance;
	}
	
	/**
	 * returns the latest edge added to list
	 * @return latest
	 */
	public CampusEdge<N,E> getLatest(){
		return this.latest;
	}
	
	/**
	 * returns an unique integer value according to accumulated distance
	 * @return integer value according to the accumulated distance
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(accDistance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * determines either two objects are equal or not
	 * @return boolean according to whether two object's accDistance is equal or not
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampusPath<?, ?> other = (CampusPath<?, ?>) obj;
		if (Double.doubleToLongBits(accDistance) != Double.doubleToLongBits(other.accDistance))
			return false;
		return true;
	}
	
	/**
	 * compares the accumulated distance of two paths
	 * @return integer value according to the comparison between two accumulated distance 
	 */
	@Override
	public int compareTo(CampusPath<?,?> arg) {
		return Double.compare(this.getAccDistance(), arg.getAccDistance());
	}
	
	/**
	 * returns a string format of this object
	 * @return a string format
	 */
	@Override
	public String toString() {
		return "[" + accDistance + ", " + path.toString() + "]";
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	private void checkRep() {
		assert(path != null && accDistance >= 0.0);
	}
}