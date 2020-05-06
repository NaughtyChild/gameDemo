
package uk.co.geolib.geolib;

public abstract class C2DBase {

    /**
     * Moves the entity by the vector provided.
     */
    public abstract void Move(C2DVector Vector);

    /**
     * Rotates this to the right about the origin provided.
     *
     * @param dAng   The angle through which to rotate.
     * @param Origin The origin about which to rotate.
     */
    public abstract void RotateToRight(double dAng, C2DPoint Origin);

    /**
     * Grows the entity
     */
    public abstract void Grow(double dFactor, C2DPoint Origin);

    /**
     * Reflection in a point
     */
    public abstract void Reflect(C2DPoint Point);

    /**
     * Reflection in a line
     */
    public abstract void Reflect(C2DLine Line);

    /**
     * Distance to a point
     */
    public abstract double Distance(C2DPoint Point);

    /**
     * Return the bounding rect
     */
    public abstract void GetBoundingRect(C2DRect Rect);

    /**
     * Projects this onto the line provided with the interval on the line returned.
     */
    public abstract void Project(C2DLine Line, CInterval Interval);

    /**
     * Projects this onto the vector provided with the interval on the line returned.
     */
    public abstract void Project(C2DVector Vector, CInterval Interval);

    /**
     * Snaps this to the conceptual grid.
     */
    public abstract void SnapToGrid(CGrid grid);


}