import java.io.*;
import java.util.*;

public class Data_packet implements Serializable
{
    ArrayList<ArrayList> field = null;
    HashMap<Integer, Place> changes = null;
    
    boolean my_turn = false;
    int num;

    Map_parser mapper;
    
    @SuppressWarnings("unchecked")
    public Data_packet(ArrayList<ArrayList> f, boolean turn, Map_parser ama, int n)
    {
        field = (ArrayList<ArrayList>)f.clone();
        my_turn = turn;
        mapper = ama;
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
