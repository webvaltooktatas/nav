package hu.webvalto.service.impl;


import hu.webvalto.dao.IEgyenlegDao;
import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.domain.Egyenleg;
import hu.webvalto.domain.Felhasznalo;
import hu.webvalto.service.BankAPI;

import javax.ejb.Stateful;
import javax.inject.Inject;
import java.util.Optional;


@Stateful
public class Bank implements BankAPI {

    @Inject
    private IFelhasznaloDao felhasznaloDao;

    @Inject
    private IEgyenlegDao egyenlegDao;

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

    @Override
    public int egyenleg() {
        return bejelentkezettFelhasznalo.getEgyenleg() != null ? bejelentkezettFelhasznalo.getEgyenleg().getErtek() : 0;
    }

    @Override
    public boolean szamlanyitas(String nev) {
        if (nev.length() >= 3) {
            Optional<Felhasznalo> optionalFelhasznalo = felhasznaloDao.lekerdezNevAlapjan(nev);
            bejelentkezettFelhasznalo = optionalFelhasznalo.isPresent() ? optionalFelhasznalo.get() : new Felhasznalo();
            if (!optionalFelhasznalo.isPresent()) {
                bejelentkezettFelhasznalo.setNev(nev);
                felhasznaloDao.mentes(bejelentkezettFelhasznalo);
            }
            return true;
        }
        return false;
    }
}