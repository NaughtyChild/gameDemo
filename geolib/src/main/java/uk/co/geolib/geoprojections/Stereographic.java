


package uk.co.geolib.geoprojections;

/**
 * Class representing an albers equal area projection.
 */
public class Stereographic extends Projection {
    /**
     * Constructor.
     */
    public Stereographic() {
        m_dStandardLatitude = 0;

        m_dStandardLongitude = 0;
    }

    /**
     * Project the given lat long to x, y using the input parameters to store the
     * result.
     */
    public void Project(double dLatY, double dLongX) {
        dLatY *= Constants.conRadiansPerDegree;

        dLongX *= Constants.conRadiansPerDegree;

        double sin_olat = Math.sin(m_dStandardLatitude);
        double sin_lat = Math.sin(dLatY);
        double cos_olat = Math.cos(m_dStandardLatitude);
        double cos_lat = Math.cos(dLatY);
        double cos_dlong = Math.cos(dLongX - m_dStandardLongitude);

        double k = 2 * Constants.conEARTH_RADIUS_METRES /
                (1 + sin_olat * sin_lat + cos_olat * cos_lat * cos_dlong);

        dLongX = k * cos_lat * Math.sin(dLongX - m_dStandardLongitude);

        dLatY = k * (cos_olat * sin_lat - sin_olat * cos_lat * cos_dlong);

    }


    /**
     * Project the given lat long to x, y using the input parameters to store the result and retaining
     * the lat long in the class passed.
     */
    public void Project(GeoLatLong rLatLong, double dx, double dy) {
        dy = rLatLong.GetLat();
        dx = rLatLong.GetLong();

        Project(dy, dx);
    }

    /**
     * Project the given x y to lat long using the input parameters to store  the result.
     */
    public void InverseProject(double dLatY, double dLongX) {
        double p = Math.sqrt(dLongX * dLongX + dLatY * dLatY);

        double c = 2 * Math.atan2(p, 2 * Constants.conEARTH_RADIUS_METRES);

        double cos_c = Math.cos(c);
        double sin_c = Math.sin(c);
        double sin_olat = Math.sin(m_dStandardLatitude);

        double cos_olat = Math.cos(m_dStandardLatitude);

        double dLat = Math.asin(cos_c * sin_olat + dLatY * sin_c * cos_olat / p);

        double dLong = Math.atan2(dLongX * sin_c, p * cos_olat * cos_c - dLatY * sin_olat * sin_c);

        dLong += m_dStandardLongitude;


        dLatY = dLat * Constants.conDegreesPerRadian;

        dLongX = dLong * Constants.conDegreesPerRadian;
    }


    /**
     * Project the given x y to lat long using the input lat long class to get the result.
     */
    public void InverseProject(GeoLatLong rLatLong, double dX, double dY) {
        double dLat = dY;

        double dLong = dX;

        InverseProject(dLat, dLong);

        rLatLong.SetLatDegrees(dLat);

        rLatLong.SetLongDegrees(dLong);
    }


    /**
     *
     */
    public void SetOrigin(double dLat, double dLong) {
        m_dStandardLatitude = dLat * Constants.conRadiansPerDegree;

        m_dStandardLongitude = dLong * Constants.conRadiansPerDegree;
    }

    private double m_dStandardLatitude;
    private double m_dStandardLongitude;
}