package geometries.api;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * Abstract base class for all geometric shapes. It serves as a base for all
 * geometries in the 3D space.
 */
public abstract class Geometry extends Intersectable {

	private Color _emission = Color.BLACK;
	private Material _material = new Material();

	public Geometry setEmission(Color color) {
		this._emission = color;
		return this;
	}

	public Color getEmission() {
		return this._emission;
	}

	/**
	 * Getter for the material of the geometry * @return the material
	 */
	public Material getMaterial() {
		return this._material;
	}

	/**
	 * Setter for the material (Builder pattern chaining) * @param material the new
	 * material to set
	 * 
	 * @return the Geometry object itself
	 */
	public Geometry setMaterial(Material material) {
		this._material = material;
		return this;
	}

	/**
	 * Gets the normal vector to the geometry at a specific point.
	 * 
	 * @param point the point on the geometry surface
	 * @return the normal Vector perpendicular to the surface at the given point
	 */
	public abstract Vector getNormal(Point point);
}