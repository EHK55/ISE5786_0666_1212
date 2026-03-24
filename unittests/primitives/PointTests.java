package primitives;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for primitives.Point class
 */
class PointTests {

	/**
	 * Delta value for accuracy when comparing double values.
	 */
	private static final double DELTA = 0.000001;

	/**
	 * Test method for {@link primitives.Point#add(primitives.Vector)}.
	 */
	@Test
	void testAdd() {
		Point p1 = new Point(1, 2, 3);
		Vector v1 = new Vector(-1, -2, -3);

		// ============ Equivalence Partitions Tests ==============
		// TC01: Test adding a vector to a point
		assertEquals(new Point(0, 0, 0), p1.add(v1), "ERROR: Point + Vector does not work correctly");
	}

	/**
	 * Test method for {@link primitives.Point#subtract(primitives.Point)}.
	 */
	@Test
	void testSubtract() {
		Point p1 = new Point(1, 2, 3);
		Point p2 = new Point(2, 4, 6);

		// ============ Equivalence Partitions Tests ==============
		// TC01: Test subtracting a point from another point
		assertEquals(new Vector(1, 2, 3), p2.subtract(p1), "ERROR: Point - Point does not work correctly");

		// =============== Boundary Values Tests ==================
		// TC11: Test subtracting a point from itself
		assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
				"ERROR: Point - itself does not throw an exception");
	}

	/**
	 * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
	 */
	@Test
	void testDistanceSquared() {
		Point p1 = new Point(1, 2, 3);
		Point p2 = new Point(1, 2, 5);

		// ============ Equivalence Partitions Tests ==============
		// TC01: Test squared distance between two points
		assertEquals(4, p1.distanceSquared(p2), DELTA, "ERROR: distanceSquared() wrong value");

		// =============== Boundary Values Tests ==================
		// TC11: Test squared distance from a point to itself
		assertEquals(0, p1.distanceSquared(p1), DELTA, "ERROR: point squared distance to itself is not zero");
	}

	/**
	 * Test method for {@link primitives.Point#distance(primitives.Point)}.
	 */
	@Test
	void testDistance() {
		Point p1 = new Point(1, 2, 3);
		Point p2 = new Point(1, 2, 5);

		// ============ Equivalence Partitions Tests ==============
		// TC01: Test distance between two points
		assertEquals(2, p1.distance(p2), DELTA, "ERROR: distance() wrong value");

		// =============== Boundary Values Tests ==================
		// TC11: Test distance from a point to itself
		assertEquals(0, p1.distance(p1), DELTA, "ERROR: point distance to itself is not zero");
	}
}