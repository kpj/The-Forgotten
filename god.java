import java.lang.*;
import java.util.*;
import java.awt.*;

public class god implements Runnable
{
    applet_handler window = new applet_handler();
    content_handler content = new content_handler();

    fight_handler fighter = null;
    world_handler world = null;
    map_handler map = null;
    
    int t = 0;
    
    public god()
    {
        new Thread(this).start();
    }
    
    public void run() {
        while(true) {
            sleep(42);
            update();
        }
    }
    
    public void update() {
        t++;
        draw();
        
        if (fighter != null){
            // Fight is taking place
            
            if (fighter.is_over) {
                fighter = null;
                window.give_fight_handler(fighter);
            }
        }
        else if (world != null) {
            // Running around in world
            check_collisions();
            world.set_params(window.get_window_width(), window.get_window_height());
            world.calc_movement();
            
            window.drawer.help_with_bg(world.get_bg_image(), window.get_window_width(), window.get_window_height(), world.change_x, world.change_y);
            
            if (world.is_over) {
                world = null;
                window.give_world_handler(world);
            }
        }
        else if (map != null) {
            // We are in map
            check_mouse();
            
        }
    }
    
    public void check_mouse() {
        if (window.left_mouse) {
            window.left_mouse = false;
            
            System.out.println("POKE");
        }
        else if (window.right_mouse) {
            window.right_mouse = false;
            
            
        }
        else if (window.middle_mouse) {
            window.middle_mouse = false;
            
            
        }
    }
    
    public void check_collisions() {
        for (int i = 0 ; i < content.get_objects().size() ; i++) {
            for (int u = 0 ; u < content.get_characters().size() ; u++) {
                Thing o = (Thing)content.get_objects().get(i);
                Char c = (Char)content.get_characters().get(u);

                //System.out.println(o.get_rect().toString() +"    "+ c.get_rect().toString());
                
                if (o.get_rect().intersects(c.get_rect())) {
                    o.on_touch();
                }
            }
        }
        
        check_actions();
    }
    
    public void check_actions() {
        Thing to_rm = null;
        for (Object ob : content.get_objects()) {
            Thing o = (Thing)ob;
            
            if (o.get_touched()) {
                String type = o.get_type();
                
                if (type == "NOTHING") {
                    // Well, nothing...
                }
                else if (type == "RANDOM_FIGHT") {
                    // START FIGHT
                    create_fight();
                }
                
                to_rm = o;
                o.set_touched(false);
            }
        }
        if (to_rm != null) {
            content.rm_object(to_rm);
            to_rm = null;
        }
    }
    
    public void draw() {
        window.repaint();
    }

    public void del_world() {
        world.is_over = true;
    }
    
    public void sleep(int t) {
        try {
            Thread.sleep(t);
        }
        catch (InterruptedException e) {
            System.out.println("Nicht warten?");
        }
    }
    
    public void create_object(String name, float x_pos, float y_pos, String world_image_path, String t) {
        content.add_object(new Thing (name, x_pos, y_pos, world_image_path, t));
        window.set_objects(content.get_objects());
    }
    
    public void create_character(String name, float x_pos, float y_pos, String world_image_path, String fight_image_path) {
        key_set set = new key_set(name);
        content.add_character(new Char (name, x_pos, y_pos, world_image_path, fight_image_path, set));
        window.set_characters(content.get_characters());
    }
    
    public Item create_item(int id) {
        return new Item(id);
    }
    
    public void create_fight() {
        fighter = new fight_handler(content);
        window.give_fight_handler(fighter);
    }
    public void create_world() {
        world = new world_handler("pics/bg_image.png", content);
        window.give_world_handler(world);
        create_character("g",200,200,"pics/GedrehtPre.png", "lol");
    }
    public void create_map() {
        map = new map_handler("pics/map_bg.png", content);
    }
}
