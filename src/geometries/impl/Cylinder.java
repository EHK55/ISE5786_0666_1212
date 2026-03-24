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
		Point p0 = _axis.origin();
		Vector v = _axis.direction();

		// Check if the point is on the bottom base (t = 0)
		if (point.equals(p0))
			return v.scale(-1);

		double t = v.dotProduct(point.subtract(p0));

		// If t is 0, the point is on the bottom base
		if (t == 0)
			return v.scale(-1);

		// If t equals height, the point is on the top base
		if (t == _height)
			return v;

		// Otherwise, it's on the lateral surface (same logic as Tube)
		Point o = p0.add(v.scale(t));
		return point.subtract(o).normalize();
	}
}