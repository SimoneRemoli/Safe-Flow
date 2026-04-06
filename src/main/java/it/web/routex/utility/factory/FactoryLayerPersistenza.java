package it.web.routex.utility.factory;

import it.web.routex.dao.LayerPersistenza;
import it.web.routex.dao.LayerPersistenzaFull;

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
