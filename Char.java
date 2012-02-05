import java.awt.*;

public class Char
{
    String name;
    Image world_image;
    float x_pos, y_pos, height, width, velocity;
    key_set set;
    
    public Char(String n, float x, float y, String w_i, key_set kset)
    {
        name = n;
        world_image = Toolkit.getDefaultToolkit().getImage(w_i);
        height = world_image.getHeight(null);
        width = world_image.getWidth(null);
        x_pos = x;
        y_pos = y;
        set = kset;
        
        velocity = 5;
    }
    
    public Image get_image() {
        return world_image;
    }
    
    public float get_x() {
        return x_pos;
    }
    public float get_y() {
        return y_pos;
    }
    
    public void move(String direction) {
        if (direction == "UP") {
            y_pos -= velocity;
        }
        else if (direction == "DOWN") {
            y_pos += velocity;
        }
        else if (direction == "LEFT") {
            x_pos -= velocity;
        }
        else if (direction == "RIGHT") {
            x_pos += velocity;
        }
    }
}
