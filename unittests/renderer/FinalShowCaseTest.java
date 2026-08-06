package renderer;

import java.util.Random;

import org.junit.jupiter.api.Test;

import geometries.api.Intersectable;
import geometries.impl.Geometries;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Classe de test finale pour démontrer toutes les capacités du moteur 3D : BVH,
 * Multithreading, Glossy Surfaces, Frosted Glass, Ombres et Transparences.
 */
class FinalShowCaseTest {

	@Test
	void renderFinalMasterpiece() {
		// 1. Initialisation de la scène (Ambiance nuit spatiale)
		Scene scene = new Scene("Final Masterpiece").setBackground(new Color(5, 10, 25)) // Ciel bleu très sombre
				.setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.02))); // Légère lumière
																									// d'ambiance

		// ==========================================
		// 2. CRÉATION DES MATÉRIAUX (Le pouvoir du Super-Sampling !)
		// ==========================================

		// Matériau 1 : Le Sol - Un miroir légèrement flou (Glossy)
		Material glossyFloor = new Material().setKD(0.2).setKS(0.5).setShininess(100).setKR(0.4) // 40% de réflexion
				.setBlurRadius(0.1); // Un léger flou pour le réalisme (Glossy)

		// Matériau 2 : La Sphère Centrale - Du verre dépoli (Frosted Glass)
		Material frostedGlass = new Material().setKD(0.1).setKS(0.8).setShininess(80).setKT(0.8) // 80% de transparence
				.setBlurRadius(0.2); // Flou de transparence (Verre dépoli)

		// Matériau 3 : Les Éclats (Or et Cyan)
		Material goldMat = new Material().setKD(0.4).setKS(0.8).setShininess(50).setKR(0.1);
		Material cyanMat = new Material().setKD(0.5).setKS(0.5).setShininess(30).setKT(0.5);

		// ==========================================
		// 3. AJOUT DES GÉOMÉTRIES
		// ==========================================

		// Le Sol (Une sphère géante)
		scene.geometries.add(
				new Sphere(new Point(0, -2000, 0), 2000).setEmission(new Color(5, 5, 10)).setMaterial(glossyFloor));

		// La Pièce Maîtresse (Sphère en verre dépoli au centre)
		scene.geometries
				.add(new Sphere(new Point(0, 60, 0), 60).setEmission(new Color(0, 0, 0)).setMaterial(frostedGlass));

		// Le Cœur Magique (Petite sphère brillante DANS la sphère en verre)
		scene.geometries.add(new Sphere(new Point(0, 60, 0), 20).setEmission(new Color(255, 50, 0)) // Émet du rouge
																									// très fort
				.setMaterial(new Material().setKD(0.1).setKT(0.2)));

		// Le Tourbillon Magique (Pour tester la puissance du BVH)
		Geometries vortex = new Geometries();
		Random rand = new Random(42); // Graine fixe

		for (int i = 0; i < 800; i++) {
			// Positions aléatoires autour de la sphère centrale (Cylindre / Tornade)
			double angle = rand.nextDouble() * 2 * Math.PI;
			double radius = 80 + rand.nextDouble() * 200; // Entre 80 et 280 de distance
			double height = rand.nextDouble() * 300 - 50; // De -50 à 250 de haut

			double x = Math.cos(angle) * radius;
			double z = Math.sin(angle) * radius;

			Point p = new Point(x, height, z);

			// Alternance aléatoire entre Triangles et Sphères
			if (i % 2 == 0) {
				// Éclats dorés (Triangles)
				Point p2 = p.add(new Vector(10, rand.nextDouble() * 15, 0));
				Point p3 = p.add(new Vector(0, rand.nextDouble() * 15, 10));
				vortex.add(new Triangle(p, p2, p3).setEmission(new Color(200, 150, 0)).setMaterial(goldMat));
			} else {
				// Bulles magiques (Sphères cyan en verre)
				vortex.add(new Sphere(p, 6).setEmission(new Color(0, 100, 150)).setMaterial(cyanMat));
			}
		}
		scene.geometries.add(vortex);

		// ==========================================
		// 4. AJOUT DES LUMIÈRES
		// ==========================================

		// Une lumière à l'intérieur de la sphère en verre (brille vers l'extérieur)
		scene.lights.add(new PointLight(new Color(255, 100, 0), new Point(0, 60, 0)).setKl(0.0005).setKq(0.00005));

		// Un spot bleu théâtral qui éclaire la scène d'en haut à droite
		scene.lights.add(new SpotLight(new Color(100, 150, 255), new Point(300, 500, 300), new Vector(-1, -1, -1))
				.setKl(0.00001).setKq(0.0000005));

		// Une douce lumière lunaire (Directional)
		scene.lights.add(new DirectionalLight(new Color(20, 30, 50), new Vector(1, -0.5, 0)));

		// ==========================================
		// 5. CONFIGURATION DU MOTEUR (L'optimisation)
		// ==========================================

		// Activation manuelle du BVH (très important !)
		Intersectable.setCbrActive(true);
		Geometries.setBvhActive(true);
		scene.geometries.buildBox();
		scene.geometries.buildBVHTree(); // On force la construction pour être sûr

		// Configuration du Ray Tracer avec un faisceau de rayons (Super-Sampling)
		// Faisceau de 9 rayons (3x3). C'est ici que tu seras heureux d'avoir réglé le
		// Problème 2 !
		SimpleRayTracer rayTracer = new SimpleRayTracer(scene).setBeamResolution(9);

		// Lancement du rendu !
		System.out.println("Démarrage du rendu Final Showcase...");
		long startTime = System.currentTimeMillis();

		Camera.getBuilder().setDirection(new Vector(0, -0.2, -1), Vector.AXIS_Y) // Regarde légèrement vers le bas
				.setLocation(new Point(0, 120, 600)) // Caméra reculée
				.setVpDistance(1000).setVpSize(200, 200).setResolution(800, 800) // Haute résolution pour admirer les
																					// détails
				.setRayTracer(rayTracer).setMultithreading(-2) // Utilise presque tous les cœurs (L'arme secrète !)
				.setDebugPrint(0.1) // Affiche un % d'avancement
				.build().renderImage().writeToImage("Final_Showcase_Masterpiece");

		double renderTime = (System.currentTimeMillis() - startTime) / 1000.0;
		System.out.println("Rendu terminé avec succès en : " + renderTime + " secondes !");
	}
}