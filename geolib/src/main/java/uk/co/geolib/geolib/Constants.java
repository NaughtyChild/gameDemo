
package uk.co.geolib.geolib;

public class Constants {
    /**
     *
     */
    public static double conRadiansPerDegree = 0.017453292519943295769236907684886;
    /**
     *
     */
    public static double conDegreesPerRadian = 57.295779513082320876798154814105;
    /**
     *
     */
    public static double conMetresPerNauticalMile = 1852.0;
    /**
     *
     */
    public static double conEARTH_RADIUS_METRES = 6370999.0; // Volumetric mean
    /**
     *
     */
    public static double conSecondsPerDegree = 3600.0;
    /**
     *
     */
    public static double conMinutesPerDegree = 60.0;
    /**
     *
     */
    public static double conMetersPerFoot = 0.3048;

    /**
     * This defined how close 2 doubles need to be to each other in order to be considered
     * Equal. If the difference between the 2 divided by 1 of them is less than this they are
     * equal.
     */
    public static double conEqualityTolerance = 0.0000000001;
    /**
     * Random number perturbation seed.
     */
    public static double coniPerturbationFactor = 0.0568412;
    /**
     * Random number perturbation seed.
     */
    public static double conjPerturbationFactor = 0.0345687;
    /**
     *
     */
    public static double conPI = 3.14159265358979323846;
    /**
     *
     */
    public static double conTWOPI = 6.283185307179586476925286766559;
    /**
     *
     */
    public static double conSIXTHPI = conPI / 6.0;        // 30 degrees
    /**
     *
     */
    public static double conQUARTPI = conPI / 4.0;        // 30 degrees
    /**
     *
     */
    public static double conTHIRDPI = conPI / 3.0;        // 60 degrees
    /**
     *
     */
    public static double conTWOTHIRDPI = conTWOPI / 3.0;    // 120 degrees
    /**
     *
     */
    public static double conHALFPI = conPI / 2.0;
    /**
     *
     */
    public static double conMetresPerDataMile = 1828.80;
    /**
     *
     */
    public static double conDataMilesPerNauticalMile = 1.0111666666666666666666666666667;
    /**
     * Earth semi-major axis of ellipsoid in meters
     */
    public static double conGeocent_Major = 6378137.0;
    /**
     * Earth semi-minor axis of ellipsoid in meters
     */
    public static double conGeocent_Minor = 6356752.3142;
    /**
     * Earth axis mean in meters
     */
    public static double conGeocent_Mean = 6367444.6571;
    /**
     * Earth axis eccentricity squared
     */
    public static double conGeocent_e2 = 0.0066943799901413800;
    /**
     * Earth axis 2nd eccentricity squared
     */
    public static double conGeocent_ep2 = 0.00673949675658690300;
    /**
     *
     */
    public static double conLbsPerKilogram = 2.2;
    /**
     *
     */
    public long conSecondsPerDay = 86400;
    /**
     *
     */
    public static double conSecondsPerHour = 3600.0;
    /**
     *
     */
    public static double conSecondsPerMinute = 60.0;
    /**
     *
     */
    public static double conE = 2.71828182845904523536;
    /**
     *
     */
    public static double conRoot2 = 1.4142135623731;
    /**
     *
     */
    public static double conRoot3 = 1.73205080756888;

}