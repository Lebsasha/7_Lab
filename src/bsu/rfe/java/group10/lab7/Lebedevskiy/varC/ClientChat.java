package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientChat extends JFrame {
    private String WithWhom;
    private int CountMsg;
    private JTextArea Incoming;
    private JTextField Outgoing;
    private BufferedReader Read;
    private PrintWriter Write;
    private Thread t;

    ClientChat(Socket Sck, Client user, String Nm)
    {
        super(user.getName()+"→"+Nm);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(kit.getScreenSize().width/2, kit.getScreenSize().height/2);
        setSize(kit.getScreenSize().width/4, kit.getScreenSize().height/4);
        WithWhom = Nm;
        CountMsg = 0;
        Incoming = new JTextArea();
        Incoming.setEditable(false);
        JScrollPane ForIncoming = new JScrollPane(Incoming);
        //ForIncoming.setMaximumSize(new Dimension(getSize().width, ForIncoming.getMinimumSize().height+55));//TODO ?
        Outgoing = new JTextField();
        Outgoing.setMaximumSize(new Dimension(getSize().width, Outgoing.getMinimumSize().height));
        Outgoing.requestFocus();
        try {
            Read = new BufferedReader(new InputStreamReader(Sck.getInputStream()));
            Write = new PrintWriter(Sck.getOutputStream());
            Write.println("Ask"+"-"+WithWhom+"-"+CountMsg);
            Write.flush();
            String Msg = Read.readLine();
            Incoming.setText(Msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JButton Send = new JButton("Send");
        Send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Write.println("MMM"+"-"+WithWhom+"-"+Outgoing.getText());
                Write.flush();
                Outgoing.setText("");
            }
        });
        Box Content = Box.createVerticalBox();
        Content.add(ForIncoming);
        Content.add(Outgoing);
        Content.add(Send);
        getContentPane().add(Content, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    t.interrupt();
                super.windowClosing(e);
            }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        t = new Thread(new GetIncomingMessages());
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
                    Help.cout(Msg);
                    if (!Msg.equals("")) {
                        Msg = Msg.replace('§', '\n');
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
             catch (IOException | InterruptedException ignored)
             {
             }
        }
    }
}
