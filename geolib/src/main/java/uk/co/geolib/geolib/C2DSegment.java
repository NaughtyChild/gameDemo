
package uk.co.geolib.geolib;

public class C2DSegment extends C2DBase {
    /**
     * Constructor.
     */
    public C2DSegment() {
    }


    /**
     * Constructor.
     *
     * @param ArcOther The other arc.
     */
    public C2DSegment(C2DArc ArcOther) {
        arc.Set(ArcOther);
    }

    /**
     * Contructor.
     *
     * @param PtFrom         The point the arc is to go from.
     * @param PtTo           The point the arc is to go to.
     * @param dRadius        The corresponding circles radius.
     * @param bCentreOnRight Whether the centre is on the right.
     * @param bArcOnRight    Whether the arc is to the right of the line.
     */
    public C2DSegment(C2DPoint PtFrom, C2DPoint PtTo, double dRadius,
                      boolean bCentreOnRight, boolean bArcOnRight) {
        arc.Set(PtFrom, PtTo, dRadius, bCentreOnRight, bArcOnRight);
    }


    /**
     * Contructor.
     *
     * @param PtFrom         The point the arc is to go from.
     * @param Vector         The vector defining the end point.
     * @param dRadius        The corresponding circles radius.
     * @param bCentreOnRight Whether the centre is on the right.
     * @param bArcOnRight    Whether the arc is to the right of the line.
     */
    public C2DSegment(C2DPoint PtFrom, C2DVector Vector, double dRadius,
                      boolean bCentreOnRight, boolean bArcOnRight) {
        arc.Set(PtFrom, Vector, dRadius, bCentreOnRight, bArcOnRight);
    }


    /**
     * Contructor.
     *
     * @param Line           The line defining the start and end point of the arc.
     * @param dRadius        The corresponding circles radius.
     * @param bCentreOnRight Whether the centre is on the right.
     * @param bArcOnRight    Whether the arc is to the right of the line.
     */
    public C2DSegment(C2DLine Line, double dRadius,
                      boolean bCentreOnRight, boolean bArcOnRight) {
        arc.Set(Line, dRadius, bCentreOnRight, bArcOnRight);
    }


    /**
     * Assignment.
     *
     * @param PtFrom         The point the arc is to go from.
     * @param PtTo           The point the arc is to go to.
     * @param dRadius        The corresponding circles radius.
     * @param bCentreOnRight Whether the centre is on the right.
     * @param bArcOnRight    Whether the arc is to the right of the line.
     */
    public void Set(C2DPoint PtFrom, C2DPoint PtTo, double dRadius,
                    boolean bCentreOnRight, boolean bArcOnRight) {
        arc.Set(PtFrom, PtTo, dRadius, bCentreOnRight, bArcOnRight);
    }

    /**
     * Assignment.
     *
     * @param PtFrom         The point the arc is to go from.
     * @param Vector         The vector defining the end point.
     * @param dRadius        The corresponding circles radius.
     * @param bCentreOnRight Whether the centre is on the right.
     * @param bArcOnRight    Whether the arc is to the right of the line.
     */
    public void Set(C2DPoint PtFrom, C2DVector Vector, double dRadius,
                    boolean bCentreOnRight, boolean bArcOnRight) {
        arc.Set(PtFrom, Vector, dRadius, bCentreOnRight, bArcOnRight);
    }

    /**
     * Assignment.
     *
     * @param Line           The line defining the start and end point of the arc.
     * @param dRadius        The corresponding circles radius.
     * @param bCentreOnRight Whether the centre is on the right.
     * @param bArcOnRight    Whether the arc is to the right of the line.
     */
    public void Set(C2DLine Line, double dRadius,
                    boolean bCentreOnRight, boolean bArcOnRight) {
        arc.Set(Line, dRadius, bCentreOnRight, bArcOnRight);
    }

    /**
     * Tests to see if the radius is large enough to connect the end points.
     */
    public boolean IsValid() {
        return arc.IsValid();
    }

    /**
     * Returns the corresponding circle's centre.
     */
    public C2DPoint GetCircleCentre() {
        return arc.GetCircleCentre();
    }

    /**
     * Returns the perimeter of the shape.
     */
    public double GetPerimeter() {
        return (arc.getline().vector.GetLength() + arc.GetLength());
    }

    /**
     * Returns the length of the arc.
     */
    public double GetArcLength() {
        return arc.GetLength();
    }

    /**
     * Returns the bounding rectangle.
     */
    public void GetBoundingRect(C2DRect Rect) {
        arc.GetBoundingRect(Rect);
    }

    /**
     * Returns the inverse of this i.e. the other part of the circle to this.
     *
     * @param Other The other segment.
     */
    public void GetInverse(C2DSegment Other) {
        Other.Set(arc.getline(), arc.Radius,
                arc.CentreOnRight, !arc.ArcOnRight);
    }

    /**
     * Always +ve and LESS than PI. In radians.
     */
    public double GetSegmentAngle() {
        if (!IsValid())
            return 0;

        return arc.GetSegmentAngle();
    }

    /**
     * Returns the area.
     */
    public double GetArea() {
        double dSegAng = arc.GetSegmentAngle();
        double dRadius = arc.Radius;
        if (arc.CentreOnRight ^ arc.ArcOnRight) {
            return (dRadius * dRadius * (dSegAng - Math.sin(dSegAng)) / 2);

        } else {
            // if the curve is the big bit.
            return (dRadius * dRadius * (Constants.conPI - (dSegAng - Math.sin(dSegAng)) / 2));
        }
    }

