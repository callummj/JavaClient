package Client;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {


    private String ID;
    private Scanner in;
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

        String idStr = getLastConnection();


        if (idStr.equals("No data found")){
            sendCommand("new connection"); //See Server
            idStr = recieveMessage();
            saveData(idStr);
        }else{
            sendCommand(idStr);
        }
        this.ID = idStr;
    }

    //Private methods
    private void saveData(String userID){
        try {
            FileWriter myWriter = new FileWriter("userdata.txt");
            myWriter.write(userID);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Error creating save data");
        }
    }

    private void connect() throws IOException {
        this.socket = new Socket("localhost", 8888);
        OutputStream output = socket.getOutputStream();
        this.printWriter = new PrintWriter(output, true);
        this.in = new Scanner(new InputStreamReader(socket.getInputStream()));
    }

    private String getLastConnection(){
        File userdata;
        userdata = new File("userdata.txt");
        Scanner userDataReader = null;
        try {
            userDataReader = new Scanner(userdata);
        } catch (FileNotFoundException e) {
            return "No data found";
        }
        String data = userDataReader.nextLine();
        userDataReader.close();
        return data;
    }

    //public methdods

    public String getID(){
        return this.ID;
    }

    public void sendCommand(String cmd){
        printWriter.println(cmd);
    }

    public String recieveMessage(){

        String message = "";
        try{
            message = in.nextLine();
        }catch (NoSuchElementException | IndexOutOfBoundsException e){
            System.out.println("Connection with the server has been lost");
            System.exit(606);
        }


        return message;
    }

}
