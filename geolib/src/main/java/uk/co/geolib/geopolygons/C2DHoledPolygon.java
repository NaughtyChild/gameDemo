
package uk.co.geolib.geopolygons;

import uk.co.geolib.geolib.C2DPoint;
import uk.co.geolib.geolib.C2DVector;
import uk.co.geolib.geolib.CGrid;

import java.util.ArrayList;


/**
 * Class to represent a 2D polygon with holes.
 */
public class C2DHoledPolygon extends C2DHoledPolyBase {


    /**
     * Constructor.
     */
    public C2DHoledPolygon() {
        Rim = new C2DPolygon();
    }

    /**
     * Constructor.
     */
    public C2DHoledPolygon(C2DHoledPolyBase Other) {
        Rim = new C2DPolygon(Other.Rim);
        for (int i = 0; i < Other.getHoleCount(); i++) {
            Holes.add(new C2DPolygon(Other.GetHole(i)));
        }
    }

    /**
     * Constructor.
     */
    public C2DHoledPolygon(C2DHoledPolygon Other) {
        Rim = new C2DPolygon(Other.Rim);
        for (int i = 0; i < Other.getHoleCount(); i++) {
            Holes.add(new C2DPolygon(Other.GetHole(i)));
        }
    }

    /**
     * Constructor.
     *
     * @param Other Other polygon to set this to.
     */
    public C2DHoledPolygon(C2DPolyBase Other) {
        Rim = new C2DPolygon(Other);
    }

    /**
     * Constructor.
     *
     * @param Other Other polygon to set this to.
     */
    public C2DHoledPolygon(C2DPolygon Other) {
        Rim = new C2DPolygon(Other);
    }

    /**
     * Assignment.
     *
     * @param Other Other polygon to set this to.
     */
    public void Set(C2DHoledPolyBase Other) {
        Holes.clear();
        Rim = new C2DPolygon(Other.Rim);
        for (int i = 0; i < Other.getHoleCount(); i++) {
            Holes.add(new C2DPolygon(Other.GetHole(i)));
        }
    }

    /**
     * Assignment.
     *
     * @param Other Other polygon to set this to.
     */
    public void Set(C2DHoledPolygon Other) {
        Holes.clear();
        Rim = new C2DPolygon(Other.Rim);
        for (int i = 0; i < Other.getHoleCount(); i++) {
            Holes.add(new C2DPolygon(Other.GetHole(i)));
        }
    }

    /**
     * Rotates to the right by the angle around the centroid
     *
     * @param dAng Angle in radians to rotate by.
     */
    public void RotateToRight(double dAng) {
        RotateToRight(dAng, GetCentroid());
    }

    /**
     * Grows around the centroid.
     *
     * @param dFactor Factor to grow by.
     */
    public void Grow(double dFactor) {
        Grow(dFactor, GetCentroid());
    }


    /**
     * Calculates the centroid of the polygon by moving it according to each holes
     * weighted centroid.
     */
    public C2DPoint GetCentroid() {

        C2DPoint HoleCen = new C2DPoint(0, 0);

        if (Holes.size() == 0)
            return getRim().GetCentroid();


        C2DPoint PolyCen = getRim().GetCentroid();

        double dPolyArea = getRim().GetArea();
        double dHoleArea = 0;

        for (int i = 0; i < Holes.size(); i++) {
            dHoleArea += GetHole(i).GetArea();
        }


        if (dHoleArea == 0 || dHoleArea == dPolyArea)
            return getRim().GetCentroid();
        else {
            for (int i = 0; i < Holes.size(); i++) {
                C2DPoint pt = GetHole(i).GetCentroid();
                pt.Multiply(GetHole(i).GetArea() / dHoleArea);
                HoleCen.x += pt.x;
                HoleCen.y += pt.y;
            }
        }

        C2DVector Vec = new C2DVector(HoleCen, PolyCen);

        Vec.Multiply(dHoleArea / (dPolyArea - dHoleArea));

        PolyCen.Move(Vec);

        return PolyCen;
    }


    /**
     * Calculates the area.
     */
    public double GetArea() {
        double dResult = 0;

        dResult += getRim().GetArea();

        for (int i = 0; i < Holes.size(); i++) {
            dResult -= GetHole(i).GetArea();
        }
        return dResult;
    }

    /**
     * Buffers the polygon by buffering all shapes to expand the shape.
     * No attempt to handle resulting crossing lines as designed for
     * very small buffers.
     *
     * @param dBuffer The buffer amount
     */
    public void SimpleBuffer(double dBuffer) {
        getRim().SimpleBuffer(dBuffer);

        for (int i = 0; i < Holes.size(); i++) {
            GetHole(i).SimpleBuffer(-dBuffer);
        }
    }

    /**
     * Removes null areas within the shape according to the tolerance.
     *
     * @param dTolerance <returns>True if the shape is no longer valid.</returns>
     */
    public boolean RemoveNullAreas(double dTolerance) {
        if (Rim instanceof C2DPolygon) {
            if (((C2DPolygon) Rim).RemoveNullAreas(dTolerance)) {
                return true;
            }
        }

        int i = 0;
        while (i < Holes.size()) {
            if (GetHole(i).RemoveNullAreas(dTolerance)) {
                Holes.remove(i);
            } else {
                i++;
            }
        }
        return false;
    }


    /**
     * Gets the non overlaps i.e. the parts of this that aren't in the other.
     *
     * @param Other    The other shape.
     * @param Polygons The set to recieve the result.
     * @param grid     The degenerate settings.
     */
    public void GetNonOverlaps(C2DHoledPolygon Other, ArrayList<C2DHoledPolygon> Polygons,
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
    public void GetUnion(C2DHoledPolygon Other, ArrayList<C2DHoledPolygon> Polygons,
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
    public void GetOverlaps(C2DHoledPolygon Other, ArrayList<C2DHoledPolygon> Polygons,
                            CGrid grid) {
        ArrayList<C2DHoledPolyBase> NewPolys = new ArrayList<C2DHoledPolyBase>();

        super.GetOverlaps(Other, NewPolys, grid);

        for (int i = 0; i < NewPolys.size(); i++)
            Polygons.add(new C2DHoledPolygon(NewPolys.get(i)));
    }


    /**
     * The rim.
     */
    public C2DPolygon getRim() {
        return (C2DPolygon) Rim;
    }

    /**
     * Gets the Hole as a C2DPolygon.
     *
     * @param i Hole index.
     */
    public C2DPolygon GetHole(int i) {
        return (C2DPolygon) Holes.get(i);
    }

    /**
     * Hole access.
     */
    public void SetHole(int i, C2DPolygon Poly) {
        Holes.set(i, Poly);
    }

    /**
     * Hole addition.
     */
    public void AddHole(C2DPolygon Poly) {
        Holes.add(Poly);
    }

    /**
     * Hole access.
     */
    public void SetHole(int i, C2DPolyBase Poly) {
        if (Poly instanceof C2DPolygon) {
            Holes.set(i, Poly);
        } else {
            //  Debug.Assert(false, "Invalid Hole type");
        }
    }

    /**
     * Hole addition.
     */
    public void AddHole(C2DPolyBase Poly) {
        if (Poly instanceof C2DPolygon) {
            Holes.add(Poly);
        } else {
            //    Debug.Assert(false, "Invalid Hole type");
        }
    }

    /**
     * Hole removal.
     */
    public void RemoveHole(int i) {
        Holes.remove(i);
    }

}