package org.wahlzeit.contract;

public class AssertResult {
    public static void notNull(Object o){
        notNull("Expected object to be not null!", o);
    }

    public static void notNull(String message, Object o){
        if(o == null){
            throw new IllegalStateException(message);
        }
    }


    public static void equal(@Nullable Object a, @Nullable Object b) {
        if(a!=null && b!=null) {
            isEqual("Objects " + a.toString() + " and " + b.toString() + " were expected to be equal", a, b);
        }
        if(a!=b){
            throw new IllegalStateException("The given two objects are not equal");
        }
    }

    public static void equal(String message, @Nullable Object a, @Nullable Object b) {
        if(a!=null && b!=null){
            isEqual(message, a, b);
        }
        if(a!=b){
            throw new IllegalStateException(message);
        }
    }

    public static void isEqual(String message, @NotNull Object a, @NotNull Object b){
        if (!a.equals(b)) {
            throw new IllegalStateException(message);
        }
    }


    public static void isFinite(double d){
        isFinite("infinite or NaN double value", d);
    }

    public static void isFinite(String message, double d){
        if(!Double.isFinite(d)){
            throw new ArithmeticException(message);
        }
    }

    /**
     * Assert the first argument to be larger than the second.
     */
    public static void greaterThan(int larger, int lower){
        if(larger<=lower){
            throw new IllegalStateException("Expected " + larger + " to be greater than " + lower + "!");
        }
    }
}
