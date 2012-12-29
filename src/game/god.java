package game;
import game.Entity.Char;
import game.Entity.Item;
import game.Entity.Thing;
import game.handler.content_handler;
import game.handler.fight_handler;
import game.handler.map_handler;
import game.handler.world_handler;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.util.jar.JarEntry;
import java.io.*;

import core.graphics.applet_handler;

import user_interface.graphics.menu_handler;
import user_interface.graphics.notification_handler;
import user_interface.graphics.window.window_manager;
import user_interface.io.key_set;

import loader.Image_loader;
import loader.Sound_loader;

public class god implements Runnable
{
    public content_handler content;
    
    int t = 0;
    
    public god()
    {
        content = new content_handler();
        
        content.menu = new menu_handler(content);
        content.notification = new notification_handler(content);
        content.window = new applet_handler(content);
        content.win_manager = new window_manager(content);
        
        content.iml = new Image_loader(list_all(".png"));
        content.sol = new Sound_loader(list_all(".wav"));
        
        content.log_level = 10;
        
        new Thread(this).start();
    }
    
    public void run() {
        while(true) {
            sleep(42);
            update();
        }
    }
    
    public void update() {
        t++;
        content.window.repaint();
        if (content.fight_active){
            // Fight is taking place
            
            content.fight.compute_selection();
            
            if (content.fight.online)
                content.fight.do_online_stuff();
            
            if (content.fight.is_over) {
                content.rm_environment(content.fight);
            }
        }
        else if (content.world_active) {
            // Running around in world
            content.world.check_collisions();
            content.world.calc_movement();
            
            if (content.world.is_over) {
                content.rm_environment(content.world);
            }
        }
        else if (content.map_active) {
            // We are in map
            
        }
    }
    
    public void sleep(int t) {
        try {
            Thread.sleep(t);
        }
        catch (InterruptedException e) {
            System.out.println("Nicht warten?");
        }
    }
    
    public String get_own_name() {
        String[] tmp = getClass().getClassLoader().getResource("data").toString().split("!")[0].split("/");
        return tmp[tmp.length-1];
    }
    public String get_own_path() {
        String tmp = getClass().getClassLoader().getResource("data").toString().split("!")[0];
        tmp = tmp.replaceFirst("jar:file:", "");
        String[] ump = tmp.split("/");
        String out = "";
        for (int i = 0 ; i < ump.length-1 ; i++) {
            out += ump[i];
            out += "/";
        }
        return out;
    }
    public ArrayList<String> list_all(String end) {
        String filename = get_own_name();
        ArrayList<String> out = new ArrayList<String>();

        try {
            java.util.jar.JarFile jarFile = new java.util.jar.JarFile(get_own_path()+filename);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                java.util.zip.ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith(end)) {
                    out.add(entry.getName());
                    //System.out.println(entry.getName());
                }
            }
        }
        catch (IOException e){
            System.err.println("error: "+e.getMessage());
        }
        return out;
    }
    
    public void create_object(String name, float x_pos, float y_pos, String world_image_path, String t) {
        content.add_object(new Thing (name, x_pos, y_pos, world_image_path, t));
    }
    
    public void create_character(String name, float x_pos, float y_pos, String world_image_path, String fight_image_path, int team) {
        key_set set = new key_set(name);
        ArrayList<String> fight_img = new ArrayList<String>();
        fight_img.add(fight_image_path);
        ArrayList<String> world_img = new ArrayList<String>();
        world_img.add(world_image_path);
        
        content.add_character(new Char (name, x_pos, y_pos, world_img, fight_img, set, team, null));
    }
    
    public Item create_item(int id) {
        return new Item(id);
    }
    
    public void create_fight(String path2fight, boolean online) {
        content.add_fight(new fight_handler(path2fight, content, online));
    }
    public void create_world() {
        content.add_world(new world_handler("/data/pics/world_bg_image.png", content));
    }
    public void create_map() {
        content.add_map(new map_handler("/data/pics/map_bg.png", content));
    }
}
