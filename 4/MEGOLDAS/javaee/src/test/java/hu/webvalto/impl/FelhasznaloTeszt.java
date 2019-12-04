package hu.webvalto.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

//JUnit5 futtato kornyezet eletciklusa
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FelhasznaloTeszt {

    private Felhasznalo felhasznalo;

    //Minden teszt futtatasa elott reseteljuk a Felhasznalo osztalyt, s igy egymastol fuggetlenne tegyuk a teszteket.
    @BeforeEach
    public void init() {
        felhasznalo = new Felhasznalo();
    }

    @Test
    public void setterGetterTest() {
        Assertions.assertEquals(null, felhasznalo.getNev());
        felhasznalo.setNev("lajos");
        Assertions.assertEquals("lajos", felhasznalo.getNev());
    }
}
