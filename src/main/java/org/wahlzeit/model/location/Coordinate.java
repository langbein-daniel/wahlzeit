package org.wahlzeit.model.location;

public interface Coordinate {
    public CartesianCoordinate asCartesianCoordinate();
    public double getCartesianDistance(Coordinate other);
    public SphericalCoordinate asSphericalCoordinate();
    public double getCentralAngle(Coordinate other);
    public boolean isEqual(Coordinate other);
}
