package loader;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import parser.Image_parser;

public class Image_loader
{
    public HashMap<String, BufferedImage> pimg = new HashMap<String, BufferedImage>(); 

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
    	if(path.charAt(0) == '/') {
    		path = path.substring(1);
    	}
        return pimg.get(path);
    }
}
