package org.wahlzeit.model.location;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Location extends DataObject {
    public static final Coordinate DEFAULT_COORDINATE = new CartesianCoordinate(1.0,2.0,3.0);

    protected final Coordinate coordinate;

    public Location() {
        coordinate = (Coordinate) DEFAULT_COORDINATE.clone();
        incWriteCount();
    }
    public Location(double x, double y, double z){
        coordinate = new CartesianCoordinate(x, y, z);
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
    public boolean isDirty(){
        return super.isDirty() || coordinate.isDirty();
    }

    @Override
    public String getIdAsString() {
        return null; // A Location object hast no ID. It is always part of a Photo.
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        coordinate.readFrom(rset);
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        coordinate.writeOn(rset);
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) {
        // nothing to do; Location object has no ID.
    }
}
