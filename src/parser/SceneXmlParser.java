package parser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import primitives.Color;
import primitives.Point;
import scene.Scene;

/**
 * Class responsible for parsing an XML file to build a Scene object.
 */
public class SceneXmlParser {

	/**
	 * Parses an XML file and initializes the provided Scene.
	 * 
	 * @param scene    The Scene object to populate
	 * @param filePath The path to the XML file
	 * @return The populated Scene
	 */
	public static Scene parse(Scene scene, String filePath) {
		try {
			// 1. Initialize the XML parser (DOM)
			File xmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);

			// Normalize the XML structure
			doc.getDocumentElement().normalize();

			// 2. Get the root element <scene>
			Element root = doc.getDocumentElement();

			// --- A. Background color (background-color) ---
			String bgColorStr = root.getAttribute("background-color");
			if (!bgColorStr.isEmpty()) {
				scene.setBackground(parseColor(bgColorStr));
			}

			// --- B. Ambient light (ambient-light) ---
			NodeList ambientLightNodes = root.getElementsByTagName("ambient-light");
			if (ambientLightNodes.getLength() > 0) {
				Element ambientLightElement = (Element) ambientLightNodes.item(0);
				String colorStr = ambientLightElement.getAttribute("color");
				if (!colorStr.isEmpty()) {
				    scene.setAmbientLight(new AmbientLight(parseColor(colorStr), primitives.Double3.ONE));
				}
			}

			// --- C. Geometries (geometries) ---

			// Parse Spheres
			NodeList sphereNodes = root.getElementsByTagName("sphere");
			for (int i = 0; i < sphereNodes.getLength(); i++) {
				Element sphereElement = (Element) sphereNodes.item(i);
				String centerStr = sphereElement.getAttribute("center");
				String radiusStr = sphereElement.getAttribute("radius");
				if (!centerStr.isEmpty() && !radiusStr.isEmpty()) {
					scene.geometries.add(new Sphere(parsePoint(centerStr), Double.parseDouble(radiusStr)));
				}
			}

			// Parse Triangles
			NodeList triangleNodes = root.getElementsByTagName("triangle");
			for (int i = 0; i < triangleNodes.getLength(); i++) {
				Element triangleElement = (Element) triangleNodes.item(i);
				String p0Str = triangleElement.getAttribute("p0");
				String p1Str = triangleElement.getAttribute("p1");
				String p2Str = triangleElement.getAttribute("p2");
				if (!p0Str.isEmpty() && !p1Str.isEmpty() && !p2Str.isEmpty()) {
					scene.geometries.add(new Triangle(parsePoint(p0Str), parsePoint(p1Str), parsePoint(p2Str)));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error parsing XML file: " + filePath, e);
		}

		return scene;
	}

	/**
	 * Helper method to convert a string like "255 191 191" into a Color object.
	 */
	private static Color parseColor(String colorStr) {
		String[] rgb = colorStr.trim().split("\\s+");
		if (rgb.length == 3) {
			return new Color(Double.parseDouble(rgb[0]), Double.parseDouble(rgb[1]), Double.parseDouble(rgb[2]));
		}
		return Color.BLACK; // Default color in case of error
	}

	/**
	 * Helper method to convert a string like "0 0 -100" into a Point object.
	 */
	private static Point parsePoint(String pointStr) {
		String[] coords = pointStr.trim().split("\\s+");
		if (coords.length == 3) {
			return new Point(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]),
					Double.parseDouble(coords[2]));
		}
		throw new IllegalArgumentException("Invalid point format: " + pointStr);
	}
}