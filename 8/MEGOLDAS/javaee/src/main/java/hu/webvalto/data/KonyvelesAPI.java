package hu.webvalto.data;

import java.io.Serializable;

public interface KonyvelesAPI extends Serializable {
    int getEgyenleg(String nev);
    void setEgyenleg(String nev, int egyenleg);
}
