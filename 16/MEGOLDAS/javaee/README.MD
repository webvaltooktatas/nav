Az alkalmazás indításakor hozzon létre automatikusan egy gazdag felhasználót, ha nem volt még a következő névvel: “gazdagjani”.


<h2>Feladat</h2>
- Készítsünk egy Singleton bean-t ami automatikusan meghívódik
- Kérje le "gazdagjanit"
- Ha nincs, hozza létre


<h1>Megoldás</h1>

<h3>StartupBean</h3>

```java

package hu.webvalto.service.impl;

import hu.webvalto.dao.impl.FelhasznaloDao;
import hu.webvalto.domain.Egyenleg;
import hu.webvalto.domain.Felhasznalo;

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

    @OneToOne(mappedBy = "felhasznalo", cascade = CascadeType.ALL)
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