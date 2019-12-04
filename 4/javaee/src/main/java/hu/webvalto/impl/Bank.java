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
    public boolean kivet(int osszeg) {
        String nev = felhasznalo.getNev();
        int egyenleg = konyveles.getEgyenleg(nev);
        if(osszeg > 0 && osszeg<=egyenleg) {
            konyveles.setEgyenleg(nev, egyenleg - osszeg);
            return true;
        }
        return false;
    }

    @Override
    public boolean betet(int osszeg) {
        if(osszeg>0) {
            String nev = felhasznalo.getNev();
            int egyenleg = konyveles.getEgyenleg(nev);
            konyveles.setEgyenleg(nev, egyenleg + osszeg);
            return true;
        }
        return false;
    }

    @Override
    public int egyenleg() {
        String nev = felhasznalo.getNev();
        return konyveles.getEgyenleg(nev);
    }
}