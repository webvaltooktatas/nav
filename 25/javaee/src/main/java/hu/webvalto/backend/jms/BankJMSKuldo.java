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
