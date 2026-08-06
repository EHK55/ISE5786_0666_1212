package renderer;

import org.junit.jupiter.api.Test;

import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.SamplingPattern;
import primitives.Vector;
import scene.Scene;

public class DiffuseGlassTest {

	private Scene createDiffuseGlassScene() {
		Scene scene = new Scene("Test Diffuse Glass");
		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.1)));

		// --- 1. BACKGROUND OBJECTS ---
		scene.geometries.add(
				// Red Sphere (back left)
				new Sphere(new Point(-40, 0, -50), 30d).setEmission(new Color(255, 0, 0))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),
				// Blue Sphere (back right)
				new Sphere(new Point(40, 0, -50), 30d).setEmission(new Color(0, 0, 255))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)));

		// --- 2. FROSTED GLASS (DIFFUSE GLASS) ---
		Material frostedGlass = new Material().setKD(0.1).setKS(0.1).setShininess(30).setKT(0.8); // Blur
																									// enabled

		scene.geometries.add(
				// Triangle 1 of the glass pane (placed in front of spheres, at Z = 50)
				new Triangle(new Point(-80, -60, 50), new Point(80, -60, 50), new Point(80, 60, 50))
						.setMaterial(frostedGlass),
				// Triangle 2 of the glass pane
				new Triangle(new Point(-80, -60, 50), new Point(-80, 60, 50), new Point(80, 60, 50))
						.setMaterial(frostedGlass));

		// --- 3. LIGHT SOURCES ---
		scene.lights
				.add(new PointLight(new Color(400, 400, 400), new Point(0, 100, 100)).setKl(0.0004).setKq(0.0000006));

		return scene;
	}

	@Test
	public void testDiffuseGlass_NoBlur() {
		Scene scene = createDiffuseGlassScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 0, 200))
				// vTo = forward, vUp = up (orthogonal)
				.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(150, 150).setVpDistance(100)
				.setResolution(600, 600).setRayTracer(new SimpleRayTracer(scene).setBeamResolution(1))
				.setMultithreading(4).setDebugPrint(0.5).build();

		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("DiffuseGlass_NoBlur");
		System.out.println(
				"Diffuse Glass (Without improvement) : " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
	}

	@Test
	public void testDiffuseGlass_WithBlur() {
		Scene scene = createDiffuseGlassScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 0, 200))
				.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(150, 150).setVpDistance(100)
				.setResolution(600, 600)
				// Enable Super-Sampling x9 and Jittering
				.setRayTracer((new SimpleRayTracer(scene).setPattern(SamplingPattern.JITTERED))).setMultithreading(4)
				.setDebugPrint(0.5).build();

		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("DiffuseGlass_WithBlur2");
		System.out.println(
				"Diffuse Glass (With Super-Sampling) : " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
	}
}