package hu.webvalto.impl;

import hu.webvalto.BankAPI;
import hu.webvalto.FelhasznaloAPI;
import hu.webvalto.KonyvelesAPI;

import javax.ejb.Stateless;
import javax.inject.Inject;


//Allapotmentes EJB letrehozasa, mar nincs szuksegunk tobbe, hogy tarolja az allapotot
@Stateless
public class Bank implements BankAPI {
    @Inject
    private KonyvelesAPI konyveles;

    @Inject
    private FelhasznaloAPI felhasznalo;

    @Override
    public void kivet(int osszeg) {
        String nev = felhasznalo.getNev();
        int egyenleg = konyveles.getEgyenleg(nev);
        konyveles.setEgyenleg(nev, egyenleg - osszeg);
    }

    @Override
    public void betet(int osszeg) {
        String nev = felhasznalo.getNev();
        int egyenleg = konyveles.getEgyenleg(nev);
        konyveles.setEgyenleg(nev, egyenleg + osszeg);
    }

    @Override
    public int egyenleg() {
        String nev = felhasznalo.getNev();
        return konyveles.getEgyenleg(nev);
    }
}