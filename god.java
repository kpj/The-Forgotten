import java.lang.*;
import java.util.*;
import java.awt.*;

public class god implements Runnable
{
    content_handler content = new content_handler();
    applet_handler window = new applet_handler(content);
    
    int t = 0;
    
    public god()
    {
        new Thread(this).start();
        
        //create_world();
        create_character("g", 200,200,"pics/GedrehtPre.png", "pics/Character/hero1.png");
        create_character("h", 300,200,"pics/GedrehtPre.png", "pics/Character/hero2.png");
        create_character("j", 400,200,"pics/GedrehtPre.png", "pics/Character/hero3.png");
        create_character("k", 100,200,"pics/GedrehtPre.png", "pics/Character/hero4.png");
        content.characters.get(0).collect_item(new Item(0));
        content.characters.get(0).collect_item(new Item(1));
        content.characters.get(0).collect_item(new Item(2));
        content.characters.get(0).equip_item(0);
        //create_object("g", 100,300,"pics/fight.png", "RANDOM_FIGHT");
        create_fight();
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
        
        if (content.fight_active){
            // Fight is taking place
            
            if (content.fight.is_over) {
                content.rm_environment(content.fight);
            }
        }
        else if (content.world_active) {
            // Running around in world
            check_collisions();
            content.world.calc_movement();
     
            
            if (content.world.is_over) {
                content.rm_environment(content.world);
            }
        }
        else if (content.map_active) {
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
    }
    
    public void create_character(String name, float x_pos, float y_pos, String world_image_path, String fight_image_path) {
        key_set set = new key_set(name);
        content.add_character(new Char (name, x_pos, y_pos, world_image_path, fight_image_path, set));
    }
    
    public Item create_item(int id) {
        return new Item(id);
    }
    
    public void create_fight() {
        content.add_fight(new fight_handler("pics/fight_bg_image.png", content));
    }
    public void create_world() {
        content.add_world(new world_handler("pics/world_bg_image.png", content));
    }
    public void create_map() {
        content.add_map(new map_handler("pics/map_bg.png", content));
    }
}
