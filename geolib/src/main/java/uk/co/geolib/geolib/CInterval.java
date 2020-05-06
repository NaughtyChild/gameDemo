
package uk.co.geolib.geolib;

public class CInterval {
    /**
     * Constructor
     */
    public CInterval() {
        dMin = 0;
        dMax = 0;
    }

    /**
     * Constructor
     */
    public CInterval(double dMinimum, double dMaximum) {
        dMin = dMinimum;
        dMax = dMaximum;
    }


    /**
     * Expands the interval to include the other
     */
    public void ExpandToInclude(CInterval Other) {
        if (Other.dMax > dMax) dMax = Other.dMax;
        if (Other.dMin < dMin) dMin = Other.dMin;
    }

    /**
     * Expands the interval to include the value
     */
    public void ExpandToInclude(double dValue) {
        if (dValue > dMax) dMax = dValue;
        else if (dValue < dMin) dMin = dValue;
    }

    /**
     * Returns the distance between the min and the max
     */
    public double GetLength() {
        return dMax - dMin;
    }

    /**
     * Assignement
     */
    public void Set(CInterval Other) {
        dMax = Other.dMax;
        dMin = Other.dMin;
    }

    /**
     * True if this overlaps the other
     */
    public boolean Overlaps(CInterval Other) {
        return (!IsBelow(Other) && !IsAbove(Other));
    }


    /**
     * True is this overlaps the other.
     *
     * @param Other
     * @param Overlap <returns></returns>
     */
    public boolean Overlaps(CInterval Other, CInterval Overlap) {
        if (Other.dMin < dMax &&
                Other.dMax > dMin) {
            Overlap.dMin = Math.max(Other.dMin, dMin);
            Overlap.dMax = Math.min(Other.dMax, dMax);
            return true;
        } else {
            return false;
        }
    }

    /**
     * True if this contains the value
     */
    public boolean Contains(double dValue) {
        return (dMin <= dValue && dValue <= dMax);
    }

    /**
     * True if this contains the other
     */
    public boolean Contains(CInterval Other) {
        return (Contains(Other.dMin) && Contains(Other.dMax));
    }

    /**
     * True if this is entirely above the other
     */
    public boolean IsAbove(CInterval Other) {
        return dMin > Other.dMax;
    }

    /**
     * True if this is entirely below the other
     */
    public boolean IsBelow(CInterval Other) {
        return dMax < Other.dMin;
    }

    /**
     * The min
     */
    public double dMin;

    /**
     * The max
     */
    public double dMax;


}