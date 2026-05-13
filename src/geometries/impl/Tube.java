package geometries.impl;

import java.util.List;

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
		// Finding the projection of the point on the axis ray
		// P0 is the head of the axis ray
		Point p0 = _axis.origin();
		Vector v = _axis.direction();

		// t = v * (P - P0)
		double t = v.dotProduct(point.subtract(p0));

		// If t is 0, the projection is exactly p0
		Point o = p0;
		if (t != 0) {
			o = p0.add(v.scale(t));
		}

		return point.subtract(o).normalize();
	}

	@Override
	protected List<Intersection> calcIntersectionsHelper(Ray ray) {

		return null;

	}
}