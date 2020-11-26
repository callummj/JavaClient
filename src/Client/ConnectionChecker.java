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
    private Client client;

    public ConnectionChecker(Socket socket, Client client){
        this.socket = socket;
        this.running = true;
        this.client = client;

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

                for (int i = 0; i < 5; i++){
                    System.out.println("here");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    client.sendCommand("reconnection");
                    client.sendCommand(client.getID());
                }
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

            //TODO Sandatise this so not using public client in.


            /*
            //DO SOMETHING HBERE
            if (client.in.hasNext()){
                String streamMsg = client.in.nextLine();
                if (streamMsg.startsWith("[UPDATE]")){
                    do{
                        System.out.println(client.in.nextLine());
                        streamMsg = client.in.nextLine();
                    }while (client.in.nextLine().startsWith("[UPDATE]"));
                }
            }*/
        }
    }
}