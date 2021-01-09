package org.wahlzeit.model.location;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Location extends DataObject {
    public static final Coordinate DEFAULT_COORDINATE = CartesianCoordinate.newCartesianCoordinate(1.2,3.4,5.6);

    protected Coordinate coordinate;

    /**
     * @methodtype constructor
     *
     * Creates a new Location with a default coordinate.
     */
    public Location() {
        coordinate = DEFAULT_COORDINATE;
        incWriteCount();
    }
    public Location(double x, double y, double z){
        coordinate = CartesianCoordinate.newCartesianCoordinate(x, y, z);
        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @methodtype boolean-query
     * @methodproperties composed
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return isEqual(location);
    }

    /**
     * @methodtype boolean-query
     * @methodproperties primitive
     */
    public boolean isEqual(Location other) {
        return Objects.equals(coordinate, other.getCoordinate());
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    public int hashCode() {
        return coordinate.hashCode();
    }

    //=== Persistence Methods ===

    @Override
    public String getIdAsString() {
        return null; // A Location object hast no ID. It is always part of a Photo.
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        double x = rset.getDouble("coordinate_x");
        double y = rset.getDouble("coordinate_y");
        double z = rset.getDouble("coordinate_z");
        coordinate = CartesianCoordinate.newCartesianCoordinate(x, y, z);
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        CartesianCoordinate cartesian = coordinate.asCartesianCoordinate();
        rset.updateDouble("coordinate_x", cartesian.getX());
        rset.updateDouble("coordinate_y", cartesian.getY());
        rset.updateDouble("coordinate_z", cartesian.getZ());
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) {
        // nothing to do; Location object has no ID.
    }
}
