package renderer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import lighting.DirectionalLight;
import primitives.*;

/**
 * Unit tests for DirectionalLight class.
 */
class DirectionalLightTests {

    /**
     * Test method for {@link lighting.DirectionalLight#getL(primitives.Point)}.
     */
    @Test
    void testGetL() {
        Vector direction = new Vector(1, 1, 1);
        DirectionalLight light = new DirectionalLight(new Color(255, 255, 255), direction);
        Point p = new Point(0, 0, 0);

        // ================== Equivalence Partitions Tests ==================
        // TC01: Test that getL returns the normalized direction vector of the light source
        assertEquals(direction.normalize(), light.getL(p), 
                "getL() for DirectionalLight should return the normalized direction vector");
    }

    /**
     * Test method for {@link lighting.DirectionalLight#getIntensity(primitives.Point)}.
     */
    @Test
    void testGetIntensity() {
        Color intensity = new Color(200, 200, 200);
        DirectionalLight light = new DirectionalLight(intensity, new Vector(0, 0, -1));
        Point p = new Point(5, 5, 5);

        // ================== Equivalence Partitions Tests ==================
        // TC01: Test that directional light intensity is constant everywhere
        assertEquals(intensity.getColor(), light.getIntensity(p).getColor(), 
                "getIntensity() for DirectionalLight should be constant at any point");
    }
}