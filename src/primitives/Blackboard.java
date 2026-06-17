package primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Blackboard {
	private static final Random RANDOM = new Random();

	public static List<Point2D> generateJitteredPoints(double targetSize, int resolution, boolean isCircle) {
		List<Point2D> points = new ArrayList<>();
		if (targetSize == 0 || resolution <= 1) {
			points.add(new Point2D(0, 0));
			return points;
		}

		double cellSize = targetSize / resolution;
		double halfSize = targetSize / 2.0;
		double radiusSq = halfSize * halfSize;
		for (int i = 0; i < resolution; i++) {
			for (int j = 0; j < resolution; j++) {
				double baseX = -halfSize + j * cellSize;
				double baseY = -halfSize + i * cellSize;

				double jitterX = RANDOM.nextDouble() * cellSize;
				double jitterY = RANDOM.nextDouble() * cellSize;

				double px = baseX + jitterX;
				double py = baseY + jitterY;

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
