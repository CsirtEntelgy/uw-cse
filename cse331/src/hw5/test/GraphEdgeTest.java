package hw5.test;

import org.junit.BeforeClass;
import org.junit.Test;
import hw5.*;

public class GraphEdgeTest {
	private static GraphEdge<String, String> g1;
	private static GraphEdge<String, String> g2;
	
	@BeforeClass 
    public static void setupBeforeTests() throws Exception {
        g1 = new GraphEdge<String, String>("a", "c", "hi");
        g2 = new GraphEdge<String, String>("a", "b", "");
    }
	
	@Test
	public void testConstructor() {
		new GraphEdge<String, String>("a", "c", "");
		new GraphEdge<String, String>("c", "d", "HELLO");
	}
	
	@Test
	public void testGetFields() {
		assert(g1.getFrom().equals("a"));
		assert(g1.getTo().equals("c"));
		assert(g1.getFrom().equals("a"));
		assert(g1.getTo().equals("c"));
		assert(!g1.getFrom().equals("q"));
		assert(g1.getData().equals("hi"));
		assert(!g1.getData().equals("nono"));
	}
	
	@Test
	public void testToString() {
		assert(g1.toString().equals("<a,c,hi>"));
		assert(g2.toString().equals("<a,b,>"));
	}
}