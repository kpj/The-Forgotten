import java.util.*;
import java.awt.*;

public class fight_handler
{
    Image bg_image;
    content_handler content;

    ArrayList<ArrayList> field;
    
    boolean is_over = false;
    
    // field
    int field_width = 100;
    int field_height = 100;
    int place_width = 100;
    int place_height = 100;
    int spacing = 3;
    
    // selector
    boolean selecting = false;
    int select_x = 0;
    int select_y = 0;
    int select_x_tmp = 0;
    int select_y_tmp = 0;
    int select_width = 0;
    int select_height = 0;
    
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
        
        place_char((Place)field.get(2).get(2), content.characters.get(0));
        place_char((Place)field.get(1).get(2), content.characters.get(1));
        place_char((Place)field.get(2).get(1), content.characters.get(2));
        place_char((Place)field.get(3).get(1), content.characters.get(3));
    }
    
    public void place_char(Place p, Char c) {
        System.out.println("Placed one char");
        p.cur = c;
    }
    
    public void draw_stuff(Graphics g3, draw_anything imo) {
        // For stroke thickness
        Graphics2D g = (Graphics2D)g3;
        
        // draw field
        ArrayList<Place> checked = new ArrayList<Place>();
        ArrayList<Place> unchecked = new ArrayList<Place>();
        
        // draw boxes + characters
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                
                int x = (p.index % field_width) * (place_width + spacing);
                int y = (int)Math.floor(p.index / field_height) * (place_height + spacing);
                
                //System.out.println(p.index + " at: " + x + " | " + y);
                g.setStroke(new BasicStroke(4));
                g.setColor(Color.black);
                if (p.checked) {
                    checked.add(p);
                }
                g.drawRect(x, y, place_width, place_height);
                
                // now characters
                if (p.cur != null) {
                    g.drawImage(p.cur.fight_image, x, y, (int)p.cur.fight_image_width, (int)p.cur.fight_image_height, imo);
                }
            }
        }
        
        // draw checked
        for (Object o : checked) {
            Place p = (Place) o;
            
            int x = (p.index % field_width) * (place_width + spacing);
            int y = (int)Math.floor(p.index / field_height) * (place_height + spacing);
            
            g.setStroke(new BasicStroke(5));
            g.setColor(Color.red);
            
            g.drawRect(x, y, place_width, place_height);
        }
        
        // draw selector
        if(selecting) {
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.blue);
            g.drawRect(select_x_tmp, select_y_tmp, select_width, select_height);
        }
        
        compute_selection(checked);
    }
    
    public void compute_selection(ArrayList<Place> sel) {
        //System.out.println(sel.size());
    }
    
    public void on_click(String button) {
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                Rectangle r = new Rectangle((p.index % field_width) * (place_width + spacing), (int)Math.floor(p.index / field_height) * (place_height + spacing), place_width, place_height);
                
                if(r.contains(content.mouse_x, content.mouse_y)) {
                    p.checked = !p.checked;
                }
            }
        }
    }
    public void on_drag(String button) {
        if (content.mouse_x - select_x > 0 && content.mouse_y - select_y > 0) {
            select_width = content.mouse_x - select_x;
            select_height = content.mouse_y - select_y;
            select_x_tmp = select_x;
            select_y_tmp = select_y;
        }
        else if (content.mouse_x - select_x < 0 && content.mouse_y - select_y < 0) {
            select_x_tmp = content.mouse_x;
            select_y_tmp = content.mouse_y;
            select_width = select_x - content.mouse_x;
            select_height = select_y - content.mouse_y;
        }
        else if (content.mouse_x - select_x > 0 && content.mouse_y - select_y < 0) {
            select_width = content.mouse_x - select_x;
            select_x_tmp = select_x;
            select_y_tmp = content.mouse_y;
            select_height = select_y - content.mouse_y;
        }
        else if (content.mouse_x - select_x < 0 && content.mouse_y - select_y > 0) {
            select_x_tmp = content.mouse_x;
            select_width = select_x - content.mouse_x;
            select_y_tmp = select_y;
            select_height = content.mouse_y - select_y;
        }
    }
    public void on_press(String button) {
        selecting = true;
        select_x = content.mouse_x;
        select_y = content.mouse_y;
        select_x_tmp = select_x;
        select_y_tmp = select_y;
    }
    public void on_release(String button) {
        selecting = false;
        
        Rectangle selection_rect = new Rectangle(select_x_tmp, select_y_tmp, select_width, select_height);
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                Rectangle r = new Rectangle((p.index % field_width) * (place_width + spacing), (int)Math.floor(p.index / field_height) * (place_height + spacing), place_width, place_height);
                
                if(selection_rect.intersects(r)) {
                    p.checked = !p.checked;
                }
            }
        }
        
        select_width = 0;
        select_height = 0;
    }
}
