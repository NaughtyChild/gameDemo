
package uk.co.geolib.geopolygons;
import uk.co.geolib.geolib.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class C2DPolygon extends C2DPolyBase {
    /**
     * Constructor.
     */
    public C2DPolygon() {
    }

    /**
     * Constructor.
     *
     * @param Points           The points to create from.
     * @param bReorderIfNeeded True if reordering is required.
     */
    public C2DPolygon(ArrayList<C2DPoint> Points, boolean bReorderIfNeeded) {
        subArea1 = null;
        subArea2 = null;
        Create(Points, bReorderIfNeeded);
    }

    /**
     * Constructor.
     *
     * @param Other The other polygon.
     */
    public C2DPolygon(C2DPolygon Other) {
        Set(Other);
    }

    /**
     * Constructor.
     *
     * @param Other The other polygon.
     */
    public C2DPolygon(C2DPolyBase Other) {
        Clear();
        for (int i = 0; i < Other.Lines.size(); i++) {
            if (Other.Lines.get(i) instanceof C2DLine) {
                Lines.add(new C2DLine((C2DLine) Other.Lines.get(i)));
            } else {
                assert false : "C2DPolygon creation with none straight line";
            }
        }
        MakeLineRects();
        MakeBoundingRect();
    }

    /**
     * Assignment.
     *
     * @param Other The other to set to.
     */
    public void Set(C2DPolyBase Other) {
        Clear();
        for (int i = 0; i < Other.Lines.size(); i++) {
            if (Other.Lines.get(i) instanceof C2DLine) {
                Lines.add(new C2DLine((C2DLine) Other.Lines.get(i)));
            } else {
                //    Debug.Assert(false, "C2DPolygon creation with none straight line");
            }
        }
        MakeLineRects();
        MakeBoundingRect();
    }

    /**
     * Assignment.
     *
     * @param Other The other to set to.
     */
    public void Set(C2DPolygon Other) {
        Clear();
        super.Set(Other);
        if (Other.subArea1 != null) {
            subArea1 = new C2DPolygon();
            subArea1.Set(Other.subArea1);
        }
        if (Other.subArea2 != null) {
            subArea2 = new C2DPolygon();
            subArea2.Set(Other.subArea1);
        }
    }

    /**
     * Creates the polygon with optional reordering of points.
     *
     * @param Points           The points to create from.
     * @param bReorderIfNeeded True if reordering is required.
     */
    public boolean Create(ArrayList<C2DPoint> Points, boolean bReorderIfNeeded) {
        subArea1 = null;
        subArea2 = null;
        if (Points.size() < 3)
            return false;
        MakeLines(Points);
        MakeLineRects();
        if (!IsClockwise())
            ReverseDirection();
        MakeBoundingRect();
        if (bReorderIfNeeded && HasCrossingLines())
            return Reorder();
        return true;
    }

    /**
     * Creates a regular polygon.
     *
     * @param Centre            The centre.
     * @param dDistanceToPoints The distance to each point.
     * @param nNumberSides      True number of sides.
     */
    public boolean CreateRegular(C2DPoint Centre, double dDistanceToPoints, int nNumberSides) {
        Clear();
        if (dDistanceToPoints == 0 || nNumberSides < 3)
            return false;
        double dAngle = Constants.conTWOPI / nNumberSides;
        C2DVector Vector = new C2DVector(0, dDistanceToPoints);
        C2DLine LineToEachPt = new C2DLine(Centre, Vector);
        C2DPointSet Points = new C2DPointSet();
        for (int i = 0; i < nNumberSides; i++) {
            C2DPoint pNewPt = new C2DPoint();
            pNewPt.Set(LineToEachPt.GetPointTo());
            Points.add(pNewPt);
            LineToEachPt.vector.TurnRight(dAngle);
        }
        return Create(Points, false);
    }

    /**
     * Creates a convex hull from another polygon. Uses Graham's algorithm.
     *
     * @param Other The other polygon.
     */
    public boolean CreateConvexHull(C2DPolygon Other) {
        Clear();
        C2DPointSet Points = new C2DPointSet();
        Other.GetPointsCopy(Points);
        C2DPointSet Hull = new C2DPointSet();
        Hull.ExtractConvexHull(Points);
        return Create(Hull, false);
    }

    /**
     * Creates a randon polygon.
     *
     * @param cBoundary  The boundary of the random shape.
     * @param nMinPoints The minimum number of points.
     * @param nMaxPoints The maximum number of points.
     */
    public boolean CreateRandom(C2DRect cBoundary, int nMinPoints, int nMaxPoints) {
        Clear();
        //     Debug.Assert(nMinPoints <= nMaxPoints);
        if (nMinPoints < 3)
            return false;
        if (nMinPoints > nMaxPoints)
            return false;
        CRandomNumber rnNumber = new CRandomNumber(nMinPoints, nMaxPoints);
        int nNumber = rnNumber.GetInt();
        C2DPoint pt = new C2DPoint();
        CRandomNumber rnX = new CRandomNumber(cBoundary.getTopLeft().x, cBoundary.getBottomRight().x);
        CRandomNumber rnY = new CRandomNumber(cBoundary.getBottomRight().y, cBoundary.getTopLeft().y);
        C2DPointSet Points = new C2DPointSet();
        for (int i = 0; i < nNumber; i++) {
            pt.x = rnX.Get();
            pt.y = rnY.Get();
            Points.AddCopy(pt);
        }
        return Create(Points, true);
    }

    /**
     * Mophs this polygon into another by the factor given.
     *
     * @param OtherFrom The other shape from.
     * @param OtherTo   The other shape to.
     * @param dFactor   The factor between the 2 polygons.
     */
    public boolean CreateMorph(C2DPolygon OtherFrom, C2DPolygon OtherTo, double dFactor) {
        int nOtherFromCount = OtherFrom.Lines.size();
        int nOtherToCount = OtherTo.Lines.size();
        if (nOtherToCount < 3 || nOtherFromCount < 3)
            return false;
        if (nOtherFromCount > nOtherToCount) {
            return CreateMorph(OtherTo, OtherFrom, 1 - dFactor);
        } else {
            // Going from poly with less points to poly with more.
            C2DPointSet Points = new C2DPointSet();
            int nOtherFromLeft = OtherFrom.GetLeftMostPoint();
            // Add the OtherFroms points starting from the left most.
            for (int i = 0; i < OtherFrom.Lines.size(); i++) {
                C2DPoint pNewPoint = new C2DPoint();
                pNewPoint.Set(OtherFrom.Lines.get((i + nOtherFromLeft) % OtherFrom.Lines.size()).GetPointFrom());
                Points.add(pNewPoint);
            }
            int nPointsToAdd = nOtherToCount - nOtherFromCount; // we know this is positive.
            int nPointsAdded = 0;
            int nLine = 0;
            // Add points to the list so that it is the same size as OtherTo.
            while (nPointsAdded < nPointsToAdd) {
                C2DLine TempLine = new C2DLine(Points.get(nLine), Points.get(nLine + 1));
                C2DPoint pNewPoint = new C2DPoint();
                pNewPoint = TempLine.GetMidPoint();
                Points.add(nLine + 1, pNewPoint);
                nLine += 2;
                nPointsAdded++;
                if (nLine > Points.size() - 2)
                    nLine = 0;
            }
            int nOtherToLeft = OtherTo.GetLeftMostPoint();
            // Debug.Assert(Points.size() == nOtherToCount);
            for (int i = 0; i < nOtherToCount; i++) {
                C2DVector vMove = new C2DVector(Points.get(i), OtherTo.Lines.get((nOtherToLeft + i) % OtherTo.Lines.size()).GetPointFrom());
                vMove.Multiply(dFactor);
                Points.get(i).Move(vMove);
            }
            return Create(Points, false);
        }
    }

    /**
     * Creates convex sub areas of the current polygon. These can then be extracted.
     * This function is also useful when obtaining minimum translation vectors.
     */
    public boolean CreateConvexSubAreas() {
        subArea1 = null;
        subArea2 = null;
        int nLineCount = Lines.size();
        if (nLineCount < 4)
            return true;
        boolean bInflection = false;
        for (int nStart = 0; nStart < nLineCount; nStart++) {
            if (IsPointInflected(nStart)) {
                bInflection = true;
                int nEnd = nStart + 2;
                boolean bContinue = true;
                while (bContinue) {
                    if (C2DTriangle.IsClockwise(GetPoint(nEnd - 2), GetPoint(nEnd - 1), GetPoint(nEnd))
                            && C2DTriangle.IsClockwise(GetPoint(nEnd - 1), GetPoint(nEnd), GetPoint(nStart))
                            && C2DTriangle.IsClockwise(GetPoint(nEnd), GetPoint(nStart), GetPoint(nStart + 1))
                            && CanPointsBeJoined(nStart, nEnd)) {
                        nEnd++;
                    } else {
                        nEnd--;
                        bContinue = false;
                    }
                }
                if (nEnd >= nStart + 2) {
                    subArea1 = new C2DPolygon();
                    subArea2 = new C2DPolygon();
                    boolean bRes = CreateSubAreas(nStart, nEnd, subArea1, subArea2);
                    bRes &= subArea1.CreateConvexSubAreas();
                    bRes &= subArea2.CreateConvexSubAreas();
                    return bRes;
                }
            }
        }
        if (!bInflection)
            return true;
        for (int nStart = 2 * nLineCount - 1; nStart >= nLineCount; nStart--) {
            if (IsPointInflected(nStart)) {
                bInflection = true;
                int nEnd = nStart - 2;
                boolean bContinue = true;
                while (bContinue) {
                    if (!C2DTriangle.IsClockwise(GetPoint(nEnd + 2), GetPoint(nEnd + 1), GetPoint(nEnd))
                            && !C2DTriangle.IsClockwise(GetPoint(nEnd + 1), GetPoint(nEnd), GetPoint(nStart))
                            && !C2DTriangle.IsClockwise(GetPoint(nEnd), GetPoint(nStart), GetPoint(nStart - 1))
                            && CanPointsBeJoined(nStart, nEnd)) {
                        nEnd--;
                    } else {
                        nEnd++;
                        bContinue = false;
                    }
                }
                if (nEnd <= nStart - 2) {
                    subArea1 = new C2DPolygon();
                    subArea2 = new C2DPolygon();
                    boolean bRes = CreateSubAreas(nStart, nEnd, subArea1, subArea2);
                    bRes &= subArea1.CreateConvexSubAreas();
                    bRes &= subArea2.CreateConvexSubAreas();
                    return bRes;
                }
            }
        }
        //    Debug.Assert(false);
        return false;
    }

    /**
     * Removes the convex sub areas.
     */
    public void ClearConvexSubAreas() {
        subArea1 = null;
        subArea2 = null;
    }

    /**
     * True if the polygon is convex.
     */
    public boolean IsConvex() {
        if (Lines.size() < 4)
            return true;
        Integer nTemp = 0;
        return !FindFirstInflection(nTemp);
    }

    /**
     * Clears all.
     */
    public void Clear() {
        this.ClearConvexSubAreas();
        super.Clear();
    }

    /**
     * Rotates the polygon to the right around the centroid.
     *
     * @param dAng The angle to rotate by.
     */
    public void RotateToRight(double dAng) {
        RotateToRight(dAng, GetCentroid());
    }

    /**
     * Grows the polygon from the centre.
     *
     * @param dFactor The factor to grow by.
     */
    public void Grow(double dFactor) {
        Grow(dFactor, GetCentroid());
    }

    /**
     * Moves this point by the vector given.
     *
     * @param vector The vector.
     */
    public void Move(C2DVector vector) {
        super.Move(vector);
        if (subArea1 != null)
            subArea1.Move(vector);
        if (subArea2 != null)
            subArea2.Move(vector);
    }

    /**
     * Rotates this to the right about the origin provided.
     *
     * @param dAng   The angle in radians through which to rotate.
     * @param Origin The origin about which to rotate.
     */
    public void RotateToRight(double dAng, C2DPoint Origin) {
        super.RotateToRight(dAng, Origin);
        if (subArea1 != null)
            subArea1.RotateToRight(dAng, Origin);
        if (subArea2 != null)
            subArea2.RotateToRight(dAng, Origin);
    }

    /**
     * Grows the polygon around the origin.
     *
     * @param dFactor The factor to grow by.
     * @param Origin  The origin about which to grow.
     */
    public void Grow(double dFactor, C2DPoint Origin) {
        super.Grow(dFactor, Origin);
        if (subArea1 != null)
            subArea1.Grow(dFactor, Origin);
        if (subArea2 != null)
            subArea2.Grow(dFactor, Origin);
    }

    /**
     * Reflects the area about the point.
     *
     * @param point The point to reflect through.
     */
    public void Reflect(C2DPoint point) {
        super.Reflect(point);
        if (subArea1 != null)
            subArea1.Reflect(point);
        if (subArea2 != null)
            subArea2.Reflect(point);
    }

    /**
     * Reflects throught the line provided.
     *
     * @param Line The line to reflect through.
     */
    public void Reflect(C2DLine Line) {
        super.Reflect(Line);
        if (subArea1 != null)
            subArea1.Reflect(Line);
        if (subArea2 != null)
            subArea2.Reflect(Line);
    }

    /**
     * True if there are repeated points.
     */
    boolean HasRepeatedPoints() {
        for (int i = 0; i < Lines.size(); i++) {
            for (int r = i + 1; r < Lines.size(); r++) {
                if (Lines.get(i).GetPointFrom().PointEqualTo(Lines.get(r).GetPointFrom()))
                    return true;
            }
        }
        return false;
    }

    /**
     * True if clockwise.
     */
    public boolean IsClockwise() {
        assert Lines.size() > 2;
        return (GetAreaSigned() < 0);
    }

    /**
     * Returns the convex sub areas if created.
     *
     * @param SubAreas Ouput. The sub areas.
     */
    public void GetConvexSubAreas(ArrayList<C2DPolygon> SubAreas) {
        if (subArea1 != null && subArea2 != null) {
            subArea1.GetConvexSubAreas(SubAreas);
            subArea2.GetConvexSubAreas(SubAreas);
        } else {
            SubAreas.add(this);
        }
    }

    /**
     * True if this overlaps another and returns the translation vector required to move
     * this apart. Exact if this is convex, approximate if concave. Better approximation
     * if convex sub areas have been created.
     *
     * @param Other                    The other polygon.
     * @param MinimumTranslationVector Ouput. The vector to move this by to move it away from the other.
     */
    public boolean Overlaps(C2DPolygon Other, C2DVector MinimumTranslationVector) {
        if (Lines.size() < 2 || Other.Lines.size() < 2)
            return false;
        if (!BoundingRect.Overlaps(Other.BoundingRect))
            return false;
        if (subArea1 != null && subArea2 != null) {
            C2DVector v1 = new C2DVector();
            C2DVector v2 = new C2DVector();
            boolean b1 = subArea1.Overlaps(Other, v1);
            boolean b2 = subArea2.Overlaps(Other, v2);
            if (b1 && b2)
                MinimumTranslationVector.Set(C2DVector.Add(v1, v2));
            else if (b1)
                MinimumTranslationVector.Set(v1);
            else if (b2)
                MinimumTranslationVector.Set(v2);
            return b1 || b2;
        } else if (Other.subArea1 != null && Other.subArea2 != null) {
            boolean bRes = Other.Overlaps(this, MinimumTranslationVector);
            if (bRes)
                MinimumTranslationVector.Reverse();
            return bRes;
        } else {
            CInterval ThisProj = new CInterval();
            CInterval OtherProj = new CInterval();
            C2DLine ProjLine = new C2DLine();
            boolean bVecFound = false;
            for (int i = 0; i < Lines.size() + Other.Lines.size(); i++) {
                if (i < Lines.size())
                    ProjLine.Set(GetPoint(i), GetPoint(i + 1));
                else
                    ProjLine.Set(Other.GetPoint(i - Lines.size()),
                            Other.GetPoint(i - Lines.size() + 1));
                ProjLine.vector.TurnRight();
                ProjLine.vector.MakeUnit();
                this.Project(ProjLine.vector, ThisProj);
                Other.Project(ProjLine.vector, OtherProj);
                if (ThisProj.dMin < OtherProj.dMax && ThisProj.dMax > OtherProj.dMax) {
                    if (!bVecFound ||
                            (OtherProj.dMax - ThisProj.dMin) < MinimumTranslationVector.GetLength()) {
                        MinimumTranslationVector.Set(ProjLine.vector);
                        MinimumTranslationVector.SetLength(OtherProj.dMax - ThisProj.dMin);
                        MinimumTranslationVector.Multiply(1.001);
                        bVecFound = true;
                    }
                } else if (OtherProj.dMin < ThisProj.dMax && OtherProj.dMax > ThisProj.dMax) {
                    if (!bVecFound ||
                            (ThisProj.dMax - OtherProj.dMin) < MinimumTranslationVector.GetLength()) {
                        MinimumTranslationVector.Set(ProjLine.vector);
                        MinimumTranslationVector.SetLength(ThisProj.dMax - OtherProj.dMin);
                        MinimumTranslationVector.Reverse();
                        MinimumTranslationVector.Multiply(1.001);
                        bVecFound = true;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Moves this away from the other by the minimal amount
     *
     * @param Other The other polygon.
     */
    public void Avoid(C2DPolygon Other) {
        C2DVector vTrans = new C2DVector();
        if (this.Overlaps(Other, vTrans))
            this.Move(vTrans);
    }

    /**
     * Returns the centroid.
     */
    public C2DPoint GetCentroid() {
        C2DPoint Centroid = new C2DPoint(0, 0);
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
        return Centroid;
    }

    /**
     * Returns the area.
     */
    public double GetArea() {
        return Math.abs(GetAreaSigned());
    }

    /**
     * Returns the area signed (-ve if clockwise).
     */
    public double GetAreaSigned() {
        double dArea = 0;
        for (int i = 0; i < Lines.size(); i++) {
            C2DPoint pt1 = Lines.get(i).GetPointFrom();
            C2DPoint pt2 = Lines.get(i).GetPointTo();
            dArea += pt1.x * pt2.y - pt2.x * pt1.y;
        }
        dArea = dArea / 2.0;
        return dArea;
    }

    /**
     * Gets the point at the specified index. Index is cyclic.
     *
     * @param nPointIndex The index of the point.
     */
    C2DPoint GetPoint(int nPointIndex) {
        if (Lines.size() == 0)
            return new C2DPoint();
        return Lines.get(nPointIndex % Lines.size()).GetPointFrom();
    }

    /**
     * Copies the points into the set object provided.
     *
     * @param PointCopy The point set to recieve the result.
     */
    public void GetPointsCopy(ArrayList<C2DPoint> PointCopy) {
        for (int i = 0; i < Lines.size(); i++) {
            PointCopy.add(Lines.get(i).GetPointFrom());
        }
    }

    /**
     * Returns the left most point.
     */
    public int GetLeftMostPoint() {
        if (Lines.size() < 2)
            return 0;
        int nRes = 0;
        for (int i = 1; i < Lines.size(); i++) {
            if (Lines.get(i).GetPointFrom().x < Lines.get(nRes).GetPointFrom().x)
                nRes = i;
        }
        return nRes;
    }

    /**
     * Smooths the polygon.
     *
     * @param dMinAngleDegrees The minimum angle between points. Default is 144 degrees.
     * @param dCropFactor      The factor to crop "sharp" lines by. Default is 0.8.
     */
    public void Smooth(double dMinAngleDegrees, double dCropFactor) {
        double dMinAngle = dMinAngleDegrees * Constants.conRadiansPerDegree;
        int i = 0;
        if (Lines.size() < 3)
            return;
        //  Debug.Assert(IsClockwise());
        int nCount = Lines.size();
        int nIt = 0;
        while (nIt < nCount) {
            C2DLineBase LineBase1 = Lines.get(i % Lines.size());
            C2DLineBase LineBase2 = Lines.get((i + 1) % Lines.size());
            C2DLine Line1 = new C2DLine(LineBase1.GetPointFrom(), LineBase1.GetPointTo());
            C2DLine Line2 = new C2DLine(LineBase2.GetPointFrom(), LineBase2.GetPointTo());
            C2DVector Vec = new C2DVector(Line1.vector);
            Vec.Reverse();
            double dAng = Line2.vector.AngleToRight(Vec);
            if (dAng < dMinAngle || dAng > (Constants.conTWOPI - dMinAngle)) {
                SetPoint(Line1.GetPointOn(dCropFactor), (i + 1));
                InsertPoint(i + 2, Line2.GetPointOn(1 - dCropFactor));
                nCount++;
                i += 2;
                nIt = 0;
            } else {
                i++;
                nIt++;
            }
            if (i >= nCount)
                i = 0;
        }
        MakeBoundingRect();
        if (subArea1 != null && subArea2 != null)
            CreateConvexSubAreas();
    }

    /**
     * Smooths the polygon using default smoothing i.e. 144 degrees and 0.8 crop factor.
     */
    public void Smooth() {
        Smooth(Constants.conPI * 0.8 * Constants.conDegreesPerRadian, 0.8);
    }

    /**
     * Get the minimum bounding circle.
     *
     * @param Circle Output. The bounding circle.
     */
    public void GetBoundingCircle(C2DCircle Circle) {
        C2DPointSet Points = new C2DPointSet();
        GetPointsCopy(Points);
        Points.GetBoundingCircle(Circle);
    }

    /**
     * Finds the index of the first inflected point if there is one.
     *
     * @param nFirstInflection Output. Inflected point.
     */
    private boolean FindFirstInflection(Integer nFirstInflection) {
        for (int i = 0; i < Lines.size(); i++) {
            if (IsPointInflected(i)) {
                nFirstInflection = i;
                return true;
            }
        }
        return false;
    }

    /**
     * True if the point is inflected.
     *
     * @param nIndex The point index.
     */
    public boolean IsPointInflected(int nIndex) {
        int usBefore;
        if (nIndex == 0)
            usBefore = Lines.size() - 1;
        else
            usBefore = nIndex - 1;
        C2DLine TestLine = new C2DLine(GetPoint(usBefore), GetPoint(nIndex));
        //    Debug.Assert(IsClockwise());
        return !TestLine.IsOnRight(GetPoint(nIndex + 1));
    }

    /**
     * True if the points can be joined with no resulting crossing lines.
     *
     * @param nStart The first point index.
     * @param nEnd   The second point index.
     */
    public boolean CanPointsBeJoined(int nStart, int nEnd) {
        int usBefore = 0;
        if (nStart == 0)
            usBefore = Lines.size() - 1;
        else
            usBefore = nStart - 1;
        C2DVector VecBefore = new C2DVector(GetPoint(nStart), GetPoint(usBefore));
        C2DVector VecAfter = new C2DVector(GetPoint(nStart), GetPoint(nStart + 1));
        C2DLine TestLine = new C2DLine(GetPoint(nStart), GetPoint(nEnd));
        //    Debug.Assert(IsClockwise());
        if (VecAfter.AngleToRight(TestLine.vector) <
                VecAfter.AngleToRight(VecBefore)) {
            TestLine.GrowFromCentre(0.99999);
            if (!this.Crosses(TestLine))
                return true;
        }
        return false;
    }

    /**
     * Creates sub areas given 2 point indexes and pointers to the new areas.
     *
     * @param nPt1      The first point index.
     * @param nPt2      The second point index.
     * @param pNewArea1 The first subarea.
     * @param pNewArea2 The second subarea.
     */
    private boolean CreateSubAreas(int nPt1, int nPt2, C2DPolygon pNewArea1, C2DPolygon pNewArea2) {
        while (nPt2 < nPt1)
            nPt2 += Lines.size();
        C2DPointSet Points1 = new C2DPointSet();
        for (int i = nPt1; i <= nPt2; i++) {
            Points1.add(GetPoint(i));
        }
        boolean bRes1 = pNewArea1.Create(Points1, false);
        while (nPt1 < nPt2)
            nPt1 += Lines.size();
        C2DPointSet Points2 = new C2DPointSet();
        for (int j = nPt2; j <= nPt1; j++) {
            Points2.add(GetPoint(j));
        }
        boolean bRes2 = pNewArea2.Create(Points2, false);
        return bRes1 && bRes2;
    }

    /**
     * Reorders the points to minimise perimeter.
     */
    public boolean Reorder() {
        if (Lines.size() < 4)
            return true;
        // Get a copy of the points.
        C2DPointSet Points = new C2DPointSet();
        GetPointsCopy(Points);
        // Make a convex hull from them.
        C2DPointSet Hull = new C2DPointSet();
        Hull.ExtractConvexHull(Points);
        // Close the hull.
        Hull.AddCopy(Hull.get(0));
        // Get the bounding rect for the hull and sort the rest by the distance from it.
        C2DRect Rect = new C2DRect();
        Hull.GetBoundingRect(Rect);
        Points.SortByDistance(Rect.GetCentre());
        Collections.reverse(Points);
        // Set up the travelling saleman and give him the route i.e. the now closed hull.
        CTravellingSalesman TS = new CTravellingSalesman();
        TS.SetPointsDirect(Hull);
        // Insert the rest starting with the closest (hopefully).
        while (Points.size() > 0)
            TS.InsertOptimally(Points.ExtractAt(Points.size() - 1));
        // Refine the TS.
        TS.Refine();
        // Get the points back
        TS.ExtractPoints(Points);
        // Remove the closure.
        Points.remove(Points.size() - 1);
        // Make the lines again.
        MakeLines(Points);
        // Make the rectangles again.
        MakeLineRects();
        // Reverse direction if needed.
        if (!IsClockwise())
            ReverseDirection();
        // Remake the bounding rectangle.
        MakeBoundingRect();
        // Eliminate crossing lines.
        if (!EliminateCrossingLines())
            return false;
        return true;
    }

    /**
     * Reorders to eliminate crossing lines.
     */
    private boolean EliminateCrossingLines() {
        boolean bRepeat = true;
        int nIt = 0;
        ArrayList<C2DPoint> Temp = new ArrayList<C2DPoint>();
        while (bRepeat && nIt < 30) {
            nIt++;
            bRepeat = false;
            for (int nCross1 = 0; nCross1 < Lines.size(); nCross1++) {
                for (int nCross2 = nCross1 + 2; nCross2 < Lines.size(); nCross2++) {
                    if ((nCross1 == 0) && (nCross2 == (Lines.size() - 1)))
                        continue;
                    if (this.LineRects.get(nCross1).Overlaps(LineRects.get(nCross2)) &&
                            Lines.get(nCross1).Crosses(Lines.get(nCross2), Temp)) {
                        int nSwapStart = nCross1 + 1; // end of first line
                        int nSwapEnd = nCross2;
                        //    Debug.Assert(nSwapEnd > nSwapStart);
                        int nHalfway = (nSwapEnd - nSwapStart) / 2;
                        for (int nPoint = 0; nPoint <= nHalfway; nPoint++) {
                            SwapPositions(nSwapStart + nPoint, nSwapEnd - nPoint);
                        }
                        //      bReordered = true;
                        bRepeat = true;
                    }
                }
            }
        }
        MakeBoundingRect();
        return (!bRepeat);
    }

    /**
     * Swaps the two points.
     *
     * @param Pos1 The first point index.
     * @param Pos2 The second point index.
     */
    private void SwapPositions(int Pos1, int Pos2) {
        C2DPoint temp = GetPoint(Pos1);
        SetPoint(GetPoint(Pos2), Pos1);
        SetPoint(temp, Pos2);
    }

    /**
     * Sets the point to the point provided.
     *
     * @param Point       The point to be set to.
     * @param nPointIndex The point index.
     */
    private void SetPoint(C2DPoint Point, int nPointIndex) {
        if (nPointIndex >= Lines.size())
            nPointIndex -= Lines.size();
        int nPointIndexBefore;
        if (nPointIndex == 0)
            nPointIndexBefore = Lines.size() - 1;
        else
            nPointIndexBefore = nPointIndex - 1;
        int nPointIndexAfter;
        if (nPointIndex == Lines.size() - 1)
            nPointIndexAfter = 0;
        else
            nPointIndexAfter = nPointIndex + 1;
        C2DLineBase pLineBase = Lines.get(nPointIndex);
        if (pLineBase instanceof C2DLine) {
            C2DLine pLine = (C2DLine) pLineBase;
            pLine.Set(Point, Lines.get(nPointIndexAfter).GetPointFrom());
            pLine.GetBoundingRect(LineRects.get(nPointIndex));
            C2DLineBase pLineBaseBefore = Lines.get(nPointIndexBefore);
            if (pLineBaseBefore instanceof C2DLine) {
                C2DLine pLineBefore = (C2DLine) pLineBaseBefore;
                pLineBefore.SetPointTo(Point);
                pLineBefore.GetBoundingRect(LineRects.get(nPointIndexBefore));
            }
        }
    }

    /**
     * Inserts a point.
     *
     * @param nPointIndex The point index.
     * @param Point       The point to be set to.
     */
    private void InsertPoint(int nPointIndex, C2DPoint Point) {
        nPointIndex = nPointIndex % Lines.size();
        int nPointIndexBefore = 0;
        if (nPointIndex == 0)
            nPointIndexBefore = Lines.size() - 1;
        else
            nPointIndexBefore = nPointIndex - 1;
        C2DLine pInsert = new C2DLine(Point, Lines.get(nPointIndex).GetPointFrom());
        C2DRect pInsertRect = new C2DRect();
        pInsert.GetBoundingRect(pInsertRect);
        C2DLineBase pLineBase = Lines.get(nPointIndexBefore);
        if (pLineBase instanceof C2DLine) {
            C2DLine pLineBefore = (C2DLine) pLineBase;
            pLineBefore.SetPointTo(Point);
            pLineBefore.GetBoundingRect(LineRects.get(nPointIndexBefore));
            Lines.add(nPointIndex, pInsert);
            LineRects.add(nPointIndex, pInsertRect);
        }
    }

    /**
     * Simple buffer around the polygon at a fixed amount. No attemp to ensure validity
     * as intended for small buffer amounts.
     *
     * @param dBuffer The buffer amount.
     */
    public void SimpleBuffer(double dBuffer) {
        ClearConvexSubAreas();
        boolean bClockwise = this.IsClockwise();
        for (int i = 0; i < Lines.size(); i++) {
            if (Lines.get(i) instanceof C2DLine) {
                C2DLine Line = (C2DLine) Lines.get(i);
                C2DVector v = new C2DVector(Line.vector);
                if (bClockwise)
                    v.TurnLeft();
                else
                    v.TurnRight();
                v.SetLength(dBuffer);
                Line.Move(v);
            } else {
                //    Debug.Assert(false);
                return;
            }
        }
        for (int i = 1; i < Lines.size(); i++) {
            C2DLine Line1 = (C2DLine) Lines.get(i - 1);
            C2DLine Line2 = (C2DLine) Lines.get(i);
            Line1.Join(Line2);
        }
        ((C2DLine) Lines.get(Lines.size() - 1)).Join((C2DLine) Lines.get(0));
        MakeLineRects();
        MakeBoundingRect();
    }

    /**
     * Removes null areas, will return true if the shape is no longer valid.
     *
     * @param dTolerance <returns>True if the polygon is no longer valid i.e. completely null.</returns>
     */
    public boolean RemoveNullAreas(double dTolerance) {
        ClearConvexSubAreas();
        int i = 0;
        boolean bChanged = false;
        while (i < Lines.size() && Lines.size() > 2) {
            int nNext = (i + 1) % Lines.size();
            double dArea = C2DTriangle.GetAreaSigned(Lines.get(i).GetPointFrom(),
                    Lines.get(i).GetPointTo(),
                    Lines.get(nNext).GetPointTo());
            if (Math.abs(dArea) < dTolerance) {
                if (Lines.get(i) instanceof C2DLine) {
                    ((C2DLine) Lines.get(i)).SetPointTo(Lines.get((i + 1) % Lines.size()).GetPointTo());
                    Lines.remove(nNext);
                    bChanged = true;
                } else {
                    //    Debug.Assert(false);
                    return true;
                }
            } else {
                i++;
            }
        }
        if (Lines.size() <= 2) {
            Clear();
            return true;
        }
        if (bChanged) {
            MakeLineRects();
            MakeBoundingRect();
        }
        return false;
    }

    /**
     * Make the lines related to the points for the base class to work on.
     *
     * @param Points The point set.
     */
    public void MakeLines(ArrayList<C2DPoint> Points) {
        Lines.clear();
        for (int i = 0; i < Points.size(); i++) {
            int nNext = i + 1;
            if (nNext == Points.size())
                nNext = 0;
            Lines.add(new C2DLine(Points.get(i), Points.get(nNext)));
        }
    }

    /**
     * Gets the non overlaps i.e. the parts of this that aren't in the other.
     *
     * @param Other    The other shape.
     * @param Polygons The set to recieve the result.
     * @param grid     The degenerate settings.
     */
    public void GetNonOverlaps(C2DPolygon Other, ArrayList<C2DHoledPolygon> Polygons,
                               CGrid grid) {
        ArrayList<C2DHoledPolyBase> NewPolys = new ArrayList<C2DHoledPolyBase>();
        super.GetNonOverlaps(Other, NewPolys, grid);
        for (int i = 0; i < NewPolys.size(); i++)
            Polygons.add(new C2DHoledPolygon(NewPolys.get(i)));
    }

    /**
     * Gets the union of the 2 shapes.
     *
     * @param Other    The other shape.
     * @param Polygons The set to recieve the result.
     * @param grid     The degenerate settings.
     */
    public void GetUnion(C2DPolygon Other, ArrayList<C2DHoledPolygon> Polygons,
                         CGrid grid) {
        ArrayList<C2DHoledPolyBase> NewPolys = new ArrayList<C2DHoledPolyBase>();
        super.GetUnion(Other, NewPolys, grid);
        for (int i = 0; i < NewPolys.size(); i++)
            Polygons.add(new C2DHoledPolygon(NewPolys.get(i)));
    }

    /**
     * Gets the overlaps of the 2 shapes.
     *
     * @param Other    The other shape.
     * @param Polygons The set to recieve the result.
     * @param grid     The degenerate settings.
     */
    public void GetOverlaps(C2DPolygon Other, ArrayList<C2DHoledPolygon> Polygons,
                            CGrid grid) {
        ArrayList<C2DHoledPolyBase> NewPolys = new ArrayList<C2DHoledPolyBase>();
        super.GetOverlaps(Other, NewPolys, grid);
        for (int i = 0; i < NewPolys.size(); i++)
            Polygons.add(new C2DHoledPolygon(NewPolys.get(i)));
    }

    /**
     * Class to hold a line reference and bounding rect for OverlapsAbove algorithm.
     */
    public class CLineBaseRect {
        /**
         * Line reference
         */
        public C2DLine Line = null;
        /**
         * Line bounding rect
         */
        public C2DRect Rect = null;
        /**
         * Set flag
         */
        public boolean bSetFlag = true;
    }

    ;

    /**
     * True if this polygon is above the other. Returns the vertical distance
     * and the points on both polygons.
     *
     * @param Other
     * @param dVerticalDistance
     * @param ptOnThis
     * @param ptOnOther         <returns></returns>
     */
    public boolean OverlapsAbove(C2DPolygon Other, Double dVerticalDistance,
                                 C2DPoint ptOnThis, C2DPoint ptOnOther) {
        C2DRect OtherBoundingRect = Other.BoundingRect;
        if (!BoundingRect.OverlapsAbove(OtherBoundingRect))
            return false;
        int nLineCount = Lines.size();
        if (nLineCount != LineRects.size())
            return false;
        int nOtherLineCount = Other.Lines.size();
        if (nOtherLineCount != Other.LineRects.size()) {
            return false;
        }
        ArrayList<CLineBaseRect> LineSet = new ArrayList<CLineBaseRect>();
        for (int i = 0; i < nLineCount; i++) {
            if (LineRects.get(i).OverlapsAbove(OtherBoundingRect)) {
                CLineBaseRect pNewLine = new CLineBaseRect();
                pNewLine.Line = (C2DLine) Lines.get(i);
                pNewLine.Rect = LineRects.get(i);
                pNewLine.bSetFlag = true;
                LineSet.add(pNewLine);
            }
        }
        for (int i = 0; i < nOtherLineCount; i++) {
            if (Other.LineRects.get(i).OverlapsBelow(this.BoundingRect)) {
                CLineBaseRect pNewLine = new CLineBaseRect();
                pNewLine.Line = (C2DLine) Other.Lines.get(i);
                pNewLine.Rect = Other.LineRects.get(i);
                pNewLine.bSetFlag = false;
                LineSet.add(pNewLine);
            }
        }
        CLineBaseRectLeftToRight Comparitor = new CLineBaseRectLeftToRight();
        Collections.sort(LineSet, Comparitor);
        boolean bResult = false;
        int j = 0;
        while (j < LineSet.size()) {
            int r = j + 1;
            double dXLimit = LineSet.get(j).Rect.GetRight();
            while (r < LineSet.size() &&
                    LineSet.get(r).Rect.GetLeft() < dXLimit) {
                Double dDistTemp = 0.0;
                C2DPoint ptOnThisTemp = new C2DPoint();
                C2DPoint ptOnOtherTemp = new C2DPoint();
                boolean bOverlap = false;
                if (LineSet.get(j).bSetFlag) {
                    if (!LineSet.get(r).bSetFlag &&
                            LineSet.get(j).Line.OverlapsAbove(LineSet.get(r).Line, dDistTemp,
                                    ptOnThisTemp, ptOnOtherTemp)) {
                        bOverlap = true;
                    }
                } else {
                    if (LineSet.get(r).bSetFlag &&
                            LineSet.get(r).Line.OverlapsAbove(LineSet.get(j).Line, dDistTemp,
                                    ptOnThisTemp, ptOnOtherTemp)) {
                        bOverlap = true;
                    }
                }
                if (bOverlap && (dDistTemp < dVerticalDistance || !bResult)) {
                    bResult = true;
                    dVerticalDistance = dDistTemp;
                    ptOnThis.Set(ptOnThisTemp);
                    ptOnOther.Set(ptOnOtherTemp);
                    if (dDistTemp == 0) {
                        j += Lines.size(); // escape;
                        r += Lines.size(); // escape;
                    }
                }
                r++;
            }
            j++;
        }
        return bResult;
    }

    /**
     * True if this polygon is above or below the other. Returns the vertical distance
     * and the points on both polygons.
     *
     * @param Other
     * @param dVerticalDistance
     * @param ptOnThis
     * @param ptOnOther         <returns></returns>
     */
    public boolean OverlapsVertically(C2DPolygon Other, Double dVerticalDistance,
                                      C2DPoint ptOnThis, C2DPoint ptOnOther) {
        C2DRect OtherBoundingRect = Other.BoundingRect;
        if (!BoundingRect.OverlapsVertically(OtherBoundingRect))
            return false;
        int nLineCount = Lines.size();
        if (nLineCount != LineRects.size())
            return false;
        int nOtherLineCount = Other.Lines.size();
        if (nOtherLineCount != Other.LineRects.size())
            return false;
        ArrayList<CLineBaseRect> LineSet = new ArrayList<CLineBaseRect>();
        for (int i = 0; i < nLineCount; i++) {
            if (LineRects.get(i).OverlapsVertically(OtherBoundingRect)) {
                CLineBaseRect pNewLine = new CLineBaseRect();
                pNewLine.Line = (C2DLine) Lines.get(i);
                pNewLine.Rect = LineRects.get(i);
                pNewLine.bSetFlag = true;
                LineSet.add(pNewLine);
            }
        }
        for (int i = 0; i < nOtherLineCount; i++) {
            if (Other.LineRects.get(i).OverlapsVertically(this.BoundingRect)) {
                CLineBaseRect pNewLine = new CLineBaseRect();
                pNewLine.Line = (C2DLine) Other.Lines.get(i);
                pNewLine.Rect = Other.LineRects.get(i);
                pNewLine.bSetFlag = false;
                LineSet.add(pNewLine);
            }
        }
        CLineBaseRectLeftToRight Comparitor = new CLineBaseRectLeftToRight();
        Collections.sort(LineSet, Comparitor);
        boolean bResult = false;
        int j = 0;
        while (j < LineSet.size()) {
            int r = j + 1;
            double dXLimit = LineSet.get(j).Rect.GetRight();
            while (r < LineSet.size() &&
                    LineSet.get(r).Rect.GetLeft() < dXLimit) {
                Double dDistTemp = 0.0;
                C2DPoint ptOnThisTemp = new C2DPoint();
                C2DPoint ptOnOtherTemp = new C2DPoint();
                boolean bOverlap = false;
                if (LineSet.get(j).bSetFlag) {
                    if (!LineSet.get(r).bSetFlag &&
                            LineSet.get(j).Line.OverlapsVertically(LineSet.get(r).Line, dDistTemp,
                                    ptOnThisTemp, ptOnOtherTemp)) {
                        bOverlap = true;
                    }
                } else {
                    if (LineSet.get(r).bSetFlag &&
                            LineSet.get(r).Line.OverlapsVertically(LineSet.get(j).Line, dDistTemp,
                                    ptOnThisTemp, ptOnOtherTemp)) {
                        bOverlap = true;
                    }
                }
                if (bOverlap && (dDistTemp < dVerticalDistance || !bResult)) {
                    bResult = true;
                    dVerticalDistance = dDistTemp;
                    ptOnThis.Set(ptOnThisTemp);
                    ptOnOther.Set(ptOnOtherTemp);
                    if (dDistTemp == 0) {
                        j += Lines.size(); // escape;
                        r += Lines.size(); // escape;
                    }
                }
                r++;
            }
            j++;
        }
        return bResult;
    }

    /**
     * Returns the minimum bounding box that is not necassarily horiztonal i.e.
     * the box can be at an angle and is defined by a line and the width to the right.
     *
     * @param Line
     * @param dWidthToRight
     */
    public void GetMinBoundingBox(C2DLine Line, Double dWidthToRight) {
        int nCount = Lines.size();
        if (nCount == 0)
            return;
        if (!IsConvex()) {
            C2DPolygon CH = new C2DPolygon();
            CH.CreateConvexHull(this);
            CH.GetMinBoundingBox(Line, dWidthToRight);
            return;
        }
        int nP1 = 0;//index of vertex with minimum y-coordinate;
        int nP2 = 0;//index of vertex with maximum y-coordinate;
        int nP3 = 0;//index of vertex with minimum x-coordinate;
        int nP4 = 0;//index of vertex with maximum x-coordinate;
        double dMinY = Lines.get(0).GetPointFrom().y;
        double dMaxY = dMinY;
        double dMinX = Lines.get(0).GetPointFrom().x;
        double dMaxX = dMinX;
        for (int i = 1; i < Lines.size(); i++) {
            C2DPoint pt = Lines.get(i).GetPointFrom();
            if (pt.y < dMinY) {
                dMinY = pt.y;
                nP1 = i;
            } else if (pt.y > dMaxY) {
                dMaxY = pt.y;
                nP2 = i;
            }
            if (pt.x < dMinX) {
                dMinX = pt.x;
                nP3 = i;
            } else if (pt.x > dMaxX) {
                dMaxX = pt.x;
                nP4 = i;
            }
        }
        double dRotatedAngle = 0;
        double dMinArea = 1.7E+308;
        // 222222
        // 3	4
        // 3    4
        // 3    4
        // 111111
        C2DLine Caliper1 = new C2DLine(Lines.get(nP1).GetPointFrom(), new C2DVector(-1, 0));    // Caliper 1 points along the negative x-axis
        C2DLine Caliper2 = new C2DLine(Lines.get(nP2).GetPointFrom(), new C2DVector(1, 0));   // Caliper 2 points along the positive x-axis
        C2DLine Caliper3 = new C2DLine(Lines.get(nP3).GetPointFrom(), new C2DVector(0, 1));    // Caliper 3 points along the positive y-axis
        C2DLine Caliper4 = new C2DLine(Lines.get(nP4).GetPointFrom(), new C2DVector(0, -1));   // Caliper 4 points along the negative y-axis
        while (dRotatedAngle < Constants.conPI) {
            int nMod = Lines.size();
            // Determine the angle between each caliper and the next adjacent edge in the polygon
            double dAngle1 = Caliper1.vector.AngleToRight(((C2DLine) Lines.get(nP1 % nMod)).vector);
            double dAngle2 = Caliper2.vector.AngleToRight(((C2DLine) Lines.get(nP2 % nMod)).vector);
            double dAngle3 = Caliper3.vector.AngleToRight(((C2DLine) Lines.get(nP3 % nMod)).vector);
            double dAngle4 = Caliper4.vector.AngleToRight(((C2DLine) Lines.get(nP4 % nMod)).vector);
            double dMinAngle;
            if (dAngle1 < dAngle2 &&
                    dAngle1 < dAngle3 &&
                    dAngle1 < dAngle4) {
                dMinAngle = dAngle1;
                nP1++;
                Caliper1.point = Lines.get(nP1 % Lines.size()).GetPointFrom();
            } else if (dAngle2 < dAngle3 &&
                    dAngle2 < dAngle4) {
                dMinAngle = dAngle2;
                nP2++;
                Caliper2.point = Lines.get(nP2 % Lines.size()).GetPointFrom();
            } else if (dAngle3 < dAngle4) {
                dMinAngle = dAngle3;
                nP3++;
                Caliper3.point = Lines.get(nP3 % Lines.size()).GetPointFrom();
            } else {
                dMinAngle = dAngle4;
                nP4++;
                Caliper4.point = Lines.get(nP4 % Lines.size()).GetPointFrom();
            }
            dRotatedAngle += dMinAngle;
            dMinAngle -= 0.00000001;
            Caliper1.vector.TurnRight(dMinAngle);
            Caliper2.vector.TurnRight(dMinAngle);
            Caliper3.vector.TurnRight(dMinAngle);
            Caliper4.vector.TurnRight(dMinAngle);
            double dWidth1 = Caliper1.DistanceAsRay(Caliper2.point);
            double dWidth2 = Caliper3.DistanceAsRay(Caliper4.point);
            double dArea = dWidth1 * dWidth2;
            if (dArea < dMinArea) {
                dMinArea = dArea;
                Line.Set(Caliper1);
                Line.point.ProjectOnRay(Caliper4);
                Line.vector.SetLength(dWidth2);
                dWidthToRight = dWidth1;
            }
        }
    }

    /**
     * Sub area 1.
     */
    protected C2DPolygon subArea1 = null;

    /**
     * Sub area 1 access.
     */
    public C2DPolygon getSubArea1() {
        return subArea1;
    }

    /**
     * Sub area 2.
     */
    protected C2DPolygon subArea2 = null;

    /**
     * Sub area 2 access.
     */
    public C2DPolygon getSubArea2() {
        return subArea2;
    }

    /**
     * Class to help with sorting.
     */
    public class CLineBaseRectLeftToRight implements Comparator<CLineBaseRect> {
        /**
         * Compare function.
         */
        public int compare(CLineBaseRect L1, CLineBaseRect L2) {
            if (L1 == L2)
                return 0;
            if (L1.Rect.getTopLeft().x > L2.Rect.getTopLeft().x)
                return 1;
            else if (L1.Rect.getTopLeft().x == L2.Rect.getTopLeft().x)
                return 0;
            else
                return -1;
        }
    }
}