
package uk.co.geolib.geolib;

import java.util.ArrayList;

public class C2DTriangle extends C2DBase {

    /**
     * Constructor.
     */
    public C2DTriangle() {
    }

    /**
     * Constructor.
     *
     * @param pt1 Point 1.
     * @param pt2 Point 2.
     * @param pt3 Point 3.
     */
    public C2DTriangle(C2DPoint pt1, C2DPoint pt2, C2DPoint pt3) {
        p1.Set(pt1);
        p2.Set(pt2);
        p3.Set(pt3);
    }


    /**
     * Assignement.
     *
     * @param pt1 Point 1.
     * @param pt2 Point 2.
     * @param pt3 Point 3.
     */
    public void Set(C2DPoint pt1, C2DPoint pt2, C2DPoint pt3) {
        p1.Set(pt1);
        p2.Set(pt2);
        p3.Set(pt3);
    }

    /**
     * True if the 3 are collinear.
     */
    public boolean Collinear() {
        return (GetAreaSigned() == 0);
    }

    /**
     * Returns the area.
     */
    public double GetArea() {
        return Math.abs(GetAreaSigned(p1, p2, p3));
    }

    /**
     * Returns the area signed (indicating weather clockwise or not).
     */
    public double GetAreaSigned() {
        return GetAreaSigned(p1, p2, p3);
    }

    /**
     * True if clockwise.
     */
    public boolean IsClockwise() {
        return GetAreaSigned() < 0;
    }

    /**
     * Returns the circumcentre.
     */
    public C2DPoint GetCircumCentre() {
        return C2DTriangle.GetCircumCentre(p1, p2, p3);
    }

    /**
     * Returns the Fermat point (also known as the Torricelli point).
     */
    public C2DPoint GetFermatPoint() {
        return C2DTriangle.GetFermatPoint(p1, p2, p3);
    }

    /**
     * InCentre function.
     */
    public C2DPoint GetInCentre() {
        return C2DTriangle.GetInCentre(p1, p2, p3);
    }

    /**
     * Returns the perimeter.
     */
    public double GetPerimeter() {
        return p1.Distance(p2) + p2.Distance(p3) + p3.Distance(p1);
    }

    /**
     * Returns true if the point is contained.
     */
    public boolean Contains(C2DPoint ptTest) {
        boolean bClockwise = GetAreaSigned() < 0;

        if ((GetAreaSigned(p1, p2, ptTest) < 0) ^ bClockwise)
            return false;

        if ((GetAreaSigned(p2, p3, ptTest) < 0) ^ bClockwise)
            return false;

        if ((GetAreaSigned(p3, p1, ptTest) < 0) ^ bClockwise)
            return false;

        return true;
    }

    /**
     * Moves this point by the vector given.
     *
     * @param Vector The vector.
     */
    public void Move(C2DVector Vector) {
        p1.Move(Vector);
        p2.Move(Vector);
        p3.Move(Vector);
    }

    /**
     * Rotates this to the right about the origin provided.
     *
     * @param dAng   The angle through which to rotate.
     * @param Origin The origin about which to rotate.
     */
    public void RotateToRight(double dAng, C2DPoint Origin) {
        p1.RotateToRight(dAng, Origin);
        p2.RotateToRight(dAng, Origin);
        p3.RotateToRight(dAng, Origin);
    }

    /**
     * Grows the triangle.
     *
     * @param dFactor The factor to grow by.
     * @param Origin  The origin through which to grow.
     */
    public void Grow(double dFactor, C2DPoint Origin) {
        p1.Grow(dFactor, Origin);
        p2.Grow(dFactor, Origin);
        p3.Grow(dFactor, Origin);
    }

    /**
     * Reflects the triangle.
     *
     * @param Point The point to reflect through.
     */
    public void Reflect(C2DPoint Point) {
        p1.Reflect(Point);
        p2.Reflect(Point);
        p3.Reflect(Point);
    }

    /**
     * Reflects the in the line given.
     *
     * @param Line The line to reflect through.
     */
    public void Reflect(C2DLine Line) {
        p1.Reflect(Line);
        p2.Reflect(Line);
        p3.Reflect(Line);
    }

    /**
     * Distance to a point.
     *
     * @param ptTest The test point.
     */
    public double Distance(C2DPoint ptTest) {
        C2DPoint p1 = new C2DPoint();
        return Distance(ptTest, p1);
    }

