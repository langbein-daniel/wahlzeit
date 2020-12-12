package org.wahlzeit.model.location;

import org.wahlzeit.services.Persistent;

public interface Coordinate extends Persistent, Cloneable{
    /**
     * @throws ConversionException if an internal conversion error occurred and
     *   the converted result does not represent this Coordinate.
     */
    CartesianCoordinate asCartesianCoordinate() throws ConversionException;
    /**
     * @throws ConversionException if an internal conversion error occurred and
     *   the converted result does not represent this Coordinate.
     */
    SphericalCoordinate asSphericalCoordinate() throws ConversionException;

    /**
     * @throws NullPointerException if the given Coordinate is null
     */
    double getCartesianDistance(Coordinate other) throws NullPointerException;
    /**
     * @throws NullPointerException if the given Coordinate is null
     */
    double getCentralAngle(Coordinate other) throws NullPointerException;

    /**
     * @throws NullPointerException if the given Coordinate is null
     */
    boolean isEqual(Coordinate other) throws NullPointerException;

    Object clone();
}
