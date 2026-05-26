package renderer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import lighting.PointLight;
import primitives.*;

/**
 * Unit tests for PointLight class.
 */
class PointLightTests {

    /**
     * Test method for {@link lighting.PointLight#getL(primitives.Point)}.
     */
    @Test
    void testGetL() {
        Point lightPosition = new Point(0, 0, 10);
        PointLight light = new PointLight(new Color(255, 255, 255), lightPosition);
        Point p = new Point(0, 0, 0);

        // ================== Equivalence Partitions Tests ==================
        // TC01: Test direction vector from light source to a point
        Vector expectedL = p.subtract(lightPosition).normalize();
        assertEquals(expectedL, light.getL(p), 
                "getL() for PointLight should return normalized vector from light to point");
    }

    /**
     * Test method for {@link lighting.PointLight#getIntensity(primitives.Point)}.
     */
    @Test
    void testGetIntensity() {
        Point lightPosition = new Point(0, 0, 0);
        Color baseIntensity = new Color(100, 100, 100);
        
        // Setup light with attenuation factors: kC=1, kL=1, kQ=1
        PointLight light = new PointLight(baseIntensity, lightPosition)
                .setKc(1)
                .setKl(1)
                .setKq(1);
                
        // Point at distance d = 2 from light source
        Point p = new Point(2, 0, 0); 
        
        // Attenuation calculation: 1 / (kC + kL*d + kQ*d^2) = 1 / (1 + 1*2 + 1*4) = 1/7
        Color expectedIntensity = baseIntensity.scale(1.0 / 7.0);

        // ================== Equivalence Partitions Tests ==================
        // TC01: Test light attenuation based on distance formulas
        assertEquals(expectedIntensity.getColor(), light.getIntensity(p).getColor(), 
                "getIntensity() failed to calculate correct distance attenuation for PointLight");
    }
}