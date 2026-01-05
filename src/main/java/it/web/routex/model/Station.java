package it.web.routex.model;
/**
 * Modello di dominio che rappresenta una Stazione nel sistema RouteX.
 *
 * La classe incapsula l’identità logica della stazione tramite il suo
 * identificativo univoco, utilizzato nei flussi applicativi di calcolo
 * e persistenza dei percorsi.
 *
 * Il modello espone esplicitamente le regole di validazione del dominio
 * tramite i metodi {@link #isValid()} e {@link #validate()}, separando
 * la creazione dell’oggetto dall’enforcement delle invarianti.
 *
 * In questo modo, i controller applicativi possono delegare al dominio
 * la responsabilità della coerenza dei dati, evitando l’uso di valori
 * sentinella o controlli tecnici distribuiti nel codice.
 * @author Simone Remoli
 */
public class Station {

    private final int id;

    public Station(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isValid() {
        return id > 0;
    }

    public void validate() {
        if (!isValid()) {
            throw new IllegalStateException(
                    "Stazione non valida: id=" + id
            );
        }
    }
}
