package Window;



import Entity.CastPoint;
import Window.displayEngine;
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

public class MapCast extends JComponent implements displayEngine{
    private int[][] Map;
    private int playerX = 0;
    private int playerY = 0;
    private inputPosition pos;
    private int scale = 1;

    private int fl = 0;
    private int pov = 20;
    private int rayN = 2;

    private Point rayOrigin = null;
    private int mapSize = 30;

    private Color blockColor = null;
    private Color rayColor = null;
    private Color playerColor = null;

    private int map_lenght = 0;
    private int map_height = 0;
    private int displayWidth = 0;
    private int displayHeight = 0;

    private rayCasting rayEngine;
    private int fov;
    private boolean oldEngine = false;

    private ArrayList<Cube> section = new ArrayList<>();
    private ArrayList<Point> castedPoint = new ArrayList<>();
    private ArrayList<CastPoint> povTotalPoint = new ArrayList<>();


    public MapCast(int[][] Map,int map_lenght,int map_height, int playerX, int playerY, inputPosition pos, int pov, int rayN, int scale,int displayWidth, int displayHeight, int fov) {
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
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.fov = fov;

    

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


        for (int i = 0; i < map_height; i++) {
            for (int j = 0; j < map_lenght; j++) {
                if (Map[i][j] > 0) {
                    graph.setColor(blockColor);
                } else {
                    graph.setColor(new Color(0, 0, 0));
                }
                graph.fillRect(j * mapSize, i * mapSize, mapSize, mapSize);
            }
        }
    }

    public void paintComponent(Graphics graph) {
            boolean minimap_enabled = pos.getMiniMapView();
            boolean fp_enable = pos.getFPView();

            int rayInitialX = 0;
            int rayInitialY = 0;
            int directionX = 0;
            int directionY = 0;
            double angle = (pov / rayN) * Math.PI / 180;
            double dAngle = 0;
            double half_inclination = (pov/2)*(Math.PI/180);

            Point rayStart;
            Point rayEnd;
            Point position = this.getPosition();
            Point miniPos = this.getMiniPosition();

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
                    rayIntersection(graph,rayStart,rayEnd,minimap_enabled,fp_enable, i);
                }catch(Exception ex){
                }
            }
            if(fp_enable && !oldEngine){
                displayEngine.displayLineV2(graph,povTotalPoint, displayWidth,displayHeight, rayN, fov);
                povTotalPoint.clear();
            }
                
            if(minimap_enabled){
                this.paintMap(graph);
                graph.setColor(playerColor);
                graph.fillRect(miniPos.x, miniPos.y,5, 5);
            }
    }

    public void rayIntersection(Graphics graph,Point rayStart, Point rayEnd, boolean minimap_enabled, boolean fp, int current_ray) throws Exception{
        int lenght = 0;
        int cache = 0;
        int stage = 0;

        for(int i=0;i<section.size();i++){
            Point a = rayEngine.rayCast(section.get(i).a,section.get(i).b, rayStart, rayEnd);
            Point b = rayEngine.rayCast(section.get(i).b,section.get(i).c, rayStart, rayEnd);
            Point c = rayEngine.rayCast(section.get(i).c,section.get(i).d, rayStart, rayEnd);
            Point d = rayEngine.rayCast(section.get(i).d,section.get(i).a, rayStart, rayEnd);

            if(a != null){
                castedPoint.add(a);
            }
            if(b != null){
                castedPoint.add(b);
            }
            if(c != null){
                castedPoint.add(c);
            }
            if(d != null){
                castedPoint.add(d);
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
            if(fp && oldEngine){
                displayEngine.displayLine(displayWidth,displayHeight,lenght, rayN, graph, current_ray, fov);
            }else{
                povTotalPoint.add(new CastPoint(castedPoint.get(stage), lenght, current_ray));
            }
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
    private Point getMiniPosition(){
        int offX = pos.getX()/(scale/mapSize);
        int offY = pos.getY()/(scale/mapSize);
        int width = map_lenght*mapSize;
        int height = map_height*mapSize;
        width -= (mapSize);
        height -= (mapSize);
        int positionX = (playerX * mapSize) + offX;
        int positionY = (playerY * mapSize) + offY;
        return new Point(positionX,positionY);
    }
}