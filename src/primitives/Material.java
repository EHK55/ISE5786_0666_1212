package primitives;

/**
 * Material class representing the physical properties of a geometry's surface.
 */
public class Material {
    /** Attenuation coefficient of ambient light */
    public Double3 kA = new Double3(1.0, 1.0, 1.0);
    /** Attenuation coefficient of diffuse reflection */
    public Double3 kD = new Double3(0.0, 0.0, 0.0);
    /** Attenuation coefficient of specular reflection */
    public Double3 kS = new Double3(0.0, 0.0, 0.0);
    /** Attenuation coefficient of transparency */
    public Double3 kT = Double3.ZERO;
    /** Attenuation coefficient of reflection */
    public Double3 kR = Double3.ZERO;
    /** Shininess exponent of the material */
    public int nShininess = 0;

    /**
     * Default constructor for Material.
     */
    public Material() {}

    /**
     * Setter for kA using Double3.
     * @param kA the ambient light attenuation coefficient
     * @return this Material instance
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Setter for kA using double.
     * @param kA the ambient light attenuation coefficient
     * @return this Material instance
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    /**
     * Setter for kD using Double3.
     * @param kD the diffuse reflection coefficient
     * @return this Material instance
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for kD using double.
     * @param kD the diffuse reflection coefficient
     * @return this Material instance
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Setter for kS using Double3.
     * @param kS the specular reflection coefficient
     * @return this Material instance
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for kS using double.
     * @param kS the specular reflection coefficient
     * @return this Material instance
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Setter for kT using Double3.
     * @param kT the transparency attenuation coefficient
     * @return this Material instance
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Setter for kT using double.
     * @param kT the transparency attenuation coefficient
     * @return this Material instance
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Setter for kR using Double3.
     * @param kR the reflection attenuation coefficient
     * @return this Material instance
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Setter for kR using double.
     * @param kR the reflection attenuation coefficient
     * @return this Material instance
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Setter for shininess.
     * @param nShininess the shininess exponent
     * @return this Material instance
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
    
    
}