package Window;

import Entity.CastPoint;
import java.awt.*;
import java.util.*;


public interface displayEngine {
    public static int light = 10;
    public static ArrayList<CastPoint> cachePoint = new ArrayList<>();

    /*
     * 
     *  this are the main engine used to render things at window. All of them is working fine and 
     *  have different capability, but there is more than 1 because the usage of one or another will depends on the situation.
     *  
     *  With the enginePrint method is easy to switch between each engine to chose the most suitable 
     *  for the situation.
     * 
     */

    public static void displayLine(Graphics graph, ArrayList<CastPoint> povPoint, int width, int height, int rayNumber,
            int fov) throws Exception {
        int rayWidth = 0;
        int rayX = 0;
        int rayHeight = 0;
        int rayY = 0;
        int distance = 0;
        try {
            for (int i = 0; i < povPoint.size(); i++) {
                distance = povPoint.get(i).distance;
                rayWidth = (int) (width / rayNumber);
                rayX = rayWidth * povPoint.get(i).currentRayN;
                rayHeight = (int) (height - distance / fov);
                rayY = (height - rayHeight) / 2;
                graph.setColor(
                        new Color(255 - light - distance / 4, 255 - light - distance / 4, 255 - light - distance / 4));
                graph.fillRect(rayX, rayY, rayWidth, rayHeight);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void displayLineV2(Graphics graph, ArrayList<CastPoint> povPoint, int width, int height,
            int rayNumber, int fov) throws Exception {
        CastPoint cp = null;
        CastPoint switchPoint = null;
        int rayWidth = (int) (width / rayNumber);
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
                SWrayY = (height - rayHeight) / 2;
                SWrayX = rayWidth * switchPoint.currentRayN;
                for (int i = 1; i < povPoint.size(); i++) {
                    cp = povPoint.get(i);
                    rayX = rayWidth * cp.currentRayN - 1;
                    rayHeight = (int) (height - cp.distance / fov);
                    rayY = (height - rayHeight) / 2;
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

                        int r = 255 - light - cp.distance / 4;
                        int g = 255 - light - cp.distance / 4;
                        int b = 255 - light - cp.distance / 4;

                        if (r < 0) {
                            r = 0;
                        }
                        if (g < 0) {
                            g = 0;
                        }
                        if (b < 0) {
                            b = 0;
                        }
                        graph.setColor(new Color(r, g, b));
                        graph.fillPolygon(xPoints, yPoints, n_point);
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

    public static void displayLineV3(Graphics graph, ArrayList<CastPoint> povPoint, int width, int height,
            int rayNumber, int fov, int resolution) throws Exception {
        try {
            if(true){
                throw new Exception("Renderign engine error: the selected engine is currently under development, change engine and recompile.");
            }
            CastPoint cp = null;
            CastPoint switchPoint = null;
            CastPoint c = null;

            int rayWidth = (int) (width / (rayNumber * resolution));
            int srayWidth = rayWidth;
            int crayWidth = rayWidth;

            int rayHeight = 0;
            int rayY = 0;
            int rayX = 0;
            int SWrayHeight = -1;
            int SWrayY = -1;
            int SWrayX = -1;

            int scalingCursor = 0;

            if (resolution < 1) {
                throw new Exception("Rendering error: invalid resolution value");
            }

            if (!povPoint.isEmpty()) {
                for (int i = 0; i < povPoint.size(); i++) {
                    c = povPoint.get(i);
                    scalingCursor = c.currentRayN;
                    for (int j = 0; j < resolution; j++) {
                        cachePoint.add(new CastPoint(new Point(c.p.x + crayWidth, 0), c.distance, scalingCursor + j));
                        crayWidth += srayWidth;
                    }
                }
                int totalPoint = rayNumber * resolution;
                switchPoint = cachePoint.get(0);
                SWrayHeight = (int) (height - switchPoint.distance / fov);
                SWrayY = (height - rayHeight) / 2;
                SWrayX = rayWidth * switchPoint.currentRayN;

                for (int i = 1; i < totalPoint; i++) {
                        cp = cachePoint.get(i);
                        rayX = rayWidth * cp.currentRayN;
                        rayHeight = (int) (height - cp.distance / fov);
                        rayY = (height - rayHeight) / 2;

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

                            int r = 255 - light - cp.distance / 4;
                            int g = 255 - light - cp.distance / 4;
                            int b = 255 - light - cp.distance / 4;

                            if (r < 0) {
                                r = 0;
                            }
                            if (g < 0) {
                                g = 0;
                            }
                            if (b < 0) {
                                b = 0;
                            }
                            graph.setColor(new Color(r, g, b));
                            graph.fillPolygon(xPoints, yPoints, n_point);
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

    public static void enginePrint(Graphics graph, ArrayList<CastPoint> povPoint, int width, int height, int rayNumber,
            int fov, int engineSelection, int resolution) throws Exception {
        try {
            switch (engineSelection) {
                case 0:
                    displayLine(graph, povPoint, width, height, rayNumber, fov);
                    break;
                case 1:
                    displayLineV2(graph, povPoint, width, height, rayNumber, fov);
                    break;
                case 2:
                    displayLineV3(graph, povPoint, width, height, rayNumber, fov, resolution);
                    break;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }
}