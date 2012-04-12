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

    int mouse_animation_counter = 1;
    
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
            
        if (content.show_mouse_animation) {
            // draw nice onclick-mouse animation
            g.drawImage(content.iml.get_img("pics/Icons/Maus/gif"+mouse_animation_counter+".png"), content.mouse_x-9, content.mouse_y-9, this);
            mouse_animation_counter++;
            if (mouse_animation_counter == 6) {
                mouse_animation_counter = 1;
                content.show_mouse_animation= false;
            }
        }
    }
}
