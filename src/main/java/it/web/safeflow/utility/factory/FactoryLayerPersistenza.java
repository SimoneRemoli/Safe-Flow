package it.web.safeflow.utility.factory;

import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.dao.LayerPersistenzaFull;

public class FactoryLayerPersistenza {

    private static LayerPersistenza instance;

    private FactoryLayerPersistenza() {}

    public static LayerPersistenza createLayerPersistenza() {

        if (instance == null) {
            instance = new LayerPersistenzaFull();
        }
        return instance;
    }
}
