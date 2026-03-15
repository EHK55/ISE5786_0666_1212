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
	 * Constructor using 3 points on the plane. Initializes the normal to null for
	 * Stage 1.
	 */
	public Plane(Point p1, Point p2, Point p3) {
		this._point = p1;
		this._normal = null; // Modification exigée par le nouveau PDF
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