package org.wahlzeit.model.location;

import org.junit.Test;
import org.wahlzeit.utils.DoubleUtil;

import java.util.*;
import java.util.stream.DoubleStream;

import static org.junit.Assert.*;
import static org.wahlzeit.model.location.AbstractCoordinate.SCALE;
import static org.wahlzeit.model.location.AbstractCoordinateTest.accuracy;
import static org.wahlzeit.model.location.AbstractCoordinateTest.orthogonalLocations;


/**
 * All test cases of the class {@link CartesianCoordinate}.
 */
public class CartesianCoordinateTest {
    public static void assertEqualCartesian(CartesianCoordinate expected, CartesianCoordinate actual){
        assertEquals(expected, actual);
        assertTrue(DoubleUtil.isEqual(expected.getX(), actual.getX(), SCALE));
        assertTrue(DoubleUtil.isEqual(expected.getY(), actual.getY(), SCALE));
        assertTrue(DoubleUtil.isEqual(expected.getZ(), actual.getZ(), SCALE));
    }

    public static void assertEqualCartesian(String message, CartesianCoordinate expected, CartesianCoordinate actual){
        assertEquals(message, expected, actual);
        assertTrue(message, DoubleUtil.isEqual(expected.getX(), actual.getX(), SCALE));
        assertTrue(message, DoubleUtil.isEqual(expected.getY(), actual.getY(), SCALE));
        assertTrue(message, DoubleUtil.isEqual(expected.getZ(), actual.getZ(), SCALE));
    }

    @Test
    public void test_newCartesian(){
        for(LocationTuple location:AbstractCoordinateTest.locations){
            CartesianCoordinate expected = location.cartesian;

            CartesianCoordinate actual = new CartesianCoordinate(expected);

            assertEqualCartesian(expected, actual);
        }
    }

    @Test
    public void test_centralAngle(){
        // a, b and c are all "orthogonal" to each other (central angle of 90°)
        CartesianCoordinate a = orthogonalLocations[0].cartesian;
        CartesianCoordinate b = orthogonalLocations[1].cartesian;
        CartesianCoordinate c = orthogonalLocations[2].cartesian;
        double halfPi = Math.PI/2.0;

        assertEquals(halfPi, a.getCentralAngle(b), accuracy);
        assertEquals(halfPi, a.getCentralAngle(c), accuracy);
        assertEquals(halfPi, b.getCentralAngle(a), accuracy);
        assertEquals(halfPi, b.getCentralAngle(c), accuracy);
        assertEquals(halfPi, c.getCentralAngle(a), accuracy);
        assertEquals(halfPi, c.getCentralAngle(b), accuracy);

    }

    @Test
    public void test_getCartesianDistance() {
        // minimum expected accuracy
        double epsilon = 0.0000000001;

        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate other1 = new CartesianCoordinate(x, y, z);
        CartesianCoordinate other2 = new CartesianCoordinate(z, x, y);
        CartesianCoordinate other3 = new CartesianCoordinate(y, z, x);


        // manually calculated distance:
        // sqrt(3*3 + 7*7 + 9*9)
        double expectedDistance = 11.78982612255159596846918375135848942610804994310659548714592171;

        // Act
        double actual1 = CartesianCoordinate.CENTER.getCartesianDistance(other1);
        double actual2 = CartesianCoordinate.CENTER.getCartesianDistance(other2);
        double actual3 = CartesianCoordinate.CENTER.getCartesianDistance(other3);

        // Assert
        assertEquals(expectedDistance, actual1, epsilon);
        assertEquals(expectedDistance, actual2, epsilon);
        assertEquals(expectedDistance, actual3, epsilon);
    }

    @Test
    public void test_isEqual_ErrorRelativeToScale() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate expected = new CartesianCoordinate(x, y, z);

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

