import java.util.*;

public class fight_handler
{
    boolean is_over = false;

    public fight_handler(ArrayList chars)
    {
        System.out.println("I am a fight handler!");
    }
    
    public void fight_is_over (){
        is_over = true;
    }
    public boolean is_over() {
        return is_over;
    }
}
