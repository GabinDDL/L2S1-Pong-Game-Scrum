package model.game_elements;

import model.Vector2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestVector2 {

    // Constructors

    /**
     * Tests {@link Vector2#Vector2(double, double)}
     */
    @Test
    void testConstructorCoordinates() {
        Vector2 v = new Vector2(3 / 5., -5. / 6);

        assertEquals(3 / 5., v.getXdir(), 1E-9);
        assertEquals(-5 / 6., v.getYdir(), 1E-9);
    }

    /**
     * Tests {@link Vector2#Vector2(Vector2)}
     */
    @Test
    void testConstructorVectorCopy() {
        Vector2 v1 = new Vector2(3 / 5., -5. / 6);
        Vector2 v2 = new Vector2(v1);

        assertTrue(v1 != v2);
        assertEquals(3 / 5., v2.getXdir(), 1E-9);
        assertEquals(-5 / 6., v2.getYdir(), 1E-9);
    }

    // Getters

    // Norm

    /**
     * Tests {@link Vector2#getNorm()}
     */
    @Test
    void testGetNorm() {
        Vector2 v = new Vector2(3 / 7., -5. / 6);
        double norm = v.getNorm();
        double calc = Math.sqrt(
                v.getXdir() * v.getXdir() + v.getYdir() * v.getYdir());

        assertEquals(calc, norm, 1E-9);
    }

    // Angle

    /**
     * Tests {@link Vector2#getAngle()}
     */
    @Test
    void testGetAngle() {
        Vector2 v = new Vector2(3 / 7., -5. / 6);
        double angle = v.getAngle();
        double calc = Math.atan2(v.getYdir(), v.getXdir());

        assertEquals(calc, angle, 1E-9);
    }

    // Setters

    // Direction

    /**
     * Tests {@link Vector2#setDirection(double, double)}
     */
    @Test
    void testSetDirectionCoordinates() {
        Vector2 v = new Vector2(3.5, 4.6);
        v.setDirection(1, 2);

        assertEquals(1, v.getXdir(), 1E-9);
        assertEquals(2, v.getYdir(), 1E-9);
    }

    /**
     * Tests {@link Vector2#setDirection(Vector2)}
     */
    @Test
    void testSetDirectionVector() {
        Vector2 v1 = new Vector2(3.5, 4.6);
        Vector2 v2 = new Vector2(3, 4);
        v1.setDirection(v2);

        assertEquals(3, v1.getXdir(), 1E-9);
        assertEquals(4, v1.getYdir(), 1E-9);
    }

    // Norm

    /**
     * Tests {@link Vector2#setNorm(double)}
     */
    @Test
    void testSetNorm() {
        Vector2 v = new Vector2(3, 4);
        v.setNorm(2.5);

        assertEquals(1.5, v.getXdir(), 1E-9);
        assertEquals(2, v.getYdir(), 1E-9);

    }

    /**
     * Tests {@link Vector2#add(double, double)}
     */
    @Test
    void testAddCoordinates() {
        Vector2 v = new Vector2(3, 4);
        v.add(4, 5);

        assertEquals(7, v.getXdir(), 1E-9);
        assertEquals(9, v.getYdir(), 1E-9);
    }

    /**
     * Tests {@link Vector2#add(Vector2)}
     */
    @Test
    void testAddVector() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(-4 / 3., -6 / 5.);
        v1.add(v2);

        assertEquals(5 / 3., v1.getXdir(), 1E-9);
        assertEquals(14 / 5., v1.getYdir(), 1E-9);
    }

    /**
     * Tests {@link Vector2#scalarMultiplication(double)}
     */
    @Test
    void testScalarMultiplication() {
        Vector2 v = new Vector2(4 / 3., 7 / 3.);
        v.scalarMultiplication(3);

        assertEquals(4, v.getXdir(), 1E-9);
        assertEquals(7, v.getYdir(), 1E-9);
    }

    /**
     * Tests {@link Vector2#normalise()}
     */
    @Test
    void testNormalise() {
        Vector2 v = new Vector2(4 / 3., 7 / 3.);
        double calc = Math.sqrt(
                v.getXdir() * v.getXdir() + v.getYdir() * v.getYdir());
        v.normalise();

        assertEquals(4 / 3. / calc, v.getXdir(), 1E-9);
        assertEquals(7 / 3. / calc, v.getYdir(), 1E-9);
    }

    /**
     * Tests {@link Vector2#updateDistanceVector(Vector2, double)}
     */
    @Test
    void testUpdateDistanceVector() {
        Vector2 v = new Vector2(2 / 3., 4 / 3.);
        v.updateDistanceVector(v, 2);

        assertEquals(2, v.getXdir(), 1E-9);
        assertEquals(4, v.getYdir(), 1E-9);
    }

    /**
     * Tests {@link Vector2#addAngle(double)}
     */
    @Test
    void testAddAngle() {
        double angle = Math.PI / 6;
        Vector2 v = new Vector2(Math.cos(angle), Math.sin(angle));

        v.addAngle(angle);

        assertEquals(0.5, v.getXdir(), 1E-9);
        assertEquals(Math.sqrt(3) / 2, v.getYdir(), 1E-9);

    }

    /**
     * Tests {@link Vector2#addAngleRestricted(double)}
     */
    @Test
    void testAddAngleRestricted() {
        double angle = Math.PI / 6.;
        double angleMax = Vector2.getAngleMax();

        // In invalid Quadrant

        // In invalid Top-Right Quadrant
        Vector2 v1 = new Vector2(
                Math.cos(angle),
                Math.sin(angle));

        assertEquals(Math.sqrt(3) / 2, v1.getXdir(), 1E-9);
        assertEquals(0.5, v1.getYdir(), 1E-9);

        v1.addAngleRestricted(angle);

        assertEquals(Math.cos(angleMax), v1.getXdir(), 1E-9);
        assertEquals(Math.sin(angleMax), v1.getYdir(), 1E-9);

        // In invalid Top-Left Quadrant
        Vector2 v2 = new Vector2(
                Math.cos(Math.PI - angle),
                Math.sin(Math.PI - angle));

        assertEquals(-Math.sqrt(3) / 2, v2.getXdir(), 1E-9);
        assertEquals(0.5, v2.getYdir(), 1E-9);

        v2.addAngleRestricted(-angle);

        assertEquals(Math.cos(3 * angleMax), v2.getXdir(), 1E-9);
        assertEquals(Math.sin(3 * angleMax), v2.getYdir(), 1E-9);

        // In invalid Bottom-Right Quadrant
        Vector2 v3 = new Vector2(
                Math.cos(-angle),
                Math.sin(-angle));

        assertEquals(Math.sqrt(3) / 2, v3.getXdir(), 1E-9);
        assertEquals(-0.5, v3.getYdir(), 1E-9);

        v3.addAngleRestricted(-angle);

        assertEquals(Math.cos(-angleMax), v3.getXdir(), 1E-9);
        assertEquals(Math.sin(-angleMax), v3.getYdir(), 1E-9);

        // In invalid Bottom-Left Quadrant
        Vector2 v4 = new Vector2(
                Math.cos(-Math.PI + angle),
                Math.sin(-Math.PI + angle));

        assertEquals(-Math.sqrt(3) / 2, v4.getXdir(), 1E-9);
        assertEquals(-0.5, v4.getYdir(), 1E-9);

        v4.addAngleRestricted(angle);

        assertEquals(Math.cos(3 * -angleMax), v4.getXdir(), 1E-9);
        assertEquals(Math.sin(3 * -angleMax), v4.getYdir(), 1E-9);

        // In valid Quadrant

        // In valid Top-Right Quadrant
        Vector2 v5 = new Vector2(
                Math.cos(angle),
                Math.sin(angle));

        assertEquals(Math.sqrt(3) / 2, v5.getXdir(), 1E-9);
        assertEquals(0.5, v5.getYdir(), 1E-9);

        v5.addAngleRestricted(0);

        assertEquals(Math.cos(angle), v5.getXdir(), 1E-9);
        assertEquals(Math.sin(angle), v5.getYdir(), 1E-9);

        // In valid Top-Left Quadrant
        Vector2 v6 = new Vector2(
                Math.cos(Math.PI - angle),
                Math.sin(Math.PI - angle));

        assertEquals(-Math.sqrt(3) / 2, v6.getXdir(), 1E-9);
        assertEquals(0.5, v6.getYdir(), 1E-9);

        v6.addAngleRestricted(0);

        assertEquals(Math.cos(5 * Math.PI / 6), v6.getXdir(), 1E-9);
        assertEquals(Math.sin(5 * Math.PI / 6), v6.getYdir(), 1E-9);

        // In valid Bottom-Right Quadrant
        Vector2 v7 = new Vector2(
                Math.cos(-angle),
                Math.sin(-angle));

        assertEquals(Math.sqrt(3) / 2, v7.getXdir(), 1E-9);
        assertEquals(-0.5, v7.getYdir(), 1E-9);

        v7.addAngleRestricted(0);

        assertEquals(Math.cos(-angle), v7.getXdir(), 1E-9);
        assertEquals(Math.sin(-angle), v7.getYdir(), 1E-9);

        // In valid Bottom-Left Quadrant
        Vector2 v8 = new Vector2(
                Math.cos(-Math.PI + angle),
                Math.sin(-Math.PI + angle));

        assertEquals(-Math.sqrt(3) / 2, v8.getXdir(), 1E-9);
        assertEquals(-0.5, v8.getYdir(), 1E-9);

        v8.addAngleRestricted(0);

        assertEquals(Math.cos(-5 * Math.PI / 6), v8.getXdir(), 1E-9);
        assertEquals(Math.sin(-5 * Math.PI / 6), v8.getYdir(), 1E-9);

    }

}