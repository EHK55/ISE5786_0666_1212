package renderer;

import java.util.Random;

import org.junit.jupiter.api.Test;

import geometries.api.Intersectable;
import geometries.impl.Cylinder;
import geometries.impl.Geometries;
import geometries.impl.Polygon;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

/**
 * Performance testing for CBR and BVH optimizations (Stage C2). Contains 12
 * measurements comparing Flat, Manual Hierarchy, and Auto Hierarchy.
 */
class PerformanceTests {

	private final Camera.Builder cameraBuilder = Camera.getBuilder().setDirection(new Vector(0, 0, -1), Vector.AXIS_Y)
			.setLocation(new Point(0, 50, 1000)).setVpDistance(1000).setVpSize(200, 200).setResolution(600, 600) // 600x600
																													// est
																													// suffisant
																													// pour
																													// voir
																													// la
																													// différence
																													// sans
																													// attendre
																													// des
																													// heures
			.setDebugPrint(0.5);

	private Scene buildManualHierarchyScene() {
		// Fond bleu nuit sombre
		Scene scene = new Scene("Performance Showcase").setBackground(new Color(10, 20, 40));

		// ==========================================
		// LES 5 SOURCES DE LUMIÈRE (Tous les types)
		// ==========================================

		// 1. DirectionalLight (Lumière de lune/soleil infinie)
		scene.lights.add(new DirectionalLight(new Color(30, 40, 60), new Vector(-1, -1, -1)));

		// 2. PointLight Centrale (Lumière chaude)
		scene.lights.add(new PointLight(new Color(255, 150, 50), new Point(0, 200, 0)).setKl(0.00005).setKq(0.000001));

		// 3. PointLight Bleutée (Au sol)
		scene.lights
				.add(new PointLight(new Color(50, 100, 255), new Point(-200, -20, 100)).setKl(0.0001).setKq(0.000005));

		// 4. SpotLight 1 (Projecteur magenta)
		scene.lights.add(new SpotLight(new Color(255, 0, 255), new Point(300, 400, 300), new Vector(-1, -1, -1))
				.setKl(0.00005).setKq(0.000001));

		// 5. SpotLight 2 (Projecteur cyan)
		scene.lights.add(new SpotLight(new Color(0, 255, 255), new Point(-300, 400, 300), new Vector(1, -1, -1))
				.setKl(0.00005).setKq(0.000001));

		// ==========================================
		// LES GÉOMÉTRIES (Tous les types)
		// ==========================================

		// Matériaux
		Material mirrorMat = new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.5); // Miroir
		Material glassMat = new Material().setKD(0.1).setKS(0.8).setShininess(80).setKT(0.8); // Verre
		Material matteMat = new Material().setKD(0.6).setKS(0.2).setShininess(30); // Mat

		// 1. La grande "Mer" (Miroir géant)
		scene.geometries
				.add(new Sphere(new Point(0, -2050, 0), 2000).setEmission(new Color(0, 10, 30)).setMaterial(mirrorMat));

		// 2. Quelques Cylindres (Piliers en verre)
		scene.geometries.add(
				new Cylinder(40, new Ray(new Point(-200, -50, 0), new Vector(0, 1, 0)), 300)
						.setEmission(new Color(20, 20, 20)).setMaterial(glassMat),
				new Cylinder(40, new Ray(new Point(200, -50, 0), new Vector(0, 1, 0)), 300)
						.setEmission(new Color(20, 20, 20)).setMaterial(glassMat));

		// Groupe pour les milliers de petits objets (BVH va l'adorer)
		Geometries floatingObjects = new Geometries();
		Random rand = new Random(42); // Graine fixe pour que les tests soient identiques

		// 3. Sphères (1000 objets)
		for (int i = 0; i < 1000; i++) {
			Point p = new Point(rand.nextDouble() * 1600 - 800, rand.nextDouble() * 300,
					rand.nextDouble() * 1000 - 500);
			Color col = new Color(rand.nextInt(200) + 55, rand.nextInt(200) + 55, rand.nextInt(200) + 55);
			floatingObjects.add(new Sphere(p, 10).setEmission(col).setMaterial(matteMat));
		}

		// 4. Triangles (500 objets, petits éclats de verre flottants)
		for (int i = 0; i < 500; i++) {
			Point p1 = new Point(rand.nextDouble() * 1600 - 800, rand.nextDouble() * 300,
					rand.nextDouble() * 1000 - 500);
			Point p2 = p1.add(new Vector(20, rand.nextDouble() * 20, rand.nextDouble() * 20));
			Point p3 = p1.add(new Vector(rand.nextDouble() * 20, 20, rand.nextDouble() * -20));

			floatingObjects.add(new Triangle(p1, p2, p3).setEmission(new Color(50, 50, 50)).setMaterial(glassMat));
		}

