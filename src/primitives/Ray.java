package primitives;

/**
 * Represents a ray (half-line) in 3D space. Defined by an origin point and a
 * normalized direction vector.
 */
public class Ray {

	/** The origin point of the ray */
	private final Point origin;

	/** The normalized direction vector of the ray */
	private final Vector direction;

	/**
	 * Constructor for Ray. Automatically normalizes the direction vector before
	 * saving it.
	 * 
	 * @param origin    the origin point
	 * @param direction the direction vector
	 */
	public Ray(Point origin, Vector direction) {
		this.origin = origin;
		this.direction = direction.normalize();
	}

	/**
	 * Returns the origin point of the ray.
	 * 
	 * @return the origin
	 */
	public Point origin() {
		return origin;
	}

	/**
	 * Returns the direction vector of the ray.
	 * 
	 * @return the direction
	 */
	public Vector direction() {
		return direction;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Ray other = (Ray) obj;
		return origin.equals(other.origin) && direction.equals(other.direction);
	}

	@Override
	public String toString() {
		return "Ray [origin=" + origin + ", direction=" + direction + "]";
	}
}