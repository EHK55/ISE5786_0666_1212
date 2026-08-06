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
		buildBox();
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

	@Override
	public void buildBox() {
		// 1. Trouver le centre du bas (p0)
		Point p0 = _axis.origin();

		// 2. Trouver le centre du haut (p1) = p0 + (direction * hauteur)
		Vector v = _axis.direction();
		Point p1 = p0.add(v.scale(_height));

		// 3. Créer deux "fausses" boîtes de sphères pour les deux extrémités
		primitives.BoundingBox b1 = new primitives.BoundingBox(p0, _radius);
		primitives.BoundingBox b2 = new primitives.BoundingBox(p1, _radius);

		// 4. Fusionner les deux boîtes en prenant les extrêmes
		box = new primitives.BoundingBox(Math.min(b1.minX, b2.minX), Math.max(b1.maxX, b2.maxX),
				Math.min(b1.minY, b2.minY), Math.max(b1.maxY, b2.maxY), Math.min(b1.minZ, b2.minZ),
				Math.max(b1.maxZ, b2.maxZ));
	}
}