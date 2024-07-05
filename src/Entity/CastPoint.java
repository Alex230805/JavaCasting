package Entity;



import Entity.CastPoint;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class CastPoint{
    public Point p;
    public int distance;
    public int currentRayN;

    public CastPoint(Point p, int distance,int currentRayN){
        this.p = new Point(p);
        this.distance = distance;
        this.currentRayN = currentRayN;
    }

}