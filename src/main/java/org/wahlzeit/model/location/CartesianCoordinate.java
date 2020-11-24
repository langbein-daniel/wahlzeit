package org.wahlzeit.model.location;

import org.wahlzeit.services.DataObject;
import org.wahlzeit.utils.DoubleUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Cartesian Coordinate
 * <p>
 * Center of coordinate-system: Center of mass of the Earth
 * Unit (of x, y, z values): Meters
 */
public class CartesianCoordinate extends AbstractCoordinate {
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
     * z-direction: From South to North Pole
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

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    public double getCartesianDistance(Coordinate other){
        CartesianCoordinate otherCartesian;
        if(other instanceof CartesianCoordinate){
            otherCartesian = (CartesianCoordinate) other;
        }else{
            otherCartesian = other.asCartesianCoordinate();
        }

        return getCartesianDistance(otherCartesian);
    }

    @Override
    public SphericalCoordinate asSphericalCoordinate() {
        return null; // TODO
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

    @Override
    public String toString() {
        return "CartesianCoordinate{" +
                "x=" + DoubleUtil.roundAsString(x, AbstractCoordinate.SCALE) +
                ", y=" + DoubleUtil.roundAsString(y, AbstractCoordinate.SCALE) +
                ", z=" + DoubleUtil.roundAsString(z, AbstractCoordinate.SCALE) +
                '}';
    }

    //=== Persistence Methods === // TODO

    @Override
    public String getIdAsString() {
        return null; // A Coordinate object hast no ID. It is always part of a Photo.
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        x = rset.getDouble("coordinate_x");
        y = rset.getDouble("coordinate_y");
        z = rset.getDouble("coordinate_z");
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateDouble("coordinate_x", x);
        rset.updateDouble("coordinate_y", y);
        rset.updateDouble("coordinate_z", z);
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        // nothing to do; Coordinate object has no ID.
    }
}
