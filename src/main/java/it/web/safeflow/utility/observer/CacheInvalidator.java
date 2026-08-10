package it.web.safeflow.utility.observer;

import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheInvalidator implements Observer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void update(EventType eventType) {

        if (eventType == EventType.COMUNICAZIONE_CORRETTAMENTE_INVIATA)
        {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            layer.invalidateNotificationsCache();
            logger.info("Cache notifiche invalidata.");
        }
    }
}
