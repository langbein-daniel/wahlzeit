package org.wahlzeit.model.location;

import org.wahlzeit.utils.DoubleUtil;

import static org.wahlzeit.model.location.CartesianCoordinate.CENTER;
import static org.wahlzeit.utils.DoubleUtil.TWO_PI;

/**
 * Spherical Coordinate in mathematical context
 * (Not in physical or geographical context)
 * <p>
 * Center of coordinate-system: Center of mass of the Earth
 * Unit (of radius): Meters
 * Unit (of phi and theta): radians (rad)
 */
public class SphericalCoordinate extends AbstractCoordinate {
    /**
     * radial distance from center in meters; radius >= 0
     */
    protected double radius;

    /**
     * inclination (polar angle or colatitude) θ;
     * angle from the zenith direction to the line segment "center to this point"
     * in the range [0, pi]
     * <p>
     * <p>
     * Latitude in Geography (Lat., φ, or phi): Elevation angle in degrees up from the equator
     * in the range [-90°, +90°]; it goes in opposite direction as theta and
     * can be calculated with: elevation = 90° - inclination
     */
    protected double theta;

    /**
     * azimuth (azimuthal angle) φ;
     * angle measured from the azimuth reference direction to the orthogonal
     * projection of the line segment "center to this point" on the reference plane
     * in the range [0, 2*pi)
     * <p>
     * <p>
     * Longitude in Geography (Long., λ, or lambda): Angle in degrees east (positive values)
     * or west (negative values) from the prime meridian (IERS Reference Meridian (IRM)),
     * in the range (-180°, +180°]
     */
    protected double phi;

    public SphericalCoordinate(double radius, double theta, double phi) {
        assertArgumentIsValidRadius(radius);
        assertArgumentIsValidTheta(theta);
        assertArgumentIsValidPhi(phi);

        doSetRadius(radius);
        doSetTheta(theta);
        doSetPhi(phi);

        assertClassInvariants();
    }

    public SphericalCoordinate(SphericalCoordinate c) {
        this(c.getRadius(), c.getTheta(), c.getPhi());
    }

