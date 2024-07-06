package Entity;



import Entity.CastPoint;
import java.awt.*;

/* Cast point used int the array of totalCastedPoint for the 3D rendering */

public class CastPoint{
    public Point p; /* point in the space */
    public int distance; /* distance from the camera */
    public int currentRayN; /* current ray number associated to the crossed point */

    public CastPoint(Point p, int distance,int currentRayN){
        this.p = new Point(p);
        this.distance = distance;
        this.currentRayN = currentRayN;
    }

}