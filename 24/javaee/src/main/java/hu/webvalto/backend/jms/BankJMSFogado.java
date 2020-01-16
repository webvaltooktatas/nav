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
