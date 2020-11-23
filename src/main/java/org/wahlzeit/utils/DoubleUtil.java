package org.wahlzeit.utils;

public class DoubleUtil {

    /**
     * Checks if two double numbers a and b differ less than epsilon.
     * If one of the compared numbers is not a finite number (e.g. if it is infinite or not-a-number)
     * then an ArithmeticException is thrown. (Except if both numbers are infinite, but with different signs, then false is returned)
     *
     * @return Returns true if the doubles a and double b differ less than epsilon.
     * @throws ArithmeticException Is thrown if a or b are not finite numbers.
     * @methodtype boolean-query
     * @methodproperties primitive
     */
    public static boolean isEqual(final double a, final double b, final double epsilon) throws ArithmeticException {

        /* === corner cases === */

        if (a == Double.POSITIVE_INFINITY && b == Double.POSITIVE_INFINITY) {
            throw new ArithmeticException("Can't tell weather 'a' and 'b' differ only by epsilon or are way more different");
        }
        if (a == Double.NEGATIVE_INFINITY && b == Double.NEGATIVE_INFINITY) {
            throw new ArithmeticException("Can't tell weather 'a' and 'b' differ only by epsilon or are way more different");
        }
        if (Double.isNaN(a) || Double.isNaN(b)) {
            throw new ArithmeticException("Can't compare NaN double values!");
        }

        if (a == b) return true;  // if a and b reference the same object

        // The given epsilon must be a finite (not NaN and not infinite) number.
        if (!Double.isFinite(epsilon)) throw new IllegalArgumentException("'epsilon' must be a finite number!");


        /* === actual comparison of numbers */

        double diff = Math.abs(a - b);
        return diff < epsilon;
    }

    /**
     * @return Returns 0 if a and b differ less than epsilon. Otherwise -1 is returned if a < b or else +1.
     * @methodtype query
     * @methodproperties composed
     */
    public static int compare(final double a, final double b, final double epsilon) {
        if (isEqual(a, b, epsilon)) {
            return 0;
        }
        if (a < b) {
            return -1;
        } else {
            return +1;
        }
    }
}
