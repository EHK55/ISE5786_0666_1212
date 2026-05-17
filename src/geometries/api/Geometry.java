package geometries.api;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing any geometric shape in 3D space.
 * Serves as a base for all geometries with material and emission properties.
 */
public abstract class Geometry extends Intersectable {
    /** Emission color of the geometry */
    private Color _emission = Color.BLACK;
    /** Material properties of the geometry's surface */
    private Material _material = new Material();

    /**
     * Default constructor for Geometry.
     */
    protected Geometry() {}

    /**
     * Getter for emission color.
     * @return the emission color
     */
    public Color getEmission() {
        return this._emission;
    }

    /**
     * Setter for emission color (chained).
     * @param emission the emission color
     * @return this Geometry instance
     */
    public Geometry setEmission(Color emission) {
        this._emission = emission;
        return this;
    }

    /**
     * Getter for material properties.
     * @return the material properties
     */
    public Material getMaterial() {
        return this._material;
    }

    /**
     * Setter for material properties (chained).
     * @param material the material properties
     * @return this Geometry instance
     */
    public Geometry setMaterial(Material material) {
        this._material = material;
        return this;
    }

    /**
     * Calculates the normal vector to the surface of the geometry at a given point.
     * @param p the point on the surface
     * @return the normalized normal vector
     */
    public abstract Vector getNormal(Point p);
}