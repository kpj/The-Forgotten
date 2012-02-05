import java.util.*;

public class key_handler
{
    ArrayList<Char> characters = new ArrayList<Char>();

    public key_handler()
    {
        
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
