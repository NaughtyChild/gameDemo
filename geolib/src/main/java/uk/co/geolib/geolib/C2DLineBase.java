
package uk.co.geolib.geolib;

import java.util.ArrayList;


public abstract class C2DLineBase extends C2DBase {
    /**
     * Intersection with another
     */
    public abstract boolean Crosses(C2DLineBase Other, ArrayList<C2DPoint> IntersectionPts);

    /**
     * Intersection with another
     */
    public abstract boolean Crosses(C2DLineBase Other);

    /**
     * Minimum distance to a point.
     */
    public abstract double Distance(C2DPoint TestPoint, C2DPoint ptOnThis);

    /**
     * Minimum distance to another.
     */
    public abstract double Distance(C2DLineBase Other, C2DPoint ptOnThis, C2DPoint ptOnOther);

    /**
     * The point from.
     */
    public abstract C2DPoint GetPointFrom();

    /**
     * The point to.
     */
    public abstract C2DPoint GetPointTo();

    /**
     * The length.
     */
    public abstract double GetLength();

    /**
     * Reverse direction of the line.
     */
    public abstract void ReverseDirection();

    /**
     * Given a set of points on the line, this function creates sub lines defined by those points.
     * Required by intersection, union and difference functions in the C2DPolyBase class.
     */
    public abstract void GetSubLines(ArrayList<C2DPoint> PtsOnLine, ArrayList<C2DLineBase> LineSet);

    /**
     * Creats a copy of the line.
     */
    public abstract C2DLineBase CreateCopy();

    /**
     * Transform by a user defined transformation. e.g. a projection.
     */
    public abstract void Transform(CTransformation pProject);

    /**
     * Transform by a user defined transformation. e.g. a projection.
     */
    public abstract void InverseTransform(CTransformation pProject);


}