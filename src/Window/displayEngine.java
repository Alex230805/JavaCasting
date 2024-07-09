package Window;

import Entity.CastPoint;
import java.awt.*;
import java.util.*;


public interface displayEngine {
    public static int light = 30;
    public static ArrayList<CastPoint> cachePoint = new ArrayList<>();
    public static final boolean protection_mode = false;
    public static int player_light = 120;
    public static int light_bias = 2;

    /*
     * 
     *  this are the main engine used to render things at window. All of them is working fine and 
     *  have different capability, but there is more than 1 because the usage of one or another will depends on the situation.
     *  
     *  With the enginePrint method is easy to switch between each engine to chose the most suitable 
     *  for the situation.
     * 
     */

    private static void displayLine(Graphics graph, ArrayList<CastPoint> povPoint, int width, int height, int rayNumber,
            int fov,boolean Plight) throws Exception {
        int rayWidth = 0;
        int rayX = 0;
        int rayHeight = 0;
        int rayY = 0;
        int distance = 0;
        int[] rgb;
        try {
            for (int i = 0; i < povPoint.size(); i++) {
                distance = povPoint.get(i).distance;
                rayWidth = (int) (width / rayNumber);
                rayX = rayWidth * povPoint.get(i).currentRayN;
                rayHeight = (int) (height - distance / fov);
                rayY = (height - rayHeight) / 2;
                rgb = shadowCasting(light_bias, distance,Plight);
                graph.setColor(new Color(rgb[0], rgb[1], rgb[2]));
                graph.fillRect(rayX, rayY, rayWidth, rayHeight);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private static void displayLineV2(Graphics graph, ArrayList<CastPoint> povPoint, int width, int height,
            int rayNumber, int fov, boolean Plight) throws Exception {
        CastPoint cp = null;
        CastPoint switchPoint = null;
        int rayWidth = (int) (width / rayNumber)+2;
        int rayHeight = 0;
        int rayY = 0;
        int rayX = 0;
        int SWrayHeight = -1;
        int SWrayY = -1;
        int SWrayX = -1;

        try {

            if (!povPoint.isEmpty()) {
                switchPoint = povPoint.get(0);
                SWrayHeight = (int) (height - switchPoint.distance / fov);
                SWrayY = (int)((height - rayHeight) * 0.5);
                SWrayX = rayWidth * switchPoint.currentRayN - rayWidth;
                for (int i = 1; i < povPoint.size(); i++) {
                    cp = povPoint.get(i);
                    rayX = rayWidth * cp.currentRayN-rayWidth;
                    rayHeight = (int) (height - cp.distance / fov);
                    rayY = (int)((height - rayHeight) * 0.5);
                    /*
                     * graph.setColor(new Color(0,0,0));
                     * graph.drawLine(rayX,rayY,rayX, rayY+rayHeight);
                     * graph.drawLine(SWrayX, SWrayY+SWrayHeight, SWrayX, SWrayY);
                     * if(SWrayX+rayWidth == rayX){
                     * graph.drawLine(SWrayX, SWrayY, rayX,rayY);
                     * graph.drawLine(rayX, rayY+rayHeight, SWrayX, SWrayY+SWrayHeight);
                     * }
                     * graph.setColor(new Color(255,255,255));
                     */
                    if (SWrayX + rayWidth == rayX) {
                        int[] xPoints = { SWrayX, SWrayX, rayX, rayX };
                        int[] yPoints = { SWrayY, SWrayY + SWrayHeight, rayY + rayHeight, rayY };
                        int n_point = 4;
                        int[] rgb = shadowCasting(light_bias, cp.distance, Plight);
                        fillVertex(xPoints, yPoints, n_point, graph, rgb);

                    }
                    SWrayHeight = rayHeight;
                    SWrayX = rayX;
                    SWrayY = rayY;
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private static void displayLineV3(Graphics graph, ArrayList<CastPoint> povPoint, int width, int height,
            int rayNumber, int fov, int resolution, boolean Plight) throws Exception {
   
            try{
                if(protection_mode){
                    throw new Exception("Renderign engine error: the selected engine is currently under development, change engine and recompile.");
                }
            }catch(Exception ex){
                throw ex;
            }

            /*
             * 
             *  TODO: implement a similar rendering engine like the V2, but adding the scaling to improve resolution.
             * 
             */
            
            CastPoint cp = null;
            CastPoint switchPoint = null;
            int totalPoint = rayNumber*resolution;
            int rayWidth = (int) (width / rayNumber)+resolution;
            int hrayWidth = (int)(rayWidth/resolution);
            int cache = hrayWidth;

            int rayHeight = 0;
            int rayY = 0;
            int rayX = 0;
            

            int SWrayHeight = -1;
            int SWrayY = -1;
            int SWrayX = -1;
            
            int SWdistance = 0;
            int currentDistance = 0;

            if(!povPoint.isEmpty()){
                SWdistance = povPoint.get(0).distance;
                for(int i=1;i<povPoint.size();i++){
                    cp = povPoint.get(i);
                    currentDistance = cp.distance;
                    SWdistance -= currentDistance;
                    SWdistance = SWdistance / resolution;

                    if(SWdistance < currentDistance){ /* if the current distance is greater than the previous distance */
                        SWdistance *= -1;
                    }
                    if(SWdistance > currentDistance){ /* if the previous  distance is greater than the current distance */
                        SWdistance *= +1;
                    }
                    rayX = rayWidth * cp.currentRayN - rayWidth;

                    for(int j=0;j<resolution;j++){
                        cachePoint.add(new CastPoint(new Point(rayX+hrayWidth, 0), currentDistance + SWdistance,(cp.currentRayN*resolution)+j));
                        currentDistance += SWdistance;
                        hrayWidth+=cache;
                    }
                    SWdistance = cp.distance;
                }
            }
        try {
            if(!cachePoint.isEmpty()){
                hrayWidth = cache;
                switchPoint = cachePoint.get(0);
                SWrayHeight = (int) (height - switchPoint.distance / fov);
                SWrayY = (int)((height - rayHeight) * 0.5);
                SWrayX = hrayWidth * switchPoint.currentRayN - hrayWidth;
                for (int i = 1; i < cachePoint.size(); i++) {
                    cp = cachePoint.get(i);
                    rayX = hrayWidth * cp.currentRayN - hrayWidth;
                    rayHeight = (int) (height - cp.distance / fov);
                    rayY = (int)((height - rayHeight) * 0.5);

                    if (SWrayX + hrayWidth == rayX) {
                        int[] xPoints = { SWrayX, SWrayX, rayX, rayX };
                        int[] yPoints = { SWrayY, SWrayY + SWrayHeight, rayY + rayHeight, rayY };
                        int n_point = 4;
                        int[] rgb = shadowCasting(light_bias, cp.distance, Plight);
                        fillVertex(xPoints, yPoints, n_point, graph, rgb);
                    }
                    SWrayHeight = rayHeight;
                    SWrayX = rayX;
                    SWrayY = rayY;
                }    
            }
            cachePoint.clear();
        } catch (Exception ex) {
            throw ex;
        }
    }

    
    private static int[] shadowCasting(int factor ,int distance, boolean Plight){
        int ambient_light = light;
        int[] rgb;

        if(Plight){
            ambient_light = light+player_light;
        }
        int r = 255 + ambient_light - distance/factor;
        int g = 255 + ambient_light - distance/factor;
        int b = 255 + ambient_light - distance/factor;

        if (r < 0) {r=0;} else if (r>255){r=255;}
        if (g < 0) {g=0;} else if (g>255){g=255;}
        if (b < 0) {b=0;} else if (b>255){b=255;}

        rgb = new int[]{r,g,b};
        return rgb;
    }
    
    private static void fillVertex(int[] xPoints, int[] yPoints, int n_point, Graphics graph, int[] rgb){
        graph.setColor(new Color(rgb[0], rgb[1], rgb[2]));
        graph.fillPolygon(xPoints, yPoints, n_point);
        return;
    }

    public static void enginePrint(Graphics graph, ArrayList<CastPoint> povPoint, int width, int height, int rayNumber,
            int fov, int engineSelection, int resolution, inputPosition pos) throws Exception {
        try {
            switch (engineSelection) {
                case 0:
                    displayLine(graph, povPoint, width, height, rayNumber, fov, pos.getLight());
                    break;
                case 1:
                    displayLineV2(graph, povPoint, width, height, rayNumber, fov, pos.getLight());
                    break;
                case 2:
                    displayLineV3(graph, povPoint, width, height, rayNumber, fov, resolution, pos.getLight());
                    break;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

}