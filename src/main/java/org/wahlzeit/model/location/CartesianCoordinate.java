package org.wahlzeit.model.location;

import org.wahlzeit.utils.DoubleUtil;

/**
 * Cartesian Coordinate
 * <p>
 * Center/Origin of coordinate-system: Center of mass of the Earth
 * Unit (of x, y, z values): Meters
 */
public class CartesianCoordinate extends AbstractCoordinate {
    public static final CartesianCoordinate CENTER = new CartesianCoordinate(0.0, 0.0, 0.0);


    /**
     * x-direction: In plane of Earth (latitude at 0 degrees);
     * from -180 to 0 degrees longitude
     */
    protected double x = 0.0;
    /**
     * y-direction: In plane of Earth (latitude at 0 degrees);
     * from -90 to +90 degrees longitude
     */
    protected double y = 0.0;
    /**
     * z-direction: From South to North Pole or in geographical words
     * from -90 to +90 degrees latitude
     */
    protected double z = 0.0;

    /**
     * @throws IllegalArgumentException if any of x, y or z is not a finite number.
     * @throws IllegalStateException    if the class invariants of this object are not adhered
     * @methodtype constructor
     */
    public CartesianCoordinate(double x, double y, double z) {
        assertArgumentIsValidXYZ(x);
        assertArgumentIsValidXYZ(y);
        assertArgumentIsValidXYZ(z);

        doSetX(x);
        doSetY(y);
        doSetZ(z);

        assertClassInvariants();
    }

    /**
     * @throws NullPointerException  See CartesianCoordinate(Coordinate)
     * @throws IllegalStateException if the class invariants of this object are not adhered
     * @methodtype constructor
     * @methodproperties convenience constructor
     */
    public CartesianCoordinate(CartesianCoordinate c) {
        this(c.getX(), c.getY(), c.getZ());
    }

    /**
     * @throws NullPointerException  if the given Coordinate is null
     * @throws ArithmeticException   if the given Coordinate converted to a SphericalCoordinate has
     *                               infinite or NaN values for radius, theta or phi
     * @throws IllegalStateException if the class invariants of this object are not adhered
     * @methodtype constructor
     */
    public CartesianCoordinate(Coordinate coordinate) throws NullPointerException {
        assertArgumentNotNull(coordinate);

        SphericalCoordinate spherical = coordinate.asSphericalCoordinate();
        double radius = spherical.getRadius();
        double theta = spherical.getTheta();
        double phi = spherical.getPhi();
        DoubleUtil.assertResultIsFinite(radius);
        DoubleUtil.assertResultIsFinite(theta);
        DoubleUtil.assertResultIsFinite(phi);


        double x, y, z;
        {
            if (DoubleUtil.isEqual(radius, 0.0, SCALE)) {
                x = 0.0;
                y = 0.0;
                z = 0.0;
            } else {
                double sinTheta = Math.sin(theta);

                x = radius * sinTheta * Math.cos(phi);
                y = radius * sinTheta * Math.sin(phi);
                z = radius * Math.cos(theta);
            }
        }

        assertResultIsValidXYZ(x);
        assertResultIsValidXYZ(y);
        assertResultIsValidXYZ(z);

        doSetX(x);
        doSetY(y);
        doSetZ(z);

        assertClassInvariants();
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return doAsCartesianCoordinate();
    }

    public CartesianCoordinate doAsCartesianCoordinate() {
        assertClassInvariants();
        return this;
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    protected SphericalCoordinate doAsSphericalCoordinate() {
        return new SphericalCoordinate(this);
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
        double xDelta = Math.abs(other.x - x);
        double yDelta = Math.abs(other.y - y);
        double zDelta = Math.abs(other.z - z);

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
        double scalarProd = x * ox + y * oy + z * oz;
        // || (this as vector) ||
        double thisDist = getCartesianDistance(CENTER);
        // || (other as vector) ||
        double otherDist = other.getCartesianDistance(CENTER);

        return Math.acos(scalarProd / (thisDist * otherDist));
    }

    //=== Getter and Setter ===

    /**
     * @methodtype get
     */
    public double getX() {
        return x;
    }

    /**
     * @methodtype set
     */
    public void setX(double x) {
        assertClassInvariants();
        assertArgumentIsValidXYZ(x);

        doSetX(x);

        assertClassInvariants();
    }

    protected void doSetX(double x) {
        this.x = x;
        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public double getY() {
        return y;
    }

    /**
     * @methodtype set
     */
    public void setY(double y) {
        assertClassInvariants();
        assertArgumentIsValidXYZ(y);

        doSetY(y);

        assertClassInvariants();
    }

    protected void doSetY(double y) {
        this.y = y;
        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public double getZ() {
        return z;
    }

    /**
     * @methodtype set
     */
    public void setZ(double z) {
        assertClassInvariants();
        assertArgumentIsValidXYZ(z);

        doSetZ(z);

        assertClassInvariants();
    }

    protected void doSetZ(double z) {
        this.z = z;
        incWriteCount();
    }

    public Object clone() {
        return new CartesianCoordinate(getX(), getY(), getZ());
    }

    /**
     * If and only if two CartesianCoordinates are considered equal,
     * they have the same String representation.
     */
    @Override
    public String toString() {
        return "CartesianCoordinate{" +
                "x=" + DoubleUtil.roundAsString(x, AbstractCoordinate.SCALE) +
                ", y=" + DoubleUtil.roundAsString(y, AbstractCoordinate.SCALE) +
                ", z=" + DoubleUtil.roundAsString(z, AbstractCoordinate.SCALE) +
                '}';
    }

    //=== Persistence Methods ===

    @Override
    public void doReadFrom(CartesianCoordinate coordinate) {
        this.x = coordinate.getX();
        this.y = coordinate.getY();
        this.z = coordinate.getZ();
    }

    //=== Assertions ===

    @Override
    protected void assertClassInvariants() {
        assertStateIsValidXYZ(getX());
        assertStateIsValidXYZ(getY());
        assertStateIsValidXYZ(getZ());
    }


    protected void assertArgumentIsValidXYZ(double xyz) {
        if (!isValidXYZ(xyz)) {
            throw new IllegalArgumentException("x, y and z must be finite");
        }
    }

    protected void assertResultIsValidXYZ(double xyz) {
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
    protected boolean isValidXYZ(double xyz) {
        return Double.isFinite(xyz);
    }
}
