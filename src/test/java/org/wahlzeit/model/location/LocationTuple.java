package org.wahlzeit.model.location;

public class LocationTuple {
    public final CartesianCoordinate cartesian;
    public final SphericalCoordinate spherical;

    public LocationTuple(CartesianCoordinate cartesian, SphericalCoordinate spherical) {
        this.cartesian = cartesian;
        this.spherical = spherical;
    }
}
