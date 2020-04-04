package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Client implements Comparable<String>{
    private String Name;
    private UPassword Passw;

    Client(String Nm, UPassword Pass) {
        Name = Nm;
        Passw = Pass;
    }

    String getName() {
        return Name;
    }

    static Client Read (DataInputStream read) throws IOException
    {
        String Name = read.readUTF();
        return new Client(Name, UPassword.Read(read));
    }


    boolean equals(Client obj) {
        if (this == obj)
            return true;
        return obj.getName().equals(Name) && obj.Passw.equals(Passw);
    }

    @Override
    public int compareTo(String s) {
        return Name.compareTo(s);
    }

    void Write(DataOutputStream write) throws IOException {
        write.writeUTF(Name);
        Passw.Write(write);
    }
}
