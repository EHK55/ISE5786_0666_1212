package scene;

import java.util.ArrayList;
import java.util.List;
import geometries.impl.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

/**
 * Class representing a 3D scene containing geometries, background, ambient light, and external light sources.
 */
public class Scene {
    /** The name of the scene */
    public final String name;
    /** The background color of the scene */
    public Color background = Color.BLACK;
    /** The ambient light of the scene */
    public AmbientLight ambientLight = new AmbientLight();
    /** The geometries in the scene */
    public Geometries geometries = new Geometries();
    /** The external light sources in the scene */
    public final List<LightSource> lights = new ArrayList<>();

    /**
     * Constructor for Scene.
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Set background color (chained).
     * @param background the background color
     * @return the Scene instance
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Set ambient light (chained).
     * @param ambientLight the ambient light
     * @return the Scene instance
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Set geometries (chained).
     * @param geometries the geometries
     * @return the Scene instance
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}