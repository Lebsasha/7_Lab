package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MessageServer{
    TreeMap<Client, String[]> ListClients;
    TreeMap <Client, String[]> Soo;
    ServerSocket ServerSckt;
    Thread ForConnect;

    class WaiterForConnections implements Runnable{
        WaiterForConnections(){}
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Socket UserSocket = ServerSckt.accept();
                    //TODO
                    Thread Handle = new Thread(new ClientHandler(UserSocket));
                    Handle.start();
                    //UserSocket.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    MessageServer()
    {
        ListClients = new TreeMap<>();
        Soo = new TreeMap<>();
        File Test = new File("src/bsu/rfe/java/group10/lab7/Lebedevskiy/varC/Client.txt");
            try {
                ServerSckt = new ServerSocket(6666);
                if (Test.exists()) {
                    BufferedReader Read = new BufferedReader(new FileReader(Test));
//          DataInputStream Clients = new DataInputStream(new FileInputStream(new File("src/bsu/rfe/java/group10/lab7/Lebedevskiy/varC/Client.txt")));
                    String line;

                    ListClients = new TreeMap<Client, String[]>(new Comparator<Client>() {
                        @Override
                        public int compare(Client client, Client t1) {
                            return client.Name.compareTo(t1.Name);
                        }
                    });

                    while ((line = Read.readLine()) != null) {
                        ListClients.put(Client.Read(line), null);
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
        }
        ForConnect = new Thread(new MessageServer.WaiterForConnections());
        ForConnect.start();
    }

    class ClientHandler implements Runnable
    {

        Socket S;
        BufferedReader Read;
        String Temp;
        ClientHandler(Socket s)
        {
            S = s;
            try {
                Read = new BufferedReader(new InputStreamReader(S.getInputStream()));
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while ((Temp = Read.readLine()) != null) {
                    System.out.println("“read " + Temp);//TODO
                    if (Temp.startsWith("<>^"))
                    {
                        System.out.println("OK");
                        String TT;
                        System.out.println(Temp.substring(2));
                        ArrayList<String> a;
                        for (int i = ListClients.size()-1; i >= 0; --i)
                        {
                            if (ListClients.containsKey(new Client(Temp.substring(3), null)))

                        }
                        if (ListClients.containsKey(Temp.substring(3)))
                        {}
//                            Write.append
                    }
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public boolean IsAClient(String text, UPassword uPassword) {
        return ListClients.containsKey(new Client(text, uPassword));//TODO
    }

    public void addClient(String text, UPassword uPassword) {
        try {
            if ((ListClients.containsKey(new Client(text, uPassword)))) {
                throw new Exception("addClient, (ListClients.containsKey(new Client(text, uPassword)))");
            }
        }
        catch (Exception ex)
        {
            ex.getMessage();
        } //TODO Remove!!!
        ListClients.put(new Client(text, uPassword), null);
    }
}
