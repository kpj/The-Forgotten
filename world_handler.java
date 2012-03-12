import java.util.*;
import java.awt.*;

public class world_handler
{
    Image bg_image;
    
    content_handler content;
    
    int border = 100;
    int change_x, change_y, char_x, char_y, char_w, char_h, window_h, window_w;
    
    boolean is_over = false;

    public world_handler(String bg_img, content_handler ch)
    {
        bg_image = Toolkit.getDefaultToolkit().getImage(bg_img);
        content = ch;
    }
        
    public void set_params(int ww, int wh) {
        window_w = ww;
        window_h = wh;
    }
    
    public void calc_movement() {
        if (content.get_characters().size() == 0) {
            return;
        }
        Char cur = content.get_characters().get(0);
        
        change_x = 0;
        change_y = 0;
        
        char_x = (int)cur.get_x();
        char_y = (int)cur.get_y();
        char_w = (int)cur.get_width();
        char_h = (int)cur.get_height();
        
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
    }
    
    public Image get_bg_image() {
        return bg_image;
    }
}