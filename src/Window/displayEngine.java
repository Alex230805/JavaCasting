package Window;


import Entity.CastPoint;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public interface displayEngine{
    public static int light = 10;
    public static void displayLine(int width, int height,int distance, int rayN, Graphics graph, int currentRay, int fov){
        int rayWidth = (int)(width/rayN);
        int rayX = rayWidth*currentRay;
        int rayHeight = (int)(height-distance/fov);
        int rayY = (height - rayHeight)/2;
        graph.setColor(new Color(255-light-distance/4,255-light-distance/4,255-light-distance/4));
        graph.fillRect(rayX,rayY, rayWidth,rayHeight);
    }

    public static void displayLineV2(Graphics graph,ArrayList<CastPoint> povPoint, int width, int height, int rayNumber, int fov){
        CastPoint cp = null;
        CastPoint switchPoint = null;
        int rayWidth = (int)(width/rayNumber);
        int rayHeight = 0;
        int rayY = 0;
        int rayX = 0;
        int SWrayHeight = -1;
        int SWrayY = -1;
        int SWrayX  = -1;
        int pre = 0;

        if(!povPoint.isEmpty()){
            System.out.println(" ");
            switchPoint = povPoint.get(0);
            SWrayHeight =(int)(height-switchPoint.distance/fov);
            SWrayY = (height - rayHeight)/2;
            SWrayX = rayWidth*switchPoint.currentRayN;
            for(int i=1;i<povPoint.size();i++){
                cp = povPoint.get(i);
                rayX = rayWidth*cp.currentRayN-1;
                rayHeight = (int)(height-cp.distance/fov);
                rayY = (height - rayHeight)/2;
                /* 
                graph.setColor(new Color(0,0,0));
                graph.drawLine(rayX,rayY,rayX, rayY+rayHeight);
                graph.drawLine(SWrayX, SWrayY+SWrayHeight, SWrayX, SWrayY);
                if(SWrayX+rayWidth == rayX){
                    graph.drawLine(SWrayX, SWrayY, rayX,rayY);
                    graph.drawLine(rayX, rayY+rayHeight, SWrayX, SWrayY+SWrayHeight);
                }
                graph.setColor(new Color(255,255,255));
                */
                if(SWrayX+rayWidth == rayX){
                    int[] xPoints = {SWrayX,SWrayX,rayX,rayX};
                    int[] yPoints = {SWrayY,SWrayY+SWrayHeight, rayY+rayHeight,rayY};
                    int n_point = 4;

                    int r = 255-light-cp.distance/4;
                    int g = 255-light-cp.distance/4;
                    int b = 255-light-cp.distance/4;
                    
                    if(r < 0){
                        r = 0;
                    }
                    if(g < 0){
                        g = 0;
                    }
                    if(b < 0){
                        b = 0;
                    }   
                    graph.setColor(new Color(r,g,b));
                    graph.fillPolygon(xPoints,yPoints,n_point);
                }

                SWrayHeight = rayHeight;
                SWrayX = rayX;
                SWrayY = rayY;

            }
        }else{
            System.out.println("Is Empty");
        }
    }
}