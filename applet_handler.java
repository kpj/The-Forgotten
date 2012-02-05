import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class applet_handler implements KeyListener
{
    draw_anything drawer = new draw_anything();
    key_handler keyer = new key_handler();
    Set<Character> pressed = new HashSet<Character>();

    public applet_handler()
    {
        JFrame f = new JFrame("The Forgotten");
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize(600, 500);
        f.add(drawer);
        f.setVisible(true);
        f.addKeyListener(this);
        
        //Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        keyer.start();
    }
    
    public void set_objects(ArrayList ol) {
        drawer.set_objects(ol);
    }
    public void set_characters(ArrayList cl) {
        keyer.set_characters(cl);
        drawer.set_characters(cl);
    }
    
    public void repaint() {
        drawer.repaint();
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
