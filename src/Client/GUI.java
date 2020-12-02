package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI implements Runnable, ActionListener {


    private Client client;

    private JTextArea consoleArea;
    private JScrollPane scrollPane;
    private JTextPane connectionsArea;

    public ArrayList<String> connectedIDs = new ArrayList<>();

    public GUI(Client client){
        this.client = client;
    }
    private ArrayList<String> connections = new ArrayList<String>();

    public void drawGUI(){
        //Creating the Frame
        JFrame frame = new JFrame("Stock Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
                e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        JLabel IDLabel = new JLabel("Your ID: " + client.getID());
        IDLabel.setForeground(Color.RED);
        IDLabel.setFont(new Font("Comic sans", Font.ITALIC, 15));


        //Creating the MenuBar and adding components
        JMenuBar menuBar = new JMenuBar();
        JMenu menuButton = new JMenu("Menu");
        menuBar.add(menuButton);
        menuBar.add(IDLabel);
        JMenuItem disconnectMenuButton = new JMenuItem("Disconnect");
        JMenuItem helpMenuButton = new JMenuItem("Help");

        menuButton.add(disconnectMenuButton);
        menuButton.add(helpMenuButton);

        connectionsArea = new JTextPane();
        connectionsArea.setEditable(false);
        connectionsArea.setText("Connections");
        connectionsArea.setBackground(Color.LIGHT_GRAY);
        connectionsArea.setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(connectionsArea);



        //Creating the panel at bottom and adding components
        JPanel traderPanel = new JPanel(); // the panel is not visible in output
        JLabel traderIDLabel = new JLabel("Enter a Trader ID");



        JTextField traderID = new JTextField(10); // accepts upto 10 characters


        JButton buyButton = new JButton("Buy");
        JButton sellButton = new JButton("Sell");
        JButton statusButton = new JButton("Status");
        JButton connectionsButton = new JButton("Connections");

        JSeparator separator = new JSeparator();



        traderPanel.add(traderIDLabel); // Components Added using Flow Layout
        traderPanel.add(traderID);
        traderPanel.add(sellButton);
        traderPanel.add(separator);
        traderPanel.add(buyButton);
        traderPanel.add(separator);
        traderPanel.add(statusButton);
        traderPanel.add(separator);
        traderPanel.add(connectionsButton);


        //Adding Actionlisteners

        disconnectMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                client.sendCommand("quit");
                System.out.println(">Disconnecting from server...");
                System.exit(1);
            }
        });

        helpMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateConsole("**Help**\n>Press 'buy' to buy the stock\n>Enter an online traders ID and press 'sell' to give the stock\n>'Status' to see who owns the stock\n>'Connections' to see who's online(This will not include you)");
            }
        });

        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //TODO may have to make this synchronised etc if errors arise. or t.Run() rather than running constantly
                Thread t = new Thread() {
                    @Override
                    public void run() {  // override the run() for the running behaviors
                        client.sendCommand("buy");

                    }
                };
                t.run();  // call back run()
            }

        });

        connectionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //TODO may have to make this synchronised etc if errors arise. or t.Run() rather than running constantly
                Thread t = new Thread() {
                    @Override
                    public void run() {  // override the run() for the running behaviors
                        client.sendCommand("connections");

                    }
                };
                t.run();  // call back run()
            }

        });

        statusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //TODO may have to make this synchronised etc if errors arise. or t.Run() rather than running constantly
                Thread t = new Thread() {
                    @Override
                    public void run() {  // override the run() for the running behaviors
                        client.sendCommand("status");

                    }
                };
                t.run();  // call back run()
            }

        });

        sellButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                // Create a new Thread to do the counting
                Thread t = new Thread() {
                    @Override
                    public void run() {  // override the run() for the running behaviors
                        String buyer = traderID.getText();
                        if (!(buyer.equals(""))){
                            client.sendCommand("sell");
                            client.sendCommand(buyer);
                        }else{
                            System.out.println("Sell empty");
                        }
                    }
                };
                t.run();  // call back run()

            }
        });


        buyButton.addActionListener(this);
        sellButton.addActionListener(this);



        consoleArea = new JTextArea();
        consoleArea.setEditable(false);

        scrollPane = new JScrollPane(consoleArea);


        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, traderPanel);
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
        frame.getContentPane().add(BorderLayout.EAST, scroll);
        frame.setVisible(true);

        client.sendCommand("connections");
        client.sendCommand("status");
    }



    private void connectionsToArray(String connectionsStr){

        StringTokenizer stringTokenizer = new StringTokenizer(connectionsStr);

        while (stringTokenizer.hasMoreTokens())
        {
            connections.add(stringTokenizer.nextToken());
        }

    }




    public void removeConnection(String ID){
        ID = ID.strip();
        System.out.println("removing conn: "+ ID);
        connectedIDs.remove(ID);
        connectionsArea.setText("Connections: " + connectionsToString());
        System.out.println("Connectiosn to stirn:G" + connectionsToString());
    }

    public void newConnection(String ID){
        if (!(connectedIDs.contains(ID)) && (!(ID.equals("disconnected")))){
            connectedIDs.add(ID);
        }
        connectionsArea.setText("Connections: " + connectionsToString());
    }

    private String connectionsToString(){
        String result = "";
        for(String ID: connectedIDs){
            result += "\n" + ID;
        }
        return result;
    }


    public void updateConsole(String message){
        consoleArea.append("\n"+message);
        consoleArea.revalidate();
        JScrollBar sb = scrollPane.getVerticalScrollBar();
        sb.setValue( sb.getMaximum() );
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getId());
        drawGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
