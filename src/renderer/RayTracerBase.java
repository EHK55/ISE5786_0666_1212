package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract class representing a ray tracer. Uses package-private visibility as
 * requested.
 */
abstract class RayTracerBase {

	/** The scene to be rendered */
	protected Scene _scene;

	/**
	 * Constructor initializing the scene.
	 * 
	 * @param scene the scene to trace
	 */
	public RayTracerBase(Scene scene) {
		this._scene = scene;
	}

	/**
	 * Traces a ray and determines the color at the intersection point.
	 * 
	 * @param ray the ray to trace
	 * @return the determined color
	 */
	abstract Color traceRay(Ray ray);
}