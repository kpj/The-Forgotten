import java.io.*;
import java.util.*;

public class Map_parser
{
    content_handler content;

    String path;
    boolean parsing_map = false;
    int map_index = 0;

    ArrayList<ArrayList> elements = new ArrayList<ArrayList>();
    HashMap<Integer, Char> map = new HashMap<Integer, Char>();

    int field_width = 0;;
    int field_height = 0;
    String path2bg = "";

    public Map_parser(String p, content_handler con) {
        content = con;
        path = p;
        FileReader fr = null;

        //System.out.println("Loading: "+path);
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
        //System.out.println(elements);
        //System.out.println();
        //System.out.println(field_width + ":" + field_height);
        //System.out.println(map);
    }

    public void parse_line(String l) {
        char[] line = l.toCharArray();
        if (line.length == 0) {
            parsing_map = true;
            return;
        }
        // Find extra things
        String[] splitted = l.split(":");
        String imp = splitted[0];
        if (imp.length() != 0) {
            if (imp.equals("backgroundimg")) {
                path2bg = splitted[1];
            }
        }

        if (!parsing_map) {
            ArrayList<String> args = new ArrayList<String>();

            String cur = "";
            for (Object o : line) {
                Character c = (Character)o;

                if (c != ':') {
                    cur += c;
                }
                else {
                    args.add(cur);
                    cur = "";
                }
            }
            elements.add(args);
        }
        else {
            field_height++;
            field_width = line.length;

            for (Object o : line) {
                Character c = (Character)o;

                
                Char real_char = gen_char(c);
                
                map.put(map_index, real_char);

                map_index++;
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public Char gen_char(Character c) {
        for (Object o : elements) {
            ArrayList<String> l = (ArrayList<String>)o;
            
            if (l.get(0).charAt(0) == c) {
                return new Char(l.get(1), (float)0, (float)0, "", l.get(2), null, 2);
            }
        }
        return null;
    }
    
    public fight_handler get_fight() {
        return new fight_handler(path2bg, field_width, field_height, map, content);
    }
}