package renderer;

import primitives.*;
import java.util.MissingResourceException;

/**
 * Camera class representing the viewer's eye and responsible for ray construction.
 * This class uses the Builder pattern for its construction.
 */
public class Camera implements Cloneable {
    private Point p0;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double width;
    private double height;
    private double distance;

    // Internal resolution fields initialized to 1 as per requirements
    private int nX = 1; 
    private int nY = 1;

    /**
     * Private constructor for Camera to be used only by its Builder.
     */
    private Camera() {}

    // Getters
    public Point getP0() { return p0; }
    public Vector getvTo() { return vTo; }
    public Vector getvUp() { return vUp; }
    public Vector getvRight() { return vRight; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getDistance() { return distance; }

    /**
     * Constructs a ray through the center of a specific pixel (j, i).
     * Uses the internal resolution fields nX and nY.
     * * @param j The pixel index on the x-axis (column index)
     * @param i The pixel index on the y-axis (row index)
     * @return The ray passing from the camera origin through the pixel center
     */
    public Ray constructRay(int j, int i) {
        // Find the center of the view plane 
        Point pc = p0.add(vTo.scale(distance));

        // Calculate pixel dimensions 
        double ry = height / nY;
        double rx = width / nX;

        // Calculate offsets from the center to the pixel 
        double yi = -(i - (nY - 1) / 2.0) * ry;
        double xj = (j - (nX - 1) / 2.0) * rx;

        Point pij = pc;
        // Shift the point pij from the center to the specific pixel 
        if (!Util.isZero(xj)) pij = pij.add(vRight.scale(xj));
        if (!Util.isZero(yi)) pij = pij.add(vUp.scale(yi));

        // Return ray from camera origin to the pixel center 
        return new Ray(p0, pij.subtract(p0));
    }

    /**
     * Builder class for Camera following the Builder pattern.
     */
    public static class Builder {
        private final Camera camera = new Camera();
        private Vector vToTemp = null;
        private Point targetTemp = null;
        private Vector vUpTemp = new Vector(0, 1, 0);

        /**
         * Sets the camera location point.
         * * @param p0 The location point
         * @return The Builder instance
         */
        public Builder setLocation(Point p0) {
            this.camera.p0 = p0;
            return this;
        }

        /**
         * Sets the camera direction using two vectors.
         * * @param to Forward viewing vector
         * @param up General upward vector
         * @return The Builder instance
         */
        public Builder setDirection(Vector to, Vector up) {
            this.vToTemp = to;
            this.vUpTemp = up;
            this.targetTemp = null;
            return this;
        }

        /**
         * Sets the camera direction using a target point and an upward vector.
         * * @param target The point the camera is looking at
         * @param up     General upward vector
         * @return The Builder instance
         */
        public Builder setDirection(Point target, Vector up) {
            this.targetTemp = target;
            this.vUpTemp = up;
            this.vToTemp = null;
            return this;
        }

        /**
         * Sets the camera direction using only a target point.
         * * @param target The point the camera is looking at
         * @return The Builder instance
         */
        public Builder setDirection(Point target) {
            this.targetTemp = target;
            this.vToTemp = null;
            return this;
        }

        /**
         * Sets the view plane size.
         * * @param width  Physical width
         * @param height Physical height
         * @return The Builder instance
         */
        public Builder setVpSize(double width, double height) {
            this.camera.width = width;
            this.camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         * * @param distance The distance value
         * @return The Builder instance
         */
        public Builder setVpDistance(double distance) {
            this.camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the view plane.
         * * @param nX Number of pixels in width
         * @param nY Number of pixels in height
         * @return The Builder instance
         */
        public Builder setResolution(int nX, int nY) {
            this.camera.nX = nX;
            this.camera.nY = nY;
            return this;
        }

        /**
         * Finalizes the camera construction, performs calculations and validations.
         * * @return A ready-to-use Camera object
         * @throws MissingResourceException If required data is missing
         * @throws IllegalArgumentException If data is invalid
         */
        public Camera build() {
            final String name = "Camera";
            // Check essential data presence
            if (camera.p0 == null) throw new MissingResourceException("Missing location", name, "p0");
            if (vToTemp == null && targetTemp == null) throw new MissingResourceException("Missing direction", name, "vTo");

            // Calculate viewing direction 
            if (vToTemp == null) {
                camera.vTo = targetTemp.subtract(camera.p0).normalize();
            } else {
                camera.vTo = vToTemp.normalize();
            }

            // Compute right vector 
            try {
                camera.vRight = camera.vTo.crossProduct(vUpTemp).normalize();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Vto and Vup cannot be parallel");
            }

            // Recompute up vector for perfect orthogonality 
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            
            // Validate values
            if (camera.nX <= 0 || camera.nY <= 0) throw new IllegalArgumentException("Resolution must be positive");
            if (camera.width <= 0 || camera.height <= 0) throw new IllegalArgumentException("VP size must be positive");
            if (camera.distance <= 0) throw new IllegalArgumentException("Distance must be positive");

            // Return a copy of the constructed camera 
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    /**
     * Static method to start the building process.
     * * @return A new Camera Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }
}