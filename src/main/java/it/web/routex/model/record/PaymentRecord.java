package it.web.routex.model.record;

import it.web.routex.model.domain.TypesOfPersistenceLayer;

public record PaymentRecord(
        String city,
        int quantity,
        double total,
        String method,
        TypesOfPersistenceLayer persistenceLayer
) { }
