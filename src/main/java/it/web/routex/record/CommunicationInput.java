package it.web.routex.record;

import java.sql.Timestamp;

public record CommunicationInput(
        String message,
        Timestamp date,
        String city,
        boolean pickpocketAlert,
        boolean fightAlert,
        boolean crowdAlert,
        boolean generalAlert,
        String stationName,
        String suspectClothing
) {}
