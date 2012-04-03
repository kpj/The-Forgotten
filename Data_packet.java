import java.io.*;
import java.util.*;

public class Data_packet implements Serializable
{
    ArrayList<ArrayList> field = null;
    HashMap<Integer, Place> changes = null;
    
    boolean my_turn = false;
    int num;
    
    int field_width, field_height;
    String path2bg;

    Map_parser mapper;
    
    boolean nr = true;
    
    @SuppressWarnings("unchecked")
    public Data_packet(ArrayList<ArrayList> f, boolean turn, int w, int h, String p2b, int n)
    {
        field = (ArrayList<ArrayList>)f.clone();
        my_turn = turn;
        field_width = w;
        field_height = h;
        path2bg = p2b;
        num = n;
    }
    @SuppressWarnings("unchecked")
    public Data_packet(HashMap<Integer, Place> c, boolean turn, int n)
    {
        changes = (HashMap<Integer, Place>)c.clone();
        my_turn = turn;
        num = n;
    }
}
