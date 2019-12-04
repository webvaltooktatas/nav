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

//Mockito keretrendszer hasznalata JUnit 5-tel
@ExtendWith(MockitoExtension.class)
//JUnit5 futtato kornyezet eletciklusa
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankTeszt {

    //Konyveles mock objektum letrehozasa
    @Mock
    private Konyveles konyveles;

    //Felhasznalo mock objektum letrehozasa
    @Mock
    private Felhasznalo felhasznalo;

    //Kimockolt osztalyok beinjektalasa a tesztelendo osztalyba
    @InjectMocks
    private Bank bank;

    //Minden teszt futtatasa elott reseteljuk a Bank osztalyt, s igy egymastol fuggetlenne tegyuk a teszteket.
    @BeforeEach
    public void init() {
        bank = new Bank();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void egyenlegTeszt() {
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(121, bank.egyenleg());
    }

    @Test
    public void betetSikeresTeszt() {
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(true, bank.betet(1));
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
        Assertions.assertEquals(false, bank.kivet(-122));
    }
}