		// 5. Polygones/Carrés (500 objets)
		for (int i = 0; i < 500; i++) {
			double x = rand.nextDouble() * 1600 - 800;
			double y = rand.nextDouble() * 300;
			double z = rand.nextDouble() * 1000 - 500;

			Point p1 = new Point(x, y, z);
			Point p2 = new Point(x + 15, y, z);
			Point p3 = new Point(x + 15, y + 15, z);
			Point p4 = new Point(x, y + 15, z);

			floatingObjects.add(new Polygon(p1, p2, p3, p4).setEmission(new Color(150, 50, 50)).setMaterial(matteMat));
		}

		scene.geometries.add(floatingObjects);
		return scene;
	}

	/**
	 * Utility to run a specific configuration.
	 */
	private void runConfig(String testName, Geometries geometries, boolean useCBR, boolean useAutoBVH,
			int threadsCount) {

		// 1. RESET DES FLAGS POUR ÉVITER QUE LES TESTS SE CONTAMINENT ENTRE EUX !
		Intersectable.setCbrActive(false);
		Geometries.setBvhActive(false);

		Scene scene = buildManualHierarchyScene();
		scene.geometries = geometries; // Inject the specific layout (Flat or Hierarchy)

		// Control mode: If we test Auto Hierarchy WITHOUT CBR, we must force tree
		// building manually
		if (useAutoBVH && !useCBR) {
			scene.geometries.buildBox();
			scene.geometries.buildBVHTree();
		}

		cameraBuilder.setRayTracer(new SimpleRayTracer(scene)).setMultithreading(threadsCount);

		if (useCBR && !useAutoBVH)
			cameraBuilder.enableCBR();
		if (useCBR && useAutoBVH)
			cameraBuilder.enableBVH();

		long startTime = System.currentTimeMillis();

		cameraBuilder.build().renderImage().writeToImage(testName);

		// 2. AFFICHAGE DYNAMIQUE DU NOM DU TEST ET DU TEMPS EXACT
		System.out.println(testName + " : " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
	}

	// ==========================================
	// 1. FLAT SCENE (No hierarchy)
	// ==========================================
	@Test
	void test01_Flat_NoAccel_Single() {
		runConfig("01_Flat_NoAccel_Single", buildManualHierarchyScene().geometries.flatten(), false, false, 0);
	}

	@Test
	void test02_Flat_NoAccel_Multi() {
		runConfig("02_Flat_NoAccel_Multi", buildManualHierarchyScene().geometries.flatten(), false, false, -2);
	}

	@Test
	void test03_Flat_CBR_Single() {
		runConfig("03_Flat_CBR_Single", buildManualHierarchyScene().geometries.flatten(), true, false, 0);
	}

	@Test
	void test04_Flat_CBR_Multi() {
		runConfig("04_Flat_CBR_Multi", buildManualHierarchyScene().geometries.flatten(), true, false, -2);
	}

	// ==========================================
	// 2. MANUAL HIERARCHY (From buildManualHierarchyScene)
	// ==========================================
	@Test
	void test05_Manual_NoAccel_Single() {
		runConfig("05_Manual_NoAccel_Single", buildManualHierarchyScene().geometries, false, false, 0);
	}

	@Test
	void test06_Manual_NoAccel_Multi() {
		runConfig("06_Manual_NoAccel_Multi", buildManualHierarchyScene().geometries, false, false, -2);
	}

	@Test
	void test07_Manual_CBR_Single() {
		runConfig("07_Manual_CBR_Single", buildManualHierarchyScene().geometries, true, false, 0);
	}

	@Test
	void test08_Manual_CBR_Multi() {
		runConfig("08_Manual_CBR_Multi", buildManualHierarchyScene().geometries, true, false, -2);
	}

	// ==========================================
	// 3. AUTO HIERARCHY (Flattened then rebuilt automatically)
	// ==========================================
	@Test
	void test09_Auto_NoAccel_Single() {
		runConfig("09_Auto_NoAccel_Single", buildManualHierarchyScene().geometries.flatten(), false, true, 0);
	}

	@Test
	void test10_Auto_NoAccel_Multi() {
		runConfig("10_Auto_NoAccel_Multi", buildManualHierarchyScene().geometries.flatten(), false, true, -2);
	}

	@Test
	void test11_Auto_BVH_Single() {
		runConfig("11_Auto_BVH_Single", buildManualHierarchyScene().geometries.flatten(), true, true, 0);
	}

	@Test
	void test12_Auto_BVH_Multi() {
		runConfig("12_Auto_BVH_Multi", buildManualHierarchyScene().geometries.flatten(), true, true, -2);
	}
}