package hu.webvalto.dao;

import java.util.Optional;

public interface Dao<T> {
    Optional<T> lekerdez(Long id);

    void mentes(T t);

    T frissit(T t);

    void torol(T t);
}
