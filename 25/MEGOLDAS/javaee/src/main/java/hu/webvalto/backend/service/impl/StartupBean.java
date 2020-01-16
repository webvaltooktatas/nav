package hu.webvalto.backend.service.impl;

import hu.webvalto.backend.dao.impl.FelhasznaloDao;
import hu.webvalto.backend.domain.Egyenleg;
import hu.webvalto.backend.domain.Felhasznalo;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.Optional;

@Startup
@Singleton
public class StartupBean {

    @Inject
    private FelhasznaloDao felhasznaloDao;

    @Inject
    private Logger logger;

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


    @Lock(LockType.READ)
    @Schedule(hour = "*", minute = "*/1", info = "otto szamlajanak visszaallitasa", persistent = false)
    public void szamlaVisszaallitasa(Timer timer) {
        Optional<Felhasznalo> otto = felhasznaloDao.lekerdezNevAlapjan("Otto");
        if (otto.isPresent()) {
            logger.info("Otto szamlaja visszaallitva");
            if (otto.get().getEgyenleg() == null) {
                Egyenleg egyenleg = new Egyenleg();
                egyenleg.setErtek(1000000);
                egyenleg.setFelhasznalo(otto.get());
                otto.get().setEgyenleg(egyenleg);
            } else {
                otto.get().getEgyenleg().setErtek(1000000);
            }
            felhasznaloDao.frissit(otto.get());
        } else {
            logger.info("Otto idozitoje leallitva!");
            timer.cancel();
        }
    }
}
