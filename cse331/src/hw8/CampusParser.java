package hw8;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hw6.MarvelParser.MalformedDataException;

/**
 * CampusParser is an immutable class for parsing a file into a map.
 * Immutable meaning this object can't be altered after initiation.
 * <p>
 *
 * @author Young Bin Cho
 */

public class CampusParser {
	
	// Representation Invariant:
	//		nodes != null && map != null
	// In other words:
	//		Map (and the nodes being considered) must be initialized
	
	/**List that holds all the nodes in the map and also holds the map itself*/
	private List<CampusNode<Point2D>> nodes;
	private Map<CampusNode<Point2D>, List<CampusEdge<Point2D,Double>>> map;
	
	/**
	 * @effects constructs an empty parser
	 */
	public CampusParser() {
		map = new HashMap<CampusNode<Point2D>, List<CampusEdge<Point2D,Double>>>();
		nodes = new ArrayList<CampusNode<Point2D>>();
		checkRep();
	}
	
	/**
	 * obtains all the nodes from file holding building informations
	 * @param filename file holding building informations
	 * @throws MalformedDataException if given file is out of format
	 * @effects obtains and populates the set "nodes"
	 */
	public void parseBuildings(String filename) throws MalformedDataException{
		BufferedReader reader = null;
	    try {
	    	//reading in the given file in the hw8/data/ directory
	    	reader = new BufferedReader(new FileReader("src/hw8/data/"+filename));
	        String inputLine;
	        //if the line is not null and doesn't start with # (being comments), reads the line
	        while ((inputLine = reader.readLine()) != null) {
	            if (inputLine.startsWith("#"))
	                continue;
	            //splits the line by tabs
	            String[] tokens = inputLine.split("\t");
	            //there should be exactly 4 tokens
	            if (tokens.length != 4)
	                throw new MalformedDataException("Line should contain exactly three tabs: " + inputLine);
	            //create Point2D object out of given x, y coordinates
	            Point2D cord = new Point2D.Double(Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
	            //creates and stores the CampusNode
	            nodes.add(new CampusNode<Point2D>(cord, tokens[0], tokens[1]));
	        }
	    } 
	    catch (IOException e) {
	    	//exception handle
	        System.err.println(e.toString());
	        e.printStackTrace(System.err);
	    } 
	    finally {
	    	//closing the reader
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e) {
	                System.err.println(e.toString());
	                e.printStackTrace(System.err);
	            }
	        }
	    }
	}
	
	/**
	 * completes the map by adding edges to appropriate nodes
	 * @param filename file containing path informations
	 * @throws MalformedDataException if given file is out of format
	 * @effects populates map with according nodes and edges
	 */
	public void parsePaths(String filename){
		BufferedReader reader = null;
	    try {
	    	//reading in the given file in the hw8/data/ directory
	    	reader = new BufferedReader(new FileReader("src/hw8/data/"+filename));
	    	//read in first line
	        String inputLine = reader.readLine();
	        //while lines are not null
	        while (inputLine != null) {
	        	//ignoring comment lines
	            if (inputLine.startsWith("#"))
	                continue;
	            //if the line doesn't start with tab
	            if (inputLine.startsWith("")) {
	            	//splitting the non-tab line to make a CampusNode
	            	String[] point1 = inputLine.split(",");
	            	CampusNode<Point2D> from = new CampusNode<Point2D>
											  	(new Point2D.Double(Double.parseDouble(point1[0]), 
											  	 Double.parseDouble(point1[1])),"","");
	            	//if the created CampusNode has the same coordinate as one of the nodes, 
	            	//let it be that node
	            	if(nodes.contains(from))
	            		from = nodes.get(nodes.indexOf(from));
	            	//create temporary list for each non-tab line
	            	List<CampusEdge<Point2D,Double>> tempList = new ArrayList<CampusEdge<Point2D,Double>>();
	            	//read in next line
	            	inputLine = reader.readLine();
	            	//while current line isn't null and starts with a tab
	            	while(!(inputLine == null) && inputLine.startsWith("\t")) {
	            		//parsing the tab line into a coordinate and distance
	            		inputLine = inputLine.replace("\t", "");
	            		String[] parts = inputLine.split(": ");
	            		String[] point2 = parts[0].split(",");
	            		//creating a CampusNode from the coordinate
	            		CampusNode<Point2D> to = new CampusNode<Point2D>
													(new Point2D.Double(Double.parseDouble(point2[0]), 
													 Double.parseDouble(point2[1])),"","");
	            		//if the CampusNode is present in the set of nodes, replace
	            		if(nodes.contains(to))
		            		to = nodes.get(nodes.indexOf(to));
	            		//add the CampusEdge object created from above information to the list
	            		tempList.add(new CampusEdge<Point2D,Double>(from, to, Double.parseDouble(parts[1])));
	            		//read next line
	            		inputLine = reader.readLine();
	            	}
	            	//finished examining all the tab lines under the non-tab line
	            	//add the (key: non-tab line, value: tab lines) to the map 
	            	map.put(from, tempList);
	            	//read next non-tab line
	            }
	        }
	    } 
	    catch (IOException e) {
	    	//exception handle
	        System.err.println(e.toString());
	        e.printStackTrace(System.err);
	    } 
	    finally {
	    	//closing the reader
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e) {
	                System.err.println(e.toString());
	                e.printStackTrace(System.err);
	            }
	        }
	    }
	}
	
	/**
	 * returns a protected copy of the parsed map
	 * @returns map
	 */
	public Map<CampusNode<Point2D>, List<CampusEdge<Point2D,Double>>> getMap(){
		return this.map;
	}
	
	/**
	 * Checks that the representation invariant holds (if any)
	 */
	private void checkRep() {
		assert(nodes != null && map != null);
	}
}