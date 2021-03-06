package org.wahlzeit.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static java.lang.Math.PI;
import static org.junit.Assert.*;

/**
 * All test cases of the class {@link DoubleUtil}.
 */
public class DoubleUtilTest {

    @Test
    public void doubleRemainder() {
        /*
         * We have some x that is not in the desired
         * range [0, 2*pi]
         * Using the modulus operator, one can subtract
         * multiples of 2*pi from x so that the result is in the range
         */
        double upperRangeLimit = 2.0 * PI;
        double factor = 1.75;
        double x = factor * upperRangeLimit; // x is not in the range
        double expected = 0.75 * upperRangeLimit;

        double inRange = x % upperRangeLimit;
        double inRange2 = DoubleUtil.posRemainder(x, upperRangeLimit, 10);

        assertEquals(expected, inRange, 0.000001);
        assertEquals(expected, inRange2, 0.000001);
    }

    @Test
    public void negativeDoubleRemainder(){
        double upperRangeLimit = 2.0 * PI;
        double factor = -0.75;
        double x = factor * upperRangeLimit; // x is not in the range
        double expected = 0.25 * upperRangeLimit;

        // as x is negative, the "%" will the smallest negative remainder
        // if we subtract this from upperRangeLimit, we get a value in the range
        double inRange = upperRangeLimit + (x % upperRangeLimit);
        double inRange2 = DoubleUtil.posRemainder(x, upperRangeLimit, 10);

        assertEquals(expected, inRange, 0.000001);
        assertEquals(expected, inRange2, 0.000001);
    }

    @Test
    public void doublePrecision() {
        // one double is represented by numBits bit
        int numBits = Double.BYTES * 8;
        // the largest number to represent with numBits bit is the number
        // with all bits being at "1"
        // This is equivalent to "2^(numBits+1) - 1"
        BigDecimal maxNumber = new BigDecimal("2").pow(numBits + 1).subtract(BigDecimal.ONE);
        // maxNumber consists of numDigits decimal digits
        // (maxNumber is a natural number)
        int numDigits = maxNumber.toString().length();

        System.out.println("doubles can't have more than " + numDigits + " digits of precision");
    }

    @Test
    public void testIsEqual_SlightlyDifferent() {
        double aNumber = 1.0;
        double otherNumber = 2.0;
        int tooLargeScale = 0;

        assertFalse(DoubleUtil.isEqual(aNumber, otherNumber, tooLargeScale));
    }

    @Test
    public void testIsEqual_SlightlyDifferent2() {
        double aNumber = 10.0;
        double otherNumber = 10.1;
        int smallEnoughScale = 0;
        int tooHighScale = 1;

        assertTrue(DoubleUtil.isEqual(aNumber, otherNumber, smallEnoughScale));
        assertFalse(DoubleUtil.isEqual(aNumber, otherNumber, tooHighScale));
    }

    @Test
    public void testIsEqual_SlightlyDifferent3() {
        double aNumber = 10.0;
        double otherNumber = 10.01;
        int smallEnoughScale = 1;
        int tooHighScale = 2;

        assertTrue(DoubleUtil.isEqual(aNumber, otherNumber, smallEnoughScale));
        assertFalse(DoubleUtil.isEqual(aNumber, otherNumber, tooHighScale));
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_NotANumber1() {
        double nanA = Double.NaN;
        double nanB = Double.NaN;

        // if one or both of the compared doubles are not a number, they should not be seen as equal!
        DoubleUtil.isEqual(nanA, nanB, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_NotANumber2() {
        double nanA = Double.NaN;
        double aNumber = 13.0;

        // if one or both of the compared doubles are not a number, they should not be seen as equal!
        DoubleUtil.isEqual(aNumber, nanA, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_NotANumber3() {
        double nanB = Double.NaN;
        double aNumber = 13.0;

        // if one or both of the compared doubles are not a number, they should not be seen as equal!
        DoubleUtil.isEqual(nanB, aNumber, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_PositiveInfinity() {
        double posInfA = Double.POSITIVE_INFINITY;
        double posInfB = Double.POSITIVE_INFINITY;

        DoubleUtil.isEqual(posInfA, posInfB, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_NegativeInfinity() {
        double negInfC = Double.NEGATIVE_INFINITY;
        double negInfD = Double.NEGATIVE_INFINITY;

        DoubleUtil.isEqual(negInfC, negInfD, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_DifferentSignInfinity() {
        double posInfA = Double.POSITIVE_INFINITY;
        double negInfC = Double.NEGATIVE_INFINITY;

        DoubleUtil.isEqual(posInfA, negInfC, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_DifferentSignInfinity2() {
        double posInfA = Double.NEGATIVE_INFINITY;
        double negInfC = Double.POSITIVE_INFINITY;

        DoubleUtil.isEqual(negInfC, posInfA, 1);
    }
}
