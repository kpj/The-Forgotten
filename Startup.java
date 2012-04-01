import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Startup
{
    content_handler content = new content_handler();
    applet_handler window;
    
    JPanel panel = new JPanel(new GridLayout(0,1));
    
    JButton sp = new JButton("Singleplayer");
    
    JButton mp = new JButton("Multiplayer");
    JTextField mp_ip = new JTextField("localhost");
    JTextField mp_port = new JTextField("4223");
    
    JButton servp = new JButton("Create Server");
    JTextField serv_port = new JTextField("4223");
    JTextField serv_path = new JTextField("data/fights/test.txt");

    public Startup()
    {
        content.window_width = 100;
        content.window_height = 300;
        
        window = new applet_handler(content);
        
        panel.add(sp);
        panel.add(new JSeparator());
        panel.add(mp);
        panel.add(mp_ip);
        panel.add(mp_port);
        panel.add(new JSeparator());
        panel.add(servp);
        panel.add(serv_port);
        panel.add(serv_path);
        
        ActionListener asp = new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                content.f.dispose();
                start_singleplayer();
            }
        };
        sp.addActionListener(asp);
        ActionListener msp = new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                content.f.dispose();
                start_multiplayer();
            }
        };
        mp.addActionListener(msp);
        ActionListener servsp = new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                content.f.dispose();
                start_server();
            }
        };
        servp.addActionListener(servsp);
        
        content.f.add(panel);
        content.f.pack();
    }
    
    public static void main (String[] args) {
        Startup startup = new Startup();
    }
    
    public void start_singleplayer() {
        System.out.println("Starting singleplayer");
        god good = new god();
        
        good.create_fight("data/fights/test.txt", false);
    }
    
    public void start_multiplayer() {
        System.out.println("Starting multiplayer");
        god good = new god();
        good.content.ip = mp_ip.getText();
        good.content.port = Integer.parseInt(mp_port.getText());
        
        good.create_fight("", true);
    }
    
    public void start_server() {
        System.out.println("Starting server");
        content.port = Integer.parseInt(serv_port.getText());
        Server serv = new Server(content, serv_path.getText());
    }
}
