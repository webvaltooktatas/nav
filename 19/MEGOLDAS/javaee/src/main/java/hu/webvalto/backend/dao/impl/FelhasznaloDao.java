package hu.webvalto.backend.dao.impl;

import hu.webvalto.backend.dao.IFelhasznaloDao;
import hu.webvalto.data.persistence.PerzisztenciaAPI;
import hu.webvalto.backend.domain.Felhasznalo;

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

