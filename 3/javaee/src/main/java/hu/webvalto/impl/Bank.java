package hu.webvalto.impl;

import hu.webvalto.BankAPI;

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