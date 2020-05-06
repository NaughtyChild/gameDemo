
package uk.co.geolib.geolib;

import java.util.ArrayList;


public class C2DLine extends C2DLineBase {


    /// The first point.
    public C2DPoint point = new C2DPoint();
    /// The vector to the second point.
    public C2DVector vector = new C2DVector();

    public C2DLine() {
    }


    /**
     * Constructor.
     *
     * @param dPt1x The x value of the first point.
     * @param dPt1y The y value of the first point.
     * @param dPt2x The x value of the second point.
     * @param dPt2y The y value of the second point.
     */
    public C2DLine(double dPt1x, double dPt1y, double dPt2x, double dPt2y) {
        point.Set(dPt1x, dPt1y);
        vector.Set(dPt2x - dPt1x, dPt2y - dPt1y);
    }

    /**
     * Constructor.
     *
     * @param PointFrom The point from.
     * @param VectorTo  The vector defining the second point.
     */
    public C2DLine(C2DPoint PointFrom, C2DVector VectorTo) {
        point.Set(PointFrom);
        vector.Set(VectorTo);
    }

    /**
     * Constructor.
     *
     * @param PointFrom The point from.
     * @param PointTo   The point to.
     */
    public C2DLine(C2DPoint PointFrom, C2DPoint PointTo) {
        point.Set(PointFrom);
        vector.Set(PointFrom, PointTo);

    }

    /**
     * Constructor.
     *
     * @param Other The other line.
     */
    public C2DLine(C2DLine Other) {
        point.Set(Other.point);
        vector.Set(Other.vector);
    }

    /**
     * Assignment.
     *
     * @param Other The other line.
     */
    public void Set(C2DLine Other) {
        point.Set(Other.point);
        vector.Set(Other.vector);
    }

    /**
     * Assignment.
     *
     * @param PointFrom The point from.
     * @param PointTo   The point to.
     */
    public void Set(C2DPoint PointFrom, C2DPoint PointTo) {
        point.Set(PointFrom);
        vector.Set(PointFrom, PointTo);
    }

    /**
     * Assignment.
     *
     * @param PointFrom The point from.
     * @param VectorTo  The vector defining the second point.
     */
    public void Set(C2DPoint PointFrom, C2DVector VectorTo) {
        point.Set(PointFrom);
        vector.Set(VectorTo);
    }


    /**
     * Sets the point that this is going to.
     *
     * @param PointTo The point to go to.
     */
    public void SetPointTo(C2DPoint PointTo) {
        vector.Set(C2DPoint.Minus(PointTo, point));
    }

    /**
     * Sets the point that this is going to. The second point is unchanged.
     *
     * @param PointFrom The point to go from.
     */
    public void SetPointFrom(C2DPoint PointFrom) {
        // Get point to.
        C2DPoint PointTo = new C2DPoint(point.x + vector.i, point.y + vector.j);
        // Set the point from 
        point.Set(PointFrom);
        // Rest the point to.
        vector.Set(C2DPoint.Minus(PointTo, point));
    }

    /**
     * Sets the length. The first point is unchanged.
     *
     * @param dLength The new length.
     */
    public void SetLength(double dLength) {
        vector.SetLength(dLength);
    }

    /**
     * Creates a copy of this.
     */
    public C2DLineBase CreateCopy() {
        return new C2DLine(this);
    }

    /**
     * Reverses the direction of the line.
     */
    public void ReverseDirection() {
        point.Set(GetPointTo());
        vector.Reverse();
    }

    /**
     * True if the point is to the right of the line.
     *
     * @param OtherPoint The new point to test.
     */
    public boolean IsOnRight(C2DPoint OtherPoint) {
        return (C2DTriangle.GetAreaSigned(point, GetPointTo(), OtherPoint) < 0);
    }

    /**
     * Returns the second point as a new object.
     */
    public C2DPoint GetPointTo() {
        C2DPoint PointTo = new C2DPoint(point.x + vector.i, point.y + vector.j);
        return PointTo;
    }

