package it.web.routex.utility.observer;

import it.web.routex.dao.LayerPersistenza;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

public class CacheInvalidator implements Observer {

    @Override
    public void update(EventType eventType) {

        if (eventType == EventType.COMUNICAZIONE_CORRETTAMENTE_INVIATA || eventType == EventType.COMUNICAZIONE_CORRETTAMENTE_RISOLTA)
        {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            layer.invalidateNotificationsCache();

            System.out.println("Cache notifiche invalidata");
        }
    }
}
