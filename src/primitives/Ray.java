package primitives;

import java.util.List;

import geometries.api.Intersectable.Intersection;

/**
 * Represents a ray (half-line) in 3D space. Defined by an origin point and a
 * normalized direction vector.
 */
public class Ray {

	/** The origin point of the ray */
	private final Point _origin;

	/** The normalized direction vector of the ray */
	private final Vector _direction;

	/**
	 * Constructor for Ray. Automatically normalizes the direction vector before
	 * saving it.
	 * 
	 * @param origin    the origin point
	 * @param direction the direction vector
	 */
	public Ray(Point origin, Vector direction) {
		this._origin = origin;
		this._direction = direction.normalize();
	}

	/**
	 * Returns the origin point of the ray.
	 * 
	 * @return the origin
	 */
	public Point origin() {
		return _origin;
	}

	/**
	 * Returns the direction vector of the ray.
	 * 
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
	 * Calculates a point on the ray at a distance t from the origin
	 * 
	 * @param t distance from the ray origin (can be positive, negative or zero)
	 * @return the calculated point: P = P0 + t * v
	 */
	public Point getPoint(double t) {
		try {
			// P = P0 + t * v
			return _origin.add(_direction.scale(t));
		} catch (Exception e) {
			// In case t is so small that scaling results in Vector(0,0,0)
			return _origin;
		}
	}

	/**
	 * Finds the closest point to the ray's head from a list of points.
	 * 
	 * @param points a list of points
	 * @return the closest point, or null if the list is empty/null
	 */

//	public Point findClosestPoint(List<Point> points) {
//		if (points == null) {
//			return null; // Case of an empty or null list
//		}
//
//		Point closestPoint = null;
//		double minDistance = Double.POSITIVE_INFINITY; // Initialization to infinity
//
//		for (Point point : points) {
//			// Using squared distance to optimize performance
//			double distance = point.distanceSquared(this._origin);
//
//			if (distance < minDistance) {
//				minDistance = distance;
//				closestPoint = point;
//			}
//		}
//
//		return closestPoint;
//	}

	/**
	 * Finds the closest Intersection to the ray's origin from a list of
	 * intersections. * @param intersections a list of intersections
	 * 
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
	 * Finds the closest point to the ray's origin from a list of points. Kept for
	 * backward compatibility. * @param points a list of points
	 * 
	 * @return the closest point, or null if the list is empty/null
	 */
	public Point findClosestPoint(List<Point> points) {
		return points == null || points.isEmpty() ? null
				: findClosestIntersection(points.stream().map(point -> new Intersection(null, point)).toList()).point;
	}
}