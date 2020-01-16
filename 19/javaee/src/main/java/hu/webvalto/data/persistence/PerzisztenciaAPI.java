package hu.webvalto.data.persistence;

import javax.persistence.EntityManager;
import javax.sql.DataSource;


public interface PerzisztenciaAPI {
    EntityManager getEntityManager();

    DataSource getDS();
}
