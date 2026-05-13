package renderer;

import java.util.MissingResourceException;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;
import scene.Scene;

/**
 * Camera class representing the viewer's eye and responsible for ray
 * construction. This class uses the Builder pattern for its construction.
 */
public class Camera implements Cloneable {
	/** Camera location point */
	private Point p0;
	/** Forward viewing direction vector */
	private Vector vTo;
	/** Upward orientation vector */
	private Vector vUp;
	/** Rightward orientation vector */
	private Vector vRight;
	/** Physical width of the view plane */
	private double width;
	/** Physical height of the view plane */
	private double height;
	/** Distance from the camera to the view plane */
	private double distance;

	/** Internal resolution field for horizontal pixel count */
	private int nX = 1;
	/** Internal resolution field for vertical pixel count */
	private int nY = 1;

	private ImageWriter imageWriter;
	private RayTracerBase _rayTracer;

	/**
	 * Private constructor for Camera to be used only by its Builder.
	 */
	private Camera() {
	}

	/**
	 * Getter for camera location.
	 * 
	 * @return The location point p0
	 */
	public Point getP0() {
		return p0;
	}

	/**
	 * Getter for forward vector.
	 * 
	 * @return The vTo vector
	 */
	public Vector getvTo() {
		return vTo;
	}

	/**
	 * Getter for up vector.
	 * 
	 * @return The vUp vector
	 */
	public Vector getvUp() {
		return vUp;
	}

	/**
	 * Getter for right vector.
	 * 
	 * @return The vRight vector
	 */
	public Vector getvRight() {
		return vRight;
	}

	/**
	 * Getter for view plane width.
	 * 
	 * @return The width value
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Getter for view plane height.
	 * 
	 * @return The height value
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Getter for distance to view plane.
	 * 
	 * @return The distance value
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Constructs a ray through the center of a specific pixel (j, i).
	 * 
	 * @param xIndex The pixel index on the x-axis (column index)
	 * @param yIndex The pixel index on the y-axis (row index)
	 * @return The ray passing from the camera origin through the pixel center
	 */
	public Ray constructRay(int xIndex, int yIndex) {
		Point pc = p0.add(vTo.scale(distance));

		double ry = height / nY;
		double rx = width / nX;

		double yi = -(yIndex - (nY - 1) / 2.0) * ry;
		double xj = (xIndex - (nX - 1) / 2.0) * rx;

		Point pij = pc;
		if (!Util.isZero(xj))
			pij = pij.add(vRight.scale(xj));
		if (!Util.isZero(yi))
			pij = pij.add(vUp.scale(yi));

		return new Ray(p0, pij.subtract(p0));
	}

	/**
	 * Renders the image by casting rays for every pixel.
	 * 
	 * @return the camera instance
	 */
	public Camera renderImage() {
		for (int i = 0; i < nX; i++) {
			for (int j = 0; j < nY; j++) {
				castRay(i, j);
			}
		}
		return this;
	}

	/**
	 * Casts a ray through a specific pixel and writes its color.
	 * 
	 * @param xIndex the x-coordinate of the pixel (column)
	 * @param yIndex the y-coordinate of the pixel (row)
	 */
	private void castRay(int xIndex, int yIndex) {
		// 1. Construct the ray
		Ray ray = constructRay(xIndex, yIndex);
		// 2. Trace the ray and get its color
		Color color = _rayTracer.traceRay(ray);
		// 3. Color the pixel
		imageWriter.writePixel(xIndex, yIndex, color);
	}

	/**
	 * Prints a grid on the image.
	 * 
	 * @param interval the interval between grid lines
	 * @param color    the color of the grid
	 * @return the camera instance
	 */
	/**
	 * Prints a grid on the image. * @param interval The grid interval (in pixels)
	 * 
	 * @param color The color of the grid lines
	 * @return The camera object itself for method chaining
	 */
	public Camera printGrid(int interval, Color color) {
		// Outer loop: iterate over the rows (y-axis)
		for (int yIndex = 0; yIndex < nY; yIndex++) {
			// Inner loop: iterate over the columns (x-axis)
			for (int xIndex = 0; xIndex < nX; xIndex++) {
				// If the pixel is on the grid line, paint it with the grid color
				if (xIndex % interval == 0 || yIndex % interval == 0) {
					imageWriter.writePixel(xIndex, yIndex, color);
				}
			}
		}
		return this;
	}

