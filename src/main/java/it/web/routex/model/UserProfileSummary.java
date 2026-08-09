package it.web.routex.model;

public class UserProfileSummary {

    private final String codiceFiscale;
    private final String nome;
    private final String cognome;
    private final String role;
    private final String bio;
    private final boolean avatarPresent;

    public UserProfileSummary(String codiceFiscale,
                              String nome,
                              String cognome,
                              String role,
                              String bio,
                              boolean avatarPresent) {
        this.codiceFiscale = codiceFiscale;
        this.nome = nome == null ? "" : nome;
        this.cognome = cognome == null ? "" : cognome;
        this.role = role == null ? "" : role;
        this.bio = bio == null ? "" : bio;
        this.avatarPresent = avatarPresent;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getRole() {
        return role;
    }

    public String getBio() {
        return bio;
    }

    public boolean isAvatarPresent() {
        return avatarPresent;
    }

    public String getDisplayName() {
        String fullName = (nome + " " + cognome).trim();
        return fullName.isBlank() ? "Unknown user" : fullName;
    }

    public String getInitials() {
        StringBuilder initials = new StringBuilder();
        appendInitial(initials, nome);
        appendInitial(initials, cognome);
        return initials.isEmpty() ? "SF" : initials.toString();
    }

    private void appendInitial(StringBuilder initials, String value) {
        if (value != null && !value.isBlank()) {
            initials.append(value.trim().substring(0, 1).toUpperCase());
        }
    }
}
