package it.web.routex.model;

import java.time.LocalDateTime;

public class Ticket {

    private final String codice;
    private final String citta;
    private final LocalDateTime dataAcquisto;

    public Ticket(String codice, String citta, LocalDateTime dataAcquisto) {
        if (codice == null || codice.isBlank())
            throw new IllegalArgumentException("Codice non valido");
        if (citta == null || citta.isBlank())
            throw new IllegalArgumentException("Città non valida");
        if (dataAcquisto == null)
            throw new IllegalArgumentException("Data mancante");

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
}


//model anemico