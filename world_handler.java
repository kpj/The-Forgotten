// std libs
import java.util.*;

public class world_handler
{
    ArrayList<Object> objects = new ArrayList<Object>();
    ArrayList<Char> characters = new ArrayList<Char>();

    public world_handler()
    {
        
    }
    
    public void add_object(Object o) {
        objects.add(o);
    }
    public void add_character(Char c) {
        characters.add(c);
    }
    
    public ArrayList get_objects() {
        return objects;
    }
    public ArrayList get_characters() {
        return characters;
    }
}