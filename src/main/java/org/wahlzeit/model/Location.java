package org.wahlzeit.model;

import java.util.Objects;

public class Location {
    public static final Location DEFAULT_LOCATION = new Location(1.0,2.0,3.0);

    protected Coordinate coordinate = new Coordinate();

    public Location() {
    }
    public Location(double x, double y, double z){
        setCoordinate(new Coordinate(x, y, z));
    }

    /**
     * @methodtype get
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @methodtype set
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
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
        return Objects.equals(coordinate, other.coordinate);
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }
}
