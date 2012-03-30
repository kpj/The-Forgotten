import java.util.*;
import java.net.*;
import java.io.*;

public class Client extends Thread
{
    content_handler content;
    
    Socket client;
    
    String name;
    
    boolean disconnected = false;
    
    ArrayList<ArrayList> cur = null;
    ArrayList<ArrayList> old = null;
    
    public Client(Socket socket, content_handler con) {
        super("Client");
        
        content = con;

        client = socket;
        name = socket.getInetAddress().getHostAddress();
        
        Field_getter fg = new Field_getter(this, content);
        fg.start();
    }
    @SuppressWarnings("unchecked")
    public void run() {
        ArrayList<ArrayList> tmp = new ArrayList<ArrayList>();
        while (true) {
            tmp = recv_arraylist();
            
            if (tmp != null) {
                cur = (ArrayList<ArrayList>)tmp.clone();
                tmp = null;
            }
        }
    }
    @SuppressWarnings("unchecked")
    public synchronized void send_field() {
        if (cur == null || cur == old) {
            return;
        }
        
        System.out.println("Received valid data ("+cur.size()+")");
        
        for (Object o : content.connected) {
            Client c = (Client)o;
            //if (c == this)
            //    break;
            c.send_arraylist(cur);
        }
        old = (ArrayList<ArrayList>)cur.clone();
    }
    
    public void send_arraylist(ArrayList<ArrayList> list) {
        try {
            OutputStream out = client.getOutputStream();
            byte[] bal = serialize(list);
            System.out.println("Sending "+bal.length+" bytes");
            out.write(bal);
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            e.printStackTrace();
        }
    }
    
    int size = 11533830;
    int cur_size = 0;
    int read_size = 65536;
    byte[] ba = new byte[size];
    @SuppressWarnings("unchecked")
    public ArrayList<ArrayList> recv_arraylist() {
        try {
            InputStream in = client.getInputStream();
            
            int read_bytes = in.read(ba, cur_size, Math.min(read_size, size-cur_size));
            if (read_bytes == -1) {
                //System.out.println("No data received");
                return null;
            }
            //System.out.println("Added "+read_bytes+" to "+cur_size+" bytes into "+ba.length);
            
            cur_size += read_bytes;
            if (cur_size == size) {
                cur_size = 0;
                ArrayList<ArrayList> al = (ArrayList<ArrayList>)deserialize(ba);
                return al;
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            e.printStackTrace();
            return null;
        }
    }
    
    public static byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            return out.toByteArray();
        }
        catch (IOException e) {
            System.out.println("[S] error: "+e);
            e.printStackTrace();
            return null;
        }
    }
    public static Object deserialize(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        }
        catch (IOException e) {
            System.out.println("[DES] error: "+e);
            e.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException e) {
            System.out.println("error: "+e);
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    public class Field_getter extends Thread {
        content_handler content;
        Client parent;
        public Field_getter(Client par, content_handler con) {
            content = con;
            parent = par;
        }
        
        public void run() {
            while (true) {
                parent.send_field();
            }
        }
    }
}