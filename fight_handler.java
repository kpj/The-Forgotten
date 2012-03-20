import java.util.*;
import java.awt.*;
import javax.swing.*;

public class fight_handler
{
    Image bg_image;
    
    content_handler content;

    ArrayList<ArrayList> field;
    
    boolean is_over = false;
    
    // field
    int field_width;
    int field_height;
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
    
    // dragging
    int drag_x = 0;
    int drag_y = 0;
    int drag_start_x = 0;
    int drag_start_y = 0;
    int move_x = 0; // CHANGE HERE
    int move_y = 0; // CHANGE HERE
    int move_x_tmp = move_x;
    int move_y_tmp = move_y;
    int old_move_x = move_x;
    int old_move_y = move_y;
    
    // move 'em
    boolean unit_selected = false;
    boolean multiple_units_selected = false;
    
    boolean show_move_radius = true;
    ArrayList<Place> in_reach = new ArrayList<Place>();
    boolean can_modify_list = true;
    
    ArrayList<Place> checked;
    ArrayList<Place> unchecked;
    
    @SuppressWarnings("unchecked")
    public fight_handler(String bg_img, int field_w, int field_h, HashMap<Integer, Char> to_insert, content_handler con)
    {
        content = con;
        
        
        bg_image = Toolkit.getDefaultToolkit().getImage(bg_img);
        field_width = field_w;
        field_height = field_h;
        
        // Create fighting place
        field = new ArrayList<ArrayList>();
        //System.out.println(field_width+"x"+field_height);
        
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
        
        // Adding chars
        for (Map.Entry<Integer, Char> o : to_insert.entrySet()) {
            place_char2(o.getKey(), o.getValue());
        }
    }
    
