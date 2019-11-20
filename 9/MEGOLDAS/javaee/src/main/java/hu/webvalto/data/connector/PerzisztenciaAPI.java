package hu.webvalto.data.connector;

import java.sql.SQLException;

public interface PerzisztenciaAPI {
    void felhasznaloTarolasa(String nev) throws SQLException;

    void egyenlegLetrehozasa(String nev, int kezdoEgyenleg) throws SQLException;

    boolean felhasznaloLetezik(String nev) throws SQLException;

    Integer egyenlegLekerdezese(String nev) throws SQLException;

    void egyenlegFrissitese(String nev, int ujEgyenleg) throws SQLException;
}
