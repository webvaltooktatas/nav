package hu.webvalto.backend.service;

import javax.ejb.Remote;
import java.io.Serializable;

@Remote
public interface BankAPIRemote extends Serializable {
    boolean szamlanyitas(String nev);
}
