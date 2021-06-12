package hw8;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import hw6.MarvelParser.MalformedDataException;

/**
 * CampusMap is an immutable interface for representing a map around UW campus.
 * Immutable meaning the map itself is unchangeable after initiation.
 * <p>
 *
 * A CampusMap can be described as all buildings and paths around campus, each path
 * weighted via their distance.
 * <p>
 *
 * An example of a CampusMap can be seen in the image files included in the package
 *
 * @author Young Bin Cho
 */

public class CampusMap {
	
	// This class represents a map around UW campus
	//
	// Abstraction Function:
	//		Each building (node) contained in map i
	//		i.isConnected(some element(s) in graph.keySet())
	//		and that some element isConnected to i
	//		(this kept by format of the file)
	//
	// Representation Invariant:
	//		map != null
	// 		for all <i ... i_n> in map.getAllEdges()
	//			All i, ... ,i_n has non negative getDistance() values
	//			and all edges must be connected to or from some node
	//		(this kept when reading in file)
	
	/**a main method for user interface*/
	public static void main(String args[]) throws MalformedDataException {
		sc = new Scanner(System.in);
		CampusMap cm = new CampusMap("campus_buildings.dat", "campus_paths.dat");
		//displaying initial menu options
		System.out.println("Menu:");
		System.out.println("\t" + "r to find a route");
		System.out.println("\t" + "b to see a list of all buildings");
		System.out.println("\t" + "q to quit");
		System.out.println();
		System.out.print("Enter an option ('m' to see the menu): ");
		//reading-in user input
		String userInput = sc.nextLine();
		while(userInput!= null) {
			//ignoring all comment lines
			if(userInput.startsWith("#") || userInput.equals("")) {
				System.out.println(userInput);
				userInput = sc.nextLine();
				continue;
			}
			//route finding
			if(userInput.equals("r")) {
				//getting user input
				System.out.print("Abbreviated name of starting building: ");
				String start = sc.nextLine();
				System.out.print("Abbreviated name of ending building: ");
				String dest = sc.nextLine();
				//finding long names
				String longNameStart = null;
				String longNameDest = null;
				for(CampusNode<Point2D> c : cm.getAllNodes()) {
					if(c.getShortName().equals(start))
						longNameStart = c.getLongName();
					if(c.getShortName().equals(dest))
						longNameDest = c.getLongName();
				}
				if(longNameStart == null || longNameDest == null) {
					if(longNameStart == null)
						System.out.println("Unknown building: " + start);
					if(longNameDest == null)
						System.out.println("Unknown building: " + dest);
				}
				else {
					System.out.println("Path from " + longNameStart + " to " + longNameDest + ":");
					//path finding
					CampusPath<Point2D,Double> tempPath = cm.findPath(start, dest);
					List<CampusEdge<Point2D,Double>> tempList = tempPath.getPath();
					for(CampusEdge<Point2D,Double> c : tempList) {
						System.out.println("\t" + "Walk " + String.format("%.0f", c.getDistance()) + " feet " 
											+ cm.findDirection(c) + " to " + "(" +
											String.format("%.0f", c.getTo().getPoint().getX()) + ", "
											+ String.format("%.0f", c.getTo().getPoint().getY()) + ")");
					}
					System.out.println("Total distance: "
							+ String.format("%.0f", tempPath.getAccDistance()) + " feet");
				}
			}
			else if(userInput.equals("b")) {
				List<CampusNode<Point2D>> tempList = new ArrayList<CampusNode<Point2D>>(cm.getAllNodes());
				Collections.sort(tempList);
				for(CampusNode<Point2D> c : tempList) {
					if(!c.getShortName().equals("") && !c.getLongName().equals(""))
						System.out.println(c.getShortName() + ": " + c.getLongName());
				}
			}
			else if(userInput.equals("q")) {
				break;
			}
			else if(userInput.equals("m")) {
				System.out.println("Menu:");
				System.out.println("\t" + "r to find a route");
				System.out.println("\t" + "b to see a list of all buildings");
				System.out.println("\t" + "q to quit");
			}
			else {
				System.out.println("Unknown option");
			}
			//prompting user for input
			System.out.println();
			System.out.print("Enter an option ('m' to see the menu): ");
			userInput = sc.nextLine();
		}
		sc.close();
	}
	
	/**CampusGraph object to represent the map around campus 
	 * and scanner for use in the main method*/
	private CampusGraph<Point2D,Double> map;
	private static Scanner sc;
	
	/**
	 * @effects constructs an empty map
	 */
	public CampusMap() {
		map = new CampusGraph<Point2D,Double>();
		checkRep();
	}
	
	/**
	 * constructs campus map out of two given files
	 * @param buildings file containing building informations
	 * @param paths file containing path informations
	 * @requires buildings and paths be a valid file name
	 * @throws MalformedDataException if given files are out of format
	 * @effects constructs a new map from given files
	 */
	public CampusMap(String buildings, String paths) throws MalformedDataException {
		CampusParser cp = new CampusParser();
		cp.parseBuildings(buildings);
		cp.parsePaths(paths);
		map = new CampusGraph<Point2D,Double>(cp.getMap());
		checkRep();
	}
	
	/**
	 * finds path between two buildings
	 * @param start short name for the building to start with
	 * @param dest short name for the building to end with
	 * @requires given strings != null
	 * @return a shortest CampusPath from start to destination (weighted by distance)
	 */
	public CampusPath<Point2D,Double> findPath(String start, String dest){
		DijkstraAlgorithm<Point2D,Double> da = new DijkstraAlgorithm<Point2D,Double>(map);
		return da.search(start, dest);
	}
	
	/**
	 * finds the compass direction of the given edge
	 * @param c given CampusEdge to be examined
	 * @requires c != null && c.getTo() and c.getFrom() != null
	 * @return a string representing the direction of this edge
	 */
	private String findDirection(CampusEdge<Point2D,Double> c) {
		double atanTo = Math.atan2(c.getTo().getPoint().getX() - c.getFrom().getPoint().getX(),
								   c.getTo().getPoint().getY() - c.getFrom().getPoint().getY());	
		if(atanTo < 0.0)
			atanTo += (2*Math.PI);
		double atanFinal = Math.toDegrees(atanTo) - 90;
		if(atanFinal < 0.0)
			atanFinal = 360 + atanFinal;
		if((0.0 <= atanFinal && atanFinal <= 180/8.0 || ((15*180/8.0) <= atanFinal && atanFinal <= 360.0)))
			return "E";
		if((180/8.0) < atanFinal && atanFinal < (3*180/8.0))
			return "NE";
		if((3*180/8.0) <= atanFinal && atanFinal <= (5*180/8.0))
			return "N";
		if((5*180/8.0) < atanFinal && atanFinal < (7*180/8.0))
			return "NW";
		if((7*180/8.0) <= atanFinal && atanFinal <= (9*180/8.0))
			return "W";
		if((9*180/8.0) < atanFinal && atanFinal < (11*180/8.0))
			return "SW";
		if((11*180/8.0) <= atanFinal && atanFinal <= (13*180/8.0))
			return "S";
		if((13*180/8.0) < atanFinal && atanFinal < (15*180/8.0))
			return "SE";
		return "";
	}
	
	/**
	 * returns the protected set of all nodes (buildings) in this map
	 * @requires map != null
	 * @returns set view of all the nodes
	 */
	public Set<CampusNode<Point2D>> getAllNodes() {
		return map.getAllNodes();
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	private void checkRep() {
		assert(map != null);
	}
}