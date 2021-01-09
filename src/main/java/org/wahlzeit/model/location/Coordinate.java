package org.wahlzeit.model.location;

import org.wahlzeit.utils.Immutable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * All implementing Coordinate classes shall be immutable.
 * Furthermore an implementation shall be compatible with the Persistent interface, see writeOn(rset) below.
 */
public interface Coordinate extends Immutable {

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

    /**
     * Required for object persistence, see Persistent interface.
     *
     * As Coordinate is Immutable, the "void Persistent.readFrom(rset)" approach does not work. One is left
     * free on how to solve this.
     *
     * But the writeOn(rset) method from the Persistent interface can and shall be implemented as usual.
     */
    void writeOn(ResultSet rset) throws SQLException;
}
