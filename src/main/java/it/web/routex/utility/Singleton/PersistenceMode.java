package it.web.routex.utility.Singleton;

import it.web.routex.model.domain.TypesOfPersistenceLayer;
/**
 * Singleton che gestisce la modalità di persistenza utilizzata dal sistema.
 * Implementazione thread-safe tramite inner static holder.
 * Autore: Simone Remoli
 */

public class PersistenceMode {

    private TypesOfPersistenceLayer tipo;
    private static class Container
    {
        public final static PersistenceMode instance = new PersistenceMode();
    }
    protected PersistenceMode()
    {
        this.tipo = TypesOfPersistenceLayer.JDBC;
    }
    public static final PersistenceMode getSingletonInstance()
    {
        return Container.instance;
    }
    public TypesOfPersistenceLayer getTipo() {
        return tipo;
    }
    public void setTipo(TypesOfPersistenceLayer tipo) {
        this.tipo = tipo;
    }
}
