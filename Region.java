import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Region
{
    int pos_x, pos_y;
    Image image;

    public Region(int x, int y, Image img)
    {
        pos_x = x;
        pos_y = y;
        image = img;
    }
}
