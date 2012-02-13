import java.util.*;
import java.lang.*;

public class key_handler extends Thread
{
    ArrayList<Char> characters = new ArrayList<Char>();
    Set pressed = new HashSet();
    
    fight_handler fighter = null;
    world_handler world = null;
    
    int one_runtime = 42;

    public key_handler()
    {
        
    }
    
    float start_time, end_time, time_diff;
    public void run() {
        while (true) {
            start_time = System.currentTimeMillis();
            try {
                for (java.lang.Object v : pressed) {
                    handle_key(v.toString().charAt(0));
                }
            }
            catch (ConcurrentModificationException e) {}
            
            end_time = System.currentTimeMillis();
            
            time_diff = one_runtime - (end_time - start_time);
            if (time_diff > 0) {
                try {sleep((int)time_diff);} catch(InterruptedException e) {}
            }
        }
    }
    
    public void set_key_list(Set set) {
        pressed = set;
    }
    
    public void give_fight_handler(fight_handler fi) {
        fighter = fi;
        pressed = new HashSet();
    }
    public void give_world_handler(world_handler wo) {
        world = wo;
        pressed = new HashSet();
    }
    
    public void handle_key(Character c) {
        if (world != null) {
            world_movement(c);
        }
        else if (fighter != null) {
            fight_movement(c);
        }
        else {
            map_movement(c);
        }
    }
    
    public void map_movement(Character c) {
        System.out.println("USE MOUSE");
    }
    
    public void fight_movement(Character c) {
        System.out.println(c);
    }
        
    public void world_movement (Character c) {
        if (characters.size() == 0) {
            return;
        }
        for (Char current : characters) {
            HashMap cur_set = (HashMap)current.set.get_set().get(current.name);
            
            if (c == cur_set.get("UP")) {
                current.move("UP");
            }
            else if (c == cur_set.get("RIGHT")) {
                current.move("RIGHT");
            }
            else if (c == cur_set.get("LEFT")) {
                current.move("LEFT");
            }
            else if (c == cur_set.get("DOWN")) {
                current.move("DOWN");
            }
            
            if (c == cur_set.get("INVENTAR")) {
                System.out.println("INV");
            }
        }
    }
    
    public void set_characters(ArrayList<Char> cl) {
        characters = cl;
    }
}
