


package uk.co.geolib.geoprojections;


/**
 * Class representing a circle.
 */
public class BonneProjection extends Projection {

    /**
     * Constructor.
     */
    public BonneProjection() {
        SetOrigin(40.0, 0);
    }


    /**
     * Projection.
     */
    public void Project(double dLatY, double dLongX) {
        dLatY *= Constants.conRadiansPerDegree;

        dLongX *= Constants.conRadiansPerDegree;

        double dP = m_cot_SP + m_dStandardParallel - dLatY;

        double dE = (dLongX - m_dCentralMeridian) * Math.cos(dLatY) / dP;

        dLongX = dP * Math.sin(dE);

        dLatY = m_cot_SP - dP * Math.cos(dE);
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
     * brief Project the given x y to lat long using the input parameters to store the result.
     */
    public void InverseProject(double dLatY, double dLongX) {
        double cot_SPLessY = m_cot_SP - dLatY;

        double dP;

        if (dLatY >= m_cot_SP)
            dP = -Math.sqrt(dLongX * dLongX + cot_SPLessY * cot_SPLessY);
        else
            dP = Math.sqrt(dLongX * dLongX + cot_SPLessY * cot_SPLessY);

        double dLat = m_cot_SP + m_dStandardParallel - dP;
        // Added a fix on the inverse which happens around the extemes of longitude 
        boolean bfix = false;
        if (dLat > Constants.conHALFPI) {
            bfix = true;
            dLat = m_cot_SP + m_dStandardParallel + dP;
        }

        double dA = Math.atan2(dLongX, (m_cot_SP - dLatY));

        double dLong = m_dCentralMeridian + (dP * dA) / Math.cos(dLat);

        if (bfix)
            dLong *= -1;

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
     * Sets the origin.
     */
    public void SetOrigin(double dStandardParallel, double dCentralMeridian) {
        m_dStandardParallel = dStandardParallel * Constants.conRadiansPerDegree;

        m_dCentralMeridian = dCentralMeridian * Constants.conRadiansPerDegree;

        m_cot_SP = 1 / Math.tan(m_dStandardParallel);
    }

    private double m_dStandardParallel;

    private double m_dCentralMeridian;

    private double m_cot_SP;

}