//package geometries.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import geometries.api.Intersectable;
//import primitives.Ray;
//
///**
// * Composite class representing a collection of intersectable geometries
// */
//public class Geometries extends Intersectable {
//	private final List<Intersectable> _geometries = new ArrayList<>();
//
//
//	
//	public Geometries() {
//	}
//
//	public Geometries(Intersectable... geometries) {
//		add(geometries);
//	}
//
//	/**
//	 * Adds geometries to the collection using a for-each loop
//	 */
//	public void add(Intersectable... geometries) {
//		for (Intersectable item : geometries) {
//			_geometries.add(item);
//		}
//	}
//
//	@Override
//	protected List<Intersection> calcIntersectionsHelper(Ray ray) {
//		List<Intersection> result = null;
//		for (Intersectable geo : _geometries) {
//			var intersections = geo.calcIntersections(ray);
//			if (intersections != null) {
//				if (result == null) {
//					result = new ArrayList<>(intersections);
//				} else {
//					result.addAll(intersections);
//				}
//			}
//		}
//		return result;
//	}
//	
//	
//	
//	@Override
//	public void buildBox() {
//		if (_geometries.isEmpty()) {
//			box = null;
//			return;
//		}
//
//		double minX = Double.POSITIVE_INFINITY;
//		double minY = Double.POSITIVE_INFINITY;
//		double minZ = Double.POSITIVE_INFINITY;
//		
//		double maxX = Double.NEGATIVE_INFINITY;
//		double maxY = Double.NEGATIVE_INFINITY;
//		double maxZ = Double.NEGATIVE_INFINITY;
//
//		boolean hasBox = false;
//
//		// Expand the super-box to encompass all internal bounding boxes
//		for (Intersectable geo : _geometries) {
//			geo.buildBox(); 
//			if (geo.box != null) {
//				hasBox = true;
//				minX = Math.min(minX, geo.box.minX);
//				minY = Math.min(minY, geo.box.minY);
//				minZ = Math.min(minZ, geo.box.minZ);
//				
//				maxX = Math.max(maxX, geo.box.maxX);
//				maxY = Math.max(maxY, geo.box.maxY);
//				maxZ = Math.max(maxZ, geo.box.maxZ);
//			}
//		}
//
//		if (hasBox) {
//			box = new primitives.BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
//		} else {
//			box = null;
//		}
//	}
//	
//	/**
//	 * Builds the Bounding Volume Hierarchy (BVH) recursively.
//	 * Groups the internal geometries into a hierarchical binary tree structure 
//	 * to significantly optimize ray-intersection checks.
//	 */
//	public void buildBVHTree() {
//		
//		// Base case: 2 or fewer items don't need further grouping
//		if (_geometries.isEmpty() || _geometries.size() <= 2) {
//			buildBox();
//			return;
//		}
//
//		buildBox(); // Encompass all current elements to define the box dimensions
//
//		if (box == null) return;
//
//		// Find the longest dimension of the enclosing box to split along
//		double dx = box.maxX - box.minX;
//		double dy = box.maxY - box.minY;
//		double dz = box.maxZ - box.minZ;
//
//		int axis = 0; // 0 = X, 1 = Y, 2 = Z
//		if (dy > dx && dy > dz) {
//			axis = 1;
//		} else if (dz > dx && dz > dy) {
//			axis = 2;
//		}
//
//		final int finalAxis = axis;
//
//		// Sort geometries based on their centers along the longest axis
//		_geometries.sort((g1, g2) -> {
//			if (g1.box == null || g2.box == null) return 0;
//
//			double center1 = 0, center2 = 0;
//			if (finalAxis == 0) {
//				center1 = (g1.box.minX + g1.box.maxX) / 2.0;
//				center2 = (g2.box.minX + g2.box.maxX) / 2.0;
//			} else if (finalAxis == 1) {
//				center1 = (g1.box.minY + g1.box.maxY) / 2.0;
//				center2 = (g2.box.minY + g2.box.maxY) / 2.0;
//			} else {
//				center1 = (g1.box.minZ + g1.box.maxZ) / 2.0;
//				center2 = (g2.box.minZ + g2.box.maxZ) / 2.0;
//			}
//			return Double.compare(center1, center2);
//		});
//
//		// Split the sorted list into two equal halves (left and right branches)
//		int mid = _geometries.size() / 2;
//		Geometries leftGeometries = new Geometries();
//		Geometries rightGeometries = new Geometries();
//
//		for (int i = 0; i < mid; i++) {
//			leftGeometries.add(_geometries.get(i));
//		}
//		for (int i = mid; i < _geometries.size(); i++) {
//			rightGeometries.add(_geometries.get(i));
//		}
//
//		// Recursively build the hierarchy for the child nodes
//		leftGeometries.buildBVHTree();
//
//		rightGeometries.buildBVHTree();
//
//		// Replace the flat list with the two hierarchical nodes
//		_geometries.clear();
//		_geometries.add(leftGeometries);
//		_geometries.add(rightGeometries);
//	}
//	
//	/**
//	 * Flattens the hierarchical structure into a single flat Geometries object.
//	 * Required for Stage C2 control testing.
//	 * @return A new flat Geometries collection containing only the leaf geometries.
//	 */
//	public Geometries flatten() {
//		Geometries flatGeometries = new Geometries();
//		flattenHelper(this, flatGeometries);
//		return flatGeometries;
//	}
//
//	/**
//	 * Recursive helper method for flattening the hierarchy.
//	 * @param current The current intersectable being examined.
//	 * @param target The target Geometries collection to add leaves to.
//	 */
//	private void flattenHelper(Intersectable current, Geometries target) {
//		if (current instanceof Geometries composite) {
//			for (Intersectable item : composite._geometries) {
//				flattenHelper(item, target);
//			}
//		} else {
//			target.add(current); // It's a leaf (Sphere, Triangle, etc.)
//		}
//	}
//}

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

	// --- 🛡️ RESTAURATION DE LA SÉCURITÉ MULTITHREADING ---
	/** Flag to check if the BVH tree has already been built for this collection */
	private volatile boolean _isBVHBuilt = false;

	/** Global flag to dynamically activate or deactivate BVH from the tests */
	private static boolean bvhActive = false;

	/**
	 * Setter to dynamically activate or deactivate BVH from the tests.
	 * 
	 * @param active true to activate BVH
	 */
	public static void setBvhActive(boolean active) {
		bvhActive = active;
	}
	// ------------------------------------------------------

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
		// --- 🛡️ RESTAURATION DU LAZY LOADING ---
		if (bvhActive && !_isBVHBuilt)
			initializeTreeSafely();

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

	// --- 🛡️ RESTAURATION DE LA FONCTION SYNCHRONIZED ---
	/**
	 * Safely initializes the BVH tree exactly once. Uses synchronized to prevent
	 * race conditions during multithreading rendering.
	 */
	private synchronized void initializeTreeSafely() {
		if (_isBVHBuilt)
			return; // Double check inside the lock

		buildBVHTree();
		_isBVHBuilt = true; // Mark as built ONLY when completely finished
	}
	// ---------------------------------------------------

	@Override
	public void buildBox() {
		if (_geometries.isEmpty()) {
			box = null;
			return;
		}

		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;

		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;

		boolean hasBox = false;

		// Expand the super-box to encompass all internal bounding boxes
		for (Intersectable geo : _geometries) {
			geo.buildBox(); // Ton excellent filet de sécurité !
			if (geo.box != null) {
				hasBox = true;
				minX = Math.min(minX, geo.box.minX);
				minY = Math.min(minY, geo.box.minY);
				minZ = Math.min(minZ, geo.box.minZ);

				maxX = Math.max(maxX, geo.box.maxX);
				maxY = Math.max(maxY, geo.box.maxY);
				maxZ = Math.max(maxZ, geo.box.maxZ);
			}
		}

		if (hasBox) {
			box = new primitives.BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
		} else {
			box = null;
		}
	}

	/**
	 * Builds the Bounding Volume Hierarchy (BVH) recursively. Groups the internal
	 * geometries into a hierarchical binary tree structure to significantly
	 * optimize ray-intersection checks.
	 */
	public void buildBVHTree() {

		// Base case: 2 or fewer items don't need further grouping
		if (_geometries.isEmpty() || _geometries.size() <= 2) {
			buildBox();
			return;
		}

		buildBox(); // Encompass all current elements to define the box dimensions

		if (box == null)
			return;

		// Find the longest dimension of the enclosing box to split along
		double dx = box.maxX - box.minX;
		double dy = box.maxY - box.minY;
		double dz = box.maxZ - box.minZ;

		int axis = 0; // 0 = X, 1 = Y, 2 = Z
		if (dy > dx && dy > dz) {
			axis = 1;
		} else if (dz > dx && dz > dy) {
			axis = 2;
		}

		final int finalAxis = axis;

		// Sort geometries based on their centers along the longest axis
		_geometries.sort((g1, g2) -> {
			if (g1.box == null || g2.box == null)
				return 0;

			double center1 = 0, center2 = 0;
			if (finalAxis == 0) {
				center1 = (g1.box.minX + g1.box.maxX) / 2.0;
				center2 = (g2.box.minX + g2.box.maxX) / 2.0;
			} else if (finalAxis == 1) {
				center1 = (g1.box.minY + g1.box.maxY) / 2.0;
				center2 = (g2.box.minY + g2.box.maxY) / 2.0;
			} else {
				center1 = (g1.box.minZ + g1.box.maxZ) / 2.0;
				center2 = (g2.box.minZ + g2.box.maxZ) / 2.0;
			}
			return Double.compare(center1, center2);
		});

		// Split the sorted list into two equal halves (left and right branches)
		int mid = _geometries.size() / 2;
		Geometries leftGeometries = new Geometries();
		Geometries rightGeometries = new Geometries();

		for (int i = 0; i < mid; i++) {
			leftGeometries.add(_geometries.get(i));
		}
		for (int i = mid; i < _geometries.size(); i++) {
			rightGeometries.add(_geometries.get(i));
		}

		// Recursively build the hierarchy for the child nodes
		leftGeometries.buildBVHTree();
		// --- 🛡️ RESTAURATION DES FLAGS DE SÉCURITÉ ---
		leftGeometries._isBVHBuilt = true;

		rightGeometries.buildBVHTree();
		rightGeometries._isBVHBuilt = true;
		// ----------------------------------------------

		// Replace the flat list with the two hierarchical nodes
		_geometries.clear();
		_geometries.add(leftGeometries);
		_geometries.add(rightGeometries);
	}

	/**
	 * Flattens the hierarchical structure into a single flat Geometries object.
	 * Required for Stage C2 control testing.
	 * 
	 * @return A new flat Geometries collection containing only the leaf geometries.
	 */
	public Geometries flatten() {
		Geometries flatGeometries = new Geometries();
		flattenHelper(this, flatGeometries);
		return flatGeometries;
	}

	/**
	 * Recursive helper method for flattening the hierarchy.
	 * 
	 * @param current The current intersectable being examined.
	 * @param target  The target Geometries collection to add leaves to.
	 */
	private void flattenHelper(Intersectable current, Geometries target) {
		if (current instanceof Geometries composite) {
			for (Intersectable item : composite._geometries) {
				flattenHelper(item, target);
			}
		} else {
			target.add(current); // It's a leaf (Sphere, Triangle, etc.)
		}
	}
}