package lighting;

import primitives.Color;

/**
 * Abstract base class for all light types.
 */
abstract class Light {
    /** The intensity of the light source */
    protected final Color _intensity;

    /**
     * Constructor for Light.
     * @param intensity the intensity of the light
     */
    protected Light(Color intensity) {
        this._intensity = intensity;
    }

    /**
     * Getter for the light intensity.
     * @return the light intensity
     */
    public Color getIntensity() {
        return _intensity;
    }
}