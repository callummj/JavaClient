package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static ArrayList <String> onlineTraders = new ArrayList<String>();

    public static void main(String[] args) {



        Client client = null;
        try {
            client = new Client();
            new Thread(new ConnectionChecker(client.socket, client)).start();
        }catch(IOException e){
            System.out.println("Error connecting to the server.");
            System.exit(606);
        }

        System.out.println("main thread: " + Thread.currentThread().getId());

//        new Thread(new GUI(client)).start();

        new Thread(new InputThread(client)).start();

        Scanner scan = new Scanner(System.in);
        boolean running = true;

        System.out.println("Successfully connected to market with ID: " + client.getID() + ". Type 'help' for list of commands.");
        while(running){
            System.out.print("> ");
            if (scan.hasNext()) {

                String input = scan.nextLine();
                switch (input) {
                    case "balance":
                        client.sendCommand("balance");
                        //System.out.println("Balance: " + client.recieveMessage());
                        //response(client);
                        break;
                    case "buy":
                        client.sendCommand("buy");
                        //System.out.println(client.recieveMessage());
                        //response(client);
                        break;
                    case "sell":
                        System.out.println("Enter an ID of who you would like to sell to: ");
                        boolean clientAccepted = false;

                        int recipient;
                        recipient = scan.nextInt();


                        client.sendCommand("sell");
                        client.sendCommand(String.valueOf(recipient));
                        //response(client);
                        break;
                    case "quit":
                        client.sendCommand("quit");
                        System.out.println("Disconnecting from server...");
                        System.exit(1);
                        break;
                    case "help":
                        System.out.println("Available commands:\n> balance\n> sell\n> status\n> connections");
                        break;
                    case "status":
                        client.sendCommand("status");
                        //response(client);

                        break;
                    case "connections":
                        client.sendCommand("connections");
                        //String connections = client.recieveMessage();
                        //response(client);
                        break;
                    default:
                        System.out.println("Invalid command, type 'help' for a list of all commands.");
                }
            }
        }

    }

    /*
    private static void response(Client client){
        System.out.println("here2");
        Scanner scan = client.getScanner(); //more readable
        String message = "";
        boolean gettingStream = true;
        while (gettingStream){
            if (scan.hasNextLine()){
                System.out.println("has next line");
                message += scan.nextLine();
            }else{
                gettingStream = false;
            }
        }

        System.out.println("Message: " + message);
    }*/


}