    /**
     * Returns the area which is positive if anti-clockwise -ve if clockwise
     */
    public double GetAreaSigned() {
        if (arc.ArcOnRight)
            return GetArea();
        else
            return -GetArea();
    }

    /**
     * Returns the centroid.
     */
    public C2DPoint GetCentroid() {
        // Find the area first. Do it explicitly as we may need bits of the calc later.
        double dSegAng = arc.GetSegmentAngle();
        boolean bBig = arc.ArcOnRight == arc.CentreOnRight;

        double dRadius = arc.Radius;
        double dRadiusSquare = dRadius * dRadius;
        double dCircleArea = dRadiusSquare * Constants.conPI;
        double dArea = dRadiusSquare * ((dSegAng - Math.sin(dSegAng)) / 2);

        // Find the maximum length of the small segment along the direction of the line.
        double dLength = arc.getline().GetLength();
        // Now find the average height of the segment over that line
        double dHeight = dArea / dLength;

        // Find the centre point on the line and the centre of the circle
        C2DPoint ptLineCen = new C2DPoint(arc.getline().GetMidPoint());
        C2DPoint ptCircleCen = new C2DPoint(arc.GetCircleCentre());

        // Set up a line from the mid point on the line to the circle centre
        // then set the length of it to the average height divided by 2. The end
        // point of the line is then the centroid. If we are using the small bit, 
        // The line needs to be reversed.
        C2DLine Line = new C2DLine(ptLineCen, ptCircleCen);

        Line.vector.Reverse();

        Line.vector.SetLength(dHeight / 2);

        if (bBig) {
            C2DPoint ptSmallCen = new C2DPoint(Line.GetPointTo());
            // Return the weighted average of the 2 centroids.

            ptCircleCen.Multiply(dCircleArea);
            ptSmallCen.Multiply(dArea);
            C2DPoint pRes = C2DPoint.Minus(ptCircleCen, ptSmallCen);
            pRes.Multiply(1.0 / (dCircleArea - dArea));
            return pRes;
            //     return ( new C2DPoint(ptCircleCen * dCircleArea - ptSmallCen * dArea) ) / ( dCircleArea - dArea);
        } else
            return Line.GetPointTo();
    }

    /**
     * Gets the first point on the straight line.
     */
    public C2DPoint GetPointFrom() {
        return arc.getline().GetPointFrom();
    }

    /**
     * Gets the second point on the straight line.
     */
    public C2DPoint GetPointTo() {
        return arc.getline().GetPointTo();
    }


    /**
     * Returns a reference to the line as a new object.
     */
    public C2DLine GetLine() {
        return new C2DLine(arc.getline());
    }

    /**
     * Returns whether the point is in the shape.
     */
    public boolean Contains(C2DPoint TestPoint) {
        C2DPoint ptCentre = new C2DPoint(GetCircleCentre());

        if (TestPoint.Distance(ptCentre) > arc.Radius)
            return false;

        else {
            if (arc.getline().IsOnRight(TestPoint)) {
                return arc.ArcOnRight;
            } else {
                return !arc.ArcOnRight;
            }
        }
    }

    /**
     * Moves this point by the vector given.
     *
     * @param vector The vector.
     */
    public void Move(C2DVector vector) {
        arc.Move(vector);
    }

    /**
     * Rotates this to the right about the origin provided.
     *
     * @param dAng   The angle through which to rotate.
     * @param Origin The origin about which to rotate.
     */
    public void RotateToRight(double dAng, C2DPoint Origin) {
        arc.RotateToRight(dAng, Origin);
    }

    /**
     * Grows the segment by the factor from the origin provided.
     *
     * @param dFactor The factor to grow by.
     * @param Origin  The origin about which to grow.
     */
    public void Grow(double dFactor, C2DPoint Origin) {
        arc.Grow(dFactor, Origin);
    }

    /**
     * Reflects the shape throught the point given.
     *
     * @param point The point to reflect through.
     */
    public void Reflect(C2DPoint point) {
        arc.Reflect(point);
    }

    /**
     * Reflects the in the line given.
     *
     * @param Line The line to reflect through.
     */
    public void Reflect(C2DLine Line) {
        arc.Reflect(Line);
    }

    /**
     * Returns the distance to the point given.
     *
     * @param TestPoint The point.
     */
    public double Distance(C2DPoint TestPoint) {
        if (Contains(TestPoint))
            return 0;
        double d1 = arc.Distance(TestPoint);
        double d2 = arc.getline().Distance(TestPoint);
        return Math.min(d1, d2);
    }

    /**
     * Projects this onto the line given.
     *
     * @param Line     The line.
     * @param Interval The projection.
     */
    public void Project(C2DLine Line, CInterval Interval) {
        arc.Project(Line, Interval);
        CInterval LineInterval = new CInterval();
        arc.getline().Project(Line, LineInterval);
        Interval.ExpandToInclude(LineInterval);
    }

    /**
     * Projects this onto the vector given.
     *
     * @param Vector   The Vector.
     * @param Interval The projection.
     */
    public void Project(C2DVector Vector, CInterval Interval) {
        arc.Project(Vector, Interval);
        CInterval LineInterval = new CInterval();
        arc.getline().Project(Vector, LineInterval);
        Interval.ExpandToInclude(LineInterval);
    }

    /**
     * Snaps this to the conceptual grid.
     *
     * @param grid The grid.
     */
    public void SnapToGrid(CGrid grid) {
        arc.SnapToGrid(grid);

    }

    /**
     * The arc.
     */
    protected C2DArc arc = new C2DArc();

    /**
     * The arc.
     */
    public C2DArc getArc() {
        return arc;
    }
}