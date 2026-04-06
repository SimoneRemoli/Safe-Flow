package it.web.routex.domain;

import it.web.routex.utility.singleton.Credentials;

import javax.servlet.http.HttpSession;

public final class SessionAuthUtil {

    private SessionAuthUtil() {
    }

    public static boolean isLoggedIn(HttpSession session) {
        if (session == null) {
            return false;
        }

        Object cfAttribute = session.getAttribute("codiceFiscale");
        if (cfAttribute instanceof String && !((String) cfAttribute).isBlank()) {
            return true;
        }

        Credentials credentials = Credentials.getInstanceSingleton();
        return credentials.getCodiceFiscale() != null && !credentials.getCodiceFiscale().isBlank();
    }
}