    /**
     * Returns the first point as a new object.
     */
    public C2DPoint GetPointFrom() {
        return new C2DPoint(point);
    }

    /**
     * True if this line would cross the other if this were infinite.
     *
     * @param Other The other line to test.
     */
    public boolean WouldCross(C2DLine Other) {
        boolean bPointOnRight = IsOnRight(Other.point);
        boolean bPointToOnRight = IsOnRight(Other.GetPointTo());

        return (bPointOnRight ^ bPointToOnRight);
    }

    /**
     * True if this line crosses the other line, returns the intersection pt.
     *
     * @param Other The other line to test.
     */
    public boolean Crosses(C2DLineBase Other) {
        return Crosses(Other, new ArrayList<C2DPoint>());
    }

    /**
     * True if this line crosses the other line, returns the intersection pt.
     *
     * @param Other           The other line to test.
     * @param IntersectionPts Output. The intersection points.
     */
    public boolean Crosses(C2DLineBase Other, ArrayList<C2DPoint> IntersectionPts) {
        if (Other instanceof C2DLine) {
            return Crosses((C2DLine) Other, IntersectionPts);
        } else if (Other instanceof C2DArc) {
            C2DArc Arc = (C2DArc) Other;
            return Arc.Crosses(this, IntersectionPts);
        } else {
            assert false : "Invalid Line type";
            return false;
        }
    }

    /**
     * True if this line crosses the other line, returns the intersection pt.
     *
     * @param Other           The other line to test.
     * @param IntersectionPts Output. The intersection points.
     */
    public boolean Crosses(C2DLine Other, ArrayList<C2DPoint> IntersectionPts) {
        Boolean bOnThis = true;
        Boolean bOnOther = true;

        return Crosses(Other, IntersectionPts, bOnThis, bOnOther, false);
    }


    /**
     * True if this line crosses the other. Returns the point is a collection is provided.
     * Returns whether it would cross on this or on the other. Can opt to get the point
     * where the cross would occur (if not parallel) even if they don't cross.
     *
     * @param Other           The other line
     * @param IntersectionPts To recieve the result
     * @param bOnThis         Output. True is the intersection would be on this line.
     * @param bOnOther        Output. True is the intersection would be on the other line.
     * @param bAddPtIfFalse   Input. True to add the intersection point even if there is no intersection.
     *                        <returns></returns>
     */
    public boolean Crosses(C2DLine Other, ArrayList<C2DPoint> IntersectionPts,
                           Boolean bOnThis, Boolean bOnOther, boolean bAddPtIfFalse) {
        bOnThis = false;
        bOnOther = false;

        C2DPoint p1 = point;
        C2DPoint p2 = GetPointTo();

        C2DPoint p3 = Other.point;
        C2DPoint p4 = Other.GetPointTo();

        double Ua = (p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x);
        double Ub = (p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x);

        double dDenominator = (p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y);

        if (dDenominator == 0)
            return false;

        Ua = Ua / dDenominator;
        Ub = Ub / dDenominator;

        bOnThis = (Ua >= 0 && Ua < 1);        // For ints we need the line to be the point set [a,b);
        bOnOther = (Ub >= 0 && Ub < 1);        // For ints we need the line to be the point set [a,b);
        boolean bResult = bOnThis && bOnOther;

        if (bAddPtIfFalse || bResult) {
            IntersectionPts.add(new C2DPoint(p1.x + Ua * (p2.x - p1.x), p1.y + Ua * (p2.y - p1.y)));
        }

        return (bResult);

    }


    /**
     * Function to join the 2 lines at the point where they do / would intersect. If they do then
     * the lines are clipped to remove the smallest part of the line. Returns false if they
     * cannot be joined.
     *
     * @param Other The other line
     */
    public boolean Join(C2DLine Other) {
        C2DPoint p1 = point;
        C2DPoint p2 = GetPointTo();

        C2DPoint p3 = Other.point;
        C2DPoint p4 = Other.GetPointTo();

        double Ua = (p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x);
        double Ub = (p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x);

        double dDenominator = (p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y);

        if (dDenominator == 0)
            return false;

        Ua = Ua / dDenominator;
        Ub = Ub / dDenominator;

        C2DPoint IntPt = new C2DPoint(p1.x + Ua * (p2.x - p1.x), p1.y + Ua * (p2.y - p1.y));
        if (Ua >= 0.5)
            this.SetPointTo(IntPt);
        else
            this.SetPointFrom(IntPt);

        if (Ub >= 0.5)
            Other.SetPointTo(IntPt);
        else
            Other.SetPointFrom(IntPt);

        return true;
    }

