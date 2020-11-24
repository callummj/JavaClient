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

    public GUI(Client client){
        this.client = client;
    }


    public void drawGUI(){
        //Creating the Frame
        JFrame frame = new JFrame("Stock Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

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

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu menuButton = new JMenu("Menu");
        mb.add(menuButton);
        JMenuItem disconnectMenuButton = new JMenuItem("Disconnect");
        JMenuItem helpMenuButton = new JMenuItem("Help");
        menuButton.add(disconnectMenuButton);
        menuButton.add(helpMenuButton);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel traderIDLabel = new JLabel("Enter a Trader ID");

        JTextField traderID = new JTextField(10); // accepts upto 10 characters


        JButton buyButton = new JButton("Buy");
        JButton sellButton = new JButton("Sell");

        JSeparator separator = new JSeparator();



        panel.add(traderIDLabel); // Components Added using Flow Layout
        panel.add(traderID);
        panel.add(sellButton);
        panel.add(separator);
        panel.add(buyButton);


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
                System.out.println("help button");
            }
        });

        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {


                // Create a new Thread to do the counting
                Thread t = new Thread() {
                    @Override
                    public void run() {  // override the run() for the running behaviors
                        client.sendCommand("buy");
                        System.out.println(client.recieveMessage());
                    }
                };
                t.start();  // call back run()
            }

        });


        //TODO UI Freezes when second user clicks either sell or buy
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
                            System.out.println(client.recieveMessage());
                        }else{
                            System.out.println("Sell empty");
                        }
                    }
                };
                t.start();  // call back run()




            }
        });


        buyButton.addActionListener(this);
        sellButton.addActionListener(this);

        // Text Area at the Center
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        client.sendCommand("connections");
        String result = client.recieveMessage();
        System.out.println(result);

        if (result.startsWith("[UPDATE]")){
            ArrayList<String> connectionsArray = connectionsToArray(result);
            String connectionsOutputResult = "Online Traders ID:\n";
            for (int i = 0; i < connectionsArray.size(); i++){
                if (!(connectionsArray.get(i).equals("[UPDATE]"))){
                    connectionsOutputResult += connectionsArray.get(i) + "\n";
                }
            }
           ta.setText(connectionsOutputResult);
        }else{
           ta.setText("no");
        }




        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }



    private ArrayList<String> connectionsToArray(String connectionsStr){
        ArrayList<String> connections = new ArrayList<String>();

        StringTokenizer stringTokenizer = new StringTokenizer(connectionsStr);

        while (stringTokenizer.hasMoreTokens())
        {
            connections.add(stringTokenizer.nextToken());
        }
        return connections;

    }

//TODO Put gui on thread.


    @Override
    public void run() {
        System.out.println("running thread");
        System.out.println(Thread.currentThread().getId());
        drawGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
