package org.wahlzeit.contract;

import org.wahlzeit.utils.DoubleUtil;

public class AssertArgument {
    public static void notNull(Object o) {
        notNull("Object must not be null!", o);
    }

    public static void notNull(String message, Object o) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Can be used to assert that some double argument is neither negative, infinite or NaN
     *
     * @methodtype assertion
     * @methodproperties composed
     */
    public static void isPositiveFinite(double d) throws IllegalArgumentException {
        isPositiveFinite("double value negative, infinite or NaN: " + d, d);
    }

    public static void isPositiveFinite(String message, double d) throws IllegalArgumentException {
        if (!DoubleUtil.isPositiveFinite(d)) {
            throw new IllegalArgumentException(message);
        }
    }
}
