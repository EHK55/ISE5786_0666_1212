package primitives;

/**
 * Represents a mathematical vector in 3D space. Inherits from Point and adds
 * vector operations.
 */
public final class Vector extends Point {

	/** X axis vector (1,0,0) */
	public static final Vector AXIS_X = new Vector(1, 0, 0);
	/** Y axis vector (0,1,0) */
	public static final Vector AXIS_Y = new Vector(0, 1, 0);
	/** Z axis vector (0,0,1) */
	public static final Vector AXIS_Z = new Vector(0, 0, 1);

	/**
	 * Constructor taking 3 coordinates.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @throws IllegalArgumentException if the vector is zero
	 */
	public Vector(double x, double y, double z) {
		super(x, y, z);
		if (this._xyz.equals(Double3.ZERO)) {
			throw new IllegalArgumentException("Vector zero is forbidden");
		}
	}

	/**
	 * Constructor taking a Double3 object.
	 * 
	 * @param xyz the coordinates as Double3
	 * @throws IllegalArgumentException if the vector is zero
	 */
	public Vector(Double3 xyz) {
		super(xyz);
		if (this._xyz.equals(Double3.ZERO)) {
			throw new IllegalArgumentException("Vector zero is forbidden");
		}
	}

	/**
	 * Adds a vector to this vector.
	 * 
	 * @param other the other vector
	 * @return a new vector
	 */
	public Vector add(Vector other) {
		return new Vector(this._xyz.add(other._xyz));
	}

	/**
	 * Scales this vector by a number.
	 * 
	 * @param scalar the scaling factor
	 * @return a new scaled vector
	 */
	public Vector scale(double scalar) {
		return new Vector(this._xyz.scale(scalar));
	}

	/**
	 * Calculates the dot product of this vector and another.
	 * 
	 * @param other the other vector
	 * @return the dot product
	 */
	public double dotProduct(Vector other) {
		return this._xyz._d1() * other._xyz._d1() + this._xyz._d2() * other._xyz._d2()
				+ this._xyz._d3() * other._xyz._d3();
	}

	/**
	 * Calculates the cross product of this vector and another.
	 * 
	 * @param other the other vector
	 * @return a new vector resulting from the cross product
	 */
	public Vector crossProduct(Vector other) {
		return new Vector(this._xyz._d2() * other._xyz._d3() - this._xyz._d3() * other._xyz._d2(),
				this._xyz._d3() * other._xyz._d1() - this._xyz._d1() * other._xyz._d3(),
				this._xyz._d1() * other._xyz._d2() - this._xyz._d2() * other._xyz._d1());
	}

	/**
	 * Calculates the squared length of the vector. DRY principle: uses dotProduct.
	 * 
	 * @return squared length
	 */
	public double lengthSquared() {
		return dotProduct(this);
	}

	/**
	 * Calculates the length of the vector. DRY principle: uses lengthSquared.
	 * 
	 * @return length
	 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}

	/**
	 * Normalizes the vector (makes its length 1).
	 * 
	 * @return a new normalized vector
	 */
	public Vector normalize() {
		return scale(1d / length());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "->" + super.toString();
	}
}