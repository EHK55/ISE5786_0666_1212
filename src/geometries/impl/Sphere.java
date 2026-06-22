package geometries.impl;

import java.util.List;

import primitives.Point;
import primitives.Ray;
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

	@Override
	protected List<Intersection> calcIntersectionsHelper(Ray ray) {

		Point p0 = ray.origin();
		Vector v = ray.direction();
		Vector u;

		try {
			u = _center.subtract(p0);
		} catch (IllegalArgumentException ignore) {
			return List.of(new Intersection(this, ray.getPoint(_radius))); // Ray starts at center
		}

		double tm = v.dotProduct(u);
		double dSquared = u.lengthSquared() - tm * tm;
		double thSquared = _radius * _radius - dSquared;

		// No intersections
		if (primitives.Util.alignZero(thSquared) <= 0)
			return null;

		double th = Math.sqrt(thSquared);
		double t1 = primitives.Util.alignZero(tm - th);
		double t2 = primitives.Util.alignZero(tm + th);

		// Only return points in the direction of the ray (t > 0)
		if (t1 > 0 && t2 > 0) {
			return List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));
		}
		if (t1 > 0)
			return List.of(new Intersection(this, ray.getPoint(t1)));
		if (t2 > 0)
			return List.of(new Intersection(this, ray.getPoint(t2)));

		return null;

	}
	
	@Override
	protected void buildBox() {
		// The bounding box is created legally using the new constructor in primitives
		box = new primitives.BoundingBox(_center, _radius);
	}
}