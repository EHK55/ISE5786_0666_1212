//package renderer;
//
//import org.junit.jupiter.api.Test;
//
//import geometries.impl.Plane;
//import geometries.impl.Sphere;
//import lighting.AmbientLight;
//import lighting.PointLight;
//import primitives.Color;
//import primitives.Double3;
//import primitives.Material;
//import primitives.Point;
//import primitives.Vector;
//import scene.Scene;
//
///**
// * Tests pour vérifier l'effet Glossy (réflexion floue) et Diffuse Glass.
// */
//public class GlossyTest {
//
//	private Scene scene = new Scene("Test Glossy Scene");
//
//	@Test
//	public void testGlossyMirrorFloor() {
//		Scene scene = new Scene("Test Glossy Floor");
//
//		// On baisse la lumière ambiante pour mieux voir les volumes 3D
//		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.05)));
//
//		// --- 1. LES GÉOMÉTRIES ---
//		scene.geometries.add(
//				// LE SOL INFINI (Le miroir flou avec blurRadius à 0.05)
//				// On le place à Y = -30 pour que les sphères soient posées dessus
//				new Plane(new Point(0, -30, 0), new Vector(0, 1, 0)).setEmission(new Color(20, 20, 20))
//						.setMaterial(new Material().setKD(0.5).setKR(1.0).setBlurRadius(0.05)),
//
//				// SPHÈRE ROUGE
//				// Centre à Y = 0, Rayon = 30. Le bas touche exactement le sol (0 - 30 = -30) !
//				new Sphere(new Point(-40, 0, 50), 30d).setEmission(new Color(255, 0, 0))
//						// kS réduit à 0.3 (moins de blanc), shininess augmenté à 60 (point plus
//						// concentré)
//						.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(60)),
//
//				// SPHÈRE BLEUE
//				new Sphere(new Point(40, 0, 50), 30d).setEmission(new Color(0, 0, 255))
//						.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(60)));
//
//		// --- 2. LA LUMIÈRE ---
//		// On baisse drastiquement la couleur de base de la lumière
//		scene.lights
//				.add(new PointLight(new Color(400, 400, 400), new Point(100, 200, 300)).setKl(0.0004).setKq(0.0000006));
//
//		// --- 3. LA CAMÉRA ---
//		Camera camera = Camera.getBuilder()
//				// On monte la caméra BEAUCOUP plus haut (Y = 150) et un peu plus près
//				.setLocation(new Point(0, 150, 600))
//				// On regarde vers le centre, la caméra va donc pointer vers le bas !
//				.setDirection(new Point(0, 0, 0), new Vector(0, 1, 0)).setVpSize(150, 150).setVpDistance(400) // On
//																												// réduit
//																												// un
//																												// peu
//																												// la
//																												// distance
//																												// pour
//																												// un
//																												// plan
//																												// plus
//																												// large
//				.setResolution(500, 500).setRayTracer(new SimpleRayTracer(scene).setBeamResolution(9)).build();
//
//		// --- 4. LE RENDU ---
//		camera.renderImage().writeToImage("glossyFloorTest_V3"); // Nouveau nom !
//	}
//}

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

public class GlossyTest {

	private Scene createGlossyScene() {
		Scene scene = new Scene("Test Glossy Floor");
		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.05)));

		// --- 1. LES GÉOMÉTRIES ---
		scene.geometries.add(
				// LE SOL MIROIR FLOU
				new Plane(new Point(0, -30, 0), new Vector(0, 1, 0)).setEmission(new Color(20, 20, 20))
						.setMaterial(new Material().setKD(0.5).setKR(1.0).setBlurRadius(0.05)),

				// SPHÈRE ROUGE
				new Sphere(new Point(-40, 0, 50), 30d).setEmission(new Color(255, 0, 0))
						.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(60)),

				// SPHÈRE BLEUE
				new Sphere(new Point(40, 0, 50), 30d).setEmission(new Color(0, 0, 255))
						.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(60)));

		// --- 2. LA LUMIÈRE ---
		scene.lights
				.add(new PointLight(new Color(400, 400, 400), new Point(100, 200, 300)).setKl(0.0004).setKq(0.0000006));

		return scene;
	}

	@Test
	public void testGlossyMirrorFloor_NoBlur() {
		Scene scene = createGlossyScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 150, 600))
				// Vecteurs calculés pour regarder vers le bas (0, -0.25, -1) de manière
				// orthogonale
				.setDirection(new Vector(0, -0.25, -1), new Vector(0, 1, -0.25)).setVpSize(150, 150).setVpDistance(400)
				.setResolution(500, 500)
				.setRayTracer(new SimpleRayTracer(scene).setBeamResolution(1).setJittered(false)).setMultithreading(4)
				.setDebugPrint(0.5).build();

		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("GlossyFloor_NoBlur");
		System.out.println("Miroir (Sans amélioration) : " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
	}

	@Test
	public void testGlossyMirrorFloor_WithBlur() {
		Scene scene = createGlossyScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 150, 600))
				.setDirection(new Vector(0, -0.25, -1), new Vector(0, 1, -0.25)).setVpSize(150, 150).setVpDistance(400)
				.setResolution(500, 500)
				// Activation du Super-Sampling x9 et du Jittering
				.setRayTracer(new SimpleRayTracer(scene).setBeamResolution(9).setJittered(true)).setMultithreading(4)
				.setDebugPrint(0.5).build();

		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("GlossyFloor_WithBlur");
		System.out.println("Miroir (Avec Super-Sampling) : " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
	}
}