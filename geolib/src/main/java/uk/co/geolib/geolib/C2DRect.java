
package uk.co.geolib.geolib;

public class C2DRect extends C2DBase {
    /**
     * Constructor.
     */
    public C2DRect() {
    }


    /**
     * Constructor.
     *
     * @param Other The other rect.
     */
    public C2DRect(C2DRect Other) {
        topLeft.Set(Other.topLeft);
        bottomRight.Set(Other.bottomRight);
    }

    /**
     * Constructor.
     *
     * @param pttopLeft     The top left point.
     * @param ptbottomRight The bottom right point.
     */
    public C2DRect(C2DPoint pttopLeft, C2DPoint ptbottomRight) {
        topLeft.Set(pttopLeft);
        bottomRight.Set(ptbottomRight);
    }

    /**
     * Constructor.
     *
     * @param dLeft   Left.
     * @param dTop    Top.
     * @param dRight  Right.
     * @param dBottom Bottom.
     */
    public C2DRect(double dLeft, double dTop, double dRight, double dBottom) {
        topLeft.x = dLeft;
        topLeft.y = dTop;

        bottomRight.x = dRight;
        bottomRight.y = dBottom;
    }

    /**
     * Constructor sets both the top left and bottom right to equal the rect.
     *
     * @param pt Point.
     */
    public C2DRect(C2DPoint pt) {
        topLeft.Set(pt);
        bottomRight.Set(pt);
    }

    /**
     * Sets both the top left and bottom right to equal the rect.
     *
     * @param pt Point.
     */
    public void Set(C2DPoint pt) {
        topLeft.Set(pt);
        bottomRight.Set(pt);
    }

    /**
     * Assignment.
     *
     * @param pttopLeft     The top left point.
     * @param ptbottomRight The bottom right point.
     */
    public void Set(C2DPoint pttopLeft, C2DPoint ptbottomRight) {
        topLeft.Set(pttopLeft);
        bottomRight.Set(ptbottomRight);
    }

    /**
     * Assignment.
     *
     * @param dLeft   Left.
     * @param dTop    Top.
     * @param dRight  Right.
     * @param dBottom Bottom.
     */
    public void Set(double dLeft, double dTop, double dRight, double dBottom) {
        topLeft.x = dLeft;
        topLeft.y = dTop;

        bottomRight.x = dRight;
        bottomRight.y = dBottom;
    }

    /**
     * Assignment.
     *
     * @param dTop Top.
     */
    public void SetTop(double dTop) {
        topLeft.y = dTop;
    }


    /**
     * Assignment.
     *
     * @param dLeft Left.
     */
    public void SetLeft(double dLeft) {
        topLeft.x = dLeft;
    }

    /**
     * Assignment.
     *
     * @param dBottom Bottom.
     */
    public void SetBottom(double dBottom) {
        bottomRight.y = dBottom;
    }


    /**
     * Assignment.
     *
     * @param dRight Right.
     */
    public void SetRight(double dRight) {
        bottomRight.x = dRight;
    }

    /**
     * Clears the rectangle.
     */
    public void Clear() {
        topLeft.x = 0;
        topLeft.y = 0;
        bottomRight.x = 0;
        bottomRight.y = 0;
    }

    /**
     * Expands to include the point.
     *
     * @param NewPt Point.
     */
    public void ExpandToInclude(C2DPoint NewPt) {
        if (NewPt.x > bottomRight.x)
            bottomRight.x = NewPt.x;
        else if (NewPt.x < topLeft.x)
            topLeft.x = NewPt.x;
        if (NewPt.y > topLeft.y)
            topLeft.y = NewPt.y;
        else if (NewPt.y < bottomRight.y)
            bottomRight.y = NewPt.y;
    }

    /**
     * Expands to include the rectangle.
     *
     * @param Other Rectangle.
     */
    public void ExpandToInclude(C2DRect Other) {
        ExpandToInclude(Other.topLeft);
        ExpandToInclude(Other.bottomRight);
    }

