

package uk.co.geolib.geoprojections;

/**
 * Class representing a LambertConformalConic.
 */
public class LambertConformalConic extends Projection {
    /**
     * Constructor.
     */
    public LambertConformalConic() {
        m_dStandardParallel1 = 50 * Constants.conRadiansPerDegree;

        m_dStandardParallel2 = -10 * Constants.conRadiansPerDegree;

        m_dOriginLat = 0;

        m_dOriginLong = 0;

        CalculateConstants();
    }


    /**
     * Project the given lat long to x, y using the input parameters to store the
     * result.
     */
    public void Project(double dLatY, double dLongX) {
        dLatY *= Constants.conRadiansPerDegree;

        dLongX *= Constants.conRadiansPerDegree;


        double p = m_dF * Math.pow(1 / Math.tan(Constants.conQUARTPI + dLatY / 2), m_dn);

        double p0 = m_dF * Math.pow(1 / Math.tan(Constants.conQUARTPI + m_dOriginLat / 2), m_dn);

        double x = p * Math.sin(m_dn * (dLongX - m_dOriginLong));

        double y = p0 - p * Math.cos(m_dn * (dLongX - m_dOriginLong));

        dLongX = x;

        dLatY = y;
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
        double p0 = m_dF * Math.pow(1 / Math.tan(Constants.conQUARTPI + m_dOriginLat / 2), m_dn);


        double p = Math.sqrt(dLongX * dLongX + (p0 - dLatY) * (p0 - dLatY));
        if (m_dn < 0)
            p = -p;

        double theta = Math.atan(dLongX / (p0 - dLatY));

        dLatY = 2 * Math.atan(Math.pow(m_dF / p, 1 / m_dn)) - Constants.conHALFPI;

        dLongX = m_dOriginLong + theta / m_dn;


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
    void SetStandardParallels(double dStandardParallel, double dStandardParalle2) {
        m_dStandardParallel1 = dStandardParallel * Constants.conRadiansPerDegree;

        m_dStandardParallel2 = dStandardParalle2 * Constants.conRadiansPerDegree;

        CalculateConstants();
    }


    /**
     *
     */
    void SetOrigin(double dLat, double dLong) {
        m_dOriginLat = dLat * Constants.conRadiansPerDegree;

        m_dOriginLong = dLong * Constants.conRadiansPerDegree;

        CalculateConstants();
    }

    /**
     *
     */
    private void CalculateConstants() {
        double cos_phi1 = Math.cos(m_dStandardParallel1);

        double n1 = Math.log(cos_phi1 * (1 / Math.cos(m_dStandardParallel2)));
        double n2 = Math.tan(Constants.conQUARTPI + m_dStandardParallel2 / 2);
        double n3 = 1 / (Math.tan(Constants.conQUARTPI + m_dStandardParallel1 / 2));
        m_dn = n1 / Math.log(n2 * n3);
        m_dF = cos_phi1 * Math.pow(Math.tan(Constants.conQUARTPI + m_dStandardParallel1 / 2), m_dn) / m_dn;
    }

    private double m_dStandardParallel1;

    private double m_dStandardParallel2;

    private double m_dOriginLat;

    private double m_dOriginLong;

    // derived constants

    private double m_dn;

    private double m_dF;
}