package geometries.impl;

import geometries.api.Geometry;

/**
 * Abstract base class for geometries with a radius.
 */
public abstract class RadialGeometry extends Geometry {

	/** The radius of the geometry */
	protected final double _radius;

	/** The squared radius for optimization purposes */
	protected final double _radiusSquared;

	/**
	 * Constructor for RadialGeometry.
	 * 
	 * @param radius the radius
	 */
	public RadialGeometry(double radius) {
		this._radius = radius;
		this._radiusSquared = radius * radius;
	}
}