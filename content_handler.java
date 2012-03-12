import java.util.*;

public class content_handler
{
    ArrayList<Thing> objects = new ArrayList<Thing>();
    ArrayList<Char> characters = new ArrayList<Char>();

    public content_handler()
    {
        
    }
    
    public void add_object(Thing o) {
        objects.add(o);
    }
    public void add_character(Char c) {
        characters.add(c);
    }
    
    public void rm_object(Thing o) {
        objects.remove(o);
    }
    
    public ArrayList<Thing> get_objects() {
        return objects;
    }
    public ArrayList<Char> get_characters() {
        return characters;
    }
}
