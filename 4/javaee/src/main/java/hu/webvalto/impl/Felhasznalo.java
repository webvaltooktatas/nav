package hu.webvalto.impl;

import hu.webvalto.FelhasznaloAPI;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

//Munkamenet idejere taroljuk a felhasznalot
@SessionScoped
public class Felhasznalo implements FelhasznaloAPI {
    private String nev;

    @Override
    public String getNev() {
        return nev;
    }

    @Override
    public void setNev(String nev) {
        this.nev = nev;
    }
}
