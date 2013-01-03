package core.graphics;

import game.Entity.Char;
import game.Entity.Thing;
import game.handler.content_handler;
import game.handler.fight_handler;
import game.handler.map_handler;
import game.handler.world_handler;

import javax.swing.*;

import parser.Image_parser;

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
        get_mouse_pos();
        
        content.g = (Graphics2D)g;
        
        // some font related stuff
        content.font_metric = content.g.getFontMetrics();
        
        content.g.setFont(content.font);
     
        
        if (content.fight_active)
            ((fight_handler)content.get_active_environment()).draw_stuff(g, this);
        if (content.world_active)
            ((world_handler)content.get_active_environment()).draw_stuff(g, this);
        if (content.map_active)
            ((map_handler)content.get_active_environment()).draw_stuff(g, this);
            
        if(content.win_manager != null)
    		content.win_manager.draw_windows(g);
        
        if (content.show_mouse_animation) {
            // draw nice onclick-mouse animation
            g.drawImage(content.iml.get_img("/data/pics/Icons/Maus/gif"+mouse_animation_counter+".png"), content.mouse_x-9, content.mouse_y-9, this);
            mouse_animation_counter++;
            if (mouse_animation_counter == 6) {
                mouse_animation_counter = 1;
                content.show_mouse_animation= false;
            }
        }
        
        //if (content.fight_active)
        //    g.drawImage(content.iml.get_img("/data/pics/Icons/Maus/Schwerticon.png"), content.mouse_x, content.mouse_y, this);
        
        g.drawImage(new Image_parser("/data/pics/Icons/Maus/Schwerticon.png").get_img(), content.mouse_x, content.mouse_y, this);
    }
    
    public void get_mouse_pos() {
        content.mouse_x = (int) MouseInfo.getPointerInfo().getLocation().x - content.f.getLocationOnScreen().x;
        content.mouse_y = (int) MouseInfo.getPointerInfo().getLocation().y - content.f.getLocationOnScreen().y;
    }
}
