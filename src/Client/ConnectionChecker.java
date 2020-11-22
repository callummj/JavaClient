package Client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConnectionChecker implements Runnable{

    private boolean running;
    private Socket socket;

    private OutputStream os;

    public ConnectionChecker(Socket socket){
        this.socket = socket;
        this.running = true;

        try {
           this.os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(this.running){
            try {
                Thread.sleep(5000); //Ping the server every 5 seconds to check that the connection is still alive.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try{
                os.write(1);
            }catch (IOException e){
                System.out.println("Error: the server has been lost.");
                System.exit(606);
            }
        }
    }
}
