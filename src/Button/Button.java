package Button;

import java.awt.*;
import javax.swing.*;

/*
 * 
 * this button object is not used int the project. Add it may cause some issue. 
 * 
 */

public class Button extends JComponent{
    private String title = null;
    private int lenghtMax = 0;
    private int width;
    private int height;
    private int x;
    private int y;
    public Color bgColor = new Color(255, 255, 255);
    public Color bgColorHover = new Color(190, 190, 190);
    public Color selected = bgColor;
    public boolean state = false;

    public Button(int x, int y, String title) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = title.length() * 12;
        this.height = 40;
        this.setEnabled(true);
        this.setName(title);
        this.setSize(width,height);
        this.setBackground(new Color(255,0,0));
    }

    public void paintComponent(Graphics graph) {
        Font f = new Font("Monospaced", Font.PLAIN, 16);
        FontMetrics metrics = getFontMetrics(f);
        int posX = x + (width - metrics.stringWidth(title)) / 2;
        int posY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();

        graph.setColor(selected);
        graph.fillRect(this.x, this.y, width, height);
        graph.setColor(new Color(0, 0, 0));
        graph.setFont(f);

        graph.drawString(title, posX, posY);
    
    }
}
