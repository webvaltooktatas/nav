package hu.webvalto.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BankTeszt {

    @InjectMocks
    private Bank bank;

    @Mock
    private Konyveles konyveles;

    @Mock
    private Felhasznalo felhasznalo;

    @BeforeEach
    public void init() {
        bank = new Bank();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void egyenlegTeszt() {
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(123);
        Assertions.assertEquals(123, bank.egyenleg());
    }

    @Test
    public void betetTeszt(){
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        bank.betet(2);
        Mockito.verify(konyveles, Mockito.times(1)).setEgyenleg("lajos", 123);
    }

    @Test
    public void betetSikertelenNemLehetNullaTeszt() {
        Assertions.assertEquals(false, bank.betet(0));
    }

    @Test
    public void betetSikertelenNemLehetNegativTeszt() {
        Assertions.assertEquals(false, bank.betet(-10));
    }

    @Test
    public void kivetSikeresTeszt() {
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(true, bank.kivet(10));
    }

    @Test
    public void kivetSikeresEgyenlegLeNullazasaTeszt() {
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(true, bank.kivet(121));
    }

    @Test
    public void kivetSikertelenNemLehetNullaTeszt() {
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(false, bank.kivet(0));
    }

    @Test
    public void kivetSikertelenNemLehetNegativTeszt() {
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(false, bank.kivet(-10));
    }

    @Test
    public void kivetSikertelenNincsElegFedezetTeszt() {
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(false, bank.kivet(122));
    }
}
