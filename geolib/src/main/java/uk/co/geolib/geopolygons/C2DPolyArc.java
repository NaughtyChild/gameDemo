
package uk.co.geolib.geopolygons;

import uk.co.geolib.geolib.*;

import java.util.ArrayList;

public class C2DPolyArc extends C2DPolyBase {
    /**
     * Constructor.
     */
    public C2DPolyArc() {
    }

    /**
     * Constructor.
     *
     * @param Other The other polygon.
     */
    public C2DPolyArc(C2DPolyBase Other) {
        super(Other);
    }

    /**
     * Constructor.
     */
    public C2DPolyArc(C2DPolyArc Other) {
        super(Other);
    }

    /**
     * Sets the starting point.
     *
     * @param Point The start point.
     */
    public void SetStartPoint(C2DPoint Point) {
        Clear();

        Lines.add(new C2DLine(Point, Point));
    }

    /**
     * Arc to a new point.
     *
     * @param Point          The point to go to.
     * @param dRadius        The radius.
     * @param bCentreOnRight Indicates whether the centre of the arc is to the right of the line.
     * @param bArcOnRight    indicates whether the curve is to the right i.e. anti-clockwise.
     */
    public void LineTo(C2DPoint Point, double dRadius,
                       boolean bCentreOnRight, boolean bArcOnRight) {
        if (Lines.size() == 0)
            return;

        C2DArc pLine = new C2DArc(Lines.get(Lines.size() - 1).GetPointTo(), Point,
                dRadius, bCentreOnRight, bArcOnRight);

        if (Lines.size() == 1 && Lines.get(0) instanceof C2DLine &&
                Lines.get(0).GetPointTo().PointEqualTo(Lines.get(0).GetPointFrom()))  // CR 19-1-09
        {
            Lines.set(0, pLine);
        } else {
            Lines.add(pLine);
        }
    }

    /**
     * Adds a point which is a striaght line from the previous.
     *
     * @param Point The point to go to.
     */
    public void LineTo(C2DPoint Point) {
        if (Lines.size() == 0)
            return;

        C2DLine pLine = new C2DLine(Lines.get(Lines.size() - 1).GetPointTo(), Point);

        if (Lines.size() == 1 && Lines.get(0) instanceof C2DLine &&
                Lines.get(0).GetPointTo().PointEqualTo(Lines.get(0).GetPointFrom()))  // CR 19-1-09
        {
            Lines.set(0, pLine);
        } else {
            Lines.add(pLine);
        }
    }

    /**
     * Close with a curved line back to the first point.
     *
     * @param dRadius        The radius.
     * @param bCentreOnRight Indicates whether the centre of the arc is to the right of the line.
     * @param bArcOnRight    indicates whether the curve is to the right i.e. anti-clockwise.
     */
    public void Close(double dRadius, boolean bCentreOnRight, boolean bArcOnRight) {
        if (Lines.size() == 0)
            return;

        Lines.add(new C2DArc(Lines.get(Lines.size() - 1).GetPointTo(), Lines.get(0).GetPointFrom(),
                dRadius, bCentreOnRight, bArcOnRight));

        MakeLineRects();
        MakeBoundingRect();

    }

    /**
     * Close with a straight line back to the first point.
     */
    public void Close() {
        if (Lines.size() == 0)
            return;

        Lines.add(new C2DLine(Lines.get(Lines.size() - 1).GetPointTo(), Lines.get(0).GetPointFrom()));

        MakeLineRects();
        MakeBoundingRect();

    }

    /**
     * Creates a random shape.
     *
     * @param cBoundary  The boundary.
     * @param nMinPoints The minimum points.
     * @param nMaxPoints The maximum points.
     */
    public boolean CreateRandom(C2DRect cBoundary, int nMinPoints, int nMaxPoints) {
        C2DPolygon Poly = new C2DPolygon();
        if (!Poly.CreateRandom(cBoundary, nMinPoints, nMaxPoints))
            return false;

        CRandomNumber rCenOnRight = new CRandomNumber(0, 1);

        this.Set(Poly);

        for (int i = 0; i < Lines.size(); i++) {
            C2DLineBase pLine = Lines.get(i);

            boolean bCenOnRight = (rCenOnRight.GetInt() > 0);
            double dLength = pLine.GetLength();
            CRandomNumber Radius = new CRandomNumber(dLength, dLength * 3);


            C2DArc pNew = new C2DArc(pLine.GetPointFrom(), pLine.GetPointTo(),
                    Radius.Get(), bCenOnRight, !bCenOnRight);

            if (!this.Crosses(pNew)) {
                Lines.set(i, pNew);
                pNew.GetBoundingRect(LineRects.get(i));
                BoundingRect.ExpandToInclude(LineRects.get(i));
            }
        }

        //     this.MakeLineRects();
        //      this.MakeBoundingRect();

        return true;
    }