    /**
     * True if there is an overlap, returns the overlap.
     *
     * @param Other   Rectangle.
     * @param Overlap Output. The overlap.
     */
    public boolean Overlaps(C2DRect Other, C2DRect Overlap) {
        C2DPoint ptOvTL = new C2DPoint();
        C2DPoint ptOvBR = new C2DPoint();

        ptOvTL.y = Math.min(topLeft.y, Other.topLeft.y);
        ptOvBR.y = Math.max(bottomRight.y, Other.bottomRight.y);

        ptOvTL.x = Math.max(topLeft.x, Other.topLeft.x);
        ptOvBR.x = Math.min(bottomRight.x, Other.bottomRight.x);

        Overlap.Set(ptOvTL, ptOvBR);

        return Overlap.IsValid();
    }

    /**
     * True if the point is within the rectangle.
     *
     * @param Pt Point.
     */
    public boolean Contains(C2DPoint Pt) {
        return (Pt.x >= topLeft.x && Pt.x <= bottomRight.x &&
                Pt.y <= topLeft.y && Pt.y >= bottomRight.y);
    }


    /**
     * True if the entire other rectangle is within.
     *
     * @param Other Other rectangle.
     */
    public boolean Contains(C2DRect Other) {
        return (Other.GetLeft() > topLeft.x &&
                Other.GetRight() < bottomRight.x &&
                Other.GetBottom() > bottomRight.y &&
                Other.GetTop() < topLeft.y);
    }

    /**
     * True if there is an overlap.
     *
     * @param Other Other rectangle.
     */
    public boolean Overlaps(C2DRect Other) {
        boolean bOvX = !(Other.GetLeft() >= bottomRight.x ||
                Other.GetRight() <= topLeft.x);

        boolean bOvY = !(Other.GetBottom() >= topLeft.y ||
                Other.GetTop() <= bottomRight.y);

        return bOvX && bOvY;
    }

    /**
     * If the area is positive e.g. the top is greater than the bottom.
     */
    public boolean IsValid() {
        return ((topLeft.x < bottomRight.x) && (topLeft.y > bottomRight.y));
    }

    /**
     * Returns the area.
     */
    public double GetArea() {
        return ((topLeft.y - bottomRight.y) * (bottomRight.x - topLeft.x));
    }

    /**
     * Returns the width.
     */
    public double Width() {
        return (bottomRight.x - topLeft.x);
    }

    /**
     * Returns the height.
     */
    public double Height() {
        return (topLeft.y - bottomRight.y);
    }

    /**
     * Returns the top.
     */
    public double GetTop() {
        return topLeft.y;
    }

    /**
     * Returns the left.
     */
    public double GetLeft() {
        return topLeft.x;
    }


    /**
     * Returns the bottom.
     */
    public double GetBottom() {
        return bottomRight.y;
    }

    /**
     * Returns the right.
     */
    public double GetRight() {
        return bottomRight.x;
    }

    /**
     * Assignment.
     *
     * @param Other Other rectangle.
     */
    public void Set(C2DRect Other) {
        topLeft.x = Other.topLeft.x;
        topLeft.y = Other.topLeft.y;
        bottomRight.x = Other.bottomRight.x;
        bottomRight.y = Other.bottomRight.y;
    }

    /**
     * Grows it from its centre.
     *
     * @param dFactor Factor to grow by.
     */
    public void Grow(double dFactor) {
        C2DPoint ptCentre = new C2DPoint(GetCentre());

        bottomRight.x = (bottomRight.x - ptCentre.x) * dFactor + ptCentre.x;
        bottomRight.y = (bottomRight.y - ptCentre.y) * dFactor + ptCentre.y;

        topLeft.x = (topLeft.x - ptCentre.x) * dFactor + ptCentre.x;
        topLeft.y = (topLeft.y - ptCentre.y) * dFactor + ptCentre.y;

    }

    /**
     * Grow the height it from its centre.
     *
     * @param dFactor Factor to grow by.
     */
    public void GrowHeight(double dFactor) {
        C2DPoint ptCentre = new C2DPoint(GetCentre());
        bottomRight.y = (bottomRight.y - ptCentre.y) * dFactor + ptCentre.y;
        topLeft.y = (topLeft.y - ptCentre.y) * dFactor + ptCentre.y;

    }

