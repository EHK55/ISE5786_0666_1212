package primitives;

/**
 * Material class representing the physical properties of a geometry's surface.
 * This class uses a builder-like fluent API.
 */
public class Material {
    /** Attenuation coefficient of ambient light */
    public Double3 kA = new Double3(1.0, 1.0, 1.0);
    /** Attenuation coefficient of diffuse reflection */
    public Double3 kD = new Double3(0.0, 0.0, 0.0);
    /** Attenuation coefficient of specular reflection */
    public Double3 kS = new Double3(0.0, 0.0, 0.0);
    /** Shininess exponent of the material */
    public int nShininess = 0;

    /**
     * Default constructor for Material.
     */
    public Material() {}

    /**
     * Setter for kA using Double3 (chained).
     * @param kA the ambient light attenuation coefficient
     * @return this Material instance
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Setter for kA using double (chained).
     * @param kA the ambient light attenuation coefficient
     * @return this Material instance
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA, kA, kA);
        return this;
    }

    /**
     * Setter for kD using Double3 (chained).
     * @param kD the diffuse reflection coefficient
     * @return this Material instance
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for kD using double (chained).
     * @param kD the diffuse reflection coefficient
     * @return this Material instance
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD, kD, kD);
        return this;
    }

    /**
     * Setter for kS using Double3 (chained).
     * @param kS the specular reflection coefficient
     * @return this Material instance
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for kS using double (chained).
     * @param kS the specular reflection coefficient
     * @return this Material instance
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS, kS, kS);
        return this;
    }

    /**
     * Setter for shininess (chained).
     * @param nShininess the shininess exponent
     * @return this Material instance
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}