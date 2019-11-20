package hu.webvalto.data.impl;

import hu.webvalto.data.KonyvelesAPI;
import hu.webvalto.data.connector.PerzisztenciaAPI;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.SQLException;

//Alkalmazas futasanak idejere taroljuk a felhasznalok egyenleget
@ApplicationScoped
public class Konyveles implements KonyvelesAPI {

    @Inject
    private PerzisztenciaAPI perzisztencia;

    @Override
    public void setEgyenleg(String nev, int egyenleg) {
        try {
            if (perzisztencia.egyenlegLekerdezese(nev) != null) {
                perzisztencia.egyenlegFrissitese(nev, egyenleg);
            } else {
                perzisztencia.egyenlegLetrehozasa(nev, egyenleg);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getEgyenleg(String nev) {
        try {
            Integer egyenleg = perzisztencia.egyenlegLekerdezese(nev);
            if (egyenleg != null) {
                return egyenleg;
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
