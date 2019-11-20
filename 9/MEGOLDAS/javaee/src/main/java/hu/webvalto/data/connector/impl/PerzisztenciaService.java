package hu.webvalto.data.connector.impl;

import hu.webvalto.data.connector.PerzisztenciaAPI;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.sql.*;

@ApplicationScoped
public class PerzisztenciaService implements PerzisztenciaAPI {
    private Connection connection;

    @PostConstruct
    public void init() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank", "root", "1234");
    }

    @Override
    public void felhasznaloTarolasa(String nev) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Felhasznalo(nev) VALUES(?)")) {
            statement.setString(1, nev);
            statement.execute();
        }
    }

    @Override
    public void egyenlegLetrehozasa(String nev, int kezdoEgyenleg) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Egyenleg(nev, egyenleg) VALUES(?,?)")) {
            statement.setString(1, nev);
            statement.setInt(2, kezdoEgyenleg);
            statement.execute();
        }
    }

    @Override
    public boolean felhasznaloLetezik(String nev) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM Felhasznalo WHERE nev = ?")) {
            statement.setString(1, nev);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) == 1 : false;
        }
    }

    @Override
    public Integer egyenlegLekerdezese(String nev) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement("SELECT egyenleg from Egyenleg WHERE nev=?")) {
            statement.setString(1, nev);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() ? resultSet.getInt(1) : null;
        }

    }

    @Override
    public void egyenlegFrissitese(String nev, int ujEgyenleg) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement("UPDATE Egyenleg SET egyenleg=? WHERE nev=?")) {
            statement.setString(2, nev);
            statement.setInt(1, ujEgyenleg);
            statement.execute();
        }
    }

    @PreDestroy
    public void destroy() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
