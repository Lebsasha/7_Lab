package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientChat extends JFrame {
    Client User;
    String WithWhom;
    int CountMsg;
    JTextArea Incoming;
    JTextField Outgoing;
    Socket SocketU;
    BufferedReader Read;
    PrintWriter Write;
    ArrayList<String> Messages;

    ClientChat(Socket Sck, Client user, String Nm)
    {
        super(Nm);
        System.out.println(Nm);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(kit.getScreenSize().width/2, kit.getScreenSize().height/2);
        setSize(kit.getScreenSize().width/4, kit.getScreenSize().height/4);
        User = user;
        WithWhom = Nm;
        CountMsg = 0;
        Incoming = new JTextArea();
        JScrollPane ForIncoming = new JScrollPane(Incoming);
        //ForIncoming.setMaximumSize(new Dimension(getSize().width, ForIncoming.getMinimumSize().height+55));
//        ForIncoming.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        ForIncoming.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); TODO
        Outgoing = new JTextField();
        Outgoing.setMaximumSize(new Dimension(getSize().width, Outgoing.getMinimumSize().height));
        SocketU = Sck;
        try {
            Read = new BufferedReader(new InputStreamReader(SocketU.getInputStream()));
            Write = new PrintWriter(SocketU.getOutputStream());
            Messages = new ArrayList<>(5);
            Write.println("Ask"+"-"+Nm+"-"+CountMsg);
            Write.flush();
            String Msg = Read.readLine();//TODO
            Incoming.setText(Msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JButton Send = new JButton("Send");
        Send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    PrintStream Write = new PrintStream(SocketU.getOutputStream());
                    Write.println("MMM"+"-"+WithWhom+"-"+Outgoing.getText());
                    Write.flush();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                Write.println("Exit");
//                Thread.sleep(5000);
//                Write.flush();
//                super.windowClosing(e);
//            }
//        });
        Box Content = Box.createVerticalBox();
        Content.add(ForIncoming);
        Content.add(Outgoing);
        Content.add(Send);
        getContentPane().add(Content, BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//TODO
        setVisible(true);
        Thread t = new Thread(new GetIncomingMessages());
        t.start();
    }
    class GetIncomingMessages implements Runnable
    {
        @Override
        public void run() {
            String Msg;
            try {
                while (!Thread.interrupted())
                {
                    Write.println("Ask"+"-"+WithWhom+"-"+CountMsg);
                    Write.flush();
                    Msg = Read.readLine();
                    if (!Msg.equals("")) {
                        Incoming.setText(Msg);
                        CountMsg = 0;
                        for (int i = Msg.length() - 1; i >= 0; --i)
                        {
                            if (Msg.charAt(i) == '\n')
                                ++CountMsg;
                        }
                    }
                    Thread.sleep(2000);
                }
            }
             catch (IOException | InterruptedException ex)
             {
                 ex.printStackTrace();
             }
        }
    }
}
