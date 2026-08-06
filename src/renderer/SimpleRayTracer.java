package renderer;

import java.util.ArrayList;
import java.util.List;

import geometries.api.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Blackboard;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Point2D;
import primitives.Ray;
import primitives.SamplingPattern;
import primitives.TargetShape;
import primitives.Vector;
import scene.Scene;

/**
 * Simple ray tracer implementation supporting shadows, partial transparency
 * shadows, recursive reflections, and transparency.
 */
public class SimpleRayTracer extends RayTracerBase {

	/** Recursion termination constant for maximum depth level */
	private static final int MAX_CALC_COLOR_LEVEL = 3;
	/** Recursion termination constant for minimal attenuation threshold factor */
	private static final double MIN_CALC_COLOR_K = 0.001;
	/** Initial cumulative attenuation factor value */
	private static final Double3 INITIAL_K = Double3.ONE;
	/** Resolution for the beam of rays (Super-Sampling) */
	private int beamResolution = 1;

	private SamplingPattern samplingPattern = SamplingPattern.GRID; // Default to standard Grid

	/**
	 * Sets the resolution of the ray beam for Super-Sampling. * @param resolution
	 * the resolution to set
	 * 
	 * @return the SimpleRayTracer instance
	 */
	public SimpleRayTracer setBeamResolution(int resolution) {
		this.beamResolution = resolution;
		return this;
	}

	/**
	 * Sets the sampling pattern mode.
	 * 
	 * @param pattern the sampling pattern to set (GRID or JITTERED)
	 * @return the SimpleRayTracer instance
	 */
	public SimpleRayTracer setPattern(SamplingPattern pattern) {
		this.samplingPattern = pattern;
		return this;
	}

	/**
	 * Constructor for SimpleRayTracer. * @param scene the scene
	 */
	public SimpleRayTracer(Scene scene) {
		super(scene);
	}

	@Override
	public Color traceRay(Ray ray) {
		Intersection closestIntersection = findClosestIntersection(ray);
		return closestIntersection == null ? _scene.background : calcColor(closestIntersection, ray.direction());
	}

	/**
	 * Non-recursive wrapper method introducing ambient light exactly once. * @param
	 * intersection the closest intersection point
	 * 
	 * @param v the ray direction vector
	 * @return the completely calculated pixel color
	 */
	private Color calcColor(Intersection intersection, Vector v) {
		if (!preprocessIntersection(intersection, v)) {
			return Color.BLACK;
		}
		return _scene.ambientLight.getIntensity().scale(intersection.material.kA)
				.add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
	}

	/**
	 * Recursive color calculation processing local shading effects aggregated with
	 * recursive global effects. * @param intersection the processed intersection
	 * point
	 * 
	 * @param level current recursion depth tree level
	 * @param k     cumulative attenuation path index factor
	 * @return color computed up to the current recursive layer bounds
	 */
	private Color calcColor(Intersection intersection, int level, Double3 k) {
		Color color = calcLocalEffects(intersection, k);
		return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
	}

	/**
	 * Calculates the local lighting effects (emission, diffuse, specular) at an
	 * intersection. * @param intersection the intersection point
	 * 
	 * @param k cumulative attenuation factor
	 * @return the calculated color of local effects
	 */
	private Color calcLocalEffects(Intersection intersection, Double3 k) {
		Color color = intersection.geometry.getEmission();
		for (LightSource lightSource : _scene.lights) {
			if (preprocessLightSource(intersection, lightSource)) {
				Double3 ktr = transparency(intersection, lightSource);
				if (ktr.product(k).isGreaterThan(MIN_CALC_COLOR_K)) {
					Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
					color = color.add(lightIntensity.scale(calcDiffuse(intersection).add(calcSpecular(intersection))));
				}
			}
		}
		return color;
	}

	/**
	 * Calculates the diffuse reflection factor. * @param intersection the
	 * intersection point
	 * 
	 * @return the diffuse reflection coefficient
	 */
	private Double3 calcDiffuse(Intersection intersection) {
		double factor = Math.abs(intersection.ln);
		return intersection.material.kD.scale(factor);
	}

	/**
	 * Calculates the specular reflection factor. * @param intersection the
	 * intersection point
	 * 
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

	/**
	 * Computes the aggregated partial transparency opacity level along a specific
	 * shadow ray path. * @param intersection the original shaded intersection
	 * surface reference
	 * 
	 * @param lightSource the targeted illumination light source emitter
	 * @return multi-channel light propagation drop ratio coefficient
	 */
	private Double3 transparency(Intersection intersection, LightSource lightSource) {
		Vector pointToLight = intersection.l.scale(-1);
		Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.normal);

