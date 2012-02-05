import java.util.*;
import java.lang.*;

public class key_handler extends Thread
{
    ArrayList<Char> characters = new ArrayList<Char>();
    Set pressed = new HashSet();
    
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
    
    public void handle_key(Character c) {
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
        }
    }
    
    public void set_characters(ArrayList cl) {
        characters = cl;
    }
}
