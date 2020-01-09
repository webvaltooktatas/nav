Hozzuk létre a FelhasznaloDAO és KonyvelesDAO osztályokat, valamint refaktoráljuk a már létező perzisztencia megoldásunkat.

<h2>Feladat</h2>
- FelhasznaloDAO létrehozása
- KonyvelesDAO létrehozása
- Felhasznalo domain osztály refaktorálása
- Egyenleg domain osztály refaktorálása
- PerzisztenciaService refaktorálása


<h1>Megoldás</h1>

<h2>DAO</h2>

<h3>Dao.java</h3>
```java
package hu.webvalto.dao;

import java.util.Optional;

public interface Dao<T, IdT> {
    Optional<T> get(IdT id);
    void mentes(T t);
    void frissit(T t, Object[] params);
    void torol(T t);
}

```

<h3>IEgyenlegDao</h3>
```java
package hu.webvalto.dao;

import hu.webvalto.domain.Egyenleg;

public interface IEgyenlegDao extends Dao<Egyenleg, Long> {
}

```

<h3>IFelhasznaloDao</h3>
```java
package hu.webvalto.dao;

import hu.webvalto.domain.Felhasznalo;

public interface IFelhasznaloDao extends Dao<Felhasznalo, String> {
}

```

<h3>EgyenlegDao</h3>
```java
package hu.webvalto.dao.impl;

import hu.webvalto.dao.Dao;
import hu.webvalto.dao.IEgyenlegDao;
import hu.webvalto.data.connector.PerzisztenciaAPI;
import hu.webvalto.domain.Egyenleg;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class EgyenlegDao implements IEgyenlegDao {

    @Inject
    private PerzisztenciaAPI perzisztenciaAPI;

    public Optional<Egyenleg> get(Long felhasznaloId) {
        try (
                PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("SELECT id, felhasznaloId, egyenleg from Egyenleg WHERE felhasznaloId=?")) {
            statement.setLong(1, felhasznaloId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() ? buildEgyenleg(resultSet) : Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Egyenleg> buildEgyenleg(ResultSet resultSet) throws SQLException {
        Egyenleg egyenleg = new Egyenleg();
        egyenleg.setId(resultSet.getLong(1));
        egyenleg.setFelhasznaloId(resultSet.getLong(2));
        egyenleg.setEgyenleg(resultSet.getInt(3));
        return Optional.of(egyenleg);
    }

    public void mentes(Egyenleg egyenleg) {
        try (PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("INSERT INTO Egyenleg(felhasznaloId, egyenleg) VALUES(?,?)")) {
            statement.setLong(1, egyenleg.getFelhasznaloId());
            statement.setLong(2, egyenleg.getEgyenleg());
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void frissit(Egyenleg egyenleg, Object[] params) {
        try (
                PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("UPDATE Egyenleg SET egyenleg=? WHERE felhasznaloId=?")) {
            statement.setLong(2, egyenleg.getFelhasznaloId());
            statement.setInt(1, (int) params[0]);
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void torol(Egyenleg user) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}


```

<h3>FelhasznaloDao</h3>
```java
package hu.webvalto.dao.impl;

import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.data.connector.PerzisztenciaAPI;
import hu.webvalto.domain.Felhasznalo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class FelhasznaloDao implements IFelhasznaloDao {

    @Inject
    private PerzisztenciaAPI perzisztenciaAPI;

    public Optional<Felhasznalo> get(String nev) {
        try (PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("SELECT id, nev FROM Felhasznalo WHERE nev = ?")) {
            statement.setString(1, nev);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? buildFelhasznalo(resultSet) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Felhasznalo> buildFelhasznalo(ResultSet resultSet) throws SQLException {
        Felhasznalo felhasznalo = new Felhasznalo();
        felhasznalo.setId(resultSet.getLong(1));
        felhasznalo.setNev(resultSet.getString(2));
        return Optional.of(felhasznalo);
    }

    public void mentes(Felhasznalo user) {
        try (PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("INSERT INTO Felhasznalo(nev) VALUES(?)")) {
            statement.setString(1, user.getNev());
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void frissit(Felhasznalo user, Object[] params) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public void torol(Felhasznalo user) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}

```

<h3>BejelentkezettFelhasznalo</h3>
```java
package hu.webvalto.domain;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class BejelentkezettFelhasznalo extends Felhasznalo implements Serializable {
    private Felhasznalo felhasznalo;

    @Override
    public Long getId() {
        return felhasznalo.getId();
    }

    @Override
    public String getNev() {
        return felhasznalo.getNev();
    }

    public void setFelhasznalo(Felhasznalo felhasznalo) {
        this.felhasznalo = felhasznalo;
    }
}

```

