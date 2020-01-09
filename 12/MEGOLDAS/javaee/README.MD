Kezdjük el használni a Liquibase-t a sémánk inicializálásához és karbantartásához.

<h2>Feladat</h2>
- Liquibase inicializálása és beállítása
- initDB.sql alapján megírni a sémát inicializáló changelog bejegyzést
- Változtassuk meg az “Felhasznalo” tábla “nev” oszlopának a típusát “varchar(150)”-ről “varchar(200)”-re


<h1>Megoldás</h1>

<h3>pom.xml</h3>

```xml
...
<dependencies>
        <dependency>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-core</artifactId>
            <version>3.8.4</version>
        </dependency>
        <dependency>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-cdi</artifactId>
            <version>3.8.4</version>
        </dependency>
...
</dependencies>
<builds>
    ...
    <plugins>
        <plugin>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-maven-plugin</artifactId>
            <version>3.4.1</version>
            <configuration>
                <propertyFile>src/main/resources/db/liquibase.properties</propertyFile>
            </configuration>
        </plugin>
    </plugins>
</builds>
```

<h3>LiquibaseProducer</h3>

```java
package hu.webvalto.data.liquibase;

import hu.webvalto.data.connector.PerzisztenciaAPI;
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

```


<h3>BankDataSource.java</h3>

```java
package hu.webvalto.data.datasource;

import javax.annotation.sql.DataSourceDefinition;

@DataSourceDefinition(
        name="java:app/bank_in_app",
        className="com.mysql.cj.jdbc.MysqlDataSource",
        url="jdbc:mysql://localhost:3306/bank?createDatabaseIfNotExist=true",
        user="root",
        password="1234"
)
public class BankDataSource {
}

```

<h2>DB</h2>

<h3>liquibase.properties</h3>

```properties
changeLogFile=src/main/resources/db/changelog.xml
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/bank?createDatabaseIfNotExist=true
username=root
password=1234
outputChangeLogFile=src/main/resources/db/generated-changelog-from-db.xml
```

<h3>changelog.xml</h3>

```xml
<?xml version="1.0" encoding="UTF-8"?>

<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd">

    <changeSet id="1" author="adam">
        <sqlFile encoding="utf8" path="db/initTables.sql"/>
        <rollback>
            <sqlFile encoding="utf8" path="db/dropDB.sql"/>
        </rollback>
    </changeSet>
    <!--legyunk szemfulesek, mert ha NOT NULL volt
    es nem irjuk bele az uj tipusba is, akkor NULLABLE-e fog valni! -->
    <changeSet id="2" author="adam" runOnChange="true">
        <modifyDataType
                columnName="nev"
                newDataType="VARCHAR(200) NOT NULL"
                tableName="Felhasznalo"/>
    </changeSet>

</databaseChangeLog>

```

<h3>initDB.sql</h3>

```sql
CREATE DATABASE IF NOT EXISTS `Bank`;

```

<h3>initTables.sql</h3>

```sql
CREATE TABLE IF NOT EXISTS `Felhasznalo` (`id` int(10) NOT NULL AUTO_INCREMENT, `nev` varchar(150) NOT NULL, PRIMARY KEY (`id`), UNIQUE (`nev`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;
CREATE TABLE IF NOT EXISTS `Egyenleg` (`id` int(10) NOT NULL AUTO_INCREMENT, `felhasznaloId` int(10) NOT NULL, `egyenleg` int(10) NOT NULL, PRIMARY KEY (`id`), FOREIGN KEY (`felhasznaloId`) REFERENCES Felhasznalo(`id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;

```