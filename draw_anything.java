import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.util.*;

public class draw_anything extends JComponent
{
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

    public draw_anything()
    {
        
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
        if (fighter != null) {
            // Has to draw some fighting
        }
        else if (world != null) {
            // Draw a world
            g.drawImage(bg_image, 0,0,window_width,window_height ,bg_x_change,bg_y_change,bg_x_change+window_width,bg_y_change+window_height, this);
        
            for (Thing current : objects) {
                g.drawImage(current.get_image(), (int)current.get_x(), (int)current.get_y(), this);
            }
        
            for (Char current : characters) {
                g.drawImage(current.get_image(), (int)current.get_x(), (int)current.get_y(), this);
            }
        }
        else {
            // Draw nice map
            
        }
    }
    
    public void help_with_bg(Image bg_img, int ww, int wh, int cx, int cy) {
        bg_image = bg_img;
        window_width = ww;
        window_height = wh;
        bg_x_change -= cx;
        bg_y_change -= cy;
    }
    
    public void give_fight_handler(fight_handler fi) {
        fighter = fi;
    }
    public void give_world_handler(world_handler wo) {
        world = wo;
    }
    public void give_map_handler(map_handler ma) {
        map = ma;
    }
    
    public void set_objects(ArrayList<Thing> ol) {
        objects = ol;
        repaint();
    }
    public void set_characters(ArrayList<Char> cl) {
        characters = cl;
        repaint();
    }
}
