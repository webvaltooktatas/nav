package hu.webvalto.impl;

import hu.webvalto.FelhasznaloAPI;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class Felhasznalo implements FelhasznaloAPI {
    private String nev;

    @Override
    public String getnev() {
        return nev;
    }

    @Override
    public void setNev(String nev) {
        this.nev = nev;
    }
}
