package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Point light source representing an omnidirectional light source at a specific position.
 */
public class PointLight extends Light implements LightSource {
    /** The position of the light source */
    protected final Point _position;
    /** Constant attenuation factor kC */
    private double _kC = 1.0;
    /** Linear attenuation factor kL */
    private double _kL = 0.0;
    /** Quadratic attenuation factor kQ */
    private double _kQ = 0.0;

    /**
     * Constructor for PointLight.
     * @param intensity the intensity of the light
     * @param position the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this._position = position;
    }

    /**
     * Setter for the constant attenuation factor (chained).
     * @param kC the constant attenuation factor
     * @return this PointLight instance
     */
    public PointLight setKc(double kC) {
        this._kC = kC;
        return this;
    }

    /**
     * Setter for the linear attenuation factor (chained).
     * @param kL the linear attenuation factor
     * @return this PointLight instance
     */
    public PointLight setKl(double kL) {
        this._kL = kL;
        return this;
    }

    /**
     * Setter for the quadratic attenuation factor (chained).
     * @param kQ the quadratic attenuation factor
     * @return this PointLight instance
     */
    public PointLight setKq(double kQ) {
        this._kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = _position.distance(p);
        // We multiply by 1.0 divided by the attenuation factor to support double values smoothly
        return _intensity.scale(1.0 / (_kC + _kL * d + _kQ * d * d));
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(_position).normalize();
    }
}