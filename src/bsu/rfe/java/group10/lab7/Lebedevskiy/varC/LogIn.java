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
import java.util.HashMap;

public class LogIn extends JFrame {
    private JTextField UserF;
    private JPasswordField PasswF;
    private MessageServer Srv;

    private LogIn()
    {
        super("Log in");
        MessageServer.CountMessages = new HashMap<>(MessageServer.ApproxNumberOfUsers);
        setVisible(true);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(kit.getScreenSize().width/2, kit.getScreenSize().height/2);
        setSize(kit.getScreenSize().width/4, kit.getScreenSize().height/4);
//        JPanel Pane = new JPanel(new BoxLayout(this, BoxLayout.Y_AXIS));
        Srv = new MessageServer();
        JLabel UserL = new JLabel("Username", SwingConstants.CENTER);
        UserF = new JTextField();
        UserF.setMaximumSize(new Dimension(getSize().width, UserF.getMinimumSize().height));
        UserF.requestFocus();
        JLabel PasswL = new JLabel("Password", SwingConstants.LEFT);
        PasswF = new JPasswordField();
        PasswF.setMaximumSize(new Dimension(getSize().width, PasswF.getMinimumSize().height));
        JButton SignIn = new JButton("Sign in");
        SignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Srv.IsAClient(UserF.getText(), new UPassword(PasswF.getPassword()))) {
                    //setVisible(false);
                    new FindPanel(UserF.getText(), new UPassword(PasswF.getPassword())/*, InIsTrue*/);
                    UserF.setText("");
                    PasswF.setText("");
                }
                else
                    JOptionPane.showMessageDialog(LogIn.this, "Incorrect login or password", "Info", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton SignUp = new JButton("Sign up");
        SignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Srv.addClient(UserF.getText(), new UPassword(PasswF.getPassword()))) {
                    //setVisible(false);
                    new FindPanel(UserF.getText(), new UPassword(PasswF.getPassword())/*, InIsTrue*/);
                    UserF.setText("");
                    PasswF.setText("");
                }
                else
                    JOptionPane.showMessageDialog(LogIn.this, "User already exist", "Info", JOptionPane.ERROR_MESSAGE);
            }
        });
        Box ContentBox = Box.createVerticalBox();
        ContentBox.add(UserL);
        ContentBox.add(UserF);
        ContentBox.add(PasswL);
        ContentBox.add(PasswF);
        Box ButtonsBox = Box.createHorizontalBox();
        ButtonsBox.add(SignUp);
        ButtonsBox.add(SignIn);
        ContentBox.add(ButtonsBox);
        getContentPane().add(ContentBox, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("windowOpened");
                super.windowOpened(e);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("windowClosed");
                super.windowClosed(e);
            }

            @Override
            public void windowIconified(WindowEvent e) {
                System.out.println("windowIconified");
                super.windowIconified(e);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                System.out.println("windowDeiconified");
                super.windowDeiconified(e);
            }

            @Override
            public void windowActivated(WindowEvent e) {
                System.out.println("windowActivated");
                super.windowActivated(e);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                System.out.println("windowDeactivated");
                super.windowDeactivated(e);
            }

            @Override
            public void windowStateChanged(WindowEvent e) {
                System.out.println("windowStateChanged");
                super.windowStateChanged(e);
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                System.out.println("windowGainedFocus");
                super.windowGainedFocus(e);
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("windowLostFocus");
                super.windowLostFocus(e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                Socket socket;
                PrintWriter Write;
                BufferedReader Read;
                try {
                    socket = new Socket("127.0.0.1", 6666);
                    Read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Write = new PrintWriter(socket.getOutputStream());
                    Write.println("ExitClient");
                    Write.println("Exit");
                    Write.flush();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("Hello from InterruptedException");
                    }
                    System.out.println("windowClosing");
                    if (Read.readLine().equals("finish")) {
                        socket.close();
                        super.windowClosing(e);
                    }
                    else
                        Help.cout("Bad");
                }
                catch (IOException ignored)
                {}
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void main (String[] a)
    {
        new LogIn();
    }
}
