
package uk.co.geolib.geolib;

import java.util.ArrayList;

public class C2DCircle extends C2DBase {
    /**
     * Constructor.
     */
    public C2DCircle() {
    }

    /**
     * Constructor.
     *
     * @param Point     The centre.
     * @param NewRadius The radius.
     */
    public C2DCircle(C2DPoint Point, double NewRadius) {
        Centre.Set(Point);
        Radius = NewRadius;
    }

    /**
     * Constructor.
     *
     * @param Other The circle to assign this to.
     */
    public C2DCircle(C2DCircle Other) {
        Centre.Set(Other.Centre);
        Radius = Other.Radius;
    }


    /**
     * Assignment.
     *
     * @param Point     The centre.
     * @param NewRadius The radius.
     */
    public void Set(C2DPoint Point, double NewRadius) {
        Centre.Set(Point);
        Radius = NewRadius;
    }

    /**
     * Set to be the minimum bounding circle for the 2 points.
     *
     * @param Point1 The first point to include.
     * @param Point2 The second point to include.
     */
    public void SetMinimum(C2DPoint Point1, C2DPoint Point2) {
        C2DVector Vec = new C2DVector(Point1, Point2);
        Vec.Multiply(0.5);
        Radius = Vec.GetLength();
        Centre.Set(Point1.GetPointTo(Vec));
    }

    /**
     * Set to be the minimum bounding circle for the 3 points.
     *
     * @param Point1 The first point to include.
     * @param Point2 The second point to include.
     * @param Point3 The third point to include.
     */
    public void SetMinimum(C2DPoint Point1, C2DPoint Point2, C2DPoint Point3) {
        double dDist12 = Point1.Distance(Point2);
        double dDist23 = Point2.Distance(Point3);
        double dDist31 = Point3.Distance(Point1);
        if (dDist12 >= dDist23 && dDist12 >= dDist31) {
            SetMinimum(Point1, Point2);
            if (this.Contains(Point3))
                return;
        } else if (dDist23 >= dDist31) {
            SetMinimum(Point2, Point3);
            if (this.Contains(Point1))
                return;
        } else {
            SetMinimum(Point3, Point1);
            if (this.Contains(Point2))
                return;
        }

        // If here, the simple minimum of any 2 doesn't incorporate the other 1 so the
        // minimum is the circumscribed circle.
        SetCircumscribed(Point1, Point2, Point3);
    }

    /**
     * Sets the circle to be the maximum contained circle within the 3 points provided.
     *
     * @param Point1 The triangle's first point.
     * @param Point2 The triangle's second point.
     * @param Point3 The triangle's third point.
     */
    public void SetInscribed(C2DPoint Point1, C2DPoint Point2, C2DPoint Point3) {
        SetInscribed(new C2DTriangle(Point1, Point2, Point3));
    }

    /**
     * Sets the circle to be the maximum contained circle within the triangle.
     *
     * @param Triangle The triangle to bound the circle.
     */
    public void SetInscribed(C2DTriangle Triangle) {
        C2DPoint InCen = Triangle.GetInCentre();

        C2DLine Line = new C2DLine(Triangle.getp1(), Triangle.getp2());

        C2DVector vec = new C2DVector(Line.point, InCen);
        double dProj = vec.Dot(Line.vector);
        double dLength = Line.vector.GetLength();
        dProj /= dLength;

        double dFactor = dProj / dLength;

        C2DVector vProj = new C2DVector(Line.vector);
        vProj.Multiply(dFactor);
        C2DPoint ptOnLine = new C2DPoint(Line.point.x + vProj.i, Line.point.y + vProj.j);

        Set(InCen, InCen.Distance(ptOnLine));
    }

    /**
     * Set to be circle that places all 3 points on the edge.
     *
     * @param Point1 The first point.
     * @param Point2 The second point.
     * @param Point3 The third point.
     */
    public boolean SetCircumscribed(C2DPoint Point1, C2DPoint Point2, C2DPoint Point3) {
        if (C2DTriangle.Collinear(Point1, Point2, Point3))
            return false;

        Centre.Set(C2DTriangle.GetCircumCentre(Point1, Point2, Point3));
        Radius = Centre.Distance(Point1);

        return true;
    }

