import java.util.*;
import java.net.*;
import java.io.*;

public class Server
{
    content_handler content;

    ServerSocket server;
    
    boolean listening = true;
    
    int num = 0;
    
    String path2map = "data/fights/test.txt";
    
    public Server(content_handler con)
    {
        content = con;
        
        try {
            server = new ServerSocket(content.port);
            System.out.println("Listening on port "+content.port);
            while (listening) {
                num++;
                Socket s = server.accept();
                Client c = new Client(s, content, num);
                make_playable(c);
                content.connected.add(c);
                c.start();
                System.out.println("Incoming connection #"+num+": "+s.getInetAddress().getHostAddress());
            }
            
            server.close();
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            e.printStackTrace();
        }
        
    }
    
    public void make_playable(Client c) {
        Map_parser ma = new Map_parser(path2map);
        ArrayList<ArrayList> cur = ma.get_field();
        boolean turn = false;
        if (c.num == 1)
            turn = true;
        Data_packet dp = new Data_packet(cur, turn, ma);
        c.send_data(dp);
    }
    
    public void check_disconnected() {
        ArrayList<Client> to_rm = new ArrayList<Client>();
        for (Object o : content.connected) {
            Client c = (Client)o;
            if (c.disconnected) {
                System.out.println("["+c.name+"] Will disconnect");
                to_rm.add(c);
            }
        }
        content.connected.removeAll(to_rm);
        to_rm.clear();
    }
}