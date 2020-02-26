package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class FindPanel extends JFrame {
    Client User;
    Socket SocketU;
    BufferedReader ReaderU;
    PrintStream Write;
    JTextField Find;
    JComboBox<String> Results;

    FindPanel(String Nm, UPassword Pass/*, Boolean InIsTrue*/) {
        super("Find for "+Nm);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(kit.getScreenSize().width/2, kit.getScreenSize().height/2);
        setSize(kit.getScreenSize().width/4, kit.getScreenSize().height/4);
        User = new Client(Nm, Pass);
        try {
            SocketU = new Socket("127.0.0.1", 6666);
            ReaderU = new BufferedReader(new InputStreamReader(SocketU.getInputStream()));
            Write = new PrintStream(SocketU.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Find = new JTextField();
        Find.setMaximumSize(new Dimension(getSize().width, Find.getMinimumSize().height));
        Results = new JComboBox<>();
        Results.setEditable(false);//TODO
        Results.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Results.getItemCount() != 0) {
                    setVisible(false);
                    new ClientChat(SocketU, (String) ((JComboBox<String>) actionEvent.getSource()).getSelectedItem());
                }
            }
        });
        JButton FindBttn = new JButton("Find");
        FindBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Write.println("<>^" + Find.getText());
                String Line;
                Results.removeAll();
                try {
                    while ((Line = ReaderU.readLine()) != null) {
                    String[] S = Line.split("/");
                        Results.removeAllItems();
                        for (String Names : S)
                        Results.addItem(Names);
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                Results.setSelectedIndex(0);
            }
        });
        Box ContentBox = Box.createVerticalBox();
        ContentBox.add(Find);
        ContentBox.add(Results);
        ContentBox.add(FindBttn);
        getContentPane().add(ContentBox, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);//TODO
        setVisible(true);
    }
}