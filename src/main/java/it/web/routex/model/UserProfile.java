package it.web.routex.model;

public class UserProfile {

    private final String codiceFiscale;
    private final String bio;
    private final boolean avatarPresent;

    public UserProfile(String codiceFiscale, String bio, boolean avatarPresent) {
        this.codiceFiscale = codiceFiscale;
        this.bio = bio;
        this.avatarPresent = avatarPresent;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getBio() {
        return bio;
    }

    public boolean isAvatarPresent() {
        return avatarPresent;
    }
}
