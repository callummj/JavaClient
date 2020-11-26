package Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ArrayList <String> onlineTraders = new ArrayList<String>();

    public static void main(String[] args) {



        Client client = null;
        try {
            client = new Client();
            new Thread(new ConnectionChecker(client.socket)).start();
        }catch(IOException e){
            System.out.println("Error connecting to the server.");
            System.exit(606);
        }

        System.out.println("main thread: " + Thread.currentThread().getId());

        new Thread(new GUI(client)).start();


        Scanner scan = new Scanner(System.in);
        boolean running = true;

        System.out.println("Successfully connected to market with ID: " + client.getID() + ". Type 'help' for list of commands.");
        while(running){
            System.out.print("> ");
            if (scan.hasNext()) {
                System.out.print("> ");
                String input = scan.nextLine();
                switch (input) {
                    case "balance":
                        client.sendCommand("balance");
                        System.out.println("Balance: " + client.recieveMessage());
                        break;
                    case "buy":
                        client.sendCommand("buy");
                        System.out.println(client.recieveMessage());
                        break;
                    case "sell":
                        System.out.println("sell");
                        client.sendCommand("sell");
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
                        System.out.println(client.recieveMessage());
                        break;
                    case "connections":
                        client.sendCommand("connections");
                        String connections = client.recieveMessage();
                        break;
                    default:
                        System.out.println("Invalid command, type 'help' for a list of all commands.");
                }
            }
        }

    }



}
