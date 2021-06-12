package hw6.test;

import java.util.*;
import hw5.*;
import hw6.*;
import hw6.MarvelParser.MalformedDataException;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class MarvelPathsTests {
	
	private static MarvelPaths mp;
	private static List<GraphEdge<String,String>> expected1;
	private static List<GraphEdge<String,String>> expected2;
	private static List<GraphEdge<String,String>> expected3;
	
	@BeforeClass 
    public static void setupBeforeTests() throws Exception {
		mp = new MarvelPaths("marvelpathstestsdata.tsv");
		
		expected1 = new ArrayList<GraphEdge<String,String>>();
		expected2 = new ArrayList<GraphEdge<String,String>>();
		expected3 = new ArrayList<GraphEdge<String,String>>();
		
		expected1.add(new GraphEdge<String,String>("Mr.A", "Ms.A", "Book A"));
		
		expected2.add(new GraphEdge<String,String>("Ms.A", "Mr.A", "Book A"));
		expected2.add(new GraphEdge<String,String>("Mr.A", "Mr.B", "Book B"));
		
		expected3.add(new GraphEdge<String,String>("Ms.A", "Mr.A", "Book A"));
		expected3.add(new GraphEdge<String,String>("Mr.A", "Mr.Mystery", "Book Completed"));
		expected3.add(new GraphEdge<String,String>("Mr.Mystery", "Mr.End", "Book Extended"));
    }
	
	@Test
	public void testConstructor() throws MalformedDataException {
		new MarvelPaths();
		new MarvelPaths("marvelpathstestsdata.tsv");
	}
	
	@Test
	public void testPathFinding() {
		assertEquals(mp.findPath("Mr.A", "Ms.A"), expected1);
		assertEquals(mp.findPath("Ms.A", "Mr.B"), expected2);
		assertEquals(mp.findPath("Ms.A", "Mr.End"), expected3);
	}
}