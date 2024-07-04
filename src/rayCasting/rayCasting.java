package rayCasting;

import java.util.Vector;

import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import Entity.entity;


public class rayCasting {
    public rayCasting(){}

    public boolean isCasting(Point p1,Point p2,Point p3,Point p4){
        int cubeInitialX = p1.x;
        int cubeInitialY = p1.y;
        int cubeFinalX = p2.x;
        int cubeFinalY = p2.y;
        int rayInitialX = p3.x;
        int rayInitialY = p3.y;
        int rayFinalX = p4.x;
        int rayFinalY = p4.y;

        double TnominatorP1 = (cubeInitialX - rayInitialX) * (rayInitialY - rayFinalY);
        double TnominatorP2 = (cubeInitialY-rayInitialY)*(rayInitialX-rayFinalX);
        double Tnominator = TnominatorP1 - TnominatorP2;

        double UnominatorP1 = (cubeInitialX-cubeFinalX)*(cubeInitialY-rayInitialY);
        double UnominatorP2 = (cubeInitialY-rayInitialY)*(cubeInitialX-rayInitialX);
        double Unominator = UnominatorP1 - UnominatorP2;

        double t = Tnominator;
        double u = Unominator;

        if(t < 1.0 && t > 0.0 && u > 0.0){
            return true;
        }else{
            return false;
        }
    }
    
    public Point rayCast(Point p1,Point p2,Point p3,Point p4){
        int cubeInitialX = p1.x;
        int cubeInitialY = p1.y;
        int cubeFinalX = p2.x;
        int cubeFinalY = p2.y;
        int rayInitialX = p3.x;
        int rayInitialY = p3.y;
        int rayFinalX = p4.x;
        int rayFinalY = p4.y;
        Point castPoint = null;

        double denominatorP1 = (cubeInitialX - cubeFinalX) * (rayInitialY - rayFinalY);
        double denominatorP2 = (cubeInitialY - cubeFinalY) * (rayInitialX - rayFinalX);
        double denominator = denominatorP1 - denominatorP2;

        double TnominatorP1 = (cubeInitialX - rayInitialX) * (rayInitialY - rayFinalY);
        double TnominatorP2 = (cubeInitialY-rayInitialY)*(rayInitialX-rayFinalX);
        double Tnominator = TnominatorP1 - TnominatorP2;

        double UnominatorP1 = (cubeInitialX-cubeFinalX)*(cubeInitialY-rayInitialY);
        double UnominatorP2 = (cubeInitialY-cubeFinalY)*(cubeInitialX-rayInitialX);
        double Unominator = -(UnominatorP1 - UnominatorP2);
        
        double t = Tnominator/denominator;
        double u = Unominator/denominator;

        if(denominator == 0){
            return null;
        }
        if(t > 0.0 && t < 1.0 &&  u > 0.0 && u < 1.0){
            int newX = (int)(cubeInitialX+t*(cubeFinalX-cubeInitialX));
            int newY = (int)(cubeInitialY+t*(cubeFinalY-cubeInitialY));
            castPoint = new Point(newX,newY);
            return castPoint;
        }else{
           return null;
        }
    }
}
