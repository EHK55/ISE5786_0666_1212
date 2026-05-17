package renderer;

import scene.Scene;
import primitives.*;
import geometries.api.Intersectable.Intersection;
import lighting.LightSource;
import java.util.List;

/**
 * Simple ray tracer implementation that computes Phong reflection model with multiple light sources.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor for SimpleRayTracer.
     * @param scene the scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = _scene.geometries.calcIntersections(ray);
        if (intersections == null) {
            return _scene.background;
        }
        Intersection closestIntersection = ray.findClosestIntersection(intersections);
        
        // התיקון כאן: שימוש ב-ray.direction() במקום ray.getDir()
        return closestIntersection == null ? _scene.background : calcColor(closestIntersection, ray.direction());
    }

    /**
     * Calculates the color of an intersection point.
     * @param intersection the intersection point
     * @param v the ray direction
     * @return the calculated color
     */
    private Color calcColor(Intersection intersection, Vector v) {
        if (!preprocessIntersection(intersection, v)) {
            return Color.BLACK;
        }
        return _scene.ambientLight.getIntensity()
                .scale(intersection.material.kA)
                .add(calcLocalEffects(intersection));
    }

    /**
     * Calculates the local lighting effects (emission, diffuse, specular) at an intersection.
     * @param intersection the intersection point
     * @return the calculated color of local effects
     */
    private Color calcLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : _scene.lights) {
            if (preprocessLightSource(intersection, lightSource)) {
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                color = color.add(lightIntensity.scale(
                        calcDiffuse(intersection).add(calcSpecular(intersection))
                ));
            }
        }
        return color;
    }

    /**
     * Calculates the diffuse reflection factor.
     * @param intersection the intersection point
     * @return the diffuse reflection coefficient
     */
    private Double3 calcDiffuse(Intersection intersection) {
        double factor = Math.abs(intersection.ln);
        return intersection.material.kD.scale(factor);
    }

    /**
     * Calculates the specular reflection factor.
     * @param intersection the intersection point
     * @return the specular reflection coefficient
     */
    private Double3 calcSpecular(Intersection intersection) {
        double ln2 = 2 * intersection.ln;
        Vector r = intersection.l.subtract(intersection.normal.scale(ln2));
        double minusVR = -intersection.v.dotProduct(r);
        if (minusVR <= 0) {
            return Double3.ZERO;
        }
        double factor = Math.pow(minusVR, intersection.material.nShininess);
        return intersection.material.kS.scale(factor);
    }
}