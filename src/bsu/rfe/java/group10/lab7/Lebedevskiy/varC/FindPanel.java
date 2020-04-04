package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

class FindPanel extends JFrame {
    private Client User;
    private Socket SocketU;
    private BufferedReader ReaderU;
    private PrintWriter Write;
    private JTextField Find;
    private JComboBox<String> Results;
    private JButton OpenBttn;

    FindPanel(String Nm, UPassword Pass/*, Boolean InIsTrue*/) {
        super("Find for "+Nm);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(kit.getScreenSize().width/2, kit.getScreenSize().height/2);
        setSize(kit.getScreenSize().width/4, kit.getScreenSize().height/4);
        User = new Client(Nm, Pass);
        try {
            SocketU = new Socket("127.0.0.1", 6666);
            ReaderU = new BufferedReader(new InputStreamReader(SocketU.getInputStream()));
            Write = new PrintWriter(SocketU.getOutputStream());
            Write.println(User.getName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Find = new JTextField();
        Find.setMaximumSize(new Dimension(getSize().width, Find.getMinimumSize().height));
        Find.requestFocus();
        Results = new JComboBox<>();
        Results.setEditable(false);//TODO (NOT EDIT!!!) ?
//        Results.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                Help.cout(actionEvent.getActionCommand());
//                if (Results.getItemCount() != 0) {
////                    setVisible(false); //TODO Rework for click
//                    new ClientChat(SocketU, User, (String) ((JComboBox<String>) actionEvent.getSource()).getSelectedItem());
//                }
//            }
//        });
        JButton FindBttn = new JButton("Find");
        FindBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Write.println("<>^" + Find.getText());
                Write.flush();
                Results.removeAll();
                try {
                    String Line = ReaderU.readLine();
                    String[] S = Line.split("-");
                    Results.removeAllItems();
                    for (String Names : S)
                        Results.addItem(Names);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                if (Results.getItemCount() == 0)
                    JOptionPane.showMessageDialog(FindPanel.this, "Cannot find user");
                Results.setSelectedIndex(0);
                OpenBttn.setEnabled(true);
            }
        });
        OpenBttn = new JButton("Open");
        OpenBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!User.getName().equals(Results.getSelectedItem()))
                new ClientChat(SocketU, User, (String) Results.getSelectedItem());
                else
                    JOptionPane.showMessageDialog(FindPanel.this, "Cannot start dialog with yourself");
            }
        });
        OpenBttn.setEnabled(false);
        Box ContentBox = Box.createVerticalBox();
        ContentBox.add(Find);
        ContentBox.add(Results);
        ContentBox.add(FindBttn);
        ContentBox.add(OpenBttn);
        getContentPane().add(ContentBox, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
                              @Override
                              public void windowClosing(WindowEvent e) {
                                  try{
                                      Write.println("Offline");
                                  SocketU.close();
                                  }
                                  catch(IOException ignored)
                                  {}
                                  super.windowClosing(e);
                              }
                          });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}