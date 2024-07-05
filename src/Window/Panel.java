package Window;


import Button.Button;
import Window.Input;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import Entity.entity;
import rayCasting.rayCasting;

public class Panel extends JPanel implements MouseListener, Input{
    private int width;
    private int height;
    private inputPosition pos = null;
    private int[][] Map;
    private int playerX;
    private int playerY;
    private MapCast topView; 
    private Button btn_for_minimap;

    private int pov = 50;
    private int rayN = 50;
    private int rayLenght = 10000;
    private int scale = 100;
    private int fov = 3;

    public Panel(int width,int height, inputPosition pos){
        try{
            this.width = width;
            this.height = height;   
            setSize(width,height);
            this.pos = pos;
            playerX = 1;
            playerY = 1;
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
            if(rayN > pov){
                throw new Exception("Build error: too many rays");
            }
            if(pov > 360 || rayN > 360){
                throw new Exception("Build error: max cast lenght reached");
            }
            if(rayLenght > 100000000){
                throw new Exception("Build error: def. cast lenght is too high");
            }

            topView = new MapCast(Map,16 ,12 ,1, 1, pos, pov, rayN, scale, width, height, fov);
            topView.setRayOrigin(scale/4);
            topView.setRayLenght(rayLenght);
            topView.setPlayerColor(new Color(0,255,0));
            topView.setRayColor(new Color(255,0,0));
            topView.setBlockColor(new Color(255,255,255));
            this.setEnabled(true);
            this.setVisible(true);
            this.add(topView);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }



    }
    @Override
    public void paintComponent(Graphics graph){
        topView.paint(graph);

    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        }
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
