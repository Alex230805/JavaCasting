package Entity;

import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JComponent;

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
