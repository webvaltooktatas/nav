package hu.webvalto;

import java.io.Serializable;

public interface BankAPI extends Serializable {
    boolean kivet(int osszeg);

    boolean betet(int osszeg);

    int egyenleg();
}