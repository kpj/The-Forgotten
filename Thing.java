import java.awt.*;
import javax.swing.*;

public class Thing
{
    String name, type;
    Image world_image;
    ImageIcon icon;
    float x_pos, y_pos, height, width;
    boolean touched = false;

    public Thing(String n, float x, float y, String w_i, String t)
    {
        name = n;
        type = t;
        world_image = Toolkit.getDefaultToolkit().getImage(w_i);
        
        icon = new ImageIcon(world_image);
        height = icon.getIconHeight();
        width = icon.getIconWidth();
        x_pos = x;
        y_pos = y;
    }
    
    public Image get_image() {
        return world_image;
    }

    public float get_width() {
        return width;
    }
    public float get_height() {
        return height;
    }
    public Rectangle get_rect() {
        return new Rectangle((int)x_pos,(int)y_pos,(int)width,(int)height);
    }
    
    public void change_pos(int x, int y) {
        x_pos += x;
        y_pos += y;
    }
    
    public void on_touch() {
        touched = true;
    }
    public boolean get_touched() {
        return touched;
    }
    public void set_touched(boolean state) {
        touched = state;
    }
    
    public String get_type() {
        return type;
    }
}
