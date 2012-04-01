import java.io.*;
import java.util.*;

public class Data_packet implements Serializable
{
    ArrayList<ArrayList> field = null;
    boolean my_turn = false;
    int num;

    Map_parser mapper;
    
    @SuppressWarnings("unchecked")
    public Data_packet(ArrayList<ArrayList> f, boolean turn, int n)
    {
        field = (ArrayList<ArrayList>)f.clone();
        my_turn = turn;
        num = n;
    }
    @SuppressWarnings("unchecked")
    public Data_packet(ArrayList<ArrayList> f, boolean turn, Map_parser ama, int n)
    {
        field = (ArrayList<ArrayList>)f.clone();
        my_turn = turn;
        mapper = ama;
        num = n;
    }
}