    public SphericalCoordinate(Coordinate coordinate) {
        assertClassInvariants();

        double radius, theta, phi;
        {
            CartesianCoordinate cartesian = coordinate.asCartesianCoordinate();

            if (cartesian.equals(CENTER)) {
                radius = 0.0;
                theta = 0.0;
                phi = 0.0;
            } else {
                double x = cartesian.getX();
                double y = cartesian.getY();
                double z = cartesian.getZ();

                /* distance from center = sqrt(x^2 + y^2 + z^2) */
                radius = cartesian.getCartesianDistance(CENTER);

                /* arctan( sqrt(x^2 + y^2) / z  ) = arccos( z / radius ) */
                // setTheta(Math.atan(Math.hypot(x, y) / z));
                theta = Math.acos(z / radius);

                /* arctan(y, x) may produce results with wrong sign ! */
                //phi = Math.atan(y / x);
                /* atan2(y, x) */
                phi = Math.atan2(y, x);
            }
        }

        assertResultIsValidRadius(radius);
        assertResultIsValidTheta(theta);
        assertResultIsValidPhi(phi);

        doSetRadius(radius);
        doSetTheta(theta);
        doSetPhi(phi);

        assertClassInvariants();
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    public CartesianCoordinate doAsCartesianCoordinate() {
        return new CartesianCoordinate(this);
    }

    public SphericalCoordinate asSphericalCoordinate() {
        return doAsSphericalCoordinate();
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    protected SphericalCoordinate doAsSphericalCoordinate() {
        assertClassInvariants();
        return this;
    }

//    @Override
//    public double getCentralAngle(Coordinate other) {
//        if (this.isEqual(CENTER) || other.isEqual(CENTER)) {
//            return Double.NaN;
//        }
//        SphericalCoordinate otherSpherical = other.asSphericalCoordinate();
//        double otherTheta = otherSpherical.getTheta();
//        double otherPhi = otherSpherical.getPhi();
//        double deltaPhi = Math.abs(phi - otherPhi);
//
//        // don't use one simple but mathematically correct formula
//        // as there might be large inaccuracies with small angles
//        // from calculus with double values
//        // https://en.wikipedia.org/wiki/Great-circle_distance#Computational_formulas
//
////        { // variant (I): https://en.wikipedia.org/wiki/Great-circle_distance#Formulae
////            return Math.acos(
////                    Math.cos(theta)*Math.cos(otherTheta) +
////                    Math.sin(theta)*Math.sin(otherTheta)*Math.cos(deltaPhi));
////        }
//
//        { // variant (II): https://en.wikipedia.org/wiki/Great-circle_distance#Computational_formulas
//            double cosDeltaPhi = Math.cos(deltaPhi);
//            double sinTheta = Math.sin(theta);
//            double sinOtherTheta = Math.sin(otherTheta);
//            double cosTheta = Math.cos(theta);
//            double cosOtherTheta = Math.cos(otherTheta);
//
//            double mult1 = sinOtherTheta * Math.sin(deltaPhi);
//            double mult2 = sinTheta * cosOtherTheta - cosTheta * sinOtherTheta * cosDeltaPhi;
//
//            double dividend = Math.sqrt(mult1 * mult1 + mult2 * mult2);
//            double divisor = cosTheta * cosOtherTheta + sinTheta * sinOtherTheta * cosDeltaPhi;
//
//            return Math.atan(dividend / divisor);
//        }
//    }

    //=== Getter and Setter ===

    /**
     * @methodtype get
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @methodtype set
     */
    public void setRadius(double radius) {
        assertClassInvariants();
        assertArgumentIsValidRadius(radius);

        doSetRadius(radius);

        assertClassInvariants();
    }

    protected void doSetRadius(double radius) {
        this.radius = radius;
        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public double getTheta() {
        return theta;
    }

    /**
     * @methodtype set
     */
    public void setTheta(double theta) {
        assertClassInvariants();
        assertArgumentIsValidTheta(theta);

        doSetTheta(theta);

        assertClassInvariants();
    }

    protected void doSetTheta(double theta) {
        this.theta = theta;
        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public double getPhi() {
        return phi;
    }

    /**
     * @methodtype set
     */
    public void setPhi(double phi) {
        assertClassInvariants();
        assertArgumentIsValidPhi(phi);

        doSetPhi(phi);

        assertClassInvariants();
    }

    protected void doSetPhi(double phi) {
        // phi shall be in the range [0, 2*pi)
        this.phi = DoubleUtil.posRemainder(phi, TWO_PI, SCALE);
        incWriteCount();
    }

    @Override
    public Object clone() {
        return new SphericalCoordinate(getRadius(), getTheta(), getPhi());
    }

    @Override
    public String toString() {
        return "SphericalCoordinate{" +
                "radius=" + radius +
                ", theta=" + theta +
                ", phi=" + phi +
                '}';
    }

    //=== Persistence Methods ===

    @Override
    public void doReadFrom(CartesianCoordinate coordinate) {
        SphericalCoordinate spherical = coordinate.asSphericalCoordinate();

        phi = spherical.getPhi();
        theta = spherical.getTheta();
        radius = spherical.getRadius();
    }

    //=== Assertions ===

    @Override
    protected void assertClassInvariants() {
        assertStateIsValidRadius(getRadius());
        assertStateIsValidTheta(getTheta());
        assertStateIsValidPhi(getPhi());
    }


    protected void assertArgumentIsValidRadius(double radius) {
        if (!isValidRadius(radius)) {
            throw new IllegalArgumentException("radius must be positive finite");
        }
    }

    protected void assertResultIsValidRadius(double radius) {
        if (!isValidRadius(radius)) {
            throw new ArithmeticException("calculated radius is not positive finite");
        }
    }

    protected void assertStateIsValidRadius(double radius) {
        if (!isValidRadius(radius)) {
            throw new IllegalStateException("radius is not positive finite");
        }
    }

    protected boolean isValidRadius(double radius) {
        return DoubleUtil.isPositiveFinite(radius);
    }


    protected void assertArgumentIsValidTheta(double theta) {
        if (!isValidTheta(theta)) {
            throw new IllegalArgumentException("theta must be in range [0, pi]");
        }
    }

    protected void assertResultIsValidTheta(double theta) {
        if (!isValidTheta(theta)) {
            throw new ArithmeticException("calculated theta is not in range [0, pi]");
        }
    }

    protected void assertStateIsValidTheta(double theta) {
        if (!isValidTheta(theta)) {
            throw new IllegalStateException("theta not in range [0, pi]");
        }
    }

    protected boolean isValidTheta(double theta) {
        return Double.isFinite(theta) && theta <= Math.PI;
    }


    protected void assertArgumentIsValidPhi(double phi) {
        if (!isValidPhi(phi)) {
            throw new IllegalArgumentException("phi must be finite");
        }
    }

    protected void assertResultIsValidPhi(double phi) {
        if (!isValidPhi(phi)) {
            throw new ArithmeticException("calculated phi is not finite");
        }
    }

    protected void assertStateIsValidPhi(double phi) {
        if (!isValidPhi(phi)) {
            throw new IllegalStateException("phi is not finite");
        }
    }

    protected boolean isValidPhi(double phi) {
        return Double.isFinite(phi);
    }
}
