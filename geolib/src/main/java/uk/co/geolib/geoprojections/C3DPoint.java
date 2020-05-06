

package uk.co.geolib.geoprojections;


/**
 * Class for a 3D point.
 */
public class C3DPoint {

    /**
     * Constructor.
     */
    public C3DPoint() {
    }

    /**
     * Distance to another.
     */
    public double Distance(C3DPoint Other) {
        double dx = x - Other.x;
        double dy = y - Other.y;
        double dz = z - Other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * x co-ordinate.
     */
    public double x;
    /**
     * y co-ordinate.
     */
    public double y;
    /**
     * z co-ordinate.
     */
    public double z;
}