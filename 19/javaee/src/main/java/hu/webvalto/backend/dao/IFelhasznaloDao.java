package hu.webvalto.backend.dao;

import hu.webvalto.backend.domain.Felhasznalo;

import java.util.List;
import java.util.Optional;

public interface IFelhasznaloDao extends Dao<Felhasznalo> {

    Optional<Felhasznalo> lekerdezNevAlapjan(String nev);

    List<Felhasznalo> lekerdezGazdagFelhasznalok();
}
