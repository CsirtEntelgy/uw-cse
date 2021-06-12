package hw8;

import java.util.*;

/**
 * DijkstraAlgorithm is an immutable class that finds the shortest path between two nodes
 * Immutable meaning this object isn't changeable after initiation.
 * <p>
 * 
 * The generic type N stands for the type the nodes of the graph holds.
 * The generic type E stands for the type the edges of the graph holds.
 * <p>
 *
 * @author Young Bin Cho
 */

public class DijkstraAlgorithm<N,E extends Number> {
	
	// This class is a method for finding the shortest path
	// not an ADT.
	
	/**fields to hold data*/
	private List<CampusNode<N>> allNodes;
	private List<CampusEdge<N,E>> allEdges;
	private Queue<CampusPath<N,E>> active;
	private Set<CampusNode<N>> finished;
	
	/**
	 * @effects constructs an empty this
	 */
	public DijkstraAlgorithm() {
		allNodes = new ArrayList<CampusNode<N>>();
		allEdges = new ArrayList<CampusEdge<N,E>>();
		active = new PriorityQueue<CampusPath<N,E>>();
		finished = new HashSet<CampusNode<N>>();
	}
	
	/**
	 * constructs the graph out of a CampusGraph
	 * @param m the CampusGraph to be used
	 * @requires m != null
	 * @effects populates according data structures
	 */
	public DijkstraAlgorithm(CampusGraph<N,E> m) {
		allNodes= new ArrayList<CampusNode<N>>(m.getAllNodes());
		allEdges = new ArrayList<CampusEdge<N,E>>(m.getAllEdges());
		active = new PriorityQueue<CampusPath<N,E>>();
		finished = new HashSet<CampusNode<N>>();
	}
	
	/**
	 * performs the search between two buildings (names given with shortened version)
	 * @param start the node to start the search
	 * @param dest the node to end the search
	 * @return the shortest path from start to destination
	 */
	public CampusPath<N,E> search(String start, String dest){
		CampusPath<N,E> p = new CampusPath<N,E>();
		CampusNode<N> start_node = new CampusNode<N>();
		CampusNode<N> dest_node = new CampusNode<N>();
		//finding nodes that accords to the given short names
		for(CampusNode<N> c : allNodes) {
			if(c.getShortName().equals(start))
				start_node = c;
			if(c.getShortName().equals(dest))
				dest_node = c;
		}
		p.addEdge(new CampusEdge<N,E>(start_node,start_node, null));
		active.add(p);
		//while there are more nodes to examine
		while(!active.isEmpty()) {
			//temporary view of the data (currently examined node and path)
			CampusPath<N,E> minPath = active.remove();
			CampusNode<N> minDest = minPath.getLatest().getTo();
			//return the current path if destination matches 
			//remove the initial path if search not redundant
			if(minDest.getPoint().equals(dest_node.getPoint())) {
				if(!start.equals(dest))
					minPath.removeLastly(new CampusEdge<N,E>(start_node,start_node, null));
				return minPath;
			}
			//if node already visited, don't examine it
			if(finished.contains(minDest))
				continue;
			//examine edges, find ones that reach out of the latest edge of the path
			//if the nodes haven't been visited, adds them to path
			for(CampusEdge<N,E> e : allEdges) {
				if(e.getFrom().equals(minDest)) {
					if(!finished.contains(e.getTo())) {
						CampusPath<N,E> temp_path = new CampusPath<N,E>(minPath);
						temp_path.addEdge(e);
						active.add(new CampusPath<N,E>(temp_path));
					}
				}
			}
			//adds the current node to the list of visited ones
			finished.add(minDest);
		}
		//if search not found(loop terminates), return null
		return null;
	}
}