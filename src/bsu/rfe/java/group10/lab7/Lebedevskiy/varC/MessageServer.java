package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MessageServer{
    Vector<Pair<Client, ArrayList<String>>> ListClients/* = new Vector<>(5)*/;
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
        File Test = new File("src/bsu/rfe/java/group10/lab7/Lebedevskiy/varC/Client.txt");
            try {
                ServerSckt = new ServerSocket(6666);
                if (Test.exists()) {
//                    BufferedReader Read = new BufferedReader(new FileReader(Test));
                    DataInputStream Read = new DataInputStream(new FileInputStream(new File("src/bsu/rfe/java/group10/lab7/Lebedevskiy/varC/Client.txt")));
                    String line;
                    ListClients = new Vector<>(Read.readInt());
                    while (Read.available() > 0) {
//                        Pair<Client, ArrayList<String>> p = new Pair<>(Client.Read(Read), null);
                        Client client = Client.Read(Read);
                        int c = Read.readInt();
                        ArrayList<String> l = new ArrayList<>(c);
                        for (int i = 0; i < c; ++i) {
                            l.add(Read.readUTF());
                        }
                        ListClients.add(new Pair<>(client, l));
                    }
                }
                else
                {
                    ListClients = new Vector<>();
                }
            }
            catch (IOException exception) {
                exception.printStackTrace();
        }
        ForConnect = new Thread(new MessageServer.WaiterForConnections());
        ForConnect.start();
    }

    void SrvExit ()
    {
        try {
            DataOutputStream Write = new DataOutputStream(new FileOutputStream("src/bsu/rfe/java/group10/lab7/Lebedevskiy/varC/Client.txt"));
            Write.writeInt(ListClients.size());
            for (Pair<Client, ArrayList<String>> p : ListClients)
            {
                p.getKey().Write(Write);
                Write.writeInt(p.getValue().size());
                for (String s : p.getValue())
                {
                    Write.writeUTF(s);
                }
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    class ClientHandler implements Runnable
    {

        Socket SocketU;
        BufferedReader Read;
        PrintWriter writer;
        String Temp;
        int Count;

        ClientHandler(Socket socketU)
        {
            SocketU = socketU;
            try {
                Read = new BufferedReader(new InputStreamReader(SocketU.getInputStream()));
                writer = new PrintWriter(SocketU.getOutputStream());
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            Count = 0;
        }

        @Override
        public void run() {
            try {
                while ((Temp = Read.readLine()) != null) {
                    System.out.println("read " + Temp);//TODO
                    if (Temp.startsWith("<>^"))
                    {
                        System.out.println("FIND");
//                        System.out.println(Temp.substring(3));
                        ArrayList<String> a = new ArrayList<>(2);
                        for (Pair<Client, ArrayList<String>> p : ListClients)
                        {
                            if (p.getKey().getName().startsWith(Temp.substring(3)))
                                a.add(p.getKey().getName());
                        }
                        String Names = ""/*String.valueOf(a.size())*/;
                        for (String s: a)
                        {
                            Names = Names.concat("-"+s);
                        }
                        if (Names.length() >= 2)
                        Names = Names.substring(1);
                        writer.println(Names);
                    }
                    else if (Temp.startsWith("Ask"))
                    {
                        String Msg = "";
                        if (Integer.parseInt(Temp.split("-")[2]) != Count) {
                            String User = Temp.split("-")[0].substring(3);
                            String Getter = Temp.split("-")[1];
                            Count = 0;
                            for (Pair<Client, ArrayList<String>> pr : ListClients) {
                                if (pr.getKey().getName().equals(User)) {
                                    for (String s : pr.getValue()) {
                                        if (s.startsWith(Getter) || s.startsWith(User)) {
                                            Msg = Msg.concat(s+"\n");
                                            ++Count;
                                        }
                                    }
                                    break;
                                }
                            }
                            //Temporary TODO
                            int Count1 = 0;
                            for (int i = Msg.length() - 1; i >= 0; --i)
                            {
                                if (Msg.charAt(i) == '\n')
                                    ++Count1;
                            }
                            assert Count == Count1 : "Bad";//TODO
                        }
                        writer.println(Msg);
                    }
                    else if (Temp.startsWith("MMM"))
                    {
                        String[] AllInfo =  Temp.split("-");
                        if(AllInfo.length >= 3) {
                            String Sender = AllInfo[0].substring(3);
                            String Getter = AllInfo[1];
                            String Msg = AllInfo[2];
                            Pair<Client, ArrayList<String>> p;
                            for (Pair<Client, ArrayList<String>> pr : ListClients) {
                                if (pr.getKey().getName().equals(Sender)) {
                                    pr.getValue().add(Msg);
                                    ++Count;
                                    continue;
                                }
                                if (pr.getKey().getName().equals(Getter)) {
                                    pr.getValue().add(Msg);
                                }
                            }
                        }
                    }
                    else if (Temp.equals("Exit"))
                    {
                        SrvExit();
                    }
                    writer.flush();//TODO
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public boolean IsAClient(String text, UPassword uPassword) {
        Client t = new Client(text, uPassword);
        for (Pair<Client, ArrayList<String>> p : ListClients)
        {
            if (p.getKey().equals(t))
            {
                return true;
            }
        }
        return false;//TODO
    }

    public boolean addClient(String text, UPassword uPassword) {
//        try {
//            if ((ListClients.containsKey(new Client(text, uPassword)))) {
//                throw new Exception("addClient, (ListClients.containsKey(new Client(text, uPassword)))");
//            }
//        }
//        catch (Exception ex)
//        {
//            ex.getMessage();
//        } //TODO Remove!!!
        for (Pair<Client, ArrayList<String>> p: ListClients) {
            if (p.getKey().getName().equals(text))
                return false;
        }
            ListClients.add(new Pair<>(new Client(text, uPassword), new ArrayList<>(5)));
        return true;
    }
}
