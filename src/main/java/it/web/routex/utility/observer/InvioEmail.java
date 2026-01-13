package it.web.routex.utility.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvioEmail implements Observer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void update(EventType eventType) {

        if (eventType == EventType.COMUNICAZIONE_CORRETTAMENTE_INVIATA) {
            logger.info("Email inviata a tutti i lavoratori.");
        }
        if (eventType == EventType.COMUNICAZIONE_CORRETTAMENTE_RISOLTA) {
            logger.info("Comunicazione risolta.");
        }
    }
}
