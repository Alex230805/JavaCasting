package Window;

public class inputPosition {
    private int x = 0;
    private int y = 0;
    private int factor = 0;
    private int cache = 0;
    private double rotation = 0;
    private boolean miniMapState = false;
    private boolean fp = true;
    
    public inputPosition(int factor){
        this.factor = factor;
        this.cache = factor;
    }

    public void moveFw(){
        y = y-factor;
    }

    public void moveBw(){
        y = y+factor;
    }

    public void moveLf(){
        x = x-factor;
    }
    public void moveRt(){
        x = x+factor;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void rotateClockwise(){
        this.rotation += 0.1;
    }
    public void rotateAntiClockwise(){
        this.rotation -= 0.1;
    }
    public double getRotation(){
        return rotation;
    }
    public void changeFactor(int factor){
        this.factor = factor;
    }
    public void restoreFactor(){
        this.factor = cache;
    }

    public void changeMiniMapView(){
        if(miniMapState){
            miniMapState = false;
        }else{
            miniMapState = true;
        }
    }

    public boolean getMiniMapView(){
        return miniMapState;
    }

    public void changeFPView(){
        if(fp){
            fp = false;
        }else{
            fp = true;
        }
    }

    public boolean getFPView(){
        return fp;
    }
}

