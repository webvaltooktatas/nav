package hu.webvalto.dao.impl;

import hu.webvalto.dao.Dao;
import hu.webvalto.dao.IEgyenlegDao;
import hu.webvalto.data.connector.PerzisztenciaAPI;
import hu.webvalto.domain.Egyenleg;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class EgyenlegDao implements IEgyenlegDao {

    @Inject
    private PerzisztenciaAPI perzisztenciaAPI;

    public Optional<Egyenleg> get(Long felhasznaloId) {
        try (
                PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("SELECT id, felhasznaloId, egyenleg from Egyenleg WHERE felhasznaloId=?")) {
            statement.setLong(1, felhasznaloId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() ? buildEgyenleg(resultSet) : Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Egyenleg> buildEgyenleg(ResultSet resultSet) throws SQLException {
        Egyenleg egyenleg = new Egyenleg();
        egyenleg.setId(resultSet.getLong(1));
        egyenleg.setFelhasznaloId(resultSet.getLong(2));
        egyenleg.setEgyenleg(resultSet.getInt(3));
        return Optional.of(egyenleg);
    }

    public void mentes(Egyenleg egyenleg) {
        try (PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("INSERT INTO Egyenleg(felhasznaloId, egyenleg) VALUES(?,?)")) {
            statement.setLong(1, egyenleg.getFelhasznaloId());
            statement.setLong(2, egyenleg.getEgyenleg());
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void frissit(Egyenleg egyenleg, Object[] params) {
        try (
                PreparedStatement statement = perzisztenciaAPI.getDS().getConnection().prepareStatement("UPDATE Egyenleg SET egyenleg=? WHERE felhasznaloId=?")) {
            statement.setLong(2, egyenleg.getFelhasznaloId());
            statement.setInt(1, (int) params[0]);
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void torol(Egyenleg user) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}

