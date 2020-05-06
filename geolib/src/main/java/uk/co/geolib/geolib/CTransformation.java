
package uk.co.geolib.geolib;

public abstract class CTransformation {
    /**
     * Transform the given point.
     */
    public abstract void Transform(double dx, double dy);

    /**
     * Inverse transform the given point.
     */
    public abstract void InverseTransform(double dx, double dy);
}