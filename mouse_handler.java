import java.awt.*;
import java.util.*;

public class mouse_handler
{
    content_handler content;

    public mouse_handler(content_handler con)
    {
        content = con;
    }
    
    public ArrayList get_mouse_pos() {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        
        int x = (int) b.getX();
        int y = (int) b.getY();
        
        ArrayList<Integer> coords = new ArrayList<Integer>();
        coords.add(x);
        coords.add(y);
        
        return coords;
    }
}
