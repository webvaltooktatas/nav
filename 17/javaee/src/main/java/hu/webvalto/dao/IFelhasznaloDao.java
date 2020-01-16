package hu.webvalto.dao;

import hu.webvalto.domain.Felhasznalo;

import java.util.List;
import java.util.Optional;

public interface IFelhasznaloDao extends Dao<Felhasznalo> {

    Optional<Felhasznalo> lekerdezNevAlapjan(String nev);

    List<Felhasznalo> lekerdezGazdagFelhasznalok();
}
