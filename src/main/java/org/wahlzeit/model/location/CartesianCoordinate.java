package org.wahlzeit.model.location;

import org.wahlzeit.utils.DoubleUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public CartesianCoordinate() {
        incWriteCount();
    }

    public CartesianCoordinate(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public CartesianCoordinate(CartesianCoordinate c){
        this(c.getX(), c.getY(), c.getZ());
    }

    public CartesianCoordinate(Coordinate coordinate) {
        SphericalCoordinate spherical = coordinate.asSphericalCoordinate();
        double radius = spherical.getRadius();

        if (DoubleUtil.isEqual(radius, 0.0, SCALE)) {
            setX(0.0);
            setY(0.0);
            setZ(0.0);
        } else {
            double phi = spherical.getPhi();
            double theta = spherical.getTheta();
            double sinTheta = Math.sin(theta);

            setX(radius * sinTheta * Math.cos(phi));
            setY(radius * sinTheta * Math.sin(phi));
            setZ(radius * Math.cos(theta));
        }
    }


    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    public SphericalCoordinate asSphericalCoordinate() {
        return new SphericalCoordinate(this);
    }


    /**
     * @methodtype get
     * @methodproperties composed
     */
    public double getCartesianDistance(Coordinate other) {
        CartesianCoordinate otherCartesian = other.asCartesianCoordinate();
        return getCartesianDistance(otherCartesian);
    }


    /**
     * @return Central angle between this and the given coordinate.
     *   If one Coordinate has zero length (as vector), then NaN is returned.
     * @methodtype get
     * @methodproperties composed
     */
    @Override
    public double getCentralAngle(Coordinate other) {
        if(this.isEqual(CENTER)) {
            return Double.NaN;
        }
        CartesianCoordinate otherCartesian = other.asCartesianCoordinate();
        if(otherCartesian.isEqual(CENTER)) {
            return Double.NaN;
        }

        double ox = otherCartesian.getX(); /* other.x */
        double oy = otherCartesian.getY(); /* other.x */
        double oz = otherCartesian.getZ(); /* other.x */

        // < (this as vector) , (other as vector) >
        double scalarProd = x*ox + y*oy + z*oz;
        // || (this as vector) ||
        double thisDist = getCartesianDistance(CENTER);
        // || (other as vector) ||
        double otherDist = otherCartesian.getCartesianDistance(CENTER);

        return Math.acos(scalarProd / (thisDist * otherDist));
    }

    /**
     * @methodtype get
     * @methodproperties primitive
     */
    public double getCartesianDistance(CartesianCoordinate other) {
        double xDelta = Math.abs(other.x - x);
        double yDelta = Math.abs(other.y - y);
        double zDelta = Math.abs(other.z - z);

        double xyDistance = Math.hypot(xDelta, yDelta);
        return Math.hypot(xyDistance, zDelta); // xyzDistance
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
        this.z = z;
        incWriteCount();
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
    public void readFrom(CartesianCoordinate coordinate) {
        this.x = coordinate.getX();
        this.y = coordinate.getY();
        this.z = coordinate.getZ();
    }
}
