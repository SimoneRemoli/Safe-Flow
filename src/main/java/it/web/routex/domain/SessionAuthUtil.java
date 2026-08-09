package it.web.routex.domain;

import it.web.routex.enumerator.Ruolo;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public final class SessionAuthUtil {

    private SessionAuthUtil() {
    }

    public static boolean isLoggedIn(HttpSession session) {
        return codiceFiscale(session).isPresent();
    }

    public static boolean hasRole(HttpSession session, String role) {
        return ruolo(session)
                .map(currentRole -> role.equalsIgnoreCase(currentRole))
                .orElse(false);
    }

    public static Optional<String> codiceFiscale(HttpSession session) {
        return stringAttribute(session, "codiceFiscale");
    }

    public static Optional<String> ruolo(HttpSession session) {
        Optional<String> role = stringAttribute(session, "ruolo");
        if (role.isPresent()) {
            return role;
        }

        Object roleAttribute = session == null ? null : session.getAttribute("ruolo");
        if (roleAttribute instanceof Ruolo) {
            return Optional.of(((Ruolo) roleAttribute).name());
        }

        return Optional.empty();
    }

    public static Optional<String> stringAttribute(HttpSession session, String attributeName) {
        if (session == null) {
            return Optional.empty();
        }

        Object cfAttribute = session.getAttribute(attributeName);
        if (cfAttribute instanceof String && !((String) cfAttribute).isBlank()) {
            return Optional.of(((String) cfAttribute).trim());
        }

        return Optional.empty();
    }
}
