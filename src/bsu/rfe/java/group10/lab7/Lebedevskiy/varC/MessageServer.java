package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import javafx.util.Pair;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Semaphore;

public class MessageServer{
    final Vector<Pair<Client, ArrayList<String>>> ListClients = new Vector<>(ApproxNumberOfUsers);
    static HashMap<String, Integer> CountMessages;
    final static int ApproxNumberOfUsers = 5;
    ServerSocket ServerSckt;
    Thread ForConnect;
    ArrayList<ClientHandler> Online;
    final Semaphore SemForList = new Semaphore(1);

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
                if (Test.exists() && Test.length() > 0) {
//                    BufferedReader Read = new BufferedReader(new FileReader(Test));
                    DataInputStream Read = new DataInputStream(new FileInputStream(Test));
//                    ListClients = new Vector<>(Read.readInt());
                    ListClients.ensureCapacity(Read.readInt());
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
            }
            catch (IOException exception) {
                exception.printStackTrace();
        }
        ForConnect = new Thread(new MessageServer.WaiterForConnections());
        ForConnect.start();
        Online = new ArrayList<>(3);
    }

    void SrvExit ()
    {
        try {
            DataOutputStream Write = new DataOutputStream(new FileOutputStream("src/bsu/rfe/java/group10/lab7/Lebedevskiy/varC/Client.txt"));
            System.out.println("Exiting and writing");
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
            System.out.println("Exiting and writing");
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
        String UserName;
        String Request;
        HashMap<String, Integer> Count;

        ClientHandler(Socket socketU)
        {
            SocketU = socketU;
            try {
                Read = new BufferedReader(new InputStreamReader(SocketU.getInputStream()));
                writer = new PrintWriter(SocketU.getOutputStream());
                UserName = Read.readLine();
                Help.cout(UserName);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            Online.add(this);
            Count = new HashMap<>();
            int counter=0;
            for (Pair<Client, ArrayList<String>> pr : ListClients) {
                if (pr.getKey().getName().equals(UserName)) {
            for (Pair<Client, ArrayList<String>> Users : ListClients) {
                for (String Message: pr.getValue()) {
                    if (Message.startsWith(Users.getKey().getName()))
                        ++counter;
                }
                Count.put(Users.getKey().getName(), counter);
                counter=0;
            }
            break;
            }}
        }

        @Override
        public void run() {
            try {
                while ((Request = Read.readLine()) != null) {
                    SemForList.acquire();
                    System.out.println("read " + Request);
                    if (Request.startsWith("<>^"))
                    {
                        System.out.println("FIND");
//                        System.out.println(Temp.substring(3));
                        Request = Request.substring(3);
                        ArrayList<String> a = new ArrayList<>(2);
                        for (Pair<Client, ArrayList<String>> p : ListClients)
                        {
                            if (p.getKey().getName().startsWith(Request))
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
                    else if (Request.startsWith("Ask"))
                    {
                        String Msg = "";
                        String Getter = Request.split("-")[1];
                        if (Count.get(Getter) !=  Integer.parseInt(Request.split("-")[2])) {
                            Help.cout("Answer: ");
                            for (Pair<Client, ArrayList<String>> pr : ListClients) {
                                if (pr.getKey().getName().equals(UserName)) {
                                    for (String s : pr.getValue()) {
                                        Help.cout(s);
                                        if (s.startsWith(Getter)) {
                                            if (s.charAt(Getter.length()+1) == '1')
                                                Msg = Msg.concat(s.split("-")[2]+"§");
                                            else {
                                                Msg = Msg.concat(Getter + ": " + s.split("-")[2]+"§");
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        Help.cout(Msg+"1111111");
                        writer.println(Msg);
                    }
                    else if (Request.startsWith("MMM"))
                    {
                        String[] AllInfo =  Request.split("-");
                        if(AllInfo.length >= 3) {
                            String Getter = AllInfo[1];
                            String Msg = AllInfo[2];
                            for (Pair<Client, ArrayList<String>> pr : ListClients) {
                                if (pr.getKey().getName().equals(UserName)) {
                                    pr.getValue().add(Getter+"-1-"+Msg);//Я отправил?
                                    continue;
                                }
                                if (pr.getKey().getName().equals(Getter)) {
                                    pr.getValue().add(UserName+"-0-"+Msg);//Я отправил?
                                }
                            }
                            for (ClientHandler s: Online)
                            {
                                if (s.getUserName().equals(Getter)) {
                                    s.getCount().replace(UserName, s.getCount().get(UserName) + 1);
                                    break;
                                }
                            }
                            Count.replace(Getter, Count.get(Getter)+1);
                        }
                        else Help.cout("Error: bad MMM request");
                    }
                    else if (Request.startsWith("Offline"))
                    {
                        Online.remove(this);
                    }
                    else if (Request.equals("Exit"))
                    {
                        SrvExit();
                        writer.println("finish");
                    }
                    writer.flush();
                    SemForList.release();
                }
            }
            catch (IOException | InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }

        public String getUserName() {
            return UserName;
        }
        public HashMap<String, Integer> getCount() {
            return Count;
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
