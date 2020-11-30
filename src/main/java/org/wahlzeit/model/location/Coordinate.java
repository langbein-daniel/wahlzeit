package org.wahlzeit.model.location;

import org.wahlzeit.services.Persistent;

public interface Coordinate extends Persistent {
    CartesianCoordinate asCartesianCoordinate();
    SphericalCoordinate asSphericalCoordinate();

    double getCartesianDistance(Coordinate other);
    double getCentralAngle(Coordinate other);

    boolean isEqual(Coordinate other);
}
