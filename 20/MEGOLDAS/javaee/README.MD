A Bank EJB létrehozásakor ellenőrizzük, hogy létezik-e “gazdagjani” felhasználó. Amennyiben nem, úgy dobjunk hibát.
Amennyiben több mint 10 milliót szeretnénk kivenni a számláról, úgy dobjon hibát, hogy sajnos nincs ennyi elérhető pénze a banknak.


<h2>Feladat</h2>
- Az első részét oldjuk meg életciklus visszahívással
- A második felét pedig interceptorral



<h1>Megoldás</h1>

<h3>beans.xml</h3>

```xml

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd"
       bean-discovery-mode="all">
    <interceptors>
        <class>hu.webvalto.backend.interceptor.FelsoLimitInterceptor</class>
    </interceptors>
</beans>

```

<h3>FelsoLimitInterceptor</h3>

```java

package hu.webvalto.backend.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@FelsoLimit
public class FelsoLimitInterceptor {
    @AroundInvoke
    public Object kiveteliFelsoLimit(InvocationContext context) throws Exception {
        if ("kivet".equals(context.getMethod().getName())) {
            if ((int) context.getParameters()[0] >= 10000000) {
                throw new IllegalStateException("Sajnos ennyi pénzt nem vehet ki :(");
            }
        }
        return context.proceed();
    }
}

```

<h3>Bank</h3>

```java

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
        if (nev.length() >= 3) {

            bejelentkezettFelhasznalo = szamlanyitasDao.szamlanyitas(nev);
            return true;
        }
        return false;
    }
}

```