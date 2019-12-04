package hu.webvalto;

import java.io.Serializable;

public interface BankAPI extends Serializable {
    void kivet(int osszeg);

    void betet(int osszeg);

    int egyenleg();
}