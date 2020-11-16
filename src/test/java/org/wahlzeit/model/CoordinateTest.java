package org.wahlzeit.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * All test cases of the class {@link Coordinate}.
 */
public class CoordinateTest {
    @Test
    public void testGetDistance(){
        double delta = 0.0000000001; // expected accuracy

        Coordinate centerCoordinate = new Coordinate(0.0,0.0,0.0); // center of coordinate system

        double x = 3.0;
        double y = 7.0;
        double z = 9.0;
        Coordinate otherCoordinate = new Coordinate(x,y,z);

        double expectedDistance = 11.78982612255159596846918375135848942610804994310659548714592171; // sqrt(3*3 + 7*7 + 9*9)
        double actualDistance = centerCoordinate.getDistance(otherCoordinate);
        Assert.assertEquals(expectedDistance, actualDistance, delta);
    }
}