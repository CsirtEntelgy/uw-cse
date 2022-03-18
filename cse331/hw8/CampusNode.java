package hw8;

/**
 * CampusNode is an immutable class for representing a labeled node.
 * A CampusNode consists of some object of type N and two strings.
 * Nodes are compared via their object of type N.
 * Immutable meaning data stored in CampusNode can't be changed after initialization.
 * <p>
 *
 * A CampusNode can be described as
 * (n, S1, S2)
 * where n is some object of type N, some string s1, and another string s2.
 * s1 stands for the shortened name and s2 stands for the full name.
 * <p>
 * 
 * The generic type N stands for the type of the object the node holds.
 * <p>
 *
 * An example of a CampusNode includes: (<1.0, 2.0>, "short_name", "full_name") and
 * 										(<3.0, 1.2223>, "r_s", "random_short")
 *
 * @author Young Bin Cho
 */

public class CampusNode<N> implements Comparable<CampusNode<?>> {
	
	// This class represents a node storing an object of type N and two strings.
	// Does not represent an ADT.
	//
	// Representation Invariant:
	//		shortName != null && longName != null
	// In other words:
	//		String fields must be initialized (object may or may not be initialized)
	
	/** data fields that hold the object and strings that compose the CampusNode*/
	private final N point;
	private final String shortName;
	private final String longName;
	
	/**
	 * @effects constructs an empty node
	 */
	public CampusNode() {
		point = null;
		shortName = "";
		longName = "";
		checkRep();
	}
	
	/**
	 * constructs a new CampusNode out of another CampusNode
	 * @param c another CampusNode to be set as this one
	 * @requires c != null
	 * @effects constructs a new node from another node
	 */
	public CampusNode(CampusNode<N> c) {
		point = c.getPoint();
		shortName = c.getShortName();
		longName = c.getLongName();
		checkRep();
	}
	
	/**
	 * constructs a CampusNode from given object<N> and two strings
	 * @param point the object of type N that is to be set as point
	 * @param shortName the string that is to be set as shortName
	 * @param longName the string that is to be set as longName
	 * @requires point != null && shortName != null && longName != null
	 * @effects constructs a new node out of given data
	 */
	public CampusNode(N point, String shortName, String longName) {
		this.point = point;
		this.shortName = shortName;
		this.longName = longName;
		checkRep();
	}
	
	/**
	 * returns a protected copy of point
	 * @return point object of this node
	 */
	public N getPoint() {
		return this.point;
	}
	
	/**
	 * returns a protected copy of shortName
	 * @return string shortName of this node
	 */
	public String getShortName() {
		return this.shortName;
	}
	
	/**
	 * returns a protected copy of longName
	 * @return string longName of this node
	 */
	public String getLongName() {
		return this.longName;
	}
	
	/**
	 * generated method
	 * @return returns a certain integer according to point field
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}
	
	/**
	 * generated method
	 * @param the object to compare with this one
	 * @returns true if given object holds the same point field, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampusNode<?> other = (CampusNode<?>) obj;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}
	
	/**
	 * implemented method for implementing comparable interface
	 * @param some CampusNode of type ?
	 * @return result of comparing the toString of this and given node 
	 */
	@Override
	public int compareTo(CampusNode<?> arg) {
		return this.toString().compareTo(arg.toString());
	}
	
	/**
	 * a string representation of this node
	 * @return string format of this node
	 */
	@Override
	public String toString() {
		return "("+shortName+", "+longName+", "+point+")";
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	private void checkRep() {
		assert(longName != null && shortName != null);
	}
}