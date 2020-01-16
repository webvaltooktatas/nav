package hu.webvalto.data.persistence.impl;

import hu.webvalto.data.persistence.PerzisztenciaAPI;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;

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
