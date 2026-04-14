package geometries;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import geometries.api.Intersectable;
import geometries.impl.*;
import geometries.api.*;
import primitives.*;
import java.util.List;

/**
 * Testing Geometries (Composite)
 */
public class GeometriesTests {

	@Test
	void testFindIntersections() {
	    // 1. Sphere: Center (0,0,3), Radius 1 (Z range: 2 to 4)
	    Sphere sphere = new Sphere(new Point(0, 0, 3), 1d);
	    
	    // 2. Plane: Infinite plane at Z=5
	    Plane plane = new Plane(new Point(0, 0, 5), new Vector(0, 0, 1));
	    
	    // 3. Triangle: Small triangle at Z=1, away from the point (1.5, 0)
	    Triangle triangle = new Triangle(
	            new Point(-1, -1, 1), 
	            new Point(1, -1, 1), 
	            new Point(0, 1, 1));
	    
	    Geometries geometries = new Geometries(sphere, plane, triangle);

	    // EP01: Some geometries (but not all) are intersected [: 315]
	    // Ray at (1.5, 0) misses the triangle (Z=1) but hits Sphere and Plane
	    // Note: Sphere at Z=3 with radius 1 is still hit because we use a vertical ray
	    List<Point> result = geometries.findIntersections(new Ray(new Point(0.5, 0, -1), new Vector(0, 0, 1)));
	    assertNotNull(result, "Some geometries should be intersected");
	    assertEquals(3, result.size(), "Wrong number of intersection points");

	    // BV01: Empty collection [: 316]
	    assertNull(new Geometries().findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 0, 0))),
	            "Empty geometries collection should return null");

	    // BV02: No geometry is intersected [: 316]
	    assertNull(geometries.findIntersections(new Ray(new Point(10, 10, 10), new Vector(1, 1, 1))),
	            "No geometry should be intersected");

	    // BV03: Only one geometry (Plane) [: 316]
	    assertEquals(1, geometries.findIntersections(new Ray(new Point(10, 10, 0), new Vector(0, 0, 1))).size(),
	            "Only one geometry should be intersected");

	    // BV04: All geometries (4 points: Triangle=1, Sphere=2, Plane=1) [: 316]
	    assertEquals(4, geometries.findIntersections(new Ray(new Point(0, 0, -1), new Vector(0, 0, 1))).size(),
	            "All geometries should be intersected");
	}
}