import java.util.*;
import java.awt.*;

public class fight_handler
{
    Image bg_image;
    content_handler content;

    ArrayList<ArrayList> field;
    
    boolean is_over = false;
    
    int field_width = 10;
    int field_height = 10;
    
    int place_width = 100;
    int place_height = 100;
    
    @SuppressWarnings("unchecked")
    public fight_handler(String bg_img, content_handler con)
    {
        content = con;
        bg_image = Toolkit.getDefaultToolkit().getImage(bg_img);
        
        // Create fighting place
        field = new ArrayList<ArrayList>();
        for (int i = 0; i < field_width ; i++) {
            field.add(new ArrayList<Place>());
        }
        int ind = 0;
        for (int i = 0 ; i < field.size() ; i++) {
            for (int o = 0 ; o < field_height ; o++) {
                field.get(i).add(new Place(null, ind));
                ind++;
            }
        }
    }
    
    public void place_char(Character c, int place) {
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                p.cur = c;
            }
        }
    }
    
    public void draw_stuff(Graphics g2, draw_anything imo) {
        Graphics2D g = (Graphics2D)g2;
        ArrayList<Place> checked = new ArrayList<Place>();
        
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                
                int x = (p.index % field_width) * place_width;
                int y = (int)Math.floor(p.index / field_height) * place_height;
                
                //System.out.println(p.index + " at: " + x + " | " + y);
                g.setStroke(new BasicStroke(4));
                g.setColor(Color.black);
                if (p.checked) {
                    checked.add(p);
                }
                g.drawRect(x, y, place_width, place_height);
            }
        }
        for (Object o : checked) {
            Place p = (Place) o;
            
            int x = (p.index % field_width) * place_width;
            int y = (int)Math.floor(p.index / field_height) * place_height;
            
            g.setStroke(new BasicStroke(5));
            g.setColor(Color.red);
            
            g.drawRect(x, y, place_width, place_height);
        }
    }
    
    public void on_click(String button) {
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                Rectangle r = new Rectangle((p.index % field_width) * place_width, (int)Math.floor(p.index / field_height) * place_height, place_width, place_height);
                
                if(r.contains(content.mouse_x, content.mouse_y)) {
                    p.checked = !p.checked;
                }
            }
        }
    }
}
