package Entity;


import java.awt.*;

/* cube class to store the main vertex of a wall in the map */

public class Cube {
    public Point a;
    public Point b;
    public Point c;
    public Point d;

    public Cube(Point a, Point b, Point c, Point d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }   
}
