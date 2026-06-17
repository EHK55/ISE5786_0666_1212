package renderer;

import org.junit.Test;

import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

public class DiffuseGlassTest {
	@Test
	public void testDiffuseGlass() {
		Scene scene = new Scene("Test Diffuse Glass");
		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.1)));

		// --- 1. LES OBJETS AU FOND ---
		scene.geometries.add(
				// Sphère Rouge (au fond à gauche)
				new Sphere(new Point(-40, 0, -50), 30d).setEmission(new Color(255, 0, 0))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),

				// Sphère Bleue (au fond à droite)
				new Sphere(new Point(40, 0, -50), 30d).setEmission(new Color(0, 0, 255))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)));

		// --- 2. LA VITRE GIVRÉE (DIFFUSE GLASS) ---
		Material frostedGlass = new Material().setKD(0.1).setKS(0.1).setShininess(30).setKT(0.8)
				// Augmente drastiquement le flou ! Passe de 0.05 à 1.5 ou même 2.0
				.setBlurRadius(0.1);

		scene.geometries.add(
				// Triangle 1 du panneau de verre (placé devant les sphères, à Z = 50)
				new Triangle(new Point(-80, -60, 50), new Point(80, -60, 50), new Point(80, 60, 50))
						.setMaterial(frostedGlass),
				// Triangle 2 du panneau de verre
				new Triangle(new Point(-80, -60, 50), new Point(-80, 60, 50), new Point(80, 60, 50))
						.setMaterial(frostedGlass));

		// --- 3. LA LUMIÈRE ---
		scene.lights
				.add(new PointLight(new Color(400, 400, 400), new Point(0, 100, 100)).setKl(0.0004).setKq(0.0000006));

		// --- 4. LA CAMÉRA ---
		Camera camera = Camera.getBuilder()
				// On recule la caméra pour bien voir la vitre et ce qu'il y a derrière
				.setLocation(new Point(0, 0, 200)).setDirection(new Point(0, 0, 0), new Vector(0, 1, 0))
				.setVpSize(150, 150).setVpDistance(100).setResolution(500, 500)
				.setRayTracer(new SimpleRayTracer(scene).setBeamResolution(9)).build();

		// --- 5. LE RENDU ---
		camera.renderImage().writeToImage("diffuseGlassTest1");
	}

}
