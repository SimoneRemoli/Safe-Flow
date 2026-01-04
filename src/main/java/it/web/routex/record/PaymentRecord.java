package it.web.routex.record;

import it.web.routex.enumerator.TypesOfPersistenceLayer;

public record PaymentRecord(
        String city,
        int quantity,
        double total,
        String method,
        TypesOfPersistenceLayer persistenceLayer
) { }
