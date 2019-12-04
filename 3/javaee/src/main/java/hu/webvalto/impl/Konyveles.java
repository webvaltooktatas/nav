package hu.webvalto.impl;

import hu.webvalto.KonyvelesAPI;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class Konyveles implements KonyvelesAPI {
    private Map<String, Integer> fokonyv = new HashMap<>();

    @Override
    public int getEgyenleg(String nev) {
        return fokonyv.get(nev);
    }

    @Override
    public void setEgyenleg(String nev, int egyenleg) {
        fokonyv.put(nev, egyenleg);
    }
}
