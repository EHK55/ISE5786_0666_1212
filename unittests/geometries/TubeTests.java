package geometries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import geometries.impl.Tube;
import primitives.*;

/**
 * Unit tests for geometries.impl.Tube class
 */
class TubeTests {

	/**
	 * Test method for {@link geometries.impl.Tube#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: Test normal of a tube
		Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		Tube tube = new Tube(1d, axis);
		
		// The normal at point (1, 0, 5) should be (1, 0, 0)
		Vector result = tube.getNormal(new Point(1, 0, 5));
		assertEquals(new Vector(1, 0, 0), result, "ERROR: Tube getNormal() wrong result");
	}
}