package Model.Domain;

import javax.servlet.http.HttpServletRequest;
import Exception.InvalidRouteInputExceptionRemoli;

public class RouteInputExtractor {

    public static RouteInput from(HttpServletRequest request)
            throws InvalidRouteInputExceptionRemoli {

        String city = sanitize(request.getParameter("city"));
        String start = sanitize(request.getParameter("startStation"));
        String end = sanitize(request.getParameter("endStation"));

        if (city == null || city.isEmpty())
            throw new InvalidRouteInputExceptionRemoli("city", "City parameter is missing");

        if (start == null || start.isEmpty())
            throw new InvalidRouteInputExceptionRemoli("startStation", "Start station is missing");

        if (end == null || end.isEmpty())
            throw new InvalidRouteInputExceptionRemoli("endStation", "End station is missing");

        return new RouteInput(city, start, end);
    }

    private static String sanitize(String s) {
        return (s == null) ? null : s.trim();
    }
}
