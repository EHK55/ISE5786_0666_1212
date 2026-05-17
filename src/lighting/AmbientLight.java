package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Ambient light source representing a constant background illumination.
 */
public class AmbientLight extends Light {

    /**
     * Constructor for AmbientLight.
     * @param ia the original ambient light intensity
     * @param ka the ambient light attenuation factor
     */
    public AmbientLight(Color ia, Double3 ka) {
        super(ia.scale(ka));
    }

    /**
     * Default constructor for AmbientLight (sets intensity to black).
     */
    public AmbientLight() {
        super(Color.BLACK);
    }
}