    /**
     * Set to be circle that places all 3 points of the triangle on the edge.
     *
     * @param Triangle The triangle.
     */
    public boolean SetCircumscribed(C2DTriangle Triangle) {
        if (Triangle.Collinear())
            return false;

        Centre.Set(Triangle.GetCircumCentre());
        Radius = Centre.Distance(Triangle.getp1());

        return true;
    }

    /**
     * Returns the area.
     */
    public double GetArea() {
        return Constants.conPI * Radius * Radius;
    }

    /**
     * Returns the perimeter.
     */
    double GetPerimeter() {
        return Constants.conTWOPI * Radius;
    }

    /**
     * Sets this to another.
     *
     * @param Other The other circle.
     */
    public void Set(C2DCircle Other) {
        Centre.Set(Other.Centre);
        Radius = Other.Radius;
    }

    /**
     * Gets the bounding rectangle.
     *
     * @param Rect The rectangle to recieve the result.
     */
    public void GetBoundingRect(C2DRect Rect) {
        Rect.Set(Centre.x - Radius, Centre.y + Radius,
                Centre.x + Radius, Centre.y - Radius);
    }

    /**
     * True if this crosses the other and returns the intersectin points.
     *
     * @param Other           The other circle.
     * @param IntersectionPts The point set to recieve the result.
     */
    public boolean Crosses(C2DCircle Other, ArrayList<C2DPoint> IntersectionPts) {
        double x1 = Centre.x;
        double y1 = Centre.y;
        double R1 = Radius;

        double x2 = Other.Centre.x;
        double y2 = Other.Centre.y;
        double R2 = Other.Radius;

        double D = Other.Centre.Distance(Centre);

        if (D == 0)
            return false;

        if (D == (R1 + R2)) {
            C2DVector V = new C2DVector(Centre, Other.Centre);
            V.SetLength(R1);
            C2DPoint P = new C2DPoint(Centre.GetPointTo(V));
            IntersectionPts.add(P);

            return true;
        }

        if (D > (R1 + R2) || D < Math.abs(R1 - R2))
            return false;

        double A = (D + R1 + R2) * (D + R1 - R2) * (D - R1 + R2) * (-D + R1 + R2);
        A = Math.sqrt(A) / 4;

        double XE1 = (x1 + x2) / 2 - (x1 - x2) * (R1 * R1 - R2 * R2) / (2 * D * D);
        double XE2 = 2 * (y1 - y2) * A / (D * D);

        double YE1 = (y1 + y2) / 2 - (y1 - y2) * (R1 * R1 - R2 * R2) / (2 * D * D);
        double YE2 = 2 * (x1 - x2) * A / (D * D);

        C2DPoint pt1 = new C2DPoint(XE1 + XE2, YE1 - YE2);
        C2DPoint pt2 = new C2DPoint(XE1 - XE2, YE1 + YE2);

        IntersectionPts.add(pt1);
        IntersectionPts.add(pt2);


        return true;

    }


    /**
     * True if this crosses the line and returns the intersectin points.
     *
     * @param Line            The line.
     * @param IntersectionPts The point set to recieve the result.
     */
    public boolean Crosses(C2DLine Line, ArrayList<C2DPoint> IntersectionPts) {
        double x1 = Line.point.x;
        double x2 = Line.point.x + Line.vector.i;
        double x3 = Centre.x;

        double y1 = Line.point.y;
        double y2 = Line.point.y + Line.vector.j;
        double y3 = Centre.y;

        double r = Radius;

        double a = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

        double b = 2 * ((x2 - x1) * (x1 - x3) + (y2 - y1) * (y1 - y3));

        double c = x3 * x3 + y3 * y3 + x1 * x1 + y1 * y1 - 2 * (x3 * x1 + y3 * y1) - r * r;

        double u = -b / (2 * a);

        C2DPoint ptClosestToCen = new C2DPoint();

        if (u < 0) {
            ptClosestToCen.Set(Line.point);
        } else if (u > 1) {
            ptClosestToCen.Set(Line.GetPointTo());
        } else {
            C2DVector V1 = new C2DVector(Line.vector);
            V1.Multiply(u);
            ptClosestToCen = Line.point.GetPointTo(V1);
        }

        double dDist = ptClosestToCen.Distance(Centre);

        if (dDist > Radius) {
            return false;
        } else {
            // Calculate the points.
            double d1 = b * b - 4 * a * c;
            assert d1 >= 0;

            if (d1 < 0)
                return false;
            else if (d1 == 0) {
                double p1 = -b / (2 * a);
                IntersectionPts.add(Line.GetPointOn(p1));
                return true;
            } else {
                d1 = Math.sqrt(d1);
                double p1 = (-b + d1) / (2 * a);
                double p2 = (-b - d1) / (2 * a);

                boolean bResult = false;
                if (p2 >= 0 && p2 <= 1) {
                    bResult = true;
                    IntersectionPts.add(Line.GetPointOn(p2));
                }

                if (p1 >= 0 && p1 <= 1) {
                    bResult = true;
                    IntersectionPts.add(Line.GetPointOn(p1));
                }

                return bResult;
            }
        }
    }

