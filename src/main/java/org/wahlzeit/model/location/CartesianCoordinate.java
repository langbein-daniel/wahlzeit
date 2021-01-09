package org.wahlzeit.model.location;

import org.wahlzeit.contract.AssertArgument;
import org.wahlzeit.contract.AssertResult;
import org.wahlzeit.utils.DoubleUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Cartesian Coordinate
 * <p>
 * Center/Origin of coordinate-system: Center of mass of the Earth
 * Unit (of x, y, z values): Meters
 */
public class CartesianCoordinate extends AbstractCoordinate {
    public static final CartesianCoordinate CENTER = new CartesianCoordinate(0.0, 0.0, 0.0);

    private static final Map<Integer, CartesianCoordinate> allCartesianCoordinates = new HashMap<>();

    static {
        allCartesianCoordinates.put(CENTER.hashCode(), CENTER);
    }


    /**
     * x-direction: In plane of Earth (latitude at 0 degrees);
     * from -180 to 0 degrees longitude
     */
    protected final double x;
    /**
     * y-direction: In plane of Earth (latitude at 0 degrees);
     * from -90 to +90 degrees longitude
     */
    protected final double y;
    /**
     * z-direction: From South to North Pole or in geographical words
     * from -90 to +90 degrees latitude
     */
    protected final double z;

    /**
     * The value object as unique String
     */
    protected final String stringRepresentation;

    /**
     * @throws IllegalArgumentException if any of x, y or z is not a finite number.
     * @throws IllegalStateException    if the class invariants of this object are not adhered
     * @methodtype factory
     * @methodproperties composed
     */
    public static CartesianCoordinate newCartesianCoordinate(double x, double y, double z) {
        CartesianCoordinate newCoordinate = new CartesianCoordinate(x, y, z);
        return shareCoordinateValue(newCoordinate);
    }

