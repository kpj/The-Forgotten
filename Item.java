import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

public class Item implements Serializable
{
    ArrayList<ArrayList<String>> item_list = new ArrayList<ArrayList<String>>();
    ArrayList<HashMap<String, Integer>> item_effects = new ArrayList<HashMap<String, Integer>>();
    
    String name;
    HashMap<String, Integer> effect = new HashMap<String, Integer>();
    ImageIcon equipped_image;
    ImageIcon entity_image;
    
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

        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add(cur_item.get(1));
        equipped_image = new ImageIcon((new Image_parser(tmp)).get_img());
        tmp.clear();
        tmp.add(cur_item.get(2));
        entity_image = new ImageIcon((new Image_parser(tmp)).get_img());
        
        effect = item_effects.get(id);
    }
    
    @SuppressWarnings("unchecked")
    public void parse_items(String path) {
        FileReader fr;
        try {
            InputStream file_stream = getClass().getResourceAsStream(path);

            BufferedReader bufRead = new BufferedReader(new InputStreamReader(file_stream));

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
            item_effects.add( (HashMap<String, Integer>)now.clone() );

            bufRead.close();
        }
        catch (IOException e) {
            System.out.println("In parse_items: "+e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void parse_line(String l) {
        if (l.charAt(0) == '#')
            return;
        //System.out.println(l);
        String[] line = l.split(":");
        
        ArrayList<String> cur = new ArrayList<String>();
        if (line[0].equals("Item")) {
            if (now.size() != 0) {
                item_effects.add( (HashMap<String, Integer>)now.clone() );
                System.out.println((item_effects.size()-1)+" "+now);
                now.clear();
            }
            item_counter++;
            cur.add(line[1]); // Name
            cur.add(line[2]); // equipped image
            cur.add(line[3]); // entity image
            item_list.add(cur);
        }
        else {
            now.put(line[0], Integer.parseInt(line[1]));
        }
    }
}
