import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;

public class applet_handler implements KeyListener, MouseListener, MouseMotionListener, ComponentListener, MouseWheelListener
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
        
        content.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content.f.setSize(content.window_width, content.window_height);
        content.f.add(drawer);
        content.f.setVisible(true);
        
        BufferedImage mouse_img = new Image_parser("pics/Icons/Maus/Schwerticon.png").get_img();
        Point hotSpot = new Point(0,0);
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(mouse_img, hotSpot, "Pencil");
        content.f.setCursor(cursor);
        
        content.f.addMouseListener(this);
        content.f.addMouseMotionListener(this);
        content.f.addKeyListener(this);
        content.f.addComponentListener(this);
        content.f.addMouseWheelListener(this);
        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        content.f.setLocation((int)d.getWidth()/2 - content.window_width/2, (int)d.getHeight()/2 - content.window_height/2);
        
        keyer.start();
    }
    
    public void repaint() {
        drawer.repaint();
    }    
    
    // Stuff for mousy mouse
    public void mousePressed(MouseEvent e) {
        get_mouse_pos();
        
        // make nice mouse animation
        content.show_mouse_animation = true;
        
        switch(e.getModifiers()) {
            case InputEvent.BUTTON1_MASK: {
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_press("LEFT");
                break;
            }
            case InputEvent.BUTTON2_MASK: {
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_press("MIDDLE");
                break;
            }
            case InputEvent.BUTTON3_MASK: {
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_press("RIGHT");
                break;
            }
        }
    }
    public void mouseReleased(MouseEvent e) {
        get_mouse_pos();
        switch(e.getModifiers()) {
            case InputEvent.BUTTON1_MASK: {
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_release("LEFT");
                break;
            }
            case InputEvent.BUTTON2_MASK: {
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_release("MIDDLE");
                break;
            }
            case InputEvent.BUTTON3_MASK: {
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_release("RIGHT");
                break;
            }
        }
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {
        get_mouse_pos();
        switch(e.getModifiers()) {
            case InputEvent.BUTTON1_MASK: {
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_drag("LEFT");
                break;
            }
            case InputEvent.BUTTON2_MASK: {
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_drag("MIDDLE");
                break;
            }
            case InputEvent.BUTTON3_MASK: {
                if (content.fight_active)
                    ((fight_handler)content.get_active_environment()).on_drag("RIGHT");
                break;
            }
        }
    }
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
                    ((map_handler)content.get_active_environment()).on_click("RIG");
                break;
            }
        }
    }
    public void get_mouse_pos() {
        content.mouse_x = (int) MouseInfo.getPointerInfo().getLocation().x - content.f.getLocationOnScreen().x;
        content.mouse_y = (int) MouseInfo.getPointerInfo().getLocation().y - content.f.getLocationOnScreen().y;
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
    
    
    
    // stuff for window interactions
    public void componentHidden(ComponentEvent e) {
    /*System.out.println("componentHidden event from "
               + e.getComponent().getClass().getName());*/
    }

    public void componentMoved(ComponentEvent e) {
        Component c = e.getComponent();
        /*System.out.println("componentMoved event from "
                       + c.getClass().getName()
                       + "; new location: "
                       + c.getLocation().x
                       + ", "
                       + c.getLocation().y);*/
    }

    public void componentResized(ComponentEvent e) {
        Component c = e.getComponent();
        /*System.out.println("componentResized event from "
                       + c.getClass().getName()
                       + "; new size: "
                       + c.getSize().width
                       + ", "
                       + c.getSize().height);*/
        content.window_width = c.getSize().width;
        content.window_height = c.getSize().height;
    }

    public void componentShown(ComponentEvent e) {
    /*System.out.println("componentShown event from "
               + e.getComponent().getClass().getName());*/
    }
    
    
    // stuff for mousewheel
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
       
        if (content.fight_active)
            content.fight.mousewheel_used(e.getUnitsToScroll());    
    }
}
