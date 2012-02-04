import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.util.*;

public class draw_anything extends JComponent
{
    ArrayList<Object> objects = new ArrayList<Object>();
    ArrayList<Char> characters = new ArrayList<Char>();

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
    public void set_characters(ArrayList cl) {
        characters = cl;
        repaint();
    }
}
