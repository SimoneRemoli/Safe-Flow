package Model.Record;

import Model.Domain.TypesOfPersistenceLayer;

public record PaymentRecord(
        String city,
        int quantity,
        double total,
        String method,
        TypesOfPersistenceLayer persistenceLayer
) { }
