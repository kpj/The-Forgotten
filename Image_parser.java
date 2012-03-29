import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Image_parser
{
    // Always scales to size of first image given

    BufferedImage output;
    ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();

    public Image_parser(ArrayList<String> path2imgs)
    {
        for (Object o : path2imgs) {
            String s = (String)o;
            
            try {
                imgs.add(ImageIO.read(this.getClass().getResource("/"+s)));
            }
            catch (IllegalArgumentException e) {
                //System.out.println("Error with images: "+e);
            }
            catch(IOException e) {
                //System.out.println("Error with images: "+e);
            }
        }
        
        merge_imgs();
    }
    
    public void merge_imgs() {
        int w = 100;
        int h = 100;
        if (!imgs.isEmpty()) {
            w = imgs.get(0).getWidth();
            h = imgs.get(0).getHeight();
        }
        
        output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        Graphics g = output.getGraphics();
        
        for (Object o : imgs) {
            BufferedImage cur = (BufferedImage)o;
            
            g.drawImage(cur, 0, 0, null);
        }
    }
    
    public BufferedImage get_img() {
        return output;
    }
}
