package Window;


import Entity.Cube;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import Entity.entity;
import rayCasting.rayCasting;

public class miniMap extends JComponent {
    private int[][] Map;
    private int playerX = 0;
    private int playerY = 0;
    private inputPosition pos;
    private int scale = 1;

    private int fl = 0;
    private int pov = 20;
    private int rayN = 2;

    private Point rayOrigin = null;
    private Point rayEnd = null;

    private Color blockColor = null;
    private Color rayColor = null;
    private Color playerColor = null;

    private int map_lenght = 0;
    private int map_height = 0;

    private rayCasting rayEngine;


    private ArrayList<Cube> section = new ArrayList<>();
    private ArrayList<Point> castedPoint = new ArrayList<>();


    public miniMap(int[][] Map,int map_lenght,int map_height, int playerX, int playerY, inputPosition pos, int pov, int rayN, int scale) {
        this.rayEngine = new rayCasting();
        this.Map = Map;
        this.playerX = playerX;
        this.playerY = playerY;
        this.pos = pos;
        this.pov = pov;
        this.rayN = rayN;
        this.map_height = map_height;
        this.map_lenght = map_lenght;
        this.scale = scale;

        for (int i = 0; i < map_height; i++) {
            for (int j = 0; j < map_lenght; j++) {
                if (Map[i][j] != 0) {
                    Point a = new Point(j * scale,i * scale);
                    Point b = new Point((j * scale)+scale,i * scale);
                    Point c = new Point((j * scale)+scale,(i * scale)+scale);
                    Point d = new Point(j * scale,(i * scale)+scale);
                    Cube cb = new Cube(a,b,c,d);
                    section.add(cb);
                }
            }
        }

    }

    public void setRayOrigin(int origin) {
        this.rayOrigin = new Point(origin, origin);
    }

    public void setRayLenght(int rayLenght) {
        this.fl = rayLenght;
    }

    public void setBlockColor(Color color) {
        this.blockColor = color;
    }

    public void setRayColor(Color color) {
        this.rayColor = color;
    }

    public void setPlayerColor(Color color) {
        this.playerColor = color;
    }

    public void paintMap(Graphics graph){
        int block_lenght = scale;

        for (int i = 0; i < map_height; i++) {
            for (int j = 0; j < map_lenght; j++) {
                if (Map[i][j] > 0) {
                    graph.setColor(blockColor);
                } else {
                    graph.setColor(new Color(0, 0, 0));
                }
                graph.fillRect(j * scale, i * scale, block_lenght, block_lenght);
            }
        }
    }

    public void paintComponent(Graphics graph) {

            int rayInitialX = 0;
            int rayInitialY = 0;
            int directionX = 0;
            int directionY = 0;
            double angle = (pov / rayN) * Math.PI / 180;
            double dAngle = 0;
            double half_inclination = (pov/2)*(Math.PI/180);
    
            this.paintMap(graph);
    
            Point rayStart;
            Point rayEnd;
            Point position = this.getPosition();
            
            graph.setColor(playerColor);
            graph.fillRect(position.x, position.y, scale / 2, scale / 2);
            rayInitialX = position.x + rayOrigin.x;
            rayInitialY = position.y + rayOrigin.y;
    
            directionX = (int)(rayInitialX+fl*Math.cos(pos.getRotation()));
            directionY = (int)(rayInitialY+fl*Math.sin(pos.getRotation()));
                    
            rayStart = new Point(rayInitialX,rayInitialY);
    
            graph.setColor(rayColor);
    
            for(int i=0;i<rayN;i++){
                dAngle = (angle*i)-half_inclination;
                directionX = (int)(rayInitialX+fl*Math.cos(dAngle+pos.getRotation()));
                directionY = (int)(rayInitialY+fl*Math.sin(dAngle+pos.getRotation()));
                rayEnd = new Point(directionX,directionY);
                try{
                    rayIntersection(graph,rayStart,rayEnd);
                }catch(Exception ex){
                    graph.drawLine(rayInitialX,rayInitialY, directionX,directionY);
                }

            }


    }

    public void rayIntersection(Graphics graph,Point rayStart, Point rayEnd) throws Exception{
        int lenght = 0;
        int cache = 0;
        int stage = 0;

        for(int i=0;i<section.size();i++){
            Point a = rayEngine.rayCast(section.get(i).a,section.get(i).b, rayStart, rayEnd);
            Point b = rayEngine.rayCast(section.get(i).b,section.get(i).c, rayStart, rayEnd);
            Point c = rayEngine.rayCast(section.get(i).c,section.get(i).d, rayStart, rayEnd);
            Point d = rayEngine.rayCast(section.get(i).d,section.get(i).a, rayStart, rayEnd);

            if(a != null){
                //graph.fillOval(a.x, a.y, scale/10, scale/10);
                //graph.drawLine(rayStart.x,rayStart.y,a.x,a.y);
                castedPoint.add(a);                   
            }else{
                //graph.drawLine(rayStart.x,rayStart.y,rayEnd.x,rayEnd.y);
            }
            if(b != null){
                //graph.fillOval(b.x, b.y, scale/10, scale/10);
                //graph.drawLine(rayStart.x,rayStart.y,b.x,b.y);
                castedPoint.add(b);
            }else{
                //graph.drawLine(rayStart.x,rayStart.y,rayEnd.x,rayEnd.y);
            }
            if(c != null){
                //graph.fillOval(c.x, c.y, scale/10, scale/10);
                //graph.drawLine(rayStart.x,rayStart.y,c.x,c.y);
                castedPoint.add(c);
            }else{
                //graph.drawLine(rayStart.x,rayStart.y,rayEnd.x,rayEnd.y);
            }
            if(d != null){
                //graph.fillOval(d.x, d.y, scale/10, scale/10);
                //graph.drawLine(rayStart.x,rayStart.y,d.x,d.y);
                castedPoint.add(d);
            }else{
                //graph.drawLine(rayStart.x,rayStart.y,rayEnd.x,rayEnd.y);
            }
        }
        int cx = rayEnd.x - rayStart.x;
        int cy = rayEnd.y - rayStart.y;
        cx *= cx;
        cy *= cy;
        lenght= (int)(Math.sqrt(cx+cy));
        try{
            for(int i=0;i<castedPoint.size();i++){
                cx = castedPoint.get(i).x - rayStart.x;
                cy =  castedPoint.get(i).y - rayStart.y;
                cx *= cx;
                cy *= cy;
                cache = (int)(Math.sqrt(cx+cy));
                if(cache < lenght){
                    lenght = cache;
                    stage = i;
                }
            }
            graph.drawLine(rayStart.x,rayStart.y,castedPoint.get(stage).x,castedPoint.get(stage).y);
            castedPoint.clear();
        }catch(Exception ex){
            throw ex;
        }

    }

    private Point getPosition(){
        int offX = pos.getX();
        int offY = pos.getY();
        int width = map_lenght*scale;
        int height = map_height*scale;
        width -= (scale);
        height -= (scale);
        int playerSize = scale/2;
        int positionX = (playerX * scale) + offX;
        int positionY = (playerY * scale) + offY;

        if (positionX >= width-playerSize) {
            pos.setX(width-scale-playerSize);
        } else if (positionX <= scale) {
            pos.setX(0);
        }
        if (positionY >= height-playerSize) {
            pos.setY(height-scale-playerSize);
        } else if (positionY < scale) {
            pos.setY(0);
        }
        return new Point(positionX,positionY);
    }
}