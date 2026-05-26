package primitives;

import java.util.List;
import geometries.api.Intersectable.Intersection;

/**
 * Represents a ray (half-line) in 3D space. Defined by an origin point and a normalized direction vector.
 */
public class Ray {

	/** The origin point of the ray */
	private final Point _origin;

	/** The normalized direction vector of the ray */
	private final Vector _direction;

	/** Constant for ray head shifting to avoid self intersection and self shading */
	private static final double DELTA = 0.1;

	/**
	 * Constructor for Ray. Automatically normalizes the direction vector before saving it.
	 * @param origin    the origin point
	 * @param direction the direction vector
	 */
	public Ray(Point origin, Vector direction) {
		this._origin = origin;
		this._direction = direction.normalize();
	}

	/**
	 * Constructor for Ray that automatically offsets the origin point along the
	 * normal vector to avoid self shading and self intersection.
	 * @param origin    the original interaction point
	 * @param direction the direction of the new secondary ray
	 * @param normal    the surface normal vector at the origin point
	 */
	public Ray(Point origin, Vector direction, Vector normal) {
		this._direction = direction.normalize();
		double nv = normal.dotProduct(this._direction);
		
		if (Util.isZero(nv)) {
			this._origin = origin;
		} else {
			Vector deltaVector = normal.scale(nv > 0 ? DELTA : -DELTA);
			this._origin = origin.add(deltaVector);
		}
	}

	/**
	 * Returns the origin point of the ray.
	 * @return the origin
	 */
	public Point origin() {
		return _origin;
	}

	/**
	 * Returns the direction vector of the ray.
	 * @return the direction
	 */
	public Vector direction() {
		return _direction;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Ray other = (Ray) obj;
		return _origin.equals(other._origin) && _direction.equals(other._direction);
	}

	@Override
	public String toString() {
		return "Ray [origin=" + _origin + ", direction=" + _direction + "]";
	}

	/**
	 * Calculates a point on the ray at a distance t from the origin.
	 * @param t distance from the ray origin
	 * @return the calculated point
	 */
	public Point getPoint(double t) {
		try {
			return _origin.add(_direction.scale(t));
		} catch (Exception e) {
			return _origin;
		}
	}

	/**
	 * Finds the closest Intersection to the ray's origin from a list of intersections.
	 * @param intersections a list of intersections
	 * @return the closest Intersection, or null if the list is empty/null
	 */
	public Intersection findClosestIntersection(List<Intersection> intersections) {
		if (intersections == null || intersections.isEmpty()) {
			return null;
		}

		Intersection closestIntersection = null;
		double minDistance = Double.POSITIVE_INFINITY;

		for (Intersection intersection : intersections) {
			double distance = intersection.point.distanceSquared(this._origin);

			if (distance < minDistance) {
				minDistance = distance;
				closestIntersection = intersection;
			}
		}

		return closestIntersection;
	}

	/**
	 * Finds the closest point to the ray's origin from a list of points. Kept for backward compatibility.
	 * @param points a list of points
	 * @return the closest point, or null if the list is empty/null
	 */
	public Point findClosestPoint(List<Point> points) {
		return points == null || points.isEmpty() ? null
				: findClosestIntersection(points.stream().map(point -> new Intersection(null, point)).toList()).point;
	}
}