    /**
     * True if the ray provided (infinite line starting from the first point) crosses this.
     *
     * @param Ray             The other line to test.
     * @param IntersectionPts Output. The intersection points.
     */
    public boolean CrossesRay(C2DLine Ray, ArrayList<C2DPoint> IntersectionPts) {
        C2DPoint p1 = point;
        C2DPoint p2 = GetPointTo();

        C2DPoint p3 = Ray.point;
        C2DPoint p4 = Ray.GetPointTo();

        double Ua = (p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x);
        double Ub = (p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x);

        double dDenominator = (p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y);

        if (dDenominator == 0)
            return false;

        Ua = Ua / dDenominator;
        Ub = Ub / dDenominator;

        boolean bResult = (Ua >= 0 && Ua <= 1) && (Ub >= 0);

        if (bResult) {
            IntersectionPts.add(new C2DPoint(p1.x + Ua * (p2.x - p1.x), p1.y + Ua * (p2.y - p1.y)));
        }

        return bResult;
    }

    /**
     * Returns the distance from this to the point.
     *
     * @param TestPoint The test point.
     */
    public double Distance(C2DPoint TestPoint) {
        C2DPoint P1 = new C2DPoint();
        return Distance(TestPoint, P1);
    }

    /**
     * Returns the distance from this to the point.
     *
     * @param TestPoint The test pointt.
     * @param ptOnThis  Output. The closest point on this.
     */
    public double Distance(C2DPoint TestPoint, C2DPoint ptOnThis) {
        C2DVector vP1ToPoint = new C2DVector(point, TestPoint);
        double dLength = GetLength();
        double dProjLength = vP1ToPoint.Dot(vector);

        if (dProjLength < 0) {
            ptOnThis.Set(point);

            return TestPoint.Distance(point);
        } else {
            dProjLength = dProjLength / dLength;
            if (dProjLength < dLength) {
                // The projection is on the line
                double dFactorOnLine = dProjLength / dLength;
                C2DPoint PtOnLine = new C2DPoint(point.x + vector.i * dFactorOnLine,
                        point.y + vector.j * dFactorOnLine);
                ptOnThis.Set(PtOnLine);
                return TestPoint.Distance(PtOnLine);
            } else {
                ptOnThis.Set(GetPointTo());

                return TestPoint.Distance(GetPointTo());
            }
        }
    }

