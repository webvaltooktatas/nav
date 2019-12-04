package hu.webvalto.data.impl;

import hu.webvalto.data.FelhasznaloAPI;

import javax.enterprise.context.SessionScoped;

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
