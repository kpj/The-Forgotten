import java.util.*;
import java.net.*;
import java.io.*;

public class Server
{
    content_handler content;

    ServerSocket server;

    int port = 4223;
    
    boolean listening = true;
    
    public Server(int p, content_handler con)
    {
        content = con;
        //port = p;
        
        try {
            server = new ServerSocket(port);
            System.out.println("Listening on port "+port);
            while (listening) {
              Socket s = server.accept();
              Client c = new Client(s, content);
              content.connected.add(c);
              c.start();
              System.out.println("Incoming connection: "+s.getInetAddress().getHostAddress());
            }
            
            server.close();
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            e.printStackTrace();
        }
        
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