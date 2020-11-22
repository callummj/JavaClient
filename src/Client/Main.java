package Client;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Client a = null;
        try {
            a = new Client();
        }catch(IOException e){
            System.out.println("Error connecting to the server.");
            System.exit(606);
        }

        Scanner scan = new Scanner(System.in);
        boolean running = true;

        System.out.println("Successfully connected to market with ID: " + a.getID() + ". Type 'help' for list of commands.");
        while(running){
            System.out.print("> ");
            String input = scan.nextLine();
            switch (input){
                case "balance":
                    a.sendCommand("balance");

                    System.out.println("Balance: " + a.recieveMessage());
                    break;
                case "buy":
                    a.sendCommand("buy");
                    System.out.println(a.recieveMessage());
                    break;
                case "sell":
                    System.out.println("sell");
                    a.sendCommand("sell");
                    break;
                case "quit":
                    a.sendCommand("quit");
                    System.out.println("Disconnecting from server...");
                    System.exit(1);
                    break;
                case "help":
                    System.out.println("help");
                    break;
                case "status":
                    a.sendCommand("status");
                    System.out.println(a.recieveMessage());
                    break;
                case "connections":
                    System.out.println("connections");
                    break;
                default:
                    System.out.println("Invalid command, type 'help' for a list of all commands.");
            }
        }

    }

}
