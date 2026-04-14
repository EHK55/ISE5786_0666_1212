package geometries.impl;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a Triangle in 3D space.
 */
public final class Triangle extends Polygon {

	/**
	 * Constructor using 3 points.
	 * 
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 */
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
	}
	
	@Override
	public List<Point> findIntersections(Ray ray) {
	    // First, find intersection with the plane of the triangle
	    List<Point> intersections = _plane.findIntersections(ray);
	    if (intersections == null) return null;

	    Point p0 = ray.origin();
	    Vector v = ray.direction();

	    // Use the three vertices to check if the point is inside the triangle
	    // Assuming vertices are p1, p2, p3
	    Vector v1 = _vertices.get(0).subtract(p0);
	    Vector v2 = _vertices.get(1).subtract(p0);
	    Vector v3 = _vertices.get(2).subtract(p0);

	    Vector n1 = v1.crossProduct(v2).normalize();
	    Vector n2 = v2.crossProduct(v3).normalize();
	    Vector n3 = v3.crossProduct(v1).normalize();

	    double s1 = primitives.Util.alignZero(v.dotProduct(n1));
	    double s2 = primitives.Util.alignZero(v.dotProduct(n2));
	    double s3 = primitives.Util.alignZero(v.dotProduct(n3));

	    // The point is inside if it's on the same side of all three edges
	    if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
	        return intersections;
	    }

	    return null;
	}
}