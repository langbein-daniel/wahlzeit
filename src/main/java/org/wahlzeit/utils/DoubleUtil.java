package org.wahlzeit.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.PI;

public class DoubleUtil {
    public static final double TWO_PI = PI * 2.0;

    /**
     * @param x: some n umber
     * @param y: a divisor greater than zero
     * @return smallest positive remainder of "floorDiv(x, y)"
     */
    public static double posRemainder(double x, double y, final int scale) {
        assertArgumentIsPositiveFinite(y);

        if (x >= y) {
            return x % y;
        } else if (x < 0.0) {
            double negRemainder = x % y;
            if (isEqual(negRemainder, 0.0, scale)) {
                return 0.0;
            } else {
                return y + negRemainder;
            }
        } else {
            return x;
        }
    }

    /**
     * @return Returns true if a and b are equal when taking account of scale digits to the right of the decimal point.
     * @throws ArithmeticException If a and/or b are Infinite or NaN
     * @methodtype comparison
     * @methodproperties composed
     */
    public static boolean isEqual(final double a, final double b, final int scale) throws ArithmeticException {
        return compare(a, b, scale) == 0;
    }

    /**
     * @return Returns 0 if a and b are equal when taking account of scale digits to the right of the decimal point.
     * Otherwise -1 is returned if a < b or else +1.
     * @throws ArithmeticException If a and/or b are Infinite or NaN
     * @methodtype comparison
     * @methodproperties composed
     */
    public static int compare(final double a, final double b, final int scale) {
        if (!(Double.isFinite(a) && Double.isFinite(b))) {
            throw new ArithmeticException("Can't compare infinite or NaN numbers!");
        }
        // a and b are finite numbers

        // if a and b are the same reference
        if (a == b) return 0;

        assertArgumentIsPositiveFinite(scale);
        BigDecimal aRounded = doRoundAsBigDecimal(a, scale);
        BigDecimal bRounded = doRoundAsBigDecimal(b, scale);
        return aRounded.compareTo(bRounded);
    }

    /**
     * @param value double value to round and return as String
     * @param scale result is rounded to have scale digits to the right of the decimal point
     * @throws NumberFormatException If value is Infinite or NaN
     * @methodtype conversion
     * @methodproperties composed
     */
    public static String roundAsString(double value, int scale) {
        return roundAsBigDecimal(value, scale).toString();
    }

    /**
     * @param value double value to round and return as BigDecimal
     * @param scale result is rounded to have scale digits to the right of the decimal point
     * @throws NumberFormatException If value is Infinite or NaN
     * @methodtype conversion
     * @methodproperties composed
     */
    public static BigDecimal roundAsBigDecimal(double value, int scale) {
        assertArgumentIsPositiveFinite(scale);
        return doRoundAsBigDecimal(value, scale);
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    protected static BigDecimal doRoundAsBigDecimal(double value, int scale) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP);
    }


    //====== Assertions ======

    /**
     * @methodtype assertion
     * @methodproperties composed
     * <p>
     * Can be used to assert that some double argument is not negative, infinite or NaN
     */
    public static void assertArgumentIsPositiveFinite(double d) throws IllegalArgumentException {
        if (!isPositiveFinite(d)) {
            throw new IllegalArgumentException("negative double value");
        }
    }

    public static boolean isPositiveFinite(double d) throws IllegalStateException {
        return Double.isFinite(d) && d >= 0.0;
    }
}
