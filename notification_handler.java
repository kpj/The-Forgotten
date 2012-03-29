import java.util.*;
import java.awt.*;

public class notification_handler
{
    content_handler content;
    
    int x, y;
    
    int noti_height = 50;
    
    ArrayList<noti_box> noti = new ArrayList<noti_box>();
    ArrayList<noti_box> to_rm = new ArrayList<noti_box>();
    long duration = 1;
    
    public notification_handler(content_handler con)
    {
        content = con;
        calc_edges();
    }
    
    public void calc_edges() {
        x = (int)content.window_width/3;
        y = 0;
    }
    
    public void draw_stuff(Graphics g3, draw_anything imo) {
        Graphics2D g = (Graphics2D)g3;
        
        for (Object o : noti) {
            noti_box n = (noti_box)o;
            if (n.kill_me)
                to_rm.add(n);
            n.draw(g);
        }
        noti.removeAll(to_rm);
        to_rm.clear();
    }
    
    public void add_noti(String msg) {
        noti.add(new noti_box(msg, duration, new Date().getTime(), x, y + noti.size()*15, content.window_width/3, 40));
    }


    private class noti_box {
        String cap;
        long dur;
        long st;
        boolean kill_me = false;
        int x,y,w,h;
        
        public noti_box (String caption, long duration, long start_time, int xx, int yy, int ww, int hh) {
            cap = caption;
            dur = duration;
            st = start_time;
            x=xx;
            y=yy;
            w=ww;
            h=hh;
        }
        
        public boolean still_exists() {
            if (new Date().getTime() - st > dur * 1000) {
                return false;
            }
            return true;
        }
        
        public void draw(Graphics2D g) {
            g.setColor(Color.blue);
            g.fillRect(x, y, w, h);
            g.setColor(Color.black);
            g.drawString(cap, x + 20, y + 15);
            
            if(!still_exists()) {
                kill_me = true;
            }
        }
    }
}