    /**
     * @throws IllegalArgumentException if any of x, y or z is not a finite number.
     * @throws IllegalStateException    if the class invariants of this object are not adhered
     * @methodtype constructor
     */
    private CartesianCoordinate(double x, double y, double z) {
        assertArgumentIsValidXYZ(x);
        assertArgumentIsValidXYZ(y);
        assertArgumentIsValidXYZ(z);

        this.x = x;
        this.y = y;
        this.z = z;
        this.stringRepresentation = "CartesianCoordinate{" +
                "x=" + DoubleUtil.roundAsString(x, AbstractCoordinate.SCALE) +
                ", y=" + DoubleUtil.roundAsString(y, AbstractCoordinate.SCALE) +
                ", z=" + DoubleUtil.roundAsString(z, AbstractCoordinate.SCALE) +
                '}';

        assertClassInvariants();
    }


    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return doAsCartesianCoordinate();
    }
    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    protected CartesianCoordinate doAsCartesianCoordinate() {
        return this;
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    protected SphericalCoordinate doAsSphericalCoordinate() {
        double radius, theta, phi;
        {
            if (this.equals(CENTER)) {
                radius = 0.0;
                theta = 0.0;
                phi = 0.0;
            } else {
                /* distance from center = sqrt(x^2 + y^2 + z^2) */
                radius = this.getCartesianDistance(CENTER);

                /* arctan( sqrt(x^2 + y^2) / z  ) = arccos( z / radius ) */
                // setTheta(Math.atan(Math.hypot(x, y) / z));
                theta = Math.acos(getZ() / radius);

                /* arctan(y, x) may produce results with wrong sign ! */
                //phi = Math.atan(y / x);
                /* atan2(y, x) */
                phi = Math.atan2(getY(), getX());
            }
        }

        return SphericalCoordinate.newSphericalCoordinate(radius, theta, phi);
    }

    /**
     * @methodtype get
     * @methodproperties composed
     */
    @Override
    protected double doGetCartesianDistance(Coordinate other) {
        CartesianCoordinate otherCartesian = other.asCartesianCoordinate();
        return doGetCartesianDistance(otherCartesian);
    }

    /**
     * @methodtype get
     * @methodproperties primitive
     */
    protected double doGetCartesianDistance(CartesianCoordinate other) {
        double xDelta = Math.abs(other.getX() - getX());
        double yDelta = Math.abs(other.getY() - getY());
        double zDelta = Math.abs(other.getZ() - getZ());

        double xyDistance = Math.hypot(xDelta, yDelta);
        return Math.hypot(xyDistance, zDelta); // xyzDistance
    }

    /**
     * @return Central angle between this and the given coordinate.
     * If one Coordinate has zero length (as vector), then NaN is returned.
     * @methodtype get
     * @methodproperties composed
     */
    @Override
    protected double doGetCentralAngle(Coordinate other) {
        CartesianCoordinate otherCartesian = other.asCartesianCoordinate();
        return doGetCentralAngle(otherCartesian);
    }

    protected double doGetCentralAngle(CartesianCoordinate other) {
        if (this.isEqual(CENTER)) {
            return Double.NaN;
        }
        if (other.isEqual(CENTER)) {
            return Double.NaN;
        }

        double ox = other.getX(); /* other.x */
        double oy = other.getY(); /* other.x */
        double oz = other.getZ(); /* other.x */

        // < (this as vector) , (other as vector) >
        double scalarProd = getX() * ox + getY() * oy + getZ() * oz;
        // || (this as vector) ||
        double thisDist = getCartesianDistance(CENTER);
        // || (other as vector) ||
        double otherDist = other.getCartesianDistance(CENTER);

        return Math.acos(scalarProd / (thisDist * otherDist));
    }

    //=== Getter ===

    /**
     * @methodtype get
     */
    public double getX() {
        return x;
    }

    /**
     * @methodtype get
     */
    public double getY() {
        return y;
    }

    /**
     * @methodtype get
     */
    public double getZ() {
        return z;
    }

    //=== Other ===

    /**
     * If and only if two CartesianCoordinates are considered equal,
     * they have the same String representation.
     */
    @Override
    public String toString() {
        return stringRepresentation;
    }

    /**
     * Objects of this class are immutable shared value objects.
     *
     * @param coordinate If the given value is not yet known/shared, it is added to a Map
     * @return Reference to a shared value object
     */
    protected static CartesianCoordinate shareCoordinateValue(CartesianCoordinate coordinate) {
        int hash = coordinate.hashCode();
        CartesianCoordinate sharedCoordinate = allCartesianCoordinates.get(hash);

        if (sharedCoordinate == null) {
            sharedCoordinate = allCartesianCoordinates.get(hash);
            if (sharedCoordinate == null) {
                allCartesianCoordinates.put(hash, coordinate);
                sharedCoordinate = coordinate;
            }
        }

        return sharedCoordinate;
    }

    //=== Assertions ===

    @Override
    protected void assertClassInvariants() {
        assertStateIsValidXYZ(getX());
        assertStateIsValidXYZ(getY());
        assertStateIsValidXYZ(getZ());
        AssertResult.notNull(stringRepresentation);
        AssertResult.greaterThan(stringRepresentation.length(), "CartesianCoordinate".length());
    }


    protected void assertArgumentIsValidXYZ(double xyz) {
        if (!isValidXYZ(xyz)) {
            throw new IllegalArgumentException("x, y and z must be finite");
        }
    }

    protected static void assertResultIsValidXYZ(double xyz) {
        if (!isValidXYZ(xyz)) {
            throw new ArithmeticException("calculated x, y or z is not finite");
        }
    }

    protected void assertStateIsValidXYZ(double xyz) {
        if (!isValidXYZ(xyz)) {
            throw new IllegalStateException("x, y or z is not finite");
        }
    }

    /**
     * Checks weather the given coordinate (x, y or z) is valid.
     */
    protected static boolean isValidXYZ(double xyz) {
        return Double.isFinite(xyz);
    }
}
