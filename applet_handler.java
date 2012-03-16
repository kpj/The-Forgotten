import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class applet_handler implements KeyListener, MouseListener
{
    content_handler content;
    draw_anything drawer;

    JFrame f;
    
    key_handler keyer;
    Set<Character> pressed = new HashSet<Character>();
    
    boolean left_mouse = false;
    boolean right_mouse = false;
    boolean middle_mouse = false;
    

    public applet_handler(content_handler con)
    {
        content = con;
        drawer = new draw_anything(content);
        keyer = new key_handler(content);
        
        f = new JFrame("The Forgotten");
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize(content.window_width, content.window_height);
        f.add(drawer);
        f.setVisible(true);
        
        f.addMouseListener(this);
        f.addKeyListener(this);
        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((int)d.getWidth()/2 - content.window_width/2, (int)d.getHeight()/2 - content.window_height/2);
        
        keyer.start();
    }
    
    public void repaint() {
        drawer.repaint();
    }    
    
    // Stuff for mousy mouse
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
        get_mouse_pos();
        
        switch(e.getModifiers()) {
            case InputEvent.BUTTON1_MASK: {
                // LEFT     
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_click("LEFT");
                if (content.world_active)
                    ((world_handler)content.get_active_environment()).on_click("LEFT");
                if (content.map_active)
                    ((map_handler)content.get_active_environment()).on_click("LEFT");
                break;
            }
            case InputEvent.BUTTON2_MASK: {
                // MIDDLE    
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_click("MIDDLE");
                if (content.world_active)
                    ((world_handler)content.get_active_environment()).on_click("MIDDLE");
                if (content.map_active)
                    ((map_handler)content.get_active_environment()).on_click("MIDDLE");
                break;
            }
            case InputEvent.BUTTON3_MASK: {
                // RIGHT   
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_click("RIGHT");
                if (content.world_active)
                    ((world_handler)content.get_active_environment()).on_click("RIGHT");
                if (content.map_active)
                    ((map_handler)content.get_active_environment()).on_click("RIGHT");
                break;
            }
        }
    }
    public void get_mouse_pos() {
        content.mouse_x = (int) MouseInfo.getPointerInfo().getLocation().x - f.getLocationOnScreen().x;
        content.mouse_y = (int) MouseInfo.getPointerInfo().getLocation().y - f.getLocationOnScreen().y;
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
