package primitives;

/**
 * Defines the pattern used to generate sample points within the target area.
 */
public enum SamplingPattern {
	/**
	 * * Points are placed exactly at the mathematical center of each sub-cell.
	 * Produces a perfectly uniform but deterministic grid.
	 */
	GRID,

	/**
	 * * Points are randomly shifted (jittered) within their sub-cells. Produces a
	 * stochastic (random) distribution while maintaining uniform coverage.
	 */
	JITTERED
}