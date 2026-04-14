package geometries.api;

import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * Interface for geometries that can be intersected by a ray
 */
public abstract class Intersectable {
    /**
     * Finds all intersections of a given ray with the geometry
     * @param ray the ray to intersect with
     * @return list of intersection points, or null if there are none
     */
    public abstract List<Point> findIntersections(Ray ray);
}