import java.io.*;
import java.util.*;

public class Map_parser
{
    content_handler content;

    String path;
    boolean parsing_map = false;
    int map_index = 0;
    
    int arg_num;

    ArrayList<ArrayList> elements = new ArrayList<ArrayList>();
    HashMap<Integer, Char> map = new HashMap<Integer, Char>();
    HashMap<String, ArrayList<Integer>> get_items = new HashMap<String, ArrayList<Integer>>();
    HashMap<String, ArrayList<Boolean>> get_items_equipped = new HashMap<String, ArrayList<Boolean>>();

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
    }

    public void parse_line(String l) {
        char[] line = l.toCharArray();
        if (line.length == 0) {
            parsing_map = true;
            return;
        }

        if (!parsing_map) {
            // Find extra things
            String[] splitted = l.split(":");
            arg_num = splitted.length;
            String imp = splitted[0];
            if (imp.length() != 1) {
                if (imp.equals("backgroundimg")) {
                    path2bg = splitted[1];
                }
                else if (imp.equals("Item")) {
                    ArrayList<Integer> il = new ArrayList<Integer>();
                    ArrayList<Boolean> iel = new ArrayList<Boolean>();
                    for (int i = 2 ; i < splitted.length ; i++) {
                        String parse_me;
                        if (splitted[i].length() >= 2) {
                            parse_me = Character.toString(splitted[i].toCharArray()[0]);
                            iel.add(true);
                        }
                        else {
                            parse_me = splitted[i];
                            iel.add(false);
                        }
                        il.add(Integer.parseInt(parse_me));
                    }
                    get_items.put(splitted[1], il);
                    get_items_equipped.put(splitted[1], iel);
                }
                return;
            }
            
            
            ArrayList<String> args = new ArrayList<String>();
            String cur = "";
            int counter = 0;
            for (Object o : line) {
                Character c = (Character)o;

                if (c != ':') {
                    cur += c;
                }
                else {
                    args.add(cur);
                    cur = "";
                    counter++;
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
                ArrayList<String> imgs = cut_string(l.get(2), ","); 
                
                Char ret = new Char(l.get(1), (float)0, (float)0, "", imgs, null, Integer.parseInt(l.get(3)), null);
                
                for (Map.Entry<String, ArrayList<Integer>> ob : get_items.entrySet()) {
                    for (Map.Entry<String, ArrayList<Boolean>> oo : get_items_equipped.entrySet()) {
                        if (l.get(1).equals(ob.getKey()) && l.get(1).equals(oo.getKey())) {
                            int counter = 0;
                            for (Object obj : ob.getValue()) {
                                int lol = Integer.parseInt(obj.toString()); // <- really ?!
                                ret.collect_item(new Item(lol));
                                if(oo.getValue().get(counter)) {
                                    ret.equip_item(counter);
                                }
                                counter++;
                            }
                        }
                    }
                }
                //System.out.println(arg_num);
                //if (arg_num == 3)
                    return ret;
                    
                //HashMap<String, Float> prop = new HashMap<String, Float>();
                
                //return new Char(l.get(1), (float)0, (float)0, "", l.get(2), null, 2, prop);
            }
        }
        return null;
    }
    
    public ArrayList<String> cut_string(String s, String d) {
        ArrayList<String> output = new ArrayList<String>();
        
        for (Object o : s.split(d)) {
            String str = (String)o;
            
            output.add(str);
        }
        
        return output;
    }
    
    public fight_handler get_fight() {
        return new fight_handler(path2bg, field_width, field_height, map, content);
    }
}