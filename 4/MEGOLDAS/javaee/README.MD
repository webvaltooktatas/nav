<h1>Gyakorlat</h1>
Ebben a gyakorlatban, az elozo gyakorlaton mar elkezdett feladatot fogjuk tovabb okositani.

A cel itt, hogy elkeszitsuk a kulonbozo egyseg teszteket az alkalmazasunk szamara, valamint a megirt tesztek alapjan
modositsuk az alkalmazasunk, hogy megfeleljen a teszt elvarasoknak.

<h2>Feladat</h2>
- Elkesziteni a <b>Felhasznalo</b> osztaly egyseg tesztjeit
- Elkesziteni a <b>Bank</b> osztaly egyseg tesztjeit
- Elkesziteni a <b>Konyveles</b> osztaly egyseg tesztjeit
- Modositani a mar elkeszitett osztalyokat, hogy megfeleljenek a teszt elvarasoknak:
    - Beveteli osszeg nullanal nagyobb legyen
    - Kiveteli osszeg nullanal nagyobb legyen
    - Ne vehessunk ki tobbet, mint amennyink van
    - Nem letezo felhasznalo, avagy ujonnan letrehozott felhasznalo egyenlege 0 




<h2>Segitseg</h2>


A cel, hogy minden egyseg funkcio kulon-kulon tesztelve legyen es biztositsa a helyes mukodest es az integritasat a 
funkcionak.

<h2>MEGOLDAS</h2>

<h3>FelhasznaloTeszt</h3>

```java
package hu.webvalto;

import hu.webvalto.impl.Felhasznalo;import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FelhasznaloTeszt {

    private Felhasznalo felhasznalo;

    @BeforeAll
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
```

<h3>BankTeszt</h3>
```java
package hu.webvalto;


import hu.webvalto.impl.Bank;import hu.webvalto.impl.Felhasznalo;import hu.webvalto.impl.Konyveles;import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankTeszt {

    @Mock
    private Konyveles konyveles;

    @Mock
    private Felhasznalo felhasznalo;

    @InjectMocks
    private Bank bank;

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
    public void betetSikeresTeszt(){
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(true, bank.betet(1));
    }

    @Test
    public void betetSikertelenNemLehetNullaTeszt(){
        Assertions.assertEquals(false, bank.betet(0));
    }

    @Test
    public void betetSikertelenNemLehetNegativTeszt(){
        Assertions.assertEquals(false, bank.betet(-10));
    }

    @Test
    public void kivetSikeresTeszt(){
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(true, bank.kivet(10));
    }

    @Test
    public void kivetSikeresEgyenlegLeNullazasaTeszt(){
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(true, bank.kivet(121));
    }

    @Test
    public void kivetSikertelenNemLehetNullaTeszt(){
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(false, bank.kivet(0));
    }

    @Test
    public void kivetSikertelenNemLehetNegativTeszt(){
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(false, bank.kivet(-10));
    }

    @Test
    public void kivetSikertelenNincsElegFedezetTeszt(){
        Mockito.when(felhasznalo.getNev()).thenReturn("lajos");
        Mockito.when(konyveles.getEgyenleg("lajos")).thenReturn(121);
        Assertions.assertEquals(false, bank.kivet(-122));
    }
}

```

<h3>KonyvelesTeszt</h3>
```java
package hu.webvalto;

import hu.webvalto.impl.Konyveles;import org.junit.jupiter.api.Assertions;
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

```