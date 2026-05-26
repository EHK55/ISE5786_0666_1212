package renderer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import lighting.SpotLight;
import primitives.*;

/**
 * Unit tests for SpotLight class.
 */
class SpotLightTests {

    /**
     * Test method for {@link lighting.SpotLight#getL(primitives.Point)}.
     */
    @Test
    void testGetL() {
        Point lightPosition = new Point(0, 0, 10);
        Vector direction = new Vector(0, 0, -1);
        SpotLight light = new SpotLight(new Color(255, 255, 255), lightPosition, direction);
        Point p = new Point(0, 0, 0);

        // ================== Equivalence Partitions Tests ==================
        // TC01: Test direction vector from spotlight source to a point
        Vector expectedL = p.subtract(lightPosition).normalize();
        assertEquals(expectedL, light.getL(p), 
                "getL() for SpotLight should behave identically to PointLight direction calculation");
    }

    /**
     * Test method for {@link lighting.SpotLight#getIntensity(primitives.Point)}.
     */
    @Test
    void testGetIntensity() {
        Point lightPosition = new Point(0, 0, 0);
        Vector direction = new Vector(0, 0, -1); // Pointing downwards along Z-axis
        Color baseIntensity = new Color(100, 100, 100);
        
        // Setup spotlight with specific attenuation factors
        SpotLight light = new SpotLight(baseIntensity, lightPosition, direction)
                .setKc(1)
                .setKl(0)
                .setKq(0); // Only constant attenuation for simplicity of testing angle

        // Point directly in the center of the beam at distance d = 1 (vector from light to p is 0,0,-1)
        Point pCenter = new Point(0, 0, -1);
        
        // Inside beam center: dot product of direction and L is 1 (Max concentration)
        Color expectedCenterIntensity = baseIntensity.scale(1.0);

        // ================== Equivalence Partitions Tests ==================
        // TC01: Test intensity at the absolute center of the spotlight beam axis
        assertEquals(expectedCenterIntensity.getColor(), light.getIntensity(pCenter).getColor(), 
                "getIntensity() should return maximum intensity along the beam axis for SpotLight");
    }
}