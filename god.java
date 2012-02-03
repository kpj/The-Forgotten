import java.lang.*;
import java.util.*;

public class god
{
    world_handler world = new world_handler();

    public god()
    {

    }
    
    public void live() {
        while(true) {
            world.draw_all();
            world.add_object(new Object ("lol"));
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                System.out.println("Nicht warten?");
            }
        }
    }
}
