Készítsünk egy Bank EJB-t, ami programozottan kezeli a tranzakciókat és egy DAO-t, amit a számlanyitás servletből hívunk meg és az EJB helyett a DAO fog tranzakciót nyitni.

<h2>Feladat</h2>
- Új Bank EJB példány ami programozottan kezeli a tranzakciót
- Új DAO ami maga nyit tranzakciót


<h1>Megoldás</h1>

<h3>BankAlternative</h3>

```java
package hu.webvalto.service.impl;


import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.domain.Felhasznalo;

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
```

<h3>SzamlanyitasDao</h3>

```java
package hu.webvalto.dao.impl;

import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.domain.Felhasznalo;

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

```

<h3>Bank</h3>

```java
package hu.webvalto.service.impl;


import hu.webvalto.dao.IEgyenlegDao;
import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.dao.impl.SzamlanyitasDao;
import hu.webvalto.domain.Egyenleg;
import hu.webvalto.domain.Felhasznalo;
import hu.webvalto.service.BankAPI;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import java.util.Optional;


@Stateful
public class Bank implements BankAPI {

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
            } else {
                Egyenleg egyenleg = new Egyenleg();
                egyenleg.setFelhasznalo(bejelentkezettFelhasznalo);
                egyenleg.setErtek(osszeg);
                egyenlegDao.mentes(egyenleg);
                bejelentkezettFelhasznalo.setEgyenleg(egyenleg);
                felhasznaloDao.frissit(bejelentkezettFelhasznalo);
                bejelentkezettFelhasznalo = felhasznaloDao.lekerdez(bejelentkezettFelhasznalo.getId()).get();
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
```