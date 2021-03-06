package Client;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {


    private String ID;
    public Scanner in;
    private PrintWriter printWriter;
    protected Socket socket;

    //Constructor
    public Client() throws IOException{
        try{
            connect();
        }catch(IOException e){
            System.out.println("Error establishing connection to the server");
            System.exit(606);
        }
        sendCommand("new connection");
        String idStr = recieveMessage();
        idStr = idStr.replace("[ID]", "");
        idStr = idStr.replace(" ", "");
        this.ID = idStr;
    }


    protected void connect() throws IOException {
        this.socket = new Socket("localhost", 8888);
        OutputStream output = socket.getOutputStream();
        this.printWriter = new PrintWriter(output, true);
        this.in = new Scanner(new InputStreamReader(socket.getInputStream()));
    }


    //public methdods

    public String getID(){
        return this.ID;
    }

    public Socket getSocket(){return this.socket;}
    public Scanner getScanner(){
        return this.in;
    }

    public synchronized void sendCommand(String cmd){
        printWriter.println(cmd);
    }



    //recieve specific message
    public String recieveMessage(){


        String message = "";
        try{

            message = in.nextLine();

        }catch (NoSuchElementException e){
            System.out.println("Connection with the server has been lost");
            System.exit(606);
        }catch (IndexOutOfBoundsException e){
            System.out.println("index out of bounds");
        }


        return message;
    }

}
