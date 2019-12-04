package hu.webvalto.impl;

import hu.webvalto.BankAPI;
import hu.webvalto.FelhasznaloAPI;
import hu.webvalto.KonyvelesAPI;

import javax.ejb.Stateless;
import javax.inject.Inject;


@Stateless
public class Bank implements BankAPI {

    @Inject
    private KonyvelesAPI konyveles;

    @Inject
    private FelhasznaloAPI felhasznalo;

    @Override
    public boolean kivet(int osszeg) {
        //Ne vehessunk ki tobbet, mint amink van
        Integer egyenleg = konyveles.getEgyenleg(felhasznalo.getNev());
        if (osszeg > 0 && osszeg <= egyenleg) {
            konyveles.setEgyenleg(felhasznalo.getNev(), egyenleg - osszeg);
            return true;
        }
        return false;

    }

    @Override
    public boolean betet(int osszeg) {
        if (osszeg > 0) {
            Integer egyenleg = konyveles.getEgyenleg(felhasznalo.getNev());
            konyveles.setEgyenleg(felhasznalo.getNev(), egyenleg + osszeg);
            return true;
        }
        return false;
    }

    @Override
    public int egyenleg() {
        return konyveles.getEgyenleg(felhasznalo.getNev());
    }


}