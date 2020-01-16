package hu.webvalto.backend.service.impl;


import hu.webvalto.backend.dao.IFelhasznaloDao;
import hu.webvalto.backend.domain.Felhasznalo;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.Optional;


@TransactionManagement(value = TransactionManagementType.BEAN)
@Stateful
public class BankAlternative {

    @Inject
    private IFelhasznaloDao felhasznaloDao;

    @Resource
    private UserTransaction userTransaction;


    public boolean szamlanyitas(String nev) throws SystemException {
        try {
            if (nev.length() >= 3) {

                Optional<Felhasznalo> optionalFelhasznalo = felhasznaloDao.lekerdezNevAlapjan(nev);
                Felhasznalo felhasznalo = optionalFelhasznalo.isPresent() ? optionalFelhasznalo.get() : new Felhasznalo();
                if (!optionalFelhasznalo.isPresent()) {
                    felhasznalo.setNev(nev);
                    userTransaction.begin();
                    felhasznaloDao.mentes(felhasznalo);
                    userTransaction.commit();
                    felhasznalo = felhasznaloDao.lekerdezNevAlapjan(nev).get();
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            userTransaction.rollback();
        }
        return false;
    }
}