    /**
     * Returns the distance from this to the other line.
     *
     * @param Other     The Other line.
     * @param ptOnThis  Output. The closest point on this.
     * @param ptOnOther Output. The closest point on the other line.
     */
    public double Distance(C2DLine Other, C2DPoint ptOnThis, C2DPoint ptOnOther) {
        // First, project the other line onto this and if it falls entirely below it or
        // above it then 1. There is no intersection, 2. This is closest to one end on this line.
        C2DPoint ptOtherP2 = new C2DPoint(Other.GetPointTo());
        C2DVector vThisP1OtherP1 = new C2DVector(point, Other.point);
        C2DVector vThisP1OtherP2 = new C2DVector(point, ptOtherP2);
        C2DPoint ptThisP2 = new C2DPoint(GetPointTo());

        double dOtherP1Proj = vThisP1OtherP1.Dot(vector);
        double dOtherP2Proj = vThisP1OtherP2.Dot(vector);
        // If they are both less than 0 then the projection falls below the line.
        if (dOtherP1Proj <= 0 && dOtherP2Proj <= 0) {
            ptOnThis.Set(point);
            return Other.Distance(point, ptOnOther);
        }
        // Now modify the projection so it is the length along this line.
        double dThisLength = GetLength();
        dOtherP1Proj = dOtherP1Proj / dThisLength;
        dOtherP2Proj = dOtherP2Proj / dThisLength;
        // If the projections are both above the line then the second point is closest
        if (dOtherP1Proj >= dThisLength && dOtherP2Proj >= dThisLength) {
            ptOnThis.Set(ptThisP2);
            return Other.Distance(ptThisP2, ptOnOther);
        }

        // This hasn't worked so try the same on the other line.
        C2DVector vOtherP1ThisP1 = new C2DVector(Other.point, point);
        C2DVector vOtherP1ThisP2 = new C2DVector(Other.point, ptThisP2);

        double dThisP1Proj = vOtherP1ThisP1.Dot(Other.vector);
        double dThisP2Proj = vOtherP1ThisP2.Dot(Other.vector);
        // If they are both less than 0 then the projection falls below the line.
        if (dThisP1Proj <= 0 && dThisP2Proj <= 0) {
            ptOnOther.Set(Other.point);
            return Distance(Other.point, ptOnThis);
        }
        // Now modify the projection so it is the length along this line.
        double dOtherLength = Other.GetLength();
        dThisP1Proj = dThisP1Proj / dOtherLength;
        dThisP2Proj = dThisP2Proj / dOtherLength;
        // If the projections are both above the line then the second point is closest
        if (dThisP1Proj >= dOtherLength && dThisP2Proj >= dOtherLength) {
            ptOnOther.Set(ptOtherP2);
            return Distance(ptOtherP2, ptOnThis);
        }
        // Now test for an intersection.
        ArrayList<C2DPoint> IntPoint = new ArrayList<C2DPoint>();
        Boolean B1 = true, B2 = true;
        if (this.Crosses(Other, IntPoint, B1, B2, false)) {
            ptOnOther.Set(IntPoint.get(0));
            ptOnThis.Set(IntPoint.get(0));
            return 0;
        }
        // Otherwise, there MUST be a point projection on one of the lines otherwise both
        // lines project on either side of each other which is impossible. 
        // So find the distances to all these projections and take the minimum.
        double dDist = 0;
        double dMinDist = 0;
        boolean bSet = false;


        C2DPoint ptOnThisTemp = new C2DPoint();
        C2DPoint ptOnOtherTemp = new C2DPoint();
        // Is the other lines first point projected on this?
        if (dOtherP1Proj >= 0 && dOtherP1Proj <= dThisLength) {
            // If so find the point on this line and get distance to it.
            double dFactor = dOtherP1Proj / dThisLength;
            ptOnThisTemp.Set(new C2DPoint(point.x + vector.i * dFactor,
                    point.y + vector.j * dFactor));

            dMinDist = Other.point.Distance(ptOnThisTemp);
            bSet = true;

            ptOnOther.Set(Other.point);
            ptOnThis.Set(ptOnThisTemp);
        }
        // Is the other lines second point projected onto this?
        if (dOtherP2Proj >= 0 && dOtherP2Proj <= dThisLength) {
            // If so find the point on this and then the distance. Is it less?
            double dFactor = dOtherP2Proj / dThisLength;
            ptOnThisTemp.Set(new C2DPoint(point.x + vector.i * dFactor,
                    point.y + vector.j * dFactor));

            dDist = ptOtherP2.Distance(ptOnThisTemp);
            if (!bSet || dDist < dMinDist) {
                ptOnOther.Set(ptOtherP2);
                ptOnThis.Set(ptOnThisTemp);
                dMinDist = dDist;

                bSet = true;
            }
        }
        // Is the first point on this projected onto the other line?
        if (dThisP1Proj >= 0 && dThisP1Proj <= dOtherLength) {
            // If so find the point and the distance. Is it less?
            double dFactor = dThisP1Proj / dOtherLength;
            ptOnOtherTemp.Set(new C2DPoint(Other.point.x + Other.vector.i * dFactor,
                    Other.point.y + Other.vector.j * dFactor));

            dDist = point.Distance(ptOnOtherTemp);
            if (!bSet || dDist < dMinDist) {
                ptOnThis.Set(point);
                ptOnOther.Set(ptOnOtherTemp);
                dMinDist = dDist;

                bSet = true;
            }

        }

        // Is the second point on this projected onto the other line?
        if (dThisP2Proj >= 0 && dThisP2Proj <= dOtherLength) {
            // If so find the point and the distance. Is it less?
            double dFactor = dThisP2Proj / dOtherLength;

            ptOnOtherTemp.Set(new C2DPoint(Other.point.x + Other.vector.i * dFactor,
                    Other.point.y + Other.vector.j * dFactor));

            dDist = ptThisP2.Distance(ptOnOtherTemp);
            if (!bSet || dDist < dMinDist) {
                ptOnThis.Set(ptThisP2);
                ptOnOther.Set(ptOnOtherTemp);
                dMinDist = dDist;

                bSet = true;
            }
        }

        //  Debug.Assert( bSet );
        // Now return the minimum distance
        return dMinDist;

    }


