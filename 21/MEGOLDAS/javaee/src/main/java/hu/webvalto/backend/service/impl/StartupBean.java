package hu.webvalto.backend.service.impl;

import hu.webvalto.backend.dao.impl.FelhasznaloDao;
import hu.webvalto.backend.domain.Egyenleg;
import hu.webvalto.backend.domain.Felhasznalo;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Optional;

@Startup
@Singleton
public class StartupBean {

    @Inject
    private FelhasznaloDao felhasznaloDao;

    @PostConstruct
    public void init() {
        Optional<Felhasznalo> gazdagJani = felhasznaloDao.lekerdezNevAlapjan("gazdagjani");
        if (!gazdagJani.isPresent()) {
            Felhasznalo felhasznalo = new Felhasznalo();
            felhasznalo.setNev("gazdagjani");
            Egyenleg egyenleg = new Egyenleg();
            egyenleg.setErtek(10000000);
            egyenleg.setFelhasznalo(felhasznalo);
            felhasznalo.setEgyenleg(egyenleg);
            felhasznaloDao.mentes(felhasznalo);
        }
    }
}
