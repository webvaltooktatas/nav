package hu.webvalto.data.connector.impl;

import hu.webvalto.data.connector.PerzisztenciaAPI;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
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
    public void felhasznaloTarolasa(String nev) throws SQLException {
        try (PreparedStatement statement = bankDS.getConnection().prepareStatement("INSERT INTO Felhasznalo(nev) VALUES(?)")) {
            statement.setString(1, nev);
            statement.execute();
        }
    }

    @Override
    public void egyenlegLetrehozasa(String nev, int kezdoEgyenleg) throws SQLException {
        try (PreparedStatement statement = bankDS.getConnection().prepareStatement("INSERT INTO Egyenleg(nev, egyenleg) VALUES(?,?)")) {
            statement.setString(1, nev);
            statement.setInt(2, kezdoEgyenleg);
            statement.execute();
        }
    }

    @Override
    public boolean felhasznaloLetezik(String nev) throws SQLException {
        try (PreparedStatement statement = bankDS.getConnection().prepareStatement("SELECT 1 FROM Felhasznalo WHERE nev = ?")) {
            statement.setString(1, nev);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) == 1 : false;
        }
    }

    @Override
    public Integer egyenlegLekerdezese(String nev) throws SQLException {
        try (
                PreparedStatement statement = bankDS.getConnection().prepareStatement("SELECT egyenleg from Egyenleg WHERE nev=?")) {
            statement.setString(1, nev);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() ? resultSet.getInt(1) : null;
        }

    }

    @Override
    public void egyenlegFrissitese(String nev, int ujEgyenleg) throws SQLException {
        try (
                PreparedStatement statement = bankDS.getConnection().prepareStatement("UPDATE Egyenleg SET egyenleg=? WHERE nev=?")) {
            statement.setString(2, nev);
            statement.setInt(1, ujEgyenleg);
            statement.execute();
        }
    }
}