    /**
     * Returns the distance from this to the other line.
     *
     * @param Other     The other line.
     * @param ptOnThis  Output. The closest point on this.
     * @param ptOnOther Output. The closest point on the other line.
     */
    public double Distance(C2DLineBase Other, C2DPoint ptOnThis, C2DPoint ptOnOther) {
        if (Other instanceof C2DLine) {
            return Distance((C2DLine) Other, ptOnThis, ptOnOther);
        } else if (Other instanceof C2DArc) {
            C2DArc Arc = (C2DArc) Other;

            return Arc.Distance(this, ptOnThis, ptOnOther);
        } else {
            //      Debug.Assert(false, "Invalid Hole type");
            return 0;
        }
    }

    /**
     * Gets the mid point on the line.
     */
    public C2DPoint GetMidPoint() {
        C2DPoint Result = new C2DPoint(point.x + vector.i / 2, point.y + vector.j / 2);
        return Result;
    }

    /**
     * Gets the point on the line given by the factor. e.g. 0.5 = mid point.
     *
     * @param dFactorFromStart The factor from the start.
     */
    public C2DPoint GetPointOn(double dFactorFromStart) {
        C2DVector vNew = new C2DVector(vector);
        vNew.Multiply(dFactorFromStart);

        C2DPoint Result = new C2DPoint(point.x + vNew.i, point.y + vNew.j);
        return Result;
    }

    /**
     * Returns the bounding rectangle.
     *
     * @param Rect Output. The bounding rectangle.
     */
    public void GetBoundingRect(C2DRect Rect) {
        Rect.Set(point);
        Rect.ExpandToInclude(GetPointTo());
    }

    /**
     * Returns the length of the line.
     */
    public double GetLength() {
        return vector.GetLength();
    }

    /**
     * Moves this point by the vector given.
     *
     * @param vector The vector.
     */
    public void Move(C2DVector vector) {
        point.Move(vector);
    }

    /**
     * Rotates this to the right about the origin provided.
     *
     * @param dAng   The angle through which to rotate.
     * @param Origin The origin about which to rotate.
     */
    public void RotateToRight(double dAng, C2DPoint Origin) {
        point.RotateToRight(dAng, Origin);
        vector.TurnRight(dAng);
    }

    /**
     * Grows the line about the origin.
     *
     * @param dFactor The factor to grow by.
     * @param Origin  The origin about which to grow.
     */
    public void Grow(double dFactor, C2DPoint Origin) {
        C2DPoint pointTo = new C2DPoint(GetPointTo());
        point.Grow(dFactor, Origin);
        pointTo.Grow(dFactor, Origin);
        SetPointTo(pointTo);
    }

    /**
     * Point reflection.
     *
     * @param Point The point through which to reflect this.
     */
    public void Reflect(C2DPoint Point) {
        C2DPoint pointTo = new C2DPoint(GetPointTo());
        point.Reflect(Point);
        pointTo.Reflect(Point);
        SetPointTo(pointTo);
    }

