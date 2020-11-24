package Client;

import java.io.IOException;

import java.io.OutputStream;

import java.io.PrintWriter;
import java.net.Socket;


public class ConnectionChecker implements Runnable{

    private boolean running;
    private Socket socket;
    private PrintWriter printWriter;
    private OutputStream os;

    public ConnectionChecker(Socket socket){
        this.socket = socket;
        this.running = true;


        OutputStream output = null;
        try {
            output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.printWriter = new PrintWriter(output, true);
        try {
           this.os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Writes the character 'o' to the output stream which is sent to the server to see if the server is still available.
    @Override
    public void run() {
        System.out.println("conn checker: " + Thread.currentThread().getId());
        while(this.running){
            try {
                Thread.sleep(5000); //Ping the server every 5 seconds to check that the connection is still alive.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try{
                os.write('o');
            }catch (IOException e){
                System.out.println("Error: the server has been lost.");
                System.exit(606);
            }
            try {
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                printWriter.println();
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
