import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.util.*;

public class draw_anything extends JComponent
{
    ArrayList<Object> objects = new ArrayList();

    public draw_anything()
    {
        
    }
    
    @Override
    protected void paintComponent( Graphics g )
    {
        for (Object current : objects) {
            g.drawImage(current.get_image(), (int)current.get_x(), (int)current.get_y(), this);
        }
    }
    
    public void set_objects(ArrayList ol) {
        objects = ol;
        repaint();
    }
}