    /**
     * Point reflection.
     *
     * @param Line The line through which to reflect this.
     */
    public void Reflect(C2DLine Line) {
        C2DPoint pointTo = new C2DPoint(GetPointTo());
        point.Reflect(Line);
        pointTo.Reflect(Line);
        SetPointTo(pointTo);
    }

    /**
     * Grows from the centre by the amount.
     *
     * @param dFactor The factor to grow this by.
     */
    public void GrowFromCentre(double dFactor) {
        point.x -= (vector.i * dFactor - vector.i) / 2;
        point.y -= (vector.j * dFactor - vector.j) / 2;

        this.vector.Multiply(dFactor);
    }

    /**
     * Projection onto a line.
     *
     * @param TestLine The line to project this on.
     * @param Interval Output. The interval.
     */
    public void Project(C2DLine TestLine, CInterval Interval) {
        double dP1 = point.Project(TestLine);
        Interval.dMax = dP1;
        Interval.dMin = dP1;
        Interval.ExpandToInclude(GetPointTo().Project(TestLine));
    }

    /**
     * Projection onto a vector.
     *
     * @param Vector   The vector to project this on.
     * @param Interval Output. The interval.
     */
    public void Project(C2DVector Vector, CInterval Interval) {
        double dP1 = point.Project(Vector);
        Interval.dMax = dP1;
        Interval.dMin = dP1;
        Interval.ExpandToInclude(GetPointTo().Project(Vector));
    }

    /**
     * Returns the lines that make up this defined by the points which are assumed
     * to be on this line. i.e. splits the line up.
     *
     * @param PtsOnLine The point set defining how this is to be broken up.
     * @param LineSet   Output. The sub lines.
     */
    public void GetSubLines(ArrayList<C2DPoint> PtsOnLine, ArrayList<C2DLineBase> LineSet) {
        // if there are no points on the line to split on then add a copy of this and return.
        int usPointsCount = PtsOnLine.size();
        if (usPointsCount == 0) {
            LineSet.add(new C2DLine(this));
        } else {
            C2DPointSet TempPts = new C2DPointSet();
            TempPts.MakeCopy(PtsOnLine);

            if (usPointsCount > 1) // They need sorting
            {
                // Now sort the points according to the order in which they will be encountered
                TempPts.SortByDistance(point);
            }

            // Add the line from the start of this to the first.
            LineSet.add(new C2DLine(point, TempPts.get(0)));

            // Add all the sub lines.
            for (int i = 1; i < usPointsCount; i++)
                LineSet.add(new C2DLine(TempPts.get(i - 1), TempPts.get(i)));

            // Add the line from the last point on this to the end of this.
            LineSet.add(new C2DLine(TempPts.get(TempPts.size() - 1), GetPointTo()));
        }

        //    Debug.Assert(LineSet.Count == (PtsOnLine.Count + 1));

    }

    /**
     * Snaps this to the conceptual grid.
     *
     * @param grid The grid to snap to.
     */
    public void SnapToGrid(CGrid grid) {
        C2DPoint pt = GetPointTo();

        pt.SnapToGrid(grid);

        point.SnapToGrid(grid);
        SetPointTo(pt);
    }


    /**
     * Given x, this returns y assuming the line is infinite.
     *
     * @param dx <returns></returns>
     */
    public double GetY(double dx) {
        if (vector.i == 0) {
            assert false;
            return 0;
        }
        double m = vector.j / vector.i;
        return m * dx + point.y - m * point.x;
    }


