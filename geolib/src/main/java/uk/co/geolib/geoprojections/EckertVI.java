


package uk.co.geolib.geoprojections;

/**
 * Class representing a circle.
 */
public class EckertVI extends Projection {
    /**
     * Constructor.
     */
    public EckertVI() {
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

        double theta1 = dLatY; // Iteration start at lat

        double theta0 = theta1 + dTolerance * 10;

        int nMaxIt = 50;

        int nIt = 0;

        double sin_lat = Math.sin(dLatY);

        while (Math.abs(theta1 - theta0) > dTolerance) {
            theta0 = theta1;

            theta1 -= (theta0 + Math.sin(theta0) - (1 + Constants.conHALFPI) * sin_lat) /
                    (1 + Math.cos(theta0));

            nIt++;
            if (nIt == nMaxIt) {
                //	assert(0);

                return;
            }
        }

        double dDen = Math.sqrt(2 + Constants.conPI);

        dLongX = (dLongX - m_dStandardLongitude) * (1 + Math.cos(theta1)) /
                dDen;

        dLatY = 2 * theta1 / dDen;
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
        double dA = Math.sqrt(2 + Constants.conPI);

        double theta = 0.5 * dA * dLatY;

        dLatY = Math.asin((theta + Math.sin(theta)) / (1 + Constants.conHALFPI));

        dLongX = m_dStandardLongitude + (dA * dLongX) / (1 + Math.cos(theta));


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