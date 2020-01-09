Írjuk át az alkalmazást, hogy használjon JPA-t és cseréljük is le a tanultak alapján a meglévő domain objektumokat és egyedi SQL lekérdezéseket.

<h2>Feladat</h2>
- JPA bekötése
- Domain objektumok átalakítása JPA entitásokká
- DAO refaktorálása, hogy JPA-t használjon

<h1>Megoldás</h1>

<h3>Bank</h3>
```java
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
```


<h3>Felhasznalo</h3>

```java
package hu.webvalto.domain;

import javax.persistence.*;

@NamedQuery(name = "Felhasznalo.getByName", query = "SELECT f FROM Felhasznalo f WHERE f.nev =: nev")
@Entity
public class Felhasznalo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nev;

    @OneToOne(mappedBy = "felhasznalo")
    private Egyenleg egyenleg;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public Egyenleg getEgyenleg() {
        return egyenleg;
    }

    public void setEgyenleg(Egyenleg egyenleg) {
        this.egyenleg = egyenleg;
    }
}

```


<h3>Egyenleg</h3>

```java
package hu.webvalto.domain;

import javax.persistence.*;


@NamedQuery(name = "Egyenleg.byFelhasznaloId", query = "SELECT e FROM Egyenleg e WHERE e.felhasznalo.id =: id")
@Entity
public class Egyenleg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "egyenleg")
    private int ertek;

    @OneToOne(optional = false)
    @JoinColumn(name = "felhasznaloId")
    private Felhasznalo felhasznalo;

    public int getErtek() {
        return ertek;
    }

    public void setErtek(int ertek) {
        this.ertek = ertek;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Felhasznalo getFelhasznalo() {
        return felhasznalo;
    }

    public void setFelhasznalo(Felhasznalo felhasznalo) {
        this.felhasznalo = felhasznalo;
    }

}

```

<h3>Dao</h3>

```java
package hu.webvalto.dao;

import java.util.Optional;

public interface Dao<T> {
    Optional<T> lekerdez(Long id);

    void mentes(T t);

    T frissit(T t);

    void torol(T t);
}

```

<h3>IFelhasznaloDao</h3>

```java
package hu.webvalto.dao;

import hu.webvalto.domain.Felhasznalo;

import java.util.List;
import java.util.Optional;

public interface IFelhasznaloDao extends Dao<Felhasznalo> {

    Optional<Felhasznalo> lekerdezNevAlapjan(String nev);

    List<Felhasznalo> lekerdezGazdagFelhasznalok();
}

```


<h3>FelhasznaloDao</h3>

```java
package hu.webvalto.dao.impl;

import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.data.persistence.PerzisztenciaAPI;
import hu.webvalto.domain.Felhasznalo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FelhasznaloDao implements IFelhasznaloDao {

    @Inject
    private PerzisztenciaAPI perzisztenciaAPI;

    @Override
    public Optional<Felhasznalo> lekerdezNevAlapjan(String nev) {
        List<Felhasznalo> list = perzisztenciaAPI.getEntityManager().createNamedQuery("Felhasznalo.getByName", Felhasznalo.class)
                .setParameter("nev", nev).getResultList();

        return list.size() > 0 ? Optional.of(list.get(0)) : Optional.empty();
    }

    @Override
    public List<Felhasznalo> lekerdezGazdagFelhasznalok() {
        return perzisztenciaAPI.getEntityManager().createQuery("SELECT f FROM Felhasznalo f, Egyenleg e WHERE f.egyenleg = e.felhasznalo" +
                " AND e.ertek > 10000000", Felhasznalo.class).getResultList();
    }

    @Override
    public Optional<Felhasznalo> lekerdez(Long id) {
        return Optional.ofNullable(perzisztenciaAPI.getEntityManager().find(Felhasznalo.class, id));
    }

    @Override
    public void mentes(Felhasznalo user) {
        perzisztenciaAPI.getEntityManager().persist(user);
    }

    @Override
    public Felhasznalo frissit(Felhasznalo user) {
        return perzisztenciaAPI.getEntityManager().merge(user);
    }

    @Override
    public void torol(Felhasznalo user) {
        perzisztenciaAPI.getEntityManager().remove(user);
    }
}


```

<h3>EgyenlegDao</h3>

```java
package hu.webvalto.dao.impl;

import hu.webvalto.dao.IEgyenlegDao;
import hu.webvalto.data.persistence.PerzisztenciaAPI;
import hu.webvalto.domain.Egyenleg;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class EgyenlegDao implements IEgyenlegDao {

    @Inject
    private PerzisztenciaAPI perzisztenciaAPI;

    @Override
    public Optional<Egyenleg> lekerdez(Long id) {
        return Optional.ofNullable(perzisztenciaAPI.getEntityManager().find(Egyenleg.class, id));
    }

    @Override
    public void mentes(Egyenleg user) {
        perzisztenciaAPI.getEntityManager().persist(user);
    }

    @Override
    public Egyenleg frissit(Egyenleg user) {
        return perzisztenciaAPI.getEntityManager().merge(user);
    }

    @Override
    public void torol(Egyenleg user) {
        perzisztenciaAPI.getEntityManager().remove(user);
    }
}


```

<h3>PersistenceService</h3>
```java
package hu.webvalto.data.persistence.impl;

import hu.webvalto.data.persistence.PerzisztenciaAPI;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@ApplicationScoped
public class PerzisztenciaService implements PerzisztenciaAPI {

    @PersistenceContext(name="bank-persistence-unit")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Resource(lookup = "java:app/bank_in_app")
    private DataSource bankDS;

//    @Resource(lookup = "java:app/bank_in_web_xml")
//    private DataSource bankDS;
//
//    @Resource(lookup = "java:jboss/bank_outside")
//    private DataSource bankDS;


    @Override
    public DataSource getDS(){
        return this.bankDS;
    }
}

```

<h3>Persistence.xml</h3>

```xml
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.2"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <!-- Define persistence unit -->
    <persistence-unit name="bank-persistence-unit">
        <jta-data-source>java:app/bank_in_app</jta-data-source>
    </persistence-unit>
</persistence>
```