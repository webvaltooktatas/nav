package hu.webvalto.impl;

import hu.webvalto.KonyvelesAPI;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

//Alkalmazas futasanak idejere taroljuk a felhasznalok egyenleget
@ApplicationScoped
public class Konyveles implements KonyvelesAPI {
    private Map<String, Integer> fokonyv = new HashMap<>();

    @Override
    public void setEgyenleg(String nev, int egyenleg) {
        fokonyv.put(nev, egyenleg);
    }

    @Override
    public int getEgyenleg(String nev) {
        return fokonyv.get(nev);
    }
}
