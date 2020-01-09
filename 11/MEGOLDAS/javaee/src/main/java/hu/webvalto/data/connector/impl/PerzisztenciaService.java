package hu.webvalto.data.connector.impl;

import hu.webvalto.data.connector.PerzisztenciaAPI;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class PerzisztenciaService implements PerzisztenciaAPI {

    @Resource(lookup = "java:app/bank_in_app")
    private DataSource bankDS;

//    @Resource(lookup = "java:app/bank_in_web_xml")
//    private DataSource bankDS;
//
//    @Resource(lookup = "java:jboss/bank_outside")
//    private DataSource bankDS;


    @Override
    public DataSource getDS(){
        return bankDS;
    }
}
