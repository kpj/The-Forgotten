import java.awt.*;
import java.util.*;

public class Item
{
    String name, n;
    String e_i, i_i;
    HashMap<String, Float> effect = new HashMap<String, Float>();
    HashMap<String, Float> property = new HashMap<String, Float>();
    Image equipped_image, entity_image;

    public Item(int id)
    {   
        if (id == 0) {
            n = "Normales Holzschwert";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("geschwindigkeit", (float)0);
            property.put("lebenspunkte", (float)0);
            property.put("manapunkte", (float)0);
            property.put("ausdauerpunkte", (float)0);
            property.put("initiative", (float)0);
            property.put("magieresistenz", (float)0);
            property.put("angriffskraft", (float)1);
            property.put("verteidigungspunkte", (float)0);
        }
        else if (id == 1) {
            n = "Normales Holzschild";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("geschwindigkeit", (float)0);
            property.put("lebenspunkte", (float)0);
            property.put("manapunkte", (float)0);
            property.put("ausdauerpunkte", (float)0);
            property.put("initiative", (float)0);
            property.put("magieresistenz", (float)0);
            property.put("angriffskraft", (float)0);
            property.put("verteidigungspunkte", (float)1);
        }
        else if (id == 2) {
            n = "Gemeine Reiserobe";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("geschwindigkeit", (float)0);
            property.put("lebenspunkte", (float)0);
            property.put("manapunkte", (float)0);
            property.put("ausdauerpunkte", (float)0);
            property.put("initiative", (float)0);
            property.put("magieresistenz", (float)0);
            property.put("angriffskraft", (float)0);
            property.put("verteidigungspunkte", (float)0.3);
        }
        
        
        
        name = n;
        equipped_image = Toolkit.getDefaultToolkit().getImage(e_i);
        entity_image = Toolkit.getDefaultToolkit().getImage(i_i);
        effect = property;
    }
}
