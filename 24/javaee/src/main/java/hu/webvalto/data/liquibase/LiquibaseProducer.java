package hu.webvalto.data.liquibase;

import hu.webvalto.data.persistence.PerzisztenciaAPI;
import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

public class LiquibaseProducer {

    @Inject
    private PerzisztenciaAPI perzisztenciaAPI;

    @Produces
    @LiquibaseType
    public CDILiquibaseConfig createConfig() {
        CDILiquibaseConfig config = new CDILiquibaseConfig();
        config.setChangeLog("db/changelog.xml");
        return config;
    }

    @Produces @LiquibaseType
    public DataSource createDataSource() throws SQLException {
        return perzisztenciaAPI.getDS();
    }

    @Produces @LiquibaseType
    public ResourceAccessor create() {
        return new ClassLoaderResourceAccessor(getClass().getClassLoader());
    }

}
