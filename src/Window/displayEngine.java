package Window;

import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public interface displayEngine{

    public static void displayLine(int width, int height,int distance, int rayN, Graphics graph, int currentRay, int fov){
        int rayWidth = (int)(width/rayN);
        int rayX = rayWidth*currentRay-1;
        int rayHeight = (int)(height-distance/fov);
        int rayY = (height - rayHeight)/2;
        int light = 40;
        graph.setColor(new Color(255-light-distance/4,255-light-distance/4,255-light-distance/4));
        graph.fillRect(rayX,rayY, rayWidth,rayHeight);
    }
}