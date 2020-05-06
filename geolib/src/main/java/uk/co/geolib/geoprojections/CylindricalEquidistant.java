

package uk.co.geolib.geoprojections;

/**
 * Class representing a circle.
 */
public class CylindricalEquidistant extends Projection {

    /**
     * Constructor.
     */
    public CylindricalEquidistant() {
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

        dLongX = (dLongX - m_dStandardLongitude) * Math.cos(m_dStandardLatitude);
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
     * Project the given x y to lat long using the input parameters to store the result.
     */
    public void InverseProject(double dLatY, double dLongX) {
        dLongX = m_dStandardLongitude + dLongX / Math.cos(m_dStandardLatitude);

        dLatY *= Constants.conDegreesPerRadian;

        dLongX *= Constants.conDegreesPerRadian;
    }


    /**
     * Project the given x y to lat long using the input lat long class to get the result.
     */
    public void InverseProject(GeoLatLong rLatLong, double dX, double dY) {
        double dLatY = dY;

        double dLongX = dX;

        InverseProject(dLatY, dLongX);

        rLatLong.SetLat(dLatY);

        rLatLong.SetLong(dLongX);
    }

    /**
     *
     */
    public void SetOrigin(double dStandardLatitude, double dStandardLongitude) {
        m_dStandardLatitude = dStandardLatitude;

        m_dStandardLongitude = dStandardLongitude;
    }

    private double m_dStandardLongitude;
    private double m_dStandardLatitude;

}