package Entity;

import java.awt.*;
import javax.swing.*;

/*
 * 
 * this entity object is not used int the project. Add it may cause some issue. 
 * 
 */

public class entity extends JPanel{
    private int width = 0;
    private int height = 0;
    private int x = 0;
    private int y = 0;
    private Color color = null;
    public entity(int height,int width, Color color){
        this.height = height;
        this.width = width;
        this.color = color;
    }
    public void updateX(int x){
        this.x = x;
    }
    public void updateY(int y){
        this.y = y;
    }
    public void paintComponent(Graphics graph){
        graph.setColor(color);
        graph.fillRect(x,y,width,height);
    }
}
