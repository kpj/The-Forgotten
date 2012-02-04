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
    
    public ArrayList get_objects() {
        return objects;
    }
}