package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing any light source in the scene.
 */
public interface LightSource {
    /**
     * Calculates the intensity of the light at a given point.
     * @param p the point in space
     * @return the color intensity
     */
    public Color getIntensity(Point p);

    /**
     * Calculates the direction vector from the light source to a given point.
     * @param p the point in space
     * @return the normalized direction vector
     */
    public Vector getL(Point p);

    /**
     * Calculates the distance from the light source to a given point.
     * @param point the point in space
     * @return the distance between the light source and the point
     */
    public double getDistance(Point point);
}