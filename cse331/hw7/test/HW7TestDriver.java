package hw7.test;

import java.io.*;
import java.util.*;
import hw5.*;
import hw7.*;
import hw6.MarvelParser.MalformedDataException;

/**
 * This class implements a testing driver which reads test scripts
 * from files for your graph ADT and improved MarvelPaths application
 * using Dijkstra's algorithm.
 **/
public class HW7TestDriver {
	public static void main(String args[]) {
        try {
        	if (args.length > 1) {
        		printUsage();
                return;
            }
            HW7TestDriver td;
            if (args.length == 0) {
                td = new HW7TestDriver(new InputStreamReader(System.in), new OutputStreamWriter(System.out));
            } else {
                String fileName = args[0];
                File tests = new File (fileName);
                if (tests.exists() || tests.canRead()) {
                    td = new HW7TestDriver(new FileReader(tests), new OutputStreamWriter(System.out));
                } else {
                    System.err.println("Cannot read from " + tests.toString());
                    printUsage();
                    return;
                }
            }
            td.runTests();
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }
	
    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("to read from a file: java hw6.test.HW6TestDriver <name of input script>");
        System.err.println("to read from standard in: java hw6.test.HW6TestDriver");
    }

    /** String -> Graph: maps the names of graphs to the actual graph **/
    private final Map<String, MarvelPaths2> graphs = new HashMap<String, MarvelPaths2>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @requires r != null && w != null
     *
     * @effects Creates a new HW5TestDriver which reads command from
     * <tt>r</tt> and writes results to <tt>w</tt>.
     **/
    public HW7TestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @effects Executes the commands read from the input and writes results to the output
     * @throws IOException if the input or output sources encounter an IOException
     **/
    public void runTests() throws IOException {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) || (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();
                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens())
                        arguments.add(st.nextToken());
                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }
    
    private void executeCommand(String command, List<String> arguments) {
        try {
        	if (command.equals("CreateGraph")) {
                createGraph(arguments);
        	}else if (command.equals("LoadGraph")) {
                loadGraph(arguments);
            } else if (command.equals("FindPath")) {
                findPath(arguments);
            } else if (command.equals("AddNode")) {
                addNode(arguments);
            } else if (command.equals("AddEdge")) {
                addEdge(arguments);
            } else if (command.equals("ListNodes")) {
                listNodes(arguments);
            } else if (command.equals("ListChildren")) {
                listChildren(arguments);
            } else {
                output.println("Unrecognized command: " + command);
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1)
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
    	if(!graphs.containsKey(graphName)) {
    		graphs.put(graphName, new MarvelPaths2());
    		output.println("created graph " + graphName);
    	}
    }
    
    private void loadGraph(List<String> arguments) throws MalformedDataException {
        if (arguments.size() != 2)
            throw new CommandException("Bad arguments to LoadGraph: " + arguments);
        createGraph(arguments.get(0), arguments.get(1));
    }

    private void createGraph(String graphName, String filepath) throws MalformedDataException {
    	if(!graphs.containsKey(graphName)) {
    		graphs.put(graphName, new MarvelPaths2(filepath));
    		output.println("loaded graph " + graphName);
    	}
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3)
            throw new CommandException("Bad arguments to FindPath: " + arguments);
        findPath(arguments.get(0), arguments.get(1).replaceAll("_", " "), arguments.get(2).replaceAll("_", " "));
    }

    private void findPath(String graphName, String char1, String charn) {
    	if(graphs.containsKey(graphName)) {
    		try {
    			double total = 0.00;
    			List<GraphEdge<String,Double>> lst = graphs.get(graphName).findPath(char1, charn).getPath();
    			output.println("path from " + char1 + " to " + charn + ":");
    			if(lst == null)
    				output.println("no path found");
    			else {
    				for(GraphEdge<String,Double> g : lst) {
    					output.println(g.getFrom() + " to " + g.getTo() + " with weight " 
    									+ String.format("%.3f", g.getData()));
    					total += g.getData();
    				}
    				output.println("total cost: " + String.format("%.3f", total));
    			}
    		}catch(IllegalArgumentException e) {
    			output.println("unknown character " + char1);
    		}catch(IllegalStateException e) {
    			output.println("unknown character " + charn);
    		}
    	}
    }
    
    private void addNode(List<String> arguments) {
        if (arguments.size() != 2)
            throw new CommandException("Bad arguments to addNode: " + arguments);
        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);
        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
    	if(!graphs.get(graphName).containsNode(nodeName)) {
    		graphs.get(graphName).addNode(nodeName);;
    		output.println("added node " + nodeName + " to " + graphName);
    	}
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4)
            throw new CommandException("Bad arguments to addEdge: " + arguments);
        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);
        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
            String edgeData) {
        graphs.get(graphName).addEdge(new GraphEdge<String,Double>(parentName, 
        								childName, Double.parseDouble(edgeData)));
        output.println("added edge " + String.format("%.3f", Double.parseDouble(edgeData)) + " from " + 
        								parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1)
            throw new CommandException("Bad arguments to listNodes: " + arguments);
        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
    	output.println(graphName + " contains:");
    	output.println(graphs.get(graphName).getAllNodes());
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2)
            throw new CommandException("Bad arguments to listChildren: " + arguments);
        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
    	output.print("the children of " + parentName + " in " + graphName + " are:");
    	List<GraphEdge<String,Double>> temp = graphs.get(graphName).getAllEdgesFrom(parentName);
    	Collections.sort(temp);
    	for(GraphEdge<String,Double> g : temp) {
    		output.print(" "+ g.getTo() + "(" + String.format("%.3f", g.getData()) + ")");
    	}
    }
    
    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {
        public CommandException() {
            super();
        }
        public CommandException(String s) {
            super(s);
        }
        public static final long serialVersionUID = 3495;
    }
}