package org.wahlzeit.model.location;

import org.junit.Test;


import java.util.*;
import java.util.stream.DoubleStream;

import static java.lang.Math.PI;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.wahlzeit.model.location.CartesianCoordinateTest.assertEqualCartesian;
import static org.wahlzeit.model.location.SphericalCoordinateTest.assertEqualSpherical;

public class AbstractCoordinateTest {
    public static final double accuracy = Math.pow(10, -AbstractCoordinate.SCALE);

    // Array of cartesian coordinates and their spherical equivalent.
    // This is used to verify results of converted coordinates
    public static final LocationTuple[] locations = new LocationTuple[]{
            new LocationTuple(new CartesianCoordinate(0.0, 0.0, 0.0),
                    new SphericalCoordinate(0.0, 0.0, 0.0)),
            new LocationTuple(new CartesianCoordinate(1.0, 0.0, 0.0),
                    new SphericalCoordinate(1.0, PI / 2.0, 0.0)),
            new LocationTuple(new CartesianCoordinate(0.0, 1.0, 0.0),
                    new SphericalCoordinate(1.0, PI / 2.0, PI / 2.0)),
            new LocationTuple(new CartesianCoordinate(0.0, 0.0, 1.0),
                    new SphericalCoordinate(1.0, 0.0, 0.0)),
            // LocationTuple with x=-1,y=0,z=0 excluded a special case
            new LocationTuple(new CartesianCoordinate(0.0, -1.0, 0.0),
                    new SphericalCoordinate(1.0, PI / 2.0, -PI / 2.0)),
            new LocationTuple(new CartesianCoordinate(0.0, 0.0, -1.0),
                    new SphericalCoordinate(1.0, PI, 0.0)),
    };

    // The three different points in space are all "orthogonal" to each other (central angle of 90°)
    public static final LocationTuple[] orthogonalLocations = new LocationTuple[]{
            new LocationTuple(new CartesianCoordinate(1.0, 0.0, 0.0),
                    new SphericalCoordinate(1.0, PI / 2.0, 0.0)),
            new LocationTuple(new CartesianCoordinate(0.0, 1.0, 0.0),
                    new SphericalCoordinate(1.0, PI / 2.0, PI / 2.0)),
            new LocationTuple(new CartesianCoordinate(0.0, 0.0, 1.0),
                    new SphericalCoordinate(1.0, 0.0, 0.0)),
    };

    @Test
    public void test_atan2VsAtan() {
        double x, y;
        double maybeWrongPhi, correctPhi;
        double delta = 0.000001;

        /*
         * Demonstration that one should prefer the usage of atan2() over atan()
         * when converting cartesian coordinates (x,y) to polar coordinates (r,phi).
         *
         * We draw two halves of the unit circle.
         * The first one has positive y values and should thus result in
         * positive values of phi
         * The second one has negative y values and should thus result in
         * negative values of phi
         *
         * atan2 handles the sign correctly whereas one would need to check for
         * special cases manually when using atan. On top of that atan2 gives
         * more precise results.
         */

        // draw first half of a circle with radius one around coordinate center
        for (double t = 0.0 + delta; t < PI - delta; t += 0.1) {
            x = Math.cos(t);
            y = Math.sin(t);
            maybeWrongPhi = Math.atan(y / x);
            correctPhi = Math.atan2(y, x);

            System.out.println("(x,y) = (" + x + "," + y + ")");
            System.out.println("\tatan:  " + maybeWrongPhi);
            System.out.println("\tatan2: " + correctPhi);
            assertTrue(correctPhi > 0.0);

            if (t > PI / 2 + delta) {
                // the signs do actually differ!
                assertNotEquals(Math.signum(maybeWrongPhi), Math.signum(correctPhi), accuracy);
            }
        }
        // draw second half of a circle with radius one around coordinate center
        for (double t = PI + delta; t < PI * 2 - delta; t += 0.1) {
            x = Math.cos(t);
            y = Math.sin(t);
            maybeWrongPhi = Math.atan(y / x);
            correctPhi = Math.atan2(y, x);

            System.out.println("(x,y) = (" + x + "," + y + ")");
            System.out.println("\tatan:  " + maybeWrongPhi);
            System.out.println("\tatan2: " + correctPhi);
            assertTrue(correctPhi < 0.0);

            if (t < 3 * PI / 2 + delta) {
                // the signs do actually differ!
                assertNotEquals(Math.signum(maybeWrongPhi), Math.signum(correctPhi), accuracy);
            }
        }
    }

