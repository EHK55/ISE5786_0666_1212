package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Spot light source representing a directional light source at a specific position with a focused beam.
 */
public class SpotLight extends PointLight {
    /** The direction of the spotlight beam */
    private final Vector _direction;
    /** Narrow beam concentration factor (bonus parameter) */
    private int _narrowBeam = 1;

    /**
     * Constructor for SpotLight.
     * @param intensity the intensity of the light
     * @param position the position of the light source
     * @param direction the direction of the spotlight beam
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this._direction = direction.normalize();
    }

    /**
     * Setter for the narrow beam concentration factor (chained, bonus).
     * @param narrowBeam the narrow beam concentration factor
     * @return this SpotLight instance
     */
    public SpotLight setNarrowBeam(int narrowBeam) {
        this._narrowBeam = narrowBeam;
        return this;
    }

    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double cosTheta = _direction.dotProduct(getL(p));
        if (cosTheta <= 0) {
            return Color.BLACK;
        }
        if (_narrowBeam > 1) {
            cosTheta = Math.pow(cosTheta, _narrowBeam);
        }
        return super.getIntensity(p).scale(cosTheta);
    }
}