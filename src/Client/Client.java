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

        this.ID = idStr;
    }


    private void connect() throws IOException {
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

    public void sendCommand(String cmd){
        printWriter.println(cmd);
    }


    //recieve specific message
    public String recieveMessage(){

        System.out.println("start recieve message");

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
