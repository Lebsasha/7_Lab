package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Client implements Comparable<String>{
    private String Name;
    private UPassword Passw;

    public Client(String Nm, UPassword Pass) {
        Name = Nm;
        Passw = Pass;
    }

    public String getName() {
        return Name;
    }

    static Client Read (DataInputStream read) throws IOException
    {
        String Name = read.readUTF();
        return new Client(Name, UPassword.Read(read));
//        String[] S = Read.split("/");
//        if (S.length != 2)
//        {
//            System.out.println("Wrong File");
//            return null;
//        }
//        else
//        {
//            return new Client(S[0], UPassword.Read(S[1]));
//        }
//        try{
//        if (S.length != 2) {
//            throw new Exception("Wrong File");
//        }
//        }
//        catch (Exception ex)
//        {
//          ex.getMessage();
//        }
    }


    public boolean equals(Client obj) {
        if (this == obj)
            return true;
        return obj.getName().equals(Name) && obj.Passw.equals(Passw);
    }

    @Override
    public int compareTo(String s) {
        return Name.compareTo(s);
    }

    public void Write(DataOutputStream write) throws IOException {
        write.writeUTF(Name);
        Passw.Write(write);
    }
}
