


package uk.co.geolib.geoprojections;

/**
 * Class representing a circle.
 */
public class GeoRotation extends Transformation {

    /**
     * Constructor.
     */
    public GeoRotation() {
    }


    /**
     * Transform the given point.
     */
    public void Transform(double dx, double dy) {
        Rotate(dy, dx);
    }

    /**
     * Inverse transform the given point.
     */
    public void InverseTransform(double dx, double dy) {
        InverseRotate(dy, dx);
    }


    /**
     * Set origin.
     */
    void SetOrigin(double dLatDegrees, double dLongDegrees) {
        m_Origin.SetLatDegrees(dLatDegrees);
        m_Origin.SetLongDegrees(dLongDegrees);
    }

    /**
     * Inverse rotate.
     */
    void Rotate(double dLatDegrees, double dLongDegrees) {
        GeoLatLong rLatLong = new GeoLatLong();
        rLatLong.SetLatDegrees(dLatDegrees);
        rLatLong.SetLongDegrees(dLongDegrees);

        Rotate(rLatLong);

        dLatDegrees = rLatLong.GetLatDegrees();
        dLongDegrees = rLatLong.GetLongDegrees();

    }

    /**
     * Inverse rotate.
     */
    void Rotate(GeoLatLong rLatLong) {
        Double dRange = 0.0;
        Double dHeading = 0.0;
        m_Origin.RangeAndHeading(rLatLong, dRange, dHeading);

        rLatLong.Set(dHeading, dRange, new GeoLatLong());

    }

    /**
     * Inverse rotate.
     */
    void InverseRotate(double dLatDegrees, double dLongDegrees) {
        GeoLatLong rLatLong = new GeoLatLong();
        rLatLong.SetLatDegrees(dLatDegrees);
        rLatLong.SetLongDegrees(dLongDegrees);

        InverseRotate(rLatLong);

        dLatDegrees = rLatLong.GetLatDegrees();
        dLongDegrees = rLatLong.GetLongDegrees();
    }

    /**
     * Inverse rotate.
     */
    void InverseRotate(GeoLatLong rLatLong) {
        Double dRange = 0.0;
        Double dHeading = 0.0;
        new GeoLatLong().RangeAndHeading(rLatLong, dRange, dHeading);

        rLatLong.Set(dHeading, dRange, m_Origin);

    }

    private GeoLatLong m_Origin = new GeoLatLong();

}