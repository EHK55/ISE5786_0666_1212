package geometries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;



import org.junit.jupiter.api.Test;

import geometries.impl.Sphere;
import primitives.Point;
import primitives.Ray;
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
	
	/**
     * Test method for {@link geometries.impl.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Sphere sphere = new Sphere(new Point(1, 0, 0), 1d);

        // ============ Equivalence Partitions Tests ==============

        // EP01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 2), new Vector(1, 1, 0))),
                "Ray's line out of sphere");

        // EP02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<Point> result = sphere.findIntersections(new Ray(new Point(-1, 0, 0),
                new Vector(3, 1, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        
        // Ensure the order is correct (by distance from ray origin)
        if (result.get(0).distanceSquared(new Point(-1, 0, 0)) > result.get(1).distanceSquared(new Point(-1, 0, 0))) {
            result = List.of(result.get(1), result.get(0));
        }
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");

        // EP03: Ray starts inside the sphere (1 point)
        assertEquals(1, sphere.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0))).size(),
                "Ray starts inside sphere");

        // EP04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(3, 0, 0), new Vector(1, 0, 0))),
                "Ray starts after sphere");

        // =============== Boundary Values Tests ==================

        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // BV11: Ray starts at sphere and goes inside (1 points)
        assertEquals(1, sphere.findIntersections(new Ray(new Point(1, -1, 0), new Vector(0, 1, 0))).size(),
                "Ray starts at sphere and goes inside");

        // BV12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, -1, 0), new Vector(0, -1, 0))),
                "Ray starts at sphere and goes outside");

        // **** Group 2: Ray's line goes through the center
        // BV21: Ray starts before the sphere (2 points)
        assertEquals(2, sphere.findIntersections(new Ray(new Point(1, -2, 0), new Vector(0, 1, 0))).size(),
                "Ray crosses center, starts before");

        // BV22: Ray starts at sphere and goes inside (1 points)
        assertEquals(1, sphere.findIntersections(new Ray(new Point(1, -1, 0), new Vector(0, 1, 0))).size(),
                "Ray crosses center, starts at sphere");

        // BV23: Ray starts inside (1 points)
        assertEquals(1, sphere.findIntersections(new Ray(new Point(1, -0.5, 0), new Vector(0, 1, 0))).size(),
                "Ray crosses center, starts inside");

        // BV24: Ray starts at the center (1 points)
        assertEquals(1, sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 1, 0))).size(),
                "Ray starts at the center");

        // BV25: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, 1, 0), new Vector(0, 1, 0))),
                "Ray crosses center, starts at sphere going out");

        // BV26: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, 2, 0), new Vector(0, 1, 0))),
                "Ray crosses center, starts after sphere");
    }
}