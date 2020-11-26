package Client;

import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStream;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class ConnectionChecker implements Runnable{

    private boolean running;
    private Socket socket;
    private PrintWriter printWriter;
    private OutputStream os;
    private Scanner scan;

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
        try {
            this.scan = new Scanner(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(this.running){

            try {
                Thread.sleep(5000); //Ping the server every 5 seconds to check that the connection is still alive.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try{
                //Singe character sent rather than reusing "connections" command, as this is a smaller amount of data to be sent
                os.write('o');

                String message = "";
                try{
                    message = scan.nextLine();
                }catch (NoSuchElementException e){
                    System.out.println("Conneciton to the server has been lost");
                    System.exit(606);
                }

                if (message.startsWith("[UPDATE]")){
                    ArrayList<String> connectionsArray = connectionsToArray(message);
                    String connectionsOutputResult = "Online Traders ID:\n";
                    for (int i = 0; i < connectionsArray.size(); i++){
                        if (!(connectionsArray.get(i).equals("[UPDATE]"))){
                            connectionsOutputResult += connectionsArray.get(i) + "\n";
                        }
                    }
                    if (connectionsArray == Main.onlineTraders){
                        System.out.println("list up to date");
                    }else{
                        System.out.println("Update connections list");
                    }
                }

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

    private ArrayList<String> connectionsToArray(String connectionsStr){
        ArrayList<String> connections = new ArrayList<String>();

        StringTokenizer stringTokenizer = new StringTokenizer(connectionsStr);

        while (stringTokenizer.hasMoreTokens())
        {
            connections.add(stringTokenizer.nextToken());
        }
        return connections;

    }
}
