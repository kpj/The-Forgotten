import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Image_loader
{
    HashMap<String, BufferedImage> pimg = new HashMap<String, BufferedImage>(); 

    public Image_loader(ArrayList<String> paths)
    {
        for (Object o : paths) {
            String path = (String)o;
            add_img(path, new Image_parser(path).get_img());
        }
    }
    
    public void add_img (String path, BufferedImage img) {
        pimg.put(path, img);
    }
    public BufferedImage get_img(String path) {
        return pimg.get(path);
    }
}
