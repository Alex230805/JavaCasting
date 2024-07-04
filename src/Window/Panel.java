package Window;


import Button.Button;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import Entity.entity;
import rayCasting.rayCasting;

public class Panel extends JPanel implements MouseListener{
    private int width;
    private int height;
    private inputPosition pos = null;
    private int[][] Map;
    private int playerX;
    private int playerY;
    private miniMap topView; 
    private Button btn_for_minimap;

    private int pov = 60;
    private int rayN = 60;
    private int rayLenght = 10000;
    private int scale = 70;

    public Panel(int width,int height, inputPosition pos){
        this.width = width;
        this.height = height;   
        setSize(width,height);
        this.pos = pos;
        playerX = 1;
        playerY = 1;
        rayCasting rayEngine = new rayCasting();
        btn_for_minimap = new Button(900, 10,"Toggle Minimap");

        /*
         *  TODO: try to find a way to make the toggle button work properly
         * 
         *  for now the main listener is attached to the main Panel
         */
        this.addMouseListener(this);

        Map = new int[][]{
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1},
            {1,1,1,1,0,1,0,0,0,0,1,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0},
            {1,0,0,0,0,0,1,0,0,0,1,1,1,1,1,0},
            {1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,0,0,0,1,0,0,0,0,1,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
            {1,1,0,0,0,0,0,1,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,0,0,1,0,0,0,0,1,0,1},
            {1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1},
        };
        topView = new miniMap(Map,16 ,12 ,1, 1, pos, pov, rayN, scale);
        topView.setRayOrigin(scale/4);
        topView.setRayLenght(rayLenght);
        topView.setPlayerColor(new Color(0,255,0));
        topView.setRayColor(new Color(255,0,0));
        topView.setBlockColor(new Color(255,255,255));
        this.setEnabled(true);
        this.setVisible(true);
        this.add(btn_for_minimap);
        this.add(topView);


    }
    @Override
    public void paintComponent(Graphics graph){
        btn_for_minimap.paint(graph);

        if(btn_for_minimap.state){
            topView.paint(graph);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!btn_for_minimap.state) {
            btn_for_minimap.state = true;
        } else {
            btn_for_minimap.state = false;
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        btn_for_minimap.selected = btn_for_minimap.bgColorHover;
    }
    @Override
    public void mouseExited(MouseEvent e) {
        btn_for_minimap.selected = btn_for_minimap.bgColor;
    }
}
