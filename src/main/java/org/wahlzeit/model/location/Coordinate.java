package org.wahlzeit.model.location;

import org.wahlzeit.services.Persistent;

public interface Coordinate extends Persistent, Cloneable {
    /**
     * @throws IllegalStateException if the class invariants of this object are not adhered
     * @throws ArithmeticException   if an internal conversion error occurred and
     *                               the converted Coordinate does not represent the same point in space as this Coordinate.
     */
    CartesianCoordinate asCartesianCoordinate() throws IllegalStateException, ArithmeticException;

    /**
     * @throws IllegalStateException if the class invariants of this object are not adhered
     * @throws ArithmeticException   if an internal conversion error occurred and
     *                               the converted Coordinate does not represent the same point in space as this Coordinate.
     */
    SphericalCoordinate asSphericalCoordinate() throws IllegalArgumentException, ArithmeticException;

    /**
     * @throws IllegalStateException if the class invariants of this object are not adhered
     * @throws NullPointerException  if the given Coordinate "other" is null
     * @throws ArithmeticException   if an internal calculation error occurred and
     *                               the resulting distance is not correct
     */
    double getCartesianDistance(Coordinate other) throws IllegalStateException, NullPointerException, ArithmeticException;

    /**
     * @throws IllegalStateException if the class invariants of this object are not adhered
     * @throws NullPointerException  if the given Coordinate "other" is null
     * @throws ArithmeticException   if an internal calculation error occurred and
     *                               the resulting angle is not correct
     */
    double getCentralAngle(Coordinate other) throws IllegalStateException, NullPointerException, ArithmeticException;

    /**
     * @throws IllegalStateException if the class invariants of this object are not adhered
     * @throws NullPointerException  if the given Coordinate "other" is null
     */
    boolean isEqual(Coordinate other) throws IllegalStateException, NullPointerException;

    Object clone();
}
