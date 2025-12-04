package it.web.routex.model.domain;

import it.web.routex.utility.singleton.Credentials;

public final class UserStatusResolver {

    private UserStatusResolver()
    {
        throw new AssertionError("Classe di utilità - non deve essere istanziata");
    }

    public static String resolve(Credentials cred) {
        if (cred.getNome() == null) return "NO REG Traveler";
        return cred.getDisabile() ? "Disabled Traveler" : "Non-disabled Traveler";
    }
}
