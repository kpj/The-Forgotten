import java.util.*;

public class content_handler
{
    // Imp
    fight_handler fight; boolean fight_active = false;
    world_handler world; boolean world_active = false;
    map_handler map; boolean map_active = false;

    // In world
    ArrayList<Thing> objects = new ArrayList<Thing>();
    
    // In map
    ArrayList<Region> regions = new ArrayList<Region>();
    
    // Everywhere
    ArrayList<Char> characters = new ArrayList<Char>();

    public content_handler()
    {
        
    }
    
    public void get_active_environment() {
        //
    }
    
    public void add_object(Thing o) {
        objects.add(o);
    }
    public void add_character(Char c) {
        characters.add(c);
    }
    public void add_region(Region r) {
        regions.add(r);
    }
    
    public void rm_object(Thing o) {
        objects.remove(o);
    }
    public void rm_character(Character c) {
        characters.remove(c);
    }
    public void region(Region r) {
        regions.remove(r);
    }
    
    public ArrayList<Thing> get_objects() {
        return objects;
    }
    public ArrayList<Char> get_characters() {
        return characters;
    }
    public ArrayList<Region> get_regions() {
        return regions;
    }
}