        assertEqualCartesian(expected, other);
    }

    @Test
    public void test_isEquals_RandomError() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate expected = new CartesianCoordinate(x, y, z);

        // how many other Coordinates with random errors shall be compared with the expected Coordinate
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
            assertEqualCartesian(expected, other);
        }
        assertFalse(randomErrorIter.hasNext());
    }

    @Test
    public void test_isEqual_NotEqualCoordinates() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate expected = new CartesianCoordinate(x, y, z);

        // no matter of the specified SCALE, this error should be considered as too large!
        double tooLargeError = 0.001;
        CartesianCoordinate otherX = new CartesianCoordinate(x + tooLargeError, y, z);
        CartesianCoordinate otherY = new CartesianCoordinate(x, y + tooLargeError, z);
        CartesianCoordinate otherZ = new CartesianCoordinate(x, y, z + tooLargeError);

        assertNotEquals(expected, otherX);
        assertNotEquals(expected, otherY);
        assertNotEquals(expected, otherZ);

    }

    @Test
    public void test_isEqual_NotEqualCoordinates2() {
        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        CartesianCoordinate expected = new CartesianCoordinate(x, y, z);

        // The factor 1.5 makes the error just a bit larger so that we have some free room
        // for internal calculation errors of isEqual(). Otherwise it might be that the error
        // annihilate itself and the compared Coordinate is considered equal.
        double slightlyTooLargeError = 1.5 * Math.pow(10, -AbstractCoordinate.SCALE);
        CartesianCoordinate otherBX = new CartesianCoordinate(x + slightlyTooLargeError, y, z);
        CartesianCoordinate otherBY = new CartesianCoordinate(x, y + slightlyTooLargeError, z);
        CartesianCoordinate otherBZ = new CartesianCoordinate(x, y, z + slightlyTooLargeError);

        assertNotEquals(expected, otherBX);
        assertNotEquals(expected, otherBY);
        assertNotEquals(expected, otherBZ);
    }

    /**
     * Coordinates with the same hashCode() shall also be equal() and vice versa.
     */
    @Test
    public void test_equalsAndHashCode() {
        Map<Integer, LinkedList<CartesianCoordinate>> map = new TreeMap<>();

        double x = 92;
        double y = 51;
        double z = 65;

        int loopCount = 100000;

        Random r = new Random();
        // random double values between -1 and +1
        // we need three of those in each loop
        DoubleStream randomDoubles = r.doubles(3 * loopCount, -1.0, 1.0);
        // random errors between -0.7 and +0.7 times accuracy
        DoubleStream randomErrors = randomDoubles.map(v -> v * 0.7 * accuracy);
        PrimitiveIterator.OfDouble randomErrorIter = randomErrors.iterator();

        for (int i = 0; i < loopCount; i++) {
            double xWithError = x + randomErrorIter.next();
            double yWithError = y + randomErrorIter.next();
            double zWithError = z + randomErrorIter.next();
            CartesianCoordinate cart = new CartesianCoordinate(xWithError, yWithError, zWithError);

            int hash = cart.hashCode();

            // Add hash code as key to map -> map contains all unique hash codes as keys
            // Add coordinate object to list of coordinates with same hash code
            LinkedList<CartesianCoordinate> value = map.getOrDefault(hash, new LinkedList<>());
            value.add(cart);
            map.put(hash, value);
        }

        /*
         * As the absolute error added to each of the three coordinate axes is larger than 0.4999 * accuracy
         * we expect the resulting coordinates to be not equal when rounding to SCALE digits to the right
         * of the dot.
         * For each coordinate we expect to at least once get an error so that the rounded coordinate is
         * coordinate + accuracy and at least once coordinate - accuracy.
         *
         * So the different values for the three coordinates we expect to get are:
         * ( 92.0*0, 92.0*1, 91.9*9 ) x ( 51.0*0, 51.0*1, 50.9*9 ) x ( 65.0*0, 65.0*1, 64.9*9 )
         *
         * => 3^3 = 27 permutations expected
         */
        assertEquals(27, map.keySet().size());

        for(Map.Entry<Integer, LinkedList<CartesianCoordinate>> entry:map.entrySet()){

            /*
             * Assert that all coordinates with the same hash are also
             * equal according to the isEqual() method!
             */


            int hash = entry.getKey();
            LinkedList<CartesianCoordinate> withEqualHash = entry.getValue();

            CartesianCoordinate previous = withEqualHash.removeFirst();
            while(!withEqualHash.isEmpty()){
                CartesianCoordinate tmp = withEqualHash.removeFirst();
                assertEquals(previous,tmp);
                previous=tmp;
            }
        }
    }
}
