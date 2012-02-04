import java.lang.*;
import java.util.*;
import java.awt.*;

public class god
{
    world_handler world = new world_handler();
    applet_handler window = new applet_handler();

    public god()
    {

    }
    
    public void live() {
        while(true) {
            create_object("rofl", 100, 100, "img/hero.png");
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                System.out.println("Nicht warten?");
            }
        }
    }
    
    public void create_object(String name, float x_pos, float y_pos, String world_image_path) {
        System.out.println("Created obj");
        world.add_object(new Object (name, x_pos, y_pos, world_image_path));
        window.set_objects(world.get_objects());
    }
}
