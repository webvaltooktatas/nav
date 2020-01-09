package hu.webvalto.domain;

import javax.persistence.*;

@NamedQuery(name = "Felhasznalo.getByName", query = "SELECT f FROM Felhasznalo f WHERE f.nev =: nev")
@Entity
public class Felhasznalo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nev;

    @OneToOne(mappedBy = "felhasznalo", cascade = CascadeType.ALL)
    private Egyenleg egyenleg;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public Egyenleg getEgyenleg() {
        return egyenleg;
    }

    public void setEgyenleg(Egyenleg egyenleg) {
        this.egyenleg = egyenleg;
    }
}
