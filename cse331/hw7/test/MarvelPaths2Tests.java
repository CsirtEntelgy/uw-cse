package hw7.test;

import hw5.*;
import hw7.*;
import hw6.MarvelParser.MalformedDataException;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class MarvelPaths2Tests {
	
	private static MarvelPaths2 mp;
	private static MarvelPath<String, Double> expected1;
	private static MarvelPath<String, Double>  expected2;
	private static MarvelPath<String, Double>  expected3;
	
	@BeforeClass 
    public static void setupBeforeTests() throws Exception {
		mp = new MarvelPaths2("marvelpathstestsdata.tsv");
		
		expected1 = new MarvelPath<String,Double>();
		expected2 = new MarvelPath<String,Double>();
		expected3 = new MarvelPath<String,Double>();
		
		expected1.addEdge(new GraphEdge<String,Double>("Ms.A", "Ms.A", 0.0));
		
		expected2.addEdge(new GraphEdge<String,Double>("Ms.B", "Mr.A", 1.0));
		expected2.addEdge(new GraphEdge<String,Double>("Mr.A", "Mr.Mystery", 1.0));
		expected2.addEdge(new GraphEdge<String,Double>("Mr.Mystery", "Mr.End", 1.0));
		
		expected3.addEdge(new GraphEdge<String,Double>("Ms.A", "Mr.A", (1.0/3.0)));
		expected3.addEdge(new GraphEdge<String,Double>("Mr.A", "Mr.Mystery", 1.0));
    }
	
	@Test
	public void testConstructor() throws MalformedDataException {
		new MarvelPaths2();
		new MarvelPaths2("marvelpathstestsdata.tsv");
	}
	
	@Test
	public void testPathFinding() {
		assertEquals(mp.findPath("Mr.xD", "Ms.xD"), null);
		assertEquals(mp.findPath("Ms.A", "Ms.A"), expected1);
		assertEquals(mp.findPath("Ms.B", "Mr.End"), expected2);
		assertEquals(mp.findPath("Ms.A", "Mr.Mystery"), expected3);
	}
}