package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class InputThread implements Runnable {


    private BufferedReader reader;

    private Socket socket;
    private Client client;
    private ArrayList<String> messageBuffer;


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
        this.messageBuffer = new ArrayList<String>();
    }

    private void updateClient(String command) {
        System.out.println("update client");
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

        }else if (message.startsWith("[CONN]")){ //Connection/Disconnecitons
            message = trimString(message, "[CONN]");
            System.out.println("Connection:" + message);

        }else if (message.startsWith("[MARKET]")){ //
            message = trimString(message, "[MARKET]");
            System.out.println("Market: " + message);

        }else if (message.startsWith("[WARNING]")){
            message = trimString(message, "[WARNING]");
            System.out.println("Warning: " + message);
        }
        System.out.print("> ");
    }

    @Override
    public void run() {
        System.out.println("input thread running");
        while (true) {
            try {
                String response = reader.readLine();
                if (response != null){
                    handleResponse(response);
                }

            } catch (IOException ex) {
                System.out.println("Server disconnected. ");
                System.exit(606);
                break;
            }
        }

    }
}
