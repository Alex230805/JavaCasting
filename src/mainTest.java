import Window.Window;
import Window.inputPosition;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class mainTest{
    public static Window canvas;
    public static int TICK_TIME = 16;
    public static String ERR = "Error deteced: ";
    public static inputPosition pos = null;
    public static mainTest runtime = null;
    public static Graphics graph = null;

    public mainTest(){
        canvas = new Window(1080,720);
        pos = canvas.getPos();
    }
    public static void main(String args[]){
        try{
            runtime = new mainTest();
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
            scheduler.scheduleAtFixedRate(()->{
                runtime.updateSchene();
            }, 0, TICK_TIME, TimeUnit.MILLISECONDS);
        }catch(Exception ex){
            System.out.println(ERR+" "+ex.getMessage());
        }
    }
    public void updateSchene(){
        try{
            canvas.updateSchene();
        }catch(Exception ex){
            System.out.println(ERR+" "+ex.getMessage());
        }
    }
}