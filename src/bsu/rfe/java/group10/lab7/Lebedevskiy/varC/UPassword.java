package bsu.rfe.java.group10.lab7.Lebedevskiy.varC;

import java.util.Arrays;

public class UPassword {
    char[] Password;
    UPassword(char[] P)
    {
        Password = new char[P.length];
        for (int i = P.length-1; i >= 0;--i)
            Password[i] = P[i];
    }
    private UPassword ()
    { }
    static UPassword Read (String S)
    {
        UPassword P = new UPassword();
        for (int i = S.length() -1; i >=0; --i)
        {
            P.Password[i] = S.charAt(i);
        }
        return P;
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UPassword uPassword = (UPassword) o;
        return Arrays.equals(Password, uPassword.Password);
    }

    @Override
    public int hashCode() {
//        int i = 0;
        return Arrays.hashCode(Password);
    }
}