    public void place_char(Place p, Char c) {
        p.cur = c;
    }
    public void place_char2(int pos, Char c) {
        if (c == null) {
            return;
        }
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                if (p.index == pos)
                    p.cur = c;
            }
        }
    }
    
    public void draw_stuff(Graphics g3, draw_anything imo) {
        // For stroke thickness
        Graphics2D g = (Graphics2D)g3;
        
        // draw bg
        g.drawImage(bg_image, 0 , 0, content.window_width, content.window_height, -move_x, -move_y, -move_x+content.window_width, -move_y+content.window_height, imo);
        
        // draw field
        checked = new ArrayList<Place>();
        unchecked = new ArrayList<Place>();
        
        // draw boxes + characters
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                
                int x = calc_offset(p.index).get(0);
                int y = calc_offset(p.index).get(1);
                
                //System.out.println(p.index + " at: " + x + " | " + y);
                g.setStroke(new BasicStroke(4));
                g.setColor(Color.black);
                if (p.checked) {
                    checked.add(p);
                }
                g.drawRect(x, y, place_width, place_height);
                g.drawString(""+p.index,x+15,y+15);
                
                // now characters + equipped items
                if (p.cur != null) {
                    g.drawImage(p.cur.fight_image, x, y, imo);
                
                    if (p.cur.did_something_this_round) {
                        g.drawString("NO MOVES LEFT",x ,y+10);
                    }
                    
                    for (Object obj : p.cur.get_equipped_items()) {
                        Item i = (Item)obj;
                        
                        g.drawImage(i.equipped_image, x, y, imo);
                    }
                }
            }
        }
        
        // draw checked
        for (Object o : checked) {
            draw_place(g, (Place)o, 4, Color.red);
        }
        
        // draw reachable
        Color reach_col = Color.red;
        if (show_move_radius) {
            reach_col = Color.green;
        }
        if (can_modify_list) {
            for (Object o : in_reach) {
                draw_place(g, (Place)o, 4, reach_col);
            }
        }
        
        // draw selector
        if(selecting) {
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.blue);
            g.drawRect(select_x_tmp, select_y_tmp, select_width, select_height);
        }
    }
    public void draw_place(Graphics2D g, Place p, int thickness, Color col) {
        int x = calc_offset(p.index).get(0);
        int y = calc_offset(p.index).get(1);
        
        g.setStroke(new BasicStroke(5));
        g.setColor(col);
        
        g.drawRect(x, y, place_width, place_height); 
    }
    public ArrayList<Integer> calc_offset(int index) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        
        int x = (index % field_width) * (place_width + spacing) + move_x;
        int y = (int)Math.floor(index / field_height) * (place_height + spacing) + move_y;
        
        ret.add(x);
        ret.add(y);
        return ret;
    }
    public ArrayList<Place> get_bordering_places(Place p) {
        ArrayList<Place> border = new ArrayList<Place>();
        int pos = p.index;
        
        if (pos >= field_width) {
            border.add(get_place(pos - field_width));
        }
        if (pos % field_width != field_width - 1) {
            border.add(get_place(pos + 1));
        }
        if (pos % field_width != 0) {
            border.add(get_place(pos - 1));
        }
        if (pos < field_width * field_height - field_width) {
            border.add(get_place(pos + field_width));
        }
        
        return border;
    }
    public Place get_place(int pos) {
        for (Object o : field) {
            ArrayList l = (ArrayList)o;
            for (Object ob : l) {
                if (pos == ((Place)ob).index) {
                    return (Place)ob;
                }
            }
        }
        return null;
    }
    
    public int get_distance(Place p1, Place p2) {
        int xi1 = p1.index % field_width;
        int yi1 = (int)Math.floor(p1.index / field_height);
        int xi2 = p2.index % field_width;
        int yi2 = (int)Math.floor(p2.index / field_height);
        return Math.abs(xi1-xi2)+Math.abs(yi1-yi2);
    }
    
    public void move_char(Place from, Place to) {
        if (from.cur.did_something_this_round) {
            System.out.println("This character can only act in the next round.");
            return;
        }
        from.cur.did_something_this_round = true;
        boolean was_fighting = false;
        boolean successful_combat = false;
        if (to.cur != null) {
            if (from.cur.team == to.cur.team) {
                // Won't interact with same team
                from.cur.did_something_this_round = false;
                return;
            }
            else {
                // Is from different team
                if (show_move_radius) {
                    // Cannot move to used field
                    from.cur.did_something_this_round = false;
                    return;
                }
                // Attack used field
                was_fighting = true;
                successful_combat = attack_char(from, to);
            }
        }
        if (!was_fighting) {
            if (!show_move_radius) {
                from.cur.did_something_this_round = false;
                return;
            }
            to.cur = from.cur;
            from.cur = null;
        }
        else {
            if (successful_combat) {
                //to.cur = from.cur;
                //from.cur = null;
                to.cur = null;
            }
        }
    }
    public boolean attack_char(Place attacker, Place defender) {
        // very simple system
        int att = attacker.cur.property_current.get("angriffskraft");
        int def = defender.cur.property_current.get("verteidigungspunkte");
        
        if (att > def) {
            defender.cur.deal_damage(att - def);
        }
        
        System.out.println(attacker.cur.name + " dealt " + (att-def) + " damage to " + defender.cur.name);
        System.out.println(defender.cur.name + " has now " + defender.cur.property_current.get("lebenspunkte") + " life points");
        if (defender.cur.property_current.get("lebenspunkte") <= 0)
            return true;
        return false;
    }
    public void next_round() {
        System.out.println("Let the next round begin!");
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                if (p.cur != null) {
                    p.cur.did_something_this_round = false;
                }
            }
        }
    }
    
    public void compute_selection() {
        ArrayList<Place> sel = checked;
        if (sel == null)
            return;
        if (sel.size() == 0) {
            
        }
        else {
            for (Object o : sel) {
                Place p = (Place)o;
                
                // detect reach
                if (p.cur != null) {
                    if (sel.size() == 1) {
                        unit_selected = true;
                        content.current_selected = p;
                    }
                    else {
                        multiple_units_selected = true;
                    }
                    in_reach = new ArrayList<Place>();
                    in_reach.add(p); // to know, who is in the center
                    
                    String imp_fac = "attackenreichweite";
                    if (show_move_radius) {
                        imp_fac = "geschwindigkeit";
                    }
                    
                    int max_dist = (int)Math.floor(p.cur.property_current.get(imp_fac).intValue()/2);

                    ArrayList<Place> working_p = new ArrayList<Place>();
                    ArrayList<Integer> working_i = new ArrayList<Integer>();
                    
                    working_p.add(p);
                    working_i.add(0);
                    
                    can_modify_list = false;
                    while (working_p.size() > 0) {
                        Place current_p = working_p.get(0);
                        int current_i = working_i.get(0);
                        
                        for (Object oOo : get_bordering_places(current_p)) {
                            Place pl = (Place)oOo;
                            
                            if (current_i < max_dist && pl.cur == null) {
                                //System.out.println(current_p.index+": "+pl.index);
                                working_p.add(pl);
                                working_i.add(working_i.get(0) + 1);
                            }
                        }
                        //System.out.println(current_p.index+": "+current_i + "/"+max_dist +"  ("+working_p.size()+")");
                        
                        in_reach.add(current_p);
                        working_p.remove(0);
                        working_i.remove(0);
                    }
                    can_modify_list = true;
                    p.checked = false;
                }
            }
        }
    }
    public HashMap<Place, Integer> make_hashmap(Place p, int i) {
        HashMap<Place, Integer> ret = new HashMap<Place, Integer>();
        ret.put(p, i);
        return ret;
    }
    
    public void on_click(String button) {
        if (button.equals("LEFT")) {
            if (!unit_selected) {
                for (Object o : field) {
                    ArrayList l = (ArrayList) o;
                    for (Object ob : l) {
                        Place p = (Place) ob;
                        Rectangle r = new Rectangle(calc_offset(p.index).get(0), calc_offset(p.index).get(1), place_width, place_height);
                        
                        if(r.contains(content.mouse_x, content.mouse_y)) {
                            p.checked = !p.checked;
                        }
                    }
                }
            }
            else if (unit_selected){
                for (Object o : field) {
                    ArrayList l = (ArrayList) o;
                    for (Object ob : l) {
                        Place p = (Place) ob;
                        Rectangle r = new Rectangle(calc_offset(p.index).get(0), calc_offset(p.index).get(1), place_width, place_height);
                        
                        if(r.contains(content.mouse_x, content.mouse_y)) {
                            if(in_reach.contains(p)) {
                                move_char(in_reach.get(0), p);
                            }
                        }
                    }
                }
                
                unit_selected= false;
                content.current_selected = null;
                multiple_units_selected = false;
                in_reach.clear();
            }
        }
        else if (button.equals("RIGHT")) {
            if (content.current_selected == null)
                return;
            
            show_move_radius = !show_move_radius;
            if (in_reach.size() != 0) {
                // Little trick, makes a new calculation
                in_reach.get(0).checked = true;
            }
        }
    }
    public void on_drag(String button) {
        if (button.equals("LEFT")) {
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
        else if(button.equals("MIDDLE")) {
            move_x_tmp = (content.mouse_x - drag_start_x);
            move_y_tmp = (content.mouse_y - drag_start_y);
            move_x = move_x_tmp + old_move_x;
            move_y = move_y_tmp + old_move_y;
        }
    }
    public void on_press(String button) {
        if (button.equals("LEFT")) {
            selecting = true;
            select_x = content.mouse_x;
            select_y = content.mouse_y;
            select_x_tmp = select_x;
            select_y_tmp = select_y;
        }
        else if (button.equals("MIDDLE")) {
            drag_start_x = content.mouse_x;
            drag_start_y = content.mouse_y;
        }
    }
    public void on_release(String button) {
        if (button.equals("LEFT")) {
            selecting = false;
        
            Rectangle selection_rect = new Rectangle(select_x_tmp, select_y_tmp, select_width, select_height);
            for (Object o : field) {
                ArrayList l = (ArrayList) o;
                for (Object ob : l) {
                    Place p = (Place) ob;
                    Rectangle r = new Rectangle(calc_offset(p.index).get(0), calc_offset(p.index).get(1), place_width, place_height);
                
                    if(selection_rect.intersects(r)) {
                        p.checked = !p.checked;
                    }
                }
            }
        
            select_width = 0;
            select_height = 0;
        }
        else if(button.equals("MIDDLE")) {
            old_move_x = move_x;
            old_move_y = move_y;
        }
    }
    
    public void on_key(Character key) {
        switch (key) {
            case 'n': {
                next_round();
                break;
            }
        }
    }
}
