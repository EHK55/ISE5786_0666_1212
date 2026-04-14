package geometries.impl;

import static primitives.Util.isZero;

import java.util.ArrayList;
import java.util.List;

import geometries.api.Geometry;
import primitives.*;

/**
 * Represents a convex polygon in a 3D Cartesian coordinate system.
 * <p>
 * The polygon is defined by an ordered sequence of vertices. All vertices must
 * lie in the same plane and be arranged along the polygon edge path.
 * </p>
 * <p>
 * The polygon must be convex.
 * </p>
 * 
 * @author Dan Zilberstein
 */
public class Polygon extends Geometry {
	/** Ordered list of polygon vertices */
	protected final List<Point> _vertices;
	/** Plane containing the polygon */
	protected final Plane _plane;
	/** Number of vertices */
	private final int _size;

	/**
	 * Constructs a convex polygon from ordered vertices.
	 * <p>
	 * The vertices must:
	 * </p>
	 * <ul>
	 * <li>Contain at least three points</li>
	 * <li>Be ordered along the polygon edge path</li>
	 * <li>Lie in the same plane</li>
	 * <li>Form a convex polygon</li>
	 * </ul>
	 * 
	 * @param vertices polygon vertices in edge order
	 * @throws IllegalArgumentException if the vertices do not form a valid convex
	 *                                  polygon
	 */
	public Polygon(Point... vertices) {
		if (vertices.length < 3)
			throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
		_vertices = List.of(vertices);
		_size = vertices.length;

		// Create the supporting plane using the first three vertices.
		// The plane stores the constant normal of the polygon.
		_plane = new Plane(vertices[0], vertices[1], vertices[2]);
		if (_size == 3)
			return; // no need for more tests for a Triangle

		Vector n = _plane.getNormal(vertices[0]);
		// Subtracting identical vertices would create a zero vector (illegal)
		Vector edge1 = vertices[_size - 1].subtract(vertices[_size - 2]);
		Vector edge2 = vertices[0].subtract(vertices[_size - 1]);

		// Cross product of consecutive edges determines orientation.
		// All edge pairs must produce the same sign relative to the normal,
		// otherwise the polygon is concave or vertices are unordered.
		boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
		for (var i = 1; i < _size; ++i) {
			// Test that the point is in the same plane as calculated originally
			if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
				throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
			// Test the consequent edges have
			edge1 = edge2;
			edge2 = vertices[i].subtract(vertices[i - 1]);
			if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
				throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
		}
	}

	@Override
	public Vector getNormal(Point point) {
		return _plane.getNormal(point);
	}
	
	@Override
	public List<Point> findIntersections(Ray ray) {
	    // Step 1: Find intersection with the plane of the polygon
	    List<Point> planeIntersections = _plane.findIntersections(ray);
	    if (planeIntersections == null) return null;

	    Point p0 = ray.origin();
	    Vector v = ray.direction();

	    // Step 2: Check if the intersection point is inside the polygon
	    int size = _vertices.size();
	    List<Vector> vectors = new ArrayList<>(size);
	    for (Point vertex : _vertices) {
	        vectors.add(vertex.subtract(p0));
	    }

	    List<Vector> normals = new ArrayList<>(size);
	    for (int i = 0; i < size; i++) {
	        Vector v1 = vectors.get(i);
	        Vector v2 = vectors.get((i + 1) % size);
	        normals.add(v1.crossProduct(v2).normalize());
	    }

	    double firstSign = primitives.Util.alignZero(v.dotProduct(normals.get(0)));
	    if (firstSign == 0) return null;

	    for (int i = 1; i < size; i++) {
	        double sign = primitives.Util.alignZero(v.dotProduct(normals.get(i)));
	        if (sign * firstSign <= 0) return null;
	    }

	    return planeIntersections;
	}
}
