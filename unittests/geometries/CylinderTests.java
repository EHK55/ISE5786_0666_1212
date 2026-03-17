package geometries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import geometries.impl.Cylinder;
import primitives.*;

/**
 * Unit tests for geometries.impl.Cylinder class
 */
class CylinderTests {

	/**
	 * Test method for {@link geometries.impl.Cylinder#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		Cylinder cylinder = new Cylinder(1d, axis, 2d);

		// ============ Equivalence Partitions Tests ==============
		// TC01: Test normal on the lateral surface
		assertEquals(new Vector(1, 0, 0), cylinder.getNormal(new Point(1, 0, 1)), 
				"ERROR: Cylinder lateral surface getNormal() wrong result");

		// TC02: Test normal on the bottom base
		assertEquals(new Vector(0, 0, -1), cylinder.getNormal(new Point(0.5, 0, 0)), 
				"ERROR: Cylinder bottom base getNormal() wrong result");

		// TC03: Test normal on the top base
		assertEquals(new Vector(0, 0, 1), cylinder.getNormal(new Point(0.5, 0, 2)), 
				"ERROR: Cylinder top base getNormal() wrong result");

		// =============== Boundary Values Tests ==================
		// TC11: Test normal at the center of the bottom base
		assertEquals(new Vector(0, 0, -1), cylinder.getNormal(new Point(0, 0, 0)), 
				"ERROR: Cylinder bottom center getNormal() wrong result");

		// TC12: Test normal at the center of the top base
		assertEquals(new Vector(0, 0, 1), cylinder.getNormal(new Point(0, 0, 2)), 
				"ERROR: Cylinder top center getNormal() wrong result");
	}
}