package primitives;

/**
 * Represents a point in a 3D Cartesian coordinate system. This class is the
 * base for Vector and uses an immutable Double3.
 */
public class Point {

	/** The coordinates of the point */
	protected final Double3 xyz;

	/** The origin point (0,0,0) */
	public static final Point ZERO = new Point(0, 0, 0);

	/**
	 * Constructor to initialize Point based on 3 coordinates.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 */
	public Point(double x, double y, double z) {
		this.xyz = new Double3(x, y, z);
	}

	/**
	 * Constructor to initialize Point based on a Double3 object.
	 * 
	 * @param xyz the coordinates as Double3
	 */
	public Point(Double3 xyz) {
		this.xyz = xyz;
	}

	/**
	 * Subtracts another point from this point to create a vector.
	 * 
	 * @param other the other point
	 * @return a new Vector from the other point to this point
	 */
	public Vector subtract(Point other) {
		return new Vector(this.xyz.subtract(other.xyz));
	}

	/**
	 * Adds a vector to this point.
	 * 
	 * @param vector the vector to add
	 * @return a new Point shifted by the vector
	 */
	public Point add(Vector vector) {
		return new Point(this.xyz.add(vector.xyz));
	}

	/**
	 * Calculates the squared distance between this point and another.
	 * 
	 * @param other the other point
	 * @return the squared distance
	 */
	public double distanceSquared(Point other) {
		double dx = this.xyz._d1() - other.xyz._d1();
		double dy = this.xyz._d2() - other.xyz._d2();
		double dz = this.xyz._d3() - other.xyz._d3();
		return dx * dx + dy * dy + dz * dz;
	}

	/**
	 * Calculates the distance between this point and another. DRY principle: calls
	 * distanceSquared.
	 * 
	 * @param other the other point
	 * @return the distance
	 */
	public double distance(Point other) {
		return Math.sqrt(distanceSquared(other));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return xyz.equals(((Point) obj).xyz);
	}

	@Override
	public String toString() {
		return "Point: " + xyz;
	}
}