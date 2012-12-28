import java.util.*;

public class Sound_loader
{
    HashMap<String, Sound> psound = new HashMap<String, Sound>();

    public Sound_loader(ArrayList<String> paths)
    {
        for (Object o : paths) {
            String path = (String)o;
            add_sound(path, new Sound(path));
        }
    }
    
    public void add_sound (String path, Sound s) {
        psound.put(path, s);
    }
    public Sound get_sound(String path) {
        return psound.get(path);
    }
}
