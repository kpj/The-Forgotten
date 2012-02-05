import java.awt.*;
import java.util.*;

public class Item
{
    int ID;
    String name;
    Image equipped_image, entity_image;
    HashMap effect;

    public Item(int id, String n, String e_i, String i_i, HashMap e)
    {
        name = n;
        ID = id;
        equipped_image = Toolkit.getDefaultToolkit().getImage(e_i);
        entity_image = Toolkit.getDefaultToolkit().getImage(i_i);
        effect = e;
    }
}
