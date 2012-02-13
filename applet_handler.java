import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class applet_handler implements KeyListener, MouseListener
{
    draw_anything drawer = new draw_anything();
    key_handler keyer = new key_handler();
    mouse_handler mice = new mouse_handler();
    Set<Character> pressed = new HashSet<Character>();
    
    int window_width = 600;
    int window_height = 500;
    
    boolean left_mouse = false;
    boolean right_mouse = false;
    boolean middle_mouse = false;
    

    public applet_handler()
    {
        JFrame f = new JFrame("The Forgotten");
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize(window_width, window_height);
        f.add(drawer);
        f.setVisible(true);
        
        f.addMouseListener(this);
        f.addKeyListener(this);
        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((int)d.getWidth()/2 - window_width/2, (int)d.getHeight()/2 - window_height/2);
        
        keyer.start();
    }
    
    public void set_objects(ArrayList<Thing> ol) {
        drawer.set_objects(ol);
    }
    public void set_characters(ArrayList<Char> cl) {
        keyer.set_characters(cl);
        drawer.set_characters(cl);
    }
    
    public int get_window_width() {
        return window_width;
    }
    public int get_window_height() {
        return window_height;
    }
    
    public void give_fight_handler(fight_handler fi) {
        drawer.give_fight_handler(fi);
        keyer.give_fight_handler(fi);
    }
    public void give_world_handler(world_handler wo) {
        drawer.give_world_handler(wo);
        keyer.give_world_handler(wo);
    }
    
    public void repaint() {
        drawer.repaint();
    }
    
    // Stuff for mousy mouse
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
        switch(e.getModifiers()) {
            case InputEvent.BUTTON1_MASK: {
                // LEFT     
                left_mouse = true;
                break;
            }
            case InputEvent.BUTTON2_MASK: {
                // MIDDLE    
                middle_mouse = true;
                break;
            }
            case InputEvent.BUTTON3_MASK: {
                // RIGHT   
                right_mouse = true;
                break;
            }
        }
    }
    
    // Stuff for multiple key detection
    @Override
    public synchronized void keyPressed(KeyEvent e) {
        pressed.add(e.getKeyChar());
        keyer.set_key_list(pressed);
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        pressed.remove(e.getKeyChar());
        keyer.set_key_list(pressed);
    }

    @Override
    public void keyTyped(KeyEvent e) {/* Not used */ }
}
