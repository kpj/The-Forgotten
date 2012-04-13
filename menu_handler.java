import java.util.*;
import java.awt.*;

public class menu_handler
{
    content_handler content;
    
    int x, y, x2, y2;
    
    int menu_height = 170;
    int menu_height_changer = 0;

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
            }
            catch (java.lang.NullPointerException e) {
                return;
            }
        }
    }
}
