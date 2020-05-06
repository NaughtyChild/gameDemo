

package uk.co.geolib.geoprojections;

/**
 * Class representing an albers equal area projection.
 */
public class Sinusoidal extends Projection {

    /**
     * Constructor.
     */
    public Sinusoidal() {
        m_dStandardLongitude = 0;
    }


    /**
     * Constructor.
     */
    public void Project(double dLatY, double dLongX) {
        dLatY *= Constants.conRadiansPerDegree;

        dLongX *= Constants.conRadiansPerDegree;

        dLongX = (dLongX - m_dStandardLongitude) * Math.cos(dLatY);
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
     * Project the given x y to lat long using the input parameters to store
     * the result.
     */
    public void InverseProject(double dLatY, double dLongX) {
        dLongX = m_dStandardLongitude + dLongX / Math.cos(dLatY);

        dLatY *= Constants.conDegreesPerRadian;

        dLongX *= Constants.conDegreesPerRadian;
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
    public void SetStandardLongitude(double dStandardLongitude) {
        m_dStandardLongitude = dStandardLongitude * Constants.conRadiansPerDegree;
    }

    private double m_dStandardLongitude;
}