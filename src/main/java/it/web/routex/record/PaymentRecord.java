package it.web.routex.record;

public record PaymentRecord(
        String city,
        int quantity,
        double total,
        String method
) { }
