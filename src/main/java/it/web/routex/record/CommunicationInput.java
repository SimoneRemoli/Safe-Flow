package it.web.routex.record;

import java.sql.Timestamp;

public record CommunicationInput(
        String message,
        Timestamp date
) {}