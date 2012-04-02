import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.jar.JarEntry;

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
    
    String chosen_fight = "";
    JComboBox fights;

    public Startup()
    {
        content.window_width = 100;
        content.window_height = 300;
        
        window = new applet_handler(content);
        fights = new JComboBox(dirList("data/fights/"));
        
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
        //fights.setSelectedIndex(0);
        
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
        Server serv = new Server(content, chosen_fight);
    }
    
    public String[] list_items(String path) {
        URL url = getClass().getClassLoader().getResource(path);
        
        //System.out.println(path);
        //System.out.println(url.getPath());
        
        InputStream file_stream = getClass().getResourceAsStream(path);
        BufferedReader bufRead = new BufferedReader(new InputStreamReader(file_stream));
        
        ArrayList<String> out = new ArrayList<String>();
        
        String line = "lolz";
        try { 
            //while(!bufRead.ready())
            //    System.out.println(file_stream.available());
            line = bufRead.readLine();
            //System.out.println(line);
            while (line != null) {
                out.add(line);
                System.out.println(line);
                line = bufRead.readLine();
                System.out.println(line);
                line = bufRead.readLine();
                System.out.println(line);
            }
            
            bufRead.close();
        }
        catch (IOException e) {
            System.out.println("Startup: "+e);
        }
        catch (NullPointerException e) {
            System.out.println("Startup: "+e);
            e.printStackTrace();
        }
        String[] outar = out.toArray(new String[]{});

        return outar;
    }
    
    public String[] dirList(String dir){
        try{
            //String filename = getClass().getClassLoader().getResource(getClass().getPackage().getName().replaceAll("\\.", "/")).getFile();
            String filename = "The-Forgotten.jar";
            if(filename.contains(".jar")){
                ArrayList<String> files = new ArrayList<String>();
                String[] tmp = filename.split("!");
                filename=tmp[0].replaceFirst("file:/", "");                     

                java.util.jar.JarFile jarFile = new java.util.jar.JarFile(filename);
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
        }catch (Exception e){
            System.err.println("Fehler beim Listen des Verzeichnises: "+dir+"\n"+e.getMessage());
        }
        return null;
    }
}
