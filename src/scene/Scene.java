package scene;

import geometries.impl.Geometries;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {

	public String name;
	public Color background = Color.BLACK;
	public AmbientLight ambientLight = AmbientLight.NONE;
	public Geometries geometries = new Geometries();

	public Scene(String n) {
		this.name = n;
	}

	public Scene setBackground(Color b) {
		this.background = b;
		return this;
	}

	public Scene setAmbientLight(AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
		return this;
	}

	public Scene setGeometries(Geometries geometries) {
		this.geometries = geometries;
		return this;
	}
}
