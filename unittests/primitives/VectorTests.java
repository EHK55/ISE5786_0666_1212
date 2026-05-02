package primitives;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for primitives.Vector class.
 * This class ensures the mathematical correctness of vector operations 
 * such as addition, subtraction, dot product, cross product, and normalization.
 */
class VectorTests {

    /**
     * Delta value for accuracy when comparing double values in tests.
     */
    private static final double DELTA = 0.000001;

    /**
     * Default constructor for VectorTests to satisfy Javadoc requirements.
     */
    public VectorTests() {}

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test adding two vectors
        Vector result = v1.add(v2);
        assertEquals(new Vector(-1, -2, -3), result, "ERROR: Vector + Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test adding a vector to its opposite
        Vector v1Opposite = new Vector(-1, -2, -3);
        assertThrows(IllegalArgumentException.class, () -> v1.add(v1Opposite),
                "ERROR: Vector + opposite vector does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#subtract(primitives.Vector)}.
     */
    @Test
    void testSubtract() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test subtracting two vectors
        Vector result = v1.subtract(v2);
        assertEquals(new Vector(3, 6, 9), result, "ERROR: Vector - Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test subtracting a vector from itself
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v1),
                "ERROR: Vector - itself does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test dot-product of two vectors
        assertEquals(-28, v1.dotProduct(v2), DELTA, "ERROR: dotProduct() wrong value");

        // =============== Boundary Values Tests ==================
        // TC11: Test dot-product of orthogonal vectors
        assertEquals(0, v1.dotProduct(v3), DELTA, "ERROR: dotProduct() for orthogonal vectors is not zero");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(0, 3, -2);
        Vector v3 = new Vector(-2, -4, -6);

        // ============ Equivalence Partitions Tests ==============
        Vector vr = v1.crossProduct(v2);

        // TC01: Test cross-product result length
        assertEquals(v1.length() * v2.length(), vr.length(), DELTA, "ERROR: crossProduct() wrong result length");

        // TC02: Test cross-product result orthogonality to its operands
        assertEquals(0, vr.dotProduct(v1), DELTA, "ERROR: crossProduct() result is not orthogonal to its 1st operand");
        assertEquals(0, vr.dotProduct(v2), DELTA, "ERROR: crossProduct() result is not orthogonal to its 2nd operand");

        // =============== Boundary Values Tests ==================
        // TC11: Test cross-product of parallel vectors
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v3),
                "ERROR: crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        Vector v = new Vector(0, 3, 4);
        Vector n = v.normalize();

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test normalize a vector
        assertEquals(1d, n.length(), DELTA, "ERROR: the normalized vector is not a unit vector");

        // TC02: Test that the normalized vector is parallel to the original one
        assertThrows(IllegalArgumentException.class, () -> v.crossProduct(n),
                "ERROR: the normalized vector is not parallel to the original one");

        // TC03: Test that the normalized vector is in the same direction
        assertTrue(v.dotProduct(n) > 0, "ERROR: the normalized vector is opposite to the original one");
    }
}