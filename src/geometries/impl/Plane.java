package geometries.impl;

import geometries.api.Geometry;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a mathematical plane in 3D space.
 */
public final class Plane extends Geometry {

	/** A point on the plane */
	private final Point q;

	/** The normal vector to the plane */
	private final Vector normal;

	/**
	 * Constructor using 3 points on the plane.
	 * 
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 */
	public Plane(Point p1, Point p2, Point p3) {
		this.q = p1;
		Vector v1 = p2.subtract(p1);
		Vector v2 = p3.subtract(p1);
		this.normal = v1.crossProduct(v2).normalize();
	}

	/**
	 * Constructor using a point and a normal vector.
	 * 
	 * @param point  a point on the plane
	 * @param normal the normal vector
	 */
	public Plane(Point point, Vector normal) {
		this.q = point;
		this.normal = normal.normalize();
	}

	/**
	 * Gets the normal of the plane.
	 * 
	 * @return the normal Vector
	 */
	public Vector getNormal() {
		return normal;
	}

	@Override
	public Vector getNormal(Point point) {
		return normal;
	}
}