    /**
     * Grows the width from its centre.
     *
     * @param dFactor Factor to grow by.
     */
    public void GrowWidth(double dFactor) {
        C2DPoint ptCentre = new C2DPoint(GetCentre());
        bottomRight.x = (bottomRight.x - ptCentre.x) * dFactor + ptCentre.x;
        topLeft.x = (topLeft.x - ptCentre.x) * dFactor + ptCentre.x;

    }

    /**
     * Expands from the centre by the fixed amount given.
     *
     * @param dRange Amount to expand by.
     */
    public void Expand(double dRange) {
        bottomRight.x += dRange;
        bottomRight.y -= dRange;

        topLeft.x -= dRange;
        topLeft.y += dRange;
    }

    /**
     * Grows it from the given point.
     *
     * @param dFactor Factor to grow by.
     * @param Origin  The origin.
     */
    public void Grow(double dFactor, C2DPoint Origin) {
        bottomRight.Grow(dFactor, Origin);
        topLeft.Grow(dFactor, Origin);
    }

    /**
     * Moves this point by the vector given.
     *
     * @param Vector The vector.
     */
    public void Move(C2DVector Vector) {
        topLeft.Move(Vector);
        bottomRight.Move(Vector);
    }

    /**
     * Reflect throught the point given.
     * Switches Top Left and Bottom Right to maintain validity.
     *
     * @param Point Reflection point.
     */
    public void Reflect(C2DPoint Point) {
        topLeft.Reflect(Point);
        bottomRight.Reflect(Point);

        double x = topLeft.x;
        double y = topLeft.y;

        topLeft.Set(bottomRight);
        bottomRight.x = x;
        bottomRight.y = y;
    }

    /**
     * Reflect throught the line by reflecting the centre of the
     * rect and keeping the validity.
     *
     * @param Line Reflection Line.
     */
    public void Reflect(C2DLine Line) {
        C2DPoint ptCen = new C2DPoint(this.GetCentre());
        C2DPoint ptNewCen = new C2DPoint(ptCen);
        ptNewCen.Reflect(Line);
        C2DVector vec = new C2DVector(ptCen, ptNewCen);
        Move(vec);
    }

    /**
     * Rotates this to the right about the origin provided.
     * Note that as the horizontal/vertical line property will be
     * preserved. If you rotate an object and its bounding box, the box may not still
     * bound the object.
     *
     * @param dAng   The angle through which to rotate.
     * @param Origin The origin about which to rotate.
     */
    public void RotateToRight(double dAng, C2DPoint Origin) {
        double dHalfWidth = Width() / 2;
        double dHalfHeight = Height() / 2;

        C2DPoint ptCen = new C2DPoint(GetCentre());
        ptCen.RotateToRight(dAng, Origin);

        topLeft.x = ptCen.x - dHalfWidth;
        topLeft.y = ptCen.y + dHalfHeight;
        bottomRight.x = ptCen.x + dHalfWidth;
        bottomRight.y = ptCen.y - dHalfHeight;
    }

    /**
     * Returns the distance from this to the point. 0 if the point inside.
     *
     * @param TestPoint Test Point.
     */
    public double Distance(C2DPoint TestPoint) {
        if (TestPoint.x > bottomRight.x) // To the east half
        {
            if (TestPoint.y > topLeft.y)            // To the north east
                return TestPoint.Distance(new C2DPoint(bottomRight.x, topLeft.y));
            else if (TestPoint.y < bottomRight.y)        // To the south east
                return TestPoint.Distance(bottomRight);
            else
                return (TestPoint.x - bottomRight.x);    // To the east
        } else if (TestPoint.x < topLeft.x)    // To the west half
        {
            if (TestPoint.y > topLeft.y)            // To the north west
                return TestPoint.Distance(topLeft);
            else if (TestPoint.y < bottomRight.y)        // To the south west
                return TestPoint.Distance(new C2DPoint(topLeft.x, bottomRight.y));
            else
                return (topLeft.x - TestPoint.x);    // To the west
        } else {
            if (TestPoint.y > topLeft.y)        //To the north
                return (TestPoint.y - topLeft.y);
            else if (TestPoint.y < bottomRight.y)    // To the south
                return (bottomRight.y - TestPoint.y);
        }

        //  assert(Contains(TestPoint));
        return 0;    // Inside
    }

