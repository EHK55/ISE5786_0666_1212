package primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for primitives.Ray class
 */
class RayTests {

	/**
	 * Delta value for accuracy when comparing double values.
	 */
	private static final double DELTA = 0.000001;

	/**
	 * Test method for
	 * {@link primitives.Ray#Ray(primitives.Point, primitives.Vector)}.
	 */
	@Test
	void testConstructor() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: Test that the direction vector is normalized by the constructor
		Vector v = new Vector(2, 4, 6); // This vector's length is not 1
		Point p = new Point(1, 2, 3);
		Ray ray = new Ray(p, v);

		// Note: adjust "getDirection()" if your method is simply named "direction()"
		assertEquals(1d, ray.direction().length(), DELTA, "ERROR: Ray direction is not normalized");
	}

	/**
	 * Test method for {@link primitives.Ray#getPoint(double)}.
	 */
	@Test
	void testGetPoint() {
		Ray ray = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));

		// ============ Equivalence Partitions Tests ==============

		// EP01: Positive distance (t > 0)
		assertEquals(new Point(2, 0, 0), ray.getPoint(1), "getPoint() failed for positive distance");

		// EP02: Negative distance (t < 0)
		assertEquals(new Point(0, 0, 0), ray.getPoint(-1), "getPoint() failed for negative distance");

		// =============== Boundary Values Tests ==================

		// BV01: Zero distance (t = 0)
		assertEquals(new Point(1, 0, 0), ray.getPoint(0), "getPoint() failed for zero distance");
	}

	@Test
	void testFindClosestPoint() {
		// Ray starting from the origin (0,0,0) and heading towards the positive X-axis
		Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

		// Test points on the X-axis
		Point p1 = new Point(2, 0, 0); // The closest one (distance 2)
		Point p2 = new Point(5, 0, 0); // Distance 5
		Point p3 = new Point(8, 0, 0); // Distance 8

		// ============ Equivalence Partitions Tests ==============
		// TC01: The closest point is in the middle of the list (EP)
		List<Point> list1 = List.of(p2, p1, p3);
		assertEquals(p1, ray.findClosestPoint(list1), "The middle point should be the closest");

		// =============== Boundary Values Tests ==================
		// TC10: The list is empty (BVA)
		assertNull(ray.findClosestPoint(List.of()), "An empty list should return null");

		// TC11: The closest point is the first one in the list (BVA)
		List<Point> list2 = List.of(p1, p2, p3);
		assertEquals(p1, ray.findClosestPoint(list2), "The first point should be the closest");

		// TC12: The closest point is the last one in the list (BVA)
		List<Point> list3 = List.of(p2, p3, p1);
		assertEquals(p1, ray.findClosestPoint(list3), "The last point should be the closest");
	}

}