package hu.webvalto.data.impl;

import hu.webvalto.data.KonyvelesAPI;

import javax.enterprise.context.ApplicationScoped;
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
        if (fokonyv.containsKey(nev)) {
            return fokonyv.get(nev);
        }
        return 0;
    }
}
