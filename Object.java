import java.awt.*;

public class Object
{
    String name;
    Image world_image;
    float x_pos, y_pos, height, width;

    public Object(String n, float x, float y, String w_i)
    {
        name = n;
        world_image = Toolkit.getDefaultToolkit().getImage(w_i);
        height = world_image.getHeight(null);
        width = world_image.getWidth(null);
        x_pos = x;
        y_pos = y;
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
}
