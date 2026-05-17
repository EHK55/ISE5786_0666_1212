package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Directional light source representing a light source at infinity (like the Sun).
 */
public class DirectionalLight extends Light implements LightSource {
    /** The direction of the light rays */
    private final Vector _direction;

    /**
     * Constructor for DirectionalLight.
     * @param intensity the intensity of the light
     * @param direction the direction of the light rays
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this._direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return _intensity;
    }

    @Override
    public Vector getL(Point p) {
        return _direction;
    }
}