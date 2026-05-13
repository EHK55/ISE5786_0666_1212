package primitives;

/**
 * PDS (Passive Data Structure) representing the material of a geometry.
 */
public class Material {

	/** Ambient light attenuation coefficient, initialized to 1 by default */
	public Double3 kA = Double3.ONE;

	/**
	 * Setter for kA (Builder pattern chaining) * @param kA the attenuation
	 * coefficient as a Double3
	 * 
	 * @return the Material object itself
	 */
	public Material setKA(Double3 kA) {
		this.kA = kA;
		return this;
	}

	/**
	 * Setter for kA (Builder pattern chaining) using a single double * @param kA
	 * the attenuation coefficient as a double
	 * 
	 * @return the Material object itself
	 */
	public Material setKA(double kA) {
		this.kA = new Double3(kA);
		return this;
	}
}