    /**
     * Returns the distance from this to the other rect. 0 if there is an overlap.
     *
     * @param Other Other rectangle.
     */
    public double Distance(C2DRect Other) {
        if (this.Overlaps(Other))
            return 0;

        if (Other.GetLeft() > this.bottomRight.x) {
            // Other is to the right
            if (Other.GetBottom() > this.topLeft.y) {
                // Other is to the top right
                C2DPoint ptTopRight = new C2DPoint(bottomRight.x, topLeft.y);
                return ptTopRight.Distance(new C2DPoint(Other.GetLeft(), Other.GetBottom()));
            } else if (Other.GetTop() < this.bottomRight.y) {
                // Other to the bottom right
                return bottomRight.Distance(Other.topLeft);
            } else {
                // to the right
                return Other.GetLeft() - this.bottomRight.x;
            }
        } else if (Other.GetRight() < this.topLeft.x) {
            // Other to the left
            if (Other.GetBottom() > this.topLeft.y) {
                // Other is to the top left
                return topLeft.Distance(Other.bottomRight);
            } else if (Other.GetTop() < this.bottomRight.y) {
                // Other to the bottom left
                C2DPoint ptBottomLeft = new C2DPoint(topLeft.x, bottomRight.y);
                return ptBottomLeft.Distance(new C2DPoint(Other.GetRight(), Other.GetTop()));
            } else {
                //Just to the left
                return (this.topLeft.x - Other.GetRight());
            }
        } else {
            // There is horizontal overlap;
            if (Other.GetBottom() > topLeft.y)
                return Other.GetBottom() - topLeft.y;
            else
                return bottomRight.y - Other.GetTop();
        }

    }

    /**
     * Returns the bounding rectangle. (Required for virtual base class).
     *
     * @param Rect Ouput. Bounding rectangle.
     */
    public void GetBoundingRect(C2DRect Rect) {
        Rect.Set(this);
    }

    /**
     * Scales the rectangle accordingly.
     */
    public void Scale(C2DPoint ptScale) {
        topLeft.x = topLeft.x * ptScale.x;
        topLeft.y = topLeft.y * ptScale.y;

        bottomRight.x = bottomRight.x * ptScale.x;
        bottomRight.y = bottomRight.y * ptScale.y;
    }

    /**
     * Returns the centre.
     */
    public C2DPoint GetCentre() {
        return bottomRight.GetMidPoint(topLeft);
    }

    /**
     * Returns the point which is closest to the origin (0,0).
     */
    public C2DPoint GetPointClosestToOrigin() {
        C2DPoint ptResult = new C2DPoint();
        if (Math.abs(topLeft.x) < Math.abs(bottomRight.x)) {
            // Left is closest to the origin.
            ptResult.x = topLeft.x;
        } else {
            // Right is closest to the origin
            ptResult.x = bottomRight.x;
        }

        if (Math.abs(topLeft.y) < Math.abs(bottomRight.y)) {
            // Top is closest to the origin.
            ptResult.y = topLeft.y;
        } else {
            // Bottom is closest to the origin
            ptResult.y = bottomRight.y;
        }

        return ptResult;
    }

    /**
     * Returns the point which is furthest from the origin (0,0).
     */
    public C2DPoint GetPointFurthestFromOrigin() {
        C2DPoint ptResult = new C2DPoint();
        if (Math.abs(topLeft.x) > Math.abs(bottomRight.x)) {
            // Left is furthest to the origin.
            ptResult.x = topLeft.x;
        } else {
            // Right is furthest to the origin
            ptResult.x = bottomRight.x;
        }

        if (Math.abs(topLeft.y) > Math.abs(bottomRight.y)) {
            // Top is furthest to the origin.
            ptResult.y = topLeft.y;
        } else {
            // Bottom is furthest to the origin
            ptResult.y = bottomRight.y;
        }

        return ptResult;
    }

