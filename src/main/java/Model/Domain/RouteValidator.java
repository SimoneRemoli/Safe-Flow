package Model.Domain;

import Model.Record.RouteRecord;

public class RouteValidator {

    public static boolean isValid(RouteRecord input) {
        return input.city() != null && !input.city().isEmpty() &&
                input.start() != null && !input.start().isEmpty() &&
                input.end() != null && !input.end().isEmpty();
    }
}
