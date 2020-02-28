package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class UPassword {
    private char[] Password;
    UPassword(char[] P)
    {
        Password = new char[P.length];
        System.arraycopy(P, 0, Password, 0, P.length);
    }

    private UPassword ()
    {}

    static UPassword Read (DataInputStream read) throws IOException
    {
        UPassword p = new UPassword();
        int l;
        p.Password = new char[l = read.readInt()];
        for (int i = 0; i < l; i++) {
            p.Password[i] = /*(char) ~*/read.readChar();
        }
        return p;
//        UPassword P = new UPassword();
//        for (int i = S.length() -1; i >=0; --i)
//        {
//            P.Password[i] = S.charAt(i);
//        }
//        return P;
    }

    public void Write (DataOutputStream write) throws IOException {
        write.writeInt(Password.length);
        for (char c: Password)
        write.writeChar(/*~*/c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UPassword uPassword = (UPassword) o;
        return Arrays.equals(Password, uPassword.Password);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(Password);
    }
}
