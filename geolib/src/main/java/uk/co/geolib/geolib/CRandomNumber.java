

package uk.co.geolib.geolib;

import java.util.Random;

public class CRandomNumber {
    /**
     * Constructor
     */
    public CRandomNumber() {
    }

    /**
     * Constructor, initialises to 2 double forming the bounds
     */
    public CRandomNumber(double dMin, double dMax) {
        Min = dMin;
        Max = dMax;
    }


    /**
     * Sets the random number bound to 2 doubles.
     */
    public void Set(double dMin, double dMax) {
        Min = dMin;
        Max = dMax;
    }

    /**
     * Sets the max.
     */
    public void SetMax(double dMax) {
        Max = dMax;
    }

    /**
     * Sets the min.
     */
    public void SetMin(double dMin) {
        Min = dMin;
    }

    /**
     * Access to the min.
     */
    public double GetMin() {
        return Min;
    }

    /**
     * Access to the max.
     */
    public double GetMax() {
        return Max;
    }

    /**
     * Gets a random number based on the settings.
     */
    public double Get() {
        double dResult = Min + (Max - Min) * _Random.nextDouble();

        return dResult;
    }

    /**
     * Gets an integer based on the settings. Sets up temporary new boundaries so that an interval
     * of e.g. 0.8 to 3.7 will become 1.0 to 3.0 allowing integers 1 and 2 only.
     */
    public int GetInt() {
        CRandomNumber Num = new CRandomNumber(Math.ceil(Min), Math.floor(Max) + 1.0);
        double dRes = Num.Get();
        if (dRes == (int) Num.GetMax())
            return (int) (dRes - 1);
        else
            return (int) dRes;
    }

    /**
     * Returns true or false.
     */
    public boolean GetBool() {
        return (_Random.nextDouble() > 0.5);
    }

    /**
     * The minimum possible value.
     */
    public double Min;

    /**
     * The maximum possible value.
     */
    public double Max;

    private static Random _Random = new Random();


}