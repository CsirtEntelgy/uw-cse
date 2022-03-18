package hw7;

import java.util.*;
import hw5.*;
import hw6.*;
import hw6.MarvelParser.MalformedDataException;

/**
 * MarvelPaths2 is a immutable class for representing the relationship between its characters.
 * It consists of a graph that maps each character to the edges that stretch out of them.
 * An edge represents a relationship of two characters appearing in the same book, labeled with a
 * double value that anti-scales with the number of times the two characters appear in the same book.
 * Immutable meaning the MarvelPaths representation cannot be changed after initialization with the given file.
 * <p>
 *
 * A MarvelPaths can be described as
 * {c_1 = [e_(c_1)->(c_i)_(b_i), ... , e_(c_n)->(c_i)_(b_i), ... , c_n = [...]}.
 * where c_1 ... c_n are characters that is being considered
 * e_(c_n)->(c_i)_(b_i) meaning the edge that
 * stretches out of the character c_n to character c_i and they mutually appeared in b_i number of the books
 * <p>
 *
 * An example of a MarvelPaths includes: {SuperMan = [GraphEdge(SuperMan, BatMan, 0.333), 
 * 													  GraphEdge(SuperMan, WonderWoman, 0.333)],
 * 									      BatMan = [GraphEdge(BatMan, SuperMan, 0.333),
 * 													GraphEdge(BatMan, WonderWoman, 0.333)],
 * 										  WonderWoman = [GraphEdge(WonderWoman, BatMan, 0.333),
 * 														 GraphEdge(WonderWoman, SuperMan, 0.333)]}
 * 
 * Which stands for the marvel universe where SuperMan, BatMan, and WonderWoman all appear in 3 movies together.
 * 
 * @author Young Bin Cho
 */

public class MarvelPaths2{
	
	// This class represents the relationship between each nodes by
	// pairing each nodes with the edges stretching out of them
	//
	// Abstraction Function:
	//		The Graph marvelUniverse represents the relationship between each nodes
	//		for(String i : marvelUniverse.keySet())
	//			All Edges in marvelUniverse.get(i) reaches out from i to some other character
	//			for(GraphEdge g : marvelUniverse.get(i))
	//				g.getData() is the inverse of number of books i and g.getTo() appear together
	//
	// Representation Invariant:
	//		marvelUniverse != null
	//		All edges hold non-negative number (this always kept by the constructor)
	// 		for <i, ... , i_n> in marvelUniverse.keySet()
	//			All i, ... ,i_n isConnected to some i_k
	//			Some i and i_k are never equal
	// In other words:
	//		The graph must be initialized.
	//		All edges of the graph must be non-negative.
	//		Every edge should be pointing from and to a node in the keySet().
	//		There should be no identical nodes (this is always kept by using the Set implementation).
	
	/** The Graph that holds the relationship between characters */
	private Graph<String, Double> marvelUniverse;
	
	/**
	 * @effects constructs an empty graph
	 */
	public MarvelPaths2() {
		marvelUniverse = new Graph<String, Double>();
		checkRep();
	}
	
	/**
	 * constructs a marvel universe out of the user given file of relationships.
	 * the edges in this graph holds the inverse of the number of times two characters 
	 * (nodes) appear in the same book.
	 * @param filename the file which holds all relationship in the format of "character" <tab> "book"
	 * @requires filename is a valid file of valid format, given the "short" format for the file name
	 * @effects marvel universe is initialized
	 */
	public MarvelPaths2(String filename) throws MalformedDataException {
		Set<String> characters = new HashSet<String>();
		Map<String, List<String>> books = new HashMap<String, List<String>>();
		MarvelParser.parseData("src/hw7/data/"+filename, characters, books);
		Set<String> temp = new HashSet<String>(characters);
		List<GraphEdge<String,Double>> resultingList = new ArrayList<GraphEdge<String,Double>>();
		for(String s1 : characters) {
			temp.remove(s1);
			for(String s2 : temp) {
				int i = 0;
				for(String s3 : books.keySet()) {
					if(books.get(s3).contains(s1) && books.get(s3).contains(s2))
						i++;
				}					
				if(i != 0) {
					resultingList.add(new GraphEdge<String,Double>(s1, s2, 1.0/i));
					resultingList.add(new GraphEdge<String,Double>(s2, s1, 1.0/i));
				}
			}
			characters = temp;
		}
		marvelUniverse = new Graph<String,Double>(resultingList);
		checkRep();
	}
	
	/**
	 * searches the path from the starting character to destination character.
	 * uses such search that returns the least cost path rather than shortest.
	 * the cost of each path is calculated by the sum of all data each edge holds
	 * that is in the path.
	 * @requires start != null && dest != null && graph contains start and dest
	 * @param start the character that search begins on
	 * @param dest the character that search ends on
	 * @return list of edges that lead from starting character to the destination character.
	 * 		   returns null if path not found or starting and ending point of search is illegal.
	 * @throws IllegalArgumentException if graph doesn't contain start as a node
	 */
	public MarvelPath<String,Double> findPath(String start, String dest){
		MarvelPath<String,Double> p = new MarvelPath<String,Double>();
		Queue<MarvelPath<String,Double>> active = new PriorityQueue<MarvelPath<String,Double>>();
		Set<String> finished = new HashSet<String>();
		p.addEdge(new GraphEdge<String,Double>(start,start, null));
		active.add(p);
		//while there are more nodes to examine
		while(!active.isEmpty()) {
			//temporary view of the data (currently examined node and path)
			MarvelPath<String,Double> minPath = active.remove();
			String minDest = minPath.getLatest().getTo();
			//return the current path if destination matches 
			//remove the initial path if search not redundant
			if(minDest.equals(dest)) {
				if(!start.equals(dest))
					minPath.removeLastly(new GraphEdge<String,Double>(start,start,null));
					return minPath;
			}
			//if node already visited, don't examine it
			if(finished.contains(minDest))
				continue;
			//examine edges, find ones that reach out of the latest edge of the path
			//if the nodes haven't been visited, adds them to path
			for(GraphEdge<String,Double> e : marvelUniverse.getAllEdges()) {
				if(e.getFrom().equals(minDest)) {
					if(!finished.contains(e.getTo())) {
						MarvelPath<String,Double> temp_path = new MarvelPath<String,Double>(minPath);
						temp_path.addEdge(e);
						active.add(new MarvelPath<String,Double>(temp_path));
					}
				}
			}
			//adds the current node to the list of visited ones
			finished.add(minDest);
		}
		//if search not found(loop terminates), return null
		return null;
	}
	
	/** Below are rewrite of methods in Graph.java due to the test driver*/
	public boolean containsNode(String str) {
		return marvelUniverse.containsNode(str);
	}
	public void addNode(String str) {
		marvelUniverse.addNode(str);
	}
	public void addEdge(GraphEdge<String,Double> g) {
		marvelUniverse.addEdge(g);
	}
	public Set<String> getAllNodes() {
		return marvelUniverse.getAllNodes();
	}
	public List<GraphEdge<String,Double>> getAllEdgesFrom(String str){
		return marvelUniverse.getAllEdgesFrom(str);
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	private void checkRep() {
		assert(marvelUniverse != null);
	}
}