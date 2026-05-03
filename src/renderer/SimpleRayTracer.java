package renderer;

import java.util.List;

import primitives.Color;
import primitives.Point;
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
		List<Point> intersections = _scene.geometries.findIntersections(ray);

		// 2. If there are no intersections, return the background color
		if (intersections == null || intersections.isEmpty()) {
			return _scene.background;
		}

		// 3. Find the closest intersection point
		Point closestPoint = ray.findClosestPoint(intersections);

		// 4. Return the color computed at the intersection point
		return calcColor(closestPoint);
	}

	/**
	 * Calculates the color at a specific intersection point.
	 * 
	 * @param intersection the point of intersection
	 * @return the calculated Color
	 */
	private Color calcColor(Point intersection) {
		// Return the AmbientLight intensity (at this stage only)
		return _scene.ambientLight.getIntensity();
	}
}