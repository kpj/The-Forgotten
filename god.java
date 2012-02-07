import java.lang.*;
import java.util.*;
import java.awt.*;

public class god implements Runnable
{
    world_handler world = new world_handler();
    applet_handler window = new applet_handler();

    public god()
    {
        new Thread(this).start();
    }
    
    public void run() {
        create_character("Hinz", 0, 0, "pics/Prop.png", "path/to/fight.img", new key_set());
        create_object("Dummy", 300, 100, "pics/hero.png");
        while(true) {
            sleep(42);
            update();
        }
    }
    
    public void update() {
        draw();
        check_collisions();
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
    
    public void create_object(String name, float x_pos, float y_pos, String world_image_path) {
        world.add_object(new Thing (name, x_pos, y_pos, world_image_path));
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
