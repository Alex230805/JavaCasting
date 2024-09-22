package Window;

import java.awt.*;
import javax.swing.*;


import java.awt.event.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Window implements KeyListener, Input {
    private int height;
    private int width;
    private JFrame frame = null;
    private Panel p = null;
    private inputPosition pos = null;



    public Window(int width, int height) {
        this.width = width;
        this.height = height;
        frame = new JFrame("Text");
        pos = new inputPosition(4);
        frame.setResizable(false);
        frame.setResizable(true);
        frame.setTitle("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, width, height);
        frame.setSize(width, height);
        frame.setFont(new Font("Monospaced", Font.PLAIN, 12));
        frame.setBackground(new Color(0, 0, 0));
        p = new Panel(width, height, pos);

        p.setVisible(true);
        frame.add(p);
        frame.setVisible(true);
        frame.addKeyListener(this);
        frame.setEnabled(true);

        ScheduledExecutorService scheduledInput = Executors.newScheduledThreadPool(1);
        scheduledInput.scheduleAtFixedRate(()->{
            if (Input.inputBuffer.contains(KeyEvent.VK_W) && Input.inputBuffer.contains(KeyEvent.VK_D)){
                pos.changeFactor(3);
            }else if (Input.inputBuffer.contains(KeyEvent.VK_W) && Input.inputBuffer.contains(KeyEvent.VK_A)){
                pos.changeFactor(3);
            }else if (Input.inputBuffer.contains(KeyEvent.VK_S) && Input.inputBuffer.contains(KeyEvent.VK_D)){
                pos.changeFactor(3);
            }else if (Input.inputBuffer.contains(KeyEvent.VK_S) && Input.inputBuffer.contains(KeyEvent.VK_A)){
                pos.changeFactor(3);
            }else{
                pos.restoreFactor();
            }

            if (Input.inputBuffer.contains(KeyEvent.VK_W)) {
                pos.moveFw();
            }
            if (Input.inputBuffer.contains(KeyEvent.VK_S)) {
                pos.moveBw();
            }
            if (Input.inputBuffer.contains(KeyEvent.VK_A)) {
                pos.moveLf();
            }
            if (Input.inputBuffer.contains(KeyEvent.VK_D)) {
                pos.moveRt();
            }

            if (Input.inputBuffer.contains(KeyEvent.VK_LEFT)){
                pos.rotateAntiClockwise();
            }
            if (Input.inputBuffer.contains(KeyEvent.VK_RIGHT)){
                pos.rotateClockwise();
            }

            if(Input.inputBuffer.contains(KeyEvent.VK_1)){
              pos.switchEngine(0);
            }
            if(Input.inputBuffer.contains(KeyEvent.VK_2)){
              pos.switchEngine(1);
            }
            if(Input.inputBuffer.contains(KeyEvent.VK_3)){
              pos.switchEngine(2);
            }


    }, 0, 16, TimeUnit.MILLISECONDS);

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public inputPosition getPos() {
        return pos;
    }

    public synchronized void updateSchene() throws Exception{
        frame.repaint();
    }

    public void keyPressed(KeyEvent e) {
        Input.inputBuffer.add(e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        Input.inputBuffer.remove(e.getKeyCode());
    }

    public void keyTyped(KeyEvent e) {
        Input.inputBuffer.add(e.getKeyCode());
        if(Input.inputBuffer.contains(KeyEvent.VK_M)){
            pos.changeMiniMapView();
        }
        if(Input.inputBuffer.contains(KeyEvent.VK_P)){
            pos.changeFPView();
        }
        if(Input.inputBuffer.contains(KeyEvent.VK_T)){
            pos.changeLight();
        }
        Input.inputBuffer.remove(e.getKeyCode());
    }
}
