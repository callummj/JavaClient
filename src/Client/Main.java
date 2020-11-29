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


        //new Thread(new GUI(client)).start();


        new Thread(new InputThread(client)).start();

        Scanner scan = new Scanner(System.in);
        boolean running = true;

        System.out.println("Successfully connected to market."+ "\nType 'help' for list of commands.");
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

                        int recipient;
                        recipient = scan.nextInt();
                        scan.nextLine();

                        client.sendCommand("sell");
                        client.sendCommand(String.valueOf(recipient));
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
                        System.out.println("Illegal command entered: " + input + " with size: " + input.length());
                        System.out.println("Invalid command, type 'help' for a list of all commands.");
                }
            }
        }


    }

    protected static void restartInputThread(Client client){
        System.out.println("restarting input thread");
        new Thread(new InputThread(client)).start();
    }

}
