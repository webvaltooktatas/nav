<h2>Leiras</h2>

Most, hogy mar megtanultok CDI beanek hasznalatat, szeparaljuk a feladatkoroket.
Hozzunk be egy uj funkcionalitast, kezeljuk az aktualis felhasznalot, valamint vezessuk a konyvelest, melyik felhasznalonak
mennyi az egyenlege.

A felhasznalonk az aktualis munkamenetunk idejeig eljen, mig a konyveles az alkalmazas eletciklusahoz igazodjon.

<h2>Feladat</h2>

- Felhasznalo osztaly elkeszitese
- Konyveles osztaly elkeszitese
- CDI beankent valo injektalasuk
- Megfelelo eletciklustartam beallitasa


<h2>MEGOLDAS</h2>

<h3>Felhasznalo</h3>
```java
package hu.webvalto.impl;

import hu.webvalto.FelhasznaloAPI;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

//Munkamenet idejere taroljuk a felhasznalot
@SessionScoped
public class Felhasznalo implements FelhasznaloAPI {
    private String nev;

    @Override
    public String getNev() {
        return nev;
    }

    @Override
    public void setNev(String nev) {
        this.nev = nev;
    }
}
```

<h3>Konyveles</h3>
```java
package hu.webvalto.impl;

import hu.webvalto.KonyvelesAPI;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

//Alkalmazas futasanak idejere taroljuk a felhasznalok egyenleget
@ApplicationScoped
public class Konyveles implements KonyvelesAPI {
    private Map<String, Integer> fokonyv = new HashMap<>();

    @Override
    public void setEgyenleg(String nev, int egyenleg) {
        fokonyv.put(nev, egyenleg);
    }

    @Override
    public int getEgyenleg(String nev) {
        return fokonyv.get(nev);
    }
}
```

<h3>Bank</h3>
```java
package hu.webvalto.impl;

import hu.webvalto.BankAPI;
import hu.webvalto.FelhasznaloAPI;
import hu.webvalto.KonyvelesAPI;

import javax.ejb.Stateless;
import javax.inject.Inject;


//Allapotmentes EJB letrehozasa, mar nincs szuksegunk tobbe, hogy tarolja az allapotot
@Stateless
public class Bank implements BankAPI {
    @Inject
    private KonyvelesAPI konyveles;

    @Inject
    private FelhasznaloAPI felhasznalo;

    @Override
    public void kivet(int osszeg) {
        String nev = felhasznalo.getNev();
        int egyenleg = konyveles.getEgyenleg(nev);
        konyveles.setEgyenleg(nev, egyenleg - osszeg);
    }

    @Override
    public void betet(int osszeg) {
        String nev = felhasznalo.getNev();
        int egyenleg = konyveles.getEgyenleg(nev);
        konyveles.setEgyenleg(nev, egyenleg + osszeg);
    }

    @Override
    public int egyenleg() {
        String nev = felhasznalo.getNev();
        return konyveles.getEgyenleg(nev);
    }
}
```