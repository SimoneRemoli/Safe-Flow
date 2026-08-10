package it.web.safeflow.model;

public class UserProfile {

    private final String codiceFiscale;
    private final String bio;
    private final boolean avatarPresent;
    private final UserProfileStats stats;

    public UserProfile(String codiceFiscale, String bio, boolean avatarPresent) {
        this(codiceFiscale, bio, avatarPresent, new UserProfileStats(0, 0, 0));
    }

    public UserProfile(String codiceFiscale, String bio, boolean avatarPresent, UserProfileStats stats) {
        this.codiceFiscale = codiceFiscale;
        this.bio = bio;
        this.avatarPresent = avatarPresent;
        this.stats = stats == null ? new UserProfileStats(0, 0, 0) : stats;
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

    public UserProfileStats getStats() {
        return stats;
    }
}