    /**
     * Distance to a point.
     *
     * @param ptTest   The test point.
     * @param ptOnThis Output. The closest point on the triangle.
     */
    public double Distance(C2DPoint ptTest, C2DPoint ptOnThis) {
        double dArea = GetAreaSigned();
        Boolean BTemp = true;
        // Construct the lines.
        C2DLine Line12 = new C2DLine(p1, p2);
        C2DLine Line23 = new C2DLine(p2, p3);
        C2DLine Line31 = new C2DLine(p3, p1);

        if (dArea == 0) {
            // Colinear so find the biggest line and return the distance from that
            double d1 = Line12.GetLength();
            double d2 = Line23.GetLength();
            double d3 = Line31.GetLength();
            if (d1 > d2 && d1 > d3)
                return Line12.Distance(ptTest, ptOnThis);
            else if (d2 > d3)
                return Line23.Distance(ptTest, ptOnThis);
            else
                return Line31.Distance(ptTest, ptOnThis);
        }
        // Find out it the triangle is clockwise or not.
        boolean bClockwise = dArea < 0;

        // Set up some pointers to record the lines that the point is "above", "above" meaning that the
        // point is on the opposite side of the line to the rest of the triangle
        C2DLine LineAbove1 = null;
        C2DLine LineAbove2 = null;

        // Find out which Lines have the point above.
        if (Line12.IsOnRight(ptTest) ^ bClockwise)  // if the pt is on the opposite side to the triangle
            LineAbove1 = Line12;
        if (Line23.IsOnRight(ptTest) ^ bClockwise) {
            if (LineAbove1 != null)
                LineAbove2 = Line23;
            else
                LineAbove1 = Line23;
        }
        if (Line31.IsOnRight(ptTest) ^ bClockwise) {
            if (LineAbove1 != null) {
                // We can't have all the lines with the point above.
                // Debug.Assert(LineAbove2 != null);
                LineAbove2 = Line31;
            } else
                LineAbove1 = Line31;
        }

        // Check for containment (if there isn't a single line that its above then it must be inside)
        if (LineAbove1 == null) {
            // Pt inside so project onto all the lines and find the closest projection (there must be one).

            // Set up a record of the point projection on the lines.
            C2DPoint ptOnLine = new C2DPoint();
            boolean bSet = false;
            double dMinDist = 0;

            if (ptTest.ProjectsOnLine(Line12, ptOnLine, BTemp)) {
                dMinDist = ptTest.Distance(ptOnLine);
                ptOnThis.Set(ptOnLine);
                bSet = true;
            }
            if (ptTest.ProjectsOnLine(Line23, ptOnLine, BTemp)) {
                double dDist = ptTest.Distance(ptOnLine);
                if (!bSet || dDist < dMinDist) {
                    dMinDist = dDist;
                    ptOnThis.Set(ptOnLine);
                    bSet = true;
                }
            }
            if (ptTest.ProjectsOnLine(Line31, ptOnLine, BTemp)) {
                double dDist = ptTest.Distance(ptOnLine);
                if (!bSet || dDist < dMinDist) {
                    dMinDist = dDist;
                    ptOnThis.Set(ptOnLine);
                    bSet = true;
                }
            }
            // Debug.Assert(bSet);
            return -dMinDist; //-ve if inside
        } else if (LineAbove2 == null) {
            // it is only above 1 of the lines so simply return the distance to that line
            return LineAbove1.Distance(ptTest, ptOnThis);
        } else {
            // It's above 2 lines so first check them both for projection. Can only be projected on 1.
            // If the point can be projected onto the line then that's the closest point.
            C2DPoint ptOnLine = new C2DPoint();
            if (ptTest.ProjectsOnLine(LineAbove1, ptOnLine, BTemp)) {
                ptOnThis = ptOnLine;
                return ptOnLine.Distance(ptTest);
            } else if (ptTest.ProjectsOnLine(LineAbove2, ptOnLine, BTemp)) {
                ptOnThis = ptOnLine;
                return ptOnLine.Distance(ptTest);
            } else {
                // The point doesn't project onto either line so find the closest point
                if (LineAbove1 == Line12) {
                    if (LineAbove2 == Line23) {
                        ptOnThis = p2;
                        return ptTest.Distance(p2);
                    } else {
                        ptOnThis = p1;
                        return ptTest.Distance(p1);
                    }
                } else {
                    ptOnThis = p3;
                    return ptTest.Distance(p3);
                }
            }
        }
    }