    /**
     * True if this crosses the ray and returns the intersectin points.
     *
     * @param Ray             The ray. A line with no end point
     * @param IntersectionPts The point set to recieve the result.
     */
    public boolean CrossesRay(C2DLine Ray, ArrayList<C2DPoint> IntersectionPts) {
        double dDist = Ray.point.Distance(Centre);
        C2DLine RayCopy = new C2DLine(Ray);
        // Ensure the copy line will go through the circle if the ray would.
        RayCopy.vector.SetLength((dDist + Radius) * 2);

        return Crosses(RayCopy, IntersectionPts);
    }

    /**
     * True if this contains the point.
     *
     * @param pt The test point.
     */
    public boolean Contains(C2DPoint pt) {
        return Centre.Distance(pt) < Radius;
    }

    /**
     * Proximity test.
     *
     * @param pt     The test point.
     * @param dRange The range.
     */
    public boolean IsWithinDistance(C2DPoint pt, double dRange) {
        return Centre.Distance(pt) < Radius + dRange;
    }

    /**
     * Moves this point by the vector given.
     *
     * @param Vector The vector.
     */
    public void Move(C2DVector Vector) {
        Centre.Move(Vector);
    }

    /**
     * Rotates this to the right about the origin provided.
     *
     * @param dAng   The angle through which to rotate.
     * @param Origin The origin about which to rotate.
     */
    public void RotateToRight(double dAng, C2DPoint Origin) {
        Centre.RotateToRight(dAng, Origin);
    }

    /**
     * Grow about the origin.
     *
     * @param dFactor The factor to grow by.
     * @param Origin  The origin.
     */
    public void Grow(double dFactor, C2DPoint Origin) {
        Centre.Grow(dFactor, Origin);
        Radius *= dFactor;
    }

    /**
     * Reflection.
     *
     * @param Point The point to reflect this through.
     */
    public void Reflect(C2DPoint Point) {
        Centre.Reflect(Point);
    }

    /**
     * Reflection trhough a line.
     *
     * @param Line The line to reflect this through.
     */
    public void Reflect(C2DLine Line) {
        Centre.Reflect(Line);
    }

    /**
     * Distance to a point.
     *
     * @param TestPoint Point to calculate the distance to.
     */
    public double Distance(C2DPoint TestPoint) {
        double dDist = Centre.Distance(TestPoint);
        return (dDist - Radius);  // -ve indicates inside.
    }

    /**
     * Distance to a point, returns the closest point on the circle.
     *
     * @param TestPoint Point to calculate the distance to.
     * @param ptOnThis  Closest point on the circle to recieve the result.
     */
    public double Distance(C2DPoint TestPoint, C2DPoint ptOnThis) {
        double dDist = Centre.Distance(TestPoint);

        if (dDist == 0) {
            // point is the centre so just arbitrary point to the circle
            C2DVector V1 = new C2DVector(Radius, 0);
            ptOnThis.Set(Centre.GetPointTo(V1));
        } else {
            // find the point on the circle.
            C2DLine LineCenToPt = new C2DLine(Centre, TestPoint);
            LineCenToPt.vector.SetLength(Radius);
            ptOnThis.Set(LineCenToPt.GetPointTo());
        }

        return (dDist - Radius);  // -ve indicates inside.
    }

