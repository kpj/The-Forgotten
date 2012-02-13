import java.awt.*;
import java.util.*;

public class mouse_handler
{
    public mouse_handler()
    {
        
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