    /**
     * Distance to a another.
     *
     * @param Other     The other triangle.
     * @param ptOnThis  Output. The closest point on the triangle.
     * @param ptOnOther Output. The closest point on the other triangle.
     */
    public double Distance(C2DTriangle Other, C2DPoint ptOnThis, C2DPoint ptOnOther) {
        C2DPoint ptTemp = new C2DPoint();
        double dMinDist = Distance(Other.p1, ptOnThis);
        ptOnOther.Set(Other.p1);

        double dDist = Distance(Other.p2, ptTemp);
        if (dDist < dMinDist) {
            ptOnOther.Set(Other.p2);
            ptOnThis.Set(ptTemp);

            dMinDist = dDist;
        }

        dDist = Distance(Other.p3, ptTemp);
        if (dDist < dMinDist) {
            ptOnOther.Set(Other.p3);
            ptOnThis.Set(ptTemp);
            dMinDist = dDist;
        }

        dDist = Other.Distance(p1, ptTemp);
        if (dDist < dMinDist) {
            ptOnOther.Set(ptTemp);
            ptOnThis.Set(p1);
            dMinDist = dDist;
        }

        dDist = Other.Distance(p2, ptTemp);
        if (dDist < dMinDist) {
            ptOnOther.Set(ptTemp);
            ptOnThis.Set(p2);
            dMinDist = dDist;
        }

        dDist = Other.Distance(p3, ptTemp);
        if (dDist < dMinDist) {
            ptOnOther.Set(ptTemp);
            ptOnThis.Set(p3);
            dMinDist = dDist;
        }

        return dMinDist;
    }

    /**
     * Returns the bounding rect.
     *
     * @param Rect Output. The bounding rectangle.
     */
    public void GetBoundingRect(C2DRect Rect) {
        Rect.Set(p1);
        Rect.ExpandToInclude(p2);
        Rect.ExpandToInclude(p3);

    }

    /**
     * Static version of area signed function.
     */
    public static double GetAreaSigned(C2DPoint pt1, C2DPoint pt2, C2DPoint pt3) {
        double dArea = pt1.x * pt2.y - pt2.x * pt1.y +
                pt2.x * pt3.y - pt3.x * pt2.y +
                pt3.x * pt1.y - pt1.x * pt3.y;

        dArea /= 2.0;

        return dArea;

    }

    /**
     * Returns whether the triangle is clockwise.
     */
    public static boolean IsClockwise(C2DPoint pt1, C2DPoint pt2, C2DPoint pt3) {
        return GetAreaSigned(pt1, pt2, pt3) < 0;
    }

    /**
     * Static version of circumcentre function.
     */
    public static C2DPoint GetCircumCentre(C2DPoint pt1, C2DPoint pt2, C2DPoint pt3) {

        C2DLine Line12 = new C2DLine(pt1, pt2);
        C2DLine Line23 = new C2DLine(pt2, pt3);

        // Move the lines to start from the midpoint on them
        Line12.point.Set(Line12.GetMidPoint());
        Line23.point.Set(Line23.GetMidPoint());
        // Turn them right (left would work as well)
        Line12.vector.TurnRight();
        Line23.vector.TurnRight();
        // Find the intersection between them taking the intersect point even if they don't 
        // intersect directly (i.e. where they would intersect because we may have turned them
        // the wrong way).
        ArrayList<C2DPoint> IntPt = new ArrayList<C2DPoint>();
        Boolean B1 = true, B2 = true;
        Line12.Crosses(Line23, IntPt, B1, B2, true);

        C2DPoint ptResult = new C2DPoint(0, 0);

        if (IntPt.size() == 1) {
            ptResult = IntPt.get(0);
        } else {
            // co-linear so fail.
            assert false : "Colinnear triangle. Cannot calculate Circum Centre";
        }

        return ptResult;

    }

