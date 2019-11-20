package hu.webvalto.data;

import java.io.Serializable;
import java.sql.SQLException;

public interface FelhasznaloAPI extends Serializable {
    String getNev();
    void setNev(String nev);
}
