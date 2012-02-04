import java.util.*;

public class key_handler
{
    ArrayList<Char> characters = new ArrayList<Char>();

    public key_handler()
    {
        
    }
    
    public void handle_key(Character c) {
        for (Char current : characters) {
            HashMap cur_set = current.set.get_set();
            
            System.out.println(current +"--"+ current.set +"--"+ cur_set.get(current.name));
            
            //if (c == cur_set.get(current.name).get("UP")) {
            //    System.out.println("move up");
            //}
        }
    }
    
    public void set_characters(ArrayList cl) {
        characters = cl;
    }
}
