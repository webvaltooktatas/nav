Cseréljük le a DriverManager megoldásunkat DataSource-ra. A DataSource-t az alkalmazás szerverben fogjuk konfigulni.


<h2>Feladat</h2>
- DriverManager lecserélése DataSource-ra
- DataSource konfigurálása az alkalmazásban 
- DataSource használata az alkalmazásban

<h1>Megoldas</h1>

<h2>Appon belul I.</h2>
<h3>DataSource.java</h3>
```java
package hu.webvalto.data.datasource;

import javax.annotation.sql.DataSourceDefinition;

@DataSourceDefinition(
        name="java:app/bank_in_app",
        className="com.mysql.cj.jdbc.MysqlDataSource",
        url="jdbc:mysql://localhost:3306/bank",
        user="root",
        password="1234"
)
public class DataSource {
}

```
<h3>PerzisztenciaService</h3>
```java
@ApplicationScoped
public class PerzisztenciaService implements PerzisztenciaAPI {

    @Resource(lookup = "java:app/bank_in_app")
    private DataSource bankDS;
```

<h2>Appon belul alternativa </h2>
<h3>web.xml</h3>
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
						http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <display-name>JSP Example</display-name>

    <data-source>
        <name>java:app/bank_in_web_xml</name>
        <class-name>com.mysql.cj.jdbc.MysqlDataSource</class-name>
        <server-name>localhost</server-name>
        <database-name>bank</database-name>
        <user>root</user>
        <password>1234</password>
        <transactional>true</transactional>
        <isolation-level>TRANSACTION_READ_COMMITTED</isolation-level>
        <initial-pool-size>2</initial-pool-size>
        <max-pool-size>10</max-pool-size>
        <min-pool-size>5</min-pool-size>
    </data-source>

</web-app>
```

<h3>PerzisztenciaService</h3>
```java
    @Resource(lookup = "java:app/bank_in_web_xml")
    private DataSource bankDS;
```

