package hu.webvalto.service.impl;


import hu.webvalto.dao.IEgyenlegDao;
import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.dao.impl.SzamlanyitasDao;
import hu.webvalto.domain.Egyenleg;
import hu.webvalto.domain.Felhasznalo;
import hu.webvalto.service.BankAPI;

import javax.ejb.*;
import javax.inject.Inject;


@Local(BankAPI.class)
@Remote(BankRemote.class)
@Stateful
public class Bank implements BankAPI, BankRemote {

    @Inject
    private IFelhasznaloDao felhasznaloDao;

    @Inject
    private IEgyenlegDao egyenlegDao;

    @Inject
    private SzamlanyitasDao szamlanyitasDao;

    private Felhasznalo bejelentkezettFelhasznalo;

    @Override
    public boolean kivet(int osszeg) {
        //Ne vehessunk ki tobbet, mint amink van

        if (osszeg > 0 && bejelentkezettFelhasznalo.getEgyenleg() != null && osszeg <= bejelentkezettFelhasznalo.getEgyenleg().getErtek()) {
            int ujEgyenleg = bejelentkezettFelhasznalo.getEgyenleg().getErtek() - osszeg;
            bejelentkezettFelhasznalo.getEgyenleg().setErtek(ujEgyenleg);
            bejelentkezettFelhasznalo = felhasznaloDao.frissit(bejelentkezettFelhasznalo);
            return true;
        }
        return false;

    }

    @Override
    public boolean betet(int osszeg) {
        if (osszeg > 0) {


            if (bejelentkezettFelhasznalo.getEgyenleg() != null) {
                int ujEgyenleg = bejelentkezettFelhasznalo.getEgyenleg().getErtek() + osszeg;
                bejelentkezettFelhasznalo.getEgyenleg().setErtek(ujEgyenleg);
                bejelentkezettFelhasznalo = felhasznaloDao.frissit(bejelentkezettFelhasznalo);
            } else {
                Egyenleg egyenleg = new Egyenleg();
                egyenleg.setFelhasznalo(bejelentkezettFelhasznalo);
                egyenleg.setErtek(osszeg);
                bejelentkezettFelhasznalo.setEgyenleg(egyenleg);
                egyenlegDao.mentes(egyenleg);
            }
            return true;
        }
        return false;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public int egyenleg() {
        return bejelentkezettFelhasznalo.getEgyenleg() != null ? bejelentkezettFelhasznalo.getEgyenleg().getErtek() : 0;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public boolean szamlanyitas(String nev) {
        if (nev.length() >= 3) {

            bejelentkezettFelhasznalo = szamlanyitasDao.szamlanyitas(nev);
            return true;
        }
        return false;
    }
}