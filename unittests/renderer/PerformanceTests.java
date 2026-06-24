package renderer;

import static java.awt.Color.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import geometries.*;
import geometries.impl.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Performance testing for CBR and BVH optimizations (Stage C2).
 * Contains 12 measurements comparing Flat, Manual Hierarchy, and Auto Hierarchy.
 */
class PerformanceTests {

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), Vector.AXIS_Y)
            .setLocation(new Point(0, 0, 1000))
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setResolution(800, 800); // רזולוציה גבוהה למדידה משמעותית

    private Scene buildManualHierarchyScene() {
        // רקע של שמיים כחולים/תכלת
        Scene scene = new Scene("Colorful Ocean BVH").setBackground(new Color(135, 206, 235)); 

        // 1. השמש: מקור אור חזק ולבן גבוה למעלה שמטיל צללים ואור מבריק
        scene.lights.add(new PointLight(new Color(255, 255, 255), new Point(0, 800, 0)).setKl(0.00001).setKq(0.000001));

        // 2. רצפת ה"ים": כדור ענק שמשמש כמשטח משקף (כמו בתמונת ה-Showcase שלך)
        Material waterFloorMat = new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.4);
        scene.geometries.add(new Sphere(new Point(0, -2050, 0), 2000)
                .setEmission(new Color(0, 50, 100)) // כחול ים עמוק
                .setMaterial(waterFloorMat));

        // 3. ה"גלים": אלפי כדורים צבעוניים מפוזרים
        Geometries floatingBalls = new Geometries();
        Random rand = new Random(42);
        
        // חומר פלסטי/זכוכיתי שמבריק בשמש
        Material shinyMat = new Material().setKD(0.4).setKS(0.8).setShininess(80).setKT(0.2);

        for (int i = 0; i < 2000; i++) {
            // פיזור במרחב - מעל הרצפה
            Point p = new Point(rand.nextDouble()*1600 - 800, 
                                rand.nextDouble()*150 - 30, // גובה קרוב למים
                                rand.nextDouble()*1600 - 800);
            
            // צבעים חיים ובוהקים (מינימום 100 כדי שלא ייצאו שחורים/כהים)
            Color vibrantColor = new Color(rand.nextInt(155)+100, rand.nextInt(155)+100, rand.nextInt(155)+100);
            
            floatingBalls.add(new Sphere(p, 12)
                    .setEmission(vibrantColor)
                    .setMaterial(shinyMat));
        }
        
        scene.geometries.add(floatingBalls);
        return scene;
    }
    /**
     * Utility to run a specific configuration.
     */
    private void runConfig(String testName, Geometries geometries, boolean useCBR, boolean useAutoBVH, int threadsCount) {
        Scene scene = buildManualHierarchyScene();
        scene.geometries = geometries; // Inject the specific layout (Flat or Hierarchy)

        // Control mode: If we test Auto Hierarchy WITHOUT CBR, we must force tree building manually,
        // because the architecture normally builds the tree only when CBR is active.
        if (useAutoBVH && !useCBR) {
            scene.geometries.buildBox();
            scene.geometries.buildBVHTree();
        }

        cameraBuilder.setRayTracer(new SimpleRayTracer(scene))
                .setMultithreading(threadsCount);

        if (useCBR && !useAutoBVH) cameraBuilder.enableCBR();
        if (useCBR && useAutoBVH) cameraBuilder.enableBVH();

        cameraBuilder.build()
                .renderImage()
                .writeToImage(testName);
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