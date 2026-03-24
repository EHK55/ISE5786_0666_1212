package geometries.impl;

import geometries.api.Geometry;
import primitives.Point;
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
}