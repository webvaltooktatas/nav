package hu.webvalto.service;

import javax.ejb.Local;
import java.io.Serializable;

@Local
public interface BankAPI extends Serializable {
    boolean kivet(int osszeg);

    boolean betet(int osszeg);

    int egyenleg();

    boolean szamlanyitas(String nev);
}
