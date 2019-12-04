package hu.webvalto;

public interface BankAPI {
    boolean kivet(int amount);

    boolean betet(int amount);

    int egyenleg();
}