    /*
     * conversion tests:
     * cartesian -> spherical -> cartesian
     */

    @Test
    public void test_CartesianToSpherical() {
        for (LocationTuple location : locations) {
            SphericalCoordinate expected = location.spherical;
            CartesianCoordinate cartesian = location.cartesian;

            // ACT
            SphericalCoordinate converted = cartesian.asSphericalCoordinate();

            String message = "Converting " + cartesian + " to spherical coordinate failed!";
            assertEqualSpherical(message, expected, converted);
        }
    }

    @Test
    public void test_cartesianToSpherical_specialCase() {
        CartesianCoordinate cartesian = new CartesianCoordinate(-1.0, 0.0, 0.0);
        SphericalCoordinate converted = cartesian.asSphericalCoordinate();

        // expected:
        // radius = 1
        // polar angle = pi/2
        // azimuth = +/- pi    // both vales are ok -> assert absolute value is correct
        SphericalCoordinate expectedA = new SphericalCoordinate(1.0, PI / 2.0, PI);
        SphericalCoordinate expectedB = new SphericalCoordinate(1.0, PI / 2.0, -1.0 * PI);

        assertTrue(expectedA.equals(converted) || expectedB.equals(converted));
        assertEquals(1.0, converted.getRadius(), accuracy);
        assertEquals(PI / 2.0, converted.getTheta(), accuracy);
        assertEquals(PI, Math.abs(converted.getPhi()), accuracy);
    }

    @Test
    public void test_sphericalToCartesian() {
        for (LocationTuple location : locations) {
            SphericalCoordinate spherical = location.spherical;
            CartesianCoordinate expected = location.cartesian;

            // ACT
            CartesianCoordinate converted = spherical.asCartesianCoordinate();

            String message = "Converting " + spherical + " to cartesian coordinate failed!";
            assertEqualCartesian(message, expected, converted);
        }
    }

    @Test
    public void test_sphericalToCartesian_specialCase() {
        double radius = 1.0;
        double theta = PI / 2.0;
        double phi = PI;

        SphericalCoordinate sphericalA = new SphericalCoordinate(radius, theta, phi);
        CartesianCoordinate convertedA = sphericalA.asCartesianCoordinate();

        SphericalCoordinate sphericalB = new SphericalCoordinate(radius, theta, -1.0 * phi);
        CartesianCoordinate convertedB = sphericalB.asCartesianCoordinate();


        // expected:
        CartesianCoordinate expected = new CartesianCoordinate(-1.0, 0.0, 0.0);


        CartesianCoordinateTest.assertEqualCartesian(expected, convertedA);
        CartesianCoordinateTest.assertEqualCartesian(expected, convertedB);
    }

    @Test
    public void test_centralAngle_orthogonal() {
        double expected = PI / 2.0; // 90° -> orthogonal

        test_centralAngle_Helper(orthogonalLocations[0], orthogonalLocations[1], expected);
        test_centralAngle_Helper(orthogonalLocations[0], orthogonalLocations[2], expected);
        test_centralAngle_Helper(orthogonalLocations[1], orthogonalLocations[0], expected);
        test_centralAngle_Helper(orthogonalLocations[1], orthogonalLocations[2], expected);
        test_centralAngle_Helper(orthogonalLocations[2], orthogonalLocations[0], expected);
        test_centralAngle_Helper(orthogonalLocations[2], orthogonalLocations[1], expected);
    }

    protected void test_centralAngle_Helper(LocationTuple a, LocationTuple b, double expectedAngle) {
        double actual1 = a.cartesian.getCentralAngle(b.spherical);
        double actual2 = a.spherical.getCentralAngle(b.cartesian);

        String message1 = "centralAngle of " + a.cartesian + " and " + b.spherical + " wrong";
        assertEquals(message1, expectedAngle, actual1, accuracy);
        String message2 = "centralAngle of " + a.spherical + " and " + b.cartesian + " wrong";
        assertEquals(message2, expectedAngle, actual2, accuracy);
    }
}
