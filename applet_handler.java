import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.util.*;

public class applet_handler
{
    draw_anything drawer = new draw_anything();

    public applet_handler()
    {
        JFrame f = new JFrame("The Forgotten");
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize(600, 500);
        f.add(drawer);
        f.setVisible(true);
        
        //Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

    }
    
    public void set_objects(ArrayList ol) {
        drawer.set_objects(ol);
    }
}
