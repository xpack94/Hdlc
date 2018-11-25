package Entities;

import java.net.Socket;
import java.io.*;
public class Server extends Thread {
    private Socket socket;
 
    public Server(Socket socket) {
        this.socket = socket;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
 
            String text;
            do{
            	text=reader.readLine();
            	writer.println("text is "+text);
            	// System.out.println("received "+text);
            	
            }while(text!=null);
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

