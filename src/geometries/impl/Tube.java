package geometries.impl;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a Tube in 3D space.
 */
public class Tube extends RadialGeometry {

	/** The central axis ray of the tube */
	protected final Ray _axis;

	/**
	 * Constructor for Tube.
	 * 
	 * @param radius the radius
	 * @param axis   the central axis
	 */
	public Tube(double radius, Ray axis) {
		super(radius);
		this._axis = axis;
	}

	@Override
	public Vector getNormal(Point point) {
		return null;
	}
}