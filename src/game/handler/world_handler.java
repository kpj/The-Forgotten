package game.handler;
import game.Entity.Char;
import game.Entity.Thing;

import java.util.*;
import java.awt.*;

import core.graphics.draw_anything;

import parser.Image_parser;

public class world_handler
{
    Image bg_image;
    
    content_handler content;
    
    int border = 100;
    int char_x, char_y, char_w, char_h, window_h, window_w;
    int change_x, change_y;
    
    public boolean is_over = false;
    
    public world_handler(String bg_img, content_handler ch)
    {
        bg_image = Toolkit.getDefaultToolkit().getImage(bg_img);
        content = ch;
    }
        
    public void draw_stuff(Graphics g, draw_anything imo) {
        g.drawImage(bg_image, 0 , 0, content.window_width, content.window_height, content.world_bg_x_change, content.world_bg_y_change, content.world_bg_x_change+content.window_width, content.world_bg_y_change+content.window_height, imo);
    
        for (Thing current : content.objects) {
            g.drawImage(current.get_image(), (int)current.x_pos, (int)current.y_pos, imo);
        }
    
        for (Char current : content.characters) { // ÜBERARBEITEN, SONST MASSIVE LAGGS
            g.drawImage(new Image_parser(current.get_image()).get_img(), (int)current.x_pos, (int)current.y_pos, imo);
        }
    }
    
    public void on_click(String button) {
        
    }
    
    public void check_collisions() {
        for (int i = 0 ; i < content.get_objects().size() ; i++) {
            for (int u = 0 ; u < content.get_characters().size() ; u++) {
                Thing o = (Thing)content.get_objects().get(i);
                Char c = (Char)content.get_characters().get(u);

                //System.out.println(o.get_rect().toString() +"    "+ c.get_rect().toString());
                
                if (o.get_rect().intersects(c.get_rect())) {
                    o.on_touch();
                }
            }
        }
        
        check_actions();
    }
    
    public void check_actions() {
        Thing to_rm = null;
        for (Object ob : content.get_objects()) {
            Thing o = (Thing)ob;
            
            if (o.get_touched()) {
                String type = o.get_type();
                
                if (type == "NOTHING") {
                    // Well, nothing...
                }
                else if (type == "RANDOM_FIGHT") {
                    // START FIGHT
                    //create_fight();
                }
                
                to_rm = o;
                o.set_touched(false);
            }
        }
        if (to_rm != null) {
            content.rm_object(to_rm);
            to_rm = null;
        }
    }
    
    public void calc_movement() {
        window_w = content.window_width;
        window_h = content.window_height;
        
        if (content.get_characters().size() == 0) {
            return;
        }
        Char cur = content.get_characters().get(0);
        
        change_x = 0;
        change_y = 0;
        
        char_x = (int)cur.x_pos;
        char_y = (int)cur.y_pos;
        char_w = (int)cur.world_image_width;
        char_h = (int)cur.world_image_height;
        
        if (char_x < border) {
            change_x += border - char_x;
        }
        if (char_y < border) {
            change_y += border - char_y;
        }
        
        if (char_x + char_w > window_w - border) {
            change_x -= (char_x + char_w) - (window_w - border);
        }
        if (char_y + char_h > window_h - border) {
            change_y -= (char_y + char_h) - (window_h - border);
        }
        
        change_pos(cur);
    }
    public void change_pos(Char c) {
        c.change_pos(change_x, change_y);
        for (Object ob : content.get_objects()) {
            Thing o = (Thing)ob;
            o.change_pos(change_x, change_y);
        }
        content.world_bg_x_change -= change_x;
        content.world_bg_y_change -= change_y;
    }
}