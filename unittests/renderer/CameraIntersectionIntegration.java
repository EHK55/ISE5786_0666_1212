package renderer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import geometries.*;
import geometries.api.Intersectable;
import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import primitives.*;
import java.util.List;

/**
 * Integration tests for Camera ray construction and Ray-Geometry intersections.
 */
public class CameraIntersectionIntegration {

    /**
     * Helper method to count intersections for all rays from a camera through a 3x3 view plane.
     * @param cam      The camera to construct rays from
     * @param geo      The geometry to intersect with
     * @param expected The expected total number of intersections
     * @param name     The name of the test case
     */
    private void assertIntersectionsCount(Camera cam, Intersectable geo, int expected, String name) {
        int count = 0;
        int nX = 3;
        int nY = 3;

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                List<Point> intersections = geo.findIntersections(cam.constructRay(j, i));
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }
        assertEquals(expected, count, "Test '" + name + "' failed: Wrong amount of intersections");
    }

    /**
     * Integration tests for Camera and Sphere.
     */
    @Test
    public void testCameraRaySphereIntegration() {
        Camera cam = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1)
                .setVpSize(3, 3)
                .setResolution(3, 3)
                .build();

        // TC01: Sphere r=1 (2 points)
        assertIntersectionsCount(cam, new Sphere(new Point(0, 0, -3), 1), 2, "Sphere r=1");

        // TC02: Sphere r=2.5 (18 points) - Move sphere slightly back to ensure camera is outside
        assertIntersectionsCount(cam, new Sphere(new Point(0, 0, -3), 2.5), 18, "Sphere r=2.5");

        // TC03: Sphere r=2 (10 points) - Center and side rays intersect twice
        assertIntersectionsCount(cam, new Sphere(new Point(0, 0, -2.5), 2), 10, "Sphere r=2");

        // TC04: Sphere r=4 (9 points) - Camera is inside the sphere
        assertIntersectionsCount(cam, new Sphere(new Point(0, 0, -1), 4), 9, "Sphere r=4");

        // TC05: Sphere r=0.5 (0 points) - Sphere is behind the camera
        assertIntersectionsCount(cam, new Sphere(new Point(0, 0, 1), 0.5), 0, "Sphere r=0.5");
    }

    /**
     * Integration tests for Camera and Plane.
     */
    @Test
    public void testCameraRayPlaneIntegration() {
        Camera cam = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1)
                .setVpSize(3, 3)
                .setResolution(3, 3)
                .build();

        // TC01: Plane parallel to VP (9 points)
        assertIntersectionsCount(cam, new Plane(new Point(0, 0, -5), new Vector(0, 0, 1)), 9, "Parallel plane");

        // TC02: Slanted plane (9 points)
        assertIntersectionsCount(cam, new Plane(new Point(0, 0, -5), new Vector(0, 1, 2)), 9, "Slanted plane");

        // TC03: Plane parallel to side rays (6 points)
        assertIntersectionsCount(cam, new Plane(new Point(0, 0, -5), new Vector(0, 5, 1)), 6, "Partially parallel plane");
    }

    /**
     * Integration tests for Camera and Triangle.
     */
    @Test
    public void testCameraRayTriangleIntegration() {
        Camera cam = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1)
                .setVpSize(3, 3)
                .setResolution(3, 3)
                .build();

        // TC01: Small triangle (1 point)
        assertIntersectionsCount(cam, new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 1, "Small triangle");

        // TC02: Tall triangle (2 points)
        assertIntersectionsCount(cam, new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 2, "Tall triangle");
    }
}