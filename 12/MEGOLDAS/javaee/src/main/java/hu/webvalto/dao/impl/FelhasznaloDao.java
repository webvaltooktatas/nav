package hu.webvalto.dao.impl;

import hu.webvalto.dao.IFelhasznaloDao;
import hu.webvalto.data.connector.PerzisztenciaAPI;
import hu.webvalto.domain.Felhasznalo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class FelhasznaloDao implements IFelhasznaloDao {

    @Inject
    private PerzisztenciaAPI perzisztenciaAPI;

    public Optional<Felhasznalo> get(String nev) {
        try (PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("SELECT id, nev FROM Felhasznalo WHERE nev = ?")) {
            statement.setString(1, nev);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? buildFelhasznalo(resultSet) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Felhasznalo> buildFelhasznalo(ResultSet resultSet) throws SQLException {
        Felhasznalo felhasznalo = new Felhasznalo();
        felhasznalo.setId(resultSet.getLong(1));
        felhasznalo.setNev(resultSet.getString(2));
        return Optional.of(felhasznalo);
    }

    public void mentes(Felhasznalo user) {
        try (PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("INSERT INTO Felhasznalo(nev) VALUES(?)")) {
            statement.setString(1, user.getNev());
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void frissit(Felhasznalo user, Object[] params) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public void torol(Felhasznalo user) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}

