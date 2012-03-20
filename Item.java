import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Item
{
    ArrayList<ArrayList<String>> item_list = new ArrayList<ArrayList<String>>();
    HashMap<Integer, HashMap<String, Integer>> item_effects = new HashMap<Integer, HashMap<String, Integer>>();

    String name;
    HashMap<String, Integer> effect = new HashMap<String, Integer>();
    BufferedImage equipped_image;
    BufferedImage entity_image;
    
    boolean is_in_use = false;
    
    int item_counter = -1;
    HashMap<String, Integer> now = new HashMap<String, Integer>();

    public Item(int id)
    {   
        parse_items("pics/item_list.txt");
        
        ArrayList<String> cur_item;
        try {
            cur_item = item_list.get(id);
        }
        catch (IndexOutOfBoundsException e) {
            //System.out.println(e);
            return;
        }
        
        name = cur_item.get(0);
        
        try {
            equipped_image = ImageIO.read(new File(cur_item.get(1)));
            entity_image = ImageIO.read(new File(cur_item.get(2)));;
        }
        catch (IOException e) {
            //System.out.println("Error with images: "+e);
        }
        
        effect = item_effects.get(id);
    }
    
    public void parse_items(String path) {
        FileReader fr;
        try {
            fr = new FileReader(path);

            BufferedReader bufRead = new BufferedReader(fr);

            String line = "";
            int counter = 0;

            line = bufRead.readLine();
            counter++;

            while (line != null) {
                parse_line(line);

                //System.out.println(counter + ". " + line);
                line = bufRead.readLine();
                counter++;
            }

            bufRead.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void parse_line(String l) {
        String[] line = l.split(":");
        
        ArrayList<String> cur = new ArrayList<String>();
        if (line[0].equals("Item")) {
            now.clear();
            item_counter++;
            cur.add(line[1]); // Name
            cur.add(line[2]); // equipped image
            cur.add(line[3]); // entity image
            item_list.add(cur);
        }
        else {
            now.put(line[0], Integer.parseInt(line[1]));
            item_effects.put(item_counter, now);
        }
        
        
        item_list.add(cur);
    }
}
