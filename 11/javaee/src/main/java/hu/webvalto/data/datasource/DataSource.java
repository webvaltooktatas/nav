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
