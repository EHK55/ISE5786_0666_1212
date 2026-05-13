package geometries.api;

import java.util.List;

import primitives.Material;
import primitives.Point;
import primitives.Ray;

/**
 * Interface for geometries that can be intersected by a ray
 */
public abstract class Intersectable {
	/**
	 * Finds all intersections of a given ray with the geometry
	 * 
	 * @param ray the ray to intersect with
	 * @return list of intersection points, or null if there are none
	 */

	protected abstract List<Intersection> calcIntersectionsHelper(Ray ray);

	public final List<Intersection> calcIntersections(Ray ray) {
		return calcIntersectionsHelper(ray);
	}

	public final List<Point> findIntersections(Ray ray) {
		var intersections = calcIntersections(ray);
		return intersections == null ? null : intersections.stream().map(intersection -> intersection.point).toList();
	}

	public static final class Intersection {
		public final Geometry geometry;
		public final Point point;

		public final Material material;

		public Intersection(Geometry g, Point p) {
			this.geometry = g;
			this.point = p;
			// If geometry is null, assign a default Material. Otherwise, get it from the
			// geometry.
			this.material = geometry == null ? new Material() : geometry.getMaterial();
		}

		public String toString() {
			return "geometry: " + this.geometry + "\n point: " + this.point;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj instanceof Intersection other) {
				return this.geometry == other.geometry && this.point.equals(other.point);
			}
			return false;
		}

	}
}