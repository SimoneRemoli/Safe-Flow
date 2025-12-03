package it.web.routex.model.domain;

import it.web.routex.utility.Singleton.Credentials;

public class UserStatusResolver {

    public static String resolve(Credentials cred) {
        if (cred.getNome() == null) return "NO REG Traveler";
        return cred.getDisabile() ? "Disabled Traveler" : "Non-disabled Traveler";
    }
}
