package primitives;

/**
 * Axis-Aligned Bounding Box (AABB) for CBR acceleration.
 * Resides in the primitives package to legally access Point's protected fields.
 */
public class BoundingBox {
    public final double minX, maxX;
    public final double minY, maxY;
    public final double minZ, maxZ;

    /**
     * Private constructor for setting limits directly.
     */
    public BoundingBox(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        this.minX = minX; this.maxX = maxX;
        this.minY = minY; this.maxY = maxY;
        this.minZ = minZ; this.maxZ = maxZ;
    }

    /**
     * Creates a Bounding Box specifically for a Sphere.
     * @param center the center point
     * @param radius the radius of the sphere
     */
    public BoundingBox(Point center, double radius) {
        // Here we legally access the protected _xyz field!
        double cx = center._xyz._d1();
        double cy = center._xyz._d2();
        double cz = center._xyz._d3();
        
        this.minX = cx - radius; this.maxX = cx + radius;
        this.minY = cy - radius; this.maxY = cy + radius;
        this.minZ = cz - radius; this.maxZ = cz + radius;
    }

    /**
     * Creates a minimal Bounding Box starting from a single point.
     * @param p the starting point
     */
    public BoundingBox(Point p) {
        this.minX = p._xyz._d1(); this.maxX = this.minX;
        this.minY = p._xyz._d2(); this.maxY = this.minY;
        this.minZ = p._xyz._d3(); this.maxZ = this.minZ;
    }

    /**
     * Expands the current box to include another point (useful for Triangles/Polygons).
     * @param p the point to include
     * @return a new expanded BoundingBox
     */
    public BoundingBox expand(Point p) {
        double px = p._xyz._d1();
        double py = p._xyz._d2();
        double pz = p._xyz._d3();
        
        return new BoundingBox(
            Math.min(minX, px), Math.max(maxX, px),
            Math.min(minY, py), Math.max(maxY, py),
            Math.min(minZ, pz), Math.max(maxZ, pz)
        );
    }

    /**
     * Checks if a ray intersects this bounding box using Smits' algorithm.
     * @param ray the ray to check
     * @return true if the ray hits the box
     */
    public boolean isIntersected(Ray ray) {
        double p0x = ray.origin()._xyz._d1();
        double p0y = ray.origin()._xyz._d2();
        double p0z = ray.origin()._xyz._d3();

        double dirx = ray.direction()._xyz._d1();
        double diry = ray.direction()._xyz._d2();
        double dirz = ray.direction()._xyz._d3();

        double tmin = Double.NEGATIVE_INFINITY;
        double tmax = Double.POSITIVE_INFINITY;

        if (!Util.isZero(dirx)) {
            double t1 = (minX - p0x) / dirx;
            double t2 = (maxX - p0x) / dirx;
            tmin = Math.max(tmin, Math.min(t1, t2));
            tmax = Math.min(tmax, Math.max(t1, t2));
        } else if (p0x < minX || p0x > maxX) return false;

        if (!Util.isZero(diry)) {
            double t1 = (minY - p0y) / diry;
            double t2 = (maxY - p0y) / diry;
            tmin = Math.max(tmin, Math.min(t1, t2));
            tmax = Math.min(tmax, Math.max(t1, t2));
        } else if (p0y < minY || p0y > maxY) return false;

        if (!Util.isZero(dirz)) {
            double t1 = (minZ - p0z) / dirz;
            double t2 = (maxZ - p0z) / dirz;
            tmin = Math.max(tmin, Math.min(t1, t2));
            tmax = Math.min(tmax, Math.max(t1, t2));
        } else if (p0z < minZ || p0z > maxZ) return false;

        return tmin <= tmax && tmax >= 0;
    }
}