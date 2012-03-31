import java.io.*;
import java.util.*;

public class Data_packet implements Serializable
{
    ArrayList<ArrayList> field = null;

    @SuppressWarnings("unchecked")
    public Data_packet(ArrayList<ArrayList> f)
    {
        field = (ArrayList<ArrayList>)f.clone();
    }
}
