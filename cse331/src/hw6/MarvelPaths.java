package hw6;

import java.util.*;
import hw5.*;
import hw6.MarvelParser.MalformedDataException;

/**
 * MarvelPaths is a immutable class for representing the relationship between its characters.
 * It consists of a graph that maps each character to the edges that stretch out of them.
 * An edge represents a relationship of two characters appearing in the same book, labeled with the
 * title of the book. Immutable meaning the MarvelPaths representation cannot be changed after initialization
 * with the given file.
 * <p>
 *
 * A MarvelPaths can be described as
 * {c_1 = [e_(c_1)->(c_i)_(b_i), ... , e_(c_n)->(c_i)_(b_i), ... , c_n = [...]}.
 * where c_1 ... c_n are characters that is being considered
 * e_(c_n)->(c_i)_(b_i) meaning the edge that
 * stretches out of the character c_n to character c_i and they mutually appear in the book b_i
 * <p>
 *
 * An example of a MarvelPaths includes: {SuperMan = [GraphEdge(SuperMan, BatMan, SuperMan vs BatMan), 
 * 													  GraphEdge(SuperMan, WonderWoman, SuperMan vs BatMan)],
 * 									      BatMan = [GraphEdge(BatMan, SuperMan, SuperMan vs BatMan),
 * 													GraphEdge(BatMan, WonderWoman, SuperMan vs BatMan)],
 * 										  WonderWoman = [GraphEdge(WonderWoman, BatMan, SuperMan vs BatMan),
 * 														 GraphEdge(WonderWoman, SuperMan, SuperMan vs BatMan)]}
 * 
 * Which stands for the book "SuperMan vs BatMan" where SuperMan, BatMan, and WonderWoman are featured.
 * 
 * @author Young Bin Cho
 */

public class MarvelPaths{
	
	// This class represents the relationship between each nodes by
	// pairing each nodes with the edges stretching out of them
	//
	// Abstraction Function:
	//		The Graph marvelUniverse represents the relationship between each nodes
	//		for(String i : marvelUniverse.keySet())
	//			All Edges in marvelUniverse.get(i) reaches out from i to some other character
	//			for(GraphEdge g : marvelUniverse.get(i))
	//				g.getData() is the book i and g.getTo() appear together
	//
	// Representation Invariant:
	//		marvelUniverse != null
	// 		for <i, ... , i_n> in marvelUniverse.keySet()
	//			All i, ... ,i_n isConnected to some i_k
	//			Some i and i_k are never equal
	// In other words:
	//		The graph must be initialized.
	//		Every edge should be pointing from and to a node in the keySet().
	//		There should be no identical nodes (this is always kept by using the Set implementation).
	
	public static void main (String args[]) throws MalformedDataException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name of the file to read in:");
		MarvelPaths mp = new MarvelPaths(sc.nextLine());
		System.out.println("Enter the name of the character that the search should begin with:");
		String from = sc.nextLine();
		System.out.println("Enter the name of the character that the search should end with:");
		String to = sc.nextLine();
		List<GraphEdge<String, String>> result = mp.findPath(from, to);
		if(result == null)
			System.out.println("No path found");
		else
			System.out.println("Path Found!: " + result);
		sc.close();
	}
	
	/** The Graph that holds the relationship between characters */
	private Graph<String,String> marvelUniverse;
	
	/**
	 * @effects constructs an empty graph
	 */
	public MarvelPaths() {
		marvelUniverse = new Graph<String,String>();
	}
	
	/**
	 * constructs a marvel universe out of the user given file of relationships
	 * @param filename the file which holds all relationship in the format of "character" <tab> "book"
	 * @requires filename is a valid file of valid format, given the "short" format for the file name
	 * @effects new marvel universe is created
	 */
	public MarvelPaths(String filename) throws MalformedDataException {
		//Call on the helper function
		parseAndInitiate(new HashSet<String>(), new HashMap<String, List<String>>(), filename);
		checkRep();
	}
	
	/**
	 * Helper function for the constructor, just to avoid long repeated code
	 * @param characters an empty set
	 * @param books an empty map
	 * @param filename the filename in "short" format, meaning without extension and of only the file name
	 * @requires characters != null && books != null && filename != null
	 * @effects has the functionality of the constructor
	 */
	private void parseAndInitiate(Set<String> characters, Map<String, List<String>> books, String filename) 
			throws MalformedDataException {
		MarvelParser.parseData("src/hw6/data/"+filename, characters, books);
		marvelUniverse = new Graph<String,String>(characters, books);
	}
	
	/**
	 * searches the path from the starting character to destination character
	 * @requires start != null && dest != null
	 * @param start the character that search begins on
	 * @param dest the character that search ends on
	 * @return list of edges that lead from starting character to the destination character.
	 * 		   returns null if path not found.
	 * @throws IllegalArgumentException if graph doesn't contain start as a node
	 */
	public List<GraphEdge<String,String>> findPath(String start, String dest){
		if(!marvelUniverse.containsNode(start))
			throw new IllegalArgumentException("Starting point not in graph");
		if(!marvelUniverse.containsNode(dest))
			throw new IllegalStateException("Ending point not in graph");
		Map<String, List<GraphEdge<String,String>>> accPaths = new HashMap<String, List<GraphEdge<String,String>>>();
		Queue<String> worklst = new LinkedList<String>();
		worklst.add(start);
		accPaths.put(start, new ArrayList<GraphEdge<String,String>>());
		while(!worklst.isEmpty()) {
			String current = worklst.remove();
			if(current.equals(dest))
				return accPaths.get(current);
			List<GraphEdge<String,String>> tmp = marvelUniverse.getAllEdgesFrom(current);
			tmp.sort((Comparator.naturalOrder()));
			for(GraphEdge<String,String> g : tmp) {
				if(!accPaths.containsKey(g.getTo())) {
					accPaths.put(g.getTo(), new ArrayList<GraphEdge<String,String>>());
					accPaths.get(g.getTo()).addAll(accPaths.get(current));
					accPaths.get(g.getTo()).add(g);
					worklst.add(g.getTo());
				}		
			}
		}
		return null;
	}
	
	/** Below are rewrite of methods in Graph.java due to the test driver*/
	public boolean containsNode(String str) {
		return marvelUniverse.containsNode(str);
	}
	public void addNode(String str) {
		marvelUniverse.addNode(str);
	}
	public void addEdge(GraphEdge<String,String> g) {
		marvelUniverse.addEdge(g);
	}
	public Set<String> getAllNodes() {
		return marvelUniverse.getAllNodes();
	}
	public List<GraphEdge<String,String>> getAllEdgesFrom(String str){
		return marvelUniverse.getAllEdgesFrom(str);
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	public void checkRep() {
		assert(marvelUniverse != null) : "Graph Not Initialized";
	}
}