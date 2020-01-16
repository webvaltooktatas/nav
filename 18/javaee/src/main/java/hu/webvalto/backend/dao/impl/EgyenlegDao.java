package hu.webvalto.backend.dao.impl;

import hu.webvalto.backend.dao.IEgyenlegDao;
import hu.webvalto.data.persistence.PerzisztenciaAPI;
import hu.webvalto.backend.domain.Egyenleg;

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

