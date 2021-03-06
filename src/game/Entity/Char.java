package game.Entity;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import user_interface.io.key_set;

public class Char implements Serializable, Comparable
{
    public String name;
    ArrayList<String> world_image;
    public ArrayList<String> fight_image;

    public int team;
    public float x_pos;
	public float y_pos;
    public float world_image_height;
	public float world_image_width;
	float fight_image_height;
	float fight_image_width;
    public key_set set;
    
    public ArrayList<Item> items = new ArrayList<Item>();
    HashMap<String, Integer> property_std = new HashMap<String, Integer>(); // standard values
    public HashMap<String, Integer> property_max = new HashMap<String, Integer>(); // values with equipped items
    public HashMap<String, Integer> property_current = new HashMap<String, Integer>(); // values during fight
    
    public boolean did_fight = false;
    public boolean did_walk = false;
    public boolean visible = true;
    
    @SuppressWarnings("unchecked")
    public Char(String n, float x, float y, ArrayList<String> w_i, ArrayList<String> f_i, key_set kset, int t, HashMap<String, Integer> prop)
    {
        name = n;
        
        world_image = w_i;
        
        fight_image = f_i;
        
        x_pos = x;
        y_pos = y;
        team = t;
        set = kset;
        
        if (prop == null) {
            // Properties
            property_std.put("geschwindigkeit", 500);
            property_std.put("lebenspunkte", 2000);
            property_std.put("manapunkte", 1000);
            property_std.put("ausdauerpunkte", 500);
            property_std.put("initiative", 800);
            property_std.put("magieresistenz", 900);
            property_std.put("angriffskraft", 200);
            property_std.put("attackenreichweite", 100);
            property_std.put("verteidigungspunkte", 100);
            property_std.put("attackenmodifikator", 1);
            property_std.put("verteidigungmodifikator", 1);
            property_std.put("initiative", 50);
        }
        else {
            property_std = new HashMap<String, Integer>(prop);
        }
        calc_property_max();
        property_current = new HashMap<String, Integer>(property_max);
    }
    
    public ArrayList<String> get_image() {
        return world_image;
    }
    
    public Rectangle get_rect() {
        return new Rectangle((int)x_pos,(int)y_pos,(int)world_image_width,(int)world_image_height);
    }
    
    public void change_pos(int x, int y) {
        x_pos += x;
        y_pos += y;
    }
    
    public void move(String direction) {
        if (direction == "UP") {
            y_pos -= property_current.get("geschwindigkeit")/100;
        }
        else if (direction == "DOWN") {
            y_pos += property_current.get("geschwindigkeit")/100;
        }
        else if (direction == "LEFT") {
            x_pos -= property_current.get("geschwindigkeit")/100;
        }
        else if (direction == "RIGHT") {
            x_pos += property_current.get("geschwindigkeit")/100;
        }
    }
    
    public void deal_damage(int val) {
        property_current.put("lebenspunkte", property_current.get("lebenspunkte") - val);
    }

    @SuppressWarnings("unchecked")
    public void collect_item(Item it) {
        items.add(it);
    }
    public void equip_item(int pos) {
        items.get(pos).is_in_use = true;
        calc_property_max();
        property_current = new HashMap<String, Integer>(property_max); //CHANGE FAST
    }
    public void unequip_item(int pos) {
        items.get(pos).is_in_use = false;
        calc_property_max();
        property_current = new HashMap<String, Integer>(property_max); //CHANGE FAST
    }
    
    public ArrayList get_equipped_items() {
        ArrayList<Item> equi = new ArrayList<Item>();
        for (Object o : items) {
            Item i = (Item)o;
            if (i.is_in_use)
                equi.add(i);
        }
        return equi;
    }
    
    @SuppressWarnings("unchecked")
    public void calc_property_max() {
        property_max = new HashMap<String, Integer>(property_std);
        //System.out.println("------\n"+name+"\n------");
        for (Object o : items) {
            Item i = (Item)o;
            if (i.is_in_use) {
                for (Map.Entry<String, Integer> ob : i.effect.entrySet()) {
                    for (Map.Entry<String, Integer> obj : property_max.entrySet()) {
                        String item_key = ob.getKey();
                        String char_key = obj.getKey();
                        
                        if (item_key.equals(char_key)) {
                            //System.out.println(">"+obj.getKey()+" ("+i.name+")\n>>"+obj.getValue());
                            property_max.put(item_key, ob.getValue() + obj.getValue());
                            //System.out.println(">>v\n>>"+obj.getValue());
                            break;
                        }
                        
                    }
                }
            }
        }
    }
    
    // Make it comparable
    public int compareTo(Object anotherChar) throws ClassCastException {
        if (!(anotherChar instanceof Char)) throw new ClassCastException("A Char object expected.");
        int anotherCharINI = ((Char) anotherChar).property_current.get("initiative");  
        return property_current.get("initiative") - anotherCharINI;
    }
}
