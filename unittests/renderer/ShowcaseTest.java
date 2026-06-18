//package renderer;
//
//import org.junit.jupiter.api.Test;
//
//import geometries.impl.Plane;
//import geometries.impl.Sphere;
//import lighting.AmbientLight;
//import lighting.PointLight;
//import lighting.SpotLight;
//import primitives.Color;
//import primitives.Double3;
//import primitives.Material;
//import primitives.Point;
//import primitives.Vector;
//import scene.Scene;
//
//public class ShowcaseTest {
//
//	/**
//	 * Méthode utilitaire pour construire la scène partagée entre les deux tests.
//	 * Elle contient exactement 10 géométries et 3 sources de lumière (+ la lumière
//	 * ambiante).
//	 */
//	private Scene createShowcaseScene() {
//		Scene scene = new Scene("Final Showcase");
//
//		// Une lumière ambiante très faible pour laisser les autres lumières créer du
//		// contraste
//		scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20), new Double3(0.1)));
//
//		// --- 1. LE SOL (Objet 1) ---
//		scene.geometries.add(new Plane(new Point(0, -50, 0), new Vector(0, 1, 0)).setEmission(new Color(15, 15, 15))
//				.setMaterial(new Material().setKD(0.5).setKR(0.3).setBlurRadius(0.05)));
//
//		// --- 2. LES 3 BOULES PRINCIPALES (Objets 2, 3, 4) ---
//		scene.geometries.add(
//				// Au centre : Le Verre Parfait (Net)
//				new Sphere(new Point(0, -10, 0), 40d).setEmission(new Color(0, 0, 0)).setMaterial(
//						new Material().setKD(0.1).setKS(0.8).setShininess(100).setKT(0.9).setBlurRadius(0.0)),
//				// À gauche : Le Métal Brossé (Miroir Flou)
//				new Sphere(new Point(-90, -20, 20), 30d).setEmission(new Color(10, 10, 10)).setMaterial(
//						new Material().setKD(0.1).setKS(0.8).setShininess(80).setKR(0.8).setBlurRadius(0.1)),
//				// À droite : Le Verre Givré (Diffuse Glass)
//				new Sphere(new Point(90, -20, 20), 30d).setEmission(new Color(0, 0, 0)).setMaterial(
//						new Material().setKD(0.1).setKS(0.8).setShininess(80).setKT(0.8).setBlurRadius(0.1)));
//
//		// --- 3. LES BOULES DE COULEUR (Objets 5, 6, 7) ---
//		scene.geometries.add(
//				// Grosse boule Rouge Cachée derrière le verre
//				new Sphere(new Point(0, -10, -80), 30d).setEmission(new Color(200, 0, 0))
//						.setMaterial(new Material().setKD(0.6).setKS(0.5).setShininess(50)),
//				// Petite boule Bleue à l'avant
//				new Sphere(new Point(30, -35, 60), 15d).setEmission(new Color(0, 50, 255))
//						.setMaterial(new Material().setKD(0.5).setKS(0.8).setShininess(80).setKR(0.2)),
//				// Petite boule Verte flottante
//				new Sphere(new Point(-40, 40, -30), 15d).setEmission(new Color(0, 255, 50))
//						.setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(100).setKR(0.4)));
//
//		// --- 4. LES PETITES BILLES DÉCORATIVES (Objets 8, 9, 10 pour atteindre le
//		// quota de 10) ---
//		scene.geometries.add(new Sphere(new Point(-60, -40, 70), 10d).setEmission(new Color(255, 200, 0)) // Jaune
//				.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),
//				new Sphere(new Point(70, -40, 50), 10d).setEmission(new Color(150, 0, 255)) // Violette
//						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),
//				new Sphere(new Point(0, -45, 90), 5d).setEmission(new Color(0, 255, 255)) // Cyan
//						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)));
//
//		// --- 5. L'ÉCLAIRAGE (3 sources de lumière) ---
//		scene.lights.add(
//				// 1. SpotLight principal (blanche/froide de face)
//				new SpotLight(new Color(800, 800, 800), new Point(0, 200, 300), new Vector(0, -1, -2)).setKl(0.00001)
//						.setKq(0.0000001));
//		scene.lights.add(
//				// 2. PointLight secondaire (lumière chaude/orange sur le côté gauche)
//				new PointLight(new Color(600, 300, 100), new Point(-200, 100, 100)).setKl(0.00005).setKq(0.0000005));
//		scene.lights.add(
//				// 3. PointLight de contour (lumière bleutée à l'arrière droite pour détourer
//				// les objets)
//				new PointLight(new Color(100, 100, 300), new Point(200, 50, -200)).setKl(0.00005).setKq(0.0000005));
//
//		return scene;
//	}
//
//	@Test
//	public void testFinalShowcase_WithoutSuperSampling() {
//		Scene scene = createShowcaseScene();
//
//		Camera camera = Camera.getBuilder().setLocation(new Point(0, 70, 300))
//				.setDirection(new Point(0, -20, 0), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(200)
//				.setResolution(600, 600)
//				// RÉSOLUTION À 1 : L'EFFET EST DÉSACTIVÉ (Lancer de rayon classique)
//				.setRayTracer(new SimpleRayTracer(scene).setBeamResolution(1)).build();
//
//		long startTime = System.currentTimeMillis();
//		camera.renderImage().writeToImage("Showcase_NoBlur");
//		long endTime = System.currentTimeMillis();
//
//		System.out.println("Temps de rendu SANS Super-Sampling : " + (endTime - startTime) / 1000.0 + " secondes");
//	}
//
//	@Test
//	public void testFinalShowcase_WithSuperSampling() {
//		Scene scene = createShowcaseScene();
//
//		Camera camera = Camera.getBuilder().setLocation(new Point(0, 70, 300))
//				.setDirection(new Point(0, -20, 0), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(200)
//				.setResolution(400, 400) // IMAGE TOUTE PETITE (150x150)
//				// FAISCEAU À 4 : Cela fera seulement 4 rayons par reflet !
//				.setRayTracer(new SimpleRayTracer(scene).setBeamResolution(5)).build();
//
//		long startTime = System.currentTimeMillis();
//		camera.renderImage().writeToImage("Showcase_WithBlur");
//		long endTime = System.currentTimeMillis();
//
//		System.out.println("Temps de rendu AVEC Super-Sampling : " + (endTime - startTime) / 1000.0 + " secondes");
//	}
//}

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
import primitives.Vector;
import scene.Scene;

