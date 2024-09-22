package Window;

import Entity.CastPoint;
import Entity.Cube;
import java.awt.*;
import java.util.*;

import javax.swing.*;
import rayCasting.rayCasting;

public class MapCast extends JComponent implements displayEngine{

    private int[][] Map;    /* map of point used to calculate the vertex, to render the minimap and to render the 3D view */
    private int playerX = 0;
    private int playerY = 0;
    private inputPosition pos;  /* object to manage the interaction with the variable of the position and orientation. */
    private int scale = 1;

    /* camera setting passed by the argument of the constructor. Here some default value */
    private int fl = 100;
    private int pov = 20;
    private int rayN = 2;

    private Point rayOrigin = null;
    private int mapSize = 30;   /* variable to render the minimap, is used as a multiplicator for the drawing method*/

    /* color for the player, the block and the ray ( if rendered ) */
    private Color blockColor = null;
    private Color rayColor = null;
    private Color playerColor = null;

    private int map_lenght = 0; /* map lenght */
    private int map_height = 0; /* map height */
    private int displayWidth = 0; /* the window width */
    private int displayHeight = 0; /* the window height */

    private rayCasting rayEngine; /* the rayCasting engine */
    private int fov;

    /*
     * 
     *  Engine Selection: chose different type of engine to render 
     *  things at screen. At the moment the supported sequence are:
     * 
     *  0 = native rendering
     *  1 = point intersection rendering
     *  2 = point intersection with scaling rendering
     * 
     *  Other selection will throw an exceptions and the programm will crash
     * 
     */

//    private int engineSelection = 2;
    /*
     *
     *  NOTE: resolutrion will only work with the engineV3.
     *  To make use of that parameter you need to set the rendering
     *  selection "engineSelection" at least as 2. 
     *  In other case the resolutrion is simply ingored.
     * 
     */
    private int resolution = 2;

    /*
     * 
     *  Main array list used to store the vertex, the casted point and the totality of the closest point casted
     *  for the 3D rendering
     * 
     */

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

    
        /*
         * 
         *  This is the nested for loop to generate the array of point per cube for each cube in the map.
         *  All point in the map array would be taken to calculate the main vertex, and the resoulted point
         *  will be used to create an object "Cube" ( wich contain all the main point ).
         *  The new Cube will be nested in an Array list of cube. 
         * 
         *  NOTE: the calculated vertex of the cube is for only the 2D casting. It's not necessary to generate all the
         *  4 point per faces of the cube due to the fact that this version of the rayCasting engine is built with the 2D
         *  casting as the main goal, and with the display engine the 2D point will be turned into a projection 3D of the object.
         *  The height and the position in the space pretty much depend on the distance from the player to the wall casted.
         * 
         */
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
        /*
         * 
         *  Method used to paint the miniMap. It use a predeterminated scale to render the block,
         *  with a similar method used to calculate the main vertex, but used for oly the painting part of
         *  the minimap.
         * 
         */
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
            /*
             * 
             *  Boolean variable to identify whether or not the map and the player view is enabled.
             * 
             */
            boolean minimap_enabled = pos.getMiniMapView();
            boolean fp_enable = pos.getFPView();


            int rayInitialX = 0;    /* variable to identify the ray origin X */
            int rayInitialY = 0;    /* variable to identify the ray origin Y */
            int directionX = 0;     /* variable to identify the ray direction X */
            int directionY = 0;     /* variable to identify the ray direction X */

            double angle = (pov / rayN) * Math.PI / 180;    /* Angle between each projected ray. This use the POV divided for the total ray number.*/
            double dAngle = 0;
            double half_inclination = (pov/2)*(Math.PI/180);    /* Calc to offset the projection to center it with the player view */

            Point rayStart;     /* Point that will contain the origin of the ray */
            Point rayEnd;       /* Point that will contain the end of the ray */
            Point position = this.getPosition();    /* get the player position as a Point */
            Point miniPos = this.getMiniPosition(); /* get the player position in the miniMap as a Point */

            rayInitialX = position.x + rayOrigin.x; /* Calculate the coordinates of the origin offsetted by the position of the player */
            rayInitialY = position.y + rayOrigin.y;
    
            directionX = (int)(rayInitialX+fl*Math.cos(pos.getRotation())); /* calculate the direction of the player with the inclination, the origin and the total focal lenght */
            directionY = (int)(rayInitialY+fl*Math.sin(pos.getRotation())); /* same but for the Y axis */

            rayStart = new Point(rayInitialX,rayInitialY);  /* create the object with the origin */
    
            graph.setColor(rayColor);   /* set the ray color */
    

                for(int i=0;i<rayN;i++){
                    /*
                     * 
                     *  Here each line is calculated and sended to the rayIntersection method.
                     *  With the calculated angle difference between each rays the direction will be recalculated
                     *  with the next anble inclination and the offset from the player view, to center the casting.
                     * 
                     */
                    dAngle = (angle*i)-half_inclination;
                    directionX = (int)(rayInitialX+fl*Math.cos(dAngle+pos.getRotation()));
                    directionY = (int)(rayInitialY+fl*Math.sin(dAngle+pos.getRotation()));
                    rayEnd = new Point(directionX,directionY);

                    /*
                     * 
                     *  The ray intersection method use the start and the end of the point to call the rayCast engine
                     *  and find out where, in the map of vertex calculated before, a ray will cross them.
                     * 
                     */
                    rayIntersection(graph,rayStart,rayEnd,minimap_enabled,fp_enable, i);
    
                }
            try{
                if(fp_enable){ /* if the first person view is enabled then the display engine will be called */
                    displayEngine.enginePrint(graph,povTotalPoint, displayWidth,displayHeight, rayN, fov, pos.getEngine(), resolution, pos);
                    povTotalPoint.clear();
                }
    
                if(minimap_enabled){ /* if the minimap view is enabled then the minimap will be printed out */
                    this.paintMap(graph);
                    graph.setColor(playerColor);
                    graph.fillRect(miniPos.x, miniPos.y,5, 5);
                }
            }catch(Exception ex){
                System.exit(1);
            }

    }

    public void rayIntersection(Graphics graph,Point rayStart, Point rayEnd, boolean minimap_enabled, boolean fp, int current_ray){
        int lenght = 0;
        int cache = 0;
        int stage = 0;

        for(int i=0;i<section.size();i++){
            /* 
             * Intersection point obtained by check for all he existing vertex if a ray will cross them
             * there are a total of 4 point, that by the situation could all be null.
             * 
             */
            Point a = rayEngine.rayCast(section.get(i).a,section.get(i).b, rayStart, rayEnd);
            Point b = rayEngine.rayCast(section.get(i).b,section.get(i).c, rayStart, rayEnd);
            Point c = rayEngine.rayCast(section.get(i).c,section.get(i).d, rayStart, rayEnd);
            Point d = rayEngine.rayCast(section.get(i).d,section.get(i).a, rayStart, rayEnd);


            /*
             * 
             *  so if the point exist then it will be added to the casted point array
             * 
             */
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
        /*
         * 
         *  here the sorting of each point crossed by a single ray will
         *  occur to find out what point is the closest to the camera origin.
         * 
         */
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
            /*
             * 
             *  The closest point will be added to the totality of the casted point, ready to be sended to 
             *  the display engine.
             * 
             */
            povTotalPoint.add(new CastPoint(castedPoint.get(stage), lenght, current_ray));
            castedPoint.clear();
        }catch(Exception ex){}

    }

    /*
     * 
     *  Method to get the main position of the player in the 3d world and in the minimap view.
     * 
     * 
     */

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
