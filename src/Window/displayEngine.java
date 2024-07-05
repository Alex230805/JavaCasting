package Window;

import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public interface displayEngine{

    public static void displayLine(int width, int height,int distance, int rayN, Graphics graph, int currentRay){
        int rayWidth = (int)(width/rayN);
        int rayX = rayWidth*currentRay-1;
        int rayHeight = (int)(height-distance);
        int rayY = (height - rayHeight)/2;
        int light = 50;
        graph.setColor(new Color(255-light-distance/2,255-light-distance/2,255-light-distance/2));
        graph.fillRect(rayX,rayY, rayWidth,rayHeight);
        graph.drawRect(rayX-1,rayY-1, rayWidth+1, rayHeight+1);
    }
}