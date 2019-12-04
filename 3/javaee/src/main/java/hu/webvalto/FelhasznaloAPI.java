package hu.webvalto;

import java.io.Serializable;

public interface FelhasznaloAPI extends Serializable {
    String getnev();
    void setNev(String nev);
}
