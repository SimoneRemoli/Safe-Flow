package it.web.routex.utility.factory;

import it.web.routex.dao.LayerPersistenza;
import it.web.routex.dao.LayerPersistenzaDemo;
import it.web.routex.dao.LayerPersistenzaFull;
import it.web.routex.enumerator.ApplicationMode;
import it.web.routex.utility.singleton.ApplicationModeManager;

public class FactoryLayerPersistenza {

    private FactoryLayerPersistenza() {
        throw new UnsupportedOperationException("Classe di utility - non istanziabile");
    }

    public static LayerPersistenza createLayerPersistenza() {

        ApplicationMode mode = ApplicationModeManager.getSingletonInstance().getMode();

        switch (mode) {
            case DEMO:
                return new LayerPersistenzaDemo();
            case FULL:
                return new LayerPersistenzaFull();
            default:
                throw new IllegalStateException("Modalità applicativa non supportata");
        }
    }
}
