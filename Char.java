import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Char
{
    String name;
    Image world_image;
    BufferedImage fight_image;
    ImageIcon icon;
    int team;
    float x_pos, y_pos;
    float world_image_height, world_image_width, fight_image_height, fight_image_width;
    key_set set;
    
    ArrayList<Item> items = new ArrayList<Item>();
    HashMap<String, Float> property_std = new HashMap<String, Float>(); // standard values
    HashMap<String, Float> property_max = new HashMap<String, Float>(); // values with equipped items
    HashMap<String, Float> property_current = new HashMap<String, Float>(); // values during fight
    
    boolean did_something_this_round = false;
    
    public Char(String n, float x, float y, String w_i, ArrayList<String> f_i, key_set kset, int t, HashMap<String, Float> prop)
    {
        name = n;
        
        world_image = Toolkit.getDefaultToolkit().getImage(w_i);
        icon = new ImageIcon(world_image);
        world_image_height = icon.getIconHeight();
        world_image_width = icon.getIconWidth();
        
        fight_image = (new Image_parser(f_i)).get_img();
        icon = new ImageIcon(fight_image);
        fight_image_height = icon.getIconHeight();
        fight_image_width = icon.getIconWidth();
        
        x_pos = x;
        y_pos = y;
        team = t;
        set = kset;
        
        if (prop == null) {
            // Properties
            property_std.put("geschwindigkeit", (float)5);
            property_std.put("lebenspunkte", (float)10);
            property_std.put("manapunkte", (float)10);
            property_std.put("ausdauerpunkte", (float)5);
            property_std.put("initiative", (float)8);
            property_std.put("magieresistenz", (float)9);
            property_std.put("angriffskraft", (float)2);
            property_std.put("attackenreichweite", (float)2);
            property_std.put("verteidigungspunkte", (float)1);
        }
        else {
            property_std = prop;
        }
        calc_property_max();
    }
    
    public Image get_image() {
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
            y_pos -= property_current.get("geschwindigkeit");
        }
        else if (direction == "DOWN") {
            y_pos += property_current.get("geschwindigkeit");
        }
        else if (direction == "LEFT") {
            x_pos -= property_current.get("geschwindigkeit");
        }
        else if (direction == "RIGHT") {
            x_pos += property_current.get("geschwindigkeit");
        }
    }
    
    public void deal_damage(float val) {
        property_current.put("lebenspunkte", property_current.get("lebenspunkte") - val);
    }
    
    // Erzeugt benötigte Item-Listen-Struktur: [Item. boolean], [Item, boolean], ...
    // Item: jeweiliges Item-Objekt, boolean: Ausgerüstet oder nicht
    @SuppressWarnings("unchecked")
    public void collect_item(Item it) {
        items.add(it);
    }
    public void equip_item(int pos) {
        items.get(pos).is_in_use = true;
        calc_property_max();
    }
    public void unequip_item(int pos) {
        items.get(pos).is_in_use = false;
        calc_property_max();
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
    
    public void calc_property_max() {
        property_max = property_std;
        for (Object o : items) {
            Item i = (Item)o;
            
            if (i.is_in_use) {
                for (Map.Entry<String, Float> ob : i.effect.entrySet()) {
                    for (Map.Entry<String, Float> obj : property_std.entrySet()) {
                        
                        String item_key = ob.getKey();
                        String char_key = obj.getKey();
                        
                        if (item_key.equals(char_key)) {
                            property_max.put(item_key, ob.getValue() + obj.getValue());
                        }
                        
                    }
                }
            }
        }
        
        property_current = property_max; // CHANGE, QUICK!!!
    }
}