    /**
     * True if part of this line is above or below the other. Returns the point
     * on this and on the other.
     *
     * @param Other
     * @param dVerticalDistance
     * @param ptOnThis
     * @param ptOnOther         <returns></returns>
     */
    public boolean OverlapsVertically(C2DLine Other, Double dVerticalDistance,
                                      C2DPoint ptOnThis, C2DPoint ptOnOther) {
        // Get the 2 points for both lines
        C2DPoint OtherTo = new C2DPoint(Other.point.x + Other.vector.i, Other.point.y + Other.vector.j);
        C2DPoint ThisTo = new C2DPoint(point.x + vector.i, point.y + vector.j);
        // Make an interval for both in the x plane
        CInterval iThis = new CInterval(point.x, point.x);
        iThis.ExpandToInclude(ThisTo.x);

        CInterval iOther = new CInterval(Other.point.x, Other.point.x);
        iOther.ExpandToInclude(OtherTo.x);
        // This is an interval for the overlap between the 2
        CInterval iOverlap = new CInterval();
        // If there is an overlap...
        if (iThis.Overlaps(iOther, iOverlap)) {
            double dThisYMin;
            double dThisYMax;

            double dOtherYMin;
            double dOtherYMax;
            // If the line is vertical then y at the x min / max can be set to the ends of the line.
            if (vector.i == 0) {
                dThisYMin = point.y;
                dThisYMax = ThisTo.y;
            } else    // otherwise, caluclate the y values at the interval ends
            {
                dThisYMin = GetY(iOverlap.dMin);
                dThisYMax = GetY(iOverlap.dMax);
            }
            // Now do the same for the other line
            if (Other.vector.i == 0) {
                dOtherYMin = Other.point.y;
                dOtherYMax = OtherTo.y;
            } else {
                dOtherYMin = Other.GetY(iOverlap.dMin);
                dOtherYMax = Other.GetY(iOverlap.dMax);
            }
            // Now find the distance between the 2 at the ends
            double dDistMin = dOtherYMin - dThisYMin;
            double dDistMax = dOtherYMax - dThisYMax;
            // If they are both same sign then there is no intersection
            if ((dDistMin * dDistMax) > 0) {
                dDistMin = Math.abs(dDistMin);
                dDistMax = Math.abs(dDistMax);
                // find which one is smallest
                if (dDistMin > dDistMax) {
                    dVerticalDistance = dDistMax;// distance at the max is smallest
                    ptOnThis.x = iOverlap.dMax;
                    ptOnThis.y = dThisYMax;
                    ptOnOther.x = iOverlap.dMax;
                    ptOnOther.y = dOtherYMax;
                } else {
                    dVerticalDistance = dDistMin;// distance at the min is smallest
                    ptOnThis.x = iOverlap.dMin;
                    ptOnThis.y = dThisYMin;
                    ptOnOther.x = iOverlap.dMin;
                    ptOnOther.y = dOtherYMin;
                }
                return true;
            } else {
                // find the intersection.
                dVerticalDistance = (double) 0;
                C2DPointSet pts = new C2DPointSet();
                if (this.Crosses(Other, pts)) {
                    ptOnThis = pts.get(0);
                    ptOnOther = ptOnThis;
                } else {
                    //     Debug.Assert(false);
                }
            }

            return true;
        } else {
            return false;
        }

    }

