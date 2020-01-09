package hu.webvalto.dao;

import java.util.Optional;

public interface Dao<T, IdT> {
    Optional<T> get(IdT id);
    void mentes(T t);
    void frissit(T t, Object[] params);
    void torol(T t);
}
