import java.io.*;

public class Place implements Serializable
{
    Char cur;
    int index;
    boolean checked = false;
    String special = "NOTHING";
    Place ancestor = null;

    public Place(Char on, int ind)
    {
        cur = on;
        index = ind;
    }
}
