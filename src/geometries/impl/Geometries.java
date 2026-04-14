package geometries.impl;

import geometries.api.Intersectable;
import primitives.Point;
import primitives.Ray;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite class representing a collection of intersectable geometries
 */
public class Geometries extends Intersectable {
    private final List<Intersectable> _geometries = new ArrayList<>();

    public Geometries() {}

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds geometries to the collection using a for-each loop
     */
    public void add(Intersectable... geometries) {
        for (Intersectable item : geometries) {
            _geometries.add(item);
        }
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;
        for (Intersectable geo : _geometries) {
            List<Point> intersections = geo.findIntersections(ray);
            if (intersections != null) {
                if (result == null) result = new ArrayList<>();
                result.addAll(intersections);
            }
        }
        return result;
    }
}