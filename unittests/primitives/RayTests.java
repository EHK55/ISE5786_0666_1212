package primitives;

import static org.junit.jupiter.api.Assertions.*;

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
	 * Test method for {@link primitives.Ray#Ray(primitives.Point, primitives.Vector)}.
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
}