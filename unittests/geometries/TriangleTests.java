package geometries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import geometries.impl.Triangle;
import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for geometries.impl.Triangle class
 */
class TriangleTests {

	/**
	 * Test method for {@link geometries.impl.Triangle#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: Test normal of a triangle
		Point p1 = new Point(0, 0, 1);
		Point p2 = new Point(1, 0, 0);
		Point p3 = new Point(0, 1, 0);
		Triangle triangle = new Triangle(p1, p2, p3);
		
		// The normal should be the same for any point on the triangle
		Vector result = triangle.getNormal(new Point(0, 0.5, 0.5));
		
		// The expected normal depends on the order of points (Right-hand rule)
		// For these points, the normal should be normalized (1,1,1)
		Vector expected = new Vector(1, 1, 1).normalize();
		
		// We use the absolute value of dot product to be sure they are parallel 
		// (or simply compare the vectors if the order is known)
		assertEquals(expected, result, "ERROR: Triangle getNormal() wrong result");
	}
}