package primitives;

/**
 * Defines the geometric shape of the target area for sampling points.
 */
public enum TargetShape {
	/** A circular target area, simulating a realistic lens bokeh */
	CIRCLE,

	/** A square target area, useful for anti-aliasing pixel grids */
	SQUARE,

	/** (Optional for future) A rectangular target area */
	// RECTANGLE
}