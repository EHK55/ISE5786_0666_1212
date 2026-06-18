package renderer;

import org.junit.jupiter.api.Test;

import geometries.impl.Plane;
import geometries.impl.Sphere;
import lighting.AmbientLight;
import lighting.PointLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Tests to verify Glossy reflection effect and Diffuse Glass.
 */
public class GlossyTest {

	private Scene createGlossyScene() {
		Scene scene = new Scene("Test Glossy Floor");
		// Lower ambient light intensity to enhance contrast and volume
		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.05)));

		// --- 1. GEOMETRIES ---
		scene.geometries.add(
				// GLOSSY MIRROR FLOOR
				new Plane(new Point(0, -30, 0), new Vector(0, 1, 0)).setEmission(new Color(20, 20, 20))
						.setMaterial(new Material().setKD(0.5).setKR(1.0).setBlurRadius(0.05)),

				// RED SPHERE
				new Sphere(new Point(-40, 0, 50), 30d).setEmission(new Color(255, 0, 0))
						.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(60)),

				// BLUE SPHERE
				new Sphere(new Point(40, 0, 50), 30d).setEmission(new Color(0, 0, 255))
						.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(60)));

		// --- 2. LIGHT SOURCES ---
		scene.lights
				.add(new PointLight(new Color(400, 400, 400), new Point(100, 200, 300)).setKl(0.0004).setKq(0.0000006));

		return scene;
	}

	@Test
	public void testGlossyMirrorFloor_NoBlur() {
		Scene scene = createGlossyScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 150, 600))
				// Vectors calculated to look downwards (0, -0.25, -1) orthogonally
				.setDirection(new Vector(0, -0.25, -1), new Vector(0, 1, -0.25)).setVpSize(150, 150).setVpDistance(400)
				.setResolution(600, 600)
				.setRayTracer(new SimpleRayTracer(scene).setBeamResolution(1).setJittered(false)).setMultithreading(4)
				.setDebugPrint(0.5).build();

		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("GlossyFloor_NoBlur");
		System.out.println("Mirror (Without improvement) : " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
	}

	@Test
	public void testGlossyMirrorFloor_WithBlur() {
		Scene scene = createGlossyScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 150, 600))
				.setDirection(new Vector(0, -0.25, -1), new Vector(0, 1, -0.25)).setVpSize(150, 150).setVpDistance(400)
				.setResolution(600, 600)
				// Enable Super-Sampling x9 and Jittering
				.setRayTracer(new SimpleRayTracer(scene).setBeamResolution(9).setJittered(true)).setMultithreading(4)
				.setDebugPrint(0.5).build();

		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("GlossyFloor_WithBlur");
		System.out.println("Mirror (With Super-Sampling) : " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
	}
}