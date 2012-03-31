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
    @SuppressWarnings("unchecked")
    public void run() {
        ArrayList<ArrayList> tmp = new ArrayList<ArrayList>();
        while (true) {
            Data_packet data = get_existing_data();
            
            System.out.println("Received data | "+data.field.size());
            
            send_to_all(data);
        }
    }
    
    @SuppressWarnings("unchecked")
    public Data_packet recv_data() {
        int cur_size = 0;
        int read_size = 65536;
        int length_size = 81; // at first the length of length-field
        int size = 0;
        try {
            InputStream in = client.getInputStream();
            byte[] ba = new byte[length_size];
            
            in.read(ba, 0, length_size);
            size = ((Integer)deserialize(ba)).intValue();
            System.out.println("Will read "+size+" bytes");
            ba = new byte[size];
            
            int read_bytes = 0;
            while (cur_size < size-length_size) {
                read_bytes = in.read(ba, cur_size, Math.min(read_size, size-cur_size));
                cur_size += read_bytes;
            }
            if (cur_size == -1) return null;
            System.out.println("Read "+cur_size+" bytes");
            return (Data_packet)deserialize(ba);
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            e.printStackTrace();
            return null;
        }
    }
    public Data_packet get_existing_data() {
        while (true) {
            Data_packet data = recv_data();
            if (data != null) return data;
        }
    }
    
    @SuppressWarnings("unchecked")
    public synchronized void send_to_all(Data_packet cur) {
        for (Object o : content.connected) {
            Client c = (Client)o;
            //if (c == this)
            //    break;
            System.out.println("Sending to "+c.name);
            c.send_data(cur);
        }
    }
    
    public void send_data(Data_packet dp) {
        try {
            OutputStream out = client.getOutputStream();
            byte[] bal = serialize(dp);
            Integer i = new Integer(bal.length + 81); // takes 81 bytes to store Integer object
            byte[] ball = serialize(i);
            System.out.println("Sending "+i+" bytes");
            out.write(concat(ball, bal));
        }
        catch (IOException e) {
            System.out.println("error: "+e);
            e.printStackTrace();
        }
    }

    public byte[] concat(byte[] A, byte[] B) {
        byte[] C= new byte[A.length+B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);

        return C;
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
}