package it.web.routex.utility.observer;

import it.web.routex.dao.LayerPersistenza;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheInvalidator implements Observer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void update(EventType eventType) {

        if (eventType == EventType.COMUNICAZIONE_CORRETTAMENTE_INVIATA || eventType == EventType.COMUNICAZIONE_CORRETTAMENTE_RISOLTA)
        {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            layer.invalidateNotificationsCache();
            logger.info("Cache notifiche invalidata.");
        }
    }
}
