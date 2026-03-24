package geometries.impl;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a Sphere in 3D space.
 */
public final class Sphere extends RadialGeometry {

	/** The center point of the sphere */
	private final Point _center;

	/**
	 * Constructor for Sphere.
	 * 
	 * @param center the center point
	 * @param radius the radius
	 */
	public Sphere(Point center, double radius) {
		super(radius);
		this._center = center;
	}

	@Override
	public Vector getNormal(Point point) {
		// Normal of a sphere is the vector from the center to the point, normalized
		return point.subtract(this._center).normalize(); 
	}
}