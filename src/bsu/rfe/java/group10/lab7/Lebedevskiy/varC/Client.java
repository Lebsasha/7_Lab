package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

public class Client implements Comparable<String>{
    String Name;
    UPassword Passw;

    public Client(String Nm, UPassword Pass) {
        Name = Nm;
        Passw = Pass;
    }

    static Client Read (String Read)
    {
        String[] S = Read.split("/");
        if (S.length != 2)
        {
            System.out.println("Wrong File");
            return null;
        }
        else
        {
            return new Client(S[0], UPassword.Read(S[1]));
        }
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

    @Override
    public int compareTo(String s) {
        return Name.compareTo(s);
    }
}
