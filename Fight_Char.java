import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Fight_Char
{
    HashMap<String, Integer> property = new HashMap<String, Integer>();
    Image fight_image;
    ImageIcon icon;
    int fight_image_width, fight_image_height;

    public Fight_Char(Char c)
    {
        property = c.property;
        
        fight_image = Toolkit.getDefaultToolkit().getImage(c.fighti);
        icon = new ImageIcon(fight_image);
        fight_image_height = icon.getIconHeight();
        fight_image_width = icon.getIconWidth();
    }
}
