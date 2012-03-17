import java.awt.*;
import java.util.*;

public class Item
{
    String name, n;
    String e_i, i_i;
    HashMap<String, Float> effect = new HashMap<String, Float>();
    HashMap<String, Float> property = new HashMap<String, Float>();
    Image equipped_image, entity_image;
    
    boolean is_in_use = false;

    public Item(int id)
    {   
        if (id == 0) {
            n = "Normales Holzschwert";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("angriffskraft", (float)1);
        }
        else if (id == 1) {
            n = "Normales Holzschild";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("verteidigungspunkte", (float)1);
        }
        else if (id == 2) {
            n = "Gemeine Reiserobe";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("verteidigungspunkte", (float)0.3);
        }
        else if (id == 3) {
            n = "Schwere Schuhe";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("geschwindigkeit", (float)-1);
        }
        else if (id == 4) {
            n = "Schuhe des Fliegens";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("geschwindigkeit", (float)1);
        }
        
        
        
        name = n;
        equipped_image = Toolkit.getDefaultToolkit().getImage(e_i);
        entity_image = Toolkit.getDefaultToolkit().getImage(i_i);
        effect = property;
    }
}
