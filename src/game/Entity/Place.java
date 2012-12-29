package game.Entity;
import java.io.*;

public class Place implements Serializable
{
    public Char cur;
    public int index;
    public boolean checked = false;
    public String special = "NOTHING";
    public Place ancestor = null;

    public Place(Char on, int ind)
    {
        cur = on;
        index = ind;
    }
}
