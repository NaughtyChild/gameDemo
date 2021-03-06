

package uk.co.geolib.geoprojections;

/**
 * Transformation abstract class.
 */
public abstract class Transformation {
    /**
     * Transform the given point.
     */
    public abstract void Transform(double dx, double dy);

    /**
     * Inverse transform the given point.
     */
    public abstract void InverseTransform(double dx, double dy);

};