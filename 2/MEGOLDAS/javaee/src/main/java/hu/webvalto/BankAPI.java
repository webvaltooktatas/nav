package hu.webvalto;
import javax.ejb.Remote;

public interface BankAPI {
    void kivet(int amount);
    void betet(int amount);
    int egyenleg();
}