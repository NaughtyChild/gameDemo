

package uk.co.geolib.geolib;

public class C2DPoint extends C2DBase {

    /**
     * The x component of the point.
     */
    public double x;
    /**
     * The y component of the point.
     */
    public double y;

    public C2DPoint() {
    }

    /**
     * Constructor.
     *
     * @param Other Point to which this will be assigned.
     */
    public C2DPoint(C2DPoint Other) {
        x = Other.x;
        y = Other.y;
    }

    /**
     * Constructor.
     *
     * @param dx The x value of the point.
     * @param dy The y value of the point.
     */
    public C2DPoint(double dx, double dy) {
        x = dx;
        y = dy;
    }

    /**
     * Sets the x and y values of the point.
     *
     * @param dx The x value of the point.
     * @param dy The y value of the point.
     */
    public void Set(double dx, double dy) {
        x = dx;
        y = dy;
    }

    /**
     * Assignment to another point.
     *
     * @param pt The point to assign to.
     */
    public void Set(C2DPoint pt) {
        x = pt.x;
        y = pt.y;
    }

    /**
     * Construction from a vector which can be thought of as a point (and vice versa).
     *
     * @param Vector Vector to assign to.
     */
    public C2DPoint(C2DVector Vector) {
        x = Vector.i;
        y = Vector.j;
    }

    /**
     * Returns the mid point between this and the other as a new object.
     *
     * @param Other Another point.
     */
    public C2DPoint GetMidPoint(C2DPoint Other) {
        return new C2DPoint((x + Other.x) / 2, (y + Other.y) / 2);
    }

    /**
     * Projects the point on the vector given returning a distance along the vector.
     *
     * @param Vector The vector to project this on.
     */
    public double Project(C2DVector Vector) {
        C2DVector vecthis = new C2DVector(x, y);

        return (vecthis.Dot(Vector)) / Vector.GetLength();
    }

    /**
     * Projects the point on the line given returning a distance along the line from the start.
     *
     * @param Line The line to project this on.
     */
    public double Project(C2DLine Line) {
        C2DVector vecthis = new C2DVector(Line.point, this);

        return (vecthis.Dot(Line.vector)) / Line.vector.GetLength();
    }

    /**
     * Projects the point on the vector given returning a distance along the vector.
     *
     * @param Vector The vector to project this on.
     *               <param name="Interval">The interval to recieve the result.
     *               Both the min and max of the interval will be set to the result.</param>
     */
    public void Project(C2DVector Vector, CInterval Interval) {
        Interval.dMax = Project(Vector);
        Interval.dMin = Interval.dMax;
    }

    /**
     * Projects the point on the vector given returning a distance along the vector.
     *
     * @param Line The vector to project this on.
     *             <param name="Interval">The interval to recieve the result.
     *             Both the min and max of the interval will be set to the result.</param>
     */
    public void Project(C2DLine Line, CInterval Interval) {
        Interval.dMax = Project(Line);
        Interval.dMin = Interval.dMax;
    }

    /**
     * True if the point projects onto the line given and returns the point on the line.
     * Also returns whether the line projects above or below the line if relevant.
     *
     * @param Line     The line to project this on.
     * @param ptOnLine The point to recieve the result.
     * @param bAbove   The flag to indicate whether it projects above or below.
     */
    public boolean ProjectsOnLine(C2DLine Line, C2DPoint ptOnLine,
                                  Boolean bAbove) {
        C2DVector vecthis = new C2DVector(x - Line.point.x, y - Line.point.y);
        double dProj = vecthis.Dot(Line.vector);

        if (dProj < 0) {
            bAbove = false;
            return false;
        }

        double dLength = Line.vector.GetLength();

        dProj /= dLength;

        if (dProj > dLength) {
            bAbove = true;
            return false;
        }

        double dFactor = dProj / dLength;

        C2DVector vProj = new C2DVector(Line.vector);
        vProj.Multiply(dFactor);
        ptOnLine.Set(Line.point.x + vProj.i, Line.point.y + vProj.j);
        return true;

    }

    /**
     * Returns the distance between this and the other point.
     *
     * @param Other The point to return the distance to.
     */
    public double Distance(C2DPoint Other) {
        double dXD = x - Other.x;
        double dYD = y - Other.y;

        return Math.sqrt(dXD * dXD + dYD * dYD);
    }

    /**
     * Returns a vector from this to the other point as a new object.
     *
     * @param PointTo The point that vector is to go to.
     */
    public C2DVector MakeVector(C2DPoint PointTo) {
        return new C2DVector(PointTo.x - x, PointTo.y - y);
    }


    /**
     * Returns the point that the vector supplied would take this point to as a new object.
     *
     * @param V1 The vector from this to the new point.
     */
    public C2DPoint GetPointTo(C2DVector V1) {
        return new C2DPoint(x + V1.i, y + V1.j);
    }

    /**
     * Adds 2 points together.
     *
     * @param P1 The first point.
     * @param P2 The second point.
     */
    public static C2DPoint Add(C2DPoint P1, C2DPoint P2) {
        C2DPoint Result = new C2DPoint(P1.x + P2.x, P1.y + P2.y);
        return Result;
    }

    /**
     * Takes 1 point from the other.
     *
     * @param P1 The first point.
     * @param P2 The second point.
     */
    public static C2DPoint Minus(C2DPoint P1, C2DPoint P2) {
        C2DPoint Result = new C2DPoint(P1.x - P2.x, P1.y - P2.y);
        return Result;
    }

