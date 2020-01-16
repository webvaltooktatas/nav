package hu.webvalto.backend.dao.impl;

import hu.webvalto.backend.dao.IFelhasznaloDao;
import hu.webvalto.backend.domain.Felhasznalo;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public class SzamlanyitasDao {

    @Inject
    private IFelhasznaloDao felhasznaloDao;

    public Felhasznalo szamlanyitas(String nev) {
        Optional<Felhasznalo> optionalFelhasznalo = felhasznaloDao.lekerdezNevAlapjan(nev);
        Felhasznalo felhasznalo = optionalFelhasznalo.isPresent() ? optionalFelhasznalo.get() : new Felhasznalo();
        if (!optionalFelhasznalo.isPresent()) {
            felhasznalo.setNev(nev);
            felhasznaloDao.mentes(felhasznalo);
            felhasznalo = felhasznaloDao.lekerdezNevAlapjan(nev).get();
        }

        return felhasznalo;
    }
}
