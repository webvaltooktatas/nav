package hu.webvalto;

import java.io.Serializable;

public interface FelhasznaloAPI extends Serializable {
    String getNev();
    void setNev(String nev);
}
