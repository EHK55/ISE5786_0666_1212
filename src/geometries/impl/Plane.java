package geometries.impl;

import java.util.List;

import geometries.api.Geometry;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a mathematical plane in 3D space.
 */
public final class Plane extends Geometry {

	private final Point _point;
	private final Vector _normal;

	/**
	 * Constructor using 3 points on the plane.
	 */
	public Plane(Point p1, Point p2, Point p3) {
		this._point = p1;

		Vector u = p2.subtract(p1);
		Vector v = p3.subtract(p1);

		this._normal = u.crossProduct(v).normalize();
	}

	/**
	 * Constructor using a point and a normal vector.
	 */
	public Plane(Point point, Vector normal) {
		this._point = point;
		this._normal = normal.normalize();
	}

	public Vector getNormal() {
		return _normal;
	}

	@Override
	public Vector getNormal(Point point) {
		return _normal;
	}

	@Override
	protected List<Intersection> calcIntersectionsHelper(Ray ray) {
		Point p0 = ray.origin();
		Vector v = ray.direction();
		Vector n = _normal;

		double nv = n.dotProduct(v);

		// Ray is parallel to the plane
		if (primitives.Util.isZero(nv)) {
			return null;
		}

		try {
			// Use the point on the plane
			Vector p0Q = _point.subtract(p0);

			double t = primitives.Util.alignZero(n.dotProduct(p0Q) / nv);

			// Return intersection only if it's in the ray's direction (t > 0)
			return t > 0 ? List.of(new Intersection(this, ray.getPoint(t))) : null;
		} catch (IllegalArgumentException ignore) {
			// Ray starts on the plane
			return null;
		}
		
		
	}
	
	@Override
	public void buildBox() {
		// Infinite shapes cannot be bounded by a box
		box = null;
	}

}