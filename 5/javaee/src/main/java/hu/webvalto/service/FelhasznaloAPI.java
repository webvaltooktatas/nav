package hu.webvalto.service;

import java.io.Serializable;

public interface FelhasznaloAPI extends Serializable {
    String getNev();
    void setNev(String nev);
}
