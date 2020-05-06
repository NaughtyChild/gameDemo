

package uk.co.geolib.geoprojections;

/**
 * Class representing a circle.
 */
public class Mercator extends Projection {


    /**
     * Constructor.
     */
    public Mercator() {
        m_dStandardLongitude = 0;
    }

    /**
     * Constructor.
     */
    public void Project(double dLatY, double dLongX) {
        dLatY *= Constants.conRadiansPerDegree;

        dLongX *= Constants.conRadiansPerDegree;

        double x = dLongX - m_dStandardLongitude;

        double y = Math.log(Math.tan(dLatY) + 1 / Math.cos(dLatY));

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
     * Project the given x y to lat long using the input parameters to store
     * the result.
     */
    public void InverseProject(double dLatY, double dLongX) {
        double dLat = Math.atan(Math.sinh(dLatY));

        double dLong = dLongX + m_dStandardLongitude;

        dLatY = dLat * Constants.conDegreesPerRadian;

        dLongX = dLong * Constants.conDegreesPerRadian;
    }


    /**
     * Project the given x y to lat long using the input lat long class to get the result.
     */
    public void InverseProject(GeoLatLong rLatLong, double dX, double dY) {
        double dLat = Math.atan(Math.sinh(dX));

        double dLong = dY + m_dStandardLongitude;

        rLatLong.SetLat(dLat);

        rLatLong.SetLong(dLong);
    }

    /**
     *
     */
    public void SetStandardLongitude(double dStandardLongitude) {
        m_dStandardLongitude = dStandardLongitude * Constants.conRadiansPerDegree;
    }

    private double m_dStandardLongitude;
}