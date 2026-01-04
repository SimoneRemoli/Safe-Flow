package it.web.routex.model;
/**
 * Modello di dominio che rappresenta un Ticket di viaggio acquistato dall’utente.
 *
 * La classe incapsula le informazioni essenziali del biglietto (codice univoco,
 * città associata e data di acquisto) ed espone esplicitamente le regole di
 * validazione del dominio tramite metodi dedicati.
 *
 * La validazione non viene effettuata nel costruttore, ma è demandata ai metodi
 * {@link #isValid()} e {@link #validate()}, in modo da separare la creazione
 * dell’oggetto dall’enforcement delle invarianti di dominio. Questo consente
 * al controller applicativo di gestire in maniera consapevole il flusso di
 * esecuzione e la propagazione degli errori.
 *
 */

import it.web.routex.exception.InvalidTicketExceptionRemoli;

import java.time.LocalDateTime;

public class Ticket {

    private final String codice;
    private final String citta;
    private final LocalDateTime dataAcquisto;

    public Ticket(String codice, String citta, LocalDateTime dataAcquisto) {
        this.codice = codice;
        this.citta = citta;
        this.dataAcquisto = dataAcquisto;
    }

    public LocalDateTime getDataAcquisto() {
        return dataAcquisto;
    }

    public String getCitta() {
        return citta;
    }

    public String getCodice() {
        return codice;
    }
    public boolean isValid() {
        return codice != null && !codice.isBlank()
                && citta != null && !citta.isBlank()
                && dataAcquisto != null;
    }

    public void validate() throws InvalidTicketExceptionRemoli {
        if (!isValid()) {
            throw new InvalidTicketExceptionRemoli(
                    "Ticket non valido",
                    "Invarianti di dominio violate nel Ticket",
                    codice
            );
        }
    }
}


