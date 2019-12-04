<h2>Feladat</h2>

A feladat, hogy elkeszitsuk a banki API-nk implementaciojat, s igy kepesek legyunk:

- lekerdezni az egyenlegunket
- penzt betenni a szamlankra
- onnan penzt kivenni.

<h2>Banki API</h2>

A banki API-nknak szukseges biztositania par alap funkciot. Itt fogjuk tarolni az adatokat, igy allapottarto EJB-t 
hozunk letre, hogy tobb kliens eseten is szeparaltan szolgaljon ki minket.

<h2>Megoldas</h2>

```java
package hu.webvalto;

import javax.ejb.Stateful;


//Allapottarto EJB letrehozasa
@Stateful
public class Bank implements BankAPI {
    //Egyenleg tarolasa
    private int egyenleg = 0;

    @Override
    public void kivet(int osszeg) {
        this.egyenleg -= osszeg;
    }

    @Override
    public void betet(int osszeg) {
        this.egyenleg += osszeg;
    }

    @Override
    public int egyenleg() {
        return egyenleg;
    }
}
```