	public Camera printFrame(Color color) {
		// Outer loop: iterate over the rows (y-axis)
		for (int yIndex = 0; yIndex < nY; yIndex++) {
			// Inner loop: iterate over the columns (x-axis)
			for (int xIndex = 0; xIndex < nX; xIndex++) {
				// If the pixel is on the grid line, paint it with the grid color
				if (xIndex == 0 || yIndex == 0 || yIndex == nY - 1 || xIndex == nX - 1) {
					imageWriter.writePixel(xIndex, yIndex, color);
				}
			}
		}
		return this;
	}

	/**
	 * Writes the rendered image to a file.
	 * 
	 * @param fileName the name of the output file
	 */
	public void writeToImage(String fileName) {
		imageWriter.writeToImage(fileName);
	}

	/**
	 * Builder class for Camera following the Builder pattern.
	 */
	public static class Builder {
		/** Internal camera instance being built */
		private final Camera camera = new Camera();
		/** Temporary storage for forward vector during build */
		private Vector vToTemp = null;
		/** Temporary storage for target point during build */
		private Point targetTemp = null;
		/** Temporary storage for up vector, initialized to Y axis */
		private Vector vUpTemp = new Vector(0, 1, 0);

		/**
		 * Default constructor for the Builder.
		 */
		public Builder() {
		}

		/**
		 * Sets the camera location point.
		 * 
		 * @param p0 The location point
		 * @return The Builder instance
		 */
		public Builder setLocation(Point p0) {
			this.camera.p0 = p0;
			return this;
		}

		/**
		 * Sets the camera direction using two vectors.
		 * 
		 * @param to Forward viewing vector
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
		 * 
		 * @param target The point the camera is looking at
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
		 * 
		 * @param target The point the camera is looking at
		 * @return The Builder instance
		 */
		public Builder setDirection(Point target) {
			this.targetTemp = target;
			this.vToTemp = null;
			return this;
		}

		/**
		 * Sets the view plane size.
		 * 
		 * @param width  Physical width
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
		 * 
		 * @param distance The distance value
		 * @return The Builder instance
		 */
		public Builder setVpDistance(double distance) {
			this.camera.distance = distance;
			return this;
		}

		/**
		 * Sets the resolution of the view plane.
		 * 
		 * @param nX Number of pixels in width
		 * @param nY Number of pixels in height
		 * @return The Builder instance
		 */
		public Builder setResolution(int nX, int nY) {
			this.camera.nX = nX;
			this.camera.nY = nY;
			return this;
		}

		private void checkResolution() {
			if (camera.nX <= 0 || camera.nY <= 0)
				throw new IllegalArgumentException("Resolution must be positive");

		}

		private void checkLocationAndDirection() {
			final String name = "Camera";
			if (camera.p0 == null)
				throw new MissingResourceException("Missing location", name, "p0");
			if (vToTemp == null && targetTemp == null)
				throw new MissingResourceException("Missing direction", name, "vTo");

			if (vToTemp == null) {
				camera.vTo = targetTemp.subtract(camera.p0).normalize();
			} else {
				camera.vTo = vToTemp.normalize();
			}

			try {
				camera.vRight = camera.vTo.crossProduct(vUpTemp).normalize();
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Vto and Vup cannot be parallel");
			}

			camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();

		}

		private void checkViewPlane() {
			if (camera.width <= 0 || camera.height <= 0)
				throw new IllegalArgumentException("VP size must be positive");
			if (camera.distance <= 0)
				throw new IllegalArgumentException("Distance must be positive");

		}

		/**
		 * Finalizes the camera construction, performs calculations and validations.
		 * 
		 * @return A ready-to-use Camera object
		 * @throws MissingResourceException If required data is missing
		 * @throws IllegalArgumentException If data is invalid
		 */

		public Camera build() {
			checkResolution();
			checkLocationAndDirection();
			checkViewPlane();
			// Check if rayTracer is null and set default
			if (camera._rayTracer == null) {
				setRayTracer(new Scene("test"), RayTracerType.SIMPLE);
			}

			// Initialize the ImageWriter
			camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
			try {
				return (Camera) camera.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}

		/**
		 * Sets the ray tracer for the camera.
		 * 
		 * @param scene the scene to render
		 * @param type  the type of ray tracer to use
		 * @return the builder instance
		 */
		public Builder setRayTracer(Scene scene, RayTracerType type) {
			if (type == RayTracerType.SIMPLE) {
				camera._rayTracer = new SimpleRayTracer(scene);
			} else {
				throw new IllegalArgumentException("Unsupported ray tracer type");
			}
			return this;
		}
	}

	/**
	 * Static method to start the building process.
	 * 
	 * @return A new Camera Builder instance
	 */
	public static Builder getBuilder() {
		return new Builder();
	}
}