package hu.webvalto.domain;

public class Egyenleg {

    private Long id;

    private Long felhasznaloId;

    private int egyenleg;

    public int getEgyenleg() {
        return egyenleg;
    }

    public void setEgyenleg(int egyenleg) {
        this.egyenleg = egyenleg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFelhasznaloId() {
        return felhasznaloId;
    }

    public void setFelhasznaloId(Long felhasznaloId) {
        this.felhasznaloId = felhasznaloId;
    }
}
