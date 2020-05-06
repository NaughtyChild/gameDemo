
package uk.co.geolib.geopolygons;

import uk.co.geolib.geolib.C2DPoint;
import uk.co.geolib.geolib.C2DVector;
import uk.co.geolib.geolib.CGrid;

import java.util.ArrayList;

public class C2DHoledPolyArc extends C2DHoledPolyBase {
    /**
     * Constructor.
     */
    public C2DHoledPolyArc() {
        Rim = new C2DPolyArc();
    }

    /**
     * Constructor.
     *
     * @param Other Other polygon to set this to.
     */
    public C2DHoledPolyArc(C2DHoledPolyBase Other) {
        Rim = new C2DPolyArc(Other.Rim);
        for (int i = 0; i < Other.getHoleCount(); i++) {
            Holes.add(new C2DPolyArc(Other.GetHole(i)));
        }
    }

    /**
     * Constructor.
     *
     * @param Other Other polygon to set this to.
     */
    public C2DHoledPolyArc(C2DPolyBase Other) {
        Rim = new C2DPolyArc(Other);
    }

    /**
     * Constructor.
     *
     * @param Other Other polygon to set this to.
     */
    public C2DHoledPolyArc(C2DPolyArc Other) {
        Rim = new C2DPolyArc(Other);
    }


    /**
     * Assignment.
     *
     * @param Other Other polygon to set this to.
     */
    public void Set(C2DHoledPolyBase Other) {
        Rim.Set(Other.Rim);
        Holes.clear();
        for (int i = 0; i < Other.getHoleCount(); i++) {
            Holes.add(new C2DPolyArc(Other.GetHole(i)));
        }
    }

    /**
     * Assignment.
     *
     * @param Other Other polygon to set this to.
     */
    public void Set(C2DHoledPolyArc Other) {
        Rim.Set(Other.Rim);
        Holes.clear();
        for (int i = 0; i < Other.getHoleCount(); i++) {
            Holes.add(new C2DPolyArc(Other.GetHole(i)));
        }
    }


    /**
     * Constructor.
     *
     * @param Other Other polygon to set this to.
     */
    public C2DHoledPolyArc(C2DHoledPolyArc Other) {
        Rim = new C2DPolyArc(Other.Rim);
        for (int i = 0; i < Other.getHoleCount(); i++) {
            Holes.add(new C2DPolyArc(Other.GetHole(i)));
        }
    }


    /**
     * Gets the area.
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
     * Gets the centroid.
     */
    C2DPoint GetCentroid() {
        C2DPoint Centroid = getRim().GetCentroid();
        double dArea = getRim().GetArea();

        for (int i = 0; i < Holes.size(); i++) {
            C2DVector vec = new C2DVector(Centroid, GetHole(i).GetCentroid());

            double dHoleArea = GetHole(i).GetArea();

            double dFactor = dHoleArea / (dHoleArea + dArea);

            vec.Multiply(dFactor);
            Centroid.x += vec.i;
            Centroid.y += vec.j;
            dArea += dHoleArea;
        }


        return Centroid;

    }


    /**
     * Gets the non overlaps i.e. the parts of this that aren't in the other.
     *
     * @param Other    The other shape.
     * @param Polygons The set to recieve the result.
     * @param grid     The degenerate settings.
     */
    public void GetNonOverlaps(C2DHoledPolyArc Other, ArrayList<C2DHoledPolyArc> Polygons,
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
    public void GetUnion(C2DHoledPolyArc Other, ArrayList<C2DHoledPolyArc> Polygons,
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
    public void GetOverlaps(C2DHoledPolyArc Other, ArrayList<C2DHoledPolyArc> Polygons,
                            CGrid grid) {
        ArrayList<C2DHoledPolyBase> NewPolys = new ArrayList<C2DHoledPolyBase>();

        super.GetOverlaps(Other, NewPolys, grid);

        for (int i = 0; i < NewPolys.size(); i++)
            Polygons.add(new C2DHoledPolyArc(NewPolys.get(i)));
    }


    /**
     * Rim access.
     */
    public C2DPolyArc getRim() {

        return (C2DPolyArc) Rim;
    }


    /**
     * Gets the Hole as a C2DPolyArc.
     *
     * @param i Hole index.
     */
    public C2DPolyArc GetHole(int i) {
        return (C2DPolyArc) Holes.get(i);
    }

    /**
     * Hole assignment.
     */
    public void SetHole(int i, C2DPolyArc Poly) {
        Holes.set(i, Poly);
    }

    /**
     * Hole addition.
     */
    public void AddHole(C2DPolyArc Poly) {
        Holes.add(Poly);
    }

    /**
     * Hole assignment.
     */
    public void SetHole(int i, C2DPolyBase Poly) {
        if (Poly instanceof C2DPolyArc) {
            Holes.set(i, Poly);
        } else {
            //     Debug.Assert(false, "Invalid Hole type" );
        }
    }

    /**
     * Hole addition.
     */
    public void AddHole(C2DPolyBase Poly) {
        if (Poly instanceof C2DPolyArc) {
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