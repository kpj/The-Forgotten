// std libs
import java.util.*;

public class world_handler
{
    ArrayList objects = new ArrayList();

    public world_handler()
    {
        
    }
    
    public void draw_all() {
        for (int i = 0 ; i < objects.size() ; i++) {
            Object current = (Object)objects.get(i);
            current.draw();
        }
    }
    
    public void add_object(Object o) {
        objects.add(o);
    }
}