<h3>Egyenleg</h3>
```java
package hu.webvalto.domain;

public class Egyenleg {

    private Long id;

    private Long felhasznaloId;

    private int egyenleg;

    public int getEgyenleg() {
        return egyenleg;
    }

    public void setEgyenleg(int egyenleg) {
        this.egyenleg = egyenleg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFelhasznaloId() {
        return felhasznaloId;
    }

    public void setFelhasznaloId(Long felhasznaloId) {
        this.felhasznaloId = felhasznaloId;
    }
}

```

<h3>Felhasznalo</h3>

```java
package hu.webvalto.domain;

public class Felhasznalo {

    private Long id;

    private String nev;

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
}

```

<h3>PerzisztenciaAPI</h3>
```java
package hu.webvalto.data.connector;

import javax.sql.DataSource;
import java.sql.Connection;


public interface PerzisztenciaAPI {
   DataSource getDS();
}


```

<h3>PerzisztenciaService</h3>
```java
package hu.webvalto.data.connector.impl;

import hu.webvalto.data.connector.PerzisztenciaAPI;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class PerzisztenciaService implements PerzisztenciaAPI {

    @Resource(lookup = "java:app/bank_in_app")
    private DataSource bankDS;

//    @Resource(lookup = "java:app/bank_in_web_xml")
//    private DataSource bankDS;
//
//    @Resource(lookup = "java:jboss/bank_outside")
//    private DataSource bankDS;


    @Override
    public DataSource getDS(){
        return bankDS;
    }
}


```

<h3>Bank</h3>
```java
package hu.webvalto.service.impl;


import hu.webvalto.dao.IEgyenlegDao;
import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.domain.BejelentkezettFelhasznalo;
import hu.webvalto.domain.Egyenleg;
import hu.webvalto.domain.Felhasznalo;
import hu.webvalto.service.BankAPI;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;


@Stateless
public class Bank implements BankAPI {

    @Inject
    private IFelhasznaloDao felhasznaloDao;

    @Inject
    private IEgyenlegDao egyenlegDao;

    @Inject
    private BejelentkezettFelhasznalo bejelentkezettFelhasznalo;

    @Override
    public boolean kivet(int osszeg) {
        //Ne vehessunk ki tobbet, mint amink van
        Optional<Egyenleg> egyenlegOptional = egyenlegDao.get(bejelentkezettFelhasznalo.getId());
        if (osszeg > 0 && egyenlegOptional.isPresent() && osszeg <= egyenlegOptional.get().getEgyenleg()) {
            int ujEgyenleg = egyenlegOptional.get().getEgyenleg() - osszeg;
            egyenlegDao.frissit(egyenlegOptional.get(), new Object[]{ujEgyenleg});
            return true;
        }
        return false;

    }

    @Override
    public boolean betet(int osszeg) {
        if (osszeg > 0) {
            Optional<Egyenleg> egyenlegOptional = egyenlegDao.get(bejelentkezettFelhasznalo.getId());

            if (egyenlegOptional.isPresent()) {
                int ujEgyenleg = egyenlegOptional.get().getEgyenleg() + osszeg;
                egyenlegDao.frissit(egyenlegOptional.get(), new Object[]{ujEgyenleg});
            } else {
                Egyenleg egyenleg = new Egyenleg();
                egyenleg.setFelhasznaloId(bejelentkezettFelhasznalo.getId());
                egyenleg.setEgyenleg(osszeg);
                egyenlegDao.mentes(egyenleg);
            }
            return true;
        }
        return false;
    }

    @Override
    public int egyenleg() {
        Optional<Egyenleg> egyenlegOptional = egyenlegDao.get(bejelentkezettFelhasznalo.getId());
        return egyenlegOptional.isPresent() ? egyenlegOptional.get().getEgyenleg() : 0;
    }

    @Override
    public boolean szamlanyitas(String nev) {
        if (nev.length() >= 3) {
            Optional<Felhasznalo> optionalFelhasznalo = felhasznaloDao.get(nev);
            Felhasznalo felhasznalo = optionalFelhasznalo.isPresent() ? optionalFelhasznalo.get() : new Felhasznalo();
            if (!optionalFelhasznalo.isPresent()) {
                felhasznalo.setNev(nev);
                felhasznaloDao.mentes(felhasznalo);
                felhasznalo = felhasznaloDao.get(nev).get();
            }
            bejelentkezettFelhasznalo.setFelhasznalo(felhasznalo);
            return true;
        }
        return false;
    }
}
```