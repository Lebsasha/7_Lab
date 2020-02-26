package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogIn extends JFrame {
    Boolean InIsTrue;
    JTextField UserF;
    JPasswordField PasswF;
    MessageServer Srv;
    LogIn()
    {
        super("Log in");
        InIsTrue = true;
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(kit.getScreenSize().width/2, kit.getScreenSize().height/2);
        setSize(kit.getScreenSize().width/4, kit.getScreenSize().height/4);
//        JPanel Pane = new JPanel(new BoxLayout(this, BoxLayout.Y_AXIS));
        Srv = new MessageServer();
        JLabel UserL = new JLabel("Username", SwingConstants.CENTER);
        UserF = new JTextField();
        UserF.setPreferredSize(new Dimension(getSize().width, UserF.getMinimumSize().height));
        JLabel PasswL = new JLabel("Password", SwingConstants.LEFT);
        PasswF = new JPasswordField();
        PasswF.setMaximumSize(new Dimension(getSize().width, PasswF.getMinimumSize().height));
        JButton SignIn = new JButton("Sign in");
        SignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                if (Srv.IsAClient(UserF.getText(), new UPassword(PasswF.getPassword())))
                    new FindPanel(UserF.getText(), new UPassword(PasswF.getPassword())/*, InIsTrue*/);
                else
                    JOptionPane.showMessageDialog(LogIn.this, "Incorrect login or password", "Info", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton SignUp = new JButton("Sign up");
        SignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                InIsTrue = false;
                setVisible(false);
                Srv.addClient(UserF.getText(), new UPassword(PasswF.getPassword()));
                new FindPanel(UserF.getText(), new UPassword(PasswF.getPassword())/*, InIsTrue*/);
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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void main (String[] a)
    {
        JFrame Frm = new LogIn();
    }
}
