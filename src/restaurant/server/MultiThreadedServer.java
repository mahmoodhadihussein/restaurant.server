package restaurant.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.*;
import java.util.Scanner;
import net.proteanit.sql.DbUtils;

public class MultiThreadedServer implements Runnable {
    /**
     * @param args the command line arguments
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws IOException, InterruptedException {
    	MultiThreadedServer mts = new MultiThreadedServer(8080);
    }

    ServerSocket socketServer;  
    int port;
    Thread runner;

    public MultiThreadedServer(int port) {
    	this.port = port;
    	
        this.runner = new Thread(this);
        this.runner.start();		
                 
        System.out.println("Server Started On Port:" + port );
        System.out.println("--------------");

        System.out.println("Press S to Stop the Server");
        
        Scanner sc = new Scanner(System.in); String s = sc.next(); 
        char ch[] = s.toCharArray(); 
        
        if(ch[0] == 's' || ch[0] == 'S') {
            stop();
        }        
    }
    
    @Override
    public void run() {
        try 
        {
            socketServer = new ServerSocket(port);

            while(true && !socketServer.isClosed()) {
                    Socket socket = socketServer.accept();

                    ClientHandler ch = new ClientHandler(socket);
                }
        } 
        catch (IOException e) {
            // TODO Auto-generated catch block
                if(!socketServer.isClosed()) {
                    e.printStackTrace();
                }
            }
    }
	
    public void stop() {
        try {
            socketServer.close();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }
}
