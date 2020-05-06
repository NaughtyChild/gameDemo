


package uk.co.geolib.geoprojections;

/**
 * Class representing an albers equal area projection.
 */
public class Mollweide extends Projection {

    /**
     * Constructor.
     */
    public Mollweide() {
        m_dStandardLongitude = 0;
    }


    /**
     * Project the given lat long to x, y using the input parameters to store the
     * result.
     */
    public void Project(double dLatY, double dLongX) {
        dLatY *= Constants.conRadiansPerDegree;

        dLongX *= Constants.conRadiansPerDegree;

        double dTolerance = 0.00000001;

        double theta1 = 2 * Math.asin(2 * dLatY / Constants.conPI); // Iteration start

        double theta0 = theta1 + dTolerance * 10.0;

        int nMaxIt = 50;

        int nIt = 0;

        double sin_lat = Math.sin(dLatY);

        while (Math.abs(theta1 - theta0) > dTolerance) {
            theta0 = theta1;

            theta1 -= (theta0 + Math.sin(theta0) - Constants.conPI * sin_lat) /
                    (1 + Math.cos(theta0));

            nIt++;
            if (nIt == nMaxIt) {
                //	assert(0);
                return;
            }
        }

        theta1 = theta1 / 2.0;

        dLongX = 2.0 * Constants.conRoot2 * (dLongX - m_dStandardLongitude) * Math.cos(theta1) / Constants.conPI;

        dLatY = Constants.conRoot2 * Math.sin(theta1);

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
        double theta = Math.asin(dLatY / Constants.conRoot2);

        dLatY = Math.asin((2 * theta + Math.sin(2 * theta)) / Constants.conPI);

        dLongX = m_dStandardLongitude + Constants.conPI * dLongX / (2 * Constants.conRoot2 * Math.cos(theta));

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