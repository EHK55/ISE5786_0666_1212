package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Public interface representing an external light source.
 */
public interface LightSource {
    /**
     * Calculates the light intensity at a given point in space.
     * @param p the illuminated point
     * @return the light intensity at p
     */
    Color getIntensity(Point p);

    /**
     * Calculates the direction of light rays from the light source to a given point.
     * @param p the illuminated point
     * @return the normalized direction vector to p
     */
    Vector getL(Point p);
}