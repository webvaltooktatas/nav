package hu.webvalto.backend.service.impl;


import hu.webvalto.backend.dao.IEgyenlegDao;
import hu.webvalto.backend.dao.IFelhasznaloDao;
import hu.webvalto.backend.dao.impl.SzamlanyitasDao;
import hu.webvalto.backend.domain.Egyenleg;
import hu.webvalto.backend.domain.Felhasznalo;
import hu.webvalto.backend.interceptor.FelsoLimit;
import hu.webvalto.backend.service.BankAPI;
import hu.webvalto.backend.service.BankAPIRemote;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;


@Remote(BankAPIRemote.class)
@Local(BankAPI.class)
@Stateful
@FelsoLimit
public class Bank implements BankAPI, BankAPIRemote {

    @Inject
    private IFelhasznaloDao felhasznaloDao;

    @Inject
    private IEgyenlegDao egyenlegDao;

    @Inject
    private SzamlanyitasDao szamlanyitasDao;

    private Felhasznalo bejelentkezettFelhasznalo;

    @PostConstruct
    public void init() {
        if (!felhasznaloDao.lekerdezNevAlapjan("gazdagjani").isPresent()) {
            throw new IllegalStateException("Gazdag Jani nem létezik!");
        }
    }

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
        try {
            bejelentkezettFelhasznalo = szamlanyitasDao.szamlanyitas(nev);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}