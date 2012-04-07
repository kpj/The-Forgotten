import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.jar.JarEntry;

public class Startup
{
    content_handler content = new content_handler();
    applet_handler window;
    
    String fight_directory = "data/fights/";
    
    JPanel panel = new JPanel(new GridLayout(0,1));
    
    JButton sp = new JButton("Singleplayer");
    
    JButton mp = new JButton("Multiplayer");
    JTextField mp_ip = new JTextField("localhost");
    JTextField mp_port = new JTextField("4223");
    
    JButton servp = new JButton("Create Server");
    JTextField serv_port = new JTextField("4223");
    
    String chosen_fight = "";
    JComboBox fights;

    public Startup()
    {
        System.out.println(get_own_path() + " " + get_own_name());
        
        content.window_width = 100;
        content.window_height = 300;
        
        window = new applet_handler(content);
        fights = new JComboBox(dirList(fight_directory));
        
        panel.add(sp);
        panel.add(new JSeparator());
        panel.add(mp);
        panel.add(mp_ip);
        panel.add(mp_port);
        panel.add(new JSeparator());
        panel.add(servp);
        panel.add(serv_port);
        panel.add(fights);
        
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
        
        ActionListener fighties = new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                JComboBox cb = (JComboBox)e.getSource();
                String choice = (String)cb.getSelectedItem();
                chosen_fight = choice;
            }
        };
        fights.addActionListener(fighties);
        fights.setSelectedIndex(0);
        
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
        Server serv = new Server(content, fight_directory+chosen_fight);
    }
    
    
    public String get_own_name() {
        String[] tmp = getClass().getClassLoader().getResource("data").toString().split("!")[0].split("/");
        return tmp[tmp.length-1];
    }
    public String get_own_path() {
        String tmp = getClass().getClassLoader().getResource("data").toString().split("!")[0];
        tmp = tmp.replaceFirst("jar:file:", "");
        String[] ump = tmp.split("/");
        String out = "";
        for (int i = 0 ; i < ump.length-1 ; i++) {
            out += ump[i];
            out += "/";
        }
        return out;
    }
    
    public String[] dirList(String dir){
        try {
            String filename = get_own_name();
            if(filename.contains(".jar")){
                ArrayList<String> files = new ArrayList<String>();
                String[] tmp = filename.split("!");
                filename=tmp[0].replaceFirst("file:/", "");                     

                java.util.jar.JarFile jarFile = new java.util.jar.JarFile(get_own_path()+filename);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements())
                {
                    java.util.zip.ZipEntry entry = entries.nextElement();
                    if(entry.getName().startsWith(dir)){
                        String tmp2 = entry.getName().replaceFirst(dir, "");
                        if(!tmp2.isEmpty()){
                            if(tmp2.charAt(tmp2.length()-1)=='/'){
                                tmp2=tmp2.substring(0, tmp2.length()-1);
                            }
                            files.add(tmp2);
                        }

                    }
                }
                Object[] ob = files.toArray();
                String files2[] = new String[ob.length];
                for (int i = 0; i < files2.length; i++) {
                    files2[i]=(String) ob[i];
                }
                return files2;
            }else{
                return new File(dir).list();
            }
        }catch (IOException e){
            System.err.println("error: "+dir+"\n"+e.getMessage());
        }
        return null;
    }
}
