import java.util.*;
import java.awt.*;

public class fight_handler
{
    Image bg_image;

    ArrayList<ArrayList> field;
    
    boolean is_over = false;
    
    int field_width = 10;
    int field_height = 10;
    
    int place_width = 50;
    int place_height = 50;
    
    @SuppressWarnings("unchecked")
    public fight_handler(String bg_img, content_handler con)
    {
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
    
    public void draw_stuff(Graphics g, draw_anything imo) {
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                
                int x = (p.index % field_width) * place_width;
                int y = (int)Math.floor(p.index / field_height) * place_height;
                
                System.out.println(p.index + " at: " + x + " | " + y);
                g.drawRect(x, y, place_width, place_height);
            }
        }
    }
}