    /**
     * Gets the non overlaps i.e. the parts of this that aren't in the other.
     *
     * @param Other    The other shape.
     * @param Polygons The set to recieve the result.
     * @param grid     The degenerate settings.
     */
    public void GetNonOverlaps(C2DPolyArc Other, ArrayList<C2DHoledPolyArc> Polygons,
                               CGrid grid) {
        ArrayList<C2DHoledPolyBase> NewPolys = new ArrayList<C2DHoledPolyBase>();

        super.GetNonOverlaps(Other, NewPolys, grid);

        for (int i = 0; i < NewPolys.size(); i++)
            Polygons.add(new C2DHoledPolyArc(NewPolys.get(i)));
    }

    /**
     * Gets the union of the 2 shapes.
     *
     * @param Other    The other shape.
     * @param Polygons The set to recieve the result.
     * @param grid     The degenerate settings.
     */
    public void GetUnion(C2DPolyArc Other, ArrayList<C2DHoledPolyArc> Polygons,
                         CGrid grid) {
        ArrayList<C2DHoledPolyBase> NewPolys = new ArrayList<C2DHoledPolyBase>();

        super.GetUnion(Other, NewPolys, grid);

        for (int i = 0; i < NewPolys.size(); i++)
            Polygons.add(new C2DHoledPolyArc(NewPolys.get(i)));
    }


    /**
     * Gets the overlaps of the 2 shapes.
     *
     * @param Other    The other shape.
     * @param Polygons The set to recieve the result.
     * @param grid     The degenerate settings.
     */
    public void GetOverlaps(C2DPolyArc Other, ArrayList<C2DHoledPolyArc> Polygons,
                            CGrid grid) {
        ArrayList<C2DHoledPolyBase> NewPolys = new ArrayList<C2DHoledPolyBase>();

        super.GetOverlaps(Other, NewPolys, grid);

        for (int i = 0; i < NewPolys.size(); i++)
            Polygons.add(new C2DHoledPolyArc(NewPolys.get(i)));
    }

    /**
     * Gets the area.
     */
    public double GetArea() {
        double dArea = 0;

        for (int i = 0; i < Lines.size(); i++) {
            C2DPoint pt1 = Lines.get(i).GetPointFrom();
            C2DPoint pt2 = Lines.get(i).GetPointTo();

            dArea += pt1.x * pt2.y - pt2.x * pt1.y;
        }
        dArea = dArea / 2.0;

        for (int i = 0; i < Lines.size(); i++) {
            if (Lines.get(i) instanceof C2DArc) {
                C2DArc Arc = (C2DArc) Lines.get(i);

                C2DSegment Seg = new C2DSegment(Arc);

                dArea += Seg.GetAreaSigned();
            }
        }

        return Math.abs(dArea);

    }

    /**
     * Returns the centroid.
     */
    public C2DPoint GetCentroid() {
        // Find the centroid and area of the straight line polygon.
        C2DPoint Centroid = new C2DPoint(0, 0);
        //   C2DPoint pti = new C2DPoint();
        //   C2DPoint ptii;
        double dArea = 0;

        for (int i = 0; i < Lines.size(); i++) {
            C2DPoint pti = Lines.get(i).GetPointFrom();
            C2DPoint ptii = Lines.get(i).GetPointTo();

            Centroid.x += (pti.x + ptii.x) * (pti.x * ptii.y - ptii.x * pti.y);
            Centroid.y += (pti.y + ptii.y) * (pti.x * ptii.y - ptii.x * pti.y);

            dArea += pti.x * ptii.y - ptii.x * pti.y;
        }
        dArea = dArea / 2.0;

        Centroid.x = Centroid.x / (6.0 * dArea);
        Centroid.y = Centroid.y / (6.0 * dArea);

        ArrayList<Double> dSegAreas = new ArrayList<Double>();
        double dTotalArea = dArea;
        ArrayList<C2DPoint> SegCentroids = new ArrayList<C2DPoint>();

        for (int i = 0; i < Lines.size(); i++) {
            if (Lines.get(i) instanceof C2DArc) {
                C2DSegment Seg = new C2DSegment((C2DArc) Lines.get(i));
                double dSegArea = Seg.GetAreaSigned();
                dTotalArea += dSegArea;
                dSegAreas.add(dSegArea);
                SegCentroids.add(Seg.GetCentroid());
            }
        }

        Centroid.Multiply(dArea);

        for (int i = 0; i < dSegAreas.size(); i++) {
            Centroid.x += SegCentroids.get(i).x * dSegAreas.get(i);
            Centroid.y += SegCentroids.get(i).y * dSegAreas.get(i);
        }

        Centroid.Multiply(1 / dTotalArea);
        return Centroid;

    }

    /// Rotates the polygon to the right around the centroid.
    /// 

    /**
     * Rotates the shape to the right about the centroid.
     *
     * @param dAng The angle to rotate by.
     */
    public void RotateToRight(double dAng) {
        RotateToRight(dAng, GetCentroid());
    }

}