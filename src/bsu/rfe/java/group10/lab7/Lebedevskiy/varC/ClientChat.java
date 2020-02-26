package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientChat extends JFrame {
    String WithWhom;
    JTextArea Incoming;
    JTextField Outgoing;
    Socket SocketU;
    ArrayList<String> Messages;

    ClientChat(Socket Sck, String Nm)
    {
        super(Nm);
        WithWhom = Nm;
        Incoming = new JTextArea();
        JScrollPane ForIncoming = new JScrollPane(Incoming);
//        ForIncoming.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        ForIncoming.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); TODO
        Outgoing = new JTextField();
        SocketU = Sck;
        JButton Send = new JButton("Send");
        Send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    PrintStream Write = new PrintStream(SocketU.getOutputStream());
                    Write.println(Outgoing.getText());
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        class GetIncomingMessages implements Runnable
        {
            @Override
            public void run() {

            }
        }
    }
}
