package geometries.impl;

import java.util.ArrayList;
import java.util.List;

import geometries.api.Intersectable;
import primitives.Ray;

/**
 * Composite class representing a collection of intersectable geometries
 */
public class Geometries extends Intersectable {
	private final List<Intersectable> _geometries = new ArrayList<>();

	public Geometries() {
	}

	public Geometries(Intersectable... geometries) {
		add(geometries);
	}

	/**
	 * Adds geometries to the collection using a for-each loop
	 */
	public void add(Intersectable... geometries) {
		for (Intersectable item : geometries) {
			_geometries.add(item);
		}
	}

	@Override
	protected List<Intersection> calcIntersectionsHelper(Ray ray) {
		List<Intersection> result = null;
		for (Intersectable geo : _geometries) {
			var intersections = geo.calcIntersections(ray);
			if (intersections != null) {
				if (result == null) {
					result = new ArrayList<>(intersections);
				} else {
					result.addAll(intersections);
				}
			}
		}
		return result;
	}
}