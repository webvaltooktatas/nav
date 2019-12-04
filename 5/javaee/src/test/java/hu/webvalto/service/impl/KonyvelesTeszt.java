package hu.webvalto.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

//JUnit5 futtato kornyezet eletciklusa
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KonyvelesTeszt {
    private Konyveles konyveles;

    //Minden teszt futtatasa elott reseteljuk a Konyveles osztalyt, s igy egymastol fuggetlenne tegyuk a teszteket.
    @BeforeEach
    public void init() {
        konyveles = new Konyveles();
    }

    @Test
    public void setterGetterTeszt() {
        konyveles.setEgyenleg("lajos", 100);
        Assertions.assertEquals(100, konyveles.getEgyenleg("lajos"));
    }

    @Test
    public void nemLetezoNevEgyenlegZeroTeszt() {
        Assertions.assertEquals(0, konyveles.getEgyenleg("lajos"));
    }

    @Test
    public void egyenlegFelulirasUtanTeszt() {
        konyveles.setEgyenleg("lajos", 100);
        Assertions.assertEquals(100, konyveles.getEgyenleg("lajos"));

        konyveles.setEgyenleg("lajos", 50);
        Assertions.assertEquals(50, konyveles.getEgyenleg("lajos"));
    }
}
