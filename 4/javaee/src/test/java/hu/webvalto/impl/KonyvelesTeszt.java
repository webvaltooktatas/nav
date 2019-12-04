package hu.webvalto.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KonyvelesTeszt {
    private Konyveles konyveles;


    @BeforeEach
    public void init() {
        konyveles = new Konyveles();
    }

    @Test
    public void setterGetterTeszt(){
        konyveles.setEgyenleg("lajos", 100);
        Assertions.assertEquals(100, konyveles.getEgyenleg("lajos"));
    }

    @Test
    public void nemLetezyoFelhasznaloEgyenlegZeroTeszt() {
        Assertions.assertEquals(0, konyveles.getEgyenleg("adam"));
    }

    @Test
    public void egyenlegFelulirasTeszt(){
        konyveles.setEgyenleg("lajos", 100);
        konyveles.setEgyenleg("lajos", 99);
        Assertions.assertEquals(99, konyveles.getEgyenleg("lajos"));
    }
}