    /**
     * Distance to a line, returns the closest point on the circle and the line.
     *
     * @param Line      Line to calculate the distance to.
     * @param ptOnThis  Closest point on the circle to recieve the result.
     * @param ptOnOther Closest point on the line to recieve the result.
     */
    public double Distance(C2DLine Line, C2DPoint ptOnThis, C2DPoint ptOnOther) {
        CInterval ProjInt = new CInterval();
        Project(Line, ProjInt);

        if (ProjInt.dMax < 0) {
            // This means that the circle projects entirely "below" the line so the nearest point
            // To this is the first point on the line and there are no interections.
            ptOnOther.Set(Line.point);

            return Distance(Line.point, ptOnThis);
        }

        double dLength = Line.GetLength();

        if (ProjInt.dMin > dLength) {
            // This means that the circle projects entirely "above" the line so the nearest point
            // To this is the second point on the line and there are no interections.
            C2DPoint ptClosest = new C2DPoint(Line.GetPointTo());
            ptOnOther.Set(ptClosest);
            return Distance(ptClosest, ptOnThis);
        }

        // Now find out if there's an intersection.
        ArrayList<C2DPoint> IntPts = new ArrayList<C2DPoint>();
        if (Crosses(Line, IntPts)) {
            ptOnThis.Set(IntPts.get(0));
            ptOnOther.Set(IntPts.get(0));

            return 0;
        }

        // Now find out if the line is entirely inside
        if (ProjInt.dMin > 0 && ProjInt.dMax < dLength && this.Contains(Line.point)) {
            double d1 = Distance(Line.point, ptOnThis);
            C2DPoint ptThisTemp = new C2DPoint();
            double d2 = Distance(Line.GetPointTo(), ptThisTemp);
            //     Debug.Assert(d1 < 0 && d2 < 0);
            if (d2 > d1) // NOTE USE OF > AS d2 and d1 are -ve.
            {
                ptOnThis.Set(ptThisTemp);
                ptOnOther.Set(Line.GetPointTo());
                return d2;
            } else {
                ptOnOther.Set(Line.point);
                return d1;
            }
        }

        // We now know the line is entirely outside.
        // Now find out if this is closest to a point on the line. 
        double dCenOnLine = (ProjInt.dMax + ProjInt.dMin) / 2.0;

        if (dCenOnLine > 0) {
            if (dCenOnLine < dLength) {
                // The centre is projected on the line
                double dFactor = dCenOnLine / dLength;

                C2DVector vProj = new C2DVector(Line.vector);
                vProj.Multiply(dFactor);
                C2DPoint ptOnLine = new C2DPoint(Line.point.GetPointTo(vProj));

                ptOnOther.Set(ptOnLine);

                return Distance(ptOnLine, ptOnThis);
            } else {
                // The centre is projected above the line.
                C2DPoint ptClosest = new C2DPoint(Line.GetPointTo());
                ptOnOther.Set(ptClosest);
                return Distance(ptClosest, ptOnThis);
            }
        } else {
            // This means that the circle projects entirely "below" the line.
            ptOnOther.Set(Line.point);
            return Distance(Line.point, ptOnThis);
        }
    }