public class ShowcaseTest {

	private Scene createShowcaseScene() {
		Scene scene = new Scene("Final Showcase");

		// Lumière ambiante douce
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30), new Double3(0.1)));

		// --- 1. LE SOL ---
		scene.geometries.add(new Plane(new Point(0, -50, 0), new Vector(0, 1, 0)).setEmission(new Color(20, 20, 20))
				.setMaterial(new Material().setKD(0.5).setKR(0.4).setBlurRadius(0.1)));

		// --- 2. LES 3 BOULES PRINCIPALES ---
		scene.geometries.add(
				// Centre : Verre Parfait (Net)
				new Sphere(new Point(0, -10, 0), 40d).setEmission(new Color(0, 0, 0)).setMaterial(
						new Material().setKD(0.1).setKS(0.8).setShininess(100).setKT(0.9).setBlurRadius(0.0)),
				// Gauche : Métal Brossé (Flou)
				new Sphere(new Point(-90, -20, 20), 30d).setEmission(new Color(20, 20, 20)).setMaterial(
						new Material().setKD(0.1).setKS(0.8).setShininess(80).setKR(0.8).setBlurRadius(0.3)),
				// Droite : Verre Givré (Flou)
				new Sphere(new Point(90, -20, 20), 30d).setEmission(new Color(0, 0, 0)).setMaterial(
						new Material().setKD(0.1).setKS(0.8).setShininess(80).setKT(0.8).setBlurRadius(0.3)));

		// --- 3. LES BOULES DE COULEUR (Décalées pour être visibles !) ---
		scene.geometries.add(
				// Grosse boule Rouge (Décalée à droite et en haut pour dépasser derrière le
				// verre)
				new Sphere(new Point(20, 10, -80), 30d).setEmission(new Color(180, 0, 0))
						.setMaterial(new Material().setKD(0.6).setKS(0.5).setShininess(50)),
				// Petite boule Bleue à l'avant (Décalée à droite)
				new Sphere(new Point(45, -35, 60), 15d).setEmission(new Color(0, 50, 200))
						.setMaterial(new Material().setKD(0.5).setKS(0.8).setShininess(80).setKR(0.2)),
				// Petite boule Verte flottante (Plus haute et à gauche)
				new Sphere(new Point(-50, 50, -10), 15d).setEmission(new Color(0, 200, 50))
						.setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(100).setKR(0.4)));

		// --- 4. LES PETITES BILLES DÉCORATIVES ---
		scene.geometries.add(
				// Jaune
				new Sphere(new Point(-70, -40, 60), 10d).setEmission(new Color(200, 150, 0))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),
				// Violette
				new Sphere(new Point(70, -40, 70), 10d).setEmission(new Color(100, 0, 150))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),
				// Cyan (Décalée à gauche pour ne pas gêner le centre)
				new Sphere(new Point(-20, -45, 80), 5d).setEmission(new Color(0, 150, 150))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)));

		// --- 5. L'ÉCLAIRAGE (Puissances réduites pour éviter le blanc pur !) ---
		scene.lights.add(
				// SpotLight adouci
				new SpotLight(new Color(300, 300, 300), new Point(0, 200, 300), new Vector(0, -1, -2)).setKl(0.00001)
						.setKq(0.0000001));
		scene.lights.add(
				// PointLight orange doux
				new PointLight(new Color(200, 100, 50), new Point(-200, 100, 100)).setKl(0.00005).setKq(0.0000005));
		scene.lights.add(
				// PointLight bleu doux
				new PointLight(new Color(50, 50, 150), new Point(200, 50, -200)).setKl(0.00005).setKq(0.0000005));

		return scene;
	}

	@Test
	public void testFinalShowcase_WithoutSuperSampling() {
		Scene scene = createShowcaseScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 70, 300))
				.setDirection(new Point(0, -20, 0), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(200)
				.setResolution(400, 400)
				.setRayTracer(new SimpleRayTracer(scene).setBeamResolution(1).setJittered(false)) // 1 seul rayon
				.setMultithreading(4) // <--- AJOUTE ÇA ICI AUSSI

				.setDebugPrint(0.5) // <--- AJOUTE ÇA ICI AUSSI
				.build();
		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("Showcase_Final_NoBlur");
		long endTime = System.currentTimeMillis();

		System.out.println("Temps SANS Super-Sampling : " + (endTime - startTime) / 1000.0 + " secondes");
	}

	@Test
	public void testFinalShowcase_WithSuperSampling() {
		Scene scene = createShowcaseScene();

		Camera camera = Camera.getBuilder().setLocation(new Point(0, 70, 300))
				.setDirection(new Point(0, -20, 0), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(200)
				.setResolution(400, 400).setRayTracer(new SimpleRayTracer(scene).setBeamResolution(9).setJittered(true)) // 9
																															// rayons
				.setMultithreading(4) // <--- AJOUTE ÇA ICI (Active les 4 cœurs)
				.setDebugPrint(0.5) // <--- AJOUTE ÇA ICI (Affiche la progression toutes les demi-secondes)
				.build();
		long startTime = System.currentTimeMillis();
		camera.renderImage().writeToImage("Showcase_Final_WithBlur");
		long endTime = System.currentTimeMillis();

		System.out.println("Temps AVEC Super-Sampling : " + (endTime - startTime) / 1000.0 + " secondes");
	}

}