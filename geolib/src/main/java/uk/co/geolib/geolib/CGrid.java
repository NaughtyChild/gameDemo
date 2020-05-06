
package uk.co.geolib.geolib;

public class CGrid {
    /**
     * Enumeration for degenerate handling methods.
     */
    public enum eDegenerateHandling {
        /// <summary> No degenerate handling. </summary>
        None,
        /// <summary> Randomly perturb shapes to avoid coincident points. </summary>
        RandomPerturbation,
        /// <summary> Grid method. Calucate grid size automatically. </summary>
        DynamicGrid,
        /// <summary> Grid method. Grid size already set. </summary>
        PreDefinedGrid,
        /// <summary> Grid method. Shapes presnapped to the grid. </summary>
        PreDefinedGridPreSnapped,
    }

    ;

    /**
     * Constructor
     */
    public CGrid() {
        ;
    }


    /**
     * Sets the size of the grid.
     */
    public void SetGridSize(double dGridSize) {
        if (dGridSize != 0) {
            gridSize = Math.abs(dGridSize);
        }
    }

    /**
     * Finds a recommended minimum grid size to avoid point equality problems.
     */
    public static double GetMinGridSize(C2DRect cRect, boolean bRoundToNearestDecimalFactor) {
        // Find the furthest possible linear distance from the origin.
        C2DPoint pt = cRect.GetPointFurthestFromOrigin();

        double dRes = Math.abs(Math.max(pt.x, pt.y));
        // Now multiply this by the eq tol. Now, 2 points which are this far apart from each other
        // (in x and y) and at the edge of the rect would be considered only just not equal.
        dRes *= Constants.conEqualityTolerance;
        // Now multiple this by an avoidance factor.
        dRes *= const_dEqualityAvoidanceFactor;

        if (dRes == 0)
            dRes = const_dEqualityAvoidanceFactor;

        if (bRoundToNearestDecimalFactor) {
            double dRound = 0.0001;

            while (dRound >= dRes)
                dRound /= 10.0;

            while (dRound < dRes)
                dRound *= 10.0;

            dRes = dRound;
        }
        return dRes;
    }

    /**
     * Sets to the minimum recommended size for degenerate handling.
     */
    public void SetToMinGridSize(C2DRect cRect, boolean bRoundToNearestDecimalFactor) {
        SetGridSize(GetMinGridSize(cRect, bRoundToNearestDecimalFactor));

    }

    /**
     * Resets the degenerate count.
     */
    public void ResetDegenerateErrors() {
        degenerateErrors = 0;
    }

    /**
     * Used to log a degenerate error.
     */
    public void LogDegenerateError() {
        degenerateErrors++;
    }

    /**
     * Grid size.
     */
    private double gridSize = 0.0001;

    /**
     * Grid size.
     */
    public double getGridSize() {
        return gridSize;
    }

    /**
     * Degenerate errors.
     */
    private int degenerateErrors = 0;

    /**
     * Degenerate errors.
     */
    public int getDegenerateErrors() {
        return degenerateErrors;
    }

    /**
     * Degenerate Handling.
     */
    public eDegenerateHandling DegenerateHandling = eDegenerateHandling.None;

    /**
     * Equality Avoidance Factor.
     */
    private static double const_dEqualityAvoidanceFactor = 1000.0;

}