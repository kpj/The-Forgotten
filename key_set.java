import java.util.*;

public class key_set
{
    HashMap<String, HashMap> player_set = new HashMap<String, HashMap>();

    public key_set()
    {
        String name = "Hinz";
        
        HashMap<String, Character> action2key = new HashMap<String, Character>();
        
        action2key.put("UP", 'w');
        action2key.put("LEFT", 'a');
        action2key.put("RIGHT", 'd');
        action2key.put("DOWN", 's');

        player_set.put(name, action2key);
    }
    
    public HashMap get_set() {
        return player_set;
    }
}
