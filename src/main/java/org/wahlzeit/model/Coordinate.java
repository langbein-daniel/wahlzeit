package org.wahlzeit.model;

import java.util.Objects;

/**
 * Cartesian Coordinate
 *
 * Center of coordinate-system: Center of mass of the Earth
 * Unit (of x, y, z values): Meters
 */
public class Coordinate {
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
    }
    public Coordinate(double x, double y, double z){
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
        return Double.compare(other.x, x) == 0 &&
                Double.compare(other.y, y) == 0 &&
                Double.compare(other.z, z) == 0;
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
