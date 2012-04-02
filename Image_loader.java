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
            pimg.put(path, new Image_parser(path).get_img());
        }
    }
}