    /**
     * Projection onto the line
     *
     * @param Line     Line to project on.
     * @param Interval Ouput. Projection.
     */
    public void Project(C2DLine Line, CInterval Interval) {
        this.topLeft.Project(Line, Interval);
        Interval.ExpandToInclude(bottomRight.Project(Line));
        C2DPoint TR = new C2DPoint(bottomRight.x, topLeft.y);
        C2DPoint BL = new C2DPoint(topLeft.x, bottomRight.y);
        Interval.ExpandToInclude(TR.Project(Line));
        Interval.ExpandToInclude(BL.Project(Line));

    }

    /**
     * Projection onto the Vector.
     *
     * @param Vector   Vector to project on.
     * @param Interval Ouput. Projection.
     */
    public void Project(C2DVector Vector, CInterval Interval) {
        this.topLeft.Project(Vector, Interval);
        Interval.ExpandToInclude(bottomRight.Project(Vector));
        C2DPoint TR = new C2DPoint(bottomRight.x, topLeft.y);
        C2DPoint BL = new C2DPoint(topLeft.x, bottomRight.y);
        Interval.ExpandToInclude(TR.Project(Vector));
        Interval.ExpandToInclude(BL.Project(Vector));

    }

    /**
     * Snaps this to the conceptual grid.
     *
     * @param grid Grid to snap to.
     */
    public void SnapToGrid(CGrid grid) {
        topLeft.SnapToGrid(grid);
        bottomRight.SnapToGrid(grid);

    }


    /**
     * True if this is above or below the other
     *
     * @param Other <returns></returns>
     */
    public boolean OverlapsVertically(C2DRect Other) {
        return !(Other.GetLeft() >= bottomRight.x ||
                Other.GetRight() <= topLeft.x);
    }


    /**
     * True if this is above the other.
     *
     * @param Other <returns></returns>
     */
    public boolean OverlapsAbove(C2DRect Other) {
        if (Other.GetLeft() >= bottomRight.x ||
                Other.GetRight() <= topLeft.x) {
            return false;
        } else {
            return topLeft.y > Other.GetBottom();
        }
    }


    /**
     * True if this is below the other.
     *
     * @param Other <returns></returns>
     */
    public boolean OverlapsBelow(C2DRect Other) {
        if (Other.GetLeft() >= bottomRight.x ||
                Other.GetRight() <= topLeft.x) {
            return false;
        } else {
            return bottomRight.y < Other.GetTop();
        }
    }


    /**
     * Returns the top left.
     */
    public C2DPoint GetBottomLeft() {
        return new C2DPoint(this.topLeft.x, this.bottomRight.y);
    }

    /**
     * Returns the bottom right.
     */
    public C2DPoint GetTopRight() {
        return new C2DPoint(this.bottomRight.x, this.topLeft.y);
    }


    /**
     * True if this crosses the line.
     */
    public boolean Crosses(C2DLineBase line) {
        C2DLine l1 = new C2DLine(bottomRight, GetTopRight());
        if (line.Crosses(l1))
            return true;

        C2DLine l2 = new C2DLine(GetTopRight(), topLeft);
        if (line.Crosses(l2))
            return true;

        C2DLine l3 = new C2DLine(topLeft, GetBottomLeft());
        if (line.Crosses(l3))
            return true;

        C2DLine l4 = new C2DLine(GetBottomLeft(), bottomRight);
        if (line.Crosses(l4))
            return true;

        return false;
    }


    /**
     * Top left.
     */
    private C2DPoint topLeft = new C2DPoint();

    /**
     * Top left.
     */
    public C2DPoint getTopLeft() {
        return topLeft;
    }

    /**
     * Bottom right.
     */
    private C2DPoint bottomRight = new C2DPoint();

    /**
     * Bottom right.
     */
    public C2DPoint getBottomRight() {
        return bottomRight;
    }

}