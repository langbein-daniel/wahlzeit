package org.wahlzeit.model.location;

import org.junit.Test;
import org.wahlzeit.utils.DoubleUtil;

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.wahlzeit.model.location.AbstractCoordinate.SCALE;
import static org.wahlzeit.model.location.AbstractCoordinateTest.accuracy;
import static org.wahlzeit.model.location.AbstractCoordinateTest.orthogonalLocations;

public class SphericalCoordinateTest {
    public static void assertEqualSpherical(SphericalCoordinate expected, SphericalCoordinate actual) {
        assertEquals(expected, actual);
        assertTrue(DoubleUtil.isEqual(expected.getRadius(), actual.getRadius(), SCALE));
        assertTrue(DoubleUtil.isEqual(expected.getTheta(), actual.getTheta(), SCALE));
        assertTrue(DoubleUtil.isEqual(expected.getPhi(), actual.getPhi(), SCALE));
    }

    public static void assertEqualSpherical(String message, SphericalCoordinate expected, SphericalCoordinate actual) {
        assertEquals(message, expected, actual);
        assertTrue(message, DoubleUtil.isEqual(expected.getRadius(), actual.getRadius(), SCALE));
        assertTrue(message, DoubleUtil.isEqual(expected.getTheta(), actual.getTheta(), SCALE));
        assertTrue(message, DoubleUtil.isEqual(expected.getPhi(), actual.getPhi(), SCALE));
    }

    @Test
    public void test_newSpherical() {
        double radius = 1.0;
        double theta = 1.1;
        double phi = 1.2;
        SphericalCoordinate spherical = new SphericalCoordinate(radius, theta, phi);

        assertEquals(radius, spherical.getRadius(), accuracy);
        assertEquals(theta, spherical.getTheta(), accuracy);
        assertEquals(phi, spherical.getPhi(), accuracy);
    }

    @Test
    public void test_newSpherical1() {
        double radius = 1.0;
        double theta = 1.1;
        double phi = 1.2;
        SphericalCoordinate expected = new SphericalCoordinate(radius, theta, phi);

        SphericalCoordinate actual = new SphericalCoordinate(expected);

        assertEqualSpherical(expected, actual);
    }

    @Test
    public void test_newSpherical2() {
        for (LocationTuple location : AbstractCoordinateTest.locations) {
            SphericalCoordinate expected = location.spherical;

            SphericalCoordinate actual = new SphericalCoordinate(expected);

            assertEqualSpherical(expected, actual);
        }
    }

    @Test
    public void test_centralAngle() {
        // a, b and c are all "orthogonal" to each other (central angle of 90°)
        SphericalCoordinate a = orthogonalLocations[0].spherical;
        SphericalCoordinate b = orthogonalLocations[1].spherical;
        SphericalCoordinate c = orthogonalLocations[2].spherical;
        double halfPi = Math.PI / 2.0;

        assertEquals(halfPi, a.getCentralAngle(b), accuracy);
        assertEquals(halfPi, a.getCentralAngle(c), accuracy);
        assertEquals(halfPi, b.getCentralAngle(a), accuracy);
        assertEquals(halfPi, b.getCentralAngle(c), accuracy);
        assertEquals(halfPi, c.getCentralAngle(a), accuracy);
        assertEquals(halfPi, c.getCentralAngle(b), accuracy);
    }
}
