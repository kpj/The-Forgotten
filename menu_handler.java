import java.util.*;
import java.awt.*;

public class menu_handler
{
    content_handler content;
    
    int x, y;
    
    int menu_height = 170;

    public menu_handler(content_handler con)
    {
        content = con;
        calc_edges();
    }
    
    public void calc_edges() {
        x = 0;
        y = content.window_height - menu_height;
    }
    
    public void draw_stuff(Graphics g3, draw_anything imo) {
        Graphics2D g = (Graphics2D)g3;
        
        g.setColor(Color.darkGray);
        g.fillRect(x, y, content.window_width, menu_height);
        g.setColor(Color.black);
        
        g.drawString("Press \"n\" for next round!", x + 700, y + 15);
        
        if (content.current_selected == null) {
            g.drawString("No character selected.", x + 5, y + 15);
        }
        else {
            Char c = content.current_selected.cur;
            g.drawString(c.name, x + 5, y + 15);
            
            int upper = 0;
            for (Map.Entry<String, Integer> ob : c.property_current.entrySet()) {
                g.drawString(ob.getKey() + " : " + ob.getValue(), x + 100, y + 50 + upper);
                upper += 12;
            }
        }
    }
}
