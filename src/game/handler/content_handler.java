package game.handler;

import game.Entity.Char;
import game.Entity.Place;
import game.Entity.Region;
import game.Entity.Thing;

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import user_interface.graphics.menu_handler;
import user_interface.graphics.notification_handler;
import user_interface.graphics.window.window_manager;

import core.graphics.applet_handler;

import network.Client;

import java.io.*;

import loader.Image_loader;
import loader.Sound_loader;

public class content_handler
{
    public Graphics2D g = null;
    public String lol = "lol";
    
    // Imp
    public fight_handler fight; public boolean fight_active = false;
    public world_handler world; public boolean world_active = false;
    map_handler map; public boolean map_active = false;

    // In fight
    ArrayList<ArrayList> field;
    public Place current_selected = null;
    boolean fight_starting = true;
    public ArrayList<Char> ini_table;
    
    // In world
    ArrayList<Thing> objects = new ArrayList<Thing>();
    int world_bg_x_change = 1000;
    int world_bg_y_change = 1000;
    
    // In map
    ArrayList<Region> regions = new ArrayList<Region>();
    
    // Everywhere
    public menu_handler menu = null;
    public notification_handler notification = null;
    public applet_handler window;
    public window_manager win_manager;
    
    public JFrame f = new JFrame("The Forgotten");
    
    public ArrayList<Char> characters = new ArrayList<Char>();
    public int window_width = 1024;
    public int window_height = 768;
    public int mouse_x;
	public int mouse_y;

    public Image_loader iml;
    public Sound_loader sol;
    
    public boolean show_mouse_animation = false;

    // window management
    public boolean is_dragging_window = false;
    public user_interface.graphics.window.Window dragged_window = null;
    
	// font
	String font_to_use = "CAELDERA.TTF";
	public Font font;
	public FontMetrics font_metric;

    // online stuff
    public ArrayList<Client> connected = new ArrayList<Client>();
    public ArrayList<Boolean> turn = new ArrayList<Boolean>();
    public String ip = "localhost";
    public int port = 4223;
    public boolean my_turn = true;
    
    public content_handler()
    {
    	this.font = this.getFont();
    }
    
	public Font getFont(){
		Font font = null;
		try {
			InputStream fin = this.getClass().getResourceAsStream("/data/fonts/"+font_to_use);
			font = Font.createFont(
				Font.TRUETYPE_FONT,
				fin
			).deriveFont(16f);
		}
		catch (Exception e) {
			System.out.println("Font creation failed");
			e.printStackTrace();
		}
		return font;
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
    public void rm_character(Char c) {
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
    
    
    public static int log_level = 0; // higher int = higher priority
    public void log(String msg, int lvl) {
        if (lvl >= log_level) System.out.println(msg);
    }
    
    
    // Return type: {width, height}
    public int[] get_string_information(String s) {
    	int width = (int) this.font.getStringBounds(s, this.g.getFontRenderContext()).getWidth();
    	int height = this.font_metric.getHeight();
    	
    	int[] i = {width, height};
    	return i;
    }
}
