Készítsük el a küldő és fogadó bean-eket, hogy létrehozhassunk felhasználót JMS üzenettel.

<h2>Feladat</h2>
- Küldő JMS bean létrehozása
- Fogadó MDB létrehozása
- Felhasználó létrehozása JMS üzeneten keresztül: “UJSZAMLA|PETYA” üzenet alapján


<h2>Előfeltétel</h2>

Elő kell konfigolni a Wildfly-t, hogy létre legyen hozva a QUEUE vagy TOPIC, amelyikre küldeni és fogadni fogunk.

Alternatívaként elindíthatjuk a Wildfly-t a standalone-full.xml konfigurációval ahol már van egy QUEUE létrehozva.

A linkelt megoldás ezt fogja használni.

Wildfly indítása a standalone-full.xml konfigurációval:

```

standalone.sh --server-config=standalone-full.xml

```


<h1>Megoldás</h1>

<h3>BankJMSKuldo</h3>

```java

package hu.webvalto.backend.jms;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;

@Singleton
@Startup
public class BankJMSKuldo {
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/jms/queue/ExpiryQueue")
    private Queue queue;

    @Inject
    private Logger logger;

    @Resource
    private TimerService timerService;


    @PostConstruct
    public void init() {
        long kesleltetes = 6000;
        TimerConfig config = new TimerConfig();
        config.setPersistent(false);
        timerService.createSingleActionTimer(kesleltetes, config);
    }


    @Timeout
    public void uzenetkuldes() throws JMSException {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer messageProducer = session.createProducer(queue);
        TextMessage message = session.createTextMessage();

        message.setText("UJSZAMLA#PETYA");
        messageProducer.send(message);
        logger.info("PETYA felhasznalo letrehozasa");
    }

}

```

<h3>BankJMSFogado</h3>

```java

package hu.webvalto.backend.jms;

import hu.webvalto.backend.dao.impl.FelhasznaloDao;
import hu.webvalto.backend.domain.Felhasznalo;
import org.slf4j.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = "java:/jms/queue/ExpiryQueue", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
                propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationLookup",
                propertyValue = "java:/jms/queue/ExpiryQueue"),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Queue")
})
public class BankJMSFogado implements MessageListener {

    @Inject
    private FelhasznaloDao felhasznaloDao;

    @Inject
    private Logger logger;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String[] uzenet = ((TextMessage) message).getText().split("#");

                if ("UJSZAMLA".equals(uzenet[0])) {
                    Felhasznalo felhasznalo = new Felhasznalo();
                    felhasznalo.setNev(uzenet[1]);
                    felhasznaloDao.mentes(felhasznalo);
                    logger.info("Petya mentve!");
                } else {
                    logger.error("ROSSZ UZENET: {}", ((TextMessage) message).getText());
                }
            } catch (JMSException e) {
                logger.error("HIBA: ", e);
            }
        }
    }
}

```