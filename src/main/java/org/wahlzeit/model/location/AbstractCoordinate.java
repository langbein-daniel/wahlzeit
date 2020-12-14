package org.wahlzeit.model.location;

import org.wahlzeit.services.DataObject;
import org.wahlzeit.utils.DoubleUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Math.PI;

/**
 * Note: A static Coordinate object representing the center can be found in subclass CartesianCoordinate.
 */
public abstract class AbstractCoordinate extends DataObject implements Coordinate {

    /**
     * Digits to the right of the decimal point that are taken into account
     * when comparing and for hash code generation of double values.
     */
    public static final int SCALE = 12;

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        assertClassInvariants();

        CartesianCoordinate coordinate = doAsCartesianCoordinate();

        assertResultIsEqual(coordinate);
        assertClassInvariants();
        return coordinate;
    }

    protected abstract CartesianCoordinate doAsCartesianCoordinate();

    @Override
    public SphericalCoordinate asSphericalCoordinate() {
        assertClassInvariants();

        SphericalCoordinate coordinate = doAsSphericalCoordinate();

        assertResultIsEqual(coordinate);
        assertClassInvariants();
        return coordinate;
    }

    protected abstract SphericalCoordinate doAsSphericalCoordinate();

    @Override
    public double getCartesianDistance(Coordinate other) {
        assertClassInvariants();
        assertArgumentNotNull(other);
        AbstractCoordinate before = (AbstractCoordinate) this.clone();

        double distance = doGetCartesianDistance(other);

        assertResultIsEqual(before);
        assertResultIsValidDistance(distance);
        assertClassInvariants();
        return distance;
    }

    protected double doGetCartesianDistance(Coordinate other) {
        // To calculate the cartesian distance,
        // we need both coordinates in cartesian representation.
        return this.doAsCartesianCoordinate().getCartesianDistance(other);
    }

    @Override
    public double getCentralAngle(Coordinate other) {
        assertClassInvariants();
        assertArgumentNotNull(other);
        AbstractCoordinate before = (AbstractCoordinate) this.clone();

        double angle = doGetCentralAngle(other);

        assertResultIsEqual(before);
        assertResultIsValidCentralAngle(angle);
        assertClassInvariants();
        return angle;
    }

    protected double doGetCentralAngle(Coordinate other) {
        // To calculate the central angle,
        // we need both coordinates in cartesian representation.
        return this.doAsCartesianCoordinate().getCentralAngle(other);
    }

    /**
     * @return false if obj is null or not a Coordinate object. Otherwise: isEqual((Coordinate) obj)
     * @methodtype comparison
     * @methodproperties composed
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinate)) return false;

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
    public boolean isEqual(Coordinate other) throws IllegalArgumentException {
        assertClassInvariants();
        assertArgumentNotNull(other);

        boolean result = doIsEqual(other);

        assertClassInvariants();
        return result;
    }

    protected boolean doIsEqual(Coordinate other) {
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

    public abstract Object clone();

    /**
     * Default implementation (for subclasses other than CartesianCoordinate)
     * so that one can print them with more details than with the default Object.toString() method.
     * Though, one may overwrite this method in a subclasses.
     */
    @Override
    public String toString() {
        return this.asCartesianCoordinate().toString();
    }

    //=== Persistence Methods ===

    @Override
    public String getIdAsString() {
        return null; // A Coordinate object hast no ID. It is always part of a Photo.
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        assertClassInvariants();

        double x = rset.getDouble("coordinate_x");
        double y = rset.getDouble("coordinate_y");
        double z = rset.getDouble("coordinate_z");

        doReadFrom(new CartesianCoordinate(x, y, z));
        assertClassInvariants();
    }

    protected abstract void doReadFrom(CartesianCoordinate coordinate);

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        assertClassInvariants();
        AbstractCoordinate before = (AbstractCoordinate) this.clone();

        doWriteOn(rset);

        assertResultIsEqual(before);
        assertClassInvariants();
    }

    protected void doWriteOn(ResultSet rset) throws SQLException {
        CartesianCoordinate cartesian = this.asCartesianCoordinate();
        rset.updateDouble("coordinate_x", cartesian.getX());
        rset.updateDouble("coordinate_y", cartesian.getY());
        rset.updateDouble("coordinate_z", cartesian.getZ());
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) {
        // nothing to do; Coordinate object has no ID.
    }

    //=== Assertions ===

    protected abstract void assertClassInvariants();

    protected void assertArgumentNotNull(Coordinate c) throws NullPointerException {
        if (c == null) {
            throw new NullPointerException("Coordinate must not be null");
        }
    }

    protected void assertResultIsEqual(Coordinate other) {
        if (!this.isEqual(other)) {
            throw new ArithmeticException("Converted Coordinate is not equal to originating Coordinate");
        }
    }

    protected void assertResultIsValidAngle(double angle) {
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

    protected void assertResultIsValidCentralAngle(double angle) {
        if (!Double.isFinite(angle) && 0.0 <= angle && angle <= PI) {
            throw new ArithmeticException("calculated angle is not in range [0,pi]");
        }
    }

    protected void assertResultIsValidDistance(double distance) {
        if (!DoubleUtil.isPositiveFinite(distance)) {
            throw new ArithmeticException("calculated distance is not positive finite");
        }
    }
}
