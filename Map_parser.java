import java.io.*;
import java.util.*;

public class Map_parser implements Serializable
{
    String path;
    boolean parsing_map = false;
    int map_index = 0;
    
    int arg_num;

    ArrayList<ArrayList> elements = new ArrayList<ArrayList>();
    HashMap<Integer, Char> map = new HashMap<Integer, Char>();
    HashMap<String, ArrayList<Integer>> get_items = new HashMap<String, ArrayList<Integer>>();
    HashMap<String, ArrayList<Boolean>> get_items_equipped = new HashMap<String, ArrayList<Boolean>>();

    int field_width = 0;
    int field_height = 0;
    String path2bg = "";
    
    ArrayList<ArrayList> field = new ArrayList<ArrayList>();

    public Map_parser(String p) {
        path = p;
        InputStream file_stream = getClass().getResourceAsStream(path);
        FileReader fr = null;

        try {
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
            
            bufRead.close();
        }
        catch (IOException e) {
            System.out.println("Mapper: "+e);
        }
    }

    public void parse_line(String l) {
        if (l.length() != 0 && l.charAt(0) == '#')
            return;
        
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
                        String parse_me = "";
                        if (splitted[i].charAt(splitted[i].length()-1) == 'e') {
                            for (int ii = 0 ; ii < splitted[i].length()-1 ; ii++) {
                                parse_me += Character.toString(splitted[i].charAt(ii));
                            }
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

                Char ret = new Char(l.get(1), (float)0, (float)0, new ArrayList<String>(), imgs, null, Integer.parseInt(l.get(3)), null);
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
                return ret;
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
    
    @SuppressWarnings("unchecked")
    public ArrayList<ArrayList> get_field() {
        for (int i = 0; i < field_width ; i++) {
            field.add(new ArrayList<Place>());
        }
        int ind = 0;
        for (int i = 0 ; i < field.size() ; i++) {
            for (int o = 0 ; o < field_height ; o++) {
                field.get(i).add(new Place(null, ind));
                ind++;
            }
        }
        
        // Adding chars
        for (Map.Entry<Integer, Char> o : map.entrySet()) {
            // Add special fields
            if (o.getValue() == null) {
                place_char2(o.getKey(), o.getValue());
            }
            else if (o.getValue().name.equals("NON-WALKABLE")) {
                (get_place(o.getKey())).special = "NON-WALKABLE";
            }
            else {
                place_char2(o.getKey(), o.getValue());
            }
        }
        return field;
    }
    public ArrayList<Char> get_ini_table() {
        return null;
    }
    
    public void place_char2(int pos, Char c) {
        if (c == null) {
            return;
        }
        for (Object o : field) {
            ArrayList l = (ArrayList) o;
            for (Object ob : l) {
                Place p = (Place) ob;
                if (p.index == pos)
                    p.cur = c;
            }
        }
    }
    public Place get_place(int pos) {
        for (Object o : field) {
            ArrayList l = (ArrayList)o;
            for (Object ob : l) {
                if (pos == ((Place)ob).index) {
                    return (Place)ob;
                }
            }
        }
        return null;
    }
}