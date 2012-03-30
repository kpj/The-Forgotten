import java.lang.*;
import java.util.*;
import java.awt.*;

public class god implements Runnable
{
    static content_handler content = null;
    
    int t = 0;
    
    public god()
    {
        if (content == null) {
            content = new content_handler();
        }
        
        content.menu = new menu_handler(content);
        content.notification = new notification_handler(content);
        content.window = new applet_handler(content);
        new Thread(this).start();
        
        create_fight("data/fights/test.txt", true);
    }
    
    public static void main (String[] args)
    {
        content = new content_handler();
        
        if (args.length == 0) {
            System.out.println("Starting game");
            god good = new god(); 
        }
        else if (args[0].equals("--server")) {
            System.out.println("Starting server");
            Server serv = new Server(content);
        }
        else {
            System.out.println("Unkown option");
        }
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
            
            content.fight.compute_selection();
            
            if (content.fight.online)
                content.fight.do_online_stuff();
            
            if (content.fight.is_over) {
                content.rm_environment(content.fight);
            }
        }
        else if (content.world_active) {
            // Running around in world
            content.world.check_collisions();
            content.world.calc_movement();
            
            if (content.world.is_over) {
                content.rm_environment(content.world);
            }
        }
        else if (content.map_active) {
            // We are in map
            
        }
    }
    
    public void draw() {
        content.window.repaint();
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
    
    public void create_character(String name, float x_pos, float y_pos, String world_image_path, String fight_image_path, int team) {
        key_set set = new key_set(name);
        ArrayList<String> fight_img = new ArrayList<String>();
        fight_img.add(fight_image_path);
        ArrayList<String> world_img = new ArrayList<String>();
        world_img.add(world_image_path);
        
        content.add_character(new Char (name, x_pos, y_pos, world_img, fight_img, set, team, null));
    }
    
    public Item create_item(int id) {
        return new Item(id);
    }
    
    public void create_fight(String path2fight, boolean online) {
        content.add_fight((new Map_parser(path2fight, content, online)).get_fight());
    }
    public void create_world() {
        content.add_world(new world_handler("pics/world_bg_image.png", content));
    }
    public void create_map() {
        content.add_map(new map_handler("pics/map_bg.png", content));
    }
}
