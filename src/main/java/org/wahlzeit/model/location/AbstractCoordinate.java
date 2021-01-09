package org.wahlzeit.model.location;

import org.wahlzeit.contract.AssertArgument;
import org.wahlzeit.contract.AssertResult;
import org.wahlzeit.contract.NotNull;
import org.wahlzeit.contract.Nullable;
import org.wahlzeit.utils.*;

import static java.lang.Math.PI;

/**
 * Note: A static Coordinate object representing the center can be found in subclass CartesianCoordinate.
 */
public abstract class AbstractCoordinate implements Coordinate {
    /**
     * Digits to the right of the decimal point that are taken into account
     * when comparing and for hash code generation of double values.
     */
    public static final int SCALE = 12;

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        CartesianCoordinate coordinate = doAsCartesianCoordinate();

        assertResultNotNull(coordinate);
        assertResultIsEqual(coordinate);
        return coordinate;
    }

    protected abstract CartesianCoordinate doAsCartesianCoordinate();

    @Override
    public SphericalCoordinate asSphericalCoordinate() {
        SphericalCoordinate coordinate = doAsSphericalCoordinate();

        assertResultNotNull(coordinate);
        assertResultIsEqual(coordinate);
        return coordinate;
    }

    protected abstract SphericalCoordinate doAsSphericalCoordinate();

    @Override
    public double getCartesianDistance(Coordinate other) {
        assertArgumentNotNull(other);

        double distance = doGetCartesianDistance(other);

        assertResultIsValidDistance(distance);
        return distance;
    }

    protected double doGetCartesianDistance(Coordinate other) {
        // To calculate the cartesian distance,
        // we need both coordinates in cartesian representation.
        return this.doAsCartesianCoordinate()
                .getCartesianDistance(other);
    }

    @Override
    public double getCentralAngle(Coordinate other) {
        assertArgumentNotNull(other);

        double angle = doGetCentralAngle(other);

        assertResultIsValidCentralAngle(angle);
        return angle;
    }

    protected double doGetCentralAngle(Coordinate other) {
        // To calculate the central angle,
        // we need both coordinates in cartesian representation.
        return this.doAsCartesianCoordinate()
                .getCentralAngle(other);
    }

    /**
     * @return false if obj is null or not a Coordinate object. Otherwise: isEqual((Coordinate) obj)
     * @methodtype comparison
     * @methodproperties composed
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinate)) {
            return false; // e.g. if obj == null
        }

        return isEqual((Coordinate) obj);
    }

    /**
     * @methodtype comparison
     * @methodproperties composed
     * <p>
     * A Coordinate shall be comparable/identifiable by the point in space it represents no matter of the internal
     * representation (spherical, cartesian, cylindrical, etc.  coordinate system).
     * Therefore we do first convert all of them to CartesianCoordinates and then compare two
     * CartesianCoordinates for equality.
     */
    @Override
    public boolean isEqual(@NotNull Coordinate other) throws IllegalArgumentException {
        return doIsEqual(other);
    }

    protected boolean doIsEqual(@NotNull Coordinate other) {
        String thisCartesianString = doAsCartesianCoordinate().toString();
        String otherCartesianString = other.asCartesianCoordinate().toString();
        return thisCartesianString.equals(otherCartesianString);
    }

    /**
     * @methodtype conversion
     * @methodproperties composed
     * <p>
     * A Coordinate shall be comparable/identifiable by the point in space it represents no matter of the internal
     * representation (spherical, cartesian, cylindrical, etc.  coordinate system).
     * Therefore we do first convert all of them to CartesianCoordinates and then generate a hash.
     */
    @Override
    public int hashCode() {
        return asCartesianCoordinate().toString().hashCode();
    }

    /**
     * Default implementation (for subclasses other than CartesianCoordinate)
     * so that one can print them with more details than with the default Object.toString() method.
     * Though, one may overwrite this method in a subclasses.
     */
    @Override
    public String toString() {
        return this.asCartesianCoordinate().toString();
    }


    //=== Assertions ===

    protected abstract void assertClassInvariants();

    protected static void assertArgumentNotNull(@Nullable Coordinate c)
            throws NullPointerException {
        AssertArgument.notNull("Coordinate must not be null!", c);
    }

    protected void assertResultNotNull(@Nullable Coordinate c)
            throws IllegalStateException {
        AssertResult.notNull("Coordinate must not be null!", c);
    }

    protected void assertResultIsEqual(@NotNull Coordinate other)
            throws ArithmeticException {
        if (!this.isEqual(other)) {
            throw new ArithmeticException("Converted Coordinate is not equal to originating Coordinate");
        }
    }

    protected void assertResultIsValidAngle(double angle)
            throws ArithmeticException {
        if (!isValidAngle(angle)) {
            throw new ArithmeticException("angle must be in the range [0, 2pi)");
        }
    }

    /**
     * Angle in radians shall be in the range [0, 2pi)
     */
    protected boolean isValidAngle(double angle) {
        return Double.isFinite(angle) && angle < DoubleUtil.TWO_PI;
    }

    protected void assertResultIsValidCentralAngle(double angle)
            throws ArithmeticException {
        if (!Double.isFinite(angle) && 0.0 <= angle && angle <= PI) {
            throw new ArithmeticException("calculated angle is not in range [0,pi]");
        }
    }

    protected void assertResultIsValidDistance(double distance)
            throws ArithmeticException {
        if (!DoubleUtil.isPositiveFinite(distance)) {
            throw new ArithmeticException("calculated distance is not positive finite");
        }
    }
}
