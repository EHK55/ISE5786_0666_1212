

package primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Blackboard {
	private static final Random random = new Random();

	// 1. On utilise nos deux Enums : TargetShape ET SamplingPattern
	public static List<Point2D> generateJitteredPoints(double targetSize, int resolution, TargetShape shape,
			SamplingPattern pattern) {
		List<Point2D> points = new ArrayList<>();

		if (resolution == 1 || targetSize == 0) {
			points.add(new Point2D(0, 0));
			return points;
		}

		double radius = targetSize;
		double radiusSq = radius * radius;
		double cellSize = (2 * radius) / resolution;
		double start = -radius;

		for (int row = 0; row < resolution; row++) {
			for (int col = 0; col < resolution; col++) {
				double jX = 0;
				double jY = 0;

				// 2. On vérifie l'Enum au lieu du boolean
				if (pattern == SamplingPattern.JITTERED) {
					jX = (random.nextDouble() - 0.5) * cellSize;
					jY = (random.nextDouble() - 0.5) * cellSize;
				}

				double px = start + col * cellSize + cellSize / 2.0 + jX;
				double py = start + row * cellSize + cellSize / 2.0 + jY;

				// 3. (Ton code précédent pour la forme)
				if (shape == TargetShape.CIRCLE) {
					if ((px * px + py * py) <= radiusSq) {
						points.add(new Point2D(px, py));
					}
				} else if (shape == TargetShape.SQUARE) {
					points.add(new Point2D(px, py));
				}
			}
		}
		return points;
	}
}