public class Place
{
    Char cur;
    int index;
    boolean checked = false;
    String special = "NOTHING";

    public Place(Char on, int ind)
    {
        cur = on;
        index = ind;
    }
}
