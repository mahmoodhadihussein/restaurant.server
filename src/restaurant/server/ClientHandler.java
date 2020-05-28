package restaurant.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class ClientHandler implements Runnable {
    private Socket socket;

    Socket getSocket() {
        return socket;
    }

    Thread runner;

    void setSoket(Socket socket) {
        this.socket = socket;
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;

        this.runner = new Thread(this);
        this.runner.start();		
    }

protected int getMaxId() {
        // TODO Auto-generated method stub
        String query="Select Max(`OrderID`) as MAXID FROM `order`";
        Connection con=getConnection();
        Statement stm;
        try {
            stm = con.createStatement();
            ResultSet res=stm.executeQuery(query);
            int max=0;
            if(res.next()) {
                    max= res.getInt("MAXID");
            }
            res.close();
            stm.close();
            con.close();
            return max;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    /**
    * Connect to database
    */
    private Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String URLCon= "jdbc:mysql://127.0.0.1:3306/restaurantdb?useSSL=false&useUnicode=true";
            String username="root";
            String password="Am#20092905";
            Connection con=DriverManager.getConnection(URLCon, username, password);
            return con;
        } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        try {
            DataInputStream din = new DataInputStream(socket.getInputStream());  
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());  

            din=new DataInputStream(socket.getInputStream());  
            dout=new DataOutputStream(socket.getOutputStream());  

            String input = din.readUTF();  

            Connection con=getConnection();

            for(int i = 0; i < input.split(",").length; i++)
            {
                    String line = input.split(",")[i];

                String command = "INSERT INTO `order` (`OrderID`, `ClientIP`, `ItemName`, `Price`, `Quantity`) VALUES (";

                command = command + "'" + Integer.toString(getMaxId() + 1) + "', ";
                command = command + "'" + socket.getInetAddress() + "', ";
                command = command + "'" + line.split("#")[0] + "', ";
                command = command + "'" + line.split("#")[1] + "', ";
                command = command + "'" + line.split("#")[2] + "')";

                try {
                    Statement stm=con.createStatement();
                    stm.execute(command);

                    stm.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    }
            }

            try {
                            con.close();
                    } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }


            dout.writeUTF("OK");  
            dout.flush();

            din.close();
            dout.close();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }
}
