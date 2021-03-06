package org.wahlzeit.model.location;

import org.junit.Test;
import org.wahlzeit.utils.DoubleUtil;

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.wahlzeit.model.location.AbstractCoordinate.SCALE;
import static org.wahlzeit.model.location.AbstractCoordinateTest.accuracy;
import static org.wahlzeit.model.location.AbstractCoordinateTest.orthogonalLocations;
import static org.wahlzeit.model.location.SphericalCoordinate.newSphericalCoordinate;

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

    @Test(expected = IllegalArgumentException.class)
    public void test_newSphericalArguments() {
        double radius = -1.0; // smaller than zero
        double theta = 0.0;
        double phi = 0.0;
        SphericalCoordinate spherical = newSphericalCoordinate(radius, theta, phi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_newSphericalArguments2() {
        double radius = 0.0;
        double theta = -1; // smaller than zero
        double phi = 0.0;
        SphericalCoordinate spherical = newSphericalCoordinate(radius, theta, phi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_newSphericalArguments3() {
        double radius = 0.0;
        double theta = PI * 1.1; // larger than PI
        double phi = 0.0;
        SphericalCoordinate spherical = newSphericalCoordinate(radius, theta, phi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_newSphericalArguments4() {
        double radius = 0.0;
        double theta = 0.0;
        double phi = Double.NaN;
        SphericalCoordinate spherical = newSphericalCoordinate(radius, theta, phi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_newSphericalArguments5() {
        double radius = 0.0;
        double theta = 0.0;
        double phi = Double.POSITIVE_INFINITY;
        SphericalCoordinate spherical = newSphericalCoordinate(radius, theta, phi);
    }

    @Test
    public void test_newSpherical() {
        double radius = 1.0;
        double theta = 1.1;
        double phi = 1.2;
        SphericalCoordinate spherical = newSphericalCoordinate(radius, theta, phi);

        assertEquals(radius, spherical.getRadius(), accuracy);
        assertEquals(theta, spherical.getTheta(), accuracy);
        assertEquals(phi, spherical.getPhi(), accuracy);
    }

    @Test
    public void test_newSpherical1() {
        double radius = 1.0;
        double theta = 1.1;
        double phi = 1.2;
        SphericalCoordinate expected = newSphericalCoordinate(radius, theta, phi);

        SphericalCoordinate actual = expected.asSphericalCoordinate();

        assertEqualSpherical(expected, actual);
    }

    @Test
    public void test_newSpherical2() {
        for (LocationTuple location : AbstractCoordinateTest.locations) {
            SphericalCoordinate expected = location.spherical;

            SphericalCoordinate actual = expected.asSphericalCoordinate();

            assertEqualSpherical(expected, actual);
        }
    }

    @Test
    public void test_centralAngle() {
        // a, b and c are all "orthogonal" to each other (central angle of 90°)
        SphericalCoordinate a = orthogonalLocations[0].spherical;
        SphericalCoordinate b = orthogonalLocations[1].spherical;
        SphericalCoordinate c = orthogonalLocations[2].spherical;
        double halfPi = PI / 2.0;

        assertEquals(halfPi, a.getCentralAngle(b), accuracy);
        assertEquals(halfPi, a.getCentralAngle(c), accuracy);
        assertEquals(halfPi, b.getCentralAngle(a), accuracy);
        assertEquals(halfPi, b.getCentralAngle(c), accuracy);
        assertEquals(halfPi, c.getCentralAngle(a), accuracy);
        assertEquals(halfPi, c.getCentralAngle(b), accuracy);
    }
}
