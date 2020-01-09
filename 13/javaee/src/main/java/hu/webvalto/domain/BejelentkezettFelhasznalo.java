package hu.webvalto.domain;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class BejelentkezettFelhasznalo extends Felhasznalo implements Serializable {
    private Felhasznalo felhasznalo;

    @Override
    public Long getId() {
        return felhasznalo.getId();
    }

    @Override
    public String getNev() {
        return felhasznalo.getNev();
    }

    public void setFelhasznalo(Felhasznalo felhasznalo) {
        this.felhasznalo = felhasznalo;
    }
}
