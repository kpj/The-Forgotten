// std libs
import java.util.*;

public class world_handler
{
    ArrayList objects = new ArrayList();

    public world_handler()
    {
        
    }
    
    public void add_object(Object o) {
        objects.add(o);
    }
    public void rm_object(Object o) {
        objects.remove(o);
    }
    
    public void draw_all() {
        for (int i = 0 ; i < objects.size() ; i++) {
            objects.get(i);
        }
    }
}