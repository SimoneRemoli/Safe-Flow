package it.web.routex.record;

public record MastercardRecord(
        String numero_carta,
        String scadenza,
        String cvv
) { }
