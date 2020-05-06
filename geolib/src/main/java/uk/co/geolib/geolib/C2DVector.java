
package uk.co.geolib.geolib;


public class C2DVector {

    /**
     * The value in the x axis.
     */
    public double i;

    /**
     * The value in the y axis.
     */
    public double j;

    public C2DVector() {
    }

    /**
     * Constructor with assignment.
     *
     * @param di i.
     * @param dj j.
     */
    public C2DVector(double di, double dj) {
        i = di;
        j = dj;
    }

    /**
     * Constructor with assignment.
     *
     * @param Other other vector.
     */
    public C2DVector(C2DVector Other) {
        i = Other.i;
        j = Other.j;
    }

    /**
     * Constructor provides 2 points, this being the vector from the first to the second.
     *
     * @param PointFrom Point 1.
     * @param PointTo   Point 2.
     */
    public C2DVector(C2DPoint PointFrom, C2DPoint PointTo) {
        i = PointTo.x - PointFrom.x;
        j = PointTo.y - PointFrom.y;
    }

    /**
     * Constructor converts a point to the vector (a point can be interpreted as a point and vice versa)
     *
     * @param Point Point to assign to.
     */
    public C2DVector(C2DPoint Point) {
        i = Point.x;
        j = Point.y;
    }

    /**
     * Constructor converts a point to the vector (a point can be interpreted as a point and vice versa)
     *
     * @param di i.
     * @param dj j.
     */
    public void Set(double di, double dj) {
        i = di;
        j = dj;
    }

    /**
     * Sets it to be the vector from the 1st to the second.
     *
     * @param PointFrom Point 1.
     * @param PointTo   Point 2.
     */
    public void Set(C2DPoint PointFrom, C2DPoint PointTo) {
        i = PointTo.x - PointFrom.x;
        j = PointTo.y - PointFrom.y;
    }

    /**
     * Assignment.
     *
     * @param Other Other vector.
     */
    public void Set(C2DVector Other) {
        i = Other.i;
        j = Other.j;
    }

    /**
     * Reverses the direction of the vector.
     */
    public void Reverse() {
        i = -i;
        j = -j;
    }

    /**
     * Turns right 90 degrees.
     */
    public void TurnRight() {
        double tempi = i;
        i = j;
        j = -tempi;
    }

    /**
     * Turns right by the angle given in radians.
     *
     * @param dAng Angle to turn through.
     */
    public void TurnRight(double dAng) {
        TurnLeft(Constants.conTWOPI - dAng);
    }

    /**
     * Turns left by 90 degrees.
     */
    public void TurnLeft() {
        double tempi = i;
        i = -j;
        j = tempi;
    }

    /**
     * Turns left by the angle given in radians.
     *
     * @param dAng Angle to turn through.
     */
    public void TurnLeft(double dAng) {
        double temp = i;

        i = Math.cos(dAng) * i - Math.sin(dAng) * j;
        j = Math.sin(dAng) * temp + Math.cos(dAng) * j;
    }

    /**
     * Returns the length of the vector.
     */
    public double GetLength() {
        return Math.sqrt(i * i + j * j);
    }

    /**
     * Sets the length of the vector.
     *
     * @param dDistance The new length.
     */
    public void SetLength(double dDistance) {
        MakeUnit();
        i = i * dDistance;
        j = j * dDistance;
    }

    /**
     * Makes the vector unit.
     */
    public void MakeUnit() {
        double dDistance = GetLength();
        if (dDistance == 0)
            return;
        i = i / dDistance;
        j = j / dDistance;
    }

    /**
     * Addition.
     */
    public static C2DVector Add(C2DVector V1, C2DVector V2) {
        C2DVector V3 = new C2DVector(V1.i + V2.i, V1.j + V2.j);
        return V3;
    }

    /**
     * Subtraction.
     */
    public static C2DVector Minus(C2DVector V1, C2DVector V2) {
        C2DVector V3 = new C2DVector(V1.i - V2.i, V1.j - V2.j);
        return V3;
    }

    /**
     * Multiplication.
     */
    public void Multiply(double dFactor) {
        i *= dFactor;
        j *= dFactor;
    }

    /**
     * Dot product.
     */
    public double Dot(C2DVector Other) {
        return i * Other.i + j * Other.j;
    }

    /**
     * Cross product.
     */
    public double Cross(C2DVector Other) {
        return i * Other.i - j * Other.j;
    }

    /**
     * Assignment to a point.
     */
    public void Set(C2DPoint Other) {
        i = Other.x;
        j = Other.y;
    }

    /**
     * Equality test.
     */
    public boolean VectorEqualTo(C2DVector Other) {
        boolean biClose;
        boolean bjClose;

        if (i == 0)
            biClose = Other.i == 0;
        else
            biClose = Math.abs((Other.i - i) / i) < Constants.conEqualityTolerance;
        if (j == 0)
            bjClose = Other.j == 0;
        else
            bjClose = Math.abs((Other.j - j) / j) < Constants.conEqualityTolerance;

        return (biClose && bjClose);
    }

    /**
     * Returns the angle from north in radians.
     */
    public double AngleFromNorth() {
        if (j == 0) {
            if (i > 0)
                return Constants.conHALFPI;
            else
                return 3 * Constants.conHALFPI;
        }
        if (i == 0) {
            if (j > 0)
                return 0;
            else
                return Constants.conPI;
        }

        double ang = Math.atan(i / j);

        if (j < 0) ang += Constants.conPI;
        else if (i < 0) ang += 2 * Constants.conPI;

        return ang;
    }

    /**
     * Returns the angle to the right to another vector.
     */
    public double AngleToRight(C2DVector Other) {
        double Result = Other.AngleFromNorth() - AngleFromNorth();
        if (Result < 0) Result += Constants.conTWOPI;

        return Result;
    }

    /**
     * Returns the angle to the left to another vector.
     */
    public double AngleToLeft(C2DVector Other) {
        return (Constants.conTWOPI - AngleToRight(Other));
    }

    /**
     * Returns the shortest angle between 2 vectors i.e. the dot product of the norms.
     */
    public double AngleBetween(C2DVector Other) {
        double dDot = this.Dot(Other);
        dDot /= (this.GetLength() * Other.GetLength());
        return Math.acos(dDot);
    }


}