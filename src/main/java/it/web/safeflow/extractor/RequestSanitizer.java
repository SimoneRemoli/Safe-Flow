package it.web.safeflow.extractor;

public final class RequestSanitizer {

    private RequestSanitizer() {
        throw new AssertionError("Classe utility, non si creano new");
    }

    public static String sanitize(String value) {
        return value == null ? "" : value.trim();
    }
}//ok
