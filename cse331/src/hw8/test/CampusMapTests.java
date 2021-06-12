package hw8.test;

import static org.junit.Assert.assertEquals;
import hw8.*;
import java.awt.geom.Point2D;
import org.junit.BeforeClass;
import org.junit.Test;

import hw6.MarvelParser.MalformedDataException;

public class CampusMapTests {
	private static CampusMap mp;
	private static CampusPath<Point2D, Double> expected1;
	private static CampusPath<Point2D, Double> expected2;
	private static CampusPath<Point2D, Double> expected3;
	
	@BeforeClass 
    public static void setupBeforeTests() throws Exception {
		mp = new CampusMap("campus_test_buildings.dat", "campus_test_paths.dat");
		
		expected1 = new CampusPath<Point2D, Double>();
		expected2 = new CampusPath<Point2D, Double>();
		expected3 = new CampusPath<Point2D, Double>();
		
		expected1.addEdge(new CampusEdge<Point2D,Double>
							(new CampusNode<Point2D>(new Point2D.Double(0.0, 0.0), "t1", "test"), 
							 new CampusNode<Point2D>(new Point2D.Double(0.0, 0.0), "t1", "test"), null));
		
		expected2.addEdge(new CampusEdge<Point2D,Double>
							(new CampusNode<Point2D>(new Point2D.Double(0.0, 0.0), "t1", "test"), 
							 new CampusNode<Point2D>(new Point2D.Double(1.0, 1.0), "", ""), 1.4142136));
		expected2.addEdge(new CampusEdge<Point2D,Double>
							(new CampusNode<Point2D>(new Point2D.Double(1.0, 1.0), "", ""), 
							 new CampusNode<Point2D>(new Point2D.Double(2.0, 2.0), "t2", "test2"), 1.4142136));
		
		expected3.addEdge(new CampusEdge<Point2D,Double>
							(new CampusNode<Point2D>(new Point2D.Double(2.0, 2.0), "t2", "test2"), 
							 new CampusNode<Point2D>(new Point2D.Double(3.0, 2.0), "", ""), 1.0));
		expected3.addEdge(new CampusEdge<Point2D,Double>
							(new CampusNode<Point2D>(new Point2D.Double(3.0, 2.0), "", ""), 
							 new CampusNode<Point2D>(new Point2D.Double(1.0, 0.0), "t3", "test3"), 2.8284273));
    }
	
	@Test
	public void testConstructor() throws MalformedDataException {
		//empty constructor test
		new CampusMap();
		//parsing data test
		new CampusMap("campus_test_buildings.dat", "campus_test_paths.dat");
		//parsing big data test
		new CampusMap("campus_buildings.dat", "campus_paths.dat");
	}
	
	@Test
	public void testPathFinding() {
		//path to itself
		assertEquals(mp.findPath("t1", "t1"), expected1);
		//single hop test
		assertEquals(mp.findPath("t1", "t2"), expected2);
		//two hop test (with two routes with different weight)
		assertEquals(mp.findPath("t2", "t3"), expected3);
		//no path test (no route)
		assertEquals(mp.findPath("t2", "t1"), null);
	}
}