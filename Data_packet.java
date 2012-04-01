import java.io.*;
import java.util.*;

public class Data_packet implements Serializable
{
    ArrayList<ArrayList> field = null;
    boolean my_turn = false;

    Map_parser mapper;
    
    @SuppressWarnings("unchecked")
    public Data_packet(ArrayList<ArrayList> f, boolean turn)
    {
        field = (ArrayList<ArrayList>)f.clone();
        my_turn = turn;
    }
    @SuppressWarnings("unchecked")
    public Data_packet(ArrayList<ArrayList> f, boolean turn, Map_parser ama)
    {
        field = (ArrayList<ArrayList>)f.clone();
        my_turn = turn;
        mapper = ama;
    }
}
