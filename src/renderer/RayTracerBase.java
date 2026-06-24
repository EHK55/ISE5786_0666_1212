package renderer;

import scene.Scene;
import primitives.Color;
import primitives.Ray;
import geometries.api.Intersectable;
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

 // Add these fields and methods to RayTracerBase

    /** Flag indicating if CBR (Conservative Bounding Region) optimization is active */
    protected boolean isCbrActive = false;
    
    /** Flag indicating if BVH (Bounding Volume Hierarchy) tree construction is active */
    protected boolean isBvhActive = false;

    /**
     * Activates or deactivates the CBR mechanism for this render session.
     * Synchronizes the global static flag in Intersectable so geometries know to use early rejection.
     * * @param active true to activate CBR, false to deactivate
     * @return this RayTracerBase instance for method chaining
     */
    public RayTracerBase setCbrActive(boolean active) {
        this.isCbrActive = active;
        geometries.api.Intersectable.setCbrActive(active); 
        return this;
    }

    /**
     * Activates or deactivates the BVH tree construction for this render session.
     * * @param active true to build the tree, false to keep the geometries flat
     * @return this RayTracerBase instance for method chaining
     */
    public RayTracerBase setBvhActive(boolean active) {
        this.isBvhActive = active;
        return this;
    }

    /**
     * Preparation mechanism before rendering starts (Option 2 implementation).
     * Builds bounding boxes and constructs the BVH tree if the optimizations are active.
     * Must be called before multi-threaded rendering begins to avoid race conditions.
     */
    public void prepareScene() {
        if (isCbrActive) {
            // 1. Force all geometries to calculate their minimal bounding boxes
            this._scene.geometries.buildBox(); 
            
            if (isBvhActive) {
                // 2. Reorganize the flat collection into a hierarchical tree structure
                this._scene.geometries.buildBVHTree(); 
            }
        }
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