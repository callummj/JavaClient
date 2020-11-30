package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class InputThread implements Runnable {


    private BufferedReader reader;

    private Socket socket;
    private Client client;


    public InputThread(Client client) {
        this.client = client;
        this.socket = client.getSocket();

        InputStream input = null;
        try {
            input = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader = new BufferedReader(new InputStreamReader(input));
    }


    //Removes the command part from message which is used by the program to know what to do with the command.
    private String trimString(String string, String toRemove){
        return string.replace(toRemove, "");
    }

    private void handleResponse(String message){
        if (message.startsWith("[UPDATE]")) {
            //updateClientMethod()
            message = trimString(message, "[UPDATE]");
            System.out.println(message);
            System.out.print("> ");

        }else if (message.startsWith("[CONN]")){ //Connection/Disconnecitons
            message = trimString(message, "[CONN]");
            message = message.replace("[CONN]", "");

            StringTokenizer connectionsTokens = new StringTokenizer(message);
            System.out.println("Connected Clients:");
            while (connectionsTokens.hasMoreTokens()){
                System.out.println("ID: " + connectionsTokens.nextToken());
            }
            System.out.print("> ");
        }else if (message.startsWith("[NEW_CONN]")){ //
            message = trimString(message, "[NEW_CONN]");
            //If new connection isn't the server sending back the client their own conneciton
            if (!(message.equals("User: " + client.getID() +" has connected to the server"))){
                System.out.println(message);
            }
            System.out.print("> ");
        }else if (message.startsWith("[MARKET]")){ //
            message = trimString(message, "[MARKET]");
            System.out.println("Market: " + message);

        }else if (message.startsWith("[WARNING]")){
            message = trimString(message, "[WARNING]");
            System.out.println("Warning: " + message);
            System.out.print("> ");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                if (response != null){
                    handleResponse(response);
                }

            } catch (IOException ex) { //Connection with server has been lost: but this is handled by connectionChecker.java

                Thread.currentThread().interrupt();

                break;
            }
        }

    }
}
