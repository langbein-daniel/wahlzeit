package org.wahlzeit.model.location;

import org.wahlzeit.services.DataObject;
import org.wahlzeit.utils.DoubleUtil;

public abstract class AbstractCoordinate extends DataObject implements Coordinate {

    /*
     * digits to the right of the decimal point that are taken into account
     * when comparing and for hash code generation of double values
     */
    public static final int SCALE = 12;

    @Override
    public abstract CartesianCoordinate asCartesianCoordinate();

    @Override
    public double getCartesianDistance(Coordinate other) {
        return this.asCartesianCoordinate().getCartesianDistance(other);
    }

    @Override
    public abstract SphericalCoordinate asSphericalCoordinate();

    @Override
    public double getCentralAngle(Coordinate other) {
        return this.asSphericalCoordinate().getCentralAngle(other);
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

        double distance = getCartesianDistance(other);
        return DoubleUtil.isEqual(distance, 0.0, SCALE);
    }

    /**
     * @methodtype conversion
     * @methodproperties composed
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * @methodtype conversion
     * @methodproperties composed
     */
    @Override
    public String toString() {
        return asCartesianCoordinate().toString();
    }
}
