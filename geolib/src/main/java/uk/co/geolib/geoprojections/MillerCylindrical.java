


package uk.co.geolib.geoprojections;

/**
 * Class representing a circle.
 */
public class MillerCylindrical extends Projection {
    /**
     * Constructor.
     */
    public MillerCylindrical() {
        m_dStandardLongitude = 0;
    }

    /**
     * Project the given lat long to x, y using the input parameters to store the
     * result.
     */
    public void Project(double dLatY, double dLongX) {
        dLatY *= Constants.conRadiansPerDegree;

        dLongX *= Constants.conRadiansPerDegree;


        dLongX = dLongX - m_dStandardLongitude;

        dLatY = 1.25 * Math.log(Math.tan(Constants.conQUARTPI + 0.2 * dLatY));

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
        //	m_dLat = 2.5 * a Math.tan(  Math.pow(conE, 0.8 * dLatY)) - 0.625 * Constants.conPI;	// orig

        dLatY = 5 * Math.atan(Math.pow(Constants.conE, 0.8 * dLatY)) - 1.25 * Constants.conPI;

        dLongX = dLongX + m_dStandardLongitude;

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
    public void SetStandardLongitude(double dStandardLongitude) {
        m_dStandardLongitude = dStandardLongitude * Constants.conRadiansPerDegree;
    }

    private double m_dStandardLongitude;
}