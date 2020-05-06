


package uk.co.geolib.geoprojections;

/**
 * Class representing an albers equal area projection.
 */
public class SlantRangeHeading extends LambertAzimuthalEqualArea {

    /**
     * Constructor.
     */
    public SlantRangeHeading() {
    }


    /**
     * Project the given lat long to x, y using the input parameters to store the
     * result.
     */
    public void Project(double dLatY, double dLongX) {
        super.Project(dLatY, dLongX);

        dLatY *= Constants.conEARTH_RADIUS_METRES;
        dLongX *= Constants.conEARTH_RADIUS_METRES;
    }


    /**
     * Project the given x y to lat long using the input parameters to store  the result.
     */
    public void InverseProject(double dLatY, double dLongX) {
        dLatY /= Constants.conEARTH_RADIUS_METRES;
        dLongX /= Constants.conEARTH_RADIUS_METRES;

        super.InverseProject(dLatY, dLongX);
    }
}


