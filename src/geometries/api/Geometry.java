package geometries.api;

import primitives.Point;
import primitives.Vector;

/**
 * Abstract base class for all geometric shapes. It serves as a base for all
 * geometries in the 3D space.
 */
public abstract class Geometry {

	/**
	 * Gets the normal vector to the geometry at a specific point.
	 * 
	 * @param point the point on the geometry surface
	 * @return the normal Vector perpendicular to the surface at the given point
	 */
	public abstract Vector getNormal(Point point);
}