import java.lang.*;
import java.util.*;
import java.awt.*;

public class god implements Runnable
{
    world_handler world = new world_handler();
    applet_handler window = new applet_handler();

    fight_handler fighter = null;
    
    public god()
    {
        new Thread(this).start();
    }
    
    public void run() {
        create_character("Hinz", 200, 200, "pics/Prop.png", "path/to/fight.img", new key_set());
        create_object("Dummy", 300, 100, "pics/hero.png", "RANDOM_FIGHT");
        while(true) {
            sleep(42);
            update();
        }
    }
    
    public void update() {
        draw();
        
        if (fighter == null) {
            // Running around in world
            check_collisions();
            world.set_params(window.get_window_width(), window.get_window_height());
            world.calc_movement();
            
            window.drawer.help_with_bg(world.get_bg_image(), window.get_window_width(), window.get_window_height(), world.change_x, world.change_y);
        }
        else {
            // Fight is taking place
            
            if (fighter.is_over) {
                fighter = null;
            }
        }
    }
    
    public void check_collisions() {
        for (int i = 0 ; i < world.get_objects().size() ; i++) {
            for (int u = 0 ; u < world.get_characters().size() ; u++) {
                Thing o = (Thing)world.get_objects().get(i);
                Char c = (Char)world.get_characters().get(u);

                //System.out.println(o.get_rect().toString() +"    "+ c.get_rect().toString());
                
                if (o.get_rect().intersects(c.get_rect())) {
                    o.on_touch();
                }
            }
        }
        
        check_actions();
    }
    
    public void check_actions() {
        for (Object ob : world.get_objects()) {
            Thing o = (Thing)ob;
            
            if (o.get_touched()) {
                String type = o.get_type();
                
                if (type == "NOTHING") {
                    // Well, nothing...
                }
                else if (type == "RANDOM_FIGHT") {
                    // START FIGHT
                    fighter = new fight_handler(world.get_characters());
                    window.drawer.give_fight_handler(fighter);
                }
                
                o.set_touched(false);
            }
        }
    }
    
    public void draw() {
        window.repaint();
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
        world.add_object(new Thing (name, x_pos, y_pos, world_image_path, t));
        window.set_objects(world.get_objects());
    }
    
    public void create_character(String name, float x_pos, float y_pos, String world_image_path, String fight_image_path, key_set set) {
        world.add_character(new Char (name, x_pos, y_pos, world_image_path, fight_image_path, set));
        window.set_characters(world.get_characters());
    }
    
    public Item create_item(int id) {
        return new Item(id);
    }
}
