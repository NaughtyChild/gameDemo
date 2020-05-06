

package uk.co.geolib.geoprojections;

/**
 * Class representing an albers equal area projection.
 */
public class Orthographic extends Projection {

    /**
     * Constructor.
     */
    public Orthographic() {
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

        double cos_Lat = Math.cos(dLatY);

        double x = cos_Lat * Math.sin(dLongX - m_dStandardLongitude);

        double y = Math.cos(m_dStandardLatitude) * Math.sin(dLatY) -
                Math.sin(m_dStandardLatitude) * cos_Lat * Math.cos(dLongX - m_dStandardLongitude);

        dLatY = y;

        dLongX = x;
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

        double c = Math.asin(p);

        double cos_c = Math.cos(c);

        double sin_c = Math.sin(c);

        double sin_olat = Math.sin(m_dStandardLatitude);

        double cos_olat = Math.cos(m_dStandardLatitude);

        double dLat = Math.asin(cos_c * sin_olat + dLatY * sin_c * cos_olat / p);

        double dLong = m_dStandardLongitude +
                Math.atan2(dLongX * sin_c, p * cos_olat * cos_c - dLatY * sin_olat * sin_c);


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
    void SetOrigin(double dLat, double dLong) {
        m_dStandardLatitude = dLat * Constants.conRadiansPerDegree;

        m_dStandardLongitude = dLong * Constants.conRadiansPerDegree;

    }

    private double m_dStandardLatitude;

    private double m_dStandardLongitude;
}