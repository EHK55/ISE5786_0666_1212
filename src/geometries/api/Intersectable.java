package geometries.api;

import java.util.List;
import primitives.*;

/**
 * Intersectable class representing shapes that can intersect with rays.
 */
public abstract class Intersectable {

    /**
     * Default constructor for Intersectable.
     */
    protected Intersectable() {}

    /**
     * Intersection helper class to store intersection points alongside their geometry.
     */
    public static final class Intersection {
        /** The geometry of the intersection */
        public final Geometry geometry;
        /** The point of intersection */
        public final Point point;
        /** The material of the intersected geometry */
        public final Material material;

        // Cache fields for optimization (Stage 7)
        /** Normal vector at the intersection point */
        public Vector normal;
        /** Ray direction vector */
        public Vector v;
        /** Dot product of ray direction and normal */
        public double vn;
        /** The active light source */
        public lighting.LightSource light;
        /** Normalized direction from light source to intersection point */
        public Vector l;
        /** Dot product of light direction and normal */
        public double ln;

        /**
         * Constructor for Intersection.
         * @param geometry the geometry of the intersection
         * @param point the point of intersection
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry == null ? new Material() : geometry.getMaterial();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Intersection other = (Intersection) obj;
            return this.geometry == other.geometry && this.point.equals(other.point);
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + (geometry == null ? "null" : geometry.getClass().getSimpleName()) +
                    ", point=" + point +
                    '}';
        }
    }

    /**
     * Finds all intersections of a ray with this shape.
     * @param ray the ray
     * @return list of intersection points
     */
    public final List<Point> findIntersections(Ray ray) {
        var intersections = calcIntersections(ray);
        return intersections == null ? null : intersections.stream().map(i -> i.point).toList();
    }

    /**
     * Calculates all intersection objects with this shape (NVI Pattern).
     * @param ray the ray
     * @return list of intersections
     */
    public final List<Intersection> calcIntersections(Ray ray) {
        return calcIntersectionsHelper(ray);
    }

    /**
     * Helper method for calculating intersections.
     * @param ray the ray
     * @return list of intersections
     */
    protected abstract List<Intersection> calcIntersectionsHelper(Ray ray);
}