package hu.webvalto.data.connector;

import javax.sql.DataSource;
import java.sql.Connection;


public interface PerzisztenciaAPI {
   DataSource getDS();
}
