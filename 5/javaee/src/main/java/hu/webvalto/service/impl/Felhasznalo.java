package hu.webvalto.service.impl;

import hu.webvalto.service.FelhasznaloAPI;

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
