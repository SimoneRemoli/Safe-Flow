package Model.Domain;

import javax.servlet.http.HttpServletRequest;

public class RouteInputExtractor {

    public static RouteInput from(HttpServletRequest request) {
        return new RouteInput(
                request.getParameter("city"),
                request.getParameter("startStation"),
                request.getParameter("endStation")
        );
    }
}