    /**
     * Static version of Fermat point function.
     */
    public static C2DPoint GetFermatPoint(C2DPoint pt1, C2DPoint pt2, C2DPoint pt3) {
        C2DLine Line12 = new C2DLine(pt1, pt2);
        C2DLine Line23 = new C2DLine(pt2, pt3);
        C2DLine Line31 = new C2DLine(pt3, pt1);

        double dAng2 = Constants.conPI - Line12.vector.AngleBetween(Line23.vector);
        if (dAng2 >= Constants.conTWOTHIRDPI) // greater than 120 degrees
        {
            return new C2DPoint(pt2);
        } else if (dAng2 < Constants.conTHIRDPI)  // if less than 60 then 1 of the other 2 could be greater than 120
        {
            double dAng3 = Constants.conPI - Line23.vector.AngleBetween(Line31.vector);
            if (dAng3 >= Constants.conTWOTHIRDPI) // greater than 120 degrees
            {
                return new C2DPoint(pt3);
            } else if ((Constants.conPI - dAng2 - dAng3) >= Constants.conTWOTHIRDPI) // if least angle is greater than 120
            {
                return new C2DPoint(pt1);
            }
        }

        boolean bClockwise = Line12.IsOnRight(pt3);

        if (bClockwise) {
            Line12.vector.TurnLeft(Constants.conTHIRDPI);        // 60 degrees
            Line23.vector.TurnLeft(Constants.conTHIRDPI);        // 60 degrees
        } else {
            Line12.vector.TurnRight(Constants.conTHIRDPI);    // 60 degrees
            Line23.vector.TurnRight(Constants.conTHIRDPI);    // 60 degrees
        }

        Line12.SetPointFrom(pt3);
        Line23.SetPointFrom(pt1);

        ArrayList<C2DPoint> IntPt = new ArrayList<C2DPoint>();
        Boolean B1 = true, B2 = true;
        if (Line12.Crosses(Line23, IntPt, B1, B2, false)) {
            return IntPt.get(0);
        } else {
            assert false;
        }

        return new C2DPoint(0, 0);
    }

    /**
     * Static version of InCentre function.
     */
    public static C2DPoint GetInCentre(C2DPoint pt1, C2DPoint pt2, C2DPoint pt3) {
        // Set up a line to bisect the lines from 1 to 2 and 1 to 3
        C2DLine Line1 = new C2DLine(pt1, pt2);
        C2DLine Line2 = new C2DLine(pt1, pt3);
        Line1.SetLength(Line2.GetLength());
        C2DLine Line12Bisect = new C2DLine(pt1, pt3.GetMidPoint(Line1.GetPointTo()));

        // Set up a line to bisect the lines from 2 to 1 and 2 to 3
        C2DLine Line3 = new C2DLine(pt2, pt1);
        C2DLine Line4 = new C2DLine(pt2, pt3);
        Line3.SetLength(Line4.GetLength());
        C2DLine Line34Bisect = new C2DLine(pt2, pt3.GetMidPoint(Line3.GetPointTo()));

        // Now intersect the 2 lines and find the point.
        ArrayList<C2DPoint> Int = new ArrayList<C2DPoint>();

        // Add the intersection even if there isn't one (i.e. infinite lines)
        Boolean B1 = true, B2 = true;
        Line12Bisect.Crosses(Line34Bisect, Int, B1, B2, true);

        ///Debug.Assert (Int.Count == 1);

        return Int.get(0);
    }

    /**
     * Static collinear.
     */
    public static boolean Collinear(C2DPoint pt1, C2DPoint pt2, C2DPoint pt3) {
        return (C2DTriangle.GetAreaSigned(pt1, pt2, pt3) == 0);
    }

    /**
     * Projects this onto the line given.
     *
     * @param Line     Line to project on.
     * @param Interval Ouput. Projection.
     */
    public void Project(C2DLine Line, CInterval Interval) {
        p1.Project(Line, Interval);
        Interval.ExpandToInclude(p2.Project(Line));
        Interval.ExpandToInclude(p3.Project(Line));
    }

    /**
     * Projection onto the Vector.
     *
     * @param Vector   Vector to project on.
     * @param Interval Ouput. Projection.
     */
    public void Project(C2DVector Vector, CInterval Interval) {
        p1.Project(Vector, Interval);
        Interval.ExpandToInclude(p2.Project(Vector));
        Interval.ExpandToInclude(p3.Project(Vector));
    }


    /**
     * Snaps this to the conceptual grid.
     *
     * @param grid Grid to snap to.
     */
    public void SnapToGrid(CGrid grid) {
        p1.SnapToGrid(grid);
        p2.SnapToGrid(grid);
        p3.SnapToGrid(grid);
    }

    /**
     * Point 1.
     */
    public C2DPoint p1 = new C2DPoint();

    /**
     * Point 1.
     */
    public C2DPoint getp1() {

        return p1;
    }

    /// Point 2.
    public C2DPoint p2 = new C2DPoint();

    /**
     * Point 2.
     */
    public C2DPoint getp2() {

        return p2;
    }

    /// Point 3.
    public C2DPoint p3 = new C2DPoint();

    /**
     * Point 3.
     */
    public C2DPoint getp3() {

        return p3;

    }

}