    /**
     * Distance to a circle, returns the closest point on both circles.
     *
     * @param Other     Circle to calculate the distance to.
     * @param ptOnThis  Closest point on this circle to recieve the result.
     * @param ptOnOther Closest point on the other circle to recieve the result.
     */
    public double Distance(C2DCircle Other, C2DPoint ptOnThis, C2DPoint ptOnOther) {
        double dCenCenDist = Centre.Distance(Other.Centre);
        double dOtherRadius = Other.Radius;

        //    C2DPoint ptThis;
        //   C2DPoint ptOther;
        double dDist = dCenCenDist - Radius - dOtherRadius;

        if (dDist > 0) {
            // they do not interect and they are outside each other.
            C2DLine Line = new C2DLine(Centre, Other.Centre);
            Line.vector.SetLength(Radius);
            ptOnThis.Set(Line.GetPointTo());

            Line.vector.Reverse();
            Line.SetPointFrom(Other.Centre);
            Line.vector.SetLength(Other.Radius);
            ptOnOther.Set(Line.GetPointTo());
        } else {
            if ((dCenCenDist + Radius) < dOtherRadius) {
                // This is inside the other
                dDist = dCenCenDist + Radius - dOtherRadius; // -ve if inside
                C2DVector vec = new C2DVector(Other.getCentre(), Centre);
                vec.Multiply(Radius / dCenCenDist); // set the vector to be the length of my radius.
                ptOnThis.Set(Centre.GetPointTo(vec));
                vec.Multiply(dDist / Radius); // set the vector to be the distance.
                ptOnOther.Set(ptOnThis.GetPointTo(vec));

            } else if ((dCenCenDist + dOtherRadius) < Radius) {
                // The other is inside this.
                dDist = dCenCenDist + dOtherRadius - Radius; // -ve if inside
                C2DVector vec = new C2DVector(Centre, Other.getCentre());
                vec.Multiply(dOtherRadius / dCenCenDist); // set the vector to be the length of my radius.
                ptOnOther.Set(Other.getCentre().GetPointTo(vec));
                vec.Multiply(dDist / dOtherRadius); // set the vector to be the distance.
                ptOnThis.Set(ptOnOther.GetPointTo(vec));

            } else {
                // there is an intersection
                dDist = 0;
                ArrayList<C2DPoint> Ints = new ArrayList<C2DPoint>();
                if (Crosses(Other, Ints)) {
                    ptOnThis.Set(Ints.get(0));
                    ptOnOther.Set(ptOnThis);
                } else {
                    //     Debug.Assert(false);
                    return 0;
                }
            }
        }

        //      if (ptOnThis)
        //        *ptOnThis = ptThis;
        //      if (ptOnOther)
        //        *ptOnOther = ptOther;

        return dDist;
    }


    /**
     * Projection onto the line as distance along the line from the start of the line.
     * Result is stored as an CInterval Min and Max,
     *
     * @param Line     Line to project this onto.
     * @param Interval Interval to recieve the result.
     */
    public void Project(C2DLine Line, CInterval Interval) {
        // Create a line that goes through the circle from edge to edge and with the same vector as the
        // Line to project on.
        C2DLine LineCopy = new C2DLine(Centre, Line.vector);

        LineCopy.vector.SetLength(Radius * 2);

        C2DVector V2 = new C2DVector(LineCopy.vector);
        V2.Multiply(-0.5);

        LineCopy.Move(V2);

        // Now just project the line onto the interval.
        LineCopy.Project(Line, Interval);
    }

    /**
     * Projection onto the vector as distance along the line from the start of the vector.
     * Result is stored as an CInterval Min and Max,
     *
     * @param Vector   Vector to project this onto.
     * @param Interval Interval to recieve the result.
     */
    public void Project(C2DVector Vector, CInterval Interval) {
        // Create a line that goes through the circle from edge to edge and with the same vector.
        C2DLine Line = new C2DLine(Centre, Vector);

        Line.vector.SetLength(Radius * 2);

        C2DVector V2 = new C2DVector(Vector);
        V2.Multiply(-0.5);
        Line.Move(V2);

        // Now just project the line onto the interval.
        Line.Project(Vector, Interval);
    }

    /**
     * Snaps to the conceptual grid
     *
     * @param grid The grid.
     */
    public void SnapToGrid(CGrid grid) {
        Centre.SnapToGrid(grid);

        double dMultiple = Math.abs(Radius / grid.getGridSize()) + 0.5;

        dMultiple = Math.floor(dMultiple);

        if (Radius < 0)
            Radius = -dMultiple * grid.getGridSize();
        else
            Radius = dMultiple * grid.getGridSize();

    }

    /**
     * The centre.
     */
    private C2DPoint Centre = new C2DPoint();

    /**
     * The centre.
     */
    public C2DPoint getCentre() {
        return Centre;
    }

    /**
     * The radius.
     */
    public double Radius;

}