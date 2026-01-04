package it.web.routex.domain;

import it.web.routex.record.RouteRecord;

public final class RouteValidator {

    private RouteValidator() {
        throw new AssertionError("Classe di utilità - non deve essere istanziata");
    }

    public static boolean isValid(RouteRecord input) {
        return input.city() != null && !input.city().isEmpty() &&
                input.start() != null && !input.start().isEmpty() &&
                input.end() != null && !input.end().isEmpty();
    }
}
