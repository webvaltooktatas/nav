package hu.webvalto.service.impl;


import hu.webvalto.dao.IEgyenlegDao;
import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.domain.BejelentkezettFelhasznalo;
import hu.webvalto.domain.Egyenleg;
import hu.webvalto.domain.Felhasznalo;
import hu.webvalto.service.BankAPI;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;


@Stateless
public class Bank implements BankAPI {

    @Inject
    private IFelhasznaloDao felhasznaloDao;

    @Inject
    private IEgyenlegDao egyenlegDao;

    @Inject
    private BejelentkezettFelhasznalo bejelentkezettFelhasznalo;

    @Override
    public boolean kivet(int osszeg) {
        //Ne vehessunk ki tobbet, mint amink van
        Optional<Egyenleg> egyenlegOptional = egyenlegDao.get(bejelentkezettFelhasznalo.getId());
        if (osszeg > 0 && egyenlegOptional.isPresent() && osszeg <= egyenlegOptional.get().getEgyenleg()) {
            int ujEgyenleg = egyenlegOptional.get().getEgyenleg() - osszeg;
            egyenlegDao.frissit(egyenlegOptional.get(), new Object[]{ujEgyenleg});
            return true;
        }
        return false;

    }

    @Override
    public boolean betet(int osszeg) {
        if (osszeg > 0) {
            Optional<Egyenleg> egyenlegOptional = egyenlegDao.get(bejelentkezettFelhasznalo.getId());

            if (egyenlegOptional.isPresent()) {
                int ujEgyenleg = egyenlegOptional.get().getEgyenleg() + osszeg;
                egyenlegDao.frissit(egyenlegOptional.get(), new Object[]{ujEgyenleg});
            } else {
                Egyenleg egyenleg = new Egyenleg();
                egyenleg.setFelhasznaloId(bejelentkezettFelhasznalo.getId());
                egyenleg.setEgyenleg(osszeg);
                egyenlegDao.mentes(egyenleg);
            }
            return true;
        }
        return false;
    }

    @Override
    public int egyenleg() {
        Optional<Egyenleg> egyenlegOptional = egyenlegDao.get(bejelentkezettFelhasznalo.getId());
        return egyenlegOptional.isPresent() ? egyenlegOptional.get().getEgyenleg() : 0;
    }

    @Override
    public boolean szamlanyitas(String nev) {
        if (nev.length() >= 3) {
            Optional<Felhasznalo> optionalFelhasznalo = felhasznaloDao.get(nev);
            Felhasznalo felhasznalo = optionalFelhasznalo.isPresent() ? optionalFelhasznalo.get() : new Felhasznalo();
            if (!optionalFelhasznalo.isPresent()) {
                felhasznalo.setNev(nev);
                felhasznaloDao.mentes(felhasznalo);
                felhasznalo = felhasznaloDao.get(nev).get();
            }
            bejelentkezettFelhasznalo.setFelhasznalo(felhasznalo);
            return true;
        }
        return false;
    }
}