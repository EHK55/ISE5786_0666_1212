package geometries;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import geometries.impl.Sphere;
import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for geometries.impl.Sphere class
 */
class SphereTests {

	/**
	 * Test method for {@link geometries.impl.Sphere#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: Test normal of a sphere
		Sphere sphere = new Sphere(new Point(0, 0, 0), 1d);
		
		// We choose a simple point on the sphere to easily calculate the expected normal
		Point p = new Point(0, 0, 1);
		Vector expectedNormal = new Vector(0, 0, 1);
		
		assertEquals(expectedNormal, sphere.getNormal(p), "ERROR: Sphere getNormal() does not work correctly");
	}
}