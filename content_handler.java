import java.util.*;

public class content_handler
{
    public String lol = "lol";

    // Imp
    fight_handler fight; public boolean fight_active = false;
    world_handler world; public boolean world_active = false;
    map_handler map; public boolean map_active = false;

    // In fight
    Place current_selected = null;
    
    // In world
    ArrayList<Thing> objects = new ArrayList<Thing>();
    int world_bg_x_change = 1000;
    int world_bg_y_change = 1000;
    
    // In map
    ArrayList<Region> regions = new ArrayList<Region>();
    
    // Everywhere
    menu_handler menu;
    applet_handler window;
    
    ArrayList<Char> characters = new ArrayList<Char>();
    int window_width = 1024;
    int window_height = 768;
    int mouse_x, mouse_y;

    public content_handler()
    {
        
    }
    
    public java.lang.Object get_active_environment() {
        if (fight_active) return fight;
        if (world_active) return world;
        if (map_active) return map;
        return null;
    }
    
    public void add_fight(fight_handler r) {
        all_false();
        fight = r;
        fight_active = true;
    }
    public void add_world(world_handler r) {
        all_false();
        world = r;
        world_active = true;
    }
    public void add_map(map_handler r) {
        all_false();
        map = r;
        map_active = true;
    }
    public void all_false() {
        fight_active = false;
        world_active = false;
        map_active = false;
    }
    
    public void rm_environment(java.lang.Object r) {
        
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
