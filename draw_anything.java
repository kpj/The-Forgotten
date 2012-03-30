import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.util.*;

public class draw_anything extends JComponent
{
    content_handler content;

    ArrayList<Thing> objects = new ArrayList<Thing>();
    ArrayList<Char> characters = new ArrayList<Char>();
    
    fight_handler fighter = null;
    world_handler world = null;
    map_handler map = null;
    
    Image bufImage;
    Graphics bufG;
    
    Image bg_image;
    
    int window_width, window_height;
    int bg_x_change = 1000;
    int bg_y_change = 1000;

    public draw_anything(content_handler con)
    {
        content = con;
    }
    
    // Double-Buffering
    @Override
    public void update(Graphics g) {
       
        int w = this.getSize().width;
        int h = this.getSize().height;
 
        if(bufImage == null){
            bufImage = this.createImage(w,h);
            bufG = bufImage.getGraphics();
        }
        
        bufG.setColor(this.getBackground());
        bufG.fillRect(0,0,w,h);
        
        bufG.setColor(this.getForeground());
        
        paint(bufG);
 
        g.drawImage(bufImage,0,0,this);
    }
    
    @Override
    protected void paintComponent( Graphics g )
    {
        content.g = (Graphics2D)g;
        
        if (content.fight_active)
            ((fight_handler)content.get_active_environment()).draw_stuff(g, this);
        if (content.world_active)
            ((world_handler)content.get_active_environment()).draw_stuff(g, this);
        if (content.map_active)
            ((map_handler)content.get_active_environment()).draw_stuff(g, this);
        
        if (content.menu != null)
            content.menu.draw_stuff(g, this);
        if (content.notification != null)
            content.notification.draw_stuff(g, this);
    }
    
    public void help_with_bg(Image bg_img, int ww, int wh, int cx, int cy) {
        bg_image = bg_img;
        window_width = ww;
        window_height = wh;
        bg_x_change -= cx;
        bg_y_change -= cy;
    }
}
