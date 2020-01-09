package hu.webvalto.data.impl;

import hu.webvalto.data.FelhasznaloAPI;
import hu.webvalto.data.connector.PerzisztenciaAPI;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.sql.SQLException;

//Munkamenet idejere taroljuk a felhasznalot
@SessionScoped
public class Felhasznalo implements FelhasznaloAPI {
    private String nev;

    @Inject
    private PerzisztenciaAPI perzisztencia;

    @Override
    public String getNev() {
        return nev;
    }

    @Override
    public void setNev(String nev) {
        this.nev = nev;
        try {
            if (!perzisztencia.felhasznaloLetezik(nev)) {
                perzisztencia.felhasznaloTarolasa(nev);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
