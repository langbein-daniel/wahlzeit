package org.wahlzeit.model.location;

import org.wahlzeit.services.DataObject;
import org.wahlzeit.utils.DoubleUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Cartesian Coordinate
 * <p>
 * Center of coordinate-system: Center of mass of the Earth
 * Unit (of x, y, z values): Meters
 */
public class Coordinate extends DataObject {
    public static final double EPSILON = 0.001; /* epsilon for double rounding errors */

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

    public Coordinate() {
        incWriteCount();
    }

    public Coordinate(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * @methodtype get
     * @methodproperties primitive
     */
    public double getDistance(Coordinate other) {
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

    /**
     * @methodtype boolean-query
     * @methodproperties composed
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) o;
        return isEqual(coordinate);
    }

    /**
     * @methodtype boolean-query
     * @methodproperties primitive
     */
    public boolean isEqual(Coordinate other) {
        return DoubleUtil.isEqual(other.x, x, EPSILON) &&
                DoubleUtil.isEqual(other.y, y, EPSILON) &&
                DoubleUtil.isEqual(other.z, z, EPSILON);

        // alternative: return DoubleUtil.isEqual(getDistance(other), 0.0, EPSILON);
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    //=== Persistence Methods ===

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
