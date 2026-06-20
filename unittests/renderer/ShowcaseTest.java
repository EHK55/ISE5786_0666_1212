package renderer;

import org.junit.jupiter.api.Test;

import geometries.impl.Plane;
import geometries.impl.Sphere;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.SamplingPattern;
import primitives.Vector;
import scene.Scene;

public class ShowcaseTest {

	private Scene createShowcaseScene() {
		Scene scene = new Scene("Final Showcase");

//		// Soft ambient light
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30), new Double3(0.1)));

		// --- 1. THE FLOOR ---
		scene.geometries.add(new Plane(new Point(0, -50, 0), new Vector(0, 1, 0)).setEmission(new Color(20, 20, 20))
				.setMaterial(new Material().setKD(0.5).setKR(0.4).setBlurRadius(0.1)));

		// --- 2. THE 3 MAIN SPHERES ---
		scene.geometries.add(
				// Center: Perfect Glass (Clear)
				new Sphere(new Point(0, -10, 0), 40d).setEmission(new Color(0, 0, 0)).setMaterial(
						new Material().setKD(0.1).setKS(0.8).setShininess(100).setKT(0.9).setBlurRadius(0.0)),
				// Left: Brushed Metal (Blurry)
				new Sphere(new Point(-90, -20, 20), 30d).setEmission(new Color(20, 20, 20)).setMaterial(
						new Material().setKD(0.1).setKS(0.8).setShininess(80).setKR(0.8).setBlurRadius(0.3)),
				// Right: Frosted Glass (Blurry)
				new Sphere(new Point(90, -20, 20), 30d).setEmission(new Color(0, 0, 0)).setMaterial(
						new Material().setKD(0.1).setKS(0.8).setShininess(80).setKT(0.8).setBlurRadius(0.3)));

		// --- 3. COLORFUL SPHERES (Shifted for visibility) ---
		scene.geometries.add(
				// Large Red Sphere (Shifted right and up to peek behind the glass)
				new Sphere(new Point(20, 10, -80), 30d).setEmission(new Color(180, 0, 0))
						.setMaterial(new Material().setKD(0.6).setKS(0.5).setShininess(50)),
				// Small Blue Sphere at the front (Shifted right)
				new Sphere(new Point(45, -35, 60), 15d).setEmission(new Color(0, 50, 200))
						.setMaterial(new Material().setKD(0.5).setKS(0.8).setShininess(80).setKR(0.2)),
				// Small Green Floating Sphere (Higher and to the left)
				new Sphere(new Point(-50, 50, -10), 15d).setEmission(new Color(0, 200, 50))
						.setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(100).setKR(0.4)));

		// --- 4. SMALL DECORATIVE MARBLES ---
		scene.geometries.add(
				// Yellow
				new Sphere(new Point(-70, -40, 60), 10d).setEmission(new Color(200, 150, 0))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),
				// Purple
				new Sphere(new Point(70, -40, 70), 10d).setEmission(new Color(100, 0, 150))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),
				// Cyan (Shifted left to avoid center obstruction)
				new Sphere(new Point(-20, -45, 80), 5d).setEmission(new Color(0, 150, 150))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)));

		// --- 5. LIGHTING (Soft 3-point setup without DirectionalLight) ---

		scene.lights.add(
				// Key Light: Softened SpotLight from the front-top
				new SpotLight(new Color(300, 300, 300), new Point(0, 200, 300), new Vector(0, -1, -2)).setKl(0.00001)
						.setKq(0.0000001));

		scene.lights.add(
				// Fill Light 1: Soft orange PointLight from the left
				new PointLight(new Color(200, 100, 50), new Point(-200, 100, 100)).setKl(0.00005).setKq(0.0000005));

		scene.lights.add(
				// Fill Light 2: Soft blue PointLight from the right rear
				new PointLight(new Color(50, 50, 150), new Point(200, 50, -200)).setKl(0.00005).setKq(0.0000005));

		return scene;
	}

	@Test
	public void testFinalShowcase_WithoutSuperSampling() {
		Scene scene = createShowcaseScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 70, 300))
				.setDirection(new Point(0, -20, 0), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(200)
				.setResolution(600, 600).setRayTracer((new SimpleRayTracer(scene))) // 1
																					// single
																					// ray
				.setMultithreading(4) // <--- ADD THIS HERE
				.setDebugPrint(0.5) // <--- ADD THIS HERE
				.build();
		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("Showcase_Final_NoBlur");
		long endTime = System.currentTimeMillis();

		System.out.println("Time WITHOUT Super-Sampling : " + (endTime - startTime) / 1000.0 + " seconds");
	}

	@Test
	public void testFinalShowcase_WithSuperSampling() {
		Scene scene = createShowcaseScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 70, 300))
				.setDirection(new Point(0, -20, 0), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(200)
				.setResolution(600, 600)
				.setRayTracer((new SimpleRayTracer(scene).setBeamResolution(9).setPattern(SamplingPattern.JITTERED))) // 9
																														// rays
				.setMultithreading(4) // <--- ADD THIS HERE (Enable 4 cores)
				.setDebugPrint(0.5) // <--- ADD THIS HERE (Display progress every 0.5s)
				.build();
		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("Showcase_Final_WithBlur");
		long endTime = System.currentTimeMillis();

		System.out.println("Time WITH Super-Sampling : " + (endTime - startTime) / 1000.0 + " seconds");
	}
}