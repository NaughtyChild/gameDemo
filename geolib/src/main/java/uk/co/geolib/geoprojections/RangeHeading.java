


package uk.co.geolib.geoprojections;

/**
 * Class representing an albers equal area projection.
 */
public class RangeHeading extends Projection {

    /**
     * Constructor.
     */
    public RangeHeading() {

    }

    /**
     * Project the given lat long to x, y using the input parameters to store the
     * result.
     */
    public void Project(double dLatY, double dLongX) {
        GeoLatLong LatLong = new GeoLatLong(dLatY * Constants.conRadiansPerDegree, dLongX * Constants.conRadiansPerDegree);

        Double dRange = 0.0;
        Double dHeading = 0.0;

        m_Origin.RangeAndHeading(LatLong, dRange, dHeading);

        dLatY = dRange * Math.cos(dHeading);

        dLongX = dRange * Math.sin(dHeading);
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
        double dHdng = Math.atan2(dLongX, dLatY);

        double dRange = Math.sqrt(dLongX * dLongX + dLatY * dLatY);

        GeoLatLong Result = new GeoLatLong();

        Result.Set(dHdng, dRange, m_Origin);

        dLatY = Result.GetLatDegrees();

        dLongX = Result.GetLongDegrees();
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
        m_Origin.SetLatDegrees(dLat);

        m_Origin.SetLongDegrees(dLong);

    }

    /**
     *
     */
    GeoLatLong GetOrigin() {
        return m_Origin;
    }

    GeoLatLong m_Origin = new GeoLatLong();
}