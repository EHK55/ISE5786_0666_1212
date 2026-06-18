package primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class for generating 2D point grids. Used primarily for
 * Super-Sampling (Anti-aliasing, soft shadows, glossy surfaces).
 */
public class Blackboard {
	private static final Random RANDOM = new Random();

	/**
	 * Generates a grid of points, with optional Jittered Sampling. * @param
	 * targetSize The total size of the target area
	 * 
	 * @param resolution  The grid resolution (creates a resolution x resolution
	 *                    grid)
	 * @param isCircle    True to filter out points outside the inscribed circle
	 * @param useJittered True to apply random jittering inside each cell, False for
	 *                    perfect grid center
	 * @return A list of generated 2D points
	 */
	public static List<Point2D> generateJitteredPoints(double targetSize, int resolution, boolean isCircle,
			boolean useJittered) {
		List<Point2D> points = new ArrayList<>();

		// Base case: if resolution is 1 or size is 0, return a single point at the
		// origin
		if (targetSize == 0 || resolution <= 1) {
			points.add(new Point2D(0, 0));
			return points;
		}

		double cellSize = targetSize / resolution;
		double halfSize = targetSize / 2.0;
		double radiusSq = halfSize * halfSize;

		for (int i = 0; i < resolution; i++) {
			for (int j = 0; j < resolution; j++) {
				// Calculate the bottom-left anchor point of the current cell
				double baseX = -halfSize + j * cellSize;
				double baseY = -halfSize + i * cellSize;

				// Jittering Logic:
				// If enabled, generate a random offset strictly within the current cell
				// boundaries.
				// If disabled, place the point exactly at the geometric center of the cell.
				double offsetX = useJittered ? RANDOM.nextDouble() * cellSize : cellSize / 2.0;
				double offsetY = useJittered ? RANDOM.nextDouble() * cellSize : cellSize / 2.0;

				double px = baseX + offsetX;
				double py = baseY + offsetY;

				// Optional: Filter points to form a circular beam instead of a square one
				if (isCircle) {
					if ((px * px + py * py) <= radiusSq) {
						points.add(new Point2D(px, py));
					}
				} else {
					points.add(new Point2D(px, py));
				}
			}
		}
		return points;
	}
}