package hu.webvalto.backend.ws;

import hu.webvalto.backend.dao.impl.FelhasznaloDao;
import hu.webvalto.backend.domain.Felhasznalo;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.transaction.Transactional;

@WebService(serviceName = "BankWS")
public class BankWS {

    @Inject
    private FelhasznaloDao felhasznaloDao;

    @Inject
    private Logger logger;

    @Transactional
    @WebMethod
    public boolean felhasznaloLetrehozasa(String nev) {
        try {
            Felhasznalo felhasznalo = new Felhasznalo();
            felhasznalo.setNev(nev);
            felhasznaloDao.mentes(felhasznalo);
            logger.info("Felhasznalo sikeresen letrehozva {}", felhasznalo.getNev());
            return true;
        } catch (Exception e) {
            logger.error("HIBAAAA {}", e);
            return false;
        }
    }
}