    /**
     * Multiplies the point by the factor.
     *
     * @param dFactor The multiplication factor.
     */
    public void Multiply(double dFactor) {
        x *= dFactor;
        y *= dFactor;
    }

    /**
     * Divides the point by the factor.
     *
     * @param dFactor The divisor.
     */
    public void Divide(double dFactor) {
        x /= dFactor;
        y /= dFactor;
    }

    /**
     * Equality test which really tests for point proximity.
     * <seealso cref="Constants.conEqualityTolerance"/>
     *
     * @param Other The other point.
     */
    public boolean PointEqualTo(C2DPoint Other) {
        boolean bxClose;
        boolean byClose;

        if (x == 0)
            bxClose = Other.x == 0;
        else
            bxClose = Math.abs((Other.x - x) / x) < Constants.conEqualityTolerance;

        if (!bxClose)
            return false;        // Get out early if we can.

        if (y == 0)
            byClose = Other.y == 0;
        else
            byClose = Math.abs((Other.y - y) / y) < Constants.conEqualityTolerance;

        return (byClose);        // We know x is close.
    }

    /**
     * Moves this point by the vector given.
     *
     * @param vector The vector.
     */
    public void Move(C2DVector vector) {
        x += vector.i;
        y += vector.j;
    }

    /**
     * Rotates this to the right about the origin provided.
     *
     * @param dAng   The angle through which to rotate.
     * @param Origin The origin about which to rotate.
     */
    public void RotateToRight(double dAng, C2DPoint Origin) {
        C2DVector vector = new C2DVector(Origin, this);
        vector.TurnRight(dAng);
        x = Origin.x + vector.i;
        y = Origin.y + vector.j;
    }

    /**
     * Grows this about the origin provided.
     * In the case of a point this will just move it away (or closer)
     * to the origin as there is no shape to grow.
     *
     * @param dFactor The factor to grow this by.
     * @param Origin  The origin about which to rotate.
     */
    public void Grow(double dFactor, C2DPoint Origin) {
        C2DVector vector = new C2DVector(Origin, this);
        vector.Multiply(dFactor);
        x = Origin.x + vector.i;
        y = Origin.y + vector.j;

    }

    /**
     * Reflects this through the point given.
     *
     * @param Other The point to reflect this through.
     */
    public void Reflect(C2DPoint Other) {
        // Set up a vector from this to the other
        C2DVector vec = new C2DVector(this, Other);
        // Now use the vector to find the reflection by continuing along it from the point given.
        this.Set(Other.GetPointTo(vec));
    }

    /**
     * Reflects this through the line given.
     *
     * @param Line The line to reflect this through.
     */
    public void Reflect(C2DLine Line) {
        // First find the point along the line that this projects onto.
        // Make a vector from the point on the line given to this point.
        C2DVector vecthis = new C2DVector(Line.point, this);
        // Find the length of the line given.
        double dLength = Line.vector.GetLength();
        // Now make the projection of this point on the line.
        double dProj = vecthis.Dot(Line.vector);
        dProj /= dLength;
        // Find the factor along the line that the projection is.
        double dFactor = dProj / dLength;
        // Now set up a copy of the vector of the line given.
        C2DVector vProj = new C2DVector(Line.vector);
        // Multiply that by that factor calculated.
        vProj.Multiply(dFactor);
        // Use the vector to find the point on the line.
        C2DPoint ptOnLine = new C2DPoint(Line.point.GetPointTo(vProj));
        // Noe simply reflect this in the point.
        this.Reflect(ptOnLine);
    }

    /**
     * Reflects through the y axis.
     */
    public void ReflectY() {
        x = -x;
    }

    /**
     * Reflects through the x axis.
     */
    public void ReflectX() {
        y = -y;
    }

    /**
     * Returns the bounding rectangle which will be set to this point.
     *
     * @param Rect The rectangle to recieve the result.
     */
    public void GetBoundingRect(C2DRect Rect) {
        Rect.Set(this);
    }


    /**
     * Snaps this to the grid. The x and y values can only be multiples or the grid size.
     *
     * @param grid The grid to snap this to.
     */
    public void SnapToGrid(CGrid grid) {
        double dxMultiple = Math.abs(x / grid.getGridSize()) + 0.5;

        dxMultiple = Math.floor(dxMultiple);

        if (x < 0)
            x = -dxMultiple * grid.getGridSize();
        else
            x = dxMultiple * grid.getGridSize();

        double dyMultiple = Math.abs(y / grid.getGridSize()) + 0.5;

        dyMultiple = Math.floor(dyMultiple);

        if (y < 0)
            y = -dyMultiple * grid.getGridSize();
        else
            y = dyMultiple * grid.getGridSize();
    }

    /**
     * True if the point projects onto the line given and returns the
     * point on the line.
     *
     * @param Line
     */
    public void ProjectOnRay(C2DLine Line) {
        C2DVector vecthis = new C2DVector(Line.point, this);
        double dProj = vecthis.Dot(Line.vector);

        double dFactor = dProj / (Line.vector.i * Line.vector.i + Line.vector.j * Line.vector.j);

        C2DVector vProj = new C2DVector(Line.vector);
        vProj.i *= dFactor;
        vProj.j *= dFactor;
        this.Set(Line.point.x + vProj.i, Line.point.y + vProj.j);
    }


}