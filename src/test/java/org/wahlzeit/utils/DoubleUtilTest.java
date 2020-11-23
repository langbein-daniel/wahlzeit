package org.wahlzeit.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * All test cases of the class {@link DoubleUtil}.
 */
public class DoubleUtilTest {
    @Test
    public void testIsEqual_SlightlyDifferent(){
        double aNumber = 13.0;
        double otherNumber = 13.05;
        double lageEnoughEpsilon = 0.1;
        double tooSmallEpsilon = 0.01;

        assertTrue(DoubleUtil.isEqual(aNumber, otherNumber, lageEnoughEpsilon));
        assertFalse(DoubleUtil.isEqual(aNumber, otherNumber, tooSmallEpsilon));
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_NotANumber1() {
        double nanA = Double.NaN;
        double nanB = Double.NaN;

        // if one or both of the compared doubles are not a number, they should not be seen as equal!
        assertFalse(DoubleUtil.isEqual(nanA, nanB, Double.MAX_VALUE));
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_NotANumber2() {
        double nanA = Double.NaN;
        double aNumber = 13.0;

        // if one or both of the compared doubles are not a number, they should not be seen as equal!
        assertFalse(DoubleUtil.isEqual(aNumber, nanA, Double.MAX_VALUE));
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_NotANumber3() {
        double nanB = Double.NaN;
        double aNumber = 13.0;

        // if one or both of the compared doubles are not a number, they should not be seen as equal!
        assertFalse(DoubleUtil.isEqual(nanB, aNumber, Double.MAX_VALUE));
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_PositiveInfinity() {
        double posInfA = Double.POSITIVE_INFINITY;
        double posInfB = Double.POSITIVE_INFINITY;

        DoubleUtil.isEqual(posInfA, posInfB, Double.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void testIsEqual_NegativeInfinity() {
        double negInfC = Double.NEGATIVE_INFINITY;
        double negInfD = Double.NEGATIVE_INFINITY;

        DoubleUtil.isEqual(negInfC, negInfD, Double.MAX_VALUE);
    }

    @Test
    public void testIsEqual_DifferentSignInfinity(){
        double posInfA = Double.POSITIVE_INFINITY;
        double negInfC = Double.NEGATIVE_INFINITY;

        assertFalse(DoubleUtil.isEqual(posInfA, negInfC, Double.MAX_VALUE));
        assertFalse(DoubleUtil.isEqual(negInfC, posInfA, Double.MAX_VALUE));
    }
}
