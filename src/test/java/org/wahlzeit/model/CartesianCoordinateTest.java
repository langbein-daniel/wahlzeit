package org.wahlzeit.model;

import org.junit.Assert;
import org.junit.Test;
import org.wahlzeit.model.location.AbstractCoordinate;
import org.wahlzeit.model.location.CartesianCoordinate;

import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.stream.DoubleStream;

import static org.junit.Assert.*;

/**
 * All test cases of the class {@link CartesianCoordinate}.
 */
public class CartesianCoordinateTest {
    @Test
    public void testGetCartesianDistance() {
        // minimum expected accuracy
        double epsilon = 0.0000000001;

        // center of coordinate system
        CartesianCoordinate centerCartesianCoordinate = new CartesianCoordinate(0.0, 0.0, 0.0);
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate otherCartesianCoordinate = new CartesianCoordinate(x, y, z);

        // manually calculated distance:
        // sqrt(3*3 + 7*7 + 9*9)
        double expectedDistance = 11.78982612255159596846918375135848942610804994310659548714592171;

        // Act
        double actualDistance = centerCartesianCoordinate.getCartesianDistance(otherCartesianCoordinate);

        // Assert
        Assert.assertEquals(expectedDistance, actualDistance, epsilon);
    }

    @Test
    public void testIsEqual_ErrorRelativeToScale() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate actual = new CartesianCoordinate(x, y, z);

        // SCALE number of digits to the right of the decimal point of
        // double values from two coordinates shall be equal for the
        // coordinates to count as equal
        // Thus we add one 10th of that as an error to each dimension of
        // the coordinate => 10 ^ (-SCALE) * 1/10 == 10 ^ (-SCALE -1)
        double error = 0.1 * Math.pow(10, -AbstractCoordinate.SCALE );
        System.out.println(error);

        double xWithError = x + error;
        double yWithError = y + error;
        double zWithError = z + error;
        CartesianCoordinate other = new CartesianCoordinate(xWithError, yWithError, zWithError);

        assertEquals(actual, other);
    }

    @Test
    public void testIsEquals_RandomError() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate actual = new CartesianCoordinate(x, y, z);

        // how many other Coordinates with random errors shall be compared with the actual Coordinate
        int loopCount = 100;

        // Theoretically one could take 0.4999 * 10^(-SCALE) as an error that would still be ignored.
        // (Because of rounding half up)
        // But as AbstractCoordinate.isEqual() does some calculation internally (it calculates the distance)
        // we need a bit mor free room, thus we choose a smaller error: 0.11 * 10^(-SCALE)
        double error = 0.11 * Math.pow(10, -AbstractCoordinate.SCALE);

        Random r = new Random();
        // random double values between -1 and +1
        // we need three of those in each loop
        DoubleStream randomDoubles = r.doubles(3*loopCount,-1.0, 1.0);
        // random errors between -error and +error
        DoubleStream randomErrors = randomDoubles.map(v -> v*error);
        PrimitiveIterator.OfDouble randomErrorIter = randomErrors.iterator();

        for (int i = 0; i < loopCount; i++) {
            double xWithError = x + randomErrorIter.next();
            double yWithError = y +  randomErrorIter.next();
            double zWithError = z +  randomErrorIter.next();
            CartesianCoordinate other = new CartesianCoordinate(xWithError, yWithError, zWithError);
            System.out.println(other.getX() + " | " + other.getY() + " | " + other.getZ());
            assertEquals(actual, other);
        }
        assertFalse(randomErrorIter.hasNext());
    }

    @Test
    public void testIsEqual_NotEqualCoordinates() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate actual = new CartesianCoordinate(x, y, z);

        // no matter of the specified SCALE, this error should be considered as too large!
        double tooLargeError = 0.001;
        CartesianCoordinate otherX = new CartesianCoordinate(x + tooLargeError, y, z);
        CartesianCoordinate otherY = new CartesianCoordinate(x, y + tooLargeError, z);
        CartesianCoordinate otherZ = new CartesianCoordinate(x, y, z + tooLargeError);

        assertNotEquals(actual, otherX);
        assertNotEquals(actual, otherY);
        assertNotEquals(actual, otherZ);

    }

    @Test
    public void testIsEqual_NotEqualCoordinates2() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate actual = new CartesianCoordinate(x, y, z);

        // The factor 1.5 makes the error just a bit larger so that we have some free room
        // for internal calculation errors of isEqual(). Otherwise it might be that the error
        // annihilate itself and the compared Coordinate is considered equal.
        double slightlyTooLargeError = 1.5 * Math.pow(10, -AbstractCoordinate.SCALE);
        CartesianCoordinate otherBX = new CartesianCoordinate(x + slightlyTooLargeError, y, z);
        CartesianCoordinate otherBY = new CartesianCoordinate(x, y + slightlyTooLargeError, z);
        CartesianCoordinate otherBZ = new CartesianCoordinate(x, y, z + slightlyTooLargeError);

        assertNotEquals(actual, otherBX);
        assertNotEquals(actual, otherBY);
        assertNotEquals(actual, otherBZ);
    }
}
