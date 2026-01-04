package it.web.routex.boundary.cli.extractor;

import it.web.routex.exception.InvalidRouteInputExceptionRemoli;
import it.web.routex.record.RouteRecord;


public final class RouteInputExtractorCLI
{
    private RouteInputExtractorCLI()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static RouteRecord from(String citta, String startStation, String endStation)
            throws InvalidRouteInputExceptionRemoli {

        String city = sanitize(citta);
        String start = sanitize(startStation);
        String end = sanitize(endStation);

        if (city == null || city.isEmpty())
            throw new InvalidRouteInputExceptionRemoli("city", "City parameter is missing");

        if (start == null || start.isEmpty())
            throw new InvalidRouteInputExceptionRemoli("startStation", "Start station is missing");

        if (end == null || end.isEmpty())
            throw new InvalidRouteInputExceptionRemoli("endStation", "End station is missing");

        return new RouteRecord(city, start, end);
    }

    static String sanitize(String s) {
        return (s == null) ? null : s.trim();
    }
}
