

package uk.co.geolib.geoprojections;

/**
 * Class representing an albers equal area projection.
 */
public class Polyconic extends Projection {

    /**
     * Constructor.
     */
    public Polyconic() {
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

        double E = (dLongX - m_dStandardLongitude) * Math.sin(dLatY);

        dLongX = Math.sin(E) / Math.tan(dLatY);

        dLatY = (dLatY - m_dStandardLatitude) + (1 - Math.cos(E)) / Math.tan(dLatY);
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
        double A = m_dStandardLatitude + dLatY;

        double B = dLongX * dLongX + A * A;

        double dTolerance = 0.00000001;

        double theta1 = A;
        if (dLongX != 0)
            theta1 = A * Math.abs(dLongX) * 0.1; // Iteration start

        double theta0 = theta1 + dTolerance * 10.0;

        int nMaxIt = 500;

        int nIt = 0;

        while (Math.abs(theta1 - theta0) > dTolerance) {
            theta0 = theta1;

            double tan_theta = Math.tan(theta0);

            theta1 -= (A * (theta0 * tan_theta + 1) - theta0 - 0.5 * (theta0 * theta0 + B) * Math.tan(theta0)) /
                    ((theta0 - A) / Math.tan(theta0) - 1);

            nIt++;
            if (nIt == nMaxIt) {
                //	assert(0);
                break;
            }
        }

        dLatY = theta1;

        dLongX = Math.asin(dLongX * Math.tan(theta1)) / Math.sin(theta1) + m_dStandardLongitude;

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
    void SetOrigin(double dLat, double dLong) {
        m_dStandardLatitude = dLat * Constants.conRadiansPerDegree;

        m_dStandardLongitude = dLong * Constants.conRadiansPerDegree;

    }

    private double m_dStandardLatitude;

    private double m_dStandardLongitude;
}