		List<Intersection> shadowIntersections = _scene.geometries.calcIntersections(shadowRay);
		if (shadowIntersections == null) {
			return Double3.ONE;
		}

		Double3 ktr = Double3.ONE;
		double lightDistance = lightSource.getDistance(intersection.point);

		for (Intersection i : shadowIntersections) {
			if (i.point.distance(intersection.point) < lightDistance) {
				ktr = ktr.product(i.geometry.getMaterial().kT);
				if (ktr.isLowerThan(MIN_CALC_COLOR_K)) {
					return Double3.ZERO;
				}
			}
		}
		return ktr;
	}

	/**
	 * Legacy visibility tracking method preserved for validation backwards
	 * compatibility benchmarks. * @param intersection target surface coordinates
	 * 
	 * @param lightSource referenced light source instance
	 * @return true if light source is unshaded by any sufficiently opaque
	 *         structural body
	 */
	private boolean unshaded(Intersection intersection, LightSource lightSource) {
		Vector pointToLight = intersection.l.scale(-1);
		Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.normal);

		List<Intersection> shadowIntersections = _scene.geometries.calcIntersections(shadowRay);
		if (shadowIntersections == null) {
			return true;
		}

		double lightDistance = lightSource.getDistance(intersection.point);
		for (Intersection i : shadowIntersections) {
			if (i.point.distance(intersection.point) < lightDistance) {
				if (i.geometry.getMaterial().kT.isLowerThan(MIN_CALC_COLOR_K)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Combines multiple global illumination recursively tracked components
	 * (Reflection + Refraction). * @param intersection localized surface vector
	 * metadata context
	 * 
	 * @param level recursive step tree index level
	 * @param k     cumulative coefficient index track
	 * @return combined color contribution from secondary ray channels
	 */
	private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
		Color color = Color.BLACK;
		Material material = intersection.material;

		// LE BOUCLIER ANTI-EXPLOSION EST ICI :
		// Si on est au premier rebond (MAX_CALC_COLOR_LEVEL), on fait le faisceau
		// complet.
		// Sinon, on ne tire qu'UN SEUL rayon (résolution = 1) pour les rebonds
		// suivants.
		int currentRes = (level == MAX_CALC_COLOR_LEVEL) ? this.beamResolution : 1;

		Ray reflectionRay = constructReflectionRay(intersection);
		if (reflectionRay != null) {
			// On passe currentRes à la fonction
			List<Ray> reflectionBeam = constructRayBeam(reflectionRay, material.blurRadius, intersection.normal,
					currentRes);
			color = color.add(calcAverageGlobalEffect(reflectionBeam, level, k, material.kR));
		}

		Ray transparencyRay = constructTransparencyRay(intersection);
		if (transparencyRay != null) {
			// On passe currentRes à la fonction
			List<Ray> transparencyBeam = constructRayBeam(transparencyRay, material.blurRadius, intersection.normal,
					currentRes);
			color = color.add(calcAverageGlobalEffect(transparencyBeam, level, k, material.kT));
		}

		return color;
	}

	/**
	 * Evaluates a single specific secondary recursive global lighting channel
	 * branch path. * @param ray secondary ray reference trace line tracking context
	 * 
	 * @param level recursive step tree index level
	 * @param k     cumulative index product track
	 * @param kx    target geometry material effect type factor channel
	 * @return calculated color context contribution scaled by current material
	 *         factor
	 */
	private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
		if (ray == null)
			return Color.BLACK;
		Double3 kkx = k.product(kx);
		if (kkx.isLowerThan(MIN_CALC_COLOR_K))
			return Color.BLACK;

		Intersection intersection = findClosestIntersection(ray);
		if (intersection == null)
			return _scene.background.scale(kx);

		return preprocessIntersection(intersection, ray.direction()) ? calcColor(intersection, level - 1, kkx).scale(kx)
				: Color.BLACK;
	}

	/**
	 * Centralized utility helper to query raw scene elements intersection data and
	 * extract closest point. * @param ray targeted tracking line parameter
	 * 
	 * @return absolute closest localized intersection record data or null
	 */
	private Intersection findClosestIntersection(Ray ray) {
		List<Intersection> intersections = _scene.geometries.calcIntersections(ray);
		return intersections == null ? null : ray.findClosestIntersection(intersections);
	}

	/**
	 * Constructs a secondary transparency transmission ray. * @param intersection
	 * localized source origin point tracking metadata
	 * 
	 * @return secondary refractive transmission ray line element
	 */
	private Ray constructTransparencyRay(Intersection intersection) {
		return new Ray(intersection.point, intersection.v, intersection.normal);
	}

	/**
	 * Constructs a secondary mirror-like specular reflection trace ray vector.
	 * * @param intersection localized surface point context
	 * 
	 * @return secondary specular reflection ray line element or null if parallel
	 */
	private Ray constructReflectionRay(Intersection intersection) {
		double vn = intersection.v.dotProduct(intersection.normal);
		if (primitives.Util.isZero(vn))
			return null;
		Vector r = intersection.v.subtract(intersection.normal.scale(2 * vn));
		return new Ray(intersection.point, r, intersection.normal);
	}

	/**
	 * Constructs a beam of rays around a central ray for Super-Sampling effects.
	 * * @param centerRay The main directional ray
	 * 
	 * @param blurRadius The radius of the target surface
	 * @param n          The normal vector of the intersected geometry
	 * @return A list of scattered rays
	 */
	private List<Ray> constructRayBeam(Ray centerRay, double blurRadius, Vector n, int currentResolution) {
		List<Ray> beam = new ArrayList<>();

		// 1. LE BOUCLIER MAGIQUE : Si pas de flou ou 1 seul rayon, on ne perd pas de
		// temps !
		// Cela évite le crash du zéro et accélère le rendu x100 sur les objets lisses.
		if (primitives.Util.isZero(blurRadius) || currentResolution <= 1) {
			beam.add(centerRay);
			return beam;
		}

		// Utilise currentResolution au lieu de this.beamResolution !
		List<Point2D> points2D = Blackboard.generateJitteredPoints(blurRadius, currentResolution, TargetShape.CIRCLE,
				this.samplingPattern);

		if (points2D.size() == 1) {
			beam.add(centerRay);
			return beam;
		}

		Vector v = centerRay.direction();
		Point p0 = centerRay.origin();

		Point pc = p0.add(v.scale(1.0));
		Vector vX, vY;

		try {
			vX = v.crossProduct(new Vector(0, 1, 0)).normalize();
		} catch (IllegalArgumentException e) {
			vX = v.crossProduct(new Vector(1, 0, 0)).normalize();
		}
		vY = v.crossProduct(vX).normalize();

		for (Point2D p2d : points2D) {
			Point targetPoint = pc;

			// 2. UTILISER isZero() AU LIEU DE != 0 POUR ÉVITER LES BUGS MATHÉMATIQUES
			// (Vector Zero)
			if (!primitives.Util.isZero(p2d.x)) {
				targetPoint = targetPoint.add(vX.scale(p2d.x));
			}
			if (!primitives.Util.isZero(p2d.y)) {
				targetPoint = targetPoint.add(vY.scale(p2d.y));
			}

			Vector newDir = targetPoint.subtract(p0).normalize();

			double nv = n.dotProduct(v);
			double nNewDir = n.dotProduct(newDir);

			// Ajoute le rayon seulement s'il pointe dans la bonne direction par rapport à
			// la normale
			if (nv * nNewDir > 0) {
				beam.add(new Ray(p0, newDir, n));
			}
		}

		return beam;
	}

	/**
	 * Calculates the average color contribution from a beam of scattered rays.
	 * * @param rays List of secondary rays generated for Super-Sampling
	 * 
	 * @param level recursive step tree index level
	 * @param k     cumulative index product track
	 * @param kx    target geometry material effect type factor channel
	 * @return The averaged color of all valid rays
	 */
	private Color calcAverageGlobalEffect(List<Ray> rays, int level, Double3 k, Double3 kx) {
		// SAFETY: If all rays were filtered out (or the list is empty), return black.
		if (rays == null || rays.isEmpty()) {
			return Color.BLACK;
		}

		Color color = Color.BLACK;
		for (Ray ray : rays) {
			color = color.add(calcGlobalEffect(ray, level, k, kx));
		}

		// Average the color by dividing by the ACTUAL size of the list (surviving rays)
		return color.reduce(rays.size());
	}
}