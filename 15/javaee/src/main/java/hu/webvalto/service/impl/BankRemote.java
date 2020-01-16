package hu.webvalto.service.impl;

import javax.ejb.Remote;

@Remote
public interface BankRemote {
    boolean szamlanyitas(String nev);
}
