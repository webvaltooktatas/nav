Készítsünk egy webservice-t amivel létrehozhatunk új felhasználót.

<h2>Feladat</h2>
- Felhasználó létrehozását támogató webservice készítése
- WSDL alapján kliens generálása
- Kliens írása, ami létrehozza “Petra” nevű felhasználót



<h2>Előfeltétel</h2>

Elő kell konfigolni a Wildfly-t, hogy létre legyen hozva a QUEUE vagy TOPIC, amelyikre küldeni és fogadni fogunk.

Alternatívaként elindíthatjuk a Wildfly-t a standalone-full.xml konfigurációval ahol már van egy QUEUE létrehozva.

A linkelt megoldás ezt fogja használni.

Wildfly indítása a standalone-full.xml konfigurációval:

```

standalone.sh --server-config=standalone-full.xml

```


<h1>Megoldás</h1>

<h3>Kliens generalasa WSDL-bol</h3>

``` 

wsimport -keep <WSDL URL>

```

<h3>BankWS</h3>

```java

package hu.webvalto.backend.ws;

import hu.webvalto.backend.dao.impl.FelhasznaloDao;
import hu.webvalto.backend.domain.Felhasznalo;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.transaction.Transactional;

@WebService(serviceName = "BankWS")
public class BankWS {

    @Inject
    private FelhasznaloDao felhasznaloDao;

    @Inject
    private Logger logger;

    @Transactional
    @WebMethod
    public boolean felhasznaloLetrehozasa(String nev) {
        try {
            Felhasznalo felhasznalo = new Felhasznalo();
            felhasznalo.setNev(nev);
            felhasznaloDao.mentes(felhasznalo);
            logger.info("Felhasznalo sikeresen letrehozva {}", felhasznalo.getNev());
            return true;
        } catch (Exception e) {
            logger.error("HIBAAAA {}", e);
            return false;
        }
    }
}

```

<h3>BankWSClient.java</h3>

```java
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
```