    /**
     * True if part of this line is above the other. Returns the point
     * on this and on the other.
     *
     * @param Other
     * @param dVerticalDistance
     * @param ptOnThis
     * @param ptOnOther         <returns></returns>
     */
    public boolean OverlapsAbove(C2DLine Other, Double dVerticalDistance,
                                 C2DPoint ptOnThis, C2DPoint ptOnOther) {
        // Get the 2 points for both lines
        C2DPoint OtherTo = new C2DPoint(Other.point.x + Other.vector.i, Other.point.y + Other.vector.j);
        C2DPoint ThisTo = new C2DPoint(point.x + vector.i, point.y + vector.j);
        // Make an interval for both in the x plane
        CInterval iThis = new CInterval(point.x, point.x);
        iThis.ExpandToInclude(ThisTo.x);

        CInterval iOther = new CInterval(Other.point.x, Other.point.x);
        iOther.ExpandToInclude(OtherTo.x);
        // This is an interval for the overlap between the 2
        CInterval iOverlap = new CInterval();
        // If there is an overlap...
        if (iThis.Overlaps(iOther, iOverlap)) {
            double dThisYMin;
            double dThisYMax;

            double dOtherYMin;
            double dOtherYMax;
            // If the line is vertical then y at the x min / max can be set to the ends of the line.
            if (vector.i == 0) {
                dThisYMin = point.y;
                dThisYMax = ThisTo.y;
            } else    // otherwise, caluclate the y values at the interval ends
            {
                dThisYMin = GetY(iOverlap.dMin);
                dThisYMax = GetY(iOverlap.dMax);
            }
            // Now do the same for the other line
            if (Other.vector.i == 0) {
                dOtherYMin = Other.point.y;
                dOtherYMax = OtherTo.y;
            } else {
                dOtherYMin = Other.GetY(iOverlap.dMin);
                dOtherYMax = Other.GetY(iOverlap.dMax);
            }

            // Now find the distance between the 2 at the ends
            double dDistMin = dThisYMin - dOtherYMin;
            double dDistMax = dThisYMax - dOtherYMax;
            // If they are both > 0 then no intersection
            if ((dDistMin > 0) && (dDistMax > 0)) {
                dDistMin = Math.abs(dDistMin);
                dDistMax = Math.abs(dDistMax);
                // find which one is smallest
                if (dDistMin > dDistMax) {
                    dVerticalDistance = dDistMax;    // distance at the max is smallest
                    ptOnThis.x = iOverlap.dMax;
                    ptOnThis.y = dThisYMax;
                    ptOnOther.x = iOverlap.dMax;
                    ptOnOther.y = dOtherYMax;
                } else {
                    dVerticalDistance = dDistMin;  // distance at the min is smallest
                    ptOnThis.x = iOverlap.dMin;
                    ptOnThis.y = dThisYMin;
                    ptOnOther.x = iOverlap.dMin;
                    ptOnOther.y = dOtherYMin;
                }

                return true;
            } else if ((dDistMin < 0) && (dDistMax < 0)) // This is below.
            {
                return false;
            } else {
                // find the intersection.
                dVerticalDistance = 0.0;
                C2DPointSet pts = new C2DPointSet();
                if (this.Crosses(Other, pts)) {
                    ptOnThis = pts.get(0);
                    ptOnOther = ptOnThis;
                } else {
                    //    Debug.Assert(false);
                }
            }

            return true;
        } else {
            return false;
        }

    }

    /**
     * Returns the distance from this to the point with this as a ray.
     *
     * @param TestPoint <returns></returns>
     */
    public double DistanceAsRay(C2DPoint TestPoint) {
        C2DPoint ptOnThis = new C2DPoint();
        return DistanceAsRay(TestPoint, ptOnThis);
    }


    /**
     * Returns the distance from this to the point with this as a ray.
     *
     * <param name="TestPoint">@param
     * <param name="ptOnThis">@param
     * <returns></returns>
     */
    public double DistanceAsRay(C2DPoint TestPoint, C2DPoint ptOnThis) {
        C2DVector vP1ToPoint = new C2DVector(point, TestPoint);

        // The projection is on the line
        double dFactorOnLine = vP1ToPoint.Dot(vector) / (vector.i * vector.i + vector.j * vector.j);

        ptOnThis.Set(point.x + vector.i * dFactorOnLine,
                point.y + vector.j * dFactorOnLine);
        return TestPoint.Distance(ptOnThis);
    }


    /**
     * Transform by a user defined transformation. e.g. a projection.
     */
    public void Transform(CTransformation pProject) {
        C2DPoint pt2 = new C2DPoint(point.x + vector.i, point.y + vector.j);

        pProject.Transform(point.x, point.y);
        pProject.Transform(pt2.x, pt2.y);

        vector.i = pt2.x - point.x;
        vector.j = pt2.y - point.y;
    }

    /**
     * Transform by a user defined transformation. e.g. a projection.
     */
    public void InverseTransform(CTransformation pProject) {
        C2DPoint pt2 = new C2DPoint(point.x + vector.i, point.y + vector.j);

        pProject.InverseTransform(point.x, point.y);
        pProject.InverseTransform(pt2.x, pt2.y);

        vector.i = pt2.x - point.x;
        vector.j = pt2.y - point.y;
    }


}