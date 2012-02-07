import java.awt.*;
import java.util.*;

public class Char
{
    String name;
    Image world_image, fight_image;
    float x_pos, y_pos;
    float world_image_height, world_image_width, fight_image_height, fight_image_width;
    key_set set;
    
    ArrayList<ArrayList> items = new ArrayList<ArrayList>();
    HashMap<String, Integer> property = new HashMap<String, Integer>();
    
    public Char(String n, float x, float y, String w_i, String f_i, key_set kset)
    {
        name = n;
        
        world_image = Toolkit.getDefaultToolkit().getImage(w_i);
        world_image_height = world_image.getHeight(null);
        world_image_width = world_image.getWidth(null);
        
        fight_image = Toolkit.getDefaultToolkit().getImage(f_i);
        fight_image_height = fight_image.getHeight(null);
        fight_image_width = fight_image.getWidth(null);
        
        x_pos = x;
        y_pos = y;
        set = kset;
        
        // Properties
        property.put("geschwindigkeit", 5);
        property.put("lebenspunkte", 10);
        property.put("manapunkte", 10);
        property.put("ausdauerpunkte", 5);
        property.put("initiative", 8);
        property.put("magieresistenz", 9);
        property.put("angriffskraft", 2);
        property.put("verteidigungspunkte", 1);
    }
    
    public Image get_image() {
        return world_image;
    }
    
    public float get_x() {
        return x_pos;
    }
    public float get_y() {
        return y_pos;
    }
    
    public void move(String direction) {
        if (direction == "UP") {
            y_pos -= property.get("geschwindigkeit");
        }
        else if (direction == "DOWN") {
            y_pos += property.get("geschwindigkeit");
        }
        else if (direction == "LEFT") {
            x_pos -= property.get("geschwindigkeit");
        }
        else if (direction == "RIGHT") {
            x_pos += property.get("geschwindigkeit");
        }
    }
    
    // Erzeugt benötigte Item-Listen-Struktur: [Item. boolean], [Item, boolean], ...
    // Item: jeweiliges Item-Objekt, boolean: Ausgerüstet oder nicht
    public void collect_item(Item it) {
        ArrayList inside_items = new ArrayList();
        inside_items.add(it);
        inside_items.add(false);
        items.add(inside_items);
    }
    
    public ArrayList return_items() {
        return items;
    }
}
