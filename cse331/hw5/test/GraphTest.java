package hw5.test;

import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;
import hw5.*;

public class GraphTest {
	private static Graph<String,String> g1;
	
	private static List<GraphEdge<String, String>> x1;
	private static List<GraphEdge<String, String>> x2;
	
	private static Set<String> y1;
	
	@BeforeClass 
    public static void setupBeforeTests() throws Exception {
		
		x1 = new ArrayList<GraphEdge<String, String>>();
		x2 = new ArrayList<GraphEdge<String, String>>();
		y1 = new TreeSet<String>();
		
		x1.add(new GraphEdge<String, String>("a", "b", ""));
		x1.add(new GraphEdge<String, String>("a", "c", ""));
		x1.add(new GraphEdge<String, String>("b", "c", ""));
		x1.add(new GraphEdge<String, String>("b", "c", ""));
		x1.add(new GraphEdge<String, String>("c", "d", ""));
		
		x2.add(new GraphEdge<String, String>("a", "b", ""));
		x2.add(new GraphEdge<String, String>("a", "c", ""));
		
		y1.add("a"); 
		y1.add("b"); 
		y1.add("c"); 
		y1.add("d");
		
		g1 = new Graph<String,String>(x1);
    }
	
	@Test
	public void testConstructor() {
		new Graph<String,String>();
	}
	
	@Test
	public void testAllContains() {
		assert(g1.containsEdge(new GraphEdge<String, String>("a", "b", "")));
		assert(!g1.containsEdge(new GraphEdge<String, String>("q", "z", "")));
		assert(g1.containsNode("a"));
		assert(!g1.containsNode("y"));
	}
	
	@Test
	public void testEmpty() {
		assert(new Graph<String,String>().isEmpty());
		assert(!g1.isEmpty());
	}
	
	@Test
	public void testAllGet() {
		assert(y1.equals(g1.getAllNodes()));
		assert(x1.equals(g1.getAllEdges()));
		assert(x2.equals(g1.getAllEdgesFrom("a")));
	}
	
	@Test
	public void testAllRemove() {
		Graph<String,String> tempGraph = new Graph<String,String>(x1);
		List<GraphEdge<String,String>> temp = new ArrayList<GraphEdge<String,String>>(x1);
		Set<String> temp2 = new HashSet<String>(y1);
		
		temp.remove(new GraphEdge<String, String>("a", "b", ""));
		temp.remove(new GraphEdge<String, String>("a", "c", ""));
		temp2.remove("a");
		tempGraph.removeNode("a");
		assert(temp.equals(tempGraph.getAllEdges()));
		assert(temp2.equals(tempGraph.getAllNodes()));
		
		tempGraph.removeEdge(new GraphEdge<String, String>("c", "d", ""));
		temp.remove(new GraphEdge<String, String>("c", "d", ""));
		assert(temp.equals(tempGraph.getAllEdges()));
		
		tempGraph.removeAllEdge(new GraphEdge<String, String>("b", "c", ""));
		temp.remove(new GraphEdge<String, String>("b", "c", ""));
		temp.remove(new GraphEdge<String, String>("b", "c", ""));
		assert(temp.equals(tempGraph.getAllEdges()));
	}
	
	@Test
	public void testAllAdd() {
		Graph<String,String> tempGraph = new Graph<String,String>(x1);
		List<GraphEdge<String, String>> temp = new ArrayList<GraphEdge<String, String>>(x1);
		Set<String> temp2 = new HashSet<String>(y1);
		
		tempGraph.addNode("z");
		temp2.add("z");
		assert(temp2.equals(tempGraph.getAllNodes()));
		
		tempGraph.addEdge(new GraphEdge<String, String>("z", "a", ""));
		temp.add(new GraphEdge<String, String>("z", "a", ""));
		assert(temp.equals(tempGraph.getAllEdges()));
	}
}