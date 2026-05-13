package renderer;

import geometries.api.Intersectable.Intersection;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Simple implementation of the ray tracer. Uses package-private visibility.
 */
class SimpleRayTracer extends RayTracerBase {

	/**
	 * Constructor delegating to the superclass.
	 * 
	 * @param scene the scene to render
	 */
	public SimpleRayTracer(Scene scene) {
		super(scene);
	}

	@Override
	Color traceRay(Ray ray) {
		// 1. Find the intersections of the ray with the scene geometries
		var intersections = _scene.geometries.calcIntersections(ray);

		// 2. If there are no intersections, return the background color
		if (intersections == null || intersections.isEmpty()) {
			return _scene.background;
		}

		// 3. Find the closest intersection point
		var closestIntersection = ray.findClosestIntersection(intersections);

		// 4. Return the color computed at the intersection point
		return calcColor(closestIntersection);
	}

	/**
	 * Calculates the color at a specific intersection. * @param intersection the
	 * intersection object containing the geometry and material
	 * 
	 * @return the calculated Color
	 */
	private Color calcColor(Intersection intersection) {
		// Formula: Color = (Ambient Light * Material.kA) + Emission
		return _scene.ambientLight.getIntensity().scale(intersection.material.kA)
				.add(intersection.geometry.getEmission());
	}
}