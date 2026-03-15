package geometries.impl;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a Cylinder in 3D space (a tube with finite height).
 */
public final class Cylinder extends Tube {

	/** The height of the cylinder */
	private final double _height;

	/**
	 * Constructor for Cylinder.
	 * 
	 * @param radius the radius
	 * @param axis   the central axis
	 * @param height the height
	 */
	public Cylinder(double radius, Ray axis, double height) {
		super(radius, axis);
		this._height = height;
	}

	@Override
	public Vector getNormal(Point point) {
		return null;
	}
}