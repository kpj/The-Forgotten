import java.util.*;
import java.awt.*;

public class menu_handler
{
    content_handler content;
    
    int x, y, x2, y2;
    
    int menu_height = 170;
    int menu_height_changer = 0;

    int face_pos_x = 45, face_pos_y = 8;
    int face_width = 13, face_height = 13;
    
    public menu_handler(content_handler con)
    {
        content = con;
        calc_edges();
    }
    
    public void calc_edges() {
        x = 0;
        y = content.window_height - menu_height;
        if (content.window_height - menu_height <= 20) {
            y = (int)content.window_height/4;
            menu_height_changer = content.window_height - y - menu_height;
        }
        else {
            menu_height_changer = 0;
        }
        
        x2 = content.window_width;
        y2 = content.window_height;
    }
    
    public void draw_stuff(Graphics g3, draw_anything imo) {
        calc_edges();
        
        Graphics2D g = (Graphics2D)g3;
        
        g.setColor(Color.darkGray);
        g.fillRect(x, y, content.window_width, menu_height + menu_height_changer);
        g.setColor(Color.black);
        
        g.drawString((content.my_turn)?"Press \"n\" for next character!":"Wait for other players", x2 - 200, y + 15);
        
        if (content.current_selected == null) {
            g.drawString("No character selected.", x + 5, y + 15);
        }
        else {
            Char c;
            try {
                c = content.current_selected.cur;
    
                g.drawString(c.name, x + 5, y + 15);
                
                int upper = 0;
                for (Map.Entry<String, Integer> ob : c.property_current.entrySet()) {
                    g.drawString(ob.getKey() + " : " + ob.getValue(), x + 100, y + 40 + upper);
                    upper += 12;
                }
                
                upper = 0;
                for (Object o : c.items) {
                    Item i = (Item)o;
                    g.drawString(i.name + " (" + ((i.is_in_use)?"Equipped":"Not equipped") + ")", x + 300, y + 40 + upper);
                    upper += 12;
                }
                
                draw_bar(g, c, "lebenspunkte", Color.red, x+5, y+27);
            }
            catch (java.lang.NullPointerException e) {
                return;
            }
        }
        
        try {
            int ini_counter = 0;
            for (Char c : content.ini_table) {
                g.drawImage(content.iml.get_img(c.name+"_fight_image"),
                    0 + ini_counter, y2 - face_height*2, 
                    ini_counter + face_width*2, y2, 
                    face_pos_x, face_pos_y, 
                    face_pos_x + face_width, face_pos_y + face_height, 
                imo);
                ini_counter += face_width*2 + 3;
            }
        }
        catch (java.util.ConcurrentModificationException e) {}
    }
    
    int bar_width = 15, bar_height = 100;
    public void draw_bar(Graphics2D g, Char c, String type, Color col, int x, int y) {
        float curv = (float)c.property_current.get(type);
        float maxv = (float)c.property_max.get(type);
        
        float perc_val = 1/maxv * curv;

        g.setColor(col);
        g.setStroke(new BasicStroke(2));
        
        g.drawRect(x, y, bar_width, bar_height);
        g.fillRect(x, y, bar_width, (int)(bar_height * perc_val));
    }
}
