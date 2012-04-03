import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;

public class fight_handler
{
    String bg_image, non_walkable_image;
    
    content_handler content;
    
    boolean online;
    Client client;
    boolean loading_field = false;
    HashMap<Integer, Place> changes = new HashMap<Integer, Place>();
    
    boolean is_over = false;
    
    int team = -1;
    
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
    boolean can_modify = true;
    java.util.List<Place> in_reach = Collections.synchronizedList(new ArrayList<Place>());
    
    ArrayList<Place> checked;
    ArrayList<Place> unchecked;
    
    ArrayList<Place> updated_changes = new ArrayList<Place>();
    
    @SuppressWarnings("unchecked")
    public fight_handler(String path2fight, content_handler con, boolean on)
    {
        content = con;
        online = on;
        
        if (!online) {
            Map_parser mapper = new Map_parser(path2fight);
        
            bg_image = mapper.path2bg;

            non_walkable_image = "pics/non_walkable.png";
            
            field_width = mapper.field_width;
            field_height = mapper.field_height;
            
            content.field = mapper.get_field();
        }
        else {
            try {
                client = new Client(new Socket(content.ip, content.port), content, -1);
            }
            catch (IOException e) {
                System.out.println("error: "+e);
                //e.printStackTrace();
                System.exit(-1);
            }
            System.out.println("Connected to \""+content.ip+":"+content.port+"\"");
   
            
            Data_packet cur = client.get_existing_data();
            client.num = cur.num;
            
            System.out.println("Acknowledged by server");
            content.my_turn = cur.my_turn;
            team = cur.num;
            
            field_width = cur.field_width;
            field_height = cur.field_height;
            bg_image = cur.path2bg;
            non_walkable_image = "pics/non_walkable.png";
            
            loading_field = true;
            content.field = new ArrayList<ArrayList>(cur.field);
            loading_field = false;
            content.notification.add_noti("Updated field");
            
            
            Data_getter fg = new Data_getter(this, content);
            fg.start();
        }
        
        // Generating char pics
        for (int i = 0 ; i <= field_width * field_height - 1 ; i++) {
            Char c = get_place(i).cur;
            if (c != null)
                content.iml.add_img(c.name+"_fight_image", new Image_parser(c.fight_image).get_img());
        }
        
        content.fight_starting = false;
        //System.out.println(field_width+"x"+field_height);
    }
    
    public void place_char(Place p, Char c) {
        p.cur = c;
    }
    public void place_char2(int pos, Char c) {
        for (Object o : content.field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                if (p.index == pos) {
                    p.cur = c;
                    //System.out.println("Set "+p.index+"'s char to "+c);
                }
            }
        }
    }
    public void set_place(int pos, Place p) {
        for (Object o : content.field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place pl = (Place) ob;
                if (pl.index == pos)
                    pl = p;
            }
        }
    }
    
    
    public void draw_stuff(Graphics g3, draw_anything imo) {
        // For stroke thickness
        Graphics2D g = (Graphics2D)g3;
        
        // draw bg
        g.drawImage(content.iml.pimg.get(bg_image), 0 , 0, content.window_width, content.window_height, -move_x, -move_y, -move_x+content.window_width, -move_y+content.window_height, imo);
        
        // draw field
        checked = new ArrayList<Place>();
        unchecked = new ArrayList<Place>();
        
        // draw boxes + characters
        if (!loading_field) {
            for (Object o : content.field) {
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
                    //g.drawString(""+p.index,x+15,y+15);
                    
                    // now characters + equipped items
                    if (p.cur != null) {
                        g.drawImage(content.iml.get_img(p.cur.name+"_fight_image"), x, y, imo);
                    
                        if (p.cur.did_fight) {
                            g.drawString("FOUGHT",x ,y+10);
                        }
                        if (p.cur.did_walk) {
                            g.drawString("WALKED",x ,y+30);
                        }
                        
                        for (Object obj : p.cur.get_equipped_items()) {
                            Item i = (Item)obj;
                            
                            g.drawImage(content.iml.pimg.get(i.equipped_image), x, y, imo);
                        }
                    }
                    else if (p.special.equals("NON-WALKABLE")) {
                        g.drawImage(content.iml.pimg.get(non_walkable_image), x, y, imo);
                    }
                }
            }
        }
        
                
        // draw lately updated
        if (online) {
            for (Object o : updated_changes) {
                draw_place(g, (Place)o, 1, Color.pink);
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
        if (can_modify) {
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
        for (Object o : content.field) {
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
        if (!(from.cur.team == team || team == -1)) {
            content.notification.add_noti("This character is not in your team");
            return;
        }
        if (!content.my_turn) {
            content.notification.add_noti("It is not your turn");
            return;
        }
        
        boolean was_fighting = false;
        boolean successful_combat = false;
        if (to.cur != null) {
            if (from.cur.team == to.cur.team) {
                // Won't interact with same team
                return;
            }
            else {
                // Is from different team
                if (show_move_radius) {
                    // Cannot move to used field
                    return;
                }
                if (from.cur.did_fight) {
                    content.notification.add_noti("Cannot fight in this round anymore");
                    return;
                }
                // Attack used field
                was_fighting = true;
                successful_combat = attack_char(from, to);
                from.cur.did_fight = true;
            }
        }
        if (!was_fighting) {
            if (!show_move_radius) {
                return;
            }
            if (from.cur.did_walk) {
                content.notification.add_noti("Cannot walk in this round anymore");
                return;
            }
            from.cur.did_walk = true;
            to.cur = from.cur;
            from.cur = null;
            
            changes.put(to.index, to);
            changes.put(from.index, from);
        }
        else {
            if (successful_combat) {
                //to.cur = from.cur;
                //from.cur = null;
                to.cur = null;
            }
            changes.put(to.index, to);
            changes.put(from.index, from);
        }
    }
    public boolean attack_char(Place attacker, Place defender) {
        // very simple system
        float high = 1 + (float)attacker.cur.property_current.get("attackenmodifikator")/(float)100;
        float low = 1 - (float)attacker.cur.property_current.get("attackenmodifikator")/(float)100;
        if (low < 0) low = 0;
        float att_mod = ((float)Math.random() * (high - low) + low);
        
        int att = (int)Math.round(attacker.cur.property_current.get("angriffskraft") * att_mod);
        int def = defender.cur.property_current.get("verteidigungspunkte");
        
        if (att > def) {
            defender.cur.deal_damage(att - def);
        }
        
        content.notification.add_noti(attacker.cur.name + " dealt " + (att-def) + " damage to " + defender.cur.name);
        content.notification.add_noti(defender.cur.name + " has now " + defender.cur.property_current.get("lebenspunkte") + " life points");
        if (defender.cur.property_current.get("lebenspunkte") <= 0)
            return true;
        return false;
    }
    public void next_round() {
        if (!content.my_turn) {
            content.notification.add_noti("It is not your turn");
            return;
        }
        
        content.my_turn = false;
        
        content.notification.add_noti("Let the next round begin!");
        
        for (Object o : content.field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                if (p.cur != null) {
                    p.cur.did_walk = false;
                    p.cur.did_fight = false;
                }
            }
        }
        
        if (online) {
            // Do online stuff
            client.send_data(new Data_packet(changes, true, client.num));
            //apply_changes(changes);
            changes.clear();
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
                        return;
                    }
                    
                    can_modify = false;
                    in_reach = Collections.synchronizedList(new ArrayList<Place>());
                    in_reach.add(p); // to know, who is in the center
                    
                    String imp_fac = "attackenreichweite";
                    if (show_move_radius) {
                        imp_fac = "geschwindigkeit";
                    }
                    
                    int max_dist = (int)Math.floor((p.cur.property_current.get(imp_fac)/100));

                    ArrayList<Place> working_p = new ArrayList<Place>();
                    ArrayList<Integer> working_i = new ArrayList<Integer>();
                    
                    working_p.add(p);
                    working_i.add(0);
                    
                    can_modify = false;
                    while (working_p.size() > 0) {
                        Place current_p = working_p.get(0);
                        int current_i = working_i.get(0);
                        
                        for (Object oOo : get_bordering_places(current_p)) {
                            Place pl = (Place)oOo;
                            
                            if (show_move_radius) {
                                if (current_i < max_dist && pl.cur == null && !pl.special.equals("NON-WALKABLE")) {
                                    //System.out.println(current_p.index+": "+pl.index);
                                    working_p.add(pl);
                                    working_i.add(working_i.get(0) + 1);
                                }
                            }
                            else {
                                if (pl.cur == null) {
                                    if (current_i < max_dist && !pl.special.equals("NON-WALKABLE")) {
                                        //System.out.println(current_p.index+": "+pl.index);
                                        working_p.add(pl);
                                        working_i.add(working_i.get(0) + 1);
                                    }
                                }
                                else if (!in_reach.isEmpty() && (pl.cur.team != in_reach.get(0).cur.team || pl.cur.team == in_reach.get(0).cur.team)) {
                                    if (current_i < max_dist && !pl.special.equals("NON-WALKABLE")) {
                                        //System.out.println(current_p.index+": "+pl.index);
                                        working_p.add(pl);
                                        working_i.add(working_i.get(0) + 1);
                                    }
                                }
                            }
                        }
                        //System.out.println(current_p.index+": "+current_i + "/"+max_dist +"  ("+working_p.size()+")");
                        
                        in_reach.add(current_p);
                        working_p.remove(0);
                        working_i.remove(0);
                    }
                    can_modify = true;
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
    
    public void do_online_stuff() {
        
    }
    
    public void on_click(String button) {
        if (button.equals("LEFT")) {
            if (!unit_selected) {
                for (Object o : content.field) {
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
                for (Object o : content.field) {
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
            if (!loading_field) {
                for (Object o : content.field) {
                    ArrayList l = (ArrayList) o;
                    for (Object ob : l) {
                        Place p = (Place) ob;
                        Rectangle r = new Rectangle(calc_offset(p.index).get(0), calc_offset(p.index).get(1), place_width, place_height);
                    
                        if(selection_rect.intersects(r)) {
                            p.checked = !p.checked;
                        }
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
    
    public void apply_changes(HashMap<Integer, Place> ch) {
        updated_changes.clear();
        loading_field = true;
        for (Map.Entry<Integer, Place> ob : ch.entrySet()) {
            int i = ob.getKey();
            Place p = ob.getValue();
            //System.out.println("Detected change on "+i+" ("+p.cur+")");
            place_char2(i, p.cur);
            updated_changes.add(p);
        }
        loading_field = false;
    }
    
    @SuppressWarnings("unchecked")
    public class Data_getter extends Thread {
        content_handler content;
        fight_handler parent;
        public Data_getter(fight_handler par, content_handler con) {
            content = con;
            parent = par;
        }
        
        public void run() {
            while (true) {
                Data_packet cur = parent.client.get_existing_data();
            
                System.out.println("Received data");
                
                parent.apply_changes(cur.changes);
                
                content.my_turn = cur.my_turn;
                
                content.notification.add_noti((content.my_turn)?"It is your turn":"Wait for other players");
            }
        }
    }
}
