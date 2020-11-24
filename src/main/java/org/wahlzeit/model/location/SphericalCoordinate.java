package org.wahlzeit.model.location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SphericalCoordinate extends AbstractCoordinate{
    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return null; // TODO
    }

    @Override
    public SphericalCoordinate asSphericalCoordinate() {
        return this;
    }

    //=== Persistence Methods === // TODO

    @Override
    public String getIdAsString() {
        return null; // A Coordinate object hast no ID. It is always part of a Photo.
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
       
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {

    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        // nothing to do; Coordinate object has no ID.
    }
}
