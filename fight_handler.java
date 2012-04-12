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
    boolean new_round_sending = false;
    
    int team = 1;
    boolean my_tmp_turn = true;
    
    // field
    float scale = 0;
    int scroll_speed = 2;
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
    float animation_speed = 15; // high is slow
    
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
            
            System.out.println("Acknowledged by server as #" + client.num);
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
            
            
            Data_getter dg = new Data_getter(this, content);
            dg.start();
            Data_setter ds = new Data_setter(this, content);
            ds.start();
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
                    place_width = Math.round( 100 + scale );
                    place_height = Math.round( 100 + scale );
                    
                    //System.out.println(p.index + " at: " + x + " | " + y);
                    g.setStroke(new BasicStroke(4));
                    g.setColor(Color.black);
                    if (p.checked) {
                        checked.add(p);
                    }
                    //g.drawRect(x, y, place_width, place_height);
                    //g.drawString(""+p.index,x+15,y+15);
                    
                    // now characters + equipped items
                    try {
                        if (p.cur != null && p.cur.visible) {
                            g.drawImage(content.iml.get_img(p.cur.name+"_fight_image"), x, y, place_width, place_height, imo);
                        
                            if (p.cur.did_fight) {
                                g.drawImage(content.iml.get_img("pics/Icons/Geschlagen.png"), x, y, place_width, place_height, imo);
                            }
                            if (p.cur.did_walk) {
                                g.drawImage(content.iml.get_img("pics/Icons/Gegangen.png"), x, y, place_width, place_height, imo);
                            }
                            
                            for (Object obj : p.cur.get_equipped_items()) {
                                Item i = (Item)obj;
                                
                                g.drawImage(content.iml.pimg.get(i.equipped_image), x, y, place_width, place_height, imo);
                            }
                        }
                        else if (p.special.equals("NON-WALKABLE")) {
                            g.drawImage(content.iml.pimg.get(non_walkable_image), x, y, place_width, place_height, imo);
                        }
                    }
                    catch (NullPointerException e ) {}
                }
            }
        }
        
            
        // special movement
        if (!special_animation.isEmpty()) {
            for (int ik = 0; ik < way.size() ; ik++) { 
                if (special_animation.get(ik)) {
                    int x = (int)from_special.get(ik).getX() + (int)special.get(ik).getX() + move_x;
                    int y = (int)from_special.get(ik).getY() + (int)special.get(ik).getY() + move_y;
                    
                    g.drawImage(content.iml.get_img(special_char.get(ik).name+"_fight_image"), x, y, place_width, place_height, imo);
                    for (Object obj : special_char.get(ik).get_equipped_items()) {
                        Item i = (Item)obj;
                        
                        g.drawImage(content.iml.pimg.get(i.equipped_image), x, y, place_width, place_height, imo);
                    }
                    
                    special.get(ik).translate((int)special_delta.get(ik).getX(), (int)special_delta.get(ik).getY());
                    
                    
                    if ( special_delta.get(ik).getX() < 0 && special_delta.get(ik).getY() < 0 ) {
                        if (x < to_special.get(ik).getX() + move_x && y < to_special.get(ik).getY() + move_y) {
                            special_animation.set(ik, false);  
                            animate_way(special_start.get(ik), special_aim.get(ik), special_i.get(ik)+1, ik);
                        }
                    }
                    else if ( special_delta.get(ik).getX() > 0 && special_delta.get(ik).getY() > 0 ) {
                        if (x > to_special.get(ik).getX() + move_x && y > to_special.get(ik).getY() + move_y) {
                            special_animation.set(ik, false); 
                            animate_way(special_start.get(ik), special_aim.get(ik), special_i.get(ik)+1, ik);
                        }
                    }
                    else if ( special_delta.get(ik).getX() > 0 && special_delta.get(ik).getY() < 0 ) {
                        if (x > to_special.get(ik).getX() + move_x && y < to_special.get(ik).getY() + move_y) {
                            special_animation.set(ik, false); 
                            animate_way(special_start.get(ik), special_aim.get(ik), special_i.get(ik)+1, ik);
                        }
                    }
                    else if ( special_delta.get(ik).getX() < 0 && special_delta.get(ik).getY() > 0 ) {
                        if (x < to_special.get(ik).getX() + move_x && y > to_special.get(ik).getY() + move_y) {
                            special_animation.set(ik, false); 
                            animate_way(special_start.get(ik), special_aim.get(ik), special_i.get(ik)+1, ik);
                        }
                    }
                    else if ( special_delta.get(ik).getX() == 0 && special_delta.get(ik).getY() > 0 ) {
                        if (y > to_special.get(ik).getY() + move_y) {
                            special_animation.set(ik, false); 
                            animate_way(special_start.get(ik), special_aim.get(ik), special_i.get(ik)+1, ik);
                        }
                    }
                    else if ( special_delta.get(ik).getX() == 0 && special_delta.get(ik).getY() < 0 ) {
                        if (y < to_special.get(ik).getY() + move_y) {
                            special_animation.set(ik, false); 
                            animate_way(special_start.get(ik), special_aim.get(ik), special_i.get(ik)+1, ik);
                        }
                    }
                    else if ( special_delta.get(ik).getX() < 0 && special_delta.get(ik).getY() == 0 ) {
                        if (x < to_special.get(ik).getX() + move_x) {
                            special_animation.set(ik, false); 
                            animate_way(special_start.get(ik), special_aim.get(ik), special_i.get(ik)+1, ik);
                        }
                    }
                    else if ( special_delta.get(ik).getX() > 0 && special_delta.get(ik).getY() == 0 ) {
                        if (x > to_special.get(ik).getX() + move_x) {
                            special_animation.set(ik, false); 
                            animate_way(special_start.get(ik), special_aim.get(ik), special_i.get(ik)+1, ik);
                        }
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
        if (show_move_radius) {
            for (Object o : in_reach) {
                Place p = (Place)o;
                int x = calc_offset(p.index).get(0);
                int y = calc_offset(p.index).get(1);
                g.drawImage(content.iml.get_img("pics/Icons/begehbar.png"), x, y, place_width, place_height, imo);
            }
        }
        else {
            for (Object o : in_reach) {
                Place p = (Place)o;
                int x = calc_offset(p.index).get(0);
                int y = calc_offset(p.index).get(1);
                g.drawImage(content.iml.get_img("pics/Icons/attackierbar.png"), x, y, place_width, place_height, imo);
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
    
    public void move_char(Place from, Place to, boolean human) {
        if (human) {
            if (!(from.cur.team == team || team == -1)) {
                content.notification.add_noti("This character is not in your team");
                return;
            }
            if (!content.my_turn) {
                content.notification.add_noti("It is not your turn");
                return;
            }
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
                    if (human)
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
                if (human)
                    content.notification.add_noti("Cannot walk in this round anymore");
                return;
            }
            from.cur.did_walk = true;
            
            if (human) {
                way.add(optimize_way(pathfinding(from, to)));
                animate_way(from, to, 0, way.size()-1);
            } else {
                to.cur = from.cur;
                from.cur = null;
            }
            
            
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
    
    public ArrayList<Place> optimize_way(ArrayList<Place> tw) {
        ArrayList<Place> w = new ArrayList<Place>(tw);

        Place cur, next;
        boolean sl = false;
        boolean si = false;
        ArrayList<Place> rm = new ArrayList<Place>();

        for (int i=0;i<w.size()-1;i++) {
            cur=w.get(i);
            if(i+1==w.size()) break;
            next=w.get(i+1);

            if(!si && !sl) {
                if(same_index(cur, next)) si=true;
                if(same_list(cur,next)) sl=true;
            }
            else if(si) {
                if(same_index(cur,next)) {
                    rm.add(cur);
                }
                else {
                    si=false;
                    if(same_index(cur, next)) si=true;
                    if(same_list(cur,next)) sl=true;
                }
            }
            else if(sl) {
                if(same_list(cur,next)) {
                    rm.add(cur);
                }
                else {
                    sl=false;
                    if(same_index(cur, next)) si=true;
                    if(same_list(cur,next)) sl=true;
                }
            }
        }
        w.removeAll(rm);
        return w;
    }
    @SuppressWarnings("unchecked")
    public boolean same_index(Place a, Place b) {
        boolean breaker = false;
        int a_index = -1;
        for (Object o : content.field) {
            ArrayList<Place> l = (ArrayList<Place>)o;
            
            if ((a_index = l.indexOf(a)) != -1) {
                breaker = true;
                break;
            }
            if(breaker)break;
        }
        for (Object o : content.field) {
            ArrayList<Place> l = (ArrayList<Place>)o;
            
            if (l.get(a_index).index == b.index) return true;
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    public boolean same_list(Place a, Place b) {
        for (Object o : content.field) {
            ArrayList<Place> l = (ArrayList<Place>)o;
            if (l.contains(a) && l.contains(b)) return true;
        }
        return false;
    }
    
    public void animate_way(Place start, Place aim, int i, int curi) {
        if (i == 0) {
            // first move
            special_char.add(start.cur);
            special_i.add(i);
            special_aim.add(aim);
            special_start.add(start);
            from_special.add(new Point(0,0));
            to_special.add(new Point(0,0));
            special_delta.add(new Point(0,0));
            special.add(new Point(0,0));
            special_animation.add(false);
            
            aim.cur = special_char.get(curi);
            aim.cur.visible = false;
        }
        
        if (i == way.get(curi).size()-1) {
            // end of animation
            special_char.get(curi).visible = true;
            delete_current_way(curi);
            return;
        }
        
        //System.out.println(i+" of "+(way.get(0).size()-1));
        
        special_i.set(curi, i);
        special_aim.set(curi, aim);
        special_start.set(curi, start);
        
        Place cur, next;

        if (i != way.get(curi).size()-1) next = way.get(curi).get(i+1);
        else next = null;
        cur = way.get(curi).get(i);
        
        if (next == null) return;
        
        //System.out.println(cur.index+"->"+next.index);
        animate_move(cur, next, curi);
        
        start.cur = null;
    }
    
    public void delete_current_way(int ind) {
        way.remove(ind);
        special_aim.remove(ind);
        special_start.remove(ind);
        special_i.remove(ind);
        special_animation.remove(ind);
        special.remove(ind);
        from_special.remove(ind);
        to_special.remove(ind);
        special_delta.remove(ind);
        special_char.remove(ind);
    }
    
    ArrayList<ArrayList<Place>> way = new ArrayList<ArrayList<Place>>();
    ArrayList<Place> special_aim = new ArrayList<Place>();
    ArrayList<Place> special_start = new ArrayList<Place>();
    ArrayList<Integer> special_i = new ArrayList<Integer>();
    ArrayList<Boolean> special_animation = new ArrayList<Boolean>();
    ArrayList<Point> special = new ArrayList<Point>();
    ArrayList<Point> from_special = new ArrayList<Point>();
    ArrayList<Point> to_special = new ArrayList<Point>();
    ArrayList<Point> special_delta = new ArrayList<Point>();
    ArrayList<Char> special_char = new ArrayList<Char>();
    public void animate_move(Place from, Place to, int curi) {
        from_special.set(curi, new Point(calc_offset(from.index).get(0) - move_x, calc_offset(from.index).get(1) - move_y));
        to_special.set(curi, new Point(calc_offset(to.index).get(0) - move_x, calc_offset(to.index).get(1) - move_y));
        
        special_delta.set(curi, new Point(-(int)Math.floor((from_special.get(curi).getX()-to_special.get(curi).getX())/(animation_speed)), -(int)Math.floor((from_special.get(curi).getY()-to_special.get(curi).getY())/(animation_speed))));
        
        special.set(curi, new Point(0,0));
        
        special_animation.set(curi, true);
    }
    
    public synchronized void next_round() {
        if (!content.my_turn) {
            content.notification.add_noti("It is not your turn");
            return;
        }
        
        content.notification.add_noti("Let the next round begin!");
        
        make_chars_ready();
        
        if (online) {
            new_round_sending = true;
            content.my_turn = false;
        }
        else {
            can_modify = false;
            let_ai_happen();
            can_modify = true;
        }
    }
    
    public void make_chars_ready() {
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
                    
                    for (Object ob : get_reachable_places(p)) {
                        in_reach.add((Place)ob);
                    }

                    can_modify = true;
                    p.checked = false;
                }
            }
        }
    }
    public ArrayList<Place> get_reachable_places(Place middle) {
        String imp_fac = "attackenreichweite";
        if (show_move_radius) {
            imp_fac = "geschwindigkeit";
        }

        int max_dist = (int)Math.floor((middle.cur.property_current.get(imp_fac)/100));

        ArrayList<Place> output = new ArrayList<Place>();
        
        ArrayList<Place> working_p = new ArrayList<Place>();
        ArrayList<Integer> working_i = new ArrayList<Integer>();
        
        working_p.add(middle);
        working_i.add(0);
        
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
            
            output.add(current_p);
            working_p.remove(0);
            working_i.remove(0);
        }
        return output;
    }
    
    public HashMap<Place, Integer> make_hashmap(Place p, int i) {
        HashMap<Place, Integer> ret = new HashMap<Place, Integer>();
        ret.put(p, i);
        return ret;
    }
    
    public void do_online_stuff() {
        
    }
    
    public void let_ai_happen() {
        for (int i = 0 ; i < field_width*field_height-1 ; i++) {
            Place curp = get_place(i);
            if (curp.cur != null && curp.cur.team != team && team != -1) {
                in_reach.clear();
                in_reach.add(curp);
                
                // Someone next to him?
                show_move_radius = false;
                
                for (Object o : get_reachable_places(curp)) {
                    Place op = (Place)o;
                    
                    if (op.index == curp.index) {
                        continue;
                    }
                    
                    if( op.cur != null && op.cur.team != curp.cur.team) {
                        // Found char of different team
                        move_char(curp, op, false);
                    }
                }
                
                // Someone he might move towards?
                ArrayList<Place> places_next_to_enemy = new ArrayList<Place>();
                for (int ii = 0 ; ii < field_width*field_height-1 ; ii++) {
                    Place pe = get_place(ii);
                    if (pe.cur == null) continue;
                    if (curp.cur.team != pe.cur.team) {
                        for (Object o : get_bordering_places(pe)) {
                            places_next_to_enemy.add((Place)o);
                        }
                    }
                }
                show_move_radius = true;
                for (Object o : get_reachable_places(curp)) {
                    Place op = (Place)o;
                    if (op.index == curp.index) continue;
                    for (Object ob : places_next_to_enemy) {
                        Place pay = (Place)ob;
                        if (curp.cur != null && op.index == pay.index) {
                            //System.out.println(curp.index+" to "+op.index);
                            move_char(curp, op, false);
                            break;
                        }
                    }
                }
                
                if (curp.cur != null && !curp.cur.did_fight) {
                    // Someone next to him?
                    show_move_radius = false;
                    
                    for (Object o : get_reachable_places(curp)) {
                        Place op = (Place)o;
                        
                        if (op.index == curp.index) {
                            continue;
                        }
                        
                        if( op.cur != null && op.cur.team != curp.cur.team) {
                            // Found char of different team
                            move_char(curp, op, false);
                        }
                    }
                }
                
                
                // Just some random movement
                if (curp.cur != null && !curp.cur.did_walk) {
                    show_move_radius = true;
                    for (Object o : get_reachable_places(curp)) {
                        Place op = (Place)o;
                        if (op.index == curp.index) continue;
                        
                        if (curp.cur != null && Math.random() < 0.2) {
                            //System.out.println(curp.index+" to "+op.index);
                            curp.cur.did_walk = true;
                            op.cur = curp.cur;
                            curp.cur = null;
                            break;
                        }
                    }
                }
            }
        }
        in_reach.clear();
    }
    
    
    ArrayList<Place> open_list = new ArrayList<Place>();
    ArrayList<Place> closed_list = new ArrayList<Place>();
    Place target;
    public ArrayList<Place> pathfinding(Place start, Place aim) {
        open_list.clear();
        closed_list.clear();
        ArrayList<Place> solution = new ArrayList<Place>();
                
        if (start.index == aim.index) {
            solution.add(start);
            return solution;
        }
        if (aim.cur != null || aim.special.equals("NON-WALKABLE")) {
            return null;
        }
        
        target = aim;
        
        for (Object o : get_possible_places(start)) {
            Place p = (Place)o;
            p.ancestor = start;
            open_list.add(p);
        }
        
        move_place(start);
        
        while (open_list.size() > 0 || closed_list.contains(target)) {
            Place next = next_node();
            if (next == null) break;
            move_place(next);
            
            for (Object o : get_possible_places(next)) {
                Place p = (Place)o;
                
                if (closed_list.contains(p)) continue;
                
                /*if (open_list.contains(p)) {
                    Place tmp = new Place(null, p.index);
                    tmp.ancestor = next;
                    if (calc_g(tmp) < calc_g(p)) {
                        p.ancestor = next;
                    }
                }*/
                
                if (!open_list.contains(p)) {
                    p.ancestor = next;
                    open_list.add(p);
                }
            }
            
            //show_pathfinding_state();
        }
        
        Place cur = target;
        do {
            solution.add(cur);
            //System.out.print(cur.index + ((cur.ancestor==null)?"":" -> "));
            cur = cur.ancestor;
        } while (cur != null);
        Collections.reverse(solution);
        return solution;
    }
    public void move_place(Place p) {
        closed_list.add(p);
        open_list.remove(p);
    }
    public ArrayList<Place> get_possible_places(Place middle) {
        ArrayList<Place> ret = new ArrayList<Place>();
        for (Object o : get_bordering_places(middle)) {
            Place p = (Place)o;
            
            if (p.cur != null) continue;
            if (p.special.equals("NON-WALKABLE")) continue;
            
            ret.add(p);
        }
        
        return ret;
    }
    public Place next_node() {
        ArrayList<Integer> fl = new ArrayList<Integer>();
        for (Object o : open_list) {
            Place p = (Place)o;
            int f = calc_f(p);
            fl.add(f);
        }
        if (fl.isEmpty()) return null;
        int min = Collections.min(fl);
        int counter = 0;
        for (Object o : fl) {
            if (((Integer)o).intValue() == min) break;
            counter++;
        }
        Place next = open_list.get(counter);
        return next;
    }
    public int calc_f(Place p) {
        return calc_g(p) + calc_h(p);
    }
    public int calc_h(Place p) {
        int dx = Math.abs(get_x(p) - get_x(target));
        int dy = Math.abs(get_y(p) - get_y(target));
        return (dx + dy) * 10;
    }
    public int calc_g(Place p) {
        int till_now = 0;
        if (p.ancestor != null) {
            //System.out.println(p.index+" -> "+ p.ancestor.index);
            till_now = calc_g(p.ancestor);
            
            if (p.special.equals("NOTHING")) return 10 + till_now;
        }
        else {
            if (p.special.equals("NOTHING")) return 0;
        }
        return -1;
    }
    public int get_x(Place p) {
        for (Object o : content.field) {
            ArrayList al = (ArrayList)o;
            if (al.contains(p)) return al.indexOf(p);
        }
        return -1;
    }
    public int get_y(Place p) {
        int counter = 0;
        for (Object o : content.field) {
            ArrayList al = (ArrayList)o;
            if (al.contains(p)) break;
            counter++;
        }
        return counter;
    }
    public void show_pathfinding_state() {
        System.out.println("OPEN LIST:");
        for (Object o : open_list) {
            Place p = (Place)o;
            print_place(p);
        }
        System.out.println("CLOSED LIST:");
        for (Object o : closed_list) {
            Place p = (Place)o;
            print_place(p);
        }
    }
    public void print_place(Place p) {
        System.out.println(
                "to " + 
                p.index + 
                " (" + get_x(p) +"|"+get_y(p)+")" +
                " from " + 
                ((p.ancestor != null)?p.ancestor.index:"none") +
                " costs " +
                "F:" + calc_f(p) + " " +
                "G:" + calc_g(p) + " " +
                "H:" + calc_h(p) + " "
            );
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
                                move_char(in_reach.get(0), p, true);
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
            /*if (content.mouse_x - select_x > 0 && content.mouse_y - select_y > 0) {
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
            }*/
        }
        else if(button.equals("MIDDLE")) {
            move_x_tmp = (content.mouse_x - drag_start_x);
            move_y_tmp = (content.mouse_y - drag_start_y);
            move_x = move_x_tmp + old_move_x;
            move_y = move_y_tmp + old_move_y;
        }
        else if(button.equals("RIGHT")) {
            
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
    
    public void mousewheel_used (int amount) {
        if (amount > 0) scale -= scroll_speed*3;
        if (amount < 0) scale += scroll_speed*3;
        if (scale < -84) scale = -84; // min
        if (scale > 120) scale = 120; // max
    }
    
    public synchronized void update_on_the_fly() {
        Data_packet p = new Data_packet(changes, my_tmp_turn, client.num);
        
        p.on_the_fly = !new_round_sending;
        if (new_round_sending) {
            new_round_sending = false;
            my_tmp_turn = false;
        }
        
        client.send_data(p);
        changes.clear();
    }
    
    public void apply_changes(HashMap<Integer, Place> ch) {
        updated_changes.clear();
        loading_field = true;
        for (Map.Entry<Integer, Place> ob : ch.entrySet()) {
            int i = ob.getKey();
            Place p = ob.getValue();
            //System.out.println("Detected change on "+i+" ("+p.cur+")");
            p.cur.visible = true; // let us see all chars
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
            
                if (!cur.on_the_fly) {
                    //A new round started
                    
                    System.out.println("Received data");
                    
                    parent.apply_changes(cur.changes);
                    parent.make_chars_ready();
                    
                    content.my_turn = cur.my_turn;
                    my_tmp_turn = content.my_turn;
                    
                    content.notification.add_noti((content.my_turn)?"It is your turn":"Wait for other players");
                }
                else {
                    // just updating on the fly
                    parent.apply_changes(cur.changes);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public class Data_setter extends Thread {
        content_handler content;
        fight_handler parent;
        public Data_setter(fight_handler par, content_handler con) {
            content = con;
            parent = par;
        }
        
        public void run() {
            while (true) {
                parent.update_on_the_fly();
                try{sleep(7000);}catch(InterruptedException e){};
            }
        }
    }
}
