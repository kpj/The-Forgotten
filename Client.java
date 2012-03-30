import java.util.*;
import java.net.*;
import java.io.*;

public class Client extends Thread
{
    content_handler content;
    
    Socket client;
    
    String name;
    
    boolean disconnected = false;
    
    public Client(Socket socket, content_handler con) {
        super("Client");
        
        content = con;

        client = socket;
        name = socket.getInetAddress().getHostAddress();
    }
    
    public void run() {
        ArrayList<ArrayList> l = new ArrayList<ArrayList>();
        while (true) {
            System.out.println("["+name+"] Waiting for input");
            ArrayList<ArrayList> cur = recv_arraylist();
            
            System.out.println("["+name+"] Got "+cur);
            for (Object o : content.connected) {
                Client c = (Client)o;
                //if (c == this)
                //    break;
                    
                try {
                    ObjectOutputStream out = new ObjectOutputStream(c.client.getOutputStream());
                    out.writeObject(cur);
                    /*System.out.println("["+name+"] Sending to "+c.name);
                    PrintStream os = new PrintStream(c.client.getOutputStream());
                    os.println(cur);*/
                }
                catch (IOException e) {
                    System.out.println("error: "+e);
                    //e.printStackTrace();
                }
            }
        }
    }
    
    public void process_input(ArrayList<ArrayList> inp) {
        System.out.println(inp);
    }
    
    public void send_arraylist(ArrayList<ArrayList> list) {
        try {
            OutputStream out = client.getOutputStream();
            byte[] ba = serialize(list);
            System.out.println(ba);
            out.write(ba);
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            e.printStackTrace();
        }
    }
    public void send_string(String data) {
        try {
            PrintStream os = new PrintStream(client.getOutputStream());
            os.println(data);
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            //e.printStackTrace();
        }
    }
    public String recv_string() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            return in.readLine();
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            //e.printStackTrace();
            return "";
        }
    }
    @SuppressWarnings("unchecked")
    public ArrayList<ArrayList> recv_arraylist() {
        try {
            InputStream in = client.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(in.read());
            byte[] ba = bos.toByteArray();
            ArrayList<ArrayList> al = (ArrayList<ArrayList>)deserialize(ba);
            return al;
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            //e.printStackTrace();
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
            System.out.println("error: "+e);
            //e.printStackTrace();
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
            System.out.println("error: "+e);
            //e.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException e) {
            System.out.println("error: "+e);
            //e.printStackTrace();
            return null;
        }
    }
}