package hu.webvalto.wsClient;

import hu.webvalto.generated.BankWS;
import hu.webvalto.generated.BankWS_Service;

import javax.xml.ws.WebServiceRef;

public class BankWSClient {
    private static BankWS_Service service = new BankWS_Service();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(felhasznaloLetrehozasa("petra"));
    }

    private static String felhasznaloLetrehozasa(String nev) {
        BankWS port =
                service.getBankWSPort();
        return port.felhasznaloLetrehozasa(nev) ? "SIKERES" : "SIKERTELEN";
    }
}