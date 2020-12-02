package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static ArrayList <String> onlineTraders = new ArrayList<String>();

    public static GUI gui;

    public static void main(String[] args) {



        Client client = null;
        try {
            client = new Client();
            new Thread(new ConnectionChecker(client.socket, client)).start();
        }catch(IOException e){
            System.out.println("Error connecting to the server.");
            System.exit(606);
        }



        gui = new GUI(client);
        new Thread(gui).start();


        new Thread(new InputThread(client)).start();

        Scanner scan = new Scanner(System.in);
        boolean running = true;

        System.out.println("Successfully connected to market with ID "+ client.getID() + "\nType 'help' for list of commands.");
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
                        boolean gettingReciptID = true;
                        int recipient = 0;
                        while (gettingReciptID){
                            System.out.println("Enter an ID of who you would like to sell to: ");

                            try {
                                recipient = scan.nextInt();
                                gettingReciptID = false;
                            }catch (InputMismatchException e){
                                System.out.println("Please enter a valid user ID");
                                gettingReciptID = true;
                            }
                            scan.nextLine();

                            String recipientStr = String.valueOf(recipient);
                            if (recipientStr.equals(client.getID())){
                                System.out.println("You can't sell the stock to yourself");
                            }else {
                                gettingReciptID = false;
                            }
                        }

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
