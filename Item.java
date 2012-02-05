import java.awt.*;
import java.util.*;

public class Item
{
    String name, n;
    String e_i, i_i;
    HashMap effect, property;
    Image equipped_image, entity_image;

    public Item(int id)
    {
        if (id == 0) {
            n = "Normales Holzschwert";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("geschwindigkeit", 0);
            property.put("lebenspunkte", 0);
            property.put("manapunkte", 0);
            property.put("ausdauerpunkte", 0);
            property.put("initiative", 0);
            property.put("magieresistenz", 0);
            property.put("angriffskraft", 1);
            property.put("verteidigungspunkte", 0);
        }
        else if (id == 1) {
            n = "Normales Holzschild";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("geschwindigkeit", 0);
            property.put("lebenspunkte", 0);
            property.put("manapunkte", 0);
            property.put("ausdauerpunkte", 0);
            property.put("initiative", 0);
            property.put("magieresistenz", 0);
            property.put("angriffskraft", 0);
            property.put("verteidigungspunkte", 1);
        }
        else if (id == 2) {
            n = "Gemeine Reiserobe";
            e_i = "path/to/pic";
            i_i = "path/to/pic";
            
            property.put("geschwindigkeit", 0);
            property.put("lebenspunkte", 0);
            property.put("manapunkte", 0);
            property.put("ausdauerpunkte", 0);
            property.put("initiative", 0);
            property.put("magieresistenz", 0);
            property.put("angriffskraft", 0);
            property.put("verteidigungspunkte", 0.3);
        }
        
        
        
        name = n;
        equipped_image = Toolkit.getDefaultToolkit().getImage(e_i);
        entity_image = Toolkit.getDefaultToolkit().getImage(i_i);
        effect = property;
    }
}
