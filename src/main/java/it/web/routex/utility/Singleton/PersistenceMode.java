package it.web.routex.utility.Singleton;

import it.web.routex.model.domain.TypesOfPersistenceLayer;

public class PersistenceMode {

    private static PersistenceMode instance;

    private TypesOfPersistenceLayer tipo;

    private PersistenceMode() {
        // default → JDBC
        this.tipo = TypesOfPersistenceLayer.JDBC;
    }

    public static synchronized PersistenceMode getInstance() {
        if (instance == null) {
            instance = new PersistenceMode();
        }
        return instance;
    }

    public TypesOfPersistenceLayer getTipo() {
        return tipo;
    }

    public void setTipo(TypesOfPersistenceLayer tipo) {
        this.tipo = tipo;
    }

}
