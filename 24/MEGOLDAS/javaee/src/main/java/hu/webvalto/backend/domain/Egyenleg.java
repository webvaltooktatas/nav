package hu.webvalto.backend.domain;

import javax.persistence.*;


@NamedQuery(name = "Egyenleg.byFelhasznaloId", query = "SELECT e FROM Egyenleg e WHERE e.felhasznalo.id =: id")
@Entity
public class Egyenleg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "egyenleg")
    private int ertek;

    @OneToOne(optional = false)
    @JoinColumn(name = "felhasznaloId")
    private Felhasznalo felhasznalo;

    public int getErtek() {
        return ertek;
    }

    public void setErtek(int ertek) {
        this.ertek = ertek;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Felhasznalo getFelhasznalo() {
        return felhasznalo;
    }

    public void setFelhasznalo(Felhasznalo felhasznalo) {
        this.felhasznalo = felhasznalo;
    }

}
