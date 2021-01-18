package org.wahlzeit.model.location;

import org.wahlzeit.contract.PatternInstance;
import org.wahlzeit.utils.DoubleUtil;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.PI;

/**
 * Spherical Coordinate in mathematical context
 * (Not in physical or geographical context)
 * <p>
 * Center of coordinate-system: Center of mass of the Earth
 * Unit (of radius): Meters
 * Unit (of phi and theta): radians (rad)
 */
@PatternInstance(
        patternName = "Flyweight",
        participants = SphericalCoordinate.class
)
public class SphericalCoordinate extends AbstractCoordinate {
    private static final Map<Integer, SphericalCoordinate> sharedObjects = new HashMap<>();


    /**
     * radial distance from center in meters; radius >= 0
     */
    protected final double radius;

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
    protected final double theta;

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
    protected final double phi;

    /**
     * When comparing or saving Coordinates they are always converted
     * to CartesianCoordinates. Therefore it improves the performance
     * to only once convert a SphericalCoordinate to a Cartesian one
     * and remember the cartesian representation.
     */
    protected final CartesianCoordinate cartesianRepresentation;

    /**
     * @return Null if a matching SphericalCoordinate object is not yet shared,
     * or returns the corresponding SphericalCoordinate Object
     */
    public static SphericalCoordinate getCoordinateFromHash(int hashCode) {
        return sharedObjects.get(hashCode);
    }

    /**
     * @param radius must be positive finite
     * @param theta  angle in radians in the range [0, pi]
     * @param phi    angle in radians, must be finite
     * @throws IllegalArgumentException if the given parameter violate the constraints
     * @methodtype factory
     * @methodproperties composed
     */
    public static SphericalCoordinate newSphericalCoordinate(double radius, double theta, double phi) {
        SphericalCoordinate newCoordinate = new SphericalCoordinate(radius, theta, phi);
        return shareCoordinateValue(newCoordinate);
    }

    /**
     * @param radius must be positive finite
     * @param theta  angle in radians in the range [0, pi]
     * @param phi    angle in radians, must be finite
     * @throws IllegalArgumentException if the given parameter violate the constraints
     * @methodtype constructor
     */
    private SphericalCoordinate(double radius, double theta, double phi) {
        assertArgumentIsValidRadius(radius);
        assertArgumentIsValidTheta(theta);
        assertArgumentIsValidPhi(phi);

        this.radius = radius;
        this.theta = theta;
        this.phi = phi;
        this.cartesianRepresentation = SphericalCoordinate.doAsCartesianCoordinate(radius, theta, phi);

        assertClassInvariants();
    }

    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    public CartesianCoordinate doAsCartesianCoordinate() {
        return cartesianRepresentation;
    }

    public static CartesianCoordinate doAsCartesianCoordinate(double radius, double theta, double phi) {
        double x, y, z;
        {
            if (DoubleUtil.isEqual(radius, 0.0, SCALE)) {
                x = 0.0;
                y = 0.0;
                z = 0.0;
            } else {
                double sinTheta = Math.sin(theta);

                x = radius * sinTheta * Math.cos(phi);
                y = radius * sinTheta * Math.sin(phi);
                z = radius * Math.cos(theta);
            }
        }

        CartesianCoordinate.assertResultIsValidXYZ(x);
        CartesianCoordinate.assertResultIsValidXYZ(y);
        CartesianCoordinate.assertResultIsValidXYZ(z);

        return CartesianCoordinate.newCartesianCoordinate(x, y, z);
    }

    @Override
    public SphericalCoordinate asSphericalCoordinate(){
        return doAsSphericalCoordinate();
    }
    /**
     * @methodtype conversion
     * @methodproperties primitive
     */
    @Override
    protected SphericalCoordinate doAsSphericalCoordinate() {
        return this;
    }

    //=== Getter  ===

    /**
     * @methodtype get
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @methodtype get
     */
    public double getTheta() {
        return theta;
    }

    /**
     * @methodtype get
     */
    public double getPhi() {
        return phi;
    }

    //=== Other ===

    @Override
    public String toString() {
        return "SphericalCoordinate in degrees{" +
                "radius=" + radius +
                ", theta=" + 180.0 * theta / PI +
                ", phi=" + 180.0 * phi / PI +
                '}';
    }

    @Override
    public int hashCode() {
        return cartesianRepresentation.hashCode();
    }

    /**
     * Objects of this class are immutable shared value objects.
     *
     * @param coordinate If the given value is not yet known/shared, it is added to a Map
     * @return Reference to a shared value object
     */
    protected static SphericalCoordinate shareCoordinateValue(SphericalCoordinate coordinate) {
        int hash = coordinate.hashCode();
        SphericalCoordinate sharedCoordinate = sharedObjects.get(hash);

        if (sharedCoordinate == null) {
            sharedCoordinate = sharedObjects.get(hash);
            if (sharedCoordinate == null) {
                sharedObjects.put(hash, coordinate);
                sharedCoordinate = coordinate;
            }
        }

        return sharedCoordinate;
    }

    //=== Assertions ===

    @Override
    protected void assertClassInvariants() {
        assertStateIsValidRadius(getRadius());
        assertStateIsValidTheta(getTheta());
        assertStateIsValidPhi(getPhi());

        // The cartesian representation shall be equal to this representation
        assertArgumentNotNull(cartesianRepresentation);
        assertResultIsEqual(cartesianRepresentation);
    }


    protected void assertArgumentIsValidRadius(double radius)
            throws IllegalArgumentException {
        if (!isValidRadius(radius)) {
            throw new IllegalArgumentException("radius must be positive finite");
        }
    }

    protected static void assertResultIsValidRadius(double radius)
            throws ArithmeticException {
        if (!isValidRadius(radius)) {
            throw new ArithmeticException("calculated radius is not positive finite");
        }
    }

    protected void assertStateIsValidRadius(double radius)
            throws IllegalStateException {
        if (!isValidRadius(radius)) {
            throw new IllegalStateException("radius is not positive finite");
        }
    }

    protected static boolean isValidRadius(double radius) {
        return DoubleUtil.isPositiveFinite(radius);
    }


    protected void assertArgumentIsValidTheta(double theta)
            throws IllegalArgumentException {
        if (!isValidTheta(theta)) {
            throw new IllegalArgumentException("theta must be in range [0, pi]");
        }
    }

    protected static void assertResultIsValidTheta(double theta)
            throws ArithmeticException {
        if (!isValidTheta(theta)) {
            throw new ArithmeticException("calculated theta is not in range [0, pi]");
        }
    }

    protected void assertStateIsValidTheta(double theta)
            throws IllegalStateException {
        if (!isValidTheta(theta)) {
            throw new IllegalStateException("theta not in range [0, pi]");
        }
    }

    protected static boolean isValidTheta(double theta) {
        return Double.isFinite(theta) && theta >= 0.0 && theta <= PI;
    }


    protected void assertArgumentIsValidPhi(double phi)
            throws IllegalArgumentException {
        if (!isValidPhi(phi)) {
            throw new IllegalArgumentException("phi must be finite");
        }
    }

    protected static void assertResultIsValidPhi(double phi)
            throws ArithmeticException {
        if (!isValidPhi(phi)) {
            throw new ArithmeticException("calculated phi is not finite");
        }
    }

    protected void assertStateIsValidPhi(double phi)
            throws IllegalStateException {
        if (!isValidPhi(phi)) {
            throw new IllegalStateException("phi is not finite");
        }
    }

    protected static boolean isValidPhi(double phi) {
        return Double.isFinite(phi);
    }
}
