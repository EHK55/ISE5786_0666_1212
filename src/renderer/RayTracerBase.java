package renderer;

import scene.Scene;
import primitives.Color;
import primitives.Ray;
import geometries.api.Intersectable.Intersection;
import primitives.Vector;
import lighting.LightSource;
import static primitives.Util.alignZero;

/**
 * Abstract base class for ray tracers.
 */
public abstract class RayTracerBase {
    /** The scene to trace rays through */
    protected final Scene _scene;

    /**
     * Constructor for RayTracerBase.
     * @param scene the scene
     */
    protected RayTracerBase(Scene scene) {
        this._scene = scene;
    }

    /**
     * Traces a ray and calculates the color of the closest intersection point.
     * @param ray the ray to trace
     * @return the color of the closest intersection point
     */
    public abstract Color traceRay(Ray ray);

    /**
     * Precalculates intersection fields that are independent of light sources.
     * @param intersection the intersection point to process
     * @param v the ray direction vector
     * @return true if the processed fields are valid for shading, false otherwise
     */
    protected boolean preprocessIntersection(Intersection intersection, Vector v) {
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.v = v;
        intersection.vn = alignZero(v.dotProduct(intersection.normal));
        return intersection.vn != 0;
    }

    /**
     * Precalculates intersection fields that are dependent on a specific light source.
     * @param intersection the intersection point to process
     * @param light the light source
     * @return true if the light source contributes to shading on the point's side, false otherwise
     */
    protected boolean preprocessLightSource(Intersection intersection, LightSource light) {
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.ln = alignZero(intersection.l.dotProduct(intersection.normal));
        return (intersection.ln * intersection.vn) > 0;
    }
}