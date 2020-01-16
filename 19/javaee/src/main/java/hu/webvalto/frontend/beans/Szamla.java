package hu.webvalto.frontend.beans;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Named
@ManagedBean
@SessionScoped
public class Szamla implements Serializable {
    @Size(min = 3, message = "Névnek 3 vagy hosszabbnak kell lennie")
    private String nev;

    private Integer osszeg;
    private String uzenet;
    private String muvelet;

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    @NotNull(groups = BetetKivetMuvelet.class)
    @Positive(groups = BetetKivetMuvelet.class)
    public Integer getOsszeg() {
        return osszeg;
    }

    public void setOsszeg(Integer osszeg) {
        this.osszeg = osszeg;
    }

    public String getUzenet() {
        return uzenet;
    }

    public void setUzenet(String uzenet) {
        this.uzenet = uzenet;
    }

    public String getMuvelet() {
        return muvelet;
    }

    public void setMuvelet(String muvelet) {
        this.muvelet = muvelet;
    }
}
