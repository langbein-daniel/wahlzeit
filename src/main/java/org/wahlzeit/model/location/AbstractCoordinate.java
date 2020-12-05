package org.wahlzeit.model.location;

import org.wahlzeit.services.DataObject;
import org.wahlzeit.utils.DoubleUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractCoordinate extends DataObject implements Coordinate {
    /*
     * A static CENTER Coordinate can be found in subclass CartesianCoordinate
     */

    /**
     * digits to the right of the decimal point that are taken into account
     * when comparing and for hash code generation of double values
     */
    public static final int SCALE = 12;

    @Override
    public abstract CartesianCoordinate asCartesianCoordinate();

    @Override
    public abstract SphericalCoordinate asSphericalCoordinate();


    @Override
    public double getCartesianDistance(Coordinate other) {
        // To calculate the cartesian distance,
        // we need both coordinates in cartesian representation.
        return this.asCartesianCoordinate().getCartesianDistance(other);
    }

//    @Override
//    public double getCentralAngle(Coordinate other) {
//        return this.asSphericalCoordinate().getCentralAngle(other);
//    }

    @Override
    public double getCentralAngle(Coordinate other) {
        // To calculate the central angle,
        // we need both coordinates in cartesian representation.
        return this.asCartesianCoordinate().getCentralAngle(other);
    }

    /**
     * @methodtype comparison
     * @methodproperties composed
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(!(obj instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) obj;
        return isEqual(coordinate);
    }

    /**
     * @methodtype comparison
     * @methodproperties composed
     */
    @Override
    public boolean isEqual(Coordinate other) {
        if (other == null) return false;
        return this.toString().equals(other.toString());
    }

    /**
     * @methodtype conversion
     * @methodproperties composed
     */
    @Override
    public int hashCode() {
        /*
         * A Coordinate shall be comparable/identifiable by the point in space it represents no matter of the internal representation
         * (spherical, cartesian, cylindrical, etc.  coordinate system).
         * Therefore we do first convert all of them to CartesianCoordinates.
         */
        return asCartesianCoordinate().toString().hashCode();
    }

    //=== Persistence Methods ===

    @Override
    public String getIdAsString() {
        return null; // A Coordinate object hast no ID. It is always part of a Photo.
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        double x = rset.getDouble("coordinate_x");
        double y = rset.getDouble("coordinate_y");
        double z = rset.getDouble("coordinate_z");

        readFrom(new CartesianCoordinate(x,y,z));
    }

    public abstract void readFrom(CartesianCoordinate coordinate);

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        CartesianCoordinate cartesian = this.asCartesianCoordinate();
        rset.updateDouble("coordinate_x", cartesian.getX());
        rset.updateDouble("coordinate_y", cartesian.getY());
        rset.updateDouble("coordinate_z", cartesian.getZ());
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        // nothing to do; Coordinate object has no ID.
    }
}
