package org.wahlzeit.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * All test cases of the class {@link Coordinate}.
 */
public class CoordinateTest {
    @Test
    public void testGetDistance() {
        // Arrange
        double epsilon = 0.0000000001; // expected accuracy

        Coordinate centerCoordinate = new Coordinate(0.0, 0.0, 0.0); // center of coordinate system
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        Coordinate otherCoordinate = new Coordinate(x, y, z);

        double expectedDistance = 11.78982612255159596846918375135848942610804994310659548714592171; // sqrt(3*3 + 7*7 + 9*9)

        // Act
        double actualDistance = centerCoordinate.getDistance(otherCoordinate);

        // Assert
        Assert.assertEquals(expectedDistance, actualDistance, epsilon);
    }

    @Test
    public void testEquals_DoubleRoundingErrors() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        Coordinate actual = new Coordinate(x, y, z);

        // Create one other Coordinate with values of x, y and z almost off by epsilon.
        // EPSILON is being used by Coordinate.isEqual() to accept rounding double inaccuracies.
        double epsilon = Coordinate.EPSILON * 0.95;
        double xWithError = x + epsilon;
        double yWithError = y + epsilon;
        double zWithError = z + epsilon;
        Coordinate other = new Coordinate(xWithError, yWithError, zWithError);

        assertEquals(actual, other);
    }

    @Test
    public void testEquals_RandomDoubleError() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        Coordinate actual = new Coordinate(x, y, z);

        // Create 10 other Coordinates and randomly add an error of at most 0.95 times epsilon to x, y and z.
        // These randomly different coordinates shall all be equal with the actual coordinate.
        // EPSILON is being used by Coordinate.isEqual() to accept rounding double inaccuracies.
        double epsilon = Coordinate.EPSILON * 0.95;
        for (int i = 0; i < 10; i++) {
            double xWithError = x + Math.random() * epsilon;
            double yWithError = y + Math.random() * epsilon;
            double zWithError = z + Math.random() * epsilon;
            Coordinate other = new Coordinate(xWithError, yWithError, zWithError);
            assertEquals(actual, other);
        }
    }

    @Test
    public void testEquals_notEqual() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        Coordinate actual = new Coordinate(x, y, z);

        double tooLargeError = 0.1;
        Coordinate otherX = new Coordinate(x + tooLargeError, y, z);
        Coordinate otherY = new Coordinate(x, y + tooLargeError, z);
        Coordinate otherZ = new Coordinate(x, y, z + tooLargeError);

        double slightlyTooLargeError = Coordinate.EPSILON * 1.001;
        Coordinate otherBX = new Coordinate(x + slightlyTooLargeError, y, z);
        Coordinate otherBY = new Coordinate(x, y + slightlyTooLargeError, z);
        Coordinate otherBZ = new Coordinate(x, y, z + slightlyTooLargeError);

        assertNotEquals(actual, otherX);
        assertNotEquals(actual, otherY);
        assertNotEquals(actual, otherZ);

        assertNotEquals(actual, otherBX);
        assertNotEquals(actual, otherBY);
        assertNotEquals(actual